package com.example.final_project.repository;

import android.util.Log;

import com.example.final_project.data.SharedPreferencesDataSource;
import com.example.final_project.models.Company;
import com.example.final_project.models.User;
import com.example.final_project.utils.FireStoreResults;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class AuthenticationRepository {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static AuthenticationRepository instance;
    private AuthenticationRepository() {
    }

    public static AuthenticationRepository getInstance() {
        if (instance == null) {
            instance = new AuthenticationRepository();
        }
        return instance;
    }

    public void registerUser(String email, String password, String name, String phone, String accountType ,final FireStoreResults results) {
        User user =new User(email, password, name, phone, null);
        db.collection("users").document(email).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                results.onResult(false);
            }
            else{
                db.collection("companies").document(email).get().addOnSuccessListener(documentSnapshot1 -> {
                    if (documentSnapshot1.exists()) {
                        results.onResult(false);
                    }
                    else{
                        if(accountType.equalsIgnoreCase("Seeker")){
                            db.collection("users").document(email).set(user).addOnSuccessListener(aVoid -> {
                                results.onResult(true);
                            }).addOnFailureListener(e -> {
                                results.onResult(false);
                            });
                        }
                        else{
                            db.collection("companies").document(email).set(user).addOnSuccessListener(aVoid -> {
                                results.onResult(true);
                            }).addOnFailureListener(e -> {
                                results.onResult(false);
                            });
                        }
                    }
                }).addOnFailureListener(e -> {
                    results.onResult(false);
                });
            }
        }).addOnFailureListener(e -> {
            results.onResult(false);
        });
    }

    public void loginUser(String email, String password, final FireStoreResults results){
        db.collection("users").document(email).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null && user.getPassword().equals(password)) {
                    user.setId(documentSnapshot.getId());
                    SharedPreferencesDataSource.getInstance().saveString("email", user.getEmail());
                    SharedPreferencesDataSource.getInstance().saveString("password", user.getPassword());
                    SharedPreferencesDataSource.getInstance().saveString("user", new Gson().toJson(user));
                    results.onLoginResult(true, user, null);
                }
                else{
                    results.onLoginResult(false, null, null);
                }
            }
            else{
                db.collection("companies").document(email).get().addOnSuccessListener(documentSnapshot1 -> {
                    if (documentSnapshot1.exists()) {
                        Company company = documentSnapshot1.toObject(Company.class);
                        if (company != null && company.getPassword().equals(password)) {
                            company.setId(documentSnapshot1.getId());
                            SharedPreferencesDataSource.getInstance().saveString("email", company.getEmail());
                            SharedPreferencesDataSource.getInstance().saveString("password", company.getPassword());
                            SharedPreferencesDataSource.getInstance().saveString("company", new Gson().toJson(company));
                            results.onLoginResult(true, null, company);
                        }
                        else{
                            results.onLoginResult(false, null, null);
                        }
                    }
                    else{
                        results.onLoginResult(false, null, null);
                    }
                }).addOnFailureListener(e -> {
                    results.onLoginResult(false, null, null);
                });
            }
        }).addOnFailureListener(e -> {
            results.onLoginResult(false, null, null);
        });
    }

    public String getEmail() {
        return SharedPreferencesDataSource.getInstance().getString("email");
    }

    public String getPassword() {
        return SharedPreferencesDataSource.getInstance().getString("password");
    }

    public User getUser() {
        String user = SharedPreferencesDataSource.getInstance().getString("user");
        return new Gson().fromJson(user, User.class);
    }

    public Company getCompany() {
        String company = SharedPreferencesDataSource.getInstance().getString("company");
        return new Gson().fromJson(company, Company.class);
    }
}
