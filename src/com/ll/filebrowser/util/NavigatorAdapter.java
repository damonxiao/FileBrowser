
package com.ll.filebrowser.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class NavigatorAdapter extends BaseAdapter {

    private List<File> mNavigatorDirs;

    private LayoutInflater mInflater;

    private Context mContext;

    public NavigatorAdapter(List<File> navigatorDirs, Context context) {
        super();
        this.mNavigatorDirs = navigatorDirs;
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mNavigatorDirs == null ? 0 : mNavigatorDirs.size();
    }

    @Override
    public File getItem(int position) {
        // TODO Auto-generated method stub
        return mNavigatorDirs.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(mNavigatorDirs.get(position).getAbsolutePath());
        return convertView;
    }
}
