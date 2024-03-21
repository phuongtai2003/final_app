package com.example.final_project.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.final_project.models.Company;
import com.example.final_project.viewmodel.CompanyProfileViewModel;

public class CompanyProfileViewModelFactory implements ViewModelProvider.Factory {
    private final Company company;

    public CompanyProfileViewModelFactory(Company company) {
        this.company = company;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(CompanyProfileViewModel.class)){
            return (T) new CompanyProfileViewModel(company);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
