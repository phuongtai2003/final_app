package com.example.final_project.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.final_project.repository.AuthenticationRepository;
import com.example.final_project.utils.FireStoreResults;

public class RegisterViewModel extends ViewModel {
    private AuthenticationRepository authenticationRepository;

    public RegisterViewModel() {
        authenticationRepository = AuthenticationRepository.getInstance();
    }

    public void register(String email, String password, String name, String phone, String accountType ,final FireStoreResults fireStoreResults) {
        authenticationRepository.registerUser(email, password, name, phone, accountType ,fireStoreResults);
    }
}
