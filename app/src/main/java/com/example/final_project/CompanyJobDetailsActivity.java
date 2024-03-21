package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.final_project.databinding.ActivityCompanyJobDetailsBinding;
import com.example.final_project.models.Job;
import com.example.final_project.utils.GlobalConstants;
import com.example.final_project.utils.JobDetailsViewModelFactory;
import com.example.final_project.viewmodel.CompanyJobDetailViewModel;
import com.squareup.picasso.Picasso;

public class CompanyJobDetailsActivity extends AppCompatActivity {
    private ActivityCompanyJobDetailsBinding binding;
    private CompanyJobDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompanyJobDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initVM();
        initView();
    }

    private void initVM(){
        Job job = getIntent().getParcelableExtra("job");
        JobDetailsViewModelFactory factory = new JobDetailsViewModelFactory(job);
        viewModel = new ViewModelProvider(this, factory).get(CompanyJobDetailViewModel.class);
    }

    private void initView(){
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });
        binding.viewCandidatesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyJobDetailsActivity.this, ViewCandidatesActivity.class);
                intent.putExtra("job", viewModel.getJob().getValue());
                startActivity(intent);
            }
        });
        viewModel.getJob().observe(this, job -> {
            binding.jobTitleTxt.setText(job.getJobTitle());
            binding.companyNameTxt.setText(job.getCompany().getName());
            binding.jobCatTxt.setText(job.getJobCategory());
            binding.salaryRangeTxt.setText(job.getJobSalary());
            binding.experienceTxt.setText(job.getJobExperience());
            binding.jobDescTxt.setText(job.getJobDescription());
            binding.jobReqTxt.setText(job.getJobQualifications());
            binding.jobBenefitsTxt.setText(job.getJobBenefits());
            if(job.getCompany().getImage() != null){
                Picasso.get().load(job.getCompany().getImage()).into(binding.companyImage);
            }
            else {
                Picasso.get().load(GlobalConstants.defaultCompanyLogoUrl).into(binding.companyImage);
            }
        });
    }
}