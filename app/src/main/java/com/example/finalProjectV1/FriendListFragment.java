package com.example.finalProjectV1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FriendListFragment extends Fragment {
    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;
    private List<User> friendList;
    private DatabaseReference databaseReference;
    private FloatingActionButton addFriendButton;

    public FriendListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_lists, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        friendList = new ArrayList<>();
        friendAdapter = new FriendAdapter(friendList);
        recyclerView.setAdapter(friendAdapter);
        addFriendButton = view.findViewById(R.id.addFriendButton);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Initialize Firebase
        databaseReference = FirebaseManager.getInstance().getDatabase().getReference("users").child(userId).child("friends");

        loadFriends();
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( view.getContext(),AddFriendActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void loadFriends() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User User = snapshot.getValue(User.class);
                    if (User != null) {
                        User.setUserId(snapshot.getKey());
                        friendList.add(User);
                    }
                }
                friendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize your views and set up any necessary logic here
    }
}