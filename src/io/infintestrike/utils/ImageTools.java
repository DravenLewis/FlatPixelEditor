package io.infintestrike.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.infinitestrike.core.Console;

public class ImageTools {

	public static final BufferedImage ERROR_CHECKER = ImageTools.getCheckerBoard(32, 32, 16, 0x000000, 0xFF00FF);
	public static final BufferedImage TRANS_CHECKER_L = ImageTools.getCheckerBoard(1920, 1080, 16, 0xe3e3e3, 0x888888);
	public static final BufferedImage TRASN_CHECKER_D = ImageTools.getCheckerBoard(1920, 1080, 16, 0x3e3e3e, 0x444444);
	
	public static BufferedImage getCheckerBoard(int w, int h, int s, int c1, int c2) {
		BufferedImage img = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		for(int i = 0; i < w / s; i++) {
			for(int j = 0; j < h / s; j++) {
				
				if((i % 2) == (j % 2)) {
					g.setColor(new Color(c1));
				}else {
					g.setColor(new Color(c2));
				}
				
				g.fillRect(i * s,j * s,s,s);
			}
		}
		g.dispose();
		return img;
	}
	
	public static BufferedImage getImage(String f) {
		try {
			return ImageIO.read(new File(f));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Console.Error("Cannot Read Image: " + f, e);
		}
		return null;
	}
}
