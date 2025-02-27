package com.example.finalProjectV1.notification;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.finalProjectV1.Activities.FriendRequestDetails;
import com.example.finalProjectV1.firebase.FirebaseManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.finalProjectV1.firebase.dataManeger;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestNotification {
    private DatabaseReference databaseRef;
    private ValueEventListener valueEventListener;
    private Context context;

    public FriendRequestNotification(Context context) {
        this.context = context;
        databaseRef = FirebaseManager.getInstance().getDatabase().getReference().child("users").child(dataManeger.getUser().getUserId()).child("pendingInvite");
        setupListener();
    }

    private void setupListener() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<String> stringList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String value = dataSnapshot.getValue(String.class);
                        if (value != null) {
                            stringList.add(value);
                            Intent go =new Intent(context, FriendRequestDetails.class);
                            go.putExtra("FriendID",value);
                            NotificationHelper.scheduleNotification(context,50,"Friend request",value,go);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FirebaseListener", "Error: " + error.getMessage());
            }
        };

        databaseRef.addValueEventListener(valueEventListener);
    }

    public void removeListener() {
        if (valueEventListener != null) {
            databaseRef.removeEventListener(valueEventListener);
        }
    }
}

