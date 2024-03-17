package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.final_project.adapter.JobAdapter;
import com.example.final_project.databinding.ActivityAppliedJobsBinding;
import com.example.final_project.models.Job;
import com.example.final_project.utils.OnJobClickedListener;
import com.example.final_project.viewmodel.AppliedJobViewModel;

public class AppliedJobsActivity extends AppCompatActivity {
    private ActivityAppliedJobsBinding binding;
    private AppliedJobViewModel appliedJobViewModel;
    private JobAdapter jobAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppliedJobsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViewModel();
        initViews();
    }

    private void initViewModel() {
        appliedJobViewModel = new ViewModelProvider(this).get(AppliedJobViewModel.class);
    }

    private void initViews() {
        appliedJobViewModel.getAppliedJobs().observe(this, jobs -> {
            binding.progressIndicator.hide();
            binding.appliedJobsRecyclerView.setVisibility(View.VISIBLE);
            jobAdapter = new JobAdapter(AppliedJobsActivity.this, jobs, R.layout.search_job_item, new OnJobClickedListener() {
                @Override
                public void onJobClicked(Job job) {

                }
            });
            binding.appliedJobsRecyclerView.setLayoutManager(new LinearLayoutManager(AppliedJobsActivity.this, LinearLayoutManager.VERTICAL, false));
            binding.appliedJobsRecyclerView.setAdapter(jobAdapter);
        });
    }
}