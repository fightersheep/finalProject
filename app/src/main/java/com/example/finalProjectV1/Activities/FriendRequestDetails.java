package com.example.finalProjectV1.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.classes.FullUser;
import com.example.finalProjectV1.firebase.FirebaseManager;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;
import com.example.finalProjectV1.firebase.interfaces.OnUserResultListener;
import com.example.finalProjectV1.firebase.dataManeger;

import com.example.finalProjectV1.util.CustomAlertDialog;
import com.example.finalProjectV1.util.ImageHandler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

public class FriendRequestDetails extends AppCompatActivity implements OnUserResultListener , OnAddToFirebase {
    private TextView tvFullName, tvEmail, tvLocation, tvDateOfBirth;
    private TextView tvExperience, tvGender;
    private ImageView ivprofile_pic;
    private Button btnReject, btnAccept;
    private String FriendID, userId, FriendName;
    private CustomAlertDialog dialog;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request_details);
        dialog = new CustomAlertDialog(this);
        // Initialize views
        initializeViews();
         userId = dataManeger.getUser().getUserId();
         FriendID = getIntent().getStringExtra("FriendID");
        // Get data from intent
        FirebaseManager.searchUserById(FriendID,this);
        databaseReference = FirebaseManager.getInstance().getDatabase().getReference();

        // Set click listeners
        btnReject.setOnClickListener(v -> handleReject());
        btnAccept.setOnClickListener(v -> handleAccept());
    }

    private void initializeViews() {
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvLocation = findViewById(R.id.tvLocation);
        tvDateOfBirth = findViewById(R.id.tvDateOfBirth);
        tvExperience = findViewById(R.id.tvExperience);
        tvGender = findViewById(R.id.tvGender);
        btnReject = findViewById(R.id.btnReject);
        btnAccept = findViewById(R.id.btnAccept);
        ivprofile_pic = findViewById(R.id.profile_pic);

    }

    private void displayUserInfo(FullUser fullUser) {
        if (fullUser != null) {
            String firstName = fullUser.getFirstName();
            String lastName = fullUser.getLastName();
            String email = fullUser.getEmail();
            String picture = fullUser.getProfileImage();
            String discriminator = fullUser.getDiscriminator();
            String location = fullUser.getLocation();
            String country = fullUser.getCountry();
            String experience = fullUser.getExperience();
            String dateofbirth = fullUser.getDateOfBirth();
            String gender = fullUser.getGender();
            FriendName= firstName + " "+lastName;

                    tvFullName.setText(String.format("Name: %s %s # %s", firstName, lastName,discriminator));
            tvEmail.setText(String.format("email: %s ", email));
            tvDateOfBirth.setText(String.format("Date of birth: %s",dateofbirth));
            tvLocation.setText(String.format("Location: %s ,%s",location,country));
            tvExperience.setText(String.format("experience: %s",experience));
            tvGender.setText(String.format("gender: %s",gender));

            if (picture != null){
                Bitmap bitpic = ImageHandler.stringToBitmap(picture);
                ivprofile_pic.setImageBitmap(bitpic);
            }

        }
    }

    private void handleReject() {
        // Add your reject friend request logic here
        Toast.makeText(this, "Friend request rejected", Toast.LENGTH_SHORT).show();
        databaseReference.child("users").child(userId).child("pendingInvite").child(FriendID).removeValue();
        finish();
    }

    private void handleAccept() {

        // Add your accept friend request logic here
        databaseReference.child("users").child(FriendID).child("friends").child(userId).setValue(dataManeger.getUser().getName()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                databaseReference.child("users").child(userId).child("friends").child(FriendID).setValue(FriendName).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        databaseReference.child("users").child(userId).child("pendingInvite").child(FriendID).removeValue();
                        finish();
                    }
                });

            }
        });

    }

    @Override
    public FullUser onUserFound(FullUser fullUser) {
        displayUserInfo(fullUser);
        return null;
    }

    @Override
    public void onUserNotFound() {
        dialog.showTextOnlyDialog("user not found ","user not found please try again");

    }

    @Override
    public void OnComplete() {

    }

    @Override
    public void onSearchError(String error) {

    }
}