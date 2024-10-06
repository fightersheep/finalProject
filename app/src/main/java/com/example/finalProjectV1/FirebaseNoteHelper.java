package com.example.finalProjectV1;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class FirebaseNoteHelper {

    private DatabaseReference notesRef;
    private FirebaseNoteListener listener;

    public interface FirebaseNoteListener {
        void onNotesLoaded(List<Note> notes);
        void onNoteAdded();
        void onNoteUpdated();
        void onNoteDeleted();
        void onError(String error);
    }

    public interface NoteCallback {
        void onSuccess(Note note);
        void onError(String errorMessage);
    }

    public FirebaseNoteHelper(FirebaseNoteListener listener) {
        this.listener = listener;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        notesRef = FirebaseManager.getInstance().getDatabase().getReference("users").child(userId).child("notes");
    }

    public void loadNotes() {
        notesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Note> noteList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Note note = snapshot.getValue(Note.class);
                    noteList.add(note);
                }
                listener.onNotesLoaded(noteList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public void addNote(Note note) {
        String noteId = notesRef.push().getKey();
        note.setId(noteId);
        notesRef.child(noteId).setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onNoteAdded();
                } else {
                    listener.onError("Failed to add note");
                }
            }
        });
    }

    public void updateNote(Note note) {
        notesRef.child(note.getId()).setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onNoteUpdated();
                } else {
                    listener.onError("Failed to update note");
                }
            }
        });
    }

    public void deleteNote(String noteId) {
        notesRef.child(noteId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onNoteDeleted();
                } else {
                    listener.onError("Failed to delete note");
                }
            }
        });
    }

    public void getNoteById(String noteId, final NoteCallback callback) {
        notesRef.child(noteId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Note note = dataSnapshot.getValue(Note.class);
                if (note != null) {
                    callback.onSuccess(note);
                } else {
                    callback.onError("Note not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }
}
