package com.example.finalProjectV1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {
        private EditText searchEditText;
        private RecyclerView searchResults;
    private FriendAdapter friendAdapter;
    private List<User> UserList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            UserList = new ArrayList<>();

            setContentView(R.layout.activity_add_friend);
            friendAdapter = new FriendAdapter(UserList);
            searchEditText = findViewById(R.id.searchEditText);
            searchResults = findViewById(R.id.searchResults);
            searchResults.setLayoutManager(new LinearLayoutManager(this));
            searchResults.setAdapter(friendAdapter);


            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    UserList.clear();
                    friendAdapter.notifyDataSetChanged();
                        String query = s.toString().trim();
                        if (query.length() >= 3) {
                            handleSearchInput(query);
                        }


                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
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
            }
        } else {
            username = input.toString().trim();
            performSearch(username);
        }

        // Call the search method
        FirebaseManager.searchUserWithDiscriminator(username, discriminator, new OnSearchResultListener() {
            @Override
            public void onSearchComplete(List<User> users) {
                updateSearchResults(users);
            }

            @Override
            public void onSearchError(String error) {
                Toast.makeText(AddFriendActivity.this, "Search failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void performSearch(String query) {
            FirebaseManager.searchUsers(query, new OnSearchResultListener() {
                @Override
                public void onSearchComplete(List<User> users) {
                    // Update your RecyclerView adapter with the results
                    updateSearchResults(users);
                }

                @Override
                public void onSearchError(String error) {
                    Toast.makeText(AddFriendActivity.this, "Search failed: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void updateSearchResults(List<User> users) {
            UserList.addAll(users);
            friendAdapter.notifyDataSetChanged();
        }
    }

