package com.example.finalProjectV1.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalProjectV1.R;
import com.example.finalProjectV1.classes.Note;
import com.example.finalProjectV1.firebase.FirebaseNoteHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AddNoteActivity extends AppCompatActivity implements FirebaseNoteHelper.FirebaseNoteListener {

    private EditText titleEditText, contentEditText;
    private FirebaseNoteHelper firebaseNoteHelper;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        Button saveButton = findViewById(R.id.saveButton);

        firebaseNoteHelper = new FirebaseNoteHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference myRef = database.getReference("message");
                myRef.setValue("Hello, World!");
                saveNote();
            }
        });
    }

    private void saveNote() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please enter both title and content", Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = new Note(null, title, content);
        firebaseNoteHelper.addNote(note);
    }

    @Override
    public void onNotesLoaded(List<Note> notes) {
        // This method is not used in this activity
    }

    @Override
    public void onNoteAdded() {
        Toast.makeText(this, "Note added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onNoteUpdated() {
        // This method is not used in this activity
    }

    @Override
    public void onNoteDeleted() {
        // This method is not used in this activity
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
