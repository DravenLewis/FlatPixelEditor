package io.infintestrike.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JPanel;

import io.infinitestrike.core.Console;

@SuppressWarnings({ "serial" })
public class RenderPane extends JPanel implements Runnable {

	private Thread renderThread;
	private float FPS = 200;
	private boolean running = false;

	private long lastTimeMillis = System.currentTimeMillis();
	private boolean drawDrawTimeMillis = false;

	// Event Handler Lists
	private ArrayList<RenderCall> renderCalls = new ArrayList<RenderCall>();
	private ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	private ArrayList<InputSubscriber> subscribers = new ArrayList<InputSubscriber>(); // Maybe Change to Generic
																						// "Subscriber" later;
	private InputManager manager = null;
	private String uuid = UUID.randomUUID().toString();

	public RenderPane() {
		this.renderThread = new Thread(this, "RenderPanel#" + this.toString());
		this.manager = new InputManager(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
	}

	public RenderPane(int width, int height) {
		this.renderThread = new Thread(this, "RenderPanel#" + this.toString());
		this.setPreferredSize(new Dimension(width, height));
		this.manager = new InputManager(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
	}

	public void start() {
		this.running = true;
		this.renderThread.start();
	}

	public synchronized void stop() {
		this.running = false;
		try {
			this.renderThread.join();
		} catch (Exception e) {
			Console.Log("");
		}
	}

	public void run() {
		long start = System.nanoTime();
		double delay = 1000000000 / FPS;
		double delta = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - start) / delay;
			start = now;
			if (delta >= 1) {
				tick(delta, this);
				delta--;
			}
			this.repaint();

			long elapsedNow = System.currentTimeMillis();
			long elapsedTime = elapsedNow - this.lastTimeMillis;
			this.lastTimeMillis = elapsedTime;
		}
	}

	public void tick(double delta, RenderPane p) {
		/*
		 * for(Drawable c : this.drawables) { //this.getInputManager().MOUSE_DS = 0; //
		 * TRY TO RESET EVERY FRAME? c.OnTick(delta,p); }
		 */

		for (int i = 0; i < this.drawables.size(); i++) {
			Drawable c = this.drawables.get(i);
			c.OnTick(delta, p);
		}
	}

	public void render(Graphics g, RenderPane p) {
		// Graphics gg = this.backBuffer.getGraphics();
		// gg.setColor(new Color(0x3e3e3e));
		// gg.fillRect(0, 0, this.backBuffer.getWidth(), this.backBuffer.getHeight());

		final Rectangle2D rect = new Rectangle2D.Float();
		rect.setRect(0, 0, this.getWidth(), this.getHeight());
		g.setClip(rect);

		/*
		 * for(Drawable c : this.drawables) { c.OnRender(g, p); }
		 */

		for (int i = 0; i < this.drawables.size(); i++) {
			Drawable c = this.drawables.get(i);
			c.OnRender(g, p);
		}

		if (this.drawDrawTimeMillis) {
			g.setColor(Color.white);
			g.drawString("" + (this.lastTimeMillis / 1000.0f), 30, 30);
		}
		// g.drawImage(backBuffer, 0,0,this.getSize().width, this.getSize().height,
		// null);
		// gg.dispose();
	}

	public void addRenderCall(RenderCall c) {
		c.setParent(this);
		c.OnAttach(this);
		this.addDrawable(c);
		this.addSubscriber(c);
		this.renderCalls.add(c);
	}

	public void removeRenderCall(RenderCall c) {
		c.OnDetatch(this);
		c.setParent(null);
		this.removeDrawable(c);
		this.removeSubscriber(c);
		this.renderCalls.remove(c);
	}

	public void addDrawable(Drawable d) {
		this.drawables.add(d);
	}

	public void removeDrawable(Drawable d) {
		this.drawables.remove(d);
	}

