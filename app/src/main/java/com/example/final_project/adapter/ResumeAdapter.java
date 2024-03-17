package com.example.final_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.models.Resume;
import com.example.final_project.utils.OnResumeClick;

import java.util.List;

public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder> {
    private Context mContext;
    private List<Resume> resumes;
    private OnResumeClick onResumeClick;

    public ResumeAdapter(Context mContext, List<Resume> resumes, OnResumeClick onResumeClick) {
        this.mContext = mContext;
        this.resumes = resumes;
        this.onResumeClick = onResumeClick;
    }

    class ResumeViewHolder extends RecyclerView.ViewHolder {
        TextView resumeName;
        public ResumeViewHolder(@NonNull View itemView) {
            super(itemView);
            resumeName = itemView.findViewById(R.id.resumeName);
        }
    }


    @NonNull
    @Override
    public ResumeAdapter.ResumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.resume_layout, parent, false);
        return new ResumeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResumeAdapter.ResumeViewHolder holder, int position) {
        Resume resume = resumes.get(position);
        holder.resumeName.setText(resume.getName());
        holder.itemView.setOnClickListener(v -> {
            if(onResumeClick != null){
                onResumeClick.onResumeClick(resume);
            }
        });
    }

    public void setResumes(List<Resume> resumes) {
        this.resumes = resumes;
        notifyItemRangeChanged(0, resumes.size());
    }

    @Override
    public int getItemCount() {
        return resumes != null ? resumes.size() : 0;
    }
}
