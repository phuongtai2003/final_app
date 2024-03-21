package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.example.final_project.databinding.ActivityUpdateCompanyBinding;
import com.example.final_project.models.Company;
import com.example.final_project.models.Resume;
import com.example.final_project.models.User;
import com.example.final_project.utils.CompanyProfileViewModelFactory;
import com.example.final_project.utils.ProfileFireStoreResult;
import com.example.final_project.utils.UIHelpers;
import com.example.final_project.viewmodel.CompanyProfileViewModel;

import java.util.List;

public class UpdateCompanyActivity extends AppCompatActivity {
    private ActivityUpdateCompanyBinding binding;
    private CompanyProfileViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initVM();
        initView();
    }

    private void initVM(){
        Company company = getIntent().getParcelableExtra("company");
        CompanyProfileViewModelFactory factory = new CompanyProfileViewModelFactory(company);
        viewModel = new ViewModelProvider(this, factory).get(CompanyProfileViewModel.class);
    }

    private void initView(){
        binding.backButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("company", viewModel.getCompany().getValue());
            setResult(RESULT_OK, intent);
            finish();
        });
        viewModel.getCompany().observe(this, company -> {
            binding.companyNameEdt.setText(company.getName());
            binding.companyAddressEdt.setText(company.getAddress());
            binding.companyPhoneEdt.setText(company.getPhone());
        });
        binding.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressIndicator.setVisibility(View.VISIBLE);
                binding.updateCompanyLayout.setVisibility(View.GONE);
                Company company = viewModel.getCompany().getValue();
                company.setName(binding.companyNameEdt.getText().toString());
                company.setAddress(binding.companyAddressEdt.getText().toString());
                company.setPhone(binding.companyPhoneEdt.getText().toString());
                viewModel.updateCompany(company, new ProfileFireStoreResult() {

                    @Override
                    public void onUpdateProfileResult(boolean result, User user) {

                    }

                    @Override
                    public void onUpdateCompanyProfileResult(boolean result, Company user) {
                        binding.progressIndicator.setVisibility(View.GONE);
                        binding.updateCompanyLayout.setVisibility(View.VISIBLE);
                        if (result) {
                            viewModel.setCompany(user);
                            UIHelpers.showDialog(Gravity.CENTER, "Success", UpdateCompanyActivity.this);
                        }
                        else{
                            UIHelpers.showDialog(Gravity.CENTER, "Something went wrong", UpdateCompanyActivity.this);
                        }
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