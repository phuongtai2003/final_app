package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.final_project.adapter.CompanyApplicationAdapter;
import com.example.final_project.databinding.ActivityViewCandidatesBinding;
import com.example.final_project.models.Application;
import com.example.final_project.models.Job;
import com.example.final_project.utils.JobDetailsFireStoreResult;
import com.example.final_project.utils.JobDetailsViewModelFactory;
import com.example.final_project.utils.OnCompanyApplicationClicked;
import com.example.final_project.viewmodel.ViewCandidatesViewModel;

public class ViewCandidatesActivity extends AppCompatActivity {
    private ActivityViewCandidatesBinding binding;
    private ViewCandidatesViewModel viewModel;
    private CompanyApplicationAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewCandidatesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initVM();
        initView();
    }

    private void initVM(){
        Job job = getIntent().getParcelableExtra("job");
        viewModel = new ViewModelProvider(this, new JobDetailsViewModelFactory(job)).get(ViewCandidatesViewModel.class);
    }

    private void initView(){
        binding.progressIndicator.show();
        binding.candidatesRecyclerView.setVisibility(View.GONE);
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
        viewModel.getApplications().observe(this, applications -> {
            if(applications != null){
                binding.progressIndicator.hide();
                binding.candidatesRecyclerView.setVisibility(View.VISIBLE);
                adapter = new CompanyApplicationAdapter(this, applications, new OnCompanyApplicationClicked() {
                    @Override
                    public void openResume(String resumeUrl) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resumeUrl));
                        startActivity(intent);
                    }

                    @Override
                    public void acceptApplication(Application application) {
                        binding.progressIndicator.show();
                        binding.candidatesRecyclerView.setVisibility(View.GONE);
                        viewModel.acceptApplication(application, new JobDetailsFireStoreResult() {
                            @Override
                            public void onBookmarkJobResult(boolean isBookmarked) {

                            }

                            @Override
                            public void onApplyJobResult(boolean isApplied) {
                                binding.progressIndicator.hide();
                                binding.candidatesRecyclerView.setVisibility(View.VISIBLE);
                                application.setStatus("accepted");
                                adapter.updateApplication(applications.indexOf(application), application);
                            }
                        });
                    }

                    @Override
                    public void rejectApplication(Application application) {
                        binding.progressIndicator.show();
                        binding.candidatesRecyclerView.setVisibility(View.GONE);
                        viewModel.rejectApplication(application, new JobDetailsFireStoreResult() {
                            @Override
                            public void onBookmarkJobResult(boolean isBookmarked) {

                            }

                            @Override
                            public void onApplyJobResult(boolean isApplied) {
                                binding.progressIndicator.hide();
                                binding.candidatesRecyclerView.setVisibility(View.VISIBLE);
                                application.setStatus("rejected");
                                adapter.updateApplication(applications.indexOf(application), application);
                            }
                        });
                    }
                });
                binding.candidatesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                binding.candidatesRecyclerView.setAdapter(adapter);
            }
        });
    }
}