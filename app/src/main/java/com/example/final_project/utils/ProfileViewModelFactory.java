package com.example.final_project.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.final_project.models.User;
import com.example.final_project.viewmodel.JobDetailsViewModel;
import com.example.final_project.viewmodel.ProfileViewModel;

public class ProfileViewModelFactory implements ViewModelProvider.Factory {
    private final User user;

    public ProfileViewModelFactory(User user) {
        this.user = user;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProfileViewModel.class)) {
            return (T) new ProfileViewModel(user);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
