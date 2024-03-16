package com.example.final_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.models.JobCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

public class JobCategoryAdapter extends RecyclerView.Adapter<JobCategoryAdapter.ViewHolder> {
    private List<JobCategory> jobCategories;
    private Context mContext;

    public JobCategoryAdapter(List<JobCategory> jobCategories, Context mContext) {
        this.jobCategories = jobCategories;
        this.mContext = mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView jobCategoryImage;
        TextView jobCategoryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobCategoryImage = itemView.findViewById(R.id.jobCategoryImage);
            jobCategoryName = itemView.findViewById(R.id.jobCategoryName);
        }
    }

    @NonNull
    @Override
    public JobCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.job_category_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobCategoryAdapter.ViewHolder holder, int position) {
        holder.jobCategoryName.setText(jobCategories.get(position).getName());
        Picasso.get().load(jobCategories.get(position).getImage()).into(holder.jobCategoryImage);
    }

    @Override
    public int getItemCount() {
        return jobCategories != null ? jobCategories.size() : 0;
    }
}
