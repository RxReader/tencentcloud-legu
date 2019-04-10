package io.github.v7lin;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import io.github.v7lin.commands.Command;
import io.github.v7lin.commands.ConfigureCommand;
import io.github.v7lin.commands.LeGuCommand;

import java.util.HashMap;
import java.util.Map;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Map<String, Command> commandMap = new HashMap<>();
        commandMap.put("configure", new ConfigureCommand());
        commandMap.put("legu", new LeGuCommand());

        CommandLine commandLine = new CommandLine();
        JCommander commander = new JCommander(commandLine);

        for (Map.Entry<String, Command> entry : commandMap.entrySet()) {
            commander.addCommand(entry.getKey(), entry.getValue());
        }

        try {
            commander.parse(args);
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            commander.usage();
            System.exit(1);
            return;
        }

        commandLine.parse(commander);

        final String parseCommand = commander.getParsedCommand();
        if (parseCommand != null) {
            try {
                commandMap.get(parseCommand).exec();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(1);
                return;
            }
        }
    }
}
