package com.example.final_project.utils;

import com.example.final_project.models.Application;

public interface OnCompanyApplicationClicked {
    void openResume(String resumeUrl);
    void acceptApplication(Application application);
    void rejectApplication(Application application);
}
