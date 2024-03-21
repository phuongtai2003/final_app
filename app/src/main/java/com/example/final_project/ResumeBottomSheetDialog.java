package com.example.final_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.final_project.adapter.ResumeAdapter;
import com.example.final_project.databinding.ResumeBottomSheetLayoutBinding;
import com.example.final_project.models.Company;
import com.example.final_project.models.Resume;
import com.example.final_project.models.User;
import com.example.final_project.utils.ProfileFireStoreResult;
import com.example.final_project.viewmodel.ProfileViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ResumeBottomSheetDialog extends BottomSheetDialogFragment {
    private static final int PICK_RESUME_REQUEST = 20;
    private ProfileViewModel profileViewModel;
    private ResumeBottomSheetLayoutBinding binding;
    private ResumeAdapter resumeAdapter;

    public ResumeBottomSheetDialog(ProfileViewModel profileViewModel){
        this.profileViewModel = profileViewModel;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ResumeBottomSheetLayoutBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView(){
        profileViewModel.getResumes().observe(requireActivity(), resumes -> {
            if(resumes != null){
                binding.resumeRecyclerView.setVisibility(View.VISIBLE);
                binding.noResumeFoundTxt.setVisibility(View.GONE);
                if(resumeAdapter == null){
                    resumeAdapter = new ResumeAdapter(getContext(), resumes, resume -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(resume.getUrl()));
                        startActivity(intent);
                    });
                    binding.resumeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    binding.resumeRecyclerView.setAdapter(resumeAdapter);
                }
                else{
                    resumeAdapter.setResumes(resumes);
                }
            }
            else{
                binding.resumeRecyclerView.setVisibility(View.GONE);
                binding.noResumeFoundTxt.setVisibility(View.VISIBLE);
            }
        });
        binding.addNewResumeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, PICK_RESUME_REQUEST);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_RESUME_REQUEST && resultCode == Activity.RESULT_OK && data!= null){
            Uri pdfUri = data.getData();
            if(pdfUri != null){
                binding.progressIndicator.setVisibility(View.VISIBLE);
                binding.bottomLayout.setVisibility(View.GONE);
                profileViewModel.uploadResume(pdfUri, new ProfileFireStoreResult() {
                    @Override
                    public void onUpdateCompanyProfileResult(boolean result, Company user) {

                    }

                    @Override
                    public void onUpdateProfileResult(boolean result, User user) {

                    }

                    @Override
                    public void onUploadResumeResult(boolean result, Resume resume) {
                        if(result){
                            if(resumeAdapter == null){
                                resumeAdapter = new ResumeAdapter(getContext(), profileViewModel.getResumes().getValue(), resume1 -> {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(resume1.getUrl()));
                                    startActivity(intent);
                                });
                                binding.resumeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                binding.resumeRecyclerView.setAdapter(resumeAdapter);
                            }
                            else{
                                if(profileViewModel.getResumes().getValue() != null){
                                    List<Resume> currentList = new ArrayList<>(profileViewModel.getResumes().getValue());
                                    currentList.add(resume);
                                    resumeAdapter.setResumes(currentList);
                                }
                            }
                        }
                        binding.progressIndicator.setVisibility(View.GONE);
                        binding.bottomLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFetchResumesResult(boolean result, List<Resume> resumes) {

                    }
                });
            }
        }
    }
}
