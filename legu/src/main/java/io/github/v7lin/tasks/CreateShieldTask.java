package io.github.v7lin.tasks;

import com.qcloud.cos.utils.BinaryUtils;
import com.qcloud.cos.utils.Md5Utils;
import com.tencentcloudapi.ms.v20180408.models.*;
import net.dongliu.apk.parser.bean.ApkMeta;

import java.io.File;
import java.net.URL;

public final class CreateShieldTask extends MsClientTask<URL> {

    private final File apk;
    private final ApkMeta apkMeta;
    private final URL apkUrl;

    public CreateShieldTask(String secretId, String secretKey, String region, File apk, ApkMeta apkMeta, URL apkUrl) {
        super(secretId, secretKey, region);
        this.apk = apk;
        this.apkMeta = apkMeta;
        this.apkUrl = apkUrl;
    }

    @Override
    public URL execute() throws Exception {
        AppInfo appInfo = new AppInfo();
        appInfo.setAppUrl(apkUrl.toString());
        appInfo.setAppMd5(BinaryUtils.toHex(Md5Utils.computeMD5Hash(apk)));
        appInfo.setAppPkgName(apkMeta.getPackageName());

        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setServiceEdition("basic");
        serviceInfo.setSubmitSource("legu-cli");
        serviceInfo.setCallbackUrl("");

        CreateShieldInstanceRequest request = new CreateShieldInstanceRequest();
        request.setAppInfo(appInfo);
        request.setServiceInfo(serviceInfo);

        CreateShieldInstanceResponse response = client.CreateShieldInstance(request);

        if (response.getItemId() != null && !response.getItemId().isEmpty()) {
            if (response.getProgress() == 2) {
                // 处理中
                Thread.sleep(5000L);
            }

            DescribeShieldResultRequest resultReq = new DescribeShieldResultRequest();
            resultReq.setItemId(response.getItemId());

            while(true) {
                DescribeShieldResultResponse resultResp = client.DescribeShieldResult(resultReq);
                int status = resultResp.getTaskStatus();
                switch(status) {
                    case 1:
                        ShieldInfo shieldInfo = resultResp.getShieldInfo();
                        return new URL(shieldInfo.getAppUrl());
                    case 2:
                        Thread.sleep(20000L);
                        break;
                    case 3:
                        throw new Exception(String.format("DescribeShieldResult[%s] %s, ShieldCode=%d\n 错误指引: %s", resultResp.getRequestId(), resultResp.getStatusDesc(), resultResp.getShieldInfo().getShieldCode(), resultResp.getStatusRef()));
                    default:
                        throw new Exception(String.format("DescribeShieldResult[%s] %s, taskStatus=%d\n 错误指引: %s", resultResp.getRequestId(), resultResp.getStatusDesc(), resultResp.getTaskStatus(), resultResp.getStatusRef()));
                }
            }
        } else {
            throw new Exception(String.format("CreateShieldInstance[%s] failed, item id is empty", response.getRequestId()));
        }
    }
}
