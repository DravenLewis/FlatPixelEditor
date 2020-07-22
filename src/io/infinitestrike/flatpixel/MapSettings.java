package io.infinitestrike.flatpixel;

import java.awt.image.BufferedImage;

import io.infinitestrike.flatpixel.tilemap.TileMap;

public class MapSettings {
	// struct
	public int tileSize = 32;
	public int tileCellsW = 20;
	public int tileCellsH = 20;
	public BufferedImage tileSetImage;
	public TileMap tileMap;
	public int currentlySelectedTile = 0;
	public int[] currentlySelectedTiles = {};
	public String lastSavedMapLocation = "";
	public boolean mapSaved = true;
}
