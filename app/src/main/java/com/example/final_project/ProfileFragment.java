package com.example.final_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.databinding.FragmentProfileBinding;
import com.example.final_project.models.User;
import com.example.final_project.utils.GlobalConstants;
import com.example.final_project.viewmodel.HomeViewModel;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private HomeViewModel homeViewModel;
    private final int PICK_IMAGE_REQUEST = 30;
    private final int UPDATE_PROFILE_REQUEST = 40;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        initVM();
        initUI();
        return binding.getRoot();
    }

    private void initVM(){
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            binding.profileName.setText(user.getName());
            if(user.getImage() == null){
                Picasso.get().load(GlobalConstants.defaultUserImageUrl).into(binding.profileImage);
            }
            else{
                Picasso.get().load(user.getImage()).into(binding.profileImage);
            }
        });
    }

    private void initUI(){
        binding.showAppliedJobsButton.setOnClickListener(v -> {
            Intent appliedJobsIntent = new Intent(getActivity(), AppliedJobsActivity.class);
            startActivity(appliedJobsIntent);
        });
        binding.logoutButton.setOnClickListener(v -> {
            homeViewModel.signOut();
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
        });
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
                pickImageIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(pickImageIntent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        binding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateProfileIntent = new Intent(getActivity(), UpdateProfileActivity.class);
                User user = homeViewModel.getCurrentUser().getValue();
                updateProfileIntent.putExtra("user", user);
                startActivityForResult(updateProfileIntent, UPDATE_PROFILE_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            binding.profileLayout.setVisibility(View.GONE);
            binding.progressIndicator.show();
            homeViewModel.uploadImage(data.getData(), (result, imageUrl) -> {
                if(result){
                    Picasso.get().load(imageUrl).into(binding.profileImage);
                    User user = homeViewModel.getCurrentUser().getValue();
                    if(user != null){
                        user.setImage(imageUrl);
                        homeViewModel.setCurrentUser(user);
                    }
                }
                binding.progressIndicator.hide();
                binding.profileLayout.setVisibility(View.VISIBLE);
            });
        }
        if(requestCode == UPDATE_PROFILE_REQUEST && resultCode == Activity.RESULT_OK && data != null){
            User user = data.getParcelableExtra("user");
            homeViewModel.setCurrentUser(user);
        }
    }
}