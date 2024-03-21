package com.example.final_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.databinding.FragmentCompanyProfileBinding;
import com.example.final_project.models.Company;
import com.example.final_project.models.User;
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
    private static final int UPDATE_COMPANY_REQUEST_CODE = 21;
    private static final int PICK_IMAGE_REQUEST = 30;

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
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
                pickImageIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(pickImageIntent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        companyHomeViewModel.getCurrentCompany().observe(getViewLifecycleOwner(), company -> {
            binding.profileName.setText(company.getName());
            if(company.getImage() == null){
                Picasso.get().load(GlobalConstants.defaultCompanyLogoUrl).into(binding.profileImage);
            }
            else{
                Picasso.get().load(company.getImage()).into(binding.profileImage);
            }
        });
        binding.editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UpdateCompanyActivity.class);
            intent.putExtra("company", companyHomeViewModel.getCurrentCompany().getValue());
            startActivityForResult(intent, UPDATE_COMPANY_REQUEST_CODE);
        });
        binding.logoutButton.setOnClickListener(v -> {
            companyHomeViewModel.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_COMPANY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if(data != null){
                Company company = data.getParcelableExtra("company");
                companyHomeViewModel.setCurrentCompany(company);
            }
        }
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            binding.profileLayout.setVisibility(View.GONE);
            binding.progressIndicator.show();
            companyHomeViewModel.uploadImage(data.getData(), (result, imageUrl) -> {
                if(result){
                    Picasso.get().load(imageUrl).into(binding.profileImage);
                    Company company = companyHomeViewModel.getCurrentCompany().getValue();
                    if(company != null){
                        company.setImage(imageUrl);
                        companyHomeViewModel.setCurrentCompany(company);
                    }
                }
                binding.progressIndicator.hide();
                binding.profileLayout.setVisibility(View.VISIBLE);
            });


        }
    }
}