	public void clear() {
		for (int i = 0; i < this.renderCalls.size(); i++) {
			this.renderCalls.get(i).OnDetatch(this);
			this.renderCalls.get(i).setParent(null);
			;
		}
		this.renderCalls.clear();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Color c = g.getColor();
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, this.getSize().width, this.getSize().height); // clear the screen
		g.setColor(c);
		render(g, this);
	}

	public InputManager getInputManager() {
		return this.manager;
	}

	public void addSubscriber(InputSubscriber s) {
		// TODO Auto-generated method stub
		this.subscribers.add(s);
		s.OnAttach(manager, this);
	}

	public void removeSubscriber(InputSubscriber s) {
		s.OnDetatch(this);
		this.subscribers.remove(s);
	}

	public String toString() {
		return " @ " + this.uuid;
	}

	public String getUUID() {
		return this.uuid;
	}

	// ============================================================================
	public static abstract class RenderCall implements Drawable, InputSubscriber{
		private RenderPane parent;

		public abstract void OnAttach(RenderPane c);

		public abstract void OnTick(double delta, RenderPane c);

		public abstract void OnRender(Graphics g, RenderPane c);

		public abstract void OnDetatch(RenderPane c);

		protected final void setParent(RenderPane p) {
			this.parent = p;
		}

		public final RenderPane getParent() {
			return this.parent;
		}
	}

	// ============================================================================
	public static interface InputSubscriber {
		public void OnAttach(InputManager m, RenderPane p);

		public void OnDetatch(RenderPane p);

		// KEY
		public void onKeyTyped(KeyEvent e, InputManager m, RenderPane p);
		public void onKeyPressed(KeyEvent e, InputManager m, RenderPane p);
		public void onKeyReleased(KeyEvent e, InputManager m, RenderPane p);

		// Mouse
		public void OnMouseWheelMoved(MouseWheelEvent e, InputManager m, RenderPane p);
		public void OnMouseDragged(MouseEvent e, InputManager m, RenderPane p);
		public void OnMouseMoved(MouseEvent e, InputManager m, RenderPane p);
		public void OnMouseClicked(MouseEvent e, InputManager m, RenderPane p);
		public void OnMousePressed(MouseEvent e, InputManager m, RenderPane p);
		public void OnMouseReleased(MouseEvent e, InputManager m, RenderPane p);
		public void OnMouseEntered(MouseEvent e, InputManager m, RenderPane p);
		public void OnMouseExited(MouseEvent e, InputManager m, RenderPane p);
	}

	public static interface Drawable {
		public void OnRender(Graphics g, RenderPane c);

		public void OnTick(double delta, RenderPane c);
	}

	// ============================================================================
	public static class InputManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

		public boolean[] keys = new boolean[256];
		public boolean MOUSE_LEFT = false;
		public boolean MOUSE_MIDDLE = false;
		public boolean MOUSE_RIGHT = false;
		public boolean IS_DRAGGING = false;
		public int MOUSE_X = 0;
		public int MOUSE_Y = 0;
		private int MOUSE_DS = 0;

		private RenderPane renderPaneTarget = null;

		public InputManager(RenderPane p) {
			this((Component) p);
			this.renderPaneTarget = p;
		}

		public InputManager(Component c) {
			c.addMouseListener(this);
			c.addMouseMotionListener(this);
			c.addMouseWheelListener(this);
			c.addKeyListener(this);
		}

		public String toString() {
			String ret = "";
			ret += "Mouse Pos: (" + MOUSE_X + "," + MOUSE_Y + ")\n";
			ret += "Mouse Button: [LMB:" + MOUSE_LEFT + ", MMB: " + MOUSE_MIDDLE + ", RMB: " + MOUSE_RIGHT + "]\n";
			ret += "Mouse Is Draging: " + IS_DRAGGING + ", Mouse Scroll DX: " + MOUSE_DS + ",\n";
			String keyss = "";
			for (int i = 0; i < 256; i++) {
				if (keys[i] == true)
					keyss += "1";
				if (keys[i] != true)
					keyss += "0";
			}

			long block_1 = Long.parseLong(keyss.substring(0, 63), 2);
			long block_2 = Long.parseLong(keyss.substring(64, 127), 2);
			long block_3 = Long.parseLong(keyss.substring(128, 191), 2);
			long block_4 = Long.parseLong(keyss.substring(192, 255), 2);

			String hex_1 = Long.toString(block_1, 16);
			String hex_2 = Long.toString(block_2, 16);
			String hex_3 = Long.toString(block_3, 16);
			String hex_4 = Long.toString(block_4, 16);

			ret += "\n";
			ret += "Key Value (256-Bit int): [4 64-Bit Blocks]\n";
			ret += "    Block 1: 0x" + hex_1 + "\n";
			ret += "    Block 2: 0x" + hex_2 + "\n";
			ret += "    Block 3: 0x" + hex_3 + "\n";
			ret += "    Block 4: 0x" + hex_4 + "\n";
			return ret;
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			// TODO Auto-generated method stub
			this.MOUSE_DS = e.getWheelRotation();

			if (this.isRenderPane()) {
				for (InputSubscriber s : this.getInputSubscribers()) {
					s.OnMouseWheelMoved(e, this, renderPaneTarget);
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			this.MOUSE_X = e.getX();
			this.MOUSE_Y = e.getY();
			this.IS_DRAGGING = true;

			if (this.isRenderPane()) {
				for (InputSubscriber s : this.getInputSubscribers()) {
					s.OnMouseDragged(e, this, renderPaneTarget);
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			this.MOUSE_X = e.getX();
			this.MOUSE_Y = e.getY();

			if (this.isRenderPane()) {
				for (InputSubscriber s : this.getInputSubscribers()) {
					s.OnMouseMoved(e, this, renderPaneTarget);
				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if (this.isRenderPane()) {
				for (InputSubscriber s : this.getInputSubscribers()) {
					s.OnMouseClicked(e, this, renderPaneTarget);
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				this.MOUSE_LEFT = true;
				break;
			case MouseEvent.BUTTON2:
				this.MOUSE_MIDDLE = true;
				break;
			case MouseEvent.BUTTON3:
				this.MOUSE_RIGHT = true;
				break;
			}

			if (this.isRenderPane()) {
				for (InputSubscriber s : this.getInputSubscribers()) {
					s.OnMousePressed(e, this, renderPaneTarget);
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				this.MOUSE_LEFT = false;
				break;
			case MouseEvent.BUTTON2:
				this.MOUSE_MIDDLE = false;
				break;
			case MouseEvent.BUTTON3:
				this.MOUSE_RIGHT = false;
				break;
			}
			this.IS_DRAGGING = false;
			
			if(this.isRenderPane()) {
				for(InputSubscriber s : this.getInputSubscribers()) {
					s.OnMouseReleased(e, this, renderPaneTarget);
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			this.MOUSE_X = e.getX();
			this.MOUSE_Y = e.getY();
			
			if(this.isRenderPane()) {
				for(InputSubscriber s : this.getInputSubscribers()) {
					s.OnMouseEntered(e, this, renderPaneTarget);
				}
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			this.MOUSE_X = e.getX();
			this.MOUSE_Y = e.getY();
			if(this.isRenderPane()) {
				for(InputSubscriber s : this.getInputSubscribers()) {
					s.OnMouseExited(e, this, renderPaneTarget);
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			if(this.isRenderPane()) {
				for(InputSubscriber s : this.getInputSubscribers()) {
					s.onKeyTyped(e, this, renderPaneTarget);
				}
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() >= 0 && e.getKeyCode() < 256) {
				this.keys[e.getKeyCode()] = true;
			}
			if(this.isRenderPane()) {
				for(InputSubscriber s : this.getInputSubscribers()) {
					s.onKeyPressed(e, this, renderPaneTarget);
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() >= 0 && e.getKeyCode() < 256) {
				this.keys[e.getKeyCode()] = false;
			}
			if(this.isRenderPane()) {
				for(InputSubscriber s : this.getInputSubscribers()) {
					s.onKeyReleased(e, this, renderPaneTarget);
				}
			}
		}

		public boolean isKeyDown(int code) {
			if (code >= 0 && code < 256) {
				return keys[code];
			}
			return false;
		}

		public int getMouseScroll() {
			int amount = this.MOUSE_DS;
			this.MOUSE_DS = 0;
			return amount;
		}

		private ArrayList<InputSubscriber> getInputSubscribers() {
			return this.renderPaneTarget.subscribers;
		}

		private boolean isRenderPane() {
			return this.renderPaneTarget != null;
		}
	}
	// ============================================================================
}
