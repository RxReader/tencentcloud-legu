package io.github.v7lin.tasks;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public final class DownloadTask extends SyncTask<File> {

    private static final int MAX_RETRY = 3;

    private final URL leguApkUrl;
    private final String savePath;

    public DownloadTask(URL leguApkUrl, String savePath) {
        this.leguApkUrl = leguApkUrl;
        this.savePath = savePath;
    }

    @Override
    public File execute() throws Exception {
        String fileName = getFileName(leguApkUrl);
        if (fileName == null || fileName.length() == 0) {
            throw new FileNotFoundException("can't get file name from url");
        }
        File file = new File(savePath, fileName);
        file.getParentFile().mkdirs();
        if (file.exists()) {
            if (!file.isFile()) {
                throw new IOException(fileName + "directory is exist!");
            }
            if (!file.canWrite()) {
                throw new IOException(fileName + "can't be written!");
            }
        }
        File tempFile = new File(file.getAbsolutePath() + ".ldt");
        if (tempFile.exists() && !tempFile.delete()) {
            throw new IOException("temp file can't recreate!");
        }
        // 开始下载
        int retry = 1;
        while (retry <= 3) {
            try {
                this.continueDownload(leguApkUrl, tempFile);
                break;
            } catch (SecurityException | FileNotFoundException e) {
                throw e;
            } catch (IOException e) {
                if (retry == MAX_RETRY) {
                    throw e;
                }
                retry ++;
            }
        }
        if (file.exists()) {
            file.delete();
        }
        if (!tempFile.renameTo(file)) {
            throw new IOException("temp file can't rename!");
        }
        return file;
    }

    private String getFileName(URL url) {
        String path = url.getPath();
        path = path.substring(path.lastIndexOf('/') + 1, path.length());
        path = path.substring(path.indexOf('_') + 1, path.length());
        return path.trim();
    }

    private void continueDownload(URL url, File file) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        IOException firstEx = null;

        try {
            long completeLength = file.length();
            HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
            if (completeLength > 0L) {
                httpConn.setRequestProperty("Range", "bytes=" + completeLength + "-");
            }

            is = httpConn.getInputStream();
            fos = new FileOutputStream(file, true);
            bos = new BufferedOutputStream(fos);

            byte[] buff = new byte[8192];

            int count;
            while((count = is.read(buff)) != -1) {
                bos.write(buff, 0, count);
                completeLength += (long)count;
            }
        } catch (IOException e) {
            firstEx = e;
        }

        try {
            if (bos != null) {
                bos.close();
            }

            if (is != null) {
                is.close();
            }

            if (fos != null) {
                fos.close();
            }
        } catch (IOException var12) {
            if (firstEx == null) {
                firstEx = var12;
            }
        }

        if (firstEx != null) {
            throw firstEx;
        }
    }
}
