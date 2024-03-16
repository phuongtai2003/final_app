package com.example.final_project.viewmodel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project.CompanyHomeFragment;
import com.example.final_project.ProfileFragment;
import com.example.final_project.models.Company;
import com.example.final_project.repository.HomeRepository;

import java.util.ArrayList;
import java.util.List;

public class CompanyHomeViewModel extends ViewModel {
    private List<Fragment> fragments;
    private MutableLiveData<Company> currentCompany = new MutableLiveData<>();
    private MutableLiveData<Fragment> currentFragment = new MutableLiveData<>();

    private HomeRepository homeRepository;
    public CompanyHomeViewModel(){
        homeRepository = HomeRepository.getInstance();
        this.fragments = new ArrayList<Fragment>() {{
            add(new CompanyHomeFragment());
            add(new CompanyHomeFragment());
            add(new CompanyHomeFragment());
        }};
        currentFragment.setValue(fragments.get(0));
        currentCompany.setValue(homeRepository.getCurrentCompany());
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
}
