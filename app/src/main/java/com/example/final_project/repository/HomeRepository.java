package com.example.final_project.repository;

import android.net.Uri;

import com.example.final_project.data.SharedPreferencesDataSource;
import com.example.final_project.models.Company;
import com.example.final_project.models.JobCategory;
import com.example.final_project.models.User;
import com.example.final_project.utils.FirebaseStorageResult;
import com.example.final_project.utils.HomeFireStoreResult;
import com.example.final_project.utils.UpdateProfileResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomeRepository {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private static HomeRepository instance;

    private HomeRepository() {
    }

    public static HomeRepository getInstance() {
        if (instance == null) {
            instance = new HomeRepository();
        }
        return instance;
    }

    public void saveUser(User user) {
        SharedPreferencesDataSource.getInstance().saveString("user", new Gson().toJson(user));
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

    public void updateProfile(User user, UpdateProfileResult result) {
        db.collection("users").document(user.getId()).set(user).addOnSuccessListener(aVoid -> {
            SharedPreferencesDataSource.getInstance().saveString("user", new Gson().toJson(user));
            result.onUpdateProfileResult(true, user);
        }).addOnFailureListener(e -> {
            result.onUpdateProfileResult(false, null);
        });
    }

    public void updateProfileCompany(Company company, UpdateProfileResult result) {
        db.collection("companies").document(company.getId()).set(company).addOnSuccessListener(aVoid -> {
            SharedPreferencesDataSource.getInstance().saveString("company", new Gson().toJson(company));
            result.onUpdateCompanyProfileResult(true, company);
        }).addOnFailureListener(e -> {
            result.onUpdateCompanyProfileResult(false, null);
        });
    }

    public void uploadImage(Uri imageUri, FirebaseStorageResult results) {
        StorageReference imageRef = storageReference.child("images/" + imageUri.getLastPathSegment());
        imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                User user = getCurrentUser();
                user.setImage(uri.toString());
                updateProfile(user, new UpdateProfileResult() {
                    @Override
                    public void onUpdateProfileResult(boolean result, User user) {
                        if (result) {
                            results.onImageUploadResult(true, uri.toString());
                        }
                        else {
                            results.onImageUploadResult(false, null);
                        }

                    }

                    @Override
                    public void onUpdateCompanyProfileResult(boolean result, Company user) {

                    }
                });
            });
        }).addOnFailureListener(e -> {
            results.onImageUploadResult(false, null);
        });
    }

    public void uploadImageCompany(Uri imageUri, FirebaseStorageResult results) {
        StorageReference imageRef = storageReference.child("images/" + imageUri.getLastPathSegment());
        imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Company company = getCurrentCompany();
                company.setImage(uri.toString());
                updateProfileCompany(company, new UpdateProfileResult() {
                    @Override
                    public void onUpdateProfileResult(boolean result, User user) {

                    }

                    @Override
                    public void onUpdateCompanyProfileResult(boolean result, Company company1) {
                        if (result) {
                            results.onImageUploadResult(true, uri.toString());
                        }
                        else {
                            results.onImageUploadResult(false, null);
                        }

                    }
                });
            });
        }).addOnFailureListener(e -> {
            results.onImageUploadResult(false, null);
        });
    }

}
