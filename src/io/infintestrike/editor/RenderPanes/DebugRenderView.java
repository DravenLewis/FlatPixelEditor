package io.infintestrike.editor.RenderPanes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JFrame;

import io.infinitestrike.core.Vector.Vector2i;
import io.infinitestrike.flatpixelutls.FPMath;
import io.infintestrike.editor.RenderPane;
import io.infintestrike.editor.RenderPane.InputManager;
import io.infintestrike.editor.RenderPane.RenderCall;
import io.infintestrike.editor.fonts.Fonts;

public class DebugRenderView extends RenderCall{

	int tileSize = 32;
	int height = tileSize * 10,width = tileSize * 10;
	int xo = 0;
	int yo = 0;
	int cellX = 0, cellY = 0;
	
	Font f = Fonts.changeFontSize(Fonts.VCR, 18.0f);
	boolean isInGrid = false;
	Vector2i vectorOffset = new Vector2i(0,0);
	
	String[] names = {
			"Is Cursor in Bounds? : ",
			"X Offset: ",
			"Y Offset: ",
			"TileSize: ",
			"Vector Offset: ",
			"H Cells: ",
			"V Cells: "
	};
	Object[] values = {};
	
	@Override
	public void OnAttach(RenderPane c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnTick(double delta, RenderPane c) {
		// TODO Auto-generated method stub
		if(this.getParent().getInputManager().isKeyDown(KeyEvent.VK_LEFT)) xo -= 10;
		if(this.getParent().getInputManager().isKeyDown(KeyEvent.VK_RIGHT)) xo += 10;
		if(this.getParent().getInputManager().isKeyDown(KeyEvent.VK_UP)) yo -= 10;
		if(this.getParent().getInputManager().isKeyDown(KeyEvent.VK_DOWN)) yo += 10;
		
		InputManager m = this.getParent().getInputManager();
		
		isInGrid = (m.MOUSE_X >= xo && m.MOUSE_X <= xo +width) &&
				   (m.MOUSE_Y >= yo && m.MOUSE_Y <= yo +height);
		
		this.values = new Object[]{
				isInGrid,
				xo,
				yo,
				tileSize,
				vectorOffset,
				width / tileSize,
				height / tileSize
		};
	}

	@Override
	public void OnRender(Graphics g, RenderPane c) {
		// TODO Auto-generated method stub
		
		drawGrid(g,c, xo,yo);
		g.setFont(f);
		g.setColor(new Color(0x888888));
		g.setXORMode(Color.red);
		for(int i = 0; i < names.length; i++) {
			if(i > values.length - 1) {
				g.drawString(names[i] + "N/A", 30, 30 * i + 2 + 64);
			}else {
				g.drawString(names[i] + values[i], 30, 30 * i + 2 + 64);
			}
		}
	}

	@Override
	public void OnDetatch(RenderPane c) {
		// TODO Auto-generated method stub
		
	}
	
	public void drawGrid(Graphics g, RenderPane c, int xo, int yo) {
		g.setColor(Color.green);
		g.fillRect(xo + (this.vectorOffset.x * tileSize), yo + (this.vectorOffset.y * tileSize), tileSize, tileSize);
		g.setColor(Color.white);
		
		for(int x = 0; x < width + 1; x+= tileSize) {
			g.drawLine(xo + x,yo + 0,xo +x,yo +height);
		}
		for(int y = 0; y < height + 1; y+= tileSize) {
			g.drawLine(xo +0, yo +y, xo +width,yo + y);
		}
		int index=0;
		for(int y = 0; y < height; y+=tileSize) {
			for(int x = 0; x < width; x+=tileSize) {
				g.drawString("" + index,xo + x + 2,yo + y + 12);
				index++;
			}
		}
		
		g.setColor(Color.red);
		if(this.isInGrid) {
			InputManager m = this.getParent().getInputManager();
			Vector2i offset = FPMath.getCell(xo,yo,m.MOUSE_X,m.MOUSE_Y,width,height,tileSize);
			this.vectorOffset = offset;
		}
	}
	
	public static void SpawnDebugPane() {
		JFrame frame = new JFrame("Debug Renderer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(640,480);
		RenderPane p = new RenderPane();
		DebugRenderView view = new DebugRenderView();
		p.addRenderCall(view);
		frame.setContentPane(p);
		p.start();
		frame.setVisible(true);
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
