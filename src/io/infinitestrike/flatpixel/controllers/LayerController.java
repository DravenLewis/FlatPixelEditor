package io.infinitestrike.flatpixel.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import io.infinitestrike.flatpixel.MapEditor;
import io.infinitestrike.flatpixel.Settings;
import io.infinitestrike.flatpixel.core.Strings;
import io.infinitestrike.flatpixel.grafx.drawables.RenderCallNotification;
import io.infinitestrike.flatpixel.logging.Console;
import io.infinitestrike.flatpixel.tilemap.Layer;
import io.infinitestrike.flatpixel.tilemap.Map;

public class LayerController implements Runnable {

	private JButton btnAdd, btnRemove, btnShow, btnHide, btnMoveUp, btnMoveDown;

	private final int ADD = 0, REMOVE = 1, SHOW = 2, HIDE = 3, UP = 4, DOWN = 5, LIST = 6;
	private LayerControlActionListener[] controllers = new LayerControlActionListener[7];

	private JList<String> list;
	protected Map layerMap;

	private boolean running = false;
	private final Thread updateThread;
	private int layerCount = 0;

	private static boolean doRegen = false;

	private DefaultListModel<String> data = new DefaultListModel<String>();

	public LayerController(JList<String> list, JButton add, JButton remove, JButton show, JButton hide, JButton up,
			JButton down, Map m) {

		data = new DefaultListModel<String>();

		reset(list, add, remove, show, hide, up, down, m);
		this.updateThread = new Thread(this);
	}

	public void start() {
		this.updateThread.start();
	}

	public void stop() {
		this.running = false;
		try {
			this.updateThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Console.Error("Issue When Joining Thread " + this.updateThread.getName(), e);
		}
	}

	public void reset(JList<String> list, JButton add, JButton remove, JButton show, JButton hide, JButton up,
			JButton down, Map m) {

		clearHandlers();

		this.btnAdd = add;
		this.btnRemove = remove;
		this.btnShow = show;
		this.btnHide = hide;
		this.btnMoveUp = up;
		this.btnMoveDown = down;
		this.layerMap = m;
		this.list = list;

		this.list.setModel(data);

		setupHandlers();
	}

	public void clearHandlers() {
		if (this.btnAdd != null && this.controllers[ADD] != null)
			this.btnAdd.removeActionListener(this.controllers[ADD]);
		if (this.btnRemove != null && this.controllers[REMOVE] != null)
			this.btnRemove.removeActionListener(this.controllers[REMOVE]);
		if (this.btnMoveUp != null && this.controllers[UP] != null)
			this.btnMoveUp.removeActionListener(this.controllers[UP]);
		if (this.btnMoveDown != null && this.controllers[DOWN] != null)
			this.btnMoveDown.removeActionListener(this.controllers[DOWN]);
		if (this.btnShow != null && this.controllers[SHOW] != null)
			this.btnShow.removeActionListener(this.controllers[SHOW]);
		if (this.btnHide != null && this.controllers[HIDE] != null)
			this.btnHide.removeActionListener(this.controllers[HIDE]);
	}

