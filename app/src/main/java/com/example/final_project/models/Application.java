package com.example.final_project.models;

import com.example.final_project.models.Company;
import com.example.final_project.models.Resume;

public class Application {
    private Job job;
    private Resume resume;
    private String status;
    private String time;


    public Application(Job job, Resume resume, String status, String time) {
        this.job = job;
        this.resume = resume;
        this.status = status;
        this.time = time;
    }

    public Application() {
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }


    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public String getStatus() {
        return status;
    }

public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String toString() {
        return "Application{" +
                "job=" + job +
                ", resume=" + resume +
                ", status='" + status + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

}
