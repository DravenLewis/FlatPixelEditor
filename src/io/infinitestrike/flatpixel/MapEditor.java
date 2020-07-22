package io.infinitestrike.flatpixel;

import java.awt.EventQueue;

import javax.swing.JFrame;

import io.infinitestrike.flatpixel.core.Core;
import io.infinitestrike.flatpixel.core.ResourceManager;
import io.infinitestrike.flatpixel.core.Task;
import io.infinitestrike.flatpixel.gui.EditorWindow;
import io.infinitestrike.flatpixel.logging.Console;
import io.infinitestrike.flatpixel.logging.ErrorCode;

public class MapEditor {

	private static EditorWindow window = null;
	private static boolean debug = false;
	
	public static void main(String[] args) throws Exception {
		
		JFrame frame = Splash.spawnSplashScreen();
		frame.setVisible(true);
		ResourceManager.loadResources();
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				EditorWindow w = new EditorWindow();
				window = w;
				window.setIconImage(ResourceManager.APPLICATION_ICON);
				Core.closeFrame(frame);
			}
		});
	}
	
	public static EditorWindow getApplicationWindow() {
		return window;
	}
	
	public static boolean isDebug() {
		return debug;
	}
	
	public static void ErrorExit(ErrorCode status) {
		Task.stopAll();
		Console.Log("An Error Occured with code: " + status.code + " Error Type: " + status.name);
		System.exit(status.code);
	}
}
