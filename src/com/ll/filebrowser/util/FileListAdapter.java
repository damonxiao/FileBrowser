
package com.ll.filebrowser.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ll.filebrowser.R;
import com.ll.filebrowser.util.FileUtil.FileType;
import com.ll.filebrowser.util.ImageGetter.IImageGetterCallback;

import java.io.File;
import java.util.List;
import java.util.WeakHashMap;

public class FileListAdapter extends BaseAdapter {
    
    private static final String TAG = FileListAdapter.class.getSimpleName();

    private LayoutInflater mInflater;
    private List<File> mFileList;

    private WeakHashMap<Integer, Drawable> mImageCache = new WeakHashMap<Integer, Drawable>();
    
    public FileListAdapter(Context context,List<File> fileList){
    	mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	mFileList = fileList;
    }
    
    public void refreshData(List<File> files){
    	mFileList = files;
    	mImageCache.clear();
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
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	if(convertView == null){
    		convertView = mInflater.inflate(R.layout.file_list_item, null);
    	}
    	File file = mFileList.get(position);
    	final ImageView fileTypeIcon = (ImageView) convertView.findViewById(R.id.file_type_icon);
    	TextView fileName = (TextView) convertView.findViewById(R.id.file_name);
    	fileName.setText(file.getName());
    	TextView fileDesc = (TextView) convertView.findViewById(R.id.file_desc);
    	FileType fileType = FileUtil.getFileType(file);
    	int typeIconId = R.drawable.filetype_generic;
    	switch (fileType) {
            case DIR:
                typeIconId = R.drawable.filetype_dir;
                break;
            case MUSIC:
                typeIconId = R.drawable.filetype_music;
                break;
            case VIDEO:
                typeIconId = R.drawable.filetype_video;
                break;
            case IMAGE:
                typeIconId = R.drawable.filetype_image;
                break;
            case APK:
                typeIconId = R.drawable.filetype_apk;
                break;
            case SDCARD:
                typeIconId = R.drawable.filetype_sdcard;
                break;
            case ZIP:
                typeIconId = R.drawable.filetype_zip;
                break;
            case SYS_DIR:
                typeIconId = R.drawable.filetype_sys_dir;
                break;
            case SYS_FILE:
                typeIconId = R.drawable.filetype_sys_file;
                break;
            default:
                break;
        }
    	fileTypeIcon.setImageResource(typeIconId);
        if (fileType == FileUtil.FileType.IMAGE) {
            if(mImageCache.get(position) != null){
                fileTypeIcon.setImageDrawable(mImageCache.get(position));
            } else {
                ImageGetter.loadImage(file.getAbsolutePath(), position,
                        fileTypeIcon.getWidth(), fileTypeIcon.getHeight(), new IImageGetterCallback() {
                    @Override
                    public void onLoadSuccess(Drawable drawable, int reqPosition) {
                        mImageCache.put(reqPosition, drawable);
                        notifyDataSetChanged();
                    }
                    
                    @Override
                    public void onLoadFailed() {
                        
                    }
                });
            }
        }
        return convertView;
    }
    
    public void pauseLoadImage(){
        ImageGetter.pauseLoadImage();
    }
    
    public void resumeLoadImage(){
        ImageGetter.resumeLoadImage();
    }

}
