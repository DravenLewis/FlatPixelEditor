package io.infinitestrike.flatpixel.core;

import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Properties;

import javax.swing.UIManager;

import io.infinitestrike.flatpixel.Settings;
import io.infinitestrike.flatpixel.compat.Loader;
import io.infinitestrike.flatpixel.components.OutputConsole;
import io.infinitestrike.flatpixel.lang.Language;
import io.infinitestrike.flatpixel.logging.Console;
import io.infinitestrike.utils.ImageTools;

public class ResourceManager {
	
	public static String step = "Application Starting";
	
	private static BufferedImage APPLICATION_ICONS = null;
	
	public static BufferedImage DEBUGGER_ICON = null;
	public static BufferedImage APPLICATION_ICON = null;
	public static BufferedImage FILL_ICON = null;
	public static BufferedImage DRAW_ICON = null;
	
	private static BufferedImage layerControls = null;
	
	public static BufferedImage layerShown = null;
	public static BufferedImage layerHidden = null;
	public static BufferedImage layerRemove = null;
	public static BufferedImage layerAdd = null;
	public static BufferedImage layerMoveUp = null;
	public static BufferedImage layerMoveDown = null;
	
	public static BufferedImage UINotification = null;
	public static BufferedImage UINotificationQuestion = null;
	private static boolean done = false;
	
	// Cannot Construct Singleton
	private ResourceManager() {};
	
	public static void loadResources() throws Exception{
		
		
		ResourceManager.step = "Loading Images";
		APPLICATION_ICONS = ImageTools.getImage("./res/UI/UI_Icons.png");
		DEBUGGER_ICON = APPLICATION_ICONS.getSubimage(0 * 64, 0 * 64, 64, 64);
		APPLICATION_ICON = APPLICATION_ICONS.getSubimage(1 * 64, 0 * 64, 64, 64);
		FILL_ICON = APPLICATION_ICONS.getSubimage(0 * 64, 1 * 64, 64, 64);	
		DRAW_ICON = APPLICATION_ICONS.getSubimage(1 * 64, 1 * 64, 64, 64);
		
		layerControls = ImageTools.getImage("./res/UI/UI_Layer_Controls.png");
		layerShown = layerControls.getSubimage(0, 0, 16, 16);
		layerHidden = layerControls.getSubimage(16, 16, 16, 16);
		layerRemove = layerControls.getSubimage(0, 16, 16, 16);
		layerAdd = layerControls.getSubimage(32, 0, 16, 16);
		layerMoveUp = layerControls.getSubimage(32, 16, 16, 16);
		layerMoveDown = layerControls.getSubimage(48,16,16,16);
		
		UINotification = ImageTools.getImage("./res/UI/UI_Notification.png");
		UINotificationQuestion = ImageTools.getImage("./res/UI/UI_Notification_Question.png");
		
		OutputConsole.load();
		Settings.loadSettings();
		OutputConsole.postSettingsLoad();
		
		if(Loader.getValueBoolean(Settings.getSettings().valueOf("$USE_NATIVE_LF", "true"))) {
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
