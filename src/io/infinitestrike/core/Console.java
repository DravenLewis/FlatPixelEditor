package io.infinitestrike.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.swing.JOptionPane;

import io.infinitestrike.flatpixelutls.Loader;
import io.infintestrike.editor.Settings;

public class Console {

	private static boolean writeOut = true;
	private static boolean includeStackTraces = true;
	private static String fileData = "";
	private static boolean errorOpen = false;
	private static int maxError = 2;
	private static int errorCount = 0;

	public static void setWriteOut(boolean b) {
		Console.writeOut = b;
	}

	public static void setIncludeStackTraces(boolean f) {
		Console.includeStackTraces = f;
	}

	public static final File logDirectory = new File("./logs/");
	private static File logFile = null;
	private static PrintWriter writer = null;

	public static String Log(Object o) {
		String message = "[" + new Date() + "] " + o;
		Console.writeFile(message);
		System.out.println(message);
		return message;
	}

	public static String Error(String message, Exception e) {
		if (e != null) {
			if (Console.includeStackTraces) {
				e.printStackTrace();
			}
			return Log(String.format("[%s] %s; %s", e.getClass().getName(), e.getMessage(), message));
		}else {
			return Log(String.format("[%s] %s; %s", "UnknownSource", "UnknownSource", message));
		}
	}

	public static void NotifyUserError(Exception e, boolean fatal) {

		if (Console.errorOpen == true) {
			if (Console.errorCount > Console.maxError) {
				Console.Log("Too Many Errors open, crash immenant? Closing Down!!");
				Console.ErrorExit(ErrorCode.EXIT_FATAL_ERROR);
			}
			Console.Log("Error Called While Error Open");
			Console.errorCount++;
		}

		Console.errorOpen = true;
		String message = Error("An Unknown Error Has Occured", e)
				+ (fatal ? "\n\n Would you like to abort the application?" : "");
		int error = JOptionPane.showConfirmDialog(null, message, (fatal ? "Fatal" : "") + " Exception Error",
				JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

		if (fatal || error == JOptionPane.YES_OPTION) {
			ErrorExit(ErrorCode.EXIT_FATAL_ERROR);
		}
		Console.errorOpen = false;
	}

	public static void NotifyUserError(String s, Exception e, boolean fatal) {

		if (Console.errorOpen == true) {
			if (Console.errorCount > Console.maxError) {
				Console.Log("Too Many Errors open, crash immenant? Closing Down!!");
				Console.ErrorExit(ErrorCode.EXIT_FATAL_ERROR);
			}
			Console.Log("Error Called While Error Open");
			Console.errorCount++;
		}

		String message = Error(s, e) + (fatal ? "\n\n Would you like to abort the application?" : "");
		int error = JOptionPane.showConfirmDialog(null, message, (fatal ? "Fatal" : "") + " Exception Error",
				JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
		if (fatal || error == JOptionPane.YES_OPTION) {
			ErrorExit(ErrorCode.EXIT_FATAL_ERROR);
		}
		Console.errorOpen = false;
	}

	public static void alert(String s) {
		JOptionPane.showMessageDialog(null, s);
	}

	public static String getCurrentFileName() {
		return Console.logFile.getName();
	}

	private static void validateAndOpenFile() {
		if (Console.writeOut) {
			if (!Console.logDirectory.exists()) {
				boolean success = Console.logDirectory.mkdirs();
				if (!success) {
					Console.writeOut = false;
					Console.Log("Cannot Create / Open Log Directory");
				}
			}

			logFile = new File(logDirectory.getPath() + "/log-" + System.currentTimeMillis() + ".log");
			try {
				writer = new PrintWriter(new FileWriter(logFile));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Console.Error("", e);
			}
		}
	}

	private static void writeFile(String s) {
		if (logFile == null)
			validateAndOpenFile();
		if (Console.writer != null) {
			Console.writer.println(s);
		}
	}

	public static void Save() {
		if (Console.writer != null) {
			Console.writer.flush();
		}
	}

	// Move to Map Editor?
	public static void ErrorExit(ErrorCode status) {
		Console.Log("An Error Occured with code: " + status.code + " Error Type: " + status.name);
		if(Loader.getValueBoolean(Settings.settingsFile.valueOf("$SAVE_CONSOLE_TO_FILE", "true"))) {
			Save();
		}
		if (Settings.settingsFile != null) {
			Settings.saveSettingsFile();
		}
		Console.writer.close();
		System.exit(status.code);
	}
}
