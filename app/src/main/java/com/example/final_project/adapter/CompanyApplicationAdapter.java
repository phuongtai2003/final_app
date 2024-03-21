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
import com.example.final_project.utils.GlobalConstants;
import com.example.final_project.utils.OnCompanyApplicationClicked;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CompanyApplicationAdapter extends RecyclerView.Adapter<CompanyApplicationAdapter.CompanyApplicationViewHolder> {
    private Context mContext;
    private List<Application> applications;
    private OnCompanyApplicationClicked onCompanyApplicationClicked;

    public CompanyApplicationAdapter(Context mContext, List<Application> applications, OnCompanyApplicationClicked onCompanyApplicationClicked) {
        this.mContext = mContext;
        this.applications = applications;
        this.onCompanyApplicationClicked = onCompanyApplicationClicked;
    }

    public void updateApplication(int position, Application application){
        applications.set(position, application);
        notifyItemChanged(position);
    }

    class CompanyApplicationViewHolder extends RecyclerView.ViewHolder {
        TextView candidateName, jobTitle, status;
        ShapeableImageView companyLogoImg;
        MaterialButton moreOptionBtn;

        public CompanyApplicationViewHolder(@NonNull View itemView)
        {
            super(itemView);
            candidateName = itemView.findViewById(R.id.nameTxt);
            jobTitle = itemView.findViewById(R.id.jobTitleTxt);
            status = itemView.findViewById(R.id.statusTxt);
            companyLogoImg = itemView.findViewById(R.id.companyLogoImg);
            moreOptionBtn = itemView.findViewById(R.id.moreOptionsBtn);
        }
    }


    @NonNull
    @Override
    public CompanyApplicationAdapter.CompanyApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.company_application_layout, parent, false);
        return new CompanyApplicationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyApplicationAdapter.CompanyApplicationViewHolder holder, int position) {
        Application application = applications.get(position);
        holder.candidateName.setText(application.getResume().getUser().getName());
        holder.jobTitle.setText(application.getJob().getJobTitle());
        String statusTxt = application.getStatus().substring(0, 1).toUpperCase() + application.getStatus().substring(1).toLowerCase();
        holder.status.setText(statusTxt);
        if(application.getJob().getCompany().getImage() != null){
            Picasso.get().load(application.getJob().getCompany().getImage()).into(holder.companyLogoImg);
        }
        else{
            Picasso.get().load(GlobalConstants.defaultCompanyLogoUrl).into(holder.companyLogoImg);
        }
        holder.moreOptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.inflate(R.menu.on_company_application_click_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.openResumeItem){
                            onCompanyApplicationClicked.openResume(application.getResume().getUrl());
                            return true;
                        }
                        else if (menuItem.getItemId() == R.id.acceptApplicationItem){
                            onCompanyApplicationClicked.acceptApplication(application);
                            return true;
                        }
                        else if (menuItem.getItemId() == R.id.rejectApplicationItem){
                            onCompanyApplicationClicked.rejectApplication(application);
                            return true;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return applications != null ? applications.size() : 0;
    }
}
