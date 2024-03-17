package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.final_project.adapter.JobCategoryAdapter;
import com.example.final_project.databinding.ActivityHomeBinding;
import com.example.final_project.utils.OnChangeBottomNavIndex;
import com.example.final_project.viewmodel.HomeViewModel;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity implements OnChangeBottomNavIndex {
    private ActivityHomeBinding activityHomeBinding;
    private HomeViewModel homeViewModel;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityHomeBinding.getRoot());
        initViewModel();
    }

    private void initViewModel() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        for (int i = 0; i < homeViewModel.getFragments().size(); i++) {
            if(i == 0){
                fragmentManager.beginTransaction().add(R.id.tempContainer ,homeViewModel.getFragments().get(0)).commit();
            }
            else{
                Fragment fragment = homeViewModel.getFragments().get(i);
                fragmentManager.beginTransaction().add(R.id.tempContainer ,fragment).hide(fragment).commit();
            }
        }

        activityHomeBinding.bottomNavbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = homeViewModel.getCurrentFragment().getValue();
                if(item.getItemId() == R.id.home){
                    fragmentManager.beginTransaction().hide(fragment).show(homeViewModel.getFragments().get(0)).commit();
                    homeViewModel.setCurrentFragment(0);
                }
                else if(item.getItemId() == R.id.search){
                    fragmentManager.beginTransaction().hide(fragment).show(homeViewModel.getFragments().get(1)).commit();
                    homeViewModel.setCurrentFragment(1);
                }
                else if(item.getItemId() == R.id.bookmark){
                    fragmentManager.beginTransaction().hide(fragment).show(homeViewModel.getFragments().get(2)).commit();
                    homeViewModel.setCurrentFragment(2);
                }
                else if(item.getItemId() == R.id.profile){
                    fragmentManager.beginTransaction().hide(fragment).show(homeViewModel.getFragments().get(3)).commit();
                    homeViewModel.setCurrentFragment(3);
                }
                return true;
            }
        });
    }

    @Override
    public void onChangeIndex(int index, int tabIndex, String jobCategory) {
        Fragment fragment = homeViewModel.getCurrentFragment().getValue();
        if(index == 0){
            fragmentManager.beginTransaction().hide(fragment).show(homeViewModel.getFragments().get(0)).commit();
            homeViewModel.setCurrentFragment(0);
            activityHomeBinding.bottomNavbar.setSelectedItemId(R.id.home);
        }
        else if(index == 1){
            Bundle bundle = new Bundle();
            bundle.putString("jobCategory", jobCategory);
            Fragment fragmentChange = homeViewModel.getFragments().get(1);
            fragmentChange.setArguments(bundle);
            fragmentManager.beginTransaction().hide(fragment).show(fragmentChange).commit();
            homeViewModel.setCurrentFragment(1);
            activityHomeBinding.bottomNavbar.setSelectedItemId(R.id.search);
        }
        else if(index == 2){
            fragmentManager.beginTransaction().hide(fragment).show(homeViewModel.getFragments().get(2)).commit();
            homeViewModel.setCurrentFragment(2);
            activityHomeBinding.bottomNavbar.setSelectedItemId(R.id.bookmark);
        }
        else if(index == 3){
            fragmentManager.beginTransaction().hide(fragment).show(homeViewModel.getFragments().get(3)).commit();
            homeViewModel.setCurrentFragment(3);
            activityHomeBinding.bottomNavbar.setSelectedItemId(R.id.profile);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        homeViewModel.fetchBookmarkJobs();
        homeViewModel.fetchGetJobs();
    }
}