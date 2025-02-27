package com.example.finalProjectV1.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalProjectV1.firebase.dataManeger;
import com.example.finalProjectV1.util.CustomAlertDialog;
import com.example.finalProjectV1.R;
import com.example.finalProjectV1.classes.FullUser;
import com.example.finalProjectV1.firebase.FirebaseManager;
import com.example.finalProjectV1.firebase.interfaces.OnAddToFirebase;
import com.example.finalProjectV1.fragments.mainPageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements OnAddToFirebase {

    private EditText etFirstName, etLastName, etLocation, etDateOfBirth;
    private Spinner spinnerExperience, spinnerGender, spinnerCountry;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseManager.getInstance().getDatabase().getReference();

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etLocation = findViewById(R.id.etLocation);
        etDateOfBirth = findViewById(R.id.etDateOfBirth);
        spinnerExperience = findViewById(R.id.spinnerExperience);
        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        btnRegister = findViewById(R.id.btnRegister);

        setupSpinners();
        setupDatePicker();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void setupSpinners() {
        // Experience Spinner
        ArrayAdapter<CharSequence> experienceAdapter = ArrayAdapter.createFromResource(this,
                R.array.experience_levels, android.R.layout.simple_spinner_item);
        experienceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExperience.setAdapter(experienceAdapter);

        // Gender Spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.genders, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // Country Spinner
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this,
                R.array.countries, android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);
    }

    private void setupDatePicker() {
        etDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etDateOfBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void registerUser() {

        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String dateOfBirth = etDateOfBirth.getText().toString().trim();
        String experience = spinnerExperience.getSelectedItem().toString();
        String gender = spinnerGender.getSelectedItem().toString();
        String country = spinnerCountry.getSelectedItem().toString();

        if (firstName.isEmpty() || lastName.isEmpty() || location.isEmpty() || dateOfBirth.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        String email = user.getEmail();

        if (user != null) {
            FullUser adding = new FullUser(userId,email,firstName,lastName,location,dateOfBirth,experience,gender,country, null);
            dataManeger.setUser(adding);
            FirebaseManager.AddGeneric( mDatabase.child("users").child(userId),adding,this );

        }

    }

    @Override
    public void OnComplete() {
        Intent homeIntent = new Intent(this, mainPageActivity.class);
        startActivity(homeIntent);
        finish();
    }

    @Override
    public void onSearchError(String error) {
        CustomAlertDialog alert = new CustomAlertDialog(this);
        alert.showTextOnlyDialog("error",error);

    }
}