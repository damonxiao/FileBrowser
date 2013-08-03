
package com.ll.filebrowser.ui;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.widget.ListView;

import com.ll.filebrowser.R;
import com.ll.filebrowser.util.FileFinder;
import com.ll.filebrowser.util.Logger;
import com.ll.filebrowser.util.NavigatorAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListActivity extends Activity implements ActionBar.OnNavigationListener {

    private static final String TAG = "FileListActivity";

    private List<File> mNavigatorDirs;

    private NavigatorAdapter mNavigatorAdapter;
    
    private ListView mFileListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareActionBar();
    }

    private void prepareActionBar() {
        mNavigatorDirs = new ArrayList<File>();
        mNavigatorDirs.add(Environment.getRootDirectory());
        mNavigatorDirs.add(Environment.getExternalStorageDirectory());
        mNavigatorDirs.add(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        mNavigatorDirs.add(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
        mNavigatorDirs.add(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
        mNavigatorDirs.add(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
        mNavigatorDirs.add(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));

        mNavigatorAdapter = new NavigatorAdapter(mNavigatorDirs, this);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        // List<File>
        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                mNavigatorAdapter,
                this);
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
        Logger.d(TAG, "onNavigationItemSelected()[selectedItem:" + selectedItem + "]");
        new FileFinder(new FileFinder.IFileFinderCallback() {
            @Override
            public void onLoadSuccess(List<File> files) {
                
            }

            @Override
            public void onLoadFailed(int resonCode) {

            }
        }).execute(selectedItem);
        return true;
    }

}
