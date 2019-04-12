package io.github.v7lin.setup;

import com.google.gson.Gson;
import io.github.v7lin.setup.domain.Configure;

import java.io.*;

public class Setup {

    private static final String FILE_CONF = "conf.json";

    private Setup() {
    }

    private static File saveDir() {
        File saveDir = new File(System.getProperties().getProperty("user.home"), ".legu");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        return saveDir;
    }

    public static Configure loadConf() throws IOException {
        StringBuilder builder = new StringBuilder();
        File confFile = new File(saveDir(), FILE_CONF);
        BufferedReader reader = new BufferedReader(new FileReader(confFile));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append("\n");
        }
        return new Gson().fromJson(builder.toString(), Configure.class);
    }

    public static void saveConf(Configure conf) throws IOException {
        File confFile = new File(saveDir(), FILE_CONF);
        File tmpFile = new File(confFile.getAbsolutePath() + ".ldt");
        FileWriter writer = new FileWriter(tmpFile);
        writer.write(new Gson().toJson(conf));
        writer.flush();
        writer.close();
        tmpFile.renameTo(confFile);
    }
}