	public void setupHandlers() {
		if (this.layerMap != null && this.list != null) {

			if (this.btnAdd != null) {
				LayerControlActionListener add = new ListButtonAddActionListener(this.layerMap, this.list, this);
				this.controllers[ADD] = add;
				this.btnAdd.addActionListener(add);
			}
			if (this.btnRemove != null) {
				LayerControlActionListener remove = new ListButtonRemoveActionListener(this.layerMap, this.list, this);
				this.controllers[REMOVE] = remove;
				this.btnRemove.addActionListener(remove);
			}
			if (this.btnMoveUp != null) {
				LayerControlActionListener up = new ListButtonUpActionListener(this.layerMap, this.list, this);
				this.controllers[UP] = up;
				this.btnMoveUp.addActionListener(up);
			}
			if (this.btnMoveDown != null) {
				LayerControlActionListener down = new ListButtonDownActionListener(this.layerMap, this.list, this);
				this.controllers[DOWN] = down;
				this.btnMoveDown.addActionListener(down);
			}
			if (this.btnShow != null) {
				LayerControlActionListener show = new ListButtonShowActionListener(this.layerMap, this.list, this);
				this.controllers[SHOW] = show;
				this.btnShow.addActionListener(show);
			}
			if (this.btnHide != null) {
				LayerControlActionListener hide = new ListButtonHideActionListener(this.layerMap, this.list, this);
				this.controllers[HIDE] = hide;
				this.btnHide.addActionListener(hide);
			}

			LayerControlActionListener list = new ListMouseClickListener(this.layerMap, this.list, this);
			this.controllers[LIST] = list;
			this.list.addMouseListener(list);
			this.list.setVisibleRowCount(300);
		}
	}

	public void regenerateList() {
		if (this.list != null && this.layerMap != null) {
			DefaultListModel<String> model = (DefaultListModel<String>) this.list.getModel();
			model.removeAllElements();
			Console.Log("[Regenerate List] Layers Loaded: " + this.layerMap.getLayers().size());
			for (Layer l : this.layerMap.getLayers()) {
				Console.Log("Setting Layer Name: " + l.getName());
				if (l != null) {
					model.addElement("     " + l.getName() + (!l.isDoRender() ? " - Hidden" : ""));
				}
			}
		}
	}

	public void repaintPane(JPanel p) {
		p.repaint();
		p.revalidate();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		this.running = true;

		while (this.running) {
			try {
				Thread.sleep(30); // 30ms of fake lag to let the JList Keep Up
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Console.Error("Cannot Sleep Thread", e);
			}

			if (this.doRegen || this.layerCount != this.layerMap.getLayers().size()) {
				regenerateList();
				this.layerCount = this.layerMap.getLayers().size();
				if (doRegen)
					doRegen = false;
			}
		}
	}

	public static abstract class LayerControlActionListener extends MouseAdapter implements ActionListener {
		protected final Map renderMap;
		protected final JList<String> renderLayerList;
		protected final LayerController controller;

		public LayerControlActionListener(Map m, JList<String> s, LayerController c) {
			this.renderMap = m;
			this.renderLayerList = s;
			this.controller = c;
		}
	}

	public static class ListButtonUpActionListener extends LayerControlActionListener {
		public ListButtonUpActionListener(Map m, JList<String> s, LayerController c) {
			super(m, s, c);
		}

		public void actionPerformed(ActionEvent e) {
			int selInd = renderLayerList.getSelectedIndex();
			if (selInd < 0)
				return;
			renderMap.getLayerManager().moveLayerUpSafely(selInd);
			this.controller.doRegen = true;
			Settings.getMapSettings().mapSaved = false;
		}
	}

	public static class ListButtonDownActionListener extends LayerControlActionListener {
		public ListButtonDownActionListener(Map m, JList<String> s, LayerController c) {
			super(m, s, c);
		}

		public void actionPerformed(ActionEvent e) {
			int selInd = renderLayerList.getSelectedIndex();
			if (selInd < 0)
				return;
			renderMap.getLayerManager().moveLayerDownSafely(selInd);
			this.controller.doRegen = true;
			Settings.getMapSettings().mapSaved = false;
		}
	}

	public static class ListButtonShowActionListener extends LayerControlActionListener {
		public ListButtonShowActionListener(Map m, JList<String> s, LayerController c) {
			super(m, s, c);
		}

		public void actionPerformed(ActionEvent e) {
			int selInd = renderLayerList.getSelectedIndex();
			if (selInd < 0)
				return;
			renderMap.getLayer(selInd).doRender = true;
			this.controller.doRegen = true;
			Settings.getMapSettings().mapSaved = false;
		}
	}

	public static class ListButtonHideActionListener extends LayerControlActionListener {
		public ListButtonHideActionListener(Map m, JList<String> s, LayerController c) {
			super(m, s, c);
		}

