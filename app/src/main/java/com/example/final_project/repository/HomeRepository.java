package com.example.final_project.repository;

import com.example.final_project.data.SharedPreferencesDataSource;
import com.example.final_project.models.Company;
import com.example.final_project.models.JobCategory;
import com.example.final_project.models.User;
import com.example.final_project.utils.HomeFireStoreResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomeRepository {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static HomeRepository instance;

    private HomeRepository() {
    }

    public static HomeRepository getInstance() {
        if (instance == null) {
            instance = new HomeRepository();
        }
        return instance;
    }

    public User getCurrentUser() {
        return new Gson().fromJson(SharedPreferencesDataSource.getInstance().getString("user"), User.class);
    }

    public Company getCurrentCompany() {
        return new Gson().fromJson(SharedPreferencesDataSource.getInstance().getString("company"), Company.class);
    }

    public void getCompanies(final HomeFireStoreResult results) {
        db.collection("companies").limit(5).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Company> companies = new ArrayList<>();
                for (int i = 0; i < task.getResult().size(); i++) {
                    Company company = task.getResult().toObjects(Company.class).get(i);
                    company.setId(task.getResult().getDocuments().get(i).getId());
                    companies.add(company);
                    companies.add(company);
                }
                results.onGetCompaniesResult(true, companies);
            } else {
                results.onGetCompaniesResult(false, null);
            }
        }).addOnFailureListener(e -> {
            results.onGetCompaniesResult(false, null);
        });
    }

    public void logout() {
        SharedPreferencesDataSource.getInstance().saveString("user", null);
        SharedPreferencesDataSource.getInstance().saveString("company", null);
    }

    public void getJobCategories(final HomeFireStoreResult results) {
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
