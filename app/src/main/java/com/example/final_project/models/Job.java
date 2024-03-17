package com.example.final_project.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Job implements Parcelable {
    private String id;
    private String jobTitle;
    private String jobCategory;
    private String jobExperience;
    private String jobGender;
    private String jobSalary;
    private String jobDescription;
    private String jobQualifications;
    private String jobBenefits;
    private String companyId;
    private Company company;

    public Job() {
    }
    public Job(String jobTitle, String jobCategory, String jobExperience, String jobGender, String jobSalary, String jobDescription, String jobQualifications, String jobBenefits, String companyId) {
        this.jobTitle = jobTitle;
        this.jobCategory = jobCategory;
        this.jobExperience = jobExperience;
        this.jobGender = jobGender;
        this.jobSalary = jobSalary;
        this.jobDescription = jobDescription;
        this.jobQualifications = jobQualifications;
        this.jobBenefits = jobBenefits;
        this.companyId = companyId;
    }

    protected Job(Parcel in) {
        id = in.readString();
        jobTitle = in.readString();
        jobCategory = in.readString();
        jobExperience = in.readString();
        jobGender = in.readString();
        jobSalary = in.readString();
        jobDescription = in.readString();
        jobQualifications = in.readString();
        jobBenefits = in.readString();
        companyId = in.readString();
        company = in.readParcelable(Company.class.getClassLoader());
    }

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getJobExperience() {
        return jobExperience;
    }

    public void setJobExperience(String jobExperience) {
        this.jobExperience = jobExperience;
    }

    public String getJobGender() {
        return jobGender;
    }

    public void setJobGender(String jobGender) {
        this.jobGender = jobGender;
    }

    public String getJobSalary() {
        return jobSalary;
    }

    public void setJobSalary(String jobSalary) {
        this.jobSalary = jobSalary;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobQualifications() {
        return jobQualifications;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setJobQualifications(String jobQualifications) {
        this.jobQualifications = jobQualifications;
    }

    public String getJobBenefits() {
        return jobBenefits;
    }

    public void setJobBenefits(String jobBenefits) {
        this.jobBenefits = jobBenefits;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(jobTitle);
        parcel.writeString(jobCategory);
        parcel.writeString(jobExperience);
        parcel.writeString(jobGender);
        parcel.writeString(jobSalary);
        parcel.writeString(jobDescription);
        parcel.writeString(jobQualifications);
        parcel.writeString(jobBenefits);
        parcel.writeString(companyId);
        parcel.writeParcelable(company, i);
    }
}
