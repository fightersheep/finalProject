package com.example.finalProjectV1.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalProjectV1.util.CustomAlertDialog;
import com.example.finalProjectV1.R;
import com.example.finalProjectV1.classes.FullUser;
import com.example.finalProjectV1.firebase.FirebaseManager;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class FriendDetailActivity extends AppCompatActivity implements View.OnClickListener , OnAddToFirebase {
    private TextView nameTextView;
    private TextView emailTextView;
    private Button AddFriendButton,GoToChatButton;
    private DatabaseReference databaseReference;
    private String FriendId,UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        AddFriendButton = findViewById(R.id.addFriendButton);
        GoToChatButton = findViewById(R.id.goToChat);
        GoToChatButton.setOnClickListener(this);

        UserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FriendId = getIntent().getStringExtra("userId");
        databaseReference = FirebaseManager.getInstance().getDatabase().getReference("users");
        AddFriendButton.setOnClickListener(this);
        loadFriendDetails();
    }

    private void loadFriendDetails() {
        databaseReference.child(FriendId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FullUser friend = dataSnapshot.getValue(FullUser.class);
                if (friend != null) {
                    nameTextView.setText(friend.getName());
                    emailTextView.setText(friend.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == AddFriendButton.getId()){
            FirebaseManager.AddGeneric(databaseReference.child(FriendId).child("pendingInvite").child(UserId),UserId,this);
        } else if (v.getId() == GoToChatButton.getId()) {
            Intent intent = new Intent(this,TournamentActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void OnComplete() {
        finish();
    }

    @Override
    public void onSearchError(String error) {
    CustomAlertDialog dialog = new CustomAlertDialog(this);
    dialog.showTextOnlyDialog("error",error);
    }
}