package io.github.v7lin.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;
import com.tencentcloudapi.ms.v20180408.models.CreateCosSecKeyInstanceResponse;
import io.github.v7lin.setup.Setup;
import io.github.v7lin.setup.domain.Configure;
import io.github.v7lin.tasks.CreateCosSecKeyTask;
import io.github.v7lin.tasks.CreateShieldTask;
import io.github.v7lin.tasks.DownloadTask;
import io.github.v7lin.tasks.UploadTask;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

@Parameters(commandDescription = "legu")
public final class LeGuCommand extends Command {

    @Parameter(required = true, names = {"-in"}, description = "inputApkPath", converter = FileConverter.class)
    private File inputAPK;

    @Parameter(names = {"-out"}, description = "outputPath", converter = FileConverter.class)
    private File outputPath;

    @Override
    protected void checkArgs() throws Exception {
        if (inputAPK == null || !inputAPK.isFile() || !inputAPK.getName().toLowerCase(Locale.getDefault()).endsWith(".apk")) {
            throw new IllegalArgumentException("param 'inputApkPath' error");
        }
        if (outputPath != null && outputPath.exists() && !outputPath.isDirectory()) {
            throw new IllegalArgumentException("param 'outputPath' error");
        }
    }

    @Override
    protected void execute() throws Exception {
        Configure conf = null;
        try {
            conf = Setup.loadConf();
        } catch (FileNotFoundException e) {
            System.out.println("please run configure command first");
            System.exit(1);
            return;
        }
        if (conf == null || !conf.isLegal()) {
            throw new IllegalArgumentException("conf params error");
        }

        ApkFile apkFile = new ApkFile(inputAPK);
        ApkMeta apkMeta = apkFile.getApkMeta();

        // 上传文件到COS文件存储
        System.out.println("上传APK到COS文件存储...");
        CreateCosSecKeyInstanceResponse resp = new CreateCosSecKeyTask(conf).execute();
        URL apkUrl = new UploadTask(resp, inputAPK, apkMeta).execute();

        // 加固
        System.out.println("加固中...");
        URL leguApkUrl = new CreateShieldTask(conf, inputAPK, apkMeta, apkUrl).execute();
        File leguApk = new DownloadTask(leguApkUrl, outputPath.getAbsolutePath()).execute();
        System.out.println("加固成功：" + leguApk.getAbsolutePath());
    }
}
