package com.example.finalProjectV1.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finalProjectV1.Activities.AddNoteActivity;
import com.example.finalProjectV1.util.CustomAlertDialog;
import com.example.finalProjectV1.classes.Note;
import com.example.finalProjectV1.adapters.NoteAdapter;
import com.example.finalProjectV1.R;
import com.example.finalProjectV1.firebase.FirebaseNoteHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;


public class NotesFragment extends Fragment implements FirebaseNoteHelper.FirebaseNoteListener {

    private CustomAlertDialog customAlertDialog;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private FirebaseNoteHelper firebaseNoteHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        customAlertDialog = new CustomAlertDialog(this.getContext());
        notesInit();
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddNoteActivity.class));
            }
        });
        return view;

    }
    public void notesInit(){
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), numberOfColumns));

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList, this.getContext());
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
        Toast.makeText(this.getContext(), error, Toast.LENGTH_SHORT).show();
    }
}