		public void actionPerformed(ActionEvent e) {
			int selInd = renderLayerList.getSelectedIndex();
			if (selInd < 0)
				return;
			renderMap.getLayer(selInd).doRender = false;
			this.controller.doRegen = true;
			Settings.getMapSettings().mapSaved = false;
		}
	}

	public static class ListButtonAddActionListener extends LayerControlActionListener {
		public ListButtonAddActionListener(Map m, JList<String> s, LayerController c) {
			super(m, s, c);
		}

		public void actionPerformed(ActionEvent e) {
			Layer l = new Layer(renderMap, false);
			renderMap.addLayer(l);
			this.controller.doRegen = true;
			Settings.getMapSettings().mapSaved = false;
		}
	}

	public static class ListButtonRemoveActionListener extends LayerControlActionListener {
		public ListButtonRemoveActionListener(Map m, JList<String> s, LayerController c) {
			super(m, s, c);
		}

		public void actionPerformed(ActionEvent e) {
			// int selInd = renderLayerList.getSelectedIndex();
			// if(selInd < 0) return;
			int[] selInd = this.renderLayerList.getSelectedIndices();
			if (selInd.length == 0)
				return;
			if (selInd.length > 1) {
				Strings lang = Settings.getLanguage();
				if (lang != null) {
					int opt = JOptionPane.showConfirmDialog(null,
							String.format(lang.getValue("$DIALOG_CONFIRM_MULTI_LAYER_DELETE"), selInd.length));

					if (opt == JOptionPane.YES_OPTION) {
						this.renderMap.getLayerManager().safelyRemoveIndecies(selInd);
					}
				}
			} else {
				renderMap.removeLayer(selInd[0]);
			}
			this.controller.doRegen = true;
			Settings.getMapSettings().mapSaved = false;
		}
	}

	public static class ListMouseClickListener extends LayerControlActionListener {

		public ListMouseClickListener(Map m, JList<String> s, LayerController c) {
			super(m, s, c);
		}

		@Override // not used
		public void actionPerformed(ActionEvent e) {
		}

		public void mouseClicked(MouseEvent evt) {
			@SuppressWarnings("unchecked")
			JList<String> list = (JList<String>) evt.getSource();
			if (evt.getButton() == MouseEvent.BUTTON1) { // double click with left
				int index = list.locationToIndex(evt.getPoint());

				try {
					renderMap.setCurrentLayerIndex(index);
				} catch (Exception e) {
					Console.Log(renderMap.toString());
					Console.Log(Settings.getMapSettings().tileMap.getParent().toString());
				}
			}

			if (evt.getButton() == MouseEvent.BUTTON3) { // right click
				int layerIndex = list.locationToIndex(evt.getPoint());
				JPopupMenu menu = new JPopupMenu();
				JMenuItem rename = new JMenuItem(Settings.getLanguage().getValue("$DIALOG_LAYER_RENAME"));
				rename.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						String name = JOptionPane.showInputDialog(MapEditor.getApplicationWindow(),
								String.format(Settings.getLanguage().getValue("$DIALOG_LAYER_REANME_CONFIRM"),
										renderMap.getLayer(layerIndex).getName()),
								"Rename Layer", JOptionPane.QUESTION_MESSAGE);

						if (name != null) {
							if (!name.isEmpty() && !name.equals("")) {
								renderMap.getLayer(layerIndex).setName(name);
								doRegen = true;
								Settings.getMapSettings().mapSaved = false;
							}
						}
					}
				});
				JMenuItem refresh = new JMenuItem(Settings.getLanguage().getValue("$DIALOG_LAYER_REFRESH"));
				refresh.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						doRegen = true;
					}
				});
				menu.add(refresh);
				menu.add(rename);
				menu.setLocation(evt.getPoint());
				menu.show(list, evt.getPoint().x, evt.getPoint().y);
			}
		}
	}
}
