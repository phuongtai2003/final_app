package com.example.final_project.repository;

import com.example.final_project.data.SharedPreferencesDataSource;
import com.example.final_project.models.Company;
import com.example.final_project.models.Job;
import com.example.final_project.models.JobCategory;
import com.example.final_project.utils.JobFireStoreResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class JobRepository {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static JobRepository instance;

    private JobRepository() {
    }

    public static JobRepository getInstance() {
        if (instance == null) {
            instance = new JobRepository();
        }
        return instance;
    }

    public void addJob(Job job, JobFireStoreResult results) {
        db.collection("jobs").add(job).addOnSuccessListener(documentReference -> {
            results.onAddJobResult(true);
        }).addOnFailureListener(e -> {
            results.onAddJobResult(false);
        });
    }

    public Company getCompany() {
        return new Gson().fromJson(SharedPreferencesDataSource.getInstance().getString("company"), Company.class);
    }

    public void getJobCategories(final JobFireStoreResult results) {
        db.collection("jobCategories").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<JobCategory> jobCategories = new ArrayList<>();
                for (int i = 0; i < task.getResult().size(); i++) {
                    JobCategory category = task.getResult().toObjects(JobCategory.class).get(i);
                    category.setId(task.getResult().getDocuments().get(i).getId());
                    jobCategories.add(category);
                }
                results.onGetJobCatResult(true, jobCategories);
            } else {
                results.onGetJobCatResult(false, null);
            }
        }).addOnFailureListener(e -> {
            results.onGetJobCatResult(false, null);
        });
    }
}
