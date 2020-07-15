package io.infintestrike.editor.RenderPanes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import io.infintestrike.editor.EditorWindow;
import io.infintestrike.editor.RenderPane;
import io.infintestrike.editor.RenderPane.InputManager;
import io.infintestrike.editor.RenderPane.RenderCall;

public class FormImagePreview extends RenderCall{
	private BufferedImage img = null;
	private EditorWindow window = null;
	
	public FormImagePreview(EditorWindow window) {
		this.window = window;
	}
	
	@Override
	public void OnAttach(RenderPane c) {
		// TODO Auto-generated method stub
		this.getParent().addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 && img != null) {
					JFrame frame = new JFrame("Tileset Explorer");
					frame.setSize(640,480);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					RenderPane p = new RenderPane();
					p.addRenderCall(new ImageExplorer(img));
					p.start();
					frame.setContentPane(p);
					frame.setVisible(true);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	@Override
	public void OnTick(double delta, RenderPane p) {
		// TODO Auto-generated method stub
	}

	@Override
	public void OnRender(Graphics g, RenderPane c) {
		// TODO Auto-generated method stub
		/*if (window.tilesetImage != null) {
			img = new BufferedImage(window.tilesetImage.getWidth(),window.tilesetImage.getHeight(),1);
			Graphics gg = img.getGraphics();
			//int itileSizeX = Integer.parseInt(window.tileSizeX.getText());
			//int itileSizeY = Integer.parseInt(window.tileSizeX.getText());
			gg.drawImage(window.tilesetImage, 0, 0, null);
			Color co = new Color(0x88888888);
			gg.setColor(co);
			//for(int x = 0; x < window.tilesetImage.getWidth(); x+= itileSizeX) {
				gg.drawLine(x, 0, x, window.tilesetImage.getHeight());
			}
			//for(int y = 0; y < window.tilesetImage.getHeight(); y+= itileSizeY) {
				gg.drawLine(0, y, window.tilesetImage.getWidth(), y);
			}
			g.drawImage(img,0,0,c.getWidth(),c.getHeight(),null);
			g.setColor(Color.white);
			gg.dispose();
		} else {
			g.setColor(Color.white);
			g.drawString("No Preview Available", 10, 15);
		}*/
	}

	@Override
	public void OnDetatch(RenderPane c) {
		// TODO Auto-generated method stub
		
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
