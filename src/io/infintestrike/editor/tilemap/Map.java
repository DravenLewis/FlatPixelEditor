package io.infintestrike.editor.tilemap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import io.infinitestrike.core.Console;
import io.infinitestrike.core.Core;
import io.infinitestrike.core.Vector.Vector2i;
import io.infinitestrike.editor.dialog.CreateMapDialog;
import io.infinitestrike.editor.dialog.LoaderDialog;
import io.infinitestrike.flatpixelutls.FPMath;
import io.infinitestrike.flatpixelutls.Loader;
import io.infinitestrike.flatpixelutls.Loader.LoaderEntry;
import io.infinitestrike.flatpixelutls.Loader.LoaderResult;
import io.infintestrike.editor.RenderPane;
import io.infintestrike.editor.RenderPane.Drawable;
import io.infintestrike.editor.RenderPane.InputManager;
import io.infintestrike.editor.Settings;
import io.infintestrike.utils.FPStringUtils;
import io.infintestrike.utils.ImageTools;
import io.infintestrike.utils.TextFromFile;

public class Map implements Drawable {

	private final int cellsH;
	private final int cellsV;
	private final int tileSize;

	private BufferedImage tileSetImage;
	private final BufferedImage drawBuffer;
	private final ArrayList<Layer> layers = new ArrayList<Layer>();
	private final Vector2i offset = new Vector2i(0, 0);
	private LayerManager layerManager = null;

	protected TileMap tileMap = null;

	private boolean isMapReady = false;
	private boolean isInGrid = false;

	private int currentLayerIndex = 0;

	public Map(BufferedImage img, int cellsH, int cellsV, int tileSize) {
		this.cellsH = cellsH;
		this.cellsV = cellsH;
		this.tileSize = tileSize;

		this.tileSetImage = img;
		this.drawBuffer = new BufferedImage(this.getWidth() + 1, this.getHeight() + 1, BufferedImage.TYPE_INT_ARGB);
		if (loadTileSet()) {
			// stuff that needs a tileset to be there
		}

		this.layerManager = new LayerManager(this);
	}

	public BufferedImage getTileSetImage() {
		return this.tileSetImage;
	}

	public void setTileSetImage(BufferedImage img) {
		this.tileSetImage = img;
		this.loadTileSet();
	}

	public void setCurrentLayerIndex(int i) {
		this.currentLayerIndex = i;
	}

	public int getCurrentLayerIndex() {
		return this.currentLayerIndex;
	}

	public LayerManager getLayerManager() {
		return this.layerManager;
	}

	public Layer getActiveLayer() {
		if (this.layers.size() > 0) {
			Layer l = this.getLayer(this.getCurrentLayerIndex());
			if (l == null) {
				if (getLayer(0) != null) {
					l = this.getLayer(0);
				} else {
					l = new Layer(this);
				}
			}
			return l;
		} else {
			return null;
		}
	}

	private boolean loadTileSet() {
		if (this.tileSetImage != null) {
			this.tileMap = new TileMap(this);
			if (this.tileMap.areTilesNull()) {
				CreateMapDialog createMap = new CreateMapDialog();
				JFrame frame = Core.spawnFrame(createMap, Settings.getLanguage().getValue("$DIALOG_CREATE_HEADER"),
						new Vector2i(437, 239), null); //
				return false;
			}
			this.isMapReady = true;
			Settings.globalTileMap = this.tileMap;
			return true;
		}

		return false;
	}

	public void clear() {
		for (Layer l : this.layers) {
			for (int i = 0; i < l.getMapLinearSize(); i++) {
				l.setTile(i, Layer.TILE_ID_EMPTY);
			}
		}
	}

	public void addLayer(Layer l) {
		this.layers.add(l);
	}

	public Layer getLayer(int id) {
		return this.layers.get(id);
	}

	public void removeLayer(int id) {
		this.layers.remove(id);
	}

	public void removeAllLayers() {
		this.layers.clear();
	}

	public ArrayList<Layer> getLayers() {
		return this.layers;
	}

	public int getHorizontalCellCount() {
		return this.cellsH;
	}

	public int getVerticalCellCount() {
		return this.cellsV;
	}

	public int getWidth() {
		return this.cellsH * this.tileSize;
	}

	public TileMap getTileMap() {
		return this.tileMap;
	}

	public int getHeight() {
		return this.cellsV * this.tileSize;
	}

	public int getTileSize() {
		return this.tileSize;
	}

	public boolean isMapReady() {
		return isMapReady;
	}

