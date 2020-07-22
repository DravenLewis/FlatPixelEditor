package io.infinitestrike.flatpixel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import io.infinitestrike.flatpixel.compat.Loader;
import io.infinitestrike.flatpixel.compat.Loader.LoaderResult;
import io.infinitestrike.flatpixel.core.ResourceManager;
import io.infinitestrike.flatpixel.core.Strings;
import io.infinitestrike.flatpixel.lang.Language;
import io.infinitestrike.flatpixel.logging.Console;
import io.infinitestrike.flatpixel.tilemap.TileMap;
import io.infinitestrike.utils.TextFromFile;

public class Settings {
	// Fill Modes, Externalize to Editor.
	public static final int FILL_MODE_POINT = 0;
	public static final int FILL_MODE_FLOOD = 1;

	// Current Map Settings
	private static final MapSettings MapSettings = new MapSettings();
	public static TileMap globalTileMap = null;
	public static boolean drawGridBox = true;
	public static int editorDrawMode = FILL_MODE_POINT;
	public static boolean isSaved = true;
	
	// Current Project Into
	private static File CURRENT_TILESET_FILE = null;
	
	// Loader Settings Loader
	private static final File settings = new File("./preferences.settings");
	private static LoaderResult settingsFile = null;
	public static String[] userPresets = null;

	// Global File Locations
	public static String OPTION_PATH = "";
	public static String USER_PRESET_PATH = "";
	public static String LANGUAGE_PATH = "";

	// Language Files
	private static String language = "";

	public static void loadSettings() {
		try {
			ResourceManager.step = "Reading Settings File";
			settingsFile = Loader.readFile(settings);
		}catch(Exception e) {
			Console.NotifyUserError(e, true);
		}
		
		ResourceManager.step = "Applying Settings";
		// settings
		OPTION_PATH = Loader.getValueString(settingsFile.valueOf("$OPTION_PATH", "./prefs"));
		USER_PRESET_PATH = Loader.getValueString(settingsFile.valueOf("$USER_PRESET_PATH", "./prefs/userPresets"));
		LANGUAGE_PATH = Loader.getValueString(settingsFile.valueOf("$LANGUAGE_PATH", "./prefs/lang"));
		language = Loader.getValueOption(settingsFile.valueOf("$LANGUAGE_SETTING","en_us"), settingsFile);
		// userpresets
		userPresets = loadUserPresets();
	}
	
	public static File getSettingsFile() {
		return Settings.settings;
	}
	
	public static LoaderResult getSettings() {
		return Settings.settingsFile;
	}
	
	public static Strings getLanguage() {
		return Language.languageDictionary.getLanguage(language);
	}

	public static MapSettings getMapSettings() {
		return Settings.MapSettings;
	}
	
	public static void reloadUserPresets() {
		userPresets = loadUserPresets();
	}

	public static File getCurrentTilesetFile() {
		return Settings.CURRENT_TILESET_FILE;
	}
	
	public static void setCurrentTilesetFile(File f) {
		Settings.CURRENT_TILESET_FILE = f;
	}
	
	public static void saveSettingsFile() {
		Console.Log("Saving Settings");
		String s = Loader.LoaderResultToString(settingsFile);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(settings));
			writer.append(s);
			writer.flush();
			
			Console.Log("Settings Saved!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Console.Error("Cannot Write Settings File", e);
		} finally {
			try {
				if(writer != null) writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Console.Error("Cannot Close Buffer", e);
			}
		}
	}
	
	private static String[] loadUserPresets() {
		// TODO Auto-generated method stub
		
		ResourceManager.step = "Loading user tile map presets";
		
		if (!new File(Settings.USER_PRESET_PATH).exists()) {
			if (!new File(Settings.USER_PRESET_PATH).mkdirs()) {
				Console.Log("Falied to create directory.");
			}
		}

		ArrayList<String> s = new ArrayList<String>();

		File[] files = new File(Settings.USER_PRESET_PATH).listFiles();
		for (File f : files) {
			if (!f.getName().contains(".preset"))
				continue;
			String[] lines = null;
			try {
				lines = TextFromFile.getText(f).split("\n");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				Console.Error("Cannot Load File", e);
			}
			for (String st : lines) {
				s.add(st);
			}
		}

		String[] out = new String[s.size()];
		for (int i = 0; i < out.length; i++)
			out[i] = s.get(i);

		return out;
	}
}
