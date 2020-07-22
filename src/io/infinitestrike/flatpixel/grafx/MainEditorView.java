package io.infinitestrike.flatpixel.grafx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import io.infinitestrike.flatpixel.MapEditor;
import io.infinitestrike.flatpixel.Settings;
import io.infinitestrike.flatpixel.components.RenderPane;
import io.infinitestrike.flatpixel.components.RenderPane.InputManager;
import io.infinitestrike.flatpixel.components.RenderPane.RenderCall;
import io.infinitestrike.flatpixel.core.Vector.Vector2i;
import io.infinitestrike.flatpixel.gui.Cursors;
import io.infinitestrike.flatpixel.logging.Console;
import io.infinitestrike.flatpixel.tilemap.Layer;
import io.infinitestrike.flatpixel.tilemap.Map;
import io.infinitestrike.flatpixel.tilemap.TileMap;
import io.infinitestrike.utils.FontUtils;

public class MainEditorView extends RenderCall {

	private Map m = null;
	private Layer baseLayer = null;

	// For Click and Drag
	private int mouseLastX = 0;
	private int mouseLastY = 0;
	private float xoffset = 0;
	private float yoffset = 0;

	// demo only

	public MainEditorView() {}

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

		if (replace != null) {
			this.m = replace;
			Settings.getMapSettings().tileSize = m.getTileSize();
			
			Settings.getMapSettings().tileMap = m.getTileMap();
			Settings.getMapSettings().tileCellsW = m.getHorizontalCellCount();
			Settings.getMapSettings().tileCellsH = m.getVerticalCellCount();
			Settings.getMapSettings().tileSetImage = m.getTileSetImage();

			MapEditor.getApplicationWindow().setCurrentTileSet(Settings.getMapSettings().tileSetImage);
		} else {
			
			this.m = new Map(
					Settings.getMapSettings().tileSetImage,
					Settings.getMapSettings().tileCellsW,
					Settings.getMapSettings().tileCellsH,
					Settings.getMapSettings().tileSize
			);
			TileMap tm = new TileMap(this.m);
			Settings.getMapSettings().tileMap = tm;
			
			this.baseLayer = new Layer(this.getMap());
		}

		this.getParent().addDrawable(m);
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
					activeLayer.setTile(pos.x, pos.y, Settings.getMapSettings().currentlySelectedTile);
				} else if (Settings.editorDrawMode == Settings.FILL_MODE_FLOOD) {
					this.getMap().floodFillTile(pos.x, pos.y, Settings.getMapSettings().currentlySelectedTile);
				}
				Settings.isSaved = false;
			} else {
				// Console.Log("[MainEditor] No Active Layer.");
			}
			Settings.getMapSettings().mapSaved = false;
		}

		if (m.MOUSE_RIGHT && this.getMap().isMouseOverMap()) {
			Vector2i pos = this.getMap().getCell(m.MOUSE_X, m.MOUSE_Y);
			Layer activeLayer = this.getMap().getActiveLayer();
			if (activeLayer != null) {
				this.getMap().getActiveLayer().setTile(pos.x, pos.y, Layer.TILE_ID_EMPTY);
				Settings.isSaved = false;
			} else {
				// Console.Log("[MainEditor] No Active Layer.");
			}
			
			Settings.getMapSettings().mapSaved = false;
		}

		this.getMap().setOffset((int) xoffset, (int) yoffset);

		mouseLastX = c.getInputManager().MOUSE_X;
		mouseLastY = c.getInputManager().MOUSE_Y;
		
		if (c.getInputManager().MOUSE_MIDDLE)
			c.setCursor(Cursors.PIXEL_POINTER_GRAB);
		if (!c.getInputManager().MOUSE_MIDDLE)
			c.setCursor(Cursors.PIXEL_POINTER_CURSOR);
	}

	@Override
	public void OnRender(Graphics g, RenderPane c) {
		// TODO Auto-generated method stub

		int y = c.getHeight() - 10;
		int x = 10;
		g.setFont(FontUtils.VHS);
		g.setColor(Color.white);
		g.drawString("Offset: (" + xoffset + "," + yoffset + ")", x, y);
		g.drawString("Selected Layer: " + this.getMap().getCurrentLayerIndex(), x + 250, y);
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
		Console.Log("Debug: Layer Ammount: " + m.getLayers().size());
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

	@Override
	public void OnInputAttach(InputManager m, RenderPane p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnInputDetatch(RenderPane p) {
		// TODO Auto-generated method stub
		
	}

}
