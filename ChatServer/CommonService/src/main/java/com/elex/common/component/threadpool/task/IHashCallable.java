package com.elex.common.component.threadpool.task;

import java.util.concurrent.Callable;

public interface IHashCallable<T> extends IHTask, Callable<T> {

}
