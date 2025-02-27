package com.example.finalProjectV1.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.classes.FullUser;
import com.example.finalProjectV1.firebase.FirebaseManager;
import com.example.finalProjectV1.firebase.dataManeger;
import com.example.finalProjectV1.util.ImageHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PAGE_NUMBER = "page_number";
    private TextView tvName, tvEmail;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ActivityResultLauncher<Void> mGetPic;// help to get picture.
    private ImageButton profile_pic;//image button for profile picture.

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseManager.getInstance().getDatabase().getReference();
        profile_pic = view.findViewById(R.id.profile_pic);
        profile_pic.setOnClickListener(this);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);

        mGetPic = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap result) {//getting the result of the picture.
                if (result != null){
                    profile_pic.setImageBitmap(result);
                    saveSP();
                }
            }
        });
        loadUserData();
        return view;
    }

    public void saveSP(){
        Bitmap bm=((BitmapDrawable)profile_pic.getDrawable()).getBitmap();


        mDatabase.child("users").child(dataManeger.getUser().getUserId()).child("profileImage").setValue(ImageHandler.bitmapToString(bm));
        // Creating an Editor object to edit(write to the file)

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == profile_pic.getId()){
            mGetPic.launch(null);
        }
    }

    private void loadUserData() {
        FullUser fullUser = dataManeger.getUser();

        if (fullUser != null) {
                String firstName = fullUser.getFirstName();
                String lastName = fullUser.getLastName();
                String email = fullUser.getEmail();
                String picture = fullUser.getProfileImage();

                tvName.setText(String.format("Name: %s %s", firstName, lastName));
                tvEmail.setText(String.format("Name: %s ", email));
                if (picture != null){
                    Bitmap bitpic = ImageHandler.stringToBitmap(picture);
                    profile_pic.setImageBitmap(bitpic);
                }

        }
    }
}


