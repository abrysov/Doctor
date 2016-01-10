package com.evs.doctor.util;

import java.io.*;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.crittercism.app.Crittercism;
import com.evs.doctor.R;

public class FileUtil {

    private static final int BUFFER_SIZE = 2048;

    public static boolean isFileExists(String path) {
        return path != null && new File(path).exists();
    }

    public static File getFileFromUri(Uri uri) {
        return new File(uri.getPath());
    }

    public static Uri getFileUri(String path, String fileName) {
        return Uri.fromFile(FileUtil.getFile(path, fileName));
    }

    public static File getFile(String path, String fileName) {
        return new File(path, fileName);
    }

    public static boolean isImageExists(String path, String name) {
        return FileUtil.getFile(path, name).exists();
    }

    public static boolean createFolderIfNoExists(String folderDir) {
        File folder = new File(folderDir);
        if (!folder.exists()) {
            return folder.mkdir();
        }
        return true;
    }

    public static void deleteFolder(File path) {
        if (path.isDirectory())
            for (File child : path.listFiles())
                deleteFolder(child);
        path.delete();
    }

    public static String getDataDir(Context context) {
        File dataDir;
        if (context == null) {
            dataDir = Environment.getDownloadCacheDirectory();
        } else {
            dataDir = context.getFilesDir();
        }
        // Log.i(TAG, String.format("home dir :%s ", dataDir.getAbsolutePath() + File.separator));
        return dataDir.getAbsolutePath() + File.separator;
    }

    public static boolean isSdCardUsed() {
        return Environment.getExternalStorageState() != null
                && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static String getExternalDir(Context context) {
        String home = null;
        String appName = context.getString(R.string.app_folder);
        try {
            if (isSdCardUsed()) {
                home = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + appName;
            } else {
                home = context.getCacheDir().getAbsolutePath();
            }
        } catch (Exception e) {
            home = Environment.getDownloadCacheDirectory().getAbsolutePath();
            Crittercism.logHandledException(e);
        }

        return home + File.separator;
    }

    public static File[] getFilesByFilter(String homeDir, final String startWith) {
        File[] files = new File(homeDir).listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {
                return filename.startsWith(startWith);
            }
        });

        return (files == null) ? new File[0] : files;
    }

    /**
     * @param photoDir
     * @param filterByFileName
     */
    public static String getLastFileName(String baseDir, String filter) {
        File[] files = getFilesByFilter(baseDir, filter);
        if (files.length > 0) {
            return files[files.length - 1].getName();
        }
        return null;
    }

    public static String getNextFileName(String basePath, String startWith, String endWith) {
        int number = 0;
        for (File f : FileUtil.getFilesByFilter(basePath, startWith)) {
            try {
                int fileNumber = Integer.parseInt(f.getName().split("\\.")[1]);
                if (fileNumber > number) {
                    number = fileNumber;
                }
            } catch (NumberFormatException e) {
                f.delete();
            }
        }
        return startWith + "." + String.valueOf(++number) + endWith;
    }

    /**
     * 
     * 
     * @throws IOException
     */
    public static File download(InputStream is, String to) throws IOException {

        byte buffer[] = new byte[BUFFER_SIZE];
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            File target = new File(to);
            if (!target.exists() && target.getParentFile() != null) {
                target.getParentFile().mkdirs();
            }
            bos = new BufferedOutputStream(new FileOutputStream(target));
            bis = new BufferedInputStream(is, BUFFER_SIZE);

            int readCount = 0;
            while ((readCount = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, readCount);
            }
            Logger.d(FileUtil.class, "Download complete:" + to);
            File result = new File(to);
            return result.exists() ? result : null;
        } finally {
            closeStream(bos);
            closeStream(bis);
        }
    }

    /**
     * @param absolutePath
     * @return
     * @throws IOException 
     */
    public static String getFileContent(String path) throws IOException {
        FileInputStream fis = null;
        String content = null;
        if (new File(path).exists()) {
            try {
                fis = new FileInputStream(path);
                content = getStreamAsString(fis);

            } finally {
                closeStream(fis);
            }
        } else {
            Logger.w(FileUtil.class, "File does not exists " + path);
        }
        return content;

    }

    public static String getStreamAsString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } finally {
            closeStream(reader);
        }
        return sb.toString();
    }

    public static void closeStream(Closeable stream) {
        try {
            if (stream != null)
                stream.close();
        } catch (Exception e) {
        }
    }

    /**
     * @param string
     * @param string2
     * @throws IOException 
     */
    public static void saveToFile(String filePath, String data) throws IOException {
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            fos.write(data.getBytes());
        } finally {
            closeStream(fos);
        }
    }

    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        try {
            byte[] buff = new byte[BUFFER_SIZE];
            int size;
            while ((size = is.read(buff, 0, buff.length)) != -1) {
                os.write(buff, 0, size);
            }
            os.flush();
        } finally {
            closeStream(os);
            closeStream(is);
        }
    }

}
