package com.example.finalProjectV1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalProjectV1.Activities.AddFriendActivity;
import com.example.finalProjectV1.Activities.FriendDetailActivity;
import com.example.finalProjectV1.adapters.FriendAdapter;
import com.example.finalProjectV1.R;
import com.example.finalProjectV1.classes.ShortUser;
import com.example.finalProjectV1.firebase.FirebaseManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class FriendListFragment extends Fragment{
    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;
    private List<ShortUser> ShortUserList;
    private DatabaseReference databaseReference;
    private FloatingActionButton addFriendButton;
    private List<String> friendIds;
    private String userId;
    private ChildEventListener childEventListener;

    public FriendListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_lists, container, false);
        recyclerView = view.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        friendIds= new ArrayList<>();
        ShortUserList = new ArrayList<>();
        friendAdapter = new FriendAdapter(ShortUserList, (key, value) -> {
            // Create intent to start DetailActivity
            Intent intent = new Intent(this.getContext(), FriendDetailActivity.class);
            intent.putExtra("ITEM_KEY", key);
            startActivity(intent);
        });
        recyclerView.setAdapter(friendAdapter);
        addFriendButton = view.findViewById(R.id.addFriendButton);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Initialize Firebase
        databaseReference = FirebaseManager.getInstance().getDatabase().getReference("users").child(userId).child("friends");
        attachDatabaseListener();

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( view.getContext(), AddFriendActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    

    private void attachDatabaseListener() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@androidx.annotation.NonNull DataSnapshot snapshot, String previousChildName) {
                ShortUser model = new ShortUser(
                        snapshot.getKey(),
                        snapshot.getValue(String.class)
                );
                ShortUserList.add(model);
                friendAdapter.notifyItemInserted(ShortUserList.size() - 1);
            }

            @Override
            public void onChildChanged(@androidx.annotation.NonNull DataSnapshot snapshot, String previousChildName) {
                String key = snapshot.getKey();
                String newValue = snapshot.getValue(String.class);
                for (int i = 0; i < ShortUserList.size(); i++) {
                    if (ShortUserList.get(i).getId().equals(key)) {
                        ShortUserList.get(i).setName(newValue);
                        friendAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@androidx.annotation.NonNull DataSnapshot snapshot) {
                String key = snapshot.getKey();
                for (int i = 0; i < ShortUserList.size(); i++) {
                    if (ShortUserList.get(i).getId().equals(key)) {
                        ShortUserList.remove(i);
                        friendAdapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@androidx.annotation.NonNull DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
            }
        };

        databaseReference.addChildEventListener(childEventListener);
    }
     public void onDestroy() {
         super.onDestroy();
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
        }
    }
}