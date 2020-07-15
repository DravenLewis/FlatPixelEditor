package io.infintestrike.editor.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import io.infinitestrike.core.Console;
import io.infinitestrike.core.Core;
import io.infinitestrike.flatpixelutls.Loader.LoaderEntry;
import io.infinitestrike.flatpixelutls.Loader.LoaderType;
import io.infintestrike.editor.Settings;
import io.infintestrike.editor.core.ResourceManager;
import io.infintestrike.editor.core.Strings;
import io.infintestrike.utils.TextFromFile;

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
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						languageStrings = languageStrings.replace("\n", "");
						languageStrings = languageStrings.replace("##", "");
						String[] lines = languageStrings.split(";");
						for (String entry : lines) {
							String left = entry.split("=")[0];
							String right = entry.split("=")[1];
							languageStringStruct.registerString(left, right);
						}
						loadedMaps.put(f.getName().replace(".lang", ""), languageStringStruct);
					}
				}
			}
		}else {
			Console.NotifyUserError(new NullPointerException("Cannot Load Language File."), true);
		}

		
		Console.Log("Loaded Languages");
		String languages = "";
		for(Object obj : loadedMaps.keySet().toArray()) {
			Console.Log("Loaded Entry: " + obj);
			languages += obj + ",";
		}
		languages = languages.substring(0, languages.lastIndexOf(","));
		Settings.settingsFile.set("$LANGUAGE_LIST", new LoaderEntry(LoaderType.ARRAY,languages));
	}
	
	public Strings getLanguage(String language) {
		return this.loadedMaps.get(language);
	}

}
