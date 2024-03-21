package com.example.final_project.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project.models.Application;
import com.example.final_project.models.Company;
import com.example.final_project.models.Job;
import com.example.final_project.models.JobCategory;
import com.example.final_project.repository.HomeRepository;
import com.example.final_project.repository.JobRepository;
import com.example.final_project.utils.JobFireStoreResult;

import java.util.List;

public class JobViewModel extends ViewModel {
    private JobRepository jobRepository;
    private MutableLiveData<List<JobCategory>> jobCategories = new MutableLiveData<>();

    public JobViewModel() {
        jobRepository = JobRepository.getInstance();
        jobRepository.getJobCategories(new JobFireStoreResult() {
            @Override
            public void onGetJobCatResult(boolean result, List<JobCategory> jobCategories) {
                if(result){
                    setJobCategories(jobCategories);
                }
            }

            @Override
            public void onAddJobResult(boolean result) {
            }

            @Override
            public void onGetJobsByCompanyResult(boolean result, List<Job> jobList) {

            }

            @Override
            public void onGetAppliedJobsResult(boolean result, List<Application> jobList) {

            }
        });
    }

    public void addJob(String title, String category, String experience, String gender, String salary, String description, String qualifications, String benefits, JobFireStoreResult results){
        Company company = jobRepository.getCompany();
        Job job = new Job(title, category, experience, gender, salary, description, qualifications, benefits, company.getId());
        jobRepository.addJob(job, results);
    }

    public MutableLiveData<List<JobCategory>> getJobCategories() {
        return jobCategories;
    }

    public void setJobCategories(List<JobCategory> jobCategories) {
        this.jobCategories.setValue(jobCategories);
    }
}
