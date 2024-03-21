package com.example.final_project.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.final_project.models.Job;
import com.example.final_project.viewmodel.CompanyJobDetailViewModel;
import com.example.final_project.viewmodel.JobDetailsViewModel;
import com.example.final_project.viewmodel.ViewCandidatesViewModel;

public class JobDetailsViewModelFactory implements ViewModelProvider.Factory {

    private final Job job;

    public JobDetailsViewModelFactory(Job job) {
        this.job = job;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(JobDetailsViewModel.class)) {
            return (T) new JobDetailsViewModel(job);
        }
        else if(modelClass.isAssignableFrom(CompanyJobDetailViewModel.class)){
            return (T) new CompanyJobDetailViewModel(job);
        }
        else if(modelClass.isAssignableFrom(ViewCandidatesViewModel.class)){
            return (T) new ViewCandidatesViewModel(job);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

