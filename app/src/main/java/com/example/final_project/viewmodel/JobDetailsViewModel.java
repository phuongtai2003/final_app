package com.example.final_project.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project.models.Company;
import com.example.final_project.models.Job;
import com.example.final_project.models.Resume;
import com.example.final_project.models.User;
import com.example.final_project.repository.HomeRepository;
import com.example.final_project.repository.JobRepository;
import com.example.final_project.repository.ProfileRepository;
import com.example.final_project.utils.JobDetailsFireStoreResult;
import com.example.final_project.utils.ProfileFireStoreResult;

import java.util.List;

public class JobDetailsViewModel extends ViewModel {
    private HomeRepository homeRepository;
    private JobRepository jobRepository;
    private ProfileRepository profileRepository;
    private MutableLiveData<Job> job = new MutableLiveData<>();
    private MutableLiveData<List<Resume>> resumes = new MutableLiveData<>();
    private MutableLiveData<Boolean> isBookmarked = new MutableLiveData<>();

    public JobDetailsViewModel(Job job) {
        this.jobRepository = JobRepository.getInstance();
        this.homeRepository = HomeRepository.getInstance();
        this.profileRepository = ProfileRepository.getInstance();
        this.job.setValue(job);
        this.isBookmarked.setValue(false);
        isJobBookmarked();
        fetchResumes();
    }

    public void applyJob(Resume resume, JobDetailsFireStoreResult result){
        jobRepository.applyJob(job.getValue(), resume, result);
    }

    public void fetchResumes(){
        profileRepository.fetchResumes(new ProfileFireStoreResult() {
            @Override
            public void onUpdateCompanyProfileResult(boolean result, Company user) {

            }

            @Override
            public void onUpdateProfileResult(boolean result, User user) {

            }

            @Override
            public void onUploadResumeResult(boolean result, Resume resume) {

            }

            @Override
            public void onFetchResumesResult(boolean result, List<Resume> resumes) {
                if(result){
                    JobDetailsViewModel.this.resumes.setValue(resumes);
                }
            }
        });
    }

    public void setResumes(List<Resume> resumes) {
        this.resumes.postValue(resumes);
    }

    public MutableLiveData<List<Resume>> getResumes() {
        return resumes;
    }


    public MutableLiveData<Job> getJob() {
        return job;
    }

    public void setJob(MutableLiveData<Job> job) {
        this.job = job;
    }

    public MutableLiveData<Boolean> getIsBookmarked() {
        return isBookmarked;
    }

    public void setIsBookmarked(boolean isBookmarked) {
        this.isBookmarked.setValue(isBookmarked);
    }

    public void bookmarkJob(JobDetailsFireStoreResult results){
        User user = homeRepository.getCurrentUser();
        jobRepository.bookmarkJob(job.getValue().getId(), user.getId() ,results);
    }

    public void removeBookmark(JobDetailsFireStoreResult results){
        User user = homeRepository.getCurrentUser();
        jobRepository.removeBookmark(job.getValue().getId(), user.getId(), results);
    }

    public void isJobBookmarked(){
        User user = homeRepository.getCurrentUser();
        jobRepository.isJobBookmarked(job.getValue().getId(), user.getId(), new JobDetailsFireStoreResult() {
            @Override
            public void onBookmarkJobResult(boolean isBookmarked) {
                setIsBookmarked(isBookmarked);
            }

            @Override
            public void onApplyJobResult(boolean isApplied) {

            }
        });
    }
}
