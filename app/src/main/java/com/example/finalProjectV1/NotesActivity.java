package com.example.finalProjectV1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class NotesActivity extends AppCompatActivity implements FirebaseNoteHelper.FirebaseNoteListener {
    private CustomAlertDialog customAlertDialog;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private FirebaseNoteHelper firebaseNoteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        customAlertDialog = new CustomAlertDialog(this);
        recyclerView = findViewById(R.id.recyclerView);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(noteAdapter);

        firebaseNoteHelper = new FirebaseNoteHelper(this);
        firebaseNoteHelper.loadNotes();
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @Nonnull RecyclerView.ViewHolder viewHolder, @Nonnull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@Nonnull RecyclerView.ViewHolder viewHolder, int direction) {
                // Handle swipe to delete
                customAlertDialog.showYesNoDialog("Confirmation", "Do you want to proceed?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Note Note = noteList.get(viewHolder.getAdapterPosition());
                        firebaseNoteHelper.deleteNote(Note.getId());
                    }
                });
                noteAdapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotesActivity.this, AddNoteActivity.class));
            }
        });
    }

    @Override
    public void onNotesLoaded(List<Note> notes) {
        noteList.clear();
        noteList.addAll(notes);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteAdded() {
        // This method is not used in this activity
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
