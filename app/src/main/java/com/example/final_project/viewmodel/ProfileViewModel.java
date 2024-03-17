package com.example.final_project.viewmodel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project.models.Resume;
import com.example.final_project.models.User;
import com.example.final_project.repository.ProfileRepository;
import com.example.final_project.utils.ProfileFireStoreResult;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    private ProfileRepository profileRepository;
    private MutableLiveData<User> currentUser = new MutableLiveData<>();
    private MutableLiveData<List<Resume>> resumes = new MutableLiveData<>();
    public ProfileViewModel(User user) {
        profileRepository = ProfileRepository.getInstance();
        currentUser.setValue(user);
        fetchResumes();
    }

    public MutableLiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void setUser(User user) {
        currentUser.setValue(user);
    }

    public MutableLiveData<List<Resume>> getResumes() {
        return resumes;
    }

    public void setResumes(List<Resume> resumes) {
        this.resumes.setValue(resumes);
    }

    public void fetchResumes() {
        profileRepository.fetchResumes(new ProfileFireStoreResult() {
            @Override
            public void onUpdateProfileResult(boolean result, User user) {

            }

            @Override
            public void onUploadResumeResult(boolean result, Resume resume) {

            }

            @Override
            public void onFetchResumesResult(boolean result, List<Resume> resumes) {
                if (result) {
                    setResumes(resumes);
                }
            }
        });
    }

    public void updateProfile(String name, String phone, ProfileFireStoreResult result) {
        User user = currentUser.getValue();
        if(user != null){
            user.setName(name);
            user.setPhone(phone);
            profileRepository.updateProfile(user, result);
        }
    }

    public void uploadResume(Uri pdfResume, ProfileFireStoreResult result) {
        profileRepository.uploadResume(pdfResume, result);
    }
}
