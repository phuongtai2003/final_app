package com.example.final_project.viewmodel;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project.HomeFragment;
import com.example.final_project.ProfileFragment;
import com.example.final_project.models.Company;
import com.example.final_project.models.JobCategory;
import com.example.final_project.models.User;
import com.example.final_project.repository.HomeRepository;
import com.example.final_project.repository.JobRepository;
import com.example.final_project.utils.HomeFireStoreResult;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private List<Fragment> fragments;
    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<Fragment> currentFragment = new MutableLiveData<>();
    private MutableLiveData<List<JobCategory>> jobCategories = new MutableLiveData<>();
    private MutableLiveData<List<Company>> companies = new MutableLiveData<>();
    private HomeRepository homeRepository;
    private JobRepository jobRepository;
    public HomeViewModel() {
        homeRepository = HomeRepository.getInstance();
        jobRepository = JobRepository.getInstance();
        this.fragments = new ArrayList<Fragment>() {{
            add(new HomeFragment());
            add(new HomeFragment());
            add(new HomeFragment());
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

    public void setCurrentFragment(int position) {
        currentFragment.setValue(fragments.get(position));
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

    public void signOut() {
        homeRepository.logout();
    }
}
