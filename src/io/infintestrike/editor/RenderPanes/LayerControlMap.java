package io.infintestrike.editor.RenderPanes;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import io.infintestrike.editor.EditorWindow;
import io.infintestrike.editor.RenderPane;
import io.infintestrike.editor.RenderPane.InputManager;
import io.infintestrike.editor.RenderPane.RenderCall;
import io.infintestrike.utils.ImageTools;

public class LayerControlMap extends RenderCall {

	private final static BufferedImage layerControls = ImageTools.getImage("./res/UI/UI_Layer_Controls.png");
	public final static BufferedImage layerShown = layerControls.getSubimage(0, 0, 16, 16);
	public final static BufferedImage layerHidden = layerControls.getSubimage(16, 16, 16, 16);
	public final static BufferedImage layerRemove = layerControls.getSubimage(0, 16, 16, 16);
	public final static BufferedImage layerAdd = layerControls.getSubimage(32, 0, 16, 16);
	public final static BufferedImage layerMoveUp = layerControls.getSubimage(32, 16, 16, 16);
	public final static BufferedImage layerMoveDown = layerControls.getSubimage(48,16,16,16);
	

	private int lastLayerSize = 0;
	private int scrollOffsetY = 0;
	
	
	@Override
	public void OnAttach(RenderPane c) {
		// TODO Auto-generated method stub
		c.setLayout(new BorderLayout());		
		this.refreshLayers(EditorWindow.currentEditor.getMap().getLayers().size());
	}

	@Override
	public void OnTick(double delta, RenderPane c) {
		// TODO Auto-generated method stub
		if(lastLayerSize != EditorWindow.currentEditor.getMap().getLayers().size()) {
			lastLayerSize = EditorWindow.currentEditor.getMap().getLayers().size();
			this.clear(c);
			refreshLayers(lastLayerSize);
		}
		
	}

	@Override
	public void OnRender(Graphics g, RenderPane c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnDetatch(RenderPane c) {
		// TODO Auto-generated method stub

	}

	public void refreshLayers(int size) {
		for(int i = 0; i < size; i++) {
			JLabel l = new JLabel("Layer " + i);
			l.setBounds(10, 10, this.getParent().getWidth() - 10, 16 + (i * (20)));
			
			JButton btnShow = new JButton();
			btnShow.setIcon(new ImageIcon(LayerControlMap.layerShown));
			btnShow.setBounds(l.getWidth()-32, 0, 16, 16);
			JButton btnRemove = new JButton();
			btnRemove.setIcon(new ImageIcon(LayerControlMap.layerRemove));
			btnRemove.setBounds(l.getWidth()-16, 0, 16, 16);
			
			l.setLayout(null);
			l.add(btnShow);
			l.add(btnRemove);
			
			this.getParent().add(l);
		}
	}
	
	public void clear(RenderPane c) {
		c.removeAll();
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
	
	public void lockSize() {
		int maxSize = (20 + (this.lastLayerSize * (64+20)));
		if(this.scrollOffsetY > 0) this.scrollOffsetY = 0;
		if(this.scrollOffsetY < -(maxSize)) this.scrollOffsetY = -(maxSize);
	}
}
