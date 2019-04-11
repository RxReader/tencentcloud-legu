package io.github.v7lin.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;

import java.io.File;
import java.util.Locale;

@Parameters(commandDescription = "legu")
public final class LeGuCommand extends Command {

    @Parameter(required = true, names = {"-in"}, description = "inputApkPath", converter = FileConverter.class)
    private File inputAPK;

    @Parameter(names = {"-out"}, description = "outputPath", converter = FileConverter.class)
    private File outputPath;

    @Override
    protected void checkArgs() {
        if (inputAPK == null || !inputAPK.isFile() || !inputAPK.getName().toLowerCase(Locale.getDefault()).endsWith(".apk")) {
            throw new IllegalArgumentException("param 'inputApkPath' error");
        }
        if (outputPath != null && outputPath.exists() && !outputPath.isDirectory()) {
            throw new IllegalArgumentException("param 'outputPath' error");
        }
    }

    @Override
    protected void execute() throws Exception {

    }
}
