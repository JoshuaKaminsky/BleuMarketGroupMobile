package com.mobile.bmg.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobile.bmg.R;
import com.mobile.bmg.model.Category;
import com.mobile.bmg.model.Tag;

/**
 * Created by Josh on 10/17/15.
 */
public class FilterAdapter<T extends Category> extends ArrayAdapter<T> {

    private Activity context;

    public FilterAdapter(Activity context, int resource) {
        super(context, resource);

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomDialogView(position, convertView, parent);
    }

    @Override
    public int getPosition(T item) {
        for (int index = 0; index < this.getCount(); index++) {
            Category category = this.getItem(index);
            if(category.id == item.id)
                return index;
        }

        return 0;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.browse_spinner_style, parent, false);

        T item = this.getItem(position);

        TextView itemText = (TextView) convertView.findViewById(R.id.browse_spinner_text);
        itemText.setText(item.name.toUpperCase());

        TextView countText = (TextView) convertView.findViewById(R.id.browse_spinner_count);
        countText.setText(Integer.toString(item.count));

        if(item.getClass() == Tag.class) {
            convertView.setBackgroundResource(R.color.bmg_dark_green);
            countText.setBackgroundResource(R.drawable.bmg_green_circle_shape);
        } else {
            convertView.setBackgroundResource(R.color.bmg_dark_blue);
            countText.setBackgroundResource(R.drawable.bmg_blue_circle_shape);
        }

        return convertView;
    }

    private View getCustomDialogView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.browse_spinner_style, parent, false);

        T item = this.getItem(position);

        TextView itemText = (TextView) convertView.findViewById(R.id.browse_spinner_text);
        itemText.setText(item.name.toUpperCase());
        itemText.setTextColor(context.getResources().getColor(R.color.Black));

        TextView countText = (TextView) convertView.findViewById(R.id.browse_spinner_count);
        countText.setText(Integer.toString(item.count));
        countText.setTextColor(context.getResources().getColor(R.color.bmg_dark_gray));

        return convertView;
    }
}
