package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.final_project.databinding.ActivityAddJobBinding;
import com.example.final_project.databinding.ActivityJobDetailsBinding;
import com.example.final_project.models.Job;
import com.example.final_project.utils.GlobalConstants;
import com.example.final_project.utils.JobDetailsFireStoreResult;
import com.example.final_project.utils.JobDetailsViewModelFactory;
import com.example.final_project.utils.UIHelpers;
import com.example.final_project.viewmodel.JobDetailsViewModel;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.guieffect.qual.UI;

public class JobDetailsActivity extends AppCompatActivity {
    private ActivityJobDetailsBinding binding;
    private JobDetailsViewModel jobDetailsViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViewModel();
        initViews();
    }

    private void initViewModel() {
        Job job = getIntent().getParcelableExtra("job");
        JobDetailsViewModelFactory factory = new JobDetailsViewModelFactory(job);
        jobDetailsViewModel = new ViewModelProvider(this, factory).get(JobDetailsViewModel.class);
    }

    private void initViews() {
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });
        jobDetailsViewModel.getJob().observe(this, job -> {
            binding.jobTitleTxt.setText(job.getJobTitle());
            binding.companyNameTxt.setText(job.getCompany().getName());
            if(job.getCompany().getImage() != null){
                Picasso.get().load(job.getCompany().getImage()).into(binding.companyImage);
            }
            else{
                Picasso.get().load(GlobalConstants.defaultCompanyLogoUrl).into(binding.companyImage);
            }
            binding.jobCatTxt.setText(job.getJobCategory());
            binding.experienceTxt.setText(job.getJobExperience());
            binding.salaryRangeTxt.setText(job.getJobSalary());
            binding.jobDescTxt.setText(job.getJobDescription());
            binding.jobReqTxt.setText(job.getJobQualifications());
            binding.jobBenefitsTxt.setText(job.getJobBenefits());
        });
        jobDetailsViewModel.getIsBookmarked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    binding.bookmarkBtn.setBackground(AppCompatResources.getDrawable(JobDetailsActivity.this, R.drawable.orange_button));
                }
                else{
                    binding.bookmarkBtn.setBackground(AppCompatResources.getDrawable(JobDetailsActivity.this, R.drawable.orange_button_outline));
                }
            }
        });
        binding.applyBtn.setOnClickListener(v -> {
            PickResumeDialogFragment dialog = new PickResumeDialogFragment(jobDetailsViewModel);
            dialog.show(getSupportFragmentManager(), "pick_resume");
        });
        binding.bookmarkBtn.setOnClickListener(v -> {
            binding.progressIndicator.setVisibility(View.VISIBLE);
            binding.jobDetailsLayout.setVisibility(View.GONE);
            if(!jobDetailsViewModel.getIsBookmarked().getValue()){
                jobDetailsViewModel.bookmarkJob(new JobDetailsFireStoreResult() {
                    @Override
                    public void onBookmarkJobResult(boolean isBookmarked) {
                        if(isBookmarked){
                            jobDetailsViewModel.setIsBookmarked(true);
                            UIHelpers.showDialog(Gravity.CENTER, "Job has been bookmarked" , JobDetailsActivity.this);
                        }
                        else{
                            UIHelpers.showDialog(Gravity.CENTER, "Something went wrong" , JobDetailsActivity.this);
                        }
                        binding.progressIndicator.setVisibility(View.GONE);
                        binding.jobDetailsLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onApplyJobResult(boolean isApplied) {

                    }
                });
            }
            else{
                jobDetailsViewModel.removeBookmark(new JobDetailsFireStoreResult() {
                    @Override
                    public void onBookmarkJobResult(boolean isBookmarked) {
                        if(isBookmarked){
                            jobDetailsViewModel.setIsBookmarked(false);
                            UIHelpers.showDialog(Gravity.CENTER, "Job has been removed from bookmarks" , JobDetailsActivity.this);
                        }
                        else{
                            UIHelpers.showDialog(Gravity.CENTER, "Something went wrong" , JobDetailsActivity.this);
                        }
                        binding.progressIndicator.setVisibility(View.GONE);
                        binding.jobDetailsLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onApplyJobResult(boolean isApplied) {

                    }
                });
            }
        });
    }
}