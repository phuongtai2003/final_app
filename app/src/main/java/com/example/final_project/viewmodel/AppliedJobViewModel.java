package com.example.final_project.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project.models.Application;
import com.example.final_project.models.Job;
import com.example.final_project.models.JobCategory;
import com.example.final_project.repository.JobRepository;
import com.example.final_project.utils.JobFireStoreResult;

import java.util.Arrays;
import java.util.List;

public class AppliedJobViewModel extends ViewModel {
    private JobRepository jobRepository;
    private MutableLiveData<List<Application>> appliedJobs = new MutableLiveData<>();

    public AppliedJobViewModel() {
        jobRepository = JobRepository.getInstance();
        fetchAppliedJobs();
    }

    public MutableLiveData<List<Application>> getAppliedJobs() {
        return appliedJobs;
    }

    public void setAppliedJobs(List<Application> appliedJobs) {
        this.appliedJobs.postValue(appliedJobs);
    }


    public void fetchAppliedJobs() {
        jobRepository.fetchAppliedJobs(new JobFireStoreResult() {
            @Override
            public void onGetJobCatResult(boolean result, List<JobCategory> jobCategories) {

            }

            @Override
            public void onAddJobResult(boolean result) {

            }

            @Override
            public void onGetJobsByCompanyResult(boolean result, List<Job> jobList) {
            }

            @Override
            public void onGetAppliedJobsResult(boolean result, List<Application> applications) {
                if (result) {
                    setAppliedJobs(applications);
                }
            }
        });
    }
}
