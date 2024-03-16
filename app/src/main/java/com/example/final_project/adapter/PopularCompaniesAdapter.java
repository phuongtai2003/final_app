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
import com.example.final_project.models.Company;
import com.example.final_project.utils.GlobalConstants;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PopularCompaniesAdapter extends RecyclerView.Adapter<PopularCompaniesAdapter.ViewHolder> {
    private List<Company> companies;
    private Context mContext;

    public PopularCompaniesAdapter(List<Company> companies, Context mContext) {
        this.companies = companies;
        this.mContext = mContext;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        ShapeableImageView companyImage;
        TextView companyAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.companyNameTxt);
            companyAddress = itemView.findViewById(R.id.companyAddressTxt);
            companyImage = itemView.findViewById(R.id.companyLogoImg);
        }
    }

    @NonNull
    @Override
    public PopularCompaniesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.company_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularCompaniesAdapter.ViewHolder holder, int position) {
        Company company = companies.get(position);
        holder.companyName.setText(company.getName());
        if(company.getAddress() != null){
            holder.companyAddress.setText(company.getAddress());
        }
        if(company.getImage() != null){
            Picasso.get().load(company.getImage()).into(holder.companyImage);
        }else{
            Picasso.get().load(GlobalConstants.defaultCompanyLogoUrl).into(holder.companyImage);
        }
    }

    @Override
    public int getItemCount() {
        return companies != null ? companies.size() : 0;
    }
}
