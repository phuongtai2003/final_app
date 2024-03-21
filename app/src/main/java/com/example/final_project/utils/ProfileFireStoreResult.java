package com.example.final_project.utils;

import com.example.final_project.models.Company;
import com.example.final_project.models.Resume;
import com.example.final_project.models.User;

import java.util.List;

public interface ProfileFireStoreResult {
    void onUpdateProfileResult(boolean result, User user);
    void onUpdateCompanyProfileResult(boolean result, Company user);

    void onUploadResumeResult(boolean result, Resume resume);

    void onFetchResumesResult(boolean result, List<Resume> resumes);
}
