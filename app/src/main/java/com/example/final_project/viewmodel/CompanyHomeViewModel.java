package com.example.final_project.viewmodel;

import android.net.Uri;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project.CompanyHomeFragment;
import com.example.final_project.CompanyJobListFragment;
import com.example.final_project.CompanyProfileFragment;
import com.example.final_project.ProfileFragment;
import com.example.final_project.models.Application;
import com.example.final_project.models.Company;
import com.example.final_project.models.Job;
import com.example.final_project.models.JobCategory;
import com.example.final_project.repository.HomeRepository;
import com.example.final_project.repository.JobRepository;
import com.example.final_project.utils.FirebaseStorageResult;
import com.example.final_project.utils.JobFireStoreResult;

import java.util.ArrayList;
import java.util.List;

public class CompanyHomeViewModel extends ViewModel {
    private List<Fragment> fragments;
    private MutableLiveData<Company> currentCompany = new MutableLiveData<>();
    private MutableLiveData<Fragment> currentFragment = new MutableLiveData<>();
    private MutableLiveData<List<Job>> jobs = new MutableLiveData<>();
    private HomeRepository homeRepository;
    private JobRepository jobRepository;
    public CompanyHomeViewModel(){
        homeRepository = HomeRepository.getInstance();
        jobRepository = JobRepository.getInstance();
        this.fragments = new ArrayList<Fragment>() {{
            add(new CompanyHomeFragment());
            add(new CompanyJobListFragment());
            add(new CompanyProfileFragment());
        }};
        currentFragment.setValue(fragments.get(0));
        currentCompany.setValue(homeRepository.getCurrentCompany());
        fetchCompanyJobs();
    }

    public void setCurrentCompany(Company company){
        currentCompany.setValue(company);
    }

    public void fetchCompanyJobs(){
        jobRepository.getJobsByCompany(homeRepository.getCurrentCompany().getId(), new JobFireStoreResult() {
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
                }
            }

            @Override
            public void onGetAppliedJobsResult(boolean result, List<Application> jobList) {

            }
        });
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

    public MutableLiveData<Company> getCurrentCompany() {
        return currentCompany;
    }

    public MutableLiveData<Fragment> getCurrentFragment() {
        return currentFragment;
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public void signOut() {
        homeRepository.logout();
    }
    public void uploadImage(Uri imageUri, FirebaseStorageResult results) {
        homeRepository.uploadImageCompany(imageUri, results);
    }
}
