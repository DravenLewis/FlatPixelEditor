package io.infintestrike.editor.RenderPanes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import io.infintestrike.editor.RenderPane;
import io.infintestrike.editor.RenderPane.InputManager;
import io.infintestrike.editor.RenderPane.RenderCall;
import io.infintestrike.editor.cursor.Cursors;
import io.infintestrike.editor.fonts.Fonts;

public class ImageExplorer extends RenderCall{

	private BufferedImage img = null;
	private Image simg = null;
	
	private float xoffset = 0;
	private float yoffset = 0;
	private int mouseLastX = 0;
	private int mouseLastY = 0;
	private float scale = 1;
	private float lscale = 1;
	
	public ImageExplorer(BufferedImage img) {
		this.img = img;
	}
	
	@Override
	public void OnAttach(RenderPane c) {
		// TODO Auto-generated method stub
		
		c.setCursor(Cursors.PIXEL_POINTER_CURSOR);
		
		mouseLastX = c.getInputManager().MOUSE_X;
		mouseLastY = c.getInputManager().MOUSE_Y;
		
		RenderPane parent = this.getParent();
		parent.setLayout(null);
		
		JButton button = new JButton(new ImageIcon("./res/UI/UI_Zoom_In.png"));
		button.setBounds(2,64,64,64);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setCursor(Cursors.PIXEL_POINTER_HAND);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				scale += 0.1f;
			}
			
		});
		parent.add(button);
		
		JButton button2 = new JButton(new ImageIcon("./res/UI/UI_Zoom_Out.png"));
		button2.setBounds(2,130,64,64);
		button2.setOpaque(false);
		button2.setCursor(Cursors.PIXEL_POINTER_HAND);
		button2.setContentAreaFilled(false);
		button2.setBorderPainted(false);
		button2.setFocusPainted(false);
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				scale -= 0.1f;
			}
			
		});
		parent.add(button2);
	}
	
	@Override
	public void OnTick(double delta, RenderPane c) {
		// TODO Auto-generated method stub
		
		if(c.getInputManager().MOUSE_LEFT) c.setCursor(Cursors.PIXEL_POINTER_GRAB);	
		if(!c.getInputManager().MOUSE_LEFT) c.setCursor(Cursors.PIXEL_POINTER_CURSOR);
		
		scale -= (float) c.getInputManager().getMouseScroll() / 50.0f;
		
		if(c.getInputManager().MOUSE_LEFT) {
			xoffset += (c.getInputManager().MOUSE_X - mouseLastX);
			yoffset += (c.getInputManager().MOUSE_Y - mouseLastY);
		}
		
		mouseLastX = c.getInputManager().MOUSE_X;
		mouseLastY = c.getInputManager().MOUSE_Y;
		
		if(scale <= 0.01f) {scale = 0.01f;}
		//if(scale > 2) {scale = 2;}
		
		
		if(simg == null) simg = img;
		if(lscale != scale) {
			float w = img.getWidth() * scale;
			float h = img.getHeight() * scale;
			simg = img.getScaledInstance(Math.round(w), Math.round(h), BufferedImage.SCALE_FAST);
		}
		
		lscale = scale;
	}

	@Override
	public void OnRender(Graphics g, RenderPane c) {
		// TODO Auto-generated method stub
		float x = ((c.getWidth() / 2) - (img.getWidth() / 2)) + xoffset;
		float y = ((c.getHeight() / 2) - (img.getHeight() / 2)) + yoffset;
		g.drawImage(simg,Math.round(x),Math.round(y),null);
		Font f = g.getFont();
		g.setFont(Fonts.changeFontSize(Fonts.VCR,30.0f));
		g.setColor(Color.white);
		g.drawString("Zoom: " + Math.round(((scale / 1) * 100)) + "%", 25, 35);
		g.setFont(f);
	}

	@Override
	public void OnDetatch(RenderPane c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAttach(InputManager m, RenderPane p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyTyped(KeyEvent e, InputManager m, RenderPane p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyPressed(KeyEvent e, InputManager m, RenderPane p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyReleased(KeyEvent e, InputManager m, RenderPane p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnMouseWheelMoved(MouseWheelEvent e, InputManager m, RenderPane p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnMouseDragged(MouseEvent e, InputManager m, RenderPane p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnMouseMoved(MouseEvent e, InputManager m, RenderPane p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnMouseClicked(MouseEvent e, InputManager m, RenderPane p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnMousePressed(MouseEvent e, InputManager m, RenderPane p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnMouseReleased(MouseEvent e, InputManager m, RenderPane p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnMouseEntered(MouseEvent e, InputManager m, RenderPane p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnMouseExited(MouseEvent e, InputManager m, RenderPane p) {
		// TODO Auto-generated method stub
		
	}

}
