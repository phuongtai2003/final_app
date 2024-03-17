package com.example.final_project.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.final_project.models.Job;
import com.example.final_project.viewmodel.JobDetailsViewModel;

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
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

