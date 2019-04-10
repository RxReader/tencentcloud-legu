package io.github.v7lin.tasks;

public abstract class SyncTask<R> {

    public abstract R execute() throws Exception;
}
