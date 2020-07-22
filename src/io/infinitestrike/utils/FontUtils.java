package io.infinitestrike.utils;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

import io.infinitestrike.flatpixel.logging.Console;

public class FontUtils {
	
	
	public static final Font VHS = getFont("./res/UI/font/VCR.ttf", 18);
	
	public static Font getSystemFont(String name, float size) {
		Font systemFont = Font.getFont(name);
		return changeFontSize(systemFont,size);
	}
	
	public static Font getFont(String path, float size) {
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font f = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
			ge.registerFont(f);
			return f;
		} catch (Exception e) {
			Console.Error("Cant Register Font", e);
		}
		return null;
	}

	public static Font changeFontSize(Font f, float size) {
		return f.deriveFont(size);
	}
}
