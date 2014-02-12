
package com.ll.filebrowser.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.WeakHashMap;

public class ImageGetter {

    private static final String TAG = ImageGetter.class.getSimpleName();

    private static final String LOCAL_CACHE_DIR = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separatorChar + "FileBrowser/.thum";

    private static final int MAX_CACHE_FILE_COUNT = 200;

    private static final WeakHashMap<String, Drawable> IMAGE_CACHE = new WeakHashMap<String, Drawable>();

    private static Queue<ImageGetterTask> sTaskQueue = new LinkedList<ImageGetter.ImageGetterTask>();

    private static TaskState sTaskState = TaskState.RESUME;

    private static enum TaskState {
        PAUSE, RESUME
    }

    private static class ImageHeader {
        String url;
        String md5;
        ImageView imageView;
        boolean set2bg;
        int maxWidth;
        int maxHeight;

        public ImageHeader(String url, ImageView imageView, boolean set2bg, int maxWidth,
                int maxHeight) {
            super();
            this.url = url;
            this.imageView = imageView;
            this.set2bg = set2bg;
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
            md5 = MD5Util.getFileMD5String(new File(url));
        }
    }

    private static class ImageGetterTask extends AsyncTask<Void, Void, Void> {
        ImageHeader header;

        public ImageGetterTask(ImageHeader header) {
            super();
            this.header = header;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (isLocalCacheExist()) {
                loadLocalCacheImage();
            } else {
                loadLocalImage();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            bindImage2View(header.imageView, header.set2bg, header.url);
            doQueueNext();
        }

        private void loadLocalImage() {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(header.url, options);
            int scale = 1;
            if (options.outHeight >= options.outWidth) {
                // let height to scale image
                scale = options.outHeight / header.maxHeight;
            } else {
                // let width to scale image
                scale = options.outWidth / header.maxWidth;
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            Bitmap bmp = BitmapFactory.decodeFile(header.url, options);
            Logger.d(
                    TAG,
                    "loadLocalImage()[bmp width=" + bmp.getWidth() + ",bmp height="
                            + bmp.getHeight() + "]");
            IMAGE_CACHE.put(header.url, new BitmapDrawable(bmp));
            save2LocalCache(bmp);
        }

        private boolean isLocalCacheExist() {
            File cacheFile = new File(LOCAL_CACHE_DIR, header.md5);
            return cacheFile.exists();
        }

        private void loadLocalCacheImage() {
            Bitmap bmp = BitmapFactory.decodeFile(LOCAL_CACHE_DIR + File.separator + header.md5);
            IMAGE_CACHE.put(header.url, new BitmapDrawable(bmp));
        }

        private void save2LocalCache(Bitmap bmp) {
            File cacheDir = new File(LOCAL_CACHE_DIR);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            } else if (cacheDir.listFiles() != null
                    && cacheDir.listFiles().length > MAX_CACHE_FILE_COUNT) {
                for (File file : cacheDir.listFiles()) {
                    file.delete();
                }
            }
            File cacheFile = new File(LOCAL_CACHE_DIR, header.md5);
            if (cacheFile.exists() || null == bmp) {
                return;
            }
            FileOutputStream fos = null;
            try {
                cacheFile.createNewFile();
                fos = new FileOutputStream(cacheFile);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void loadImage(String url, ImageView imageView, boolean set2bg) {
        if (null == url || null == imageView) {
            return;
        }
        if (bindImage2View(imageView, set2bg, url)) {
            return;
        }
        queueToExecute(new ImageGetterTask(new ImageHeader(url, imageView, set2bg,
                imageView.getWidth(),
                imageView.getHeight())));
    }

    public static void loadThumbnails(String url, ImageView imageView, boolean set2bg,
            int maxWidth, int maxHeight) {
        if (null == url || null == imageView) {
            return;
        }
        if (bindImage2View(imageView, set2bg, url)) {
            return;
        }
        queueToExecute(new ImageGetterTask(new ImageHeader(url, imageView, set2bg, maxWidth,
                maxHeight)));
    }

    public static void pauseLoadImage() {
        sTaskState = TaskState.PAUSE;
    }

    public static void resumeLoadImage() {
        sTaskState = TaskState.RESUME;
        doQueueNext();
    }

    private static void queueToExecute(ImageGetterTask task) {
        if (task != null) {
            sTaskQueue.add(task);
        }
        doQueueNext();
    }

    private static void doQueueNext() {
        Logger.d(TAG, "doQueueNext()[sTaskState=" + sTaskState + "]");
        if (!sTaskQueue.isEmpty() && sTaskState != TaskState.PAUSE) {
            sTaskQueue.poll().execute();
        }
    }

    private static boolean bindImage2View(ImageView imageView, boolean set2bg, String url) {
        Drawable drawable = IMAGE_CACHE.get(url);
        if (drawable != null) {
            if (set2bg) {
                imageView.setBackground(drawable);
            } else {
                imageView.setImageDrawable(drawable);
            }
            return true;
        }
        return false;
    }

}
