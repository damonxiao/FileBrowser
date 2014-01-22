package com.ll.filebrowser.ui;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ll.filebrowser.R;
import com.ll.filebrowser.util.FileFinder;
import com.ll.filebrowser.util.FileListAdapter;
import com.ll.filebrowser.util.FileUtil;
import com.ll.filebrowser.util.Logger;
import com.ll.filebrowser.util.NavigatorAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListActivity extends Activity implements
		ActionBar.OnNavigationListener, OnItemClickListener {

	private static final String TAG = "FileListActivity";

	private List<File> mNavigatorDirs;

	private NavigatorAdapter mNavigatorAdapter;

	private ListView mFileListView;

	private FileListAdapter mFileListAdapter;

	private File mCurFolder;

	private FileFinder mExecutingFinder;
	
	private final File mRootFile = new File("/");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prepareActionBar();
		mCurFolder = mRootFile;
		mFileListView = (ListView) findViewById(R.id.file_list_view);
		mFileListView.setOnItemClickListener(this);
		loadFolder(mCurFolder);
	}

	private void loadFolder(File folder) {
		if (folder == null || !folder.exists()) {
			return;
		}
		if (folder.getParentFile() != null && folder.getParentFile().exists()) {
			mNavigatorDirs.set(0, folder);
			mNavigatorAdapter.notifyDataSetChanged();
		} else {
			mNavigatorDirs.set(0, mRootFile);
			mNavigatorAdapter.notifyDataSetChanged();
		}
		getActionBar().setSelectedNavigationItem(0);
		if (mExecutingFinder != null) {
			mExecutingFinder.cancel(true);
		}
		showProgressDialog();
		mExecutingFinder = new FileFinder(new FileFinder.IFileFinderCallback() {
			@Override
			public void onLoadSuccess(List<File> files) {
				dismissProgressDialog();
				if (mFileListAdapter == null) {
					mFileListAdapter = new FileListAdapter(
							FileListActivity.this, files);
					mFileListView.setAdapter(mFileListAdapter);
				} else {
					mFileListAdapter.refreshData(files);
				}
			}

			@Override
			public void onLoadFailed(int resonCode) {
				dismissProgressDialog();
			}
		});
		mExecutingFinder.execute(folder);
	}

	private ProgressDialog mProgressDialog;

	private void showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setCancelable(false);
		}
		if (mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		mProgressDialog.show();
	}

	private void dismissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	private void prepareActionBar() {
		mNavigatorDirs = new ArrayList<File>();
		mNavigatorDirs.add(new File(""));
		mNavigatorDirs.add(mRootFile);
		mNavigatorDirs.add(Environment.getExternalStorageDirectory());
		mNavigatorDirs
				.add(Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
		mNavigatorDirs.add(Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
		mNavigatorDirs
				.add(Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
		mNavigatorDirs
				.add(Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
		mNavigatorDirs
				.add(Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));

		mNavigatorAdapter = new NavigatorAdapter(mNavigatorDirs, this);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		// List<File>
		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				mNavigatorAdapter, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		File selectedItem = mNavigatorAdapter.getItem(itemPosition);
		Logger.d(TAG, "onNavigationItemSelected()[selectedItem:" + selectedItem
				+ "]");
		if (selectedItem != null && selectedItem.exists()
				&& selectedItem.isDirectory()) {
			mCurFolder = selectedItem;
			loadFolder(mCurFolder);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Logger.d(TAG,
					"onOptionsItemSelected()[click home go to parent folder]");
			goToParent();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    public void onBackPressed() {
        if (!goToParent())
            super.onBackPressed();
    }

	private boolean goToParent() {
		if (mCurFolder != null && mCurFolder.getParentFile() != null
				&& mCurFolder.getParentFile().exists()) {
			mCurFolder = mCurFolder.getParentFile();
			loadFolder(mCurFolder);
			return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		File clickItem = mFileListAdapter.getItem(arg2);
		Logger.d(TAG, "onItemClick()[select file type:"+FileUtil.getFileType(clickItem)+"]");
		if (clickItem != null && clickItem.exists() && clickItem.isDirectory()) {
			mCurFolder = clickItem;
			loadFolder(mCurFolder);
		} else if(clickItem != null && clickItem.exists() && clickItem.isFile()){
		    viewFile(clickItem);
		}
	}
	
	private void viewFile(File file){
	    Logger.d(TAG, "viewFile()[file="+file+"]");
        Uri uri = Uri.fromFile(file);
        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(uri.toString());
        String mimeType = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        Logger.d(TAG, "viewFile()[extension="+extension+",mimeType="+mimeType+"]");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeType != null ? mimeType : "*/*");
        startActivity(Intent.createChooser(intent, "Open use"));
	}

}
