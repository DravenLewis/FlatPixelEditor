package io.infintestrike.editor.RenderPanes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import io.infinitestrike.core.Console;
import io.infinitestrike.core.Vector.Vector2i;
import io.infinitestrike.flatpixelutls.FPMath;
import io.infinitestrike.flatpixelutls.Loader;
import io.infintestrike.editor.RenderPane;
import io.infintestrike.editor.RenderPane.InputManager;
import io.infintestrike.editor.RenderPane.RenderCall;
import io.infintestrike.editor.Settings;
import io.infintestrike.editor.cursor.Cursors;
import io.infintestrike.editor.tilemap.TileMap;
import io.infintestrike.utils.ImageTools;

public class TileSelectorPane extends RenderCall {

	// For Click and Drag
	private int mouseLastX = 0;
	private int mouseLastY = 0;
	private float xoffset = 0;
	private float yoffset = 0;

	private BufferedImage gridImage = null;
	private BufferedImage mapImage = null;
	
	private int TileSetSize = 0;
	private Vector2i selected = new Vector2i(0,0);
	
	@Override
	public void OnAttach(RenderPane c) {
		// TODO Auto-generated method stub
		c.setCursor(Cursors.PIXEL_POINTER_CURSOR);
	}

	@Override
	public void OnTick(double delta, RenderPane c) {
		// TODO Auto-generated method stub
		InputManager m = c.getInputManager();

		if (m.MOUSE_MIDDLE || (m.MOUSE_LEFT && m.isKeyDown(KeyEvent.VK_SHIFT))) {
			xoffset += (m.MOUSE_X - mouseLastX);
			yoffset += (m.MOUSE_Y - mouseLastY);
		}

		if (Settings.CurrentTileSet != null) {
			if (gridImage != null) {
				if (gridImage.getHeight() != Settings.CurrentTileSet.getHeight() || 
					gridImage.getWidth() != Settings.CurrentTileSet.getWidth() ||
					this.TileSetSize != Settings.MapTileSize)
				
				{
					gridImage = new BufferedImage(Settings.CurrentTileSet.getWidth(),
							Settings.CurrentTileSet.getHeight(), BufferedImage.TYPE_INT_ARGB);
					
					this.TileSetSize = Settings.MapTileSize;
					
					generateGrid(gridImage);
					this.mapImage = this.drawLoadedTileMap();
				}
			} else {
				gridImage = new BufferedImage(Settings.CurrentTileSet.getWidth() + 1, Settings.CurrentTileSet.getHeight() + 1,
						BufferedImage.TYPE_INT_ARGB);
				generateGrid(gridImage);
				this.mapImage = this.drawLoadedTileMap();
			}
		}

		mouseLastX = c.getInputManager().MOUSE_X;
		mouseLastY = c.getInputManager().MOUSE_Y;
		
		
		if(m.MOUSE_LEFT && Settings.CurrentTileSet != null) {
			Vector2i tileID = FPMath.getCell(
					new Vector2i((int)xoffset,(int)yoffset), 
					new Vector2i(m.MOUSE_X,m.MOUSE_Y), 
					new Vector2i(Settings.CurrentTileSet.getWidth(),Settings.CurrentTileSet.getHeight()),
					Settings.MapTileSize);
			
			this.selected = tileID;
			Vector2i flipped = new Vector2i(tileID.y,tileID.x);
			
			
			Settings.CurrentlySelectedTile = flipped.y + flipped.x * (Settings.CurrentTileSet.getWidth() / Settings.MapTileSize);
		}
	}

	@Override
	public void OnRender(Graphics g, RenderPane c) {
		// TODO Auto-generated method stub
		g.drawImage(ImageTools.TRASN_CHECKER_D, 0, 0, null);
		if (Settings.CurrentTileSet != null) {
			g.drawImage(mapImage, (int) xoffset, (int) yoffset, null);
		}
		
		

		if (this.gridImage != null) {
			g.drawImage(this.gridImage, (int) xoffset, (int) yoffset, null);
		}
		
		g.setColor(Color.BLUE);
		g.drawRect((int) xoffset + (selected.x * Settings.MapTileSize),(int) yoffset + (selected.y * Settings.MapTileSize), Settings.MapTileSize, Settings.MapTileSize);
	}

	@Override
	public void OnDetatch(RenderPane c) {
		// TODO Auto-generated method stub

	}

	
	private BufferedImage drawLoadedTileMap() {
		BufferedImage img = new BufferedImage(Settings.CurrentTileSet.getWidth(),Settings.CurrentTileSet.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		if(Settings.globalTileMap != null) {
			TileMap map = Settings.globalTileMap;
			int rx = img.getWidth() / Settings.MapTileSize;
			int ry = img.getHeight() / Settings.MapTileSize;
			for(int x = 0; x < rx; x++) {
				for(int y = 0; y < ry; y++) {
					if((x+y * rx) >= (img.getWidth() * img.getHeight())) continue;
					
					g.drawImage(map.getTile(x + y * rx).getTileImage(map.getParent().getTileSetImage()), x * Settings.MapTileSize, y * Settings.MapTileSize, null);
					//[$Temp]
					//g.drawString("" + map.getTile(x + y * rx).id, x * Settings.MapTileSize, y * Settings.MapTileSize + Settings.MapTileSize);
				}
			}
		}
		g.dispose();
		return img;
	}
	
	private void generateGrid(BufferedImage img) {
		// Regenerate Grid
		Graphics2D g2d = (Graphics2D) img.getGraphics();

		g2d.setColor(new Color(Loader.getValueHex(Settings.settingsFile.valueOf("$GRID_COLOR", "888888"))));

		for(int x = 0; x < Settings.CurrentTileSet.getWidth() + 1; x+= Settings.MapTileSize) {
			g2d.drawLine(x, 0, x, gridImage.getHeight());
		}

		for(int y = 0; y < Settings.CurrentTileSet.getHeight() + 1; y+= Settings.MapTileSize) {
			g2d.drawLine(0, y, gridImage.getWidth(), y);
		}
		
		g2d.dispose();
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
