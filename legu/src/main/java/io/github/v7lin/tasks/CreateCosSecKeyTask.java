package io.github.v7lin.tasks;

import com.tencentcloudapi.ms.v20180408.models.CreateCosSecKeyInstanceRequest;
import com.tencentcloudapi.ms.v20180408.models.CreateCosSecKeyInstanceResponse;

public final class CreateCosSecKeyTask extends MsClientTask<CreateCosSecKeyInstanceResponse> {

    public CreateCosSecKeyTask(String secretId, String secretKey, String region) {
        super(secretId, secretKey, region);
    }

    @Override
    public CreateCosSecKeyInstanceResponse execute() throws Exception {
        return client.CreateCosSecKeyInstance(new CreateCosSecKeyInstanceRequest());
    }
}
