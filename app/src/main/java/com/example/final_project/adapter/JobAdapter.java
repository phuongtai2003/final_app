package com.example.final_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.models.Job;
import com.example.final_project.utils.GlobalConstants;
import com.example.final_project.utils.OnJobClickedListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private Context mContext;
    private List<Job> jobList;
    private int layout;
    private OnJobClickedListener onJobClickedListener;

    public JobAdapter(Context mContext, List<Job> jobList, int layout, OnJobClickedListener onJobClickedListener) {
        this.mContext = mContext;
        this.jobList = jobList;
        this.layout = layout;
        this.onJobClickedListener = onJobClickedListener;
    }

    class JobViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView companyLogo;
        TextView jobTitle;
        TextView companyName;
        TextView salary;
        TextView jobExperience;
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            companyLogo = itemView.findViewById(R.id.companyLogoImg);
            jobTitle = itemView.findViewById(R.id.jobTitleTxt);
            companyName = itemView.findViewById(R.id.companyNameTxt);
            salary = itemView.findViewById(R.id.salaryTxt);
            jobExperience = itemView.findViewById(R.id.experienceTxt);
        }
    }


    @NonNull
    @Override
    public JobAdapter.JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layout, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobAdapter.JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.jobTitle.setText(job.getJobTitle());
        holder.companyName.setText(job.getCompany().getName());
        holder.salary.setText(job.getJobSalary());
        holder.jobExperience.setText(job.getJobExperience());
        holder.itemView.setOnClickListener(v -> {
            if(onJobClickedListener != null){
                onJobClickedListener.onJobClicked(job);
            }
        });
        if(job.getCompany().getImage() != null){
            Picasso.get().load(job.getCompany().getImage()).into(holder.companyLogo);
        }else{
            Picasso.get().load(GlobalConstants.defaultCompanyLogoUrl).into(holder.companyLogo);
        }
    }

    @Override
    public int getItemCount() {
        return jobList != null ? jobList.size() : 0;
    }
}
