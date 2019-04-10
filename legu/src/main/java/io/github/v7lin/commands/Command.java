package io.github.v7lin.commands;

public abstract class Command {

    public final void exec() throws Exception {
        checkArgs();
        execute();
    }

    protected abstract void checkArgs();

    protected abstract void execute() throws Exception;
}
