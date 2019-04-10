package io.github.v7lin.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.converters.FileConverter;

import java.io.File;
import java.util.List;

@Parameters(commandDescription = "legu")
public final class LeGuCommand extends Command {

    @Parameter(required = true, description = "inputAPKpath [outputPath]", arity = 2, converter = FileConverter.class)
    private List<File> files;

    @Override
    protected void checkArgs() {

    }

    @Override
    protected void exec() throws Exception {

    }
}
