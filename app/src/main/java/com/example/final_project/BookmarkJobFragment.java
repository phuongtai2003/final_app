package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.adapter.JobAdapter;
import com.example.final_project.databinding.FragmentBookmarkJobBinding;
import com.example.final_project.models.Job;
import com.example.final_project.utils.OnJobClickedListener;
import com.example.final_project.viewmodel.HomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookmarkJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookmarkJobFragment extends Fragment {
    private FragmentBookmarkJobBinding binding;
    private HomeViewModel homeViewModel;
    private JobAdapter jobAdapter;

    public BookmarkJobFragment() {
    }

    public static BookmarkJobFragment newInstance(String param1, String param2) {
        BookmarkJobFragment fragment = new BookmarkJobFragment();
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
        binding = FragmentBookmarkJobBinding.inflate(inflater, container, false);
        initViewModel();
        initView();
        return binding.getRoot();
    }

    private void initViewModel() {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }
    private void initView(){
        homeViewModel.getBookmarkedJobs().observe(getViewLifecycleOwner(), jobs -> {
            if(jobs != null){
                jobAdapter = new JobAdapter(requireContext(), jobs, R.layout.search_job_item, new OnJobClickedListener() {
                    @Override
                    public void onJobClicked(Job job) {
                        Intent intent = new Intent(requireContext(), JobDetailsActivity.class);
                        intent.putExtra("job", job);
                        startActivity(intent);
                    }
                });
                binding.bookmarkJobRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                binding.bookmarkJobRecyclerView.setAdapter(jobAdapter);
            }
        });
    }
}