package io.infinitestrike.flatpixel.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;

import io.infinitestrike.flatpixel.core.Core;
import io.infinitestrike.flatpixel.core.Vector.Vector2i;

public class WindowWrapper {
	
	private String name = "Window";
	private Vector2i size = new Vector2i(640,480);
	private boolean resize = true;

	private JFrame window = null;
	private Image applicationIcon = null;
	private Component parentComponent = null;
	private JPanel contentPanel = null;
	
	public WindowWrapper(JPanel pane, Component parentComponent) {
		this.contentPanel = pane;
		this.parentComponent = parentComponent;
	}
	
	public final void Spawn(JPanel panel, Component parentComponent, Image imageIcon) {
	
		if(this.window != null) {
			this.window.requestFocus();
			return;
		}
		
		JFrame frame = Core.spawnFrame(panel, name, size, parentComponent, resize, true, applicationIcon);
		this.contentPanel = panel;
		this.window = frame;
		this.window.pack();
	};
	
	public final void Spawn() {
		Spawn(this.contentPanel,this.parentComponent,null);
	} 
	
	public final Container getContentPane() {
		return this.contentPanel;
	}
	
	public final void Close() {
		Core.closeFrame(window);
		window = null;
	}
	
	public final JFrame getFrame() {
		return this.window;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final Vector2i getSize() {
		return size;
	}

	public final void setSize(Vector2i size) {
		this.size = size;
	}

	public final boolean isResize() {
		return resize;
	}

	public final void setResize(boolean resize) {
		this.resize = resize;
	}
}
