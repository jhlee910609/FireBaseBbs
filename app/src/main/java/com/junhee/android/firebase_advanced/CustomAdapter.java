package com.junhee.android.firebase_advanced;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.junhee.android.firebase_advanced.domain.Bbs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JunHee on 2017. 7. 4..
 */

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<Bbs> list;
    public Bbs bbs;
    LayoutInflater inflater;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.bbs_item, null);
        }

        bbs = list.get(position);
        TextView title = (TextView) convertView.findViewById(R.id.list_title);
        TextView author = (TextView) convertView.findViewById(R.id.list_author);
        TextView date = (TextView) convertView.findViewById(R.id.list_date);

        title.setText(bbs.title);
        author.setText(bbs.author);
        date.setText(bbs.date);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReadActivity.class);
                intent.putExtra("LIST_POSITION", position);
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    public CustomAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Bbs> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




}
