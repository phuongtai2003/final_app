package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.adapter.JobAdapter;
import com.example.final_project.databinding.FragmentCompanyHomeBinding;
import com.example.final_project.models.Job;
import com.example.final_project.utils.OnChangeBottomNavIndex;
import com.example.final_project.utils.OnJobClickedListener;
import com.example.final_project.viewmodel.CompanyHomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyHomeFragment extends Fragment {
    private FragmentCompanyHomeBinding binding;
    private CompanyHomeViewModel companyHomeViewModel;
    private JobAdapter jobAdapter;
    private OnChangeBottomNavIndex onChangeBottomNavIndex;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnChangeBottomNavIndex){
            onChangeBottomNavIndex = (OnChangeBottomNavIndex) context;
        }
        else{
            throw new RuntimeException(context.toString() + " must implement OnChangeBottomNavIndex");
        }
    }

    public CompanyHomeFragment() {
    }

    public static CompanyHomeFragment newInstance(String param1, String param2) {
        CompanyHomeFragment fragment = new CompanyHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCompanyHomeBinding.inflate(inflater, container, false);
        initVM();
        initView();
        return binding.getRoot();
    }

    private void initVM(){
        companyHomeViewModel = new ViewModelProvider(requireActivity()).get(CompanyHomeViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView(){
        binding.viewAllJobsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangeBottomNavIndex.onChangeIndex(1, 0, null);
            }
        });
        binding.addNewJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddJobActivity.class);
                startActivity(intent);
            }
        });
        binding.companyHomeFragmentLinearLayout.setVisibility(View.GONE);
        binding.progressIndicator.setVisibility(View.VISIBLE);
        companyHomeViewModel.getCurrentCompany().observe(getViewLifecycleOwner(), company -> {
            if(company != null){
                binding.companyNameTxt.setText(company.getName());
            }
        });
        companyHomeViewModel.getJobs().observe(getViewLifecycleOwner(), jobs -> {
            binding.companyHomeFragmentLinearLayout.setVisibility(View.GONE);
            binding.progressIndicator.setVisibility(View.VISIBLE);
            if(jobs != null){
                if(jobs.isEmpty()){
                    binding.companyHomeFragmentLinearLayout.setVisibility(View.VISIBLE);
                    binding.progressIndicator.setVisibility(View.GONE);
                    binding.noJobsLayout.setVisibility(View.VISIBLE);
                    binding.jobListPagerView.setVisibility(View.GONE);
                }
                else{
                    binding.noJobsLayout.setVisibility(View.GONE);
                    binding.jobListPagerView.setVisibility(View.VISIBLE);
                    binding.companyHomeFragmentLinearLayout.setVisibility(View.VISIBLE);
                    binding.progressIndicator.setVisibility(View.GONE);
                    jobAdapter = new JobAdapter(getActivity(), jobs, R.layout.job_item_layout, new OnJobClickedListener() {
                        @Override
                        public void onJobClicked(Job job) {
                            Intent intent = new Intent(getActivity(), CompanyJobDetailsActivity.class);
                            intent.putExtra("job", job);
                            startActivity(intent);
                        }
                    });
                    binding.jobListPagerView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                    binding.jobListPagerView.setPageTransformer(new ViewPager2.PageTransformer() {
                        @Override
                        public void transformPage(@NonNull View page, float position) {
                            float absPos = Math.abs(position);
                            page.setAlpha(1 - absPos);
                        }
                    });
                    binding.jobListPagerView.setAdapter(jobAdapter);
                }
            }
        });

        binding.addJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddJobActivity.class);
                startActivity(intent);
            }
        });
    }
}