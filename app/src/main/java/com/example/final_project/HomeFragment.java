package com.example.final_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.adapter.JobCategoryAdapter;
import com.example.final_project.adapter.PopularCompaniesAdapter;
import com.example.final_project.databinding.FragmentHomeBinding;
import com.example.final_project.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {
    private JobCategoryAdapter jobCategoryAdapter;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding fragmentHomeBinding;
    private PopularCompaniesAdapter popularCompaniesAdapter;
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
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    private void initView(){
        requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fragmentHomeBinding.homeFragmentLinearLayout.setVisibility(View.GONE);
                    fragmentHomeBinding.progressIndicator.setVisibility(View.VISIBLE);

                }
            }
        );
        homeViewModel.getJobCategories().observe(getViewLifecycleOwner(), jobCategories -> {
            if(jobCategories.size() > 0){
                jobCategoryAdapter = new JobCategoryAdapter(jobCategories, requireContext());
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
                requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fragmentHomeBinding.homeFragmentLinearLayout.setVisibility(View.VISIBLE);
                            fragmentHomeBinding.progressIndicator.setVisibility(View.GONE);

                        }
                    }
                );

                fragmentHomeBinding.companiesViewPager.setAdapter(popularCompaniesAdapter);
            }
        });
    }
}