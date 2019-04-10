package io.github.v7lin;

import io.github.v7lin.commands.Command;
import io.github.v7lin.commands.ConfigureCommand;

import java.util.HashMap;
import java.util.Map;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Map<String, Command> commandMap = new HashMap<>();
        commandMap.put("configure", new ConfigureCommand());
        commandMap.put("legu", null);
    }
}
