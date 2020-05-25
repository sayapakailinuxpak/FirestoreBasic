package com.eldisprojectexpert.firestorebasic;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity{
    EditText editTextTitle, editTextDesc;
    Button buttonSave;
    TextView textViewData;
    ConstraintLayout constraintLayout;
    private static final String TAG = "MainActivity";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    ListenerRegistration listenerRegistration;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firebaseFirestore.collection("NotesCollection");
    DocumentReference documentReference = firebaseFirestore.document("NotesCollection/MyNoteDocument");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.constraint);
        editTextTitle = findViewById(R.id.editttext_title);
        editTextDesc = findViewById(R.id.editttext_description);
        buttonSave = findViewById(R.id.button_save);
        textViewData = findViewById(R.id.textview_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        documentReference.addSnapshotListener(this,new EventListener<DocumentSnapshot>() { //automatically follow activity lifecycle and we pass this activity that will detach snapShot listener when we close app
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                if (e != null){
//                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (documentSnapshot.exists()){
////                    String title = documentSnapshot.getString(KEY_TITLE);
////                    String desc = documentSnapshot.getString(KEY_DESCRIPTION);
//                    NoteModel noteModel = documentSnapshot.toObject(NoteModel.class);
//                    String title = noteModel.getTitle();
//                    String desc = noteModel.getDescription();
//
//                    textViewData.setText("Title : " + title + "\n" + "Desc : " + desc);
//                } else {
//                    textViewData.setText("");
//                }
//            }
//        });
        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    return;
                }

                String data = "";

                for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots){
                    NoteModel noteModel = querySnapshot.toObject(NoteModel.class);
                    noteModel.setDocumentId(querySnapshot.getId());
                    String documentId = noteModel.getDocumentId();
                    String title = noteModel.getTitle();
                    String desc = noteModel.getDescription();
                    data+="id : " + documentId + "\n" + "Title : " + title + "\n" + "Desc : " + desc + "\n\n";
                }
                textViewData.setText(data);
            }
        });

    }



    public void addNote(View view){
        String title = editTextTitle.getText().toString().trim();
        String desc = editTextDesc.getText().toString().trim();

//        Map<String, Object> map = new HashMap<>();
//        map.put(KEY_TITLE, title);
//        map.put(KEY_DESCRIPTION, desc);

        NoteModel noteModel = new NoteModel(title, desc);

//        firebaseFirestore.document("NoteCollection/MyNoteDocument");
//        firebaseFirestore.collection("NotesCollection").document("MyNoteDocument").set(noteModel)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(MainActivity.this, "Note Saved!", Toast.LENGTH_SHORT).show();
//                        editTextTitle.setText("");
//                        editTextDesc.setText("");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "onFailure: " + e.toString());
//                    }
//                });
        collectionReference.add(noteModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(MainActivity.this, "Note Saved!", Toast.LENGTH_SHORT).show();
                editTextTitle.setText("");
                editTextDesc.setText("");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void loadNotes(View view){
//        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()){
//                    String title = documentSnapshot.getString(KEY_TITLE);
//                    String desc = documentSnapshot.getString(KEY_DESCRIPTION);
//                    textViewData.setText("Title : " + title + "\n" + "Desc : " + desc);
//                }else {
//                    Snackbar.make(constraintLayout, "Document is not exists", BaseTransientBottomBar.LENGTH_SHORT).show();
//                }
//            }
//        })
//        .addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onFailure: "+ e.toString());
//            }
//        });

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            String data = "";
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot){
                    NoteModel noteModel = queryDocumentSnapshot.toObject(NoteModel.class);
                    noteModel.setDocumentId(queryDocumentSnapshot.getId());

                    String id = noteModel.getDocumentId();
                    String title = noteModel.getTitle();
                    String desc = noteModel.getDescription();
                    data+="id : " + id + "\n" + "Title : " + title + "\n" + "Desc : " + desc + "\n\n";

//                    collectionReference.document(id); use this to get reference to a particular document
                }
                textViewData.setText(data);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

//    public void updateNoteDescription(View view){
//        String description = editTextDesc.getText().toString().trim();
////        Map<String, Object> map = new HashMap<>();
////        map.put(KEY_DESCRIPTION, description);
////        documentReference.set(map, SetOptions.merge());
//        documentReference.update(KEY_DESCRIPTION, description);
//    }
//
//    public void deleteDescription(View view){
////        Map<String, Object> map = new HashMap<>();
////        map.put(KEY_DESCRIPTION, FieldValue.delete());
////
////        documentReference.update(map);
//        documentReference.update(KEY_DESCRIPTION, FieldValue.delete());
//    }
//
//    public void deleteNote(View view){
//        documentReference.delete();
//    }



}
