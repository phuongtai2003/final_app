package com.example.final_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.final_project.R;

import java.util.List;

public class StringSpinnerAdapter extends BaseAdapter {
    private List<String> stringList;
    private Context mContext;

    public StringSpinnerAdapter(List<String> stringList, Context mContext) {
        this.stringList = stringList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.stringList != null ? this.stringList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return this.stringList != null ? this.stringList.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.string_value_layout, viewGroup, false);
        TextView accountType = v.findViewById(R.id.stringValue);
        accountType.setText(this.stringList.get(i));
        return v;
    }
}
