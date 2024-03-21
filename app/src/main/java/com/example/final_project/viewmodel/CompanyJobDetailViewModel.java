package com.example.final_project.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project.models.Job;
import com.example.final_project.repository.JobRepository;

public class CompanyJobDetailViewModel extends ViewModel {
    private JobRepository jobRepository;

    private MutableLiveData<Job> job = new MutableLiveData<>();

    public CompanyJobDetailViewModel(Job job) {
        this.jobRepository = JobRepository.getInstance();
        this.job.setValue(job);
    }

    public MutableLiveData<Job> getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job.postValue(job);
    }
}
