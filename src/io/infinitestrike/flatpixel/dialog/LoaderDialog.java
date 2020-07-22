package io.infinitestrike.flatpixel.dialog;

import java.awt.Component;

import javax.swing.JPanel;

import io.infinitestrike.flatpixel.components.LoaderPane;
import io.infinitestrike.flatpixel.components.WindowWrapper;

public class LoaderDialog extends WindowWrapper{
	// Singleton
	public static final LoaderDialog instance = new LoaderDialog(new LoaderPane());
	
	public LoaderDialog(JPanel pane) {
		super(pane, null);
		// TODO Auto-generated constructor stub
	}

}
