package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.example.final_project.adapter.StringSpinnerAdapter;
import com.example.final_project.databinding.ActivityAddJobBinding;
import com.example.final_project.models.Application;
import com.example.final_project.models.Job;
import com.example.final_project.models.JobCategory;
import com.example.final_project.utils.GlobalConstants;
import com.example.final_project.utils.JobFireStoreResult;
import com.example.final_project.utils.UIHelpers;
import com.example.final_project.viewmodel.JobViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class AddJobActivity extends AppCompatActivity {
    private ActivityAddJobBinding binding;
    private JobViewModel jobViewModel;
    private StringSpinnerAdapter jobExperienceAdapter;
    private StringSpinnerAdapter gendersAdapter;
    private StringSpinnerAdapter salaryAdapter;
    private StringSpinnerAdapter jobCategoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initVM();
        initView();
    }

    private void initVM() {
        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);
    }

    private void initView(){
        binding.progressIndicator.show();
        binding.addJobLayout.setVisibility(View.GONE);
        binding.backBtn.setOnClickListener(v -> finish());
        salaryAdapter = new StringSpinnerAdapter(GlobalConstants.salaryRanges.subList(1, GlobalConstants.salaryRanges.size()), this);
        binding.salarySpinner.setAdapter(salaryAdapter);
        jobExperienceAdapter = new StringSpinnerAdapter(GlobalConstants.experienceLevels.subList(1, GlobalConstants.experienceLevels.size()), this);
        binding.experienceSpinner.setAdapter(jobExperienceAdapter);
        gendersAdapter = new StringSpinnerAdapter(GlobalConstants.genders, this);
        binding.genderSpinner.setAdapter(gendersAdapter);
        jobViewModel.getJobCategories().observe(this, jobCategories -> {
            List<String> categories = jobCategories.stream().filter(jobCategory -> !jobCategory.getName().equals("View All")).map(jobCategory -> jobCategory.getName()).collect(Collectors.toList());
            jobCategoryAdapter = new StringSpinnerAdapter(categories, this);
            binding.jobCategorySpinner.setAdapter(jobCategoryAdapter);
            binding.progressIndicator.hide();
            binding.addJobLayout.setVisibility(View.VISIBLE);
        });

        binding.addJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.jobTitleEdt.getText().toString();
                String description = binding.jobDescriptionEdt.getText().toString();
                String jobCategory = binding.jobCategorySpinner.getSelectedItem().toString();
                String experience = binding.experienceSpinner.getSelectedItem().toString();
                String gender = binding.genderSpinner.getSelectedItem().toString();
                String salary = binding.salarySpinner.getSelectedItem().toString();
                String jobDescription = binding.jobDescriptionEdt.getText().toString();
                String jobQualification = binding.jobQualificationsEdt.getText().toString();
                String benefits = binding.benefitsEdt.getText().toString();
                if(title.isEmpty()){
                    binding.jobTitleEdt.setError("Title is required");
                    return;
                }
                else if(description.isEmpty()){
                    binding.jobDescriptionEdt.setError("Description is required");
                    return;
                }
                else if(jobDescription.isEmpty()){
                    binding.jobDescriptionEdt.setError("Job Description is required");
                    return;
                }
                else if(jobQualification.isEmpty()){
                    binding.jobQualificationsEdt.setError("Qualifications are required");
                    return;
                }
                else if(benefits.isEmpty()){
                    binding.benefitsEdt.setError("Benefits are required");
                    return;
                }
                binding.addJobLayout.setVisibility(View.GONE);
                binding.progressIndicator.show();
                jobViewModel.addJob(title, jobCategory, experience, gender, salary, description, jobQualification, benefits, new JobFireStoreResult() {
                    @Override
                    public void onGetJobCatResult(boolean result, List<JobCategory> jobCategories) {

                    }

                    @Override
                    public void onAddJobResult(boolean result) {
                        if(result){
                            binding.addJobLayout.setVisibility(View.VISIBLE);
                            binding.progressIndicator.hide();
                            UIHelpers.showDialog(Gravity.CENTER, "Job added successfully", AddJobActivity.this);
                        }
                        else{
                            binding.addJobLayout.setVisibility(View.VISIBLE);
                            binding.progressIndicator.hide();
                            UIHelpers.showDialog(Gravity.CENTER, "Something went wrong", AddJobActivity.this);
                        }
                    }

                    @Override
                    public void onGetJobsByCompanyResult(boolean result, List<Job> jobList) {

                    }

                    @Override
                    public void onGetAppliedJobsResult(boolean result, List<Application> jobList) {

                    }
                });
            }
        });
    }
}