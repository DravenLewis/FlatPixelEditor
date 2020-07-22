package io.infinitestrike.flatpixel.core;

import java.util.ArrayList;

import io.infinitestrike.flatpixel.logging.Console;

public abstract class Task implements Runnable{

	private final Thread taskThread;
	private final String taskName;
	private final ThreadErrorWatchdog watchdog;
	
	private static final ArrayList<Task> taskList = new ArrayList<Task>();
	
	private boolean running = false;
	
	public Task(String taskName) {
		this.taskName = taskName;
		this.taskThread = new Thread(this,taskName);
		this.watchdog = new ThreadErrorWatchdog(this.taskThread);
	}
	
	public final void start() {
		taskList.add(this);
		this.running = true;
		this.taskStart(this);
		this.taskThread.start();
	}
	
	public synchronized void stop() {
		try {
			this.running = false;
			this.taskDone();
			this.taskThread.join();
			taskList.remove(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		this.taskRun(this);
		this.stop();
	}
	
	
	public abstract void taskStart(Task t);
	public abstract void taskRun(Task t);
	public abstract void taskDone();
	
	public static void stopAll() {
		Console.Log("[Task::StopAllThreads] Stopping All Running Threads");
		for(int i = 0; i < taskList.size(); i++) {
			Task t = taskList.get(i);
			Console.Log("[Task::StopAllThreads] Stopping Thread: " + t.taskName);
			t.stop();
			Console.Log("[Task::StopAllThreads] Thread " + t.taskName + " stopped.");
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean exit) {
		this.running = exit;
	}
}
