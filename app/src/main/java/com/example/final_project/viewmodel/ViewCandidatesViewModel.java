package com.example.final_project.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project.models.Application;
import com.example.final_project.models.Job;
import com.example.final_project.models.JobCategory;
import com.example.final_project.repository.JobRepository;
import com.example.final_project.utils.JobDetailsFireStoreResult;
import com.example.final_project.utils.JobFireStoreResult;

import java.util.List;

public class ViewCandidatesViewModel extends ViewModel {
    private MutableLiveData<Job> job = new MutableLiveData<>();
    private MutableLiveData<List<Application>> applications = new MutableLiveData<>();
    private JobRepository jobRepository;

    public ViewCandidatesViewModel(Job job){
        this.job.setValue(job);
        jobRepository = JobRepository.getInstance();
        fetchApplicationForJob();
    }

    public void acceptApplication(Application application, JobDetailsFireStoreResult jobFireStoreResult){
        jobRepository.acceptApplication(application, jobFireStoreResult);
    }

    public void rejectApplication(Application application, JobDetailsFireStoreResult jobFireStoreResult){
        jobRepository.rejectApplication(application, jobFireStoreResult);
    }

    public void fetchApplicationForJob(){
        jobRepository.fetchApplicationsForJob(job.getValue(), new JobFireStoreResult() {
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
            public void onGetAppliedJobsResult(boolean result, List<Application> jobList) {
                if (result){
                    setApplications(jobList);
                }
            }
        });
    }

    public MutableLiveData<List<Application>> getApplications(){
        return applications;
    }

    public void setApplications(List<Application> applications){
        this.applications.postValue(applications);
    }

    public MutableLiveData<Job> getJob(){
        return job;
    }

    public void setJob(Job job){
        this.job.setValue(job);
    }
}
