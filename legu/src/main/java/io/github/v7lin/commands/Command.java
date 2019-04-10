package io.github.v7lin.commands;

public abstract class Command {

    public final void execute() throws Exception {
        checkArgs();
        exec();
    }

    protected abstract void checkArgs();

    protected abstract void exec() throws Exception;
}
