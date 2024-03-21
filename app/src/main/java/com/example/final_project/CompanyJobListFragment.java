package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.adapter.JobAdapter;
import com.example.final_project.databinding.FragmentCompanyJobListBinding;
import com.example.final_project.models.Job;
import com.example.final_project.utils.OnJobClickedListener;
import com.example.final_project.viewmodel.CompanyHomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyJobListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyJobListFragment extends Fragment {
    private FragmentCompanyJobListBinding binding;
    private CompanyHomeViewModel companyHomeViewModel;
    private JobAdapter jobAdapter;
    public CompanyJobListFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompanyJobListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompanyJobListFragment newInstance(String param1, String param2) {
        CompanyJobListFragment fragment = new CompanyJobListFragment();
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
        binding = FragmentCompanyJobListBinding.inflate(inflater, container, false);
        initVM();
        initView();
        return binding.getRoot();
    }

    private void initVM(){
        companyHomeViewModel = new ViewModelProvider(requireActivity()).get(CompanyHomeViewModel.class);
    }
    private void initView(){
        companyHomeViewModel.getJobs().observe(getViewLifecycleOwner(), jobs ->{
            jobAdapter = new JobAdapter(requireContext(), jobs, R.layout.search_job_item, new OnJobClickedListener() {
                @Override
                public void onJobClicked(Job job) {
                    Intent intent = new Intent(requireContext(), CompanyJobDetailsActivity.class);
                    intent.putExtra("job", job);
                    startActivity(intent);
                }
            });
            binding.companyJobListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
            binding.companyJobListRecyclerView.setAdapter(jobAdapter);
        });
    }
}