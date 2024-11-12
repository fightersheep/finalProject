package com.example.finalProjectV1;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class FriendDetailActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView emailTextView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);

        String userId = getIntent().getStringExtra("userId");
        databaseReference = FirebaseManager.getInstance().getDatabase().getReference("users").child(userId);

        loadFriendDetails();
    }

    private void loadFriendDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User friend = dataSnapshot.getValue(User.class);
                if (friend != null) {
                    nameTextView.setText(friend.getName());
                    //emailTextView.setText(friend.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}