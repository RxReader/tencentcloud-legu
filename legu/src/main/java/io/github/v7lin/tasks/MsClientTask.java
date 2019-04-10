package io.github.v7lin.tasks;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.ms.v20180408.MsClient;

public abstract class MsClientTask<R> extends SyncTask<R> {

    protected final MsClient client;

    public MsClientTask(String secretId, String secretKey, String region) {
        client = new MsClient(new Credential(secretId, secretKey), region);
    }
}
