package io.github.v7lin;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public final class CommandLine {

    @Parameter(names = {"-version"}, description = "version")
    public boolean version;

    @Parameter(names = {"-help"}, help = true, description = "command line help")
    public boolean help;

    public void parse(JCommander commander) {
        if (version) {
            System.out.println(getVersion());
            return;
        }
        if (help) {
            commander.usage();
        }
    }

    private static String getVersion() {
        try {
            final Enumeration resEnum = Thread.currentThread().getContextClassLoader().getResources(JarFile.MANIFEST_NAME);
            while (resEnum.hasMoreElements()) {
                try {
                    final URL url = (URL) resEnum.nextElement();
                    final InputStream is = url.openStream();
                    if (is != null) {
                        final Manifest manifest = new Manifest(is);
                        final Attributes mainAttribs = manifest.getMainAttributes();
                        final String version = mainAttribs.getValue("LeGu-CLI-Version");
                        if (version != null) {
                            return version;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
