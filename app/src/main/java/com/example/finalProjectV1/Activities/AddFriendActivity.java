package com.example.finalProjectV1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.adapters.FriendAdapter;
import com.example.finalProjectV1.classes.FullUser;
import com.example.finalProjectV1.classes.ShortUser;
import com.example.finalProjectV1.firebase.FirebaseManager;
import com.example.finalProjectV1.firebase.interfaces.OnSearchResultListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {
    private EditText searchEditText;
    private RecyclerView searchResults;
    private FriendAdapter friendAdapter;
    private List<ShortUser> ShortUserList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShortUserList = new ArrayList<>();

        setContentView(R.layout.activity_add_friend);
        friendAdapter = new FriendAdapter(ShortUserList, (key, value) -> {
            // Create intent to start DetailActivity
            Intent intent = new Intent(this,FriendDetailActivity.class);
            intent.putExtra("userId", key);
            startActivity(intent);
        });
        searchEditText = findViewById(R.id.searchEditText);
        searchResults = findViewById(R.id.searchResults);
        searchResults.setLayoutManager(new LinearLayoutManager(this));
        searchResults.setAdapter(friendAdapter);


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                ShortUserList.clear();
                friendAdapter.notifyDataSetChanged();
                String username = s.toString().trim();
                if (username.length() >= 3) {
                    handleSearchInput(username);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    private void handleSearchInput(String input) {
        // Split input into username and discriminator if it contains '#'
        String username;
        String discriminator = "";

        if (input.contains("#")) {
            String[] parts = input.split("#");
            username = parts[0].trim();
            if (parts.length > 1) {
                discriminator = parts[1].trim();
                searchUserWithDiscriminator(username, discriminator);
            }
        } else {
            username = input.trim();
            performSearch(username);
        }

        // Call the search method

    }

    private void searchUserWithDiscriminator(String username, String discriminator) {
        FirebaseManager.searchUserWithDiscriminator(username, discriminator, new OnSearchResultListener() {
            @Override
            public void onSearchComplete(List<FullUser> fullUsers) {
                updateSearchResults(fullUsers);

            }

            @Override
            public void onSearchError(String error) {
                Toast.makeText(AddFriendActivity.this, "Search failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSearch(String username) {
        FirebaseManager.searchUsers(username, new OnSearchResultListener() {
            @Override
            public void onSearchComplete(List<FullUser> fullUsers) {
                // Update the RecyclerView adapter with the results
                updateSearchResults(fullUsers);

            }

            @Override
            public void onSearchError(String error) {
                Toast.makeText(AddFriendActivity.this, "Search failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSearchResults(List<FullUser> shortUser) {
        ShortUserList.addAll(shortUser);
        friendAdapter.notifyDataSetChanged();
    }

}




