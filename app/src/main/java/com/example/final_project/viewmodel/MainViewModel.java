package com.example.final_project.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.final_project.repository.AuthenticationRepository;

public class MainViewModel extends ViewModel {
    private AuthenticationRepository authenticationRepository;

    public MainViewModel() {
        authenticationRepository = AuthenticationRepository.getInstance();
    }

    public boolean isUserLoggedIn() {
        return authenticationRepository.getUser() != null;
    }

    public boolean isCompanyLoggedIn() {
        return authenticationRepository.getCompany() != null;
    }
}
