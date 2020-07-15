package io.infinitestrike.flatpixelutls;

import io.infinitestrike.core.Vector.Vector2i;

public class FPMath {
	
	public static Vector2i getCell(int fieldOffsetX, int fieldOffsetY, int screenSpaceX, int screenSpaceY, int wPixels,
			int hPixels, int cellSize) {

		// Bind the Position Relative to the Grid
		int boundOffsetX = screenSpaceX - fieldOffsetX;
		int boundOffsetY = screenSpaceY - fieldOffsetY;

		// Scale the Relative position to an offset in cells space
		// X Cell Location = floor(X Relative offset * (Horizontal Cell Count) / Number
		// of pixels the grid is wide)
		// Y Cell Location = floor(Y Relative offset * (Vertical Cell Count) / Number of
		// pixels the grid is high)

		int fieldQuotientX = (int) Math.floor(boundOffsetX * ((float) (wPixels / cellSize) / (float) wPixels));
		int fieldQuotientY = (int) Math.floor(boundOffsetY * ((float) (hPixels / cellSize) / (float) hPixels));

		// Clamp the output to only be valid to the grid, as the bound offset is still
		// in screen space,
		// therefore you can technically have a cell at (-1,-1) or (x + 1, y + 1), where
		// x and y are the
		// max values the grid has. Prevents IndexOutOfBoundsException.

		int gridBoundX = FPMath.clamp(fieldQuotientX, 0, (wPixels / cellSize) - 1);
		int gridBoundY = FPMath.clamp(fieldQuotientY, 0, (hPixels / cellSize) - 1);

		// Return the clamped orderd pair
		return new Vector2i(gridBoundX, gridBoundY);
	}

	public static Vector2i getCell(Vector2i fieldOffset, Vector2i screenSpacePoint, Vector2i gridPixelSize, int cellSize) {
		return getCell(fieldOffset.x,fieldOffset.y,screenSpacePoint.x,screenSpacePoint.y,gridPixelSize.x,gridPixelSize.y,cellSize);
	}
	
	public static int clamp(int i, int min, int max) {
		return Math.max(min, Math.min(max, i));
	}
}
