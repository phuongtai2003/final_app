package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.final_project.databinding.ActivityCompanyHomeBinding;
import com.example.final_project.utils.OnChangeBottomNavIndex;
import com.example.final_project.viewmodel.CompanyHomeViewModel;
import com.example.final_project.viewmodel.HomeViewModel;
import com.google.android.material.navigation.NavigationBarView;

public class CompanyHomeActivity extends AppCompatActivity implements OnChangeBottomNavIndex {
    private ActivityCompanyHomeBinding binding;
    private CompanyHomeViewModel homeViewModel;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompanyHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViewModel();
    }

    private void initViewModel() {
        homeViewModel = new ViewModelProvider(this).get(CompanyHomeViewModel.class);
        for (int i = 0; i < homeViewModel.getFragments().size(); i++) {
            if(i == 0){
                fragmentManager.beginTransaction().add(R.id.tempContainer ,homeViewModel.getFragments().get(0)).commit();
            }
            else{
                Fragment fragment = homeViewModel.getFragments().get(i);
                fragmentManager.beginTransaction().add(R.id.tempContainer ,fragment).hide(fragment).commit();
            }
        }

        binding.bottomNavbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = homeViewModel.getCurrentFragment().getValue();
                if(item.getItemId() == R.id.home){
                    fragmentManager.beginTransaction().hide(fragment).show(homeViewModel.getFragments().get(0)).commit();
                    homeViewModel.setCurrentFragment(0);
                }
                else if(item.getItemId() == R.id.jobList){
                    fragmentManager.beginTransaction().hide(fragment).show(homeViewModel.getFragments().get(1)).commit();
                    homeViewModel.setCurrentFragment(1);
                }
                else if(item.getItemId() == R.id.profile){
                    fragmentManager.beginTransaction().hide(fragment).show(homeViewModel.getFragments().get(2)).commit();
                    homeViewModel.setCurrentFragment(2);
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        homeViewModel.fetchCompanyJobs();
    }

    @Override
    public void onChangeIndex(int index, int tabIndex, String jobCategory) {
        Fragment fragment = homeViewModel.getCurrentFragment().getValue();
        homeViewModel.setCurrentFragment(index);
        fragmentManager.beginTransaction().hide(fragment).show(homeViewModel.getFragments().get(index)).commit();
        if(index == 0){
            binding.bottomNavbar.setSelectedItemId(R.id.home);
        }else if(index == 1){
            binding.bottomNavbar.setSelectedItemId(R.id.jobList);
        }
        else if(index == 2){
            binding.bottomNavbar.setSelectedItemId(R.id.profile);
        }
    }
}