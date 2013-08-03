
package com.ll.filebrowser.util;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ll.filebrowser.R;

public class FileListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<File> mFileList;

    public FileListAdapter(Context context,List<File> fileList){
    	mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	mFileList = fileList;
    }
    
    public void refreshData(List<File> files){
    	mFileList = files;
    	notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return mFileList == null ? 0 : mFileList.size();
    }

    @Override
    public File getItem(int position) {
        return mFileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	if(convertView == null){
    		convertView = mInflater.inflate(R.layout.file_list_item, null);
    	}
    	File file = mFileList.get(position);
    	ImageView fileTypeIcon = (ImageView) convertView.findViewById(R.id.file_type_icon);
    	TextView fileName = (TextView) convertView.findViewById(R.id.file_name);
    	fileName.setText(file.getName());
    	TextView fileDesc = (TextView) convertView.findViewById(R.id.file_desc);
        return convertView;
    }

}
