package com.example.final_project.repository;

import android.net.Uri;

import com.example.final_project.data.SharedPreferencesDataSource;
import com.example.final_project.models.Resume;
import com.example.final_project.models.User;
import com.example.final_project.utils.ProfileFireStoreResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProfileRepository {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private static ProfileRepository instance;

    private ProfileRepository() {
    }

    public static ProfileRepository getInstance() {
        if (instance == null) {
            instance = new ProfileRepository();
        }
        return instance;
    }

    public User getCurrentUser() {
        return new Gson().fromJson(SharedPreferencesDataSource.getInstance().getString("user"), User.class);
    }

    public void fetchResumes(ProfileFireStoreResult result) {
        User user = getCurrentUser();
        db.collection("resumes").whereEqualTo("userId", user.getId()).get().addOnSuccessListener(task -> {
            if (!task.isEmpty()) {
                List<Resume> resumes = new ArrayList<>();
                for (int i = 0; i < task.size(); i++) {
                    Resume resume = task.getDocuments().get(i).toObject(Resume.class);
                    if (resume != null) {
                        resume.setId(task.getDocuments().get(i).getId());
                        resumes.add(resume);
                    }
                }
                result.onFetchResumesResult(true, resumes);
            } else {
                result.onFetchResumesResult(false, null);
            }
        }).addOnFailureListener(e -> {
            result.onFetchResumesResult(false, null);
        });
    }

    public void uploadResume(Uri pdfResume, ProfileFireStoreResult result){
        StorageReference ref = storage.getReference().child("resumes/" + pdfResume.getLastPathSegment());
        ref.putFile(pdfResume).addOnSuccessListener(task -> {
            ref.getDownloadUrl().addOnSuccessListener(uri -> {
                User user = getCurrentUser();
                Resume resume = new Resume( pdfResume.getLastPathSegment(),user.getId(), null,uri.toString());
                db.collection("resumes").add(resume).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        result.onUploadResumeResult(true, resume);
                    } else {
                        result.onUploadResumeResult(false, null);
                    }
                }).addOnFailureListener(e -> {
                    result.onUploadResumeResult(false, null);
                });
            }).addOnFailureListener(e -> {
                result.onUploadResumeResult(false, null);
            });
        }).addOnFailureListener(e -> {
            result.onUploadResumeResult(false, null);
        });
    }

    public void updateProfile(User user, ProfileFireStoreResult result){
        db.collection("users").document(user.getId()).set(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                result.onUpdateProfileResult(true, user);
            } else {
                result.onUpdateProfileResult(false, null);
            }
        }).addOnFailureListener(e -> {
            result.onUpdateProfileResult(false, null);
        });
    }
}
