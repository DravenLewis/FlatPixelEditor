package io.infintestrike.editor.cursor;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.UUID;

public class Cursors {
	
	public static final Cursor PIXEL_POINTER_CURSOR = Cursors.createCursor("./res/UI/UI_Cursor_Pointer_2.png");
	public static final Cursor PIXEL_POINTER_HAND = Cursors.createCursor("./res/UI/UI_Cursor_Pointer.png");
	public static final Cursor PIXEL_POINTER_GRAB = Cursors.createCursor("./res/UI/UI_Cursor_Grab.png");
	
	public static Cursor createCursor(String location) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image img = tk.getImage(location);
		Cursor c = tk.createCustomCursor(img,new Point(0,0),"cursor:" + UUID.randomUUID());
		return c;
	}
}
