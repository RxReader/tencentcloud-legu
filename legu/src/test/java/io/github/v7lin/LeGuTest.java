package io.github.v7lin;

import com.sun.javaws.exceptions.InvalidArgumentException;
import com.tencentcloudapi.ms.v20180408.models.CreateCosSecKeyInstanceResponse;
import io.github.v7lin.setup.Setup;
import io.github.v7lin.setup.domain.Configure;
import io.github.v7lin.tasks.CreateCosSecKeyTask;
import io.github.v7lin.tasks.CreateShieldTask;
import io.github.v7lin.tasks.DownloadTask;
import io.github.v7lin.tasks.UploadTask;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.IconFace;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.List;
import java.util.Set;

@RunWith(JUnit4.class)
public final class LeGuTest {

    @Test
    public void test() throws Exception {
        System.out.println(System.getProperties().getProperty("user.home"));
        System.out.println(System.getProperties().getProperty("user.dir"));

        String secretId = System.getenv("TENCENT_SECRET_ID");
        String secretKey = System.getenv("TENCENT_SECRET_KEY");
        String region = System.getenv("TENCENT_REGION");

        Configure conf = new Configure(secretId, secretKey, region);
        Setup.saveConf(conf);

        conf = Setup.loadConf();
        System.out.println(conf.secretId + " - " + conf.secretKey + " - " + conf.region);

        File apkDir = new File("apk");

        File[] apks = apkDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".apk");
            }
        });

        if (apks == null || apks.length == 0) {
            throw new RuntimeException("can't find apk");
        }

        File apk = apks[0];
        System.out.println("apk: " + apk.getName());

        ApkFile apkFile = new ApkFile(apk);

        ApkMeta apkMeta = apkFile.getApkMeta();

        // 上传文件到COS文件存储
        CreateCosSecKeyInstanceResponse resp = new CreateCosSecKeyTask(conf).execute();
        URL apkUrl = new UploadTask(resp, apk, apkMeta).execute();
        System.out.println("apk url: " + apkUrl.toString());

        // 加固
        URL leguApkUrl = new CreateShieldTask(conf, apk, apkMeta, apkUrl).execute();
        System.out.println("legu apk url: " + leguApkUrl.toString());

        File leguApk = new DownloadTask(leguApkUrl, apkDir.getAbsolutePath()).execute();
        System.out.println("legu apk: " + leguApk.getName());
    }
}
