package io.infintestrike.editor.fonts;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

import io.infinitestrike.core.Console;

public class Fonts {

	public static final Font VCR = Fonts.getFont("./res/UI/font/VCR.ttf", 18.0f);
	
	public static Font getFont(String path, float size) {
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font f = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
			ge.registerFont(f);
			return f;
		}catch(Exception e) {
			Console.Error("Cant Register Font", e);
		}
		return null;
	}
	
	public static Font changeFontSize(Font f, float size) {
		return f.deriveFont(size);
	}
}
