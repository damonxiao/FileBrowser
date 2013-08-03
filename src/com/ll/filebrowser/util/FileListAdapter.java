
package com.ll.filebrowser.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.File;
import java.util.List;

public class FileListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<File> mFileList;

    @Override
    public int getCount() {
        return mFileList == null ? 0 : mFileList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }

}
