package io.infinitestrike.flatpixel.tilemap;

import java.awt.Image;
import java.awt.image.BufferedImage;

import io.infinitestrike.utils.ImageTools;


// just a data structure
// not an actual tile, can be used
// to calculate what a tile at this location would be

public class Tile {
	
	public int id = 0;
	public int tileSize = 32;
	public int tileOffsetX = 0; // this gets multiplied by size
	public int tileOffsetY = 0; // to calculate sub image
	
	
	public Tile(BufferedImage img, int tileSize, int xoffset, int yoffset) {
		this.tileSize = tileSize;
		this.tileOffsetX = xoffset;
		this.tileOffsetY = yoffset;
	}
	
	public Image getTileImage(BufferedImage tileMap) {
		if(tileOffsetX * tileSize < tileMap.getWidth() && tileOffsetY * tileSize < tileMap.getHeight()) {
			return tileMap.getSubimage(tileOffsetX * tileSize, tileOffsetY * tileSize, tileSize, tileSize);
		}else {
			return ImageTools.ERROR_CHECKER.getScaledInstance(tileSize, tileSize, BufferedImage.SCALE_FAST);
		}
	}
	
	/*public Image getTileImage(BufferedImage tileMap) {
		if(tileOffsetX * tileSizeX < tileMap.getWidth() && tileOffsetY * tileSizeY < tileMap.getHeight()) {
			return tileMap.getSubimage(tileOffsetX * tileSizeX, tileOffsetY * tileSizeY, tileSizeX, tileSizeY);
		}else {
			return ImageTools.ERROR_CHECKER.getScaledInstance(tileSizeX, tileSizeY, BufferedImage.SCALE_FAST);
		}
	}*/
}
