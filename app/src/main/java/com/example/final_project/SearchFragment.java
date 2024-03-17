package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.final_project.adapter.JobAdapter;
import com.example.final_project.adapter.StringSpinnerAdapter;
import com.example.final_project.databinding.FragmentSearchBinding;
import com.example.final_project.models.Job;
import com.example.final_project.models.JobCategory;
import com.example.final_project.utils.GlobalConstants;
import com.example.final_project.utils.OnJobClickedListener;
import com.example.final_project.viewmodel.HomeViewModel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    private FragmentSearchBinding fragmentSearchBinding;
    private HomeViewModel homeViewModel;
    private StringSpinnerAdapter jobCategoryAdapter;
    private StringSpinnerAdapter experienceAdapter;
    private StringSpinnerAdapter salaryAdapter;
    private JobAdapter jobAdapter;
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        fragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false);
        initVM();
        initView();
        return fragmentSearchBinding.getRoot();
    }

    private void initVM() {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            Bundle args = getArguments();
            if(args != null){
                String jobCategory = args.getString("jobCategory");
                if(homeViewModel.getJobCategories().getValue() != null && jobCategory != null){
                    fragmentSearchBinding.jobFilterLayout.setVisibility(View.VISIBLE);
                    int index = homeViewModel.getJobCategories().getValue().stream().map(JobCategory::getName).collect(Collectors.toList()).indexOf(jobCategory);
                    fragmentSearchBinding.jobCategorySpinner.setSelection(index);
                }
                args.clear();
            }
        }

    }

    private void initView(){
        experienceAdapter = new StringSpinnerAdapter(GlobalConstants.experienceLevels, requireContext());
        fragmentSearchBinding.experienceLevelSpinner.setAdapter(experienceAdapter);
        salaryAdapter = new StringSpinnerAdapter(GlobalConstants.salaryRanges, requireContext());
        fragmentSearchBinding.salarySpinner.setAdapter(salaryAdapter);
        homeViewModel.getJobCategories().observe(getViewLifecycleOwner(), jobCategories -> {
            if (jobCategories != null) {
                List<String> categoriesNames = jobCategories.stream().map(JobCategory::getName).collect(Collectors.toList());
                jobCategoryAdapter = new StringSpinnerAdapter(categoriesNames, requireContext());
                fragmentSearchBinding.jobCategorySpinner.setAdapter(jobCategoryAdapter);
                fragmentSearchBinding.jobCategorySpinner.setSelection(categoriesNames.indexOf("View All"));
            }
        });
        homeViewModel.getFilteredJobs().observe(getViewLifecycleOwner(), jobs -> {
            if (jobs != null) {
                jobAdapter = new JobAdapter(requireContext(), jobs, R.layout.search_job_item, new OnJobClickedListener() {
                    @Override
                    public void onJobClicked(Job job) {
                        Intent intent = new Intent(requireActivity(), JobDetailsActivity.class);
                        intent.putExtra("job", job);
                        startActivity(intent);
                    }
                });
                fragmentSearchBinding.filteredJobsList.setLayoutManager(new LinearLayoutManager(requireContext()));
                fragmentSearchBinding.filteredJobsList.setHasFixedSize(true);
                fragmentSearchBinding.filteredJobsList.setAdapter(jobAdapter);
            }
        });
        fragmentSearchBinding.filterButton.setOnClickListener(v -> {
            if(fragmentSearchBinding.jobFilterLayout.getVisibility() == View.VISIBLE)
                fragmentSearchBinding.jobFilterLayout.setVisibility(View.GONE);
            else
                fragmentSearchBinding.jobFilterLayout.setVisibility(View.VISIBLE);
        });

        fragmentSearchBinding.jobTitleEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String currentCategory = fragmentSearchBinding.jobCategorySpinner.getSelectedItem().toString();
                String currentExperience = fragmentSearchBinding.experienceLevelSpinner.getSelectedItem().toString();
                String currentSalary = fragmentSearchBinding.salarySpinner.getSelectedItem().toString();
                String currentName = editable.toString();
                homeViewModel.filterJobs(currentCategory, currentExperience, currentSalary, currentName);
            }
        });

        fragmentSearchBinding.jobCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String currentCategory = jobCategoryAdapter.getItem(i).toString();
                String currentExperience = fragmentSearchBinding.experienceLevelSpinner.getSelectedItem().toString();
                String currentSalary = fragmentSearchBinding.salarySpinner.getSelectedItem().toString();
                String currentName = fragmentSearchBinding.jobTitleEdt.getText().toString();
                homeViewModel.filterJobs(currentCategory, currentExperience, currentSalary, currentName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fragmentSearchBinding.experienceLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String currentCategory = fragmentSearchBinding.jobCategorySpinner.getSelectedItem().toString();
                String currentExperience = experienceAdapter.getItem(i).toString();
                String currentSalary = fragmentSearchBinding.salarySpinner.getSelectedItem().toString();
                String currentName = fragmentSearchBinding.jobTitleEdt.getText().toString();
                homeViewModel.filterJobs(currentCategory, currentExperience, currentSalary, currentName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fragmentSearchBinding.salarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String currentCategory = fragmentSearchBinding.jobCategorySpinner.getSelectedItem().toString();
                String currentExperience = fragmentSearchBinding.experienceLevelSpinner.getSelectedItem().toString();
                String currentSalary = salaryAdapter.getItem(i).toString();
                String currentName = fragmentSearchBinding.jobTitleEdt.getText().toString();
                homeViewModel.filterJobs(currentCategory, currentExperience, currentSalary, currentName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}