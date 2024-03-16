package com.example.final_project.models;

public class Job {
    private String jobTitle;
    private String jobCategory;
    private String jobExperience;
    private String jobGender;
    private String jobSalary;
    private String jobDescription;
    private String jobQualifications;
    private String jobBenefits;
    private String companyId;

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

    public void setJobQualifications(String jobQualifications) {
        this.jobQualifications = jobQualifications;
    }

    public String getJobBenefits() {
        return jobBenefits;
    }

    public void setJobBenefits(String jobBenefits) {
        this.jobBenefits = jobBenefits;
    }
}
