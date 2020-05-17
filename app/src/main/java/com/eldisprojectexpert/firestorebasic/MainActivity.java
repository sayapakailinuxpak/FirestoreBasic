package com.eldisprojectexpert.firestorebasic;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText editTextTitle, editTextDesc;
    Button buttonSave;
    private static final String TAG = "MainActivity";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextTitle = findViewById(R.id.editttext_title);
        editTextDesc = findViewById(R.id.editttext_description);
        buttonSave = findViewById(R.id.button_save);

    }

    public void saveNote(View view){
        String title = editTextTitle.getText().toString().trim();
        String desc = editTextDesc.getText().toString().trim();

        Map<String, Object> map = new HashMap<>();
        map.put(KEY_TITLE, title);
        map.put(KEY_DESCRIPTION, desc);

//        firebaseFirestore.document("NoteCollection/MyNoteDocument");
        firebaseFirestore.collection("NotesCollection").document("MyNoteDocument").set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note Saved!", Toast.LENGTH_SHORT).show();
                        editTextTitle.setText("");
                        editTextDesc.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
    }
}
