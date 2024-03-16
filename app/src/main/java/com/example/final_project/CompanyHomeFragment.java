package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.databinding.FragmentCompanyHomeBinding;
import com.example.final_project.viewmodel.CompanyHomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyHomeFragment extends Fragment {
    private FragmentCompanyHomeBinding binding;
    private CompanyHomeViewModel companyHomeViewModel;
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
        companyHomeViewModel = new ViewModelProvider(this).get(CompanyHomeViewModel.class);
    }

    private void initView(){
        requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.companyHomeFragmentLinearLayout.setVisibility(View.GONE);
                    binding.progressIndicator.setVisibility(View.VISIBLE);
                }
            });
        companyHomeViewModel.getCurrentCompany().observe(getViewLifecycleOwner(), company -> {
            if(company != null){
                binding.companyHomeFragmentLinearLayout.setVisibility(View.VISIBLE);
                binding.progressIndicator.setVisibility(View.GONE);
                binding.companyNameTxt.setText(company.getName());
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