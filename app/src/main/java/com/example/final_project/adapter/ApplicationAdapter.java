package com.example.final_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.models.Application;
import com.example.final_project.models.Job;
import com.example.final_project.utils.GlobalConstants;
import com.example.final_project.utils.OnApplicationClicked;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {
    private Context mContext;
    private List<Application> applications;
    private OnApplicationClicked onApplicationClicked;

    public ApplicationAdapter(Context mContext, List<Application> applications, OnApplicationClicked onApplicationClicked) {
        this.mContext = mContext;
        this.applications = applications;
        this.onApplicationClicked = onApplicationClicked;
    }

    class ApplicationViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView companyLogo;
        TextView jobTitle;
        TextView companyName;
        TextView salary;
        TextView jobExperience;
        TextView status;

        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            companyLogo = itemView.findViewById(R.id.companyLogoImg);
            jobTitle = itemView.findViewById(R.id.jobTitleTxt);
            companyName = itemView.findViewById(R.id.companyNameTxt);
            salary = itemView.findViewById(R.id.salaryTxt);
            jobExperience = itemView.findViewById(R.id.experienceTxt);
            status = itemView.findViewById(R.id.statusTxt);
        }
    }


    @NonNull
    @Override
    public ApplicationAdapter.ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.application_layout, parent, false);
        return new ApplicationAdapter.ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapter.ApplicationViewHolder holder, int position) {
        Application application = applications.get(position);
        holder.jobTitle.setText(application.getJob().getJobTitle());
        holder.companyName.setText(application.getJob().getCompany().getName());
        holder.salary.setText(application.getJob().getJobSalary());
        holder.jobExperience.setText(application.getJob().getJobExperience());
        String statusText = application.getStatus().substring(0, 1).toUpperCase() + application.getStatus().substring(1);
        holder.status.setText(statusText);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.inflate(R.menu.on_application_click_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.viewJobItem){
                            onApplicationClicked.onViewJob(application.getJob());
                        }else if(menuItem.getItemId() == R.id.openResumeItem){
                            onApplicationClicked.onOpenResume(application.getResume().getUrl());
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        if(application.getJob().getCompany().getImage() != null){
            Picasso.get().load(application.getJob().getCompany().getImage()).into(holder.companyLogo);
        }else{
            Picasso.get().load(GlobalConstants.defaultCompanyLogoUrl).into(holder.companyLogo);
        }
    }

    @Override
    public int getItemCount() {
        return applications != null ? applications.size() : 0;
    }
}
