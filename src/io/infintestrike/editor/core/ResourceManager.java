package io.infintestrike.editor.core;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Properties;

import javax.swing.UIManager;

import io.infinitestrike.core.Console;
import io.infinitestrike.editor.dialog.OutputConsole;
import io.infinitestrike.flatpixelutls.Loader;
import io.infintestrike.editor.Settings;
import io.infintestrike.editor.lang.Language;
import io.infintestrike.utils.ImageTools;

public class ResourceManager {
	
	public static String step = "Application Starting";
	
	public static BufferedImage APPLICATION_ICONS = null;
	public static BufferedImage DEBUGGER_ICON = null;
	public static BufferedImage APPLICATION_ICON = null;
	
	private static boolean done = false;
	
	// Cannot Construct Singleton
	private ResourceManager() {};
	
	public static void loadResources() throws Exception{
		
		
		ResourceManager.step = "Loading Images";
		APPLICATION_ICONS = ImageTools.getImage("./res/UI/UI_Icons.png");
		DEBUGGER_ICON = APPLICATION_ICONS.getSubimage(0 * 64, 0 * 64, 64, 64);
		APPLICATION_ICON = APPLICATION_ICONS.getSubimage(1 * 64, 0 * 64, 64, 64);
		OutputConsole.load();
		Settings.loadSettings();
		OutputConsole.postSettingsLoad();
		
		if(Loader.getValueBoolean(Settings.settingsFile.valueOf("$USE_NATIVE_LF", "true"))) {
			try {
				// Set System L&F
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				// handle exception
				Console.Error("Cannot Set Native Look", e);
			}
		}
		
		Language.languageDictionary.load();
		
		Console.Log("Hello Lets get Creative :)");
		Console.Log("The Current Time is: " + new Date());
		Console.Log("============================================================================");
		Console.Log("Lets Get Some Info For the Egg Heads Up-Stairs");
		Console.Log("============================================================================");
		Console.Log("============================================================================");

		Properties props = System.getProperties();
		for (int i = 0; i < props.size(); i++) {
			String name = (String) props.keySet().toArray()[i];
			Console.Log("System.getProperty(" + name + ")  ==  '" + props.getProperty(name) + "'.");
		}
		
		// load options into the UI
		UIManager.put("OptionPane.cancelButtonText", Settings.getLanguage().getValue("$GENERIC_CANCEL"));
		UIManager.put("OptionPane.yesButtonText", Settings.getLanguage().getValue("$GENERIC_CONFIRM"));
		UIManager.put("OptionPane.noButtonText", Settings.getLanguage().getValue("$GENERIC_DENY"));
		UIManager.put("OptionPane.okButtonText", Settings.getLanguage().getValue("$GENERIC_DISMISS"));

		done = true;
	}
	
	public static boolean isDone(){
		return done;
	}
}
