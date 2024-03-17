package com.example.final_project.viewmodel;

import android.net.Uri;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project.BookmarkJobFragment;
import com.example.final_project.HomeFragment;
import com.example.final_project.ProfileFragment;
import com.example.final_project.SearchFragment;
import com.example.final_project.models.Company;
import com.example.final_project.models.Job;
import com.example.final_project.models.JobCategory;
import com.example.final_project.models.User;
import com.example.final_project.repository.HomeRepository;
import com.example.final_project.repository.JobRepository;
import com.example.final_project.utils.FirebaseStorageResult;
import com.example.final_project.utils.HomeFireStoreResult;
import com.example.final_project.utils.JobFireStoreResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeViewModel extends ViewModel {
    private List<Fragment> fragments;
    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<Fragment> currentFragment = new MutableLiveData<>();
    private MutableLiveData<List<JobCategory>> jobCategories = new MutableLiveData<>();
    private MutableLiveData<List<Company>> companies = new MutableLiveData<>();
    private MutableLiveData<List<Job>> jobs = new MutableLiveData<>();
    private MutableLiveData<List<Job>> filteredJobs = new MutableLiveData<>();
    private MutableLiveData<List<Job>> bookmarkedJobs = new MutableLiveData<>();
    private HomeRepository homeRepository;
    private JobRepository jobRepository;
    public HomeViewModel() {
        homeRepository = HomeRepository.getInstance();
        jobRepository = JobRepository.getInstance();
        this.fragments = new ArrayList<Fragment>() {{
            add(new HomeFragment());
            add(new SearchFragment());
            add(new BookmarkJobFragment());
            add(new ProfileFragment());
        }};
        currentFragment.setValue(fragments.get(0));
        homeRepository.getJobCategories(new HomeFireStoreResult() {
            @Override
            public void onGetJobCatResult(boolean result, List<JobCategory> jobCategories) {
                if (result) {
                    setJobCategories(jobCategories);
                }
            }

            @Override
            public void onGetCompaniesResult(boolean result, List<Company> companies) {

            }
        });
        currentUser.setValue(homeRepository.getCurrentUser());
        fetchCompanies();
        fetchGetJobs();
        fetchBookmarkJobs();
    }

    public void fetchCompanies(){
        homeRepository.getCompanies(new HomeFireStoreResult() {
            @Override
            public void onGetJobCatResult(boolean result, List<JobCategory> jobCategories) {

            }

            @Override
            public void onGetCompaniesResult(boolean result, List<Company> companies) {
                if (result) {
                    setCompanies(companies);
                }
            }
        });
    }

    public void fetchGetJobs(){
        jobRepository.getJobs(new JobFireStoreResult() {
            @Override
            public void onGetJobCatResult(boolean result, List<JobCategory> jobCategories) {

            }

            @Override
            public void onAddJobResult(boolean result) {

            }

            @Override
            public void onGetJobsByCompanyResult(boolean result, List<Job> jobList) {
                if (result) {
                    setJobs(jobList);
                    filteredJobs.postValue(jobList);
                }
            }
        });
    }

    public void fetchBookmarkJobs(){
        jobRepository.getBookmarkedJobs(currentUser.getValue().getId(), new JobFireStoreResult() {
            @Override
            public void onGetJobCatResult(boolean result, List<JobCategory> jobCategories) {

            }

            @Override
            public void onAddJobResult(boolean result) {

            }

            @Override
            public void onGetJobsByCompanyResult(boolean result, List<Job> jobList) {
                if (result) {
                    Log.d("USER TAG", "onGetJobsByCompanyResult: " + jobList.size());
                    setBookmarkedJobs(new ArrayList<>(jobList));
                }
            }
        });
    }

    public MutableLiveData<List<Job>> getBookmarkedJobs() {
        return bookmarkedJobs;
    }

    public void setBookmarkedJobs(List<Job> bookmarkedJobs) {
        this.bookmarkedJobs.postValue(bookmarkedJobs);
    }

    public MutableLiveData<List<Job>> getFilteredJobs() {
        return filteredJobs;
    }

    public void filterJobs(String jobCategory, String experienceLevel, String salaryRange, String name) {
        if(jobs.getValue() == null) return;
        List<Job> filteredJobs;
        if(jobCategory.equalsIgnoreCase("View All")){
            filteredJobs = jobs.getValue();
        }
        else {
            filteredJobs = jobs.getValue().stream().filter(job -> job.getJobCategory().equalsIgnoreCase(jobCategory)).collect(Collectors.toList());
        }
        if(!experienceLevel.equalsIgnoreCase("View All")){
            filteredJobs = filteredJobs.stream().filter(job -> job.getJobExperience().equalsIgnoreCase(experienceLevel)).collect(Collectors.toList());
        }
        if(!salaryRange.equalsIgnoreCase("View All")){
            filteredJobs = filteredJobs.stream().filter(job -> job.getJobSalary().equalsIgnoreCase(salaryRange)).collect(Collectors.toList());
        }
        if(name != null && !name.isEmpty()){
            filteredJobs = filteredJobs.stream().filter(job -> job.getJobTitle().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        }
        this.filteredJobs.postValue(filteredJobs);
    }

    public MutableLiveData<List<Job>> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs.postValue(jobs);
    }
    public void setCurrentFragment(int position) {
        currentFragment.setValue(fragments.get(position));
    }
    public void setCurrentUser(User user) {
        homeRepository.saveUser(user);
        currentUser.setValue(user);
    }

    public MutableLiveData<User> getCurrentUser() {
        return currentUser;
    }

    public MutableLiveData<Fragment> getCurrentFragment() {
        return currentFragment;
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public MutableLiveData<List<JobCategory>> getJobCategories() {
        return jobCategories;
    }

    public void setJobCategories(List<JobCategory> jobCategories) {
        this.jobCategories.postValue(jobCategories);
    }

    public MutableLiveData<List<Company>> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies.postValue(companies);
    }

    public void uploadImage(Uri imageUri, FirebaseStorageResult result) {
        homeRepository.uploadImage(imageUri, result);
    }

    public void signOut() {
        homeRepository.logout();
    }
}
