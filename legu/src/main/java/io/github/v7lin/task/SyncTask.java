package io.github.v7lin.task;

public abstract class SyncTask<R> {

    public abstract R execute() throws Exception;
}
