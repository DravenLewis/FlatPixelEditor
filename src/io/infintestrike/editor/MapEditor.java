package io.infintestrike.editor;

import java.awt.EventQueue;
import java.util.Date;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.UIManager;

import io.infinitestrike.core.Console;
import io.infinitestrike.core.Core;
import io.infinitestrike.editor.dialog.Debugger;
import io.infinitestrike.flatpixelutls.Loader;
import io.infintestrike.editor.core.ResourceManager;

public class MapEditor {

	public static final boolean debug = true;
	public static EditorWindow applicationWindow = null;
	
	public static void main(String[] args) throws InterruptedException {

		JFrame splash = Splash.spawnSplashScreen();
		splash.setVisible(true);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ResourceManager.loadResources();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Console.NotifyUserError(e, true);
				}
			}
		});
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					applicationWindow = new EditorWindow();
					applicationWindow.frame.setVisible(true);
					if(Loader.getValueBoolean(Settings.settingsFile.valueOf("$CONSOLE_OPEN_ON_STARTUP", "false"))) {
						Debugger.Spawn();
					};
					Core.closeFrame(splash);
				} catch (Exception e) {
					Console.NotifyUserError(e, true);
				}
			}
		});
		// }
	}
}
