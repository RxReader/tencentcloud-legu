package io.github.v7lin.tasks;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.utils.BinaryUtils;
import com.qcloud.cos.utils.Md5Utils;
import com.tencentcloudapi.ms.v20180408.models.CreateCosSecKeyInstanceResponse;
import net.dongliu.apk.parser.bean.ApkMeta;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public final class UploadTask extends SyncTask<URL> {

    private final CreateCosSecKeyInstanceResponse resp;
    private final File apk;
    private final ApkMeta apkMeta;

    public UploadTask(CreateCosSecKeyInstanceResponse resp, File apk, ApkMeta apkMeta) {
        this.resp = resp;
        this.apk = apk;
        this.apkMeta = apkMeta;
    }

    @Override
    public URL execute() throws Exception {
        COSClient client = new COSClient(new BasicSessionCredentials(resp.getCosId(), resp.getCosKey(), resp.getCosTocken()), new ClientConfig(new Region(resp.getCosRegion())));
        String bucket = computeBucket(resp);
        String fileKeyPrefix = computeFileKeyPrefix(resp, apkMeta);
        byte[] fileMd5 = Md5Utils.computeMD5Hash(apk);
        String fileKey = fileKeyPrefix + "/" + BinaryUtils.toHex(fileMd5) + "_" + apk.getName();
        ObjectMetadata fileMetadata = getFileMetadata(client, bucket, fileKey);
        if (fileMetadata != null) {
            // 文件已存在,无需上传
            return generateUrl(client, bucket, fileKey);
        }
        // 上传文件
        uploadFile(client, bucket, fileKey, fileMd5);
        return generateUrl(client, bucket, fileKey);
    }

    private String computeBucket(CreateCosSecKeyInstanceResponse resp) {
        return resp.getCosBucket() + "-" + resp.getCosAppid();
    }

    private String computeFileKeyPrefix(CreateCosSecKeyInstanceResponse resp, ApkMeta apkMeta) {
        String fileKeyPrefix = resp.getCosPrefix();
        if (!fileKeyPrefix.isEmpty() && !fileKeyPrefix.endsWith("/")) {
            fileKeyPrefix = fileKeyPrefix + "/";
        }
        fileKeyPrefix = fileKeyPrefix + apkMeta.getPackageName() + "/" + apkMeta.getVersionCode();
        return fileKeyPrefix;
    }

    private ObjectMetadata getFileMetadata(COSClient client, String bucket, String fileKey) {
        try {
            return client.getObjectMetadata(bucket, fileKey);
        } catch (CosServiceException e) {
            if (e.getStatusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                return null;
            } else {
                throw e;
            }
        }
    }

    private URL generateUrl(COSClient client, String bucket, String fileKey) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, fileKey, HttpMethodName.GET);
        request.setExpiration(new Date(System.currentTimeMillis() + 1800000L));
        request.addRequestParameter("x-cos-security-token", this.resp.getCosTocken());
        return client.generatePresignedUrl(request);
    }

    private void uploadFile(COSClient client, String bucket, String fileKey, byte[] fileMd5) {
        PutObjectRequest request = new PutObjectRequest(bucket, fileKey, apk);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentMD5(BinaryUtils.toBase64(fileMd5));
        request.setMetadata(metadata);
        client.putObject(request);
    }
}
