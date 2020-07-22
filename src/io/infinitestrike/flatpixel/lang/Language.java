package io.infinitestrike.flatpixel.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import io.infinitestrike.flatpixel.Settings;
import io.infinitestrike.flatpixel.compat.Loader.LoaderEntry;
import io.infinitestrike.flatpixel.compat.Loader.LoaderType;
import io.infinitestrike.flatpixel.core.Core;
import io.infinitestrike.flatpixel.core.ResourceManager;
import io.infinitestrike.flatpixel.core.Strings;
import io.infinitestrike.flatpixel.logging.Console;
import io.infinitestrike.utils.TextFromFile;

public class Language {
	public static final Language languageDictionary = new Language();

	private final HashMap<String, Strings> loadedMaps = new HashMap<String, Strings>();

	private Language() {

	}

	public void load() {
		ResourceManager.step = "Loading Language";
		File languageDir = Core.ensureDirectoryExists(Settings.LANGUAGE_PATH);
		// load all of the languages
		if (languageDir != null) {

			Console.Log("Language Path: " + languageDir.getPath());
			File[] filesInDirectory = languageDir.listFiles();
			if (filesInDirectory != null && filesInDirectory.length > 0) {
				for (File f : filesInDirectory) {
					if (f.getName().contains(".lang")) {
						Strings languageStringStruct = new Strings();
						String languageStrings = "";
						try {
							languageStrings = TextFromFile.getText(f).trim();
							String[] lines = languageStrings.split("\n");
							for(int i = 0; i < lines.length; i++) {
								if(lines[i].contains("##")) continue;
								String key = lines[i].split("=")[0];
								String value = lines[i].split("=")[1].replace(";", "");
								languageStringStruct.registerString(key, value);
							}
							loadedMaps.put(f.getName().replace(".lang", ""), languageStringStruct);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							Console.Error("Error Loading Language: ", e);
						}
					}
				}
			}
		} else {
			Console.NotifyUserError(new NullPointerException("Cannot Load Language File."), true);
		}

		Console.Log("Loaded Languages");
		String languages = "";
		for (Object obj : loadedMaps.keySet().toArray()) {
			Console.Log("Loaded Entry: " + obj);
			languages += obj + ",";
		}
		languages = languages.substring(0, languages.lastIndexOf(","));
		Settings.getSettings().set("$LANGUAGE_LIST", new LoaderEntry(LoaderType.ARRAY, languages));
	}

	public Strings getLanguage(String language) {
		return this.loadedMaps.get(language);
	}

}
