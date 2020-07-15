package io.infintestrike.editor;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import io.infinitestrike.core.Console;
import io.infinitestrike.flatpixelutls.Loader;
import io.infinitestrike.flatpixelutls.Loader.LoaderResult;
import io.infintestrike.editor.core.ResourceManager;
import io.infintestrike.editor.core.Strings;
import io.infintestrike.editor.lang.Language;
import io.infintestrike.editor.tilemap.TileMap;
import io.infintestrike.utils.TextFromFile;

public class Settings {
	public static final int FILL_MODE_POINT = 0;
	public static final int FILL_MODE_FLOOD = 1;

	public static int MapTileSize = 32;
	public static int MapCellWidth = 20;
	public static int MapCellHeight = 20;
	public static int CurrentlySelectedTile = 0;

	// Multi Tile Select
	public static boolean multiple = true;

	public static BufferedImage CurrentTileSet = null;

	public static TileMap globalTileMap = null;
	public static boolean drawGridBox = true;
	public static int editorDrawMode = FILL_MODE_POINT;
	public static boolean isSaved = true;
	public static String lastSavedPath = "";
	
	// Loader Settings Loader
	public static final File settings = new File("./preferences.settings");
	public static LoaderResult settingsFile = null;

	public static String OPTION_PATH = "";
	public static String USER_PRESET_PATH = "";
	public static String LANGUAGE_PATH = "";

	public static File CURRENT_TILESET_FILE = null;
	
	private static String language = "";

	public static String[] userPresets = null;

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
	
	public static Strings getLanguage() {
		return Language.languageDictionary.getLanguage(language);
	}

	public static void reloadUserPresets() {
		userPresets = loadUserPresets();
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
