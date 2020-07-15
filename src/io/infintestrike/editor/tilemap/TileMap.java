package io.infintestrike.editor.tilemap;

import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import io.infinitestrike.core.Console;
import io.infinitestrike.core.Core;

public class TileMap {
	private final Map parentMap;
	private int ts = 0;


	private final Tile[] tiles;

	public TileMap(Map parentMap) {
		this.parentMap = parentMap;
		this.ts = this.parentMap.getTileSize();
		this.tiles = this.generateTiles();
	}

	public int getTotalHorizontalCells() {
		return parentMap.getHorizontalCellCount();
	}

	public int getTotalVerticalCells() {
		return parentMap.getVerticalCellCount();
	}

	public Map getParent() {
		return this.parentMap;
	}

	public boolean areTilesNull() {
		return this.tiles == null;
	}

	private Tile[] generateTiles() {

		final BufferedImage tileMap = this.parentMap.getTileSetImage();

		if (tileMap.getWidth() % this.ts == 0 && tileMap.getHeight() % this.ts == 0) { // even tilesize check
			int tileWidth = tileMap.getWidth() / this.ts; // how many vertical tiles that can be loaded Eg tile size
															// 64x64 with tile size 32 = 2
			int tileHeight = tileMap.getHeight() / this.ts; // ^^^^
			int index = 0; // tile index
			Tile[] tiles = new Tile[tileWidth * tileHeight];
			for (int y = 0; y < tileMap.getHeight(); y += ts) {
				for (int x = 0; x < tileMap.getWidth(); x += ts) {
					tiles[index] = new Tile(tileMap, ts, x / ts, y / ts);
					tiles[index].id = index;
					index++;
				}
			}
			return tiles;
		} else {
			Console.Log("Cannot Get Tiles, tile size is not regular (tile map extends past data)");
			Console.Log("	*Make Sure your tile size divides evenly into the size of the tileset");

			JOptionPane.showMessageDialog(null,
					"Cannot Get Tiles, tile size is not regular (tile map extends past data)\n	*Make Sure your tile size divides evenly into the size of the tileset",
					"Cant Construct TileMap", JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}

	public Tile getTile(int x, int y) {
		return this.getTile(x + (y * (this.parentMap.getTileSetImage().getWidth() / ts)));
	}

	public Tile getTile(int id) {
		if (this.tiles != null) {
			if (id >= 0 && id <= this.tiles.length - 1) {
				return this.tiles[id];
			}
			return null;
		} else {
			return null;
		}
	}
	
	public Tile[] getTiles() {
		return this.tiles;
	}
	
	public void setTile(int index,int xindex, int yindex, int tileSize) {
		if(index < 0 || index >= this.tiles.length - 1) {
			this.tiles[index] = new Tile(this.getParent().getTileSetImage(),tileSize,xindex,yindex);
		}
	}
}
