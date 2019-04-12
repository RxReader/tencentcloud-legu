package io.github.v7lin.commands;

public abstract class Command {

    public final void exec() {
        try {
            checkArgs();
            execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }
    }

    protected abstract void checkArgs() throws Exception;

    protected abstract void execute() throws Exception;
}
