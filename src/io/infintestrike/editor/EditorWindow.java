package io.infintestrike.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import io.infinitestrike.core.Console;
import io.infinitestrike.core.Core;
import io.infinitestrike.core.ErrorCode;
import io.infinitestrike.editor.dialog.CreateMapDialog;
import io.infinitestrike.editor.dialog.Debugger;
import io.infinitestrike.editor.dialog.LoaderDialog;
import io.infinitestrike.editor.dialog.SettingsWindow;
import io.infinitestrike.flatpixelutls.Loader;
import io.infintestrike.editor.RenderPanes.LayerControlMap;
import io.infintestrike.editor.RenderPanes.MainEditor;
import io.infintestrike.editor.RenderPanes.TileSelectorPane;
import io.infintestrike.editor.core.ResourceManager;
import io.infintestrike.editor.tilemap.Map;

public class EditorWindow {

	
	RenderPane renderPane_2 = null;
	
	public JFrame frame;
	public BufferedImage tilesetImage = null;
	public static MainEditor currentEditor = null;

	private CreateMapDialog createMap = null;
	private JScrollPane scrollPane_1 = null;
	
	private LayerController c = null;

	/**
	 * Create the application.
	 */
	public EditorWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setTitle(Settings.getLanguage().getValue("$APPLICATION_TITLE"));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(Settings.isSaved == false && (Loader.getValueBoolean(Settings.settingsFile.valueOf("$NOTIFY_ON_EXIT", "false")) == true)) {
					// check if saved flag is set
					int i  = JOptionPane.showConfirmDialog(
							frame, 
							Settings.getLanguage().getValue("$EDITOR_CLOSE_NOTIFICATION"), 
							Settings.getLanguage().getValue("$EDITOR_CLOSE_NOTIFICATION_TITLE"), 
							JOptionPane.YES_NO_CANCEL_OPTION
					);
					
					if(i == JOptionPane.OK_OPTION) {
						frame.dispose();
						Console.ErrorExit(ErrorCode.EXIT_OK);
					}
				}else {
					Console.ErrorExit(ErrorCode.EXIT_OK);
				}
			}
		});
		frame.setBounds(100, 100, 1234, 689);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setIconImage(ResourceManager.APPLICATION_ICON);
		frame.setResizable(false);

		RenderPane renderPane = new RenderPane();
		MainEditor ed = new MainEditor(this, Settings.MapTileSize);
		EditorWindow.this.currentEditor = ed;
		renderPane.addRenderCall(ed);
		renderPane.setBounds(0, 0, 945, 628);
		renderPane.start();

		frame.getContentPane().add(renderPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(945, 0, 273, 628);
		frame.getContentPane().add(tabbedPane);

		TileSelectorPane p = new TileSelectorPane();
		renderPane_2 = new RenderPane(640, 480);
		tabbedPane.addTab(Settings.getLanguage().getValue("$EDITOR_TAB_TILE"), null, renderPane_2, null);
		renderPane_2.addRenderCall(p);
		renderPane_2.start();

		JPanel panel_1 = new JPanel();
		panel_1.setToolTipText("Add New Layer");
		tabbedPane.addTab(Settings.getLanguage().getValue("$EDITOR_TAB_LAYER"), null, panel_1, null);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 248, 273);
		panel_1.add(scrollPane);
		
		// ==============================================================================
		// [$LayerControl]

		JButton btnAddNewLayer = new JButton("");
		btnAddNewLayer.setIcon(new ImageIcon(LayerControlMap.layerAdd));
		btnAddNewLayer.setBounds(10, 295, 114, 23);
		panel_1.add(btnAddNewLayer);
		
		JList<String> list = new JList<String>();
		scrollPane.setViewportView(list);
		
		JButton btnMoveLayerUp = new JButton();
		btnMoveLayerUp.setIcon(new ImageIcon(LayerControlMap.layerMoveUp));
		btnMoveLayerUp.setBounds(10, 329, 114, 23);
		panel_1.add(btnMoveLayerUp);
		
		JButton btnMoveLayerDown = new JButton("");
		btnMoveLayerDown.setBounds(134, 329, 124, 23);
		btnMoveLayerDown.setIcon(new ImageIcon(LayerControlMap.layerMoveDown));
		panel_1.add(btnMoveLayerDown);
		
		JButton btnShow = new JButton("");
		btnShow.setBounds(10, 363, 114, 23);
		btnShow.setIcon(new ImageIcon(LayerControlMap.layerShown));
		panel_1.add(btnShow);
		
		JButton btnHide = new JButton("");
		btnHide.setBounds(134, 363, 124, 23);
		btnHide.setIcon(new ImageIcon(LayerControlMap.layerHidden));
		panel_1.add(btnHide);
		
		JButton btnRemove = new JButton("");
		btnRemove.setBounds(134, 295, 124, 23);
		btnRemove.setIcon(new ImageIcon(LayerControlMap.layerRemove));
		
		panel_1.add(btnRemove);
		
		this.c = new LayerController(list,btnAddNewLayer,btnRemove,btnShow,btnHide,btnMoveLayerUp, btnMoveLayerDown, EditorWindow.currentEditor.getMap());
		this.c.start();
		this.c.repaintPane(panel_1);
		
		// ================================================================================
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu(Settings.getLanguage().getValue("$MENU_FILE"));
		JMenuItem newMap = new JMenuItem(Settings.getLanguage().getValue("$MENU_FILE_NEW"));
		newMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(!Settings.lastSavedPath.equals("")) {
					// check if saved flag is set
					int i  = JOptionPane.showConfirmDialog(
							frame, 
							Settings.getLanguage().getValue("$EDITOR_NEW_NOTIFICACTION"), 
							Settings.getLanguage().getValue("$MENU_FILE_NEW"), 
							JOptionPane.YES_NO_CANCEL_OPTION
					);
					
					if(i != JOptionPane.OK_OPTION) {
						return;
					}
				}
				
				
				createMap = CreateMapDialog.Spawn();
				
				createMap.setActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub

						Settings.MapTileSize = createMap.itileSize;
						Settings.MapCellWidth = createMap.imapSizeX;
						Settings.MapCellHeight = createMap.imapSizeY;
						Settings.CurrentTileSet = createMap.tileSetImage;

						EditorWindow.this.tilesetImage = Settings.CurrentTileSet;

						renderPane.clear();
						MainEditor ed = new MainEditor(EditorWindow.this, Settings.MapTileSize);
						currentEditor = ed;
						renderPane.addRenderCall(ed);
						ed.UpdateTileMap(null);
						Core.closeFrame(CreateMapDialog.getWindow());
						
						c.reset(list, btnAddNewLayer, btnRemove, btnShow, btnHide, btnMoveLayerUp, btnMoveLayerDown, ed.getMap());
						c.repaintPane(panel_1);
					}
				});
			}
		});
		newMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		
		
				
		mnFile.add(newMap);
		menuBar.add(mnFile);
		
		JMenuItem mntmSave = new JMenuItem(Settings.getLanguage().getValue("$GENERIC_SAVE_AS"));
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save(true);
			}
		});
		
		JMenuItem mntmSave_1 = new JMenuItem(Settings.getLanguage().getValue("$GENERIC_SAVE"));
		mntmSave_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save(false);
			}
		});
		
		
		mntmSave_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave_1);
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFile.add(mntmSave);
		
		JMenuItem mntmOpen = new JMenuItem(Settings.getLanguage().getValue("$GENERIC_OPEN"));
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(!Settings.lastSavedPath.equals("")) {
					// check if saved flag is set
					int i  = JOptionPane.showConfirmDialog(
							frame, 
							Settings.getLanguage().getValue("$EDITOR_NEW_NOTIFICACTION"), 
							Settings.getLanguage().getValue("$MENU_FILE_NEW"), 
							JOptionPane.YES_NO_CANCEL_OPTION
					);
					
					if(i != JOptionPane.OK_OPTION) {
						return;
					}
				}
				
				JFileChooser chooser = new JFileChooser(new File("."));
				int i  = chooser.showOpenDialog(frame);
				if(i == JFileChooser.APPROVE_OPTION) {
					final File f = chooser.getSelectedFile();
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							JFrame loaderFrame = LoaderDialog.Spawn();
							Map m = Map.openMap(chooser.getSelectedFile());
							Settings.lastSavedPath = chooser.getSelectedFile().getPath();
							if(m != null) {
								EditorWindow.currentEditor.setMap(m);
								EditorWindow.this.c.regenerateList();
								c.reset(list, btnAddNewLayer, btnRemove, btnShow, btnHide, btnMoveLayerUp, btnMoveLayerDown, ed.getMap());
								c.repaintPane(panel_1);
							}
							Core.closeFrame(loaderFrame);
						}
					});
					t.start();
				}
			}
		});
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpen);
		
		JSeparator separator_3 = new JSeparator();
		mnFile.add(separator_3);
		
		JMenuItem mntmPreferences = new JMenuItem( Settings.getLanguage().getValue("$MENU_FILE_SETTINGS"));
		mntmPreferences.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsWindow.Spawn();
			}
		});
		mntmPreferences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmPreferences.setHorizontalAlignment(SwingConstants.LEFT);
		mnFile.add(mntmPreferences);

		JMenu mnEdit = new JMenu(Settings.getLanguage().getValue("$MENU_EDIT"));
		menuBar.add(mnEdit);
		
		JMenuItem mntmClearMap = new JMenuItem(Settings.getLanguage().getValue("$MENU_EDIT_CLEAR"));
		mntmClearMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int o = JOptionPane.showConfirmDialog(null, Settings.getLanguage().getValue("$MENU_EDIT_CLEAR_CONFIRM"));
				if(o == JOptionPane.OK_OPTION) {
					currentEditor.getMap().clear();
				}
			}
		});
		mnEdit.add(mntmClearMap);
		
		JSeparator separator = new JSeparator();
		mnEdit.add(separator);
		
		JLabel lblDrawMode = new JLabel(Settings.getLanguage().getValue("$MENU_EDIT_DRAW_MODE_ITEM"));
		mnEdit.add(lblDrawMode);
		
		ButtonGroup drawModeGroup = new ButtonGroup();

		JRadioButton rdbtnPoint = new JRadioButton(Settings.getLanguage().getValue("$MENU_EDIT_DRAW_MODE_POINT"));
		rdbtnPoint.setSelected(true);
		rdbtnPoint.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(rdbtnPoint.isSelected()) Settings.editorDrawMode = Settings.FILL_MODE_POINT;
			}
		});
		mnEdit.add(rdbtnPoint);
		drawModeGroup.add(rdbtnPoint);
		
		JRadioButton rdbtnFlood = new JRadioButton(Settings.getLanguage().getValue("$MENU_EDIT_DRAW_MODE_FILL"));
		rdbtnFlood.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(rdbtnFlood.isSelected()) Settings.editorDrawMode = Settings.FILL_MODE_FLOOD;
			}
		});
		mnEdit.add(rdbtnFlood);
		drawModeGroup.add(rdbtnFlood);
		
		JSeparator separator_1 = new JSeparator();
		mnEdit.add(separator_1);
		
		JMenuItem mntmUndo = new JMenuItem("Undo");

		Action undoAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				currentEditor.getMap().getActiveLayer().undo();
			}
		};
		undoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_DOWN_MASK));
		mntmUndo.setAction(undoAction);
		mntmUndo.addActionListener(undoAction);
		
		mnEdit.add(mntmUndo);
		
		JMenu mnView = new JMenu(Settings.getLanguage().getValue("$MENU_VIEW"));
		menuBar.add(mnView);
		
		JCheckBox chckbxDrawGridLines = new JCheckBox(Settings.getLanguage().getValue("$MENU_VIEW_DRAW_GRID"));
		mnView.add(chckbxDrawGridLines);
		chckbxDrawGridLines.setSelected(true);
		
		JMenuItem mntmRenderMap = new JMenuItem(Settings.getLanguage().getValue("$MENU_VIEW_RENDER"));
		mntmRenderMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int i = chooser.showSaveDialog(EditorWindow.this.frame);
				if(i == JFileChooser.APPROVE_OPTION) {
					try {
						ImageIO.write(ed.getMap().renderMap(), "PNG", chooser.getSelectedFile());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						Console.Error("Error Saving Image", e1);
					}
				}
			}
		});
		mntmRenderMap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnView.add(mntmRenderMap);
		chckbxDrawGridLines.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Settings.drawGridBox = chckbxDrawGridLines.isSelected();
			}
		});

		JMenu mnProject = new JMenu(Settings.getLanguage().getValue("$MENU_PROJECT"));
		menuBar.add(mnProject);
		
		JMenu mnDebug = new JMenu(Settings.getLanguage().getValue("$MENU_DEBUG"));
		menuBar.add(mnDebug);
		
		/*JMenuItem mntmDrawDebugMap = new JMenuItem("Draw Debug Map");
		mntmDrawDebugMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < currentEditor.getMap().getHorizontalCellCount() * currentEditor.getMap().getVerticalCellCount() - 1; i++) {
					currentEditor.getMap().getActiveLayer().setTile(i, i);
				}
				
			}
		});
		mnDebug.add(mntmDrawDebugMap);*/
		
		JCheckBox chckbxMultiple = new JCheckBox("Multiple?");
		chckbxMultiple.setSelected(Settings.multiple);
		chckbxMultiple.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Settings.multiple = chckbxMultiple.isSelected();
			}
		});
		mnDebug.add(chckbxMultiple);
		
		JSeparator separator_2 = new JSeparator();
		mnDebug.add(separator_2);
		
		JMenuItem mntmSpawnDebugger = new JMenuItem(Settings.getLanguage().getValue("$MENU_DEBUG_SPAWN_DEBUGGER"));
		mntmSpawnDebugger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Debugger.Spawn();
			}
		});
		mnDebug.add(mntmSpawnDebugger);
	}

	public void save(boolean alwaysShow) {
		if(alwaysShow || Settings.lastSavedPath.equals("")) {
			JFileChooser chooser = new JFileChooser(new File("."));
			int choice = chooser.showSaveDialog(frame);
			if(choice == JFileChooser.APPROVE_OPTION) {
				Map.saveMap(EditorWindow.currentEditor.getMap(), chooser.getSelectedFile());
				Settings.lastSavedPath = chooser.getSelectedFile().getPath();
			}
		}else {
			Map.saveMap(EditorWindow.currentEditor.getMap(), new File(Settings.lastSavedPath));
		}
	}
	
	// ============================================================
	public static class NumberOnlyKeyListener implements KeyListener, MouseListener {

		JTextField parent = null;

		public NumberOnlyKeyListener(JTextField f) {
			f.addKeyListener(this);
			f.addMouseListener(this);
			this.parent = f;
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if ((e.getKeyChar() >= '0' && e.getKeyChar() <= '9') || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				parent.setEditable(true);
			} else {
				parent.setEditable(false);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			parent.setEnabled(true);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
}
