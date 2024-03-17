package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.databinding.FragmentCompanyProfileBinding;
import com.example.final_project.utils.GlobalConstants;
import com.example.final_project.viewmodel.CompanyHomeViewModel;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyProfileFragment extends Fragment {

    private FragmentCompanyProfileBinding binding;
    private CompanyHomeViewModel companyHomeViewModel;
    public CompanyProfileFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompanyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompanyProfileFragment newInstance(String param1, String param2) {
        CompanyProfileFragment fragment = new CompanyProfileFragment();
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
        binding = FragmentCompanyProfileBinding.inflate(inflater, container, false);
        initVM();
        initView();
        return binding.getRoot();
    }

    private void initVM(){
        companyHomeViewModel = new ViewModelProvider(requireActivity()).get(CompanyHomeViewModel.class);
    }

    private void initView() {
        companyHomeViewModel.getCurrentCompany().observe(getViewLifecycleOwner(), company -> {
            binding.profileName.setText(company.getName());
            if(company.getImage() == null){
                Picasso.get().load(GlobalConstants.defaultCompanyLogoUrl).into(binding.profileImage);
            }
            else{
                Picasso.get().load(company.getImage()).into(binding.profileImage);
            }
        });
        binding.logoutButton.setOnClickListener(v -> {
            companyHomeViewModel.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}