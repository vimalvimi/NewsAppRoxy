package com.vimalroxy.news_roxy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends ArrayAdapter<News> {

    public Adapter(Context context) {
        super(context, -1, new ArrayList<News>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView webTitle = (TextView) listView.findViewById(R.id.title);
        webTitle.setText(currentNews.getWebTitle());

        TextView sectionName = (TextView) listView.findViewById(R.id.section_name);
        sectionName.setText(currentNews.getSectionName());

        TextView authorsName = (TextView) listView.findViewById(R.id.author);
        authorsName.setText(currentNews.getAuthorName());

        TextView date = (TextView) listView.findViewById(R.id.date);
        date.setText(currentNews.getDate());

        return listView;

    }
}
