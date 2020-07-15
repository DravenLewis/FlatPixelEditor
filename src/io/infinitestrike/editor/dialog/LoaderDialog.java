package io.infinitestrike.editor.dialog;

import javax.swing.JFrame;

import io.infinitestrike.core.Core;
import io.infinitestrike.core.Vector.Vector2i;
import io.infintestrike.editor.MapEditor;
import io.infintestrike.editor.Settings;

public class LoaderDialog {

	
	private static JFrame frame = null;
	
	public static JFrame Spawn() {
		if(frame != null) {
			frame.requestFocus();
		}
		frame = Core.spawnFrame(new LoaderPane(), Settings.getLanguage().getValue("$GENERIC_LOADING"), new Vector2i(640,480), MapEditor.applicationWindow.frame);
		frame.pack();
		return frame;
	}
}
