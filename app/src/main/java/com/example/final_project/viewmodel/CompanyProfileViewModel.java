package com.example.final_project.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project.models.Company;
import com.example.final_project.repository.ProfileRepository;
import com.example.final_project.utils.ProfileFireStoreResult;

public class CompanyProfileViewModel extends ViewModel {
    private ProfileRepository profileRepository;
    private MutableLiveData<Company> company = new MutableLiveData<>();
    public CompanyProfileViewModel(Company company) {
        profileRepository = ProfileRepository.getInstance();
        this.company.setValue(company);
    }

    public MutableLiveData<Company> getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company.setValue(company);
    }

    public void updateCompany(Company company, ProfileFireStoreResult result) {
        profileRepository.updateCompany(company, result);
    }
}
