package com.eldisprojectexpert.firestorebasic;

import com.google.firebase.firestore.Exclude;

public class NoteModel {
//    private String title =  ""; to avoid null we use empty string
    private String documentId;
    private String title;
    private String description;

    public NoteModel() {
        //empty constructor is needed
    }

    public NoteModel(String title, String description){
        this.title = title;
        this.description = description;
    }

    @Exclude //to revent redundant field in Document Firestore
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
