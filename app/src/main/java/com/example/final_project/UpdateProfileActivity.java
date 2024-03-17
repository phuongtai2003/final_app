package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.final_project.databinding.ActivityUpdateProfileBinding;
import com.example.final_project.models.Resume;
import com.example.final_project.models.User;
import com.example.final_project.utils.ProfileFireStoreResult;
import com.example.final_project.utils.ProfileViewModelFactory;
import com.example.final_project.viewmodel.ProfileViewModel;

import java.util.List;

public class UpdateProfileActivity extends AppCompatActivity {
    private ActivityUpdateProfileBinding binding;
    private ProfileViewModel profileViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViewModel();
        initView();
    }

    private void initViewModel(){
        User user = getIntent().getParcelableExtra("user");
        ProfileViewModelFactory profileViewModelFactory = new ProfileViewModelFactory(user);
        profileViewModel = new ViewModelProvider(this, profileViewModelFactory).get(ProfileViewModel.class);
    }

    private void initView(){
        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("user", profileViewModel.getCurrentUser().getValue());
            setResult(RESULT_OK, intent);
            finish();
        });
        binding.cvBtn.setOnClickListener(v -> {
            ResumeBottomSheetDialog resumeBottomSheetDialog = new ResumeBottomSheetDialog(profileViewModel);
            resumeBottomSheetDialog.show(getSupportFragmentManager(), "resumeBottomSheetDialog");
        });
        profileViewModel.getCurrentUser().observe(this, user -> {
            binding.nameEdt.setText(user.getName());
            binding.phoneEdt.setText(user.getPhone());
        });

        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressIndicator.setVisibility(View.VISIBLE);
                binding.updateLayout.setVisibility(View.GONE);
                String name = binding.nameEdt.getText().toString();
                String phone = binding.phoneEdt.getText().toString();
                profileViewModel.updateProfile(name, phone, new ProfileFireStoreResult() {
                    @Override
                    public void onUpdateProfileResult(boolean result, User user) {
                        if (result) {
                            profileViewModel.setUser(user);
                        }
                        binding.progressIndicator.setVisibility(View.GONE);
                        binding.updateLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onUploadResumeResult(boolean result, Resume resume) {

                    }

                    @Override
                    public void onFetchResumesResult(boolean result, List<Resume> resumes) {

                    }
                });
            }
        });
    }
}