	public boolean isMouseOverMap() {
		return this.isInGrid;
	}

	public Vector2i getOffset() {
		return this.offset;
	}

	public void setOffset(int x, int y) {
		this.offset.x = x;
		this.offset.y = y;
	}

	public BufferedImage renderMap() {
		BufferedImage img = new BufferedImage(this.drawBuffer.getWidth(),this.drawBuffer.getHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D imgGraphics = (Graphics2D) img.getGraphics();
		if (this.tileMap != null) {
			for (int i = 0; i < this.getLayers().size(); i++) {
				this.getLayers().get(i).draw(imgGraphics);
			}
		}
		imgGraphics.dispose();
		return img;
	}
	
	@Override
	public void OnRender(Graphics g, RenderPane c) {
		// TODO Auto-generated method stub

		g.drawImage(ImageTools.TRASN_CHECKER_D, 0, 0, null);

		Graphics2D bufferedGraphics = (Graphics2D) this.drawBuffer.getGraphics();
		bufferedGraphics.setBackground(new Color(255, 255, 255, 0));
		bufferedGraphics.clearRect(0, 0, this.getWidth() + 1, this.getHeight() + 1);

		/*if (this.tileMap != null) {
			for (int i = 0; i < this.getLayers().size(); i++) {
				this.getLayers().get(i).draw(bufferedGraphics);
			}
		}*/
		
		BufferedImage img = renderMap();
		bufferedGraphics.drawImage(img, 0, 0, null);
		

		if (Settings.drawGridBox) {

			bufferedGraphics
					.setColor(new Color(Loader.getValueHex(Settings.settingsFile.valueOf("$GRID_COLOR", "16777215"))));

			for (int x = 0; x < this.getHorizontalCellCount() + 1; x++) {
				int rx = x * this.tileSize;
				bufferedGraphics.drawLine(rx, 0, rx, this.getHeight());
			}

			for (int y = 0; y < this.getVerticalCellCount() + 1; y++) {
				int ry = y * this.tileSize;
				bufferedGraphics.drawLine(0, ry, this.getWidth(), ry);
			}
		}
		g.drawImage(drawBuffer, offset.x, offset.y, null);
		bufferedGraphics.dispose();
	}

	@Override
	public void OnTick(double delta, RenderPane c) {
		// TODO Auto-generated method stub

		InputManager input = c.getInputManager();

		if (input != null) {
			this.isInGrid = (input.MOUSE_X >= this.offset.x && input.MOUSE_X <= this.offset.x + getWidth())
					&& (input.MOUSE_Y >= this.offset.y && input.MOUSE_Y <= this.offset.y + getHeight());
		}

	}

	// ==================================================================================================
	public Vector2i getCell(int screenSpaceX, int screenSpaceY) {
		return FPMath.getCell(this.offset, new Vector2i(screenSpaceX, screenSpaceY),
				new Vector2i(getWidth(), getHeight()), this.tileSize);
	}

	public void floodFillTile(int x, int y, int tile) {
		Layer l = this.getActiveLayer();
		if (l.getTile(x, y) != Layer.TILE_ID_EMPTY)
			return;
		l.setTile(x, y, tile);

		floodFillTile(x + 1, y, tile);
		floodFillTile(x - 1, y, tile);
		floodFillTile(x, y + 1, tile);
		floodFillTile(x, y - 1, tile);
	}

	public static Map openMap(File location) {

		Map m = null;
		LoaderResult r = null;

		Console.Log("=====================================================");
		Console.Log("Map Import Started");
		Console.Log("=====================================================");
		
		try {
			r = Loader.readFile(location);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Console.Error("Cannot Load Map", e);
		}

		try {
			String tilesetFilePath = location.getPath().replace(location.getName(), "")
					+ Loader.getValueString(r.valueOf("$TILE_SET", "tileset.png"));
			BufferedImage tileSet = ImageTools.getImage(tilesetFilePath);

			// create map
			int tileSize = Loader.getValueInt(r.valueOf("$TILE_SIZE", "32"));
			int cellsX = Loader.getValueInt(r.valueOf("$TILE_CELLS_X", "5"));
			int cellsY = Loader.getValueInt(r.valueOf("$TILE_CELLS_Y", "5"));
			m = new Map(tileSet, cellsX, cellsY, tileSize);
			// create tile map
			int tileAmmount = 0;
			if ((tileAmmount = Loader.getValueInt(r.valueOf("$TILE_AMMOUNT", "0"))) == m.getTileMap()
					.getTiles().length) {
				for (int i = 0; i < tileAmmount; i++) {
					String[] tileData = Loader.getValueArray(r.valueOf("$TILE_ID_" + i, "null"));
					if (!tileData[0].equals("null")) {
						int id = FPStringUtils.toInt(tileData[0]);
						int xi = FPStringUtils.toInt(tileData[1]);
						int yi = FPStringUtils.toInt(tileData[2]);

						m.getTileMap().setTile(id, xi, yi, tileSize);
					} else {
						Console.Log("Cannot Read Entry: $TILE_ID_" + i + ".");
						return null;
					}
				}
			} else {
				Console.Log("Cannot Create Tile Map, Size Was: " + tileAmmount + " Expected: "
						+ m.getTileMap().getTiles().length);
				return null;
			}
			// create layer map
			int layerCount = Loader.getValueInt(r.valueOf("$LAYER_COUNT", "-1"));
			Console.Log("[Map::OpenMap] Layer Count: " + layerCount);
			if (layerCount != -1) {
				for (int i = 0; i < layerCount; i++) {
					String name = Loader.getValueString(r.valueOf("$LAYER_ID_" + (i) + "_NAME", "New Layer"));
					Console.Log("[Map::OpenMap] Layer "+i+" Name: " + name);
					Layer l = new Layer(m, true);
					l.setName(name);
					String[] layerValues = Loader.getValueArray(r.valueOf("$LAYER_ID_" + (i), "null"));
					if (!layerValues[0].equals("null")) {
						for (int j = 0; j < layerValues.length; j++) {
							l.setTile(j, FPStringUtils.toInt(layerValues[j]));
						}
					} else {
						Console.Log("Cannot Read Layer Entry: $LAYER_ID_" + i);
						return null;
					}
					
				}
			}

			Console.Log("=====================================================");
			Console.Log("Map Import Finished");
			Console.Log("Tiles: " + m.getTileMap().getTiles().length);
			Console.Log("Layers: " + m.getLayers().size());
			Console.Log("=====================================================");
			
			Settings.CURRENT_TILESET_FILE = new File(tilesetFilePath); // only set if we get this far
			return m;
		} catch (Exception e) {
			Console.NotifyUserError("Error While Loading Map:",e, false);
			return null;
		}
	}

	// ====================================================================================================
	public static void saveMap(Map m, File location) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JFrame frame = LoaderDialog.Spawn();

				Console.Log("=====================================================");
				Console.Log("Map Export Started");
				Console.Log("=====================================================");

				LoaderResult result = new LoaderResult();
				// Save Titles Firset
				TileMap tiles = m.getTileMap();
				result.set("$TILE_SIZE", LoaderEntry.makeEntry(m.tileSize));
				result.set("$TILE_CELLS_X", LoaderEntry.makeEntry(m.cellsH));
				result.set("$TILE_CELLS_Y", LoaderEntry.makeEntry(m.cellsV));
				result.set("$TILE_AMMOUNT", LoaderEntry.makeEntry(tiles.getTiles().length));
				result.set("$TILE_SET", LoaderEntry.makeEntry(Settings.CURRENT_TILESET_FILE.getName()));
				result.set("$LAYER_COUNT", LoaderEntry.makeEntry(m.getLayers().size()));
				// Define Tiles
				for (int i = 0; i < tiles.getTiles().length; i++) {
					Tile t = tiles.getTile(i);
					result.set("$TILE_ID_" + i,
							LoaderEntry.makeEntry(new Object[] { t.id, t.tileOffsetX, t.tileOffsetY }));
				}
				// Save Layers
				for (int j = 0; j < m.getLayers().size(); j++) {
					Layer l = m.getLayer(j);
					Object[] entries = new Object[l.getTileIDMap().length];
					for (int k = 0; k < entries.length; k++) {
						entries[k] = l.getTileIDMap()[k];
					}
					result.set("$LAYER_ID_" + j, LoaderEntry.makeEntry(entries));
					result.set("$LAYER_ID_" + j + "_NAME", LoaderEntry.makeEntry(l.getName()));
				}

				TextFromFile.makeFile(location, Loader.LoaderResultToString(result).getBytes());
				try {
					ImageIO.write(m.tileSetImage, "PNG",
							new File(location.getPath().replace(location.getName(), "") + "/tileset.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Console.Error("Couldnt save tilest", e);
				}
				Core.closeFrame(frame);

				Console.Log("=====================================================");
				Console.Log("Map Export Done");
				Console.Log("=====================================================");
			}
		});
		t.start();
	}

}
