package io.github.v7lin.tasks;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.ms.v20180408.MsClient;
import io.github.v7lin.setup.domain.Configure;

public abstract class MsClientTask<R> extends SyncTask<R> {

    protected final MsClient client;

    public MsClientTask(Configure conf) {
        client = new MsClient(new Credential(conf.secretId, conf.secretKey), conf.region);
    }
}
