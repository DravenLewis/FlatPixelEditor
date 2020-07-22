package io.infinitestrike.flatpixel.grafx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import io.infinitestrike.flatpixel.Settings;
import io.infinitestrike.flatpixel.compat.FPMath;
import io.infinitestrike.flatpixel.compat.Loader;
import io.infinitestrike.flatpixel.components.RenderPane;
import io.infinitestrike.flatpixel.components.RenderPane.InputManager;
import io.infinitestrike.flatpixel.components.RenderPane.RenderCall;
import io.infinitestrike.flatpixel.core.Vector.Vector2i;
import io.infinitestrike.flatpixel.gui.Cursors;
import io.infinitestrike.flatpixel.tilemap.TileMap;
import io.infinitestrike.utils.ImageTools;

public class TileSelectorView extends RenderCall {

	// For Click and Drag
	private int mouseLastX = 0;
	private int mouseLastY = 0;
	private float xoffset = 0;
	private float yoffset = 0;

	private BufferedImage gridImage = null;
	private BufferedImage mapImage = null;

	private int TileSetSize = 0;
	private Vector2i selected = new Vector2i(0, 0);

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

		if (Settings.getMapSettings().tileSetImage != null) {
			if (gridImage != null) {
				if (gridImage.getHeight() != Settings.getMapSettings().tileSetImage.getHeight()
						|| gridImage.getWidth() != Settings.getMapSettings().tileSetImage.getWidth()
						|| this.TileSetSize != Settings.getMapSettings().tileSize)

				{
					gridImage = new BufferedImage(Settings.getMapSettings().tileSetImage.getWidth(),
							Settings.getMapSettings().tileSetImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

					this.TileSetSize = Settings.getMapSettings().tileSize;

					generateGrid(gridImage);
					this.mapImage = this.drawLoadedTileMap();
				}
			} else {
				gridImage = new BufferedImage(Settings.getMapSettings().tileSetImage.getWidth() + 1,
						Settings.getMapSettings().tileSetImage.getHeight() + 1, BufferedImage.TYPE_INT_ARGB);
				generateGrid(gridImage);
				this.mapImage = this.drawLoadedTileMap();
			}
		}else {
			if(this.gridImage == null) {
				this.gridImage = new BufferedImage(1024,1024,BufferedImage.TYPE_INT_ARGB);
			}
			generateGrid(this.gridImage);
		}

		mouseLastX = c.getInputManager().MOUSE_X;
		mouseLastY = c.getInputManager().MOUSE_Y;

		if (m.MOUSE_LEFT && Settings.getMapSettings().tileSetImage != null) {
			Vector2i tileID = FPMath
					.getCell(new Vector2i((int) xoffset, (int) yoffset), new Vector2i(m.MOUSE_X, m.MOUSE_Y),
							new Vector2i(Settings.getMapSettings().tileSetImage.getWidth(),
									Settings.getMapSettings().tileSetImage.getHeight()),
							Settings.getMapSettings().tileSize);

			this.selected = tileID;
			Vector2i flipped = new Vector2i(tileID.y, tileID.x);

			Settings.getMapSettings().currentlySelectedTile = flipped.y + flipped.x
					* (Settings.getMapSettings().tileSetImage.getWidth() / Settings.getMapSettings().tileSize);
		}

		if (c.getInputManager().MOUSE_MIDDLE)
			c.setCursor(Cursors.PIXEL_POINTER_GRAB);
		if (!c.getInputManager().MOUSE_MIDDLE)
			c.setCursor(Cursors.PIXEL_POINTER_CURSOR);
	}

	@Override
	public void OnRender(Graphics g, RenderPane c) {
		// TODO Auto-generated method stub
		g.drawImage(ImageTools.TRASN_CHECKER_D, 0, 0, null);
		if (Settings.getMapSettings().tileSetImage != null) {
			g.drawImage(mapImage, (int) xoffset, (int) yoffset, null);
		}

		if (this.gridImage != null) {
			g.drawImage(this.gridImage, (int) xoffset, (int) yoffset, null);
		}

		g.setColor(Color.BLUE);
		g.drawRect((int) xoffset + (selected.x * Settings.getMapSettings().tileSize),
				(int) yoffset + (selected.y * Settings.getMapSettings().tileSize), Settings.getMapSettings().tileSize,
				Settings.getMapSettings().tileSize);
	}

	@Override
	public void OnDetatch(RenderPane c) {
		// TODO Auto-generated method stub

	}

	private BufferedImage drawLoadedTileMap() {
		BufferedImage img = new BufferedImage(Settings.getMapSettings().tileSetImage.getWidth(),
				Settings.getMapSettings().tileSetImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		if (Settings.globalTileMap != null) {
			TileMap map = Settings.globalTileMap;
			int rx = img.getWidth() / Settings.getMapSettings().tileSize;
			int ry = img.getHeight() / Settings.getMapSettings().tileSize;
			for (int x = 0; x < rx; x++) {
				for (int y = 0; y < ry; y++) {
					if ((x + y * rx) >= (img.getWidth() * img.getHeight()))
						continue;

					g.drawImage(map.getTile(x + y * rx).getTileImage(map.getParent().getTileSetImage()),
							x * Settings.getMapSettings().tileSize, y * Settings.getMapSettings().tileSize, null);
					// [$Temp]
					// g.drawString("" + map.getTile(x + y * rx).id, x *
					// Settings.getMapSettings().tileSize, y * Settings.getMapSettings().tileSize +
					// Settings.getMapSettings().tileSize);
				}
			}
		}
		g.dispose();
		return img;
	}

	private void generateGrid(BufferedImage img) {
		// Regenerate Grid
		Graphics2D g2d = (Graphics2D) img.getGraphics();

		g2d.setColor(new Color(Loader.getValueHex(Settings.getSettings().valueOf("$GRID_COLOR", "888888"))));

		if (Settings.getMapSettings().tileSetImage != null) {
			for (int x = 0; x < Settings.getMapSettings().tileSetImage.getWidth()
					+ 1; x += Settings.getMapSettings().tileSize) {
				g2d.drawLine(x, 0, x, gridImage.getHeight());
			}

			for (int y = 0; y < Settings.getMapSettings().tileSetImage.getHeight()
					+ 1; y += Settings.getMapSettings().tileSize) {
				g2d.drawLine(0, y, gridImage.getWidth(), y);
			}
		} else {
			for (int x = 0; x < 1024 + 1; x += 32) {
				g2d.drawLine(x, 0, x, 1024);
			}

			for (int y = 0; y < 1024 + 1; y += 32) {
				g2d.drawLine(0, y, 1024, y);
			}
		}
		
		g2d.drawRect(0, 0, img.getWidth() - 1, img.getHeight() - 1);
		g2d.dispose();
	}

	@Override
	public void OnInputAttach(InputManager m, RenderPane p) {
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

	@Override
	public void OnInputDetatch(RenderPane p) {
		// TODO Auto-generated method stub

	}

}
