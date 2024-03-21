package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.final_project.adapter.ApplicationAdapter;
import com.example.final_project.adapter.JobAdapter;
import com.example.final_project.databinding.ActivityAppliedJobsBinding;
import com.example.final_project.models.Application;
import com.example.final_project.models.Job;
import com.example.final_project.utils.OnApplicationClicked;
import com.example.final_project.utils.OnJobClickedListener;
import com.example.final_project.viewmodel.AppliedJobViewModel;

public class AppliedJobsActivity extends AppCompatActivity {
    private ActivityAppliedJobsBinding binding;
    private AppliedJobViewModel appliedJobViewModel;
    private ApplicationAdapter applicationAdapter;
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
        binding.backButton.setOnClickListener(v -> {
            finish();
        });
        appliedJobViewModel.getAppliedJobs().observe(this, applications -> {
            binding.progressIndicator.hide();
            binding.appliedJobsRecyclerView.setVisibility(View.VISIBLE);
            applicationAdapter = new ApplicationAdapter(AppliedJobsActivity.this, applications, new OnApplicationClicked() {
                @Override
                public void onViewJob(Job job) {
                    Intent intent = new Intent(AppliedJobsActivity.this, JobDetailsActivity.class);
                    intent.putExtra("job", job);
                    startActivity(intent);
                }

                @Override
                public void onOpenResume(String resumeUrl) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(resumeUrl));
                    startActivity(browserIntent);
                }
            });
            binding.appliedJobsRecyclerView.setLayoutManager(new LinearLayoutManager(AppliedJobsActivity.this, LinearLayoutManager.VERTICAL, false));
            binding.appliedJobsRecyclerView.setAdapter(applicationAdapter);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.progressIndicator.show();
        binding.appliedJobsRecyclerView.setVisibility(View.GONE);
        appliedJobViewModel.fetchAppliedJobs();
    }
}