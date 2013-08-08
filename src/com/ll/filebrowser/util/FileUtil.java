
package com.ll.filebrowser.util;

import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

public class FileUtil {

    public enum FileType {
        APK, DIR, GENERIC, IMAGE, MUSIC, SDCARD, SYS_DIR, SYS_FILE, VIDEO, ZIP
    }

    public static FileType getFileType(File file) {
        if (file == null)
            throw new NullPointerException("File can't be null");
        Uri uri = Uri.fromFile(file);
        FileType fileType = FileType.GENERIC;
        if (file.isDirectory()) {
            fileType = FileType.DIR;
            return fileType;
        }
        String typeStr = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                MimeTypeMap.getFileExtensionFromUrl(uri.toString()));
        if (typeStr == null) {
            return fileType;
        }
        if (typeStr.toLowerCase().startsWith("audio/")) {
            fileType = FileType.MUSIC;
        } else if (typeStr.toLowerCase().startsWith("video/")) {
            fileType = FileType.VIDEO;
        } else if (typeStr.toLowerCase().startsWith("image/")) {
            fileType = FileType.IMAGE;
        }
        return fileType;

    }

}
