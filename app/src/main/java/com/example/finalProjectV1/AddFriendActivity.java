package com.example.finalProjectV1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFriendActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText emailEditText;
    private Button saveButton;
    private DatabaseReference databaseReference;
    private CustomAlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        saveButton = findViewById(R.id.saveButton);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseManager.getInstance().getDatabase().getReference("users").child(userId).child("friends");
        dialog = new CustomAlertDialog(this);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFriend();
            }
        });
    }

    private void saveFriend() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseManager.getUserIdFromEmail(email, new FirebaseManager.OnUserIdFetchedListener() {

            public void onUserIdFetched(String userId) {
                Friend newFriend = new Friend(name, email);

                if (userId != null) {
                    databaseReference.child(userId).setValue(newFriend)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(AddFriendActivity.this, "Friend added successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(AddFriendActivity.this, "Failed to add friend", Toast.LENGTH_SHORT).show());
                }
                // Do something with the user ID
            }

            public void onError(String errorMessage) {
                dialog.showTextOnlyDialog("erorr",errorMessage);
                // Handle the error
            }
        });



    }
}
