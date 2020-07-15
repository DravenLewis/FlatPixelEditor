package io.infintestrike.editor.RenderPanes;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import io.infinitestrike.core.Console;
import io.infinitestrike.core.Vector.Vector2i;
import io.infintestrike.editor.EditorWindow;
import io.infintestrike.editor.MapEditor;
import io.infintestrike.editor.RenderPane;
import io.infintestrike.editor.RenderPane.InputManager;
import io.infintestrike.editor.RenderPane.RenderCall;
import io.infintestrike.editor.Settings;
import io.infintestrike.editor.cursor.Cursors;
import io.infintestrike.editor.tilemap.Layer;
import io.infintestrike.editor.tilemap.Map;

public class MainEditor extends RenderCall {

	private Map m = null;
	private int ts;
	private EditorWindow parent = null;
	private Layer baseLayer = null;

	// For Click and Drag
	private int mouseLastX = 0;
	private int mouseLastY = 0;
	private float xoffset = 0;
	private float yoffset = 0;

	// demo only

	public MainEditor(EditorWindow w, int ts) {
		this.parent = w;
		this.ts = ts;

	}

	@Override
	public void OnAttach(RenderPane c) {
		// TODO Auto-generated method stub
		this.getParent().setCursor(Cursors.PIXEL_POINTER_CURSOR);
		this.UpdateTileMap(null);
	}

	public void ChangeMapTileSet(BufferedImage img) {
		if (m != null) {
			this.getMap().setTileSetImage(img);
		}
	}

	public void UpdateTileMap(Map replace) {
		if (m != null) {
			this.getParent().removeDrawable(m);
		}

		if(replace != null) {
			this.m = replace;
			this.ts = m.getTileSize();
			Settings.MapTileSize = m.getTileSize();
			Settings.MapCellWidth = m.getHorizontalCellCount();
			Settings.MapCellHeight = m.getVerticalCellCount();
			Settings.CurrentTileSet = m.getTileSetImage();
			
			MapEditor.applicationWindow.tilesetImage = Settings.CurrentTileSet;
		}else {
			this.m = new Map(parent.tilesetImage, Settings.MapCellWidth, Settings.MapCellHeight, this.ts);
		}
		
		this.getParent().addDrawable(m);

		this.baseLayer = new Layer(this.getMap());
	}

	@Override
	public void OnTick(double delta, RenderPane c) {
		// TODO Auto-generated method stub

		InputManager m = c.getInputManager();

		if (m.MOUSE_MIDDLE || (m.MOUSE_LEFT && m.isKeyDown(KeyEvent.VK_SHIFT))) {
			xoffset += (m.MOUSE_X - mouseLastX);
			yoffset += (m.MOUSE_Y - mouseLastY);
		}

		if (m.MOUSE_LEFT && this.getMap().isMouseOverMap() && !m.isKeyDown(KeyEvent.VK_SHIFT)) {
			Vector2i pos = this.getMap().getCell(m.MOUSE_X, m.MOUSE_Y);

			Layer activeLayer = this.getMap().getActiveLayer();
			if (activeLayer != null) {
				if (Settings.editorDrawMode == Settings.FILL_MODE_POINT) {
					activeLayer.setTile(pos.x, pos.y, Settings.CurrentlySelectedTile);
				} else if (Settings.editorDrawMode == Settings.FILL_MODE_FLOOD) {
					this.getMap().floodFillTile(pos.x, pos.y, Settings.CurrentlySelectedTile);
				}
			} else {
				//Console.Log("[MainEditor] No Active Layer.");
			}
		}

		if (m.MOUSE_RIGHT && this.getMap().isMouseOverMap()) {
			Vector2i pos = this.getMap().getCell(m.MOUSE_X, m.MOUSE_Y);
			Layer activeLayer = this.getMap().getActiveLayer();
			if (activeLayer != null) {
				this.getMap().getActiveLayer().setTile(pos.x, pos.y, Layer.TILE_ID_EMPTY);
			} else {
				Console.Log("[MainEditor] No Active Layer.");
			}
		}

		this.getMap().setOffset((int) xoffset, (int) yoffset);

		mouseLastX = c.getInputManager().MOUSE_X;
		mouseLastY = c.getInputManager().MOUSE_Y;
	}

	@Override
	public void OnRender(Graphics g, RenderPane c) {
		// TODO Auto-generated method stub

		if (c.getInputManager().MOUSE_MIDDLE)
			c.setCursor(Cursors.PIXEL_POINTER_GRAB);
		if (!c.getInputManager().MOUSE_MIDDLE)
			c.setCursor(Cursors.PIXEL_POINTER_CURSOR);
	}

	@Override
	public void OnDetatch(RenderPane c) {
		// TODO Auto-generated method stub
		m.getLayers().clear();
		if (m != null) {
			this.getParent().removeDrawable(m);
		}
	}

	public Map getMap() {
		return this.m;
	}

	
	public void setMap(Map m) {
		this.UpdateTileMap(m);
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
