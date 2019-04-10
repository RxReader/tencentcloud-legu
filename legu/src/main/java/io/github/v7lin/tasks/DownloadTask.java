package io.github.v7lin.tasks;

import java.io.File;
import java.net.URL;

public final class DownloadTask extends SyncTask<File> {

    private final URL leguApkUrl;
    private final File storeDir;

    public DownloadTask(URL leguApkUrl, File storeDir) {
        this.leguApkUrl = leguApkUrl;
        this.storeDir = storeDir;
    }

    @Override
    public File execute() throws Exception {
        return null;
    }
}
