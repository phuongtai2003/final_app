package com.example.final_project;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.final_project.adapter.ResumeAdapter;
import com.example.final_project.databinding.PickResumeDialogFragmentBinding;
import com.example.final_project.models.Resume;
import com.example.final_project.utils.JobDetailsFireStoreResult;
import com.example.final_project.utils.OnResumeClick;
import com.example.final_project.utils.UIHelpers;
import com.example.final_project.viewmodel.JobDetailsViewModel;

public class PickResumeDialogFragment extends DialogFragment {
    private JobDetailsViewModel jobDetailsViewModel;
    private PickResumeDialogFragmentBinding binding;
    private ResumeAdapter resumeAdapter;
    public PickResumeDialogFragment(JobDetailsViewModel jobDetailsViewModel) {
        this.jobDetailsViewModel = jobDetailsViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PickResumeDialogFragmentBinding.inflate(inflater, container, false);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initView();
        return binding.getRoot();
    }

    private void initView(){
        jobDetailsViewModel.getResumes().observe(requireActivity(), resumes -> {
            if(resumes != null){
                resumeAdapter = new ResumeAdapter(requireContext(), resumes, new OnResumeClick() {
                    @Override
                    public void onResumeClick(Resume resume) {
                        binding.pickResumeDialogLayout.setVisibility(View.GONE);
                        binding.progressIndicator.setVisibility(View.VISIBLE);
                        jobDetailsViewModel.applyJob(resume, new JobDetailsFireStoreResult() {
                            @Override
                            public void onBookmarkJobResult(boolean isBookmarked) {

                            }

                            @Override
                            public void onApplyJobResult(boolean isApplied) {
                                if(isApplied){
                                    UIHelpers.showDialog(Gravity.CENTER, "You have successfully applied for the job",  requireContext());
                                    dismiss();
                                }
                                else {
                                    UIHelpers.showDialog(Gravity.CENTER, "Something went wrong",  requireContext());
                                }
                                binding.pickResumeDialogLayout.setVisibility(View.VISIBLE);
                                binding.progressIndicator.setVisibility(View.GONE);
                            }
                        });
                    }
                });
                binding.resumeListView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                binding.resumeListView.setAdapter(resumeAdapter);
                binding.resumeListView.setHasFixedSize(true);
            }
        });
    }
}
