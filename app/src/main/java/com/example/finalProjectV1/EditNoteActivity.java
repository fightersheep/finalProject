package com.example.finalProjectV1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class EditNoteActivity extends AppCompatActivity implements FirebaseNoteHelper.FirebaseNoteListener {

    private EditText titleEditText, contentEditText;
    private FirebaseNoteHelper firebaseNoteHelper;
    private String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        Button updateButton = findViewById(R.id.updateButton);

        noteId = getIntent().getStringExtra("noteId");
        firebaseNoteHelper = new FirebaseNoteHelper(this);

        loadNoteData();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
            }
        });
    }

    private void loadNoteData() {
        firebaseNoteHelper.getNoteById(noteId, new FirebaseNoteHelper.NoteCallback() {
            @Override
            public void onSuccess(Note note) {
                titleEditText.setText(note.getTitle());
                contentEditText.setText(note.getContent());
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(EditNoteActivity.this, "Failed to load note: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateNote() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please enter both title and content", Toast.LENGTH_SHORT).show();
            return;
        }

        Note updatedNote = new Note(noteId, title, content);
        firebaseNoteHelper.updateNote(updatedNote);
    }

    @Override
    public void onNotesLoaded(List<Note> notes) {
        // This method is not used in this activity
    }

    @Override
    public void onNoteAdded() {
        // This method is not used in this activity
    }

    @Override
    public void onNoteUpdated() {
        Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
        finish();
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
