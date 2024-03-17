package com.example.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.adapter.JobAdapter;
import com.example.final_project.adapter.JobCategoryAdapter;
import com.example.final_project.adapter.PopularCompaniesAdapter;
import com.example.final_project.databinding.FragmentHomeBinding;
import com.example.final_project.models.Job;
import com.example.final_project.utils.OnChangeBottomNavIndex;
import com.example.final_project.utils.OnJobClickedListener;
import com.example.final_project.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {
    private JobCategoryAdapter jobCategoryAdapter;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding fragmentHomeBinding;
    private PopularCompaniesAdapter popularCompaniesAdapter;
    private JobAdapter jobAdapter;
    private OnChangeBottomNavIndex onChangeBottomNavIndex;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnChangeBottomNavIndex){
            onChangeBottomNavIndex = (OnChangeBottomNavIndex) context;
        }
    }

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        initViewModel();
        initView();
        return fragmentHomeBinding.getRoot();
    }

    private void initViewModel() {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    private void initView(){
        fragmentHomeBinding.homeFragmentLinearLayout.setVisibility(View.GONE);
        fragmentHomeBinding.progressIndicator.setVisibility(View.VISIBLE);
        homeViewModel.getJobCategories().observe(getViewLifecycleOwner(), jobCategories -> {
            if(jobCategories.size() > 0){
                jobCategoryAdapter = new JobCategoryAdapter(jobCategories, requireContext(), jobCategoryName -> {
                    onChangeBottomNavIndex.onChangeIndex(1, 0, jobCategoryName);
                });
                fragmentHomeBinding.jobCategoryHorizontalRecyclerView.setHasFixedSize(true);
                fragmentHomeBinding.jobCategoryHorizontalRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                fragmentHomeBinding.jobCategoryHorizontalRecyclerView.setAdapter(jobCategoryAdapter);
            }
        });
        homeViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if(user != null){
                fragmentHomeBinding.seekerNameTxt.setText(user.getName());
            }
        });
        homeViewModel.getCompanies().observe(getViewLifecycleOwner(), companies -> {
            if(!companies.isEmpty()){
                popularCompaniesAdapter = new PopularCompaniesAdapter(companies, requireContext());
                fragmentHomeBinding.companiesViewPager.setClipToPadding(false);
                fragmentHomeBinding.companiesViewPager.setPageTransformer(new ViewPager2.PageTransformer() {
                    @Override
                    public void transformPage(@NonNull View page, float position) {
                        float absPos = Math.abs(position);
                        page.setAlpha(1 - absPos);
                    }
                });
                fragmentHomeBinding.companiesViewPager.setAdapter(popularCompaniesAdapter);
            }
        });
        homeViewModel.getJobs().observe(getViewLifecycleOwner(), jobs -> {
            if(!jobs.isEmpty()){
                jobAdapter = new JobAdapter(requireContext(), jobs, R.layout.job_item_layout, new OnJobClickedListener() {
                    @Override
                    public void onJobClicked(Job job) {
                        Intent intent = new Intent(requireActivity(), JobDetailsActivity.class);
                        intent.putExtra("job", job);
                        startActivity(intent);
                    }
                });
                fragmentHomeBinding.homeFragmentLinearLayout.setVisibility(View.VISIBLE);
                fragmentHomeBinding.progressIndicator.setVisibility(View.GONE);
                fragmentHomeBinding.jobsViewPager.setPageTransformer(new ViewPager2.PageTransformer() {
                    @Override
                    public void transformPage(@NonNull View page, float position) {
                        float absPos = Math.abs(position);
                        page.setAlpha(1 - absPos);
                    }
                });
                fragmentHomeBinding.jobsViewPager.setClipToPadding(false);
                fragmentHomeBinding.jobsViewPager.setAdapter(jobAdapter);
            }
        });
    }
}