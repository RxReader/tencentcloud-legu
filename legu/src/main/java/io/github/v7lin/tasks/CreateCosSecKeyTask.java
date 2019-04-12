package io.github.v7lin.tasks;

import com.tencentcloudapi.ms.v20180408.models.CreateCosSecKeyInstanceRequest;
import com.tencentcloudapi.ms.v20180408.models.CreateCosSecKeyInstanceResponse;
import io.github.v7lin.setup.domain.Configure;

public final class CreateCosSecKeyTask extends MsClientTask<CreateCosSecKeyInstanceResponse> {

    public CreateCosSecKeyTask(Configure conf) {
        super(conf);
    }

    @Override
    public CreateCosSecKeyInstanceResponse execute() throws Exception {
        return client.CreateCosSecKeyInstance(new CreateCosSecKeyInstanceRequest());
    }
}
