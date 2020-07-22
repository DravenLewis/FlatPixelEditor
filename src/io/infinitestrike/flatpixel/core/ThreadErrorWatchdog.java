package io.infinitestrike.flatpixel.core;

import java.lang.Thread.UncaughtExceptionHandler;

import io.infinitestrike.flatpixel.logging.Console;

public class ThreadErrorWatchdog implements UncaughtExceptionHandler{
	
	private final Thread targetThread;
	
	public ThreadErrorWatchdog(Thread targetThread) {
		this.targetThread = targetThread;
		targetThread.setUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		// TODO Auto-generated method stub
		Console.Error("Error in thread: " + t.getName(),(Exception) e);
	}
}
