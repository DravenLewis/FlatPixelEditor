package io.infinitestrike.flatpixel.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;

import io.infinitestrike.flatpixel.MapEditor;
import io.infinitestrike.flatpixel.Settings;
import io.infinitestrike.flatpixel.components.RenderPane;
import io.infinitestrike.flatpixel.components.RenderPane.RenderCall;
import io.infinitestrike.flatpixel.controllers.LayerController;
import io.infinitestrike.flatpixel.core.Core;
import io.infinitestrike.flatpixel.core.ResourceManager;
import io.infinitestrike.flatpixel.dialog.CreateMapDialog;
import io.infinitestrike.flatpixel.dialog.Debugger;
import io.infinitestrike.flatpixel.dialog.LoaderDialog;
import io.infinitestrike.flatpixel.dialog.SettingsWindow;
import io.infinitestrike.flatpixel.grafx.MainEditorView;
import io.infinitestrike.flatpixel.grafx.TileSelectorView;
import io.infinitestrike.flatpixel.grafx.drawables.RenderCallNotification;
import io.infinitestrike.flatpixel.logging.Console;
import io.infinitestrike.flatpixel.logging.ErrorCode;
import io.infinitestrike.flatpixel.tilemap.Map;
import io.infinitestrike.gen.S;
import io.infinitestrike.utils.FileFilterFactory;
import io.infinitestrike.utils.ImageTools;

public class EditorWindow extends JFrame {

	private final EditorWindowController controller;

	private final RenderPane mainEditorWindowPane;
	private final RenderPane sideEditorWindowPane;

	private JMenuBar menuBar;
	private JPanel panel;
	private JPanel panel_1;

	private BufferedImage currentTileSet = null;
	private JMenuItem mntmOpen;
	private JSeparator separator;
	private JMenuItem mntmPreferences;
	private JMenuItem mntmNew;
	private JPanel panelLayerView;
	
	private LayerController layerController;
	private JPanel panel_5;
	private JPanel panel_6;
	private JScrollPane scrollPane;
	private JList<String> list;
	
	private JButton btnNewLayer;
	private JButton btnRemoveLayer;
	private JButton btnShowLayer;
	private JButton btnHideLayer;
	private JButton btnMoveLayerUp;
	private JButton btnMoveLayerDown;
	private JMenu mnEdit;
	private JMenu mnView;
	private JMenu mnProject;
	private JMenu mnDebug;
	private JMenuItem mntmShowDebugger;
	private JToolBar actionToolbar;
	private JButton btnDrawModePoint;
	private JButton btnDrawModeFill;
	private JMenuItem mntmSpawnNotif;
	private JMenuItem mntmSave;
	private JMenuItem mntmSaveAs;
	private JCheckBox chckbxToggleGrid;
	private JMenuItem mntmSaveRender;
	
	public EditorWindow() {

		setSize(1024, 768);
		this.setMinimumSize(new Dimension(1234, 689));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle(Settings.getLanguage().getValue(S.$APPLICATION_TITLE));
		
		controller = new EditorWindowController(this);
		getContentPane().setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		this.mainEditorWindowPane = new RenderPane();
		this.mainEditorWindowPane.addRenderCall(new MainEditorView());
		panel.add(mainEditorWindowPane, BorderLayout.CENTER);
		this.mainEditorWindowPane.start();

		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setPreferredSize(new Dimension(350, panel_1.getPreferredSize().height));
		getContentPane().add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_1.add(tabbedPane);

		JPanel panelTileView = new JPanel();
		tabbedPane.addTab(Settings.getLanguage().getValue(S.$EDITOR_TAB_TILE), null, panelTileView, null);
		panelTileView.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panelTileView.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BorderLayout(0, 0));

		this.sideEditorWindowPane = new RenderPane();
		FlowLayout flowLayout = (FlowLayout) this.sideEditorWindowPane.getLayout();
		flowLayout.setVgap(0);
		this.sideEditorWindowPane.addRenderCall(new TileSelectorView());
		this.sideEditorWindowPane.start();
		panel_3.add(this.sideEditorWindowPane, BorderLayout.CENTER);

		panelLayerView = new JPanel();
		tabbedPane.addTab(Settings.getLanguage().getValue(S.$EDITOR_TAB_LAYER), null, panelLayerView, null);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		list = new JList<String>();

		panelLayerView.setLayout(new BorderLayout(0, 0));
		
		panel_6 = new JPanel();
		panelLayerView.add(panel_6, BorderLayout.CENTER);
		
		btnNewLayer = new JButton(Settings.getLanguage().getValue(S.$BTN_NEW_LAYER));
		btnNewLayer.setIcon(new ImageIcon(ResourceManager.layerAdd));
		
		btnRemoveLayer = new JButton(Settings.getLanguage().getValue(S.$BTN_REMOVE_LAYER));
		btnRemoveLayer.setIcon(new ImageIcon(ResourceManager.layerRemove));
		
		btnShowLayer = new JButton(Settings.getLanguage().getValue(S.$BTN_SHOW_LAYER));
		btnShowLayer.setIcon(new ImageIcon(ResourceManager.layerShown));
		
		btnHideLayer = new JButton(Settings.getLanguage().getValue(S.$BTN_HIDE_LAYER));
		btnHideLayer.setIcon(new ImageIcon(ResourceManager.layerHidden));
		
		btnMoveLayerUp = new JButton(Settings.getLanguage().getValue(S.$BTN_MOVE_UP));
		btnMoveLayerUp.setIcon(new ImageIcon(ResourceManager.layerMoveUp));
		
		btnMoveLayerDown = new JButton(Settings.getLanguage().getValue(S.$BTN_MOVE_DOWN));
		btnMoveLayerDown.setIcon(new ImageIcon(ResourceManager.layerMoveDown));
		
		
		this.layerController = new LayerController(
				this.list,
				this.btnNewLayer,
				this.btnRemoveLayer,
				this.btnShowLayer,
				this.btnHideLayer,
				this.btnMoveLayerUp,
				this.btnMoveLayerDown,
				this.getMainEditorViewRenderCall().getMap()
		);
		
		this.layerController.start();
		this.layerController.repaintPane(this.getLayerPanel());
		
		JLabel lblCreateRemove = new JLabel(Settings.getLanguage().getValue(S.$LBL_CREATE_REMOVE));
		
		JLabel lblShowHide = new JLabel(Settings.getLanguage().getValue(S.$LBL_HIDE_SHOW));
		
		JLabel lblMoveLayerUp = new JLabel(Settings.getLanguage().getValue(S.$LBL_MOVE));
		
		JSeparator separator_1 = new JSeparator();
		
		JLabel lblLayerControls = new JLabel(Settings.getLanguage().getValue(S.$LAYER_CONTROL_HEADER));
		lblLayerControls.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addComponent(separator_1, GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
						.addComponent(lblCreateRemove)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(btnShowLayer, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnNewLayer, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
								.addComponent(btnMoveLayerUp, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addComponent(btnRemoveLayer, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
								.addComponent(btnHideLayer, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
								.addComponent(btnMoveLayerDown, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblShowHide)
							.addPreferredGap(ComponentPlacement.RELATED, 267, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblMoveLayerUp)
							.addPreferredGap(ComponentPlacement.RELATED, 218, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblLayerControls))
					.addContainerGap())
		);
		gl_panel_6.setVerticalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblLayerControls)
					.addGap(20)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblCreateRemove)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnRemoveLayer, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewLayer))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblShowHide)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnShowLayer)
						.addComponent(btnHideLayer))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblMoveLayerUp)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnMoveLayerUp)
						.addComponent(btnMoveLayerDown))
					.addContainerGap(170, Short.MAX_VALUE))
		);
		panel_6.setLayout(gl_panel_6);
		
		panel_5 = new JPanel();
		panel_5.setMinimumSize(new Dimension(10, 300));
		panel_5.setPreferredSize(new Dimension(10, 300));
		panelLayerView.add(panel_5, BorderLayout.NORTH);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		panel_5.add(scrollPane, BorderLayout.CENTER);
		
		scrollPane.setViewportView(list);
		
		actionToolbar = new JToolBar();
		actionToolbar.setPreferredSize(new Dimension(13, 32));
		actionToolbar.setMinimumSize(new Dimension(13, 32));
		actionToolbar.setMaximumSize(new Dimension(13, 32));
		getContentPane().add(actionToolbar, BorderLayout.NORTH);
		
		btnDrawModePoint = new JButton("");
		btnDrawModePoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Settings.editorDrawMode = Settings.FILL_MODE_POINT;
				new RenderCallNotification("Draw Mode: Point", 500, getMainEditorViewPane());
			}
		});
		btnDrawModePoint.setMinimumSize(new Dimension(32, 32));
		btnDrawModePoint.setMaximumSize(new Dimension(32, 32));
		btnDrawModePoint.setIcon(ImageTools.getScaledImageIcon(ResourceManager.DRAW_ICON, 20, 20));
		actionToolbar.add(btnDrawModePoint);
		
		
		btnDrawModeFill = new JButton("");
		btnDrawModeFill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Settings.editorDrawMode = Settings.FILL_MODE_FLOOD;
				new RenderCallNotification("Draw Mode: Flood", 500, getMainEditorViewPane());
			}
		});
		btnDrawModeFill.setMinimumSize(new Dimension(32, 32));
		btnDrawModeFill.setMaximumSize(new Dimension(32, 32));
		btnDrawModeFill.setIcon(ImageTools.getScaledImageIcon(ResourceManager.FILL_ICON, 20, 20));
		actionToolbar.add(btnDrawModeFill);
		
		JMenu mnFile = new JMenu(Settings.getLanguage().getValue(S.$MENU_FILE));
		menuBar.add(mnFile);

		mntmNew = new JMenuItem(Settings.getLanguage().getValue(S.$MENU_FILE_NEW));
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				CreateMapDialog createMap;
				
				if (!Settings.getMapSettings().lastSavedMapLocation.equals("")) {
					// check if saved flag is set
					int i = JOptionPane.showConfirmDialog(
							MapEditor.getApplicationWindow(),
							Settings.getLanguage().getValue(S.$EDITOR_NEW_NOTIFICACTION),
							Settings.getLanguage().getValue(S.$MENU_FILE_NEW), JOptionPane.YES_NO_CANCEL_OPTION);

					if (i != JOptionPane.OK_OPTION) {
						return;
					}
				}

				createMap = CreateMapDialog.Spawn();

				createMap.setActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub

						Settings.getMapSettings().tileSize = createMap.itileSize;
						Settings.getMapSettings().tileCellsW = createMap.imapSizeX;
						Settings.getMapSettings().tileCellsH = createMap.imapSizeY;
						Settings.getMapSettings().tileSetImage = createMap.tileSetImage;

						MapEditor.getApplicationWindow().currentTileSet = Settings.getMapSettings().tileSetImage;

						MainEditorView mainEditor = getMainEditorViewRenderCall();
						mainEditor.UpdateTileMap(null);
						Core.closeFrame(CreateMapDialog.getWindow());

						getLayerPanel();
						
						layerController.reset(
								list, 
								btnNewLayer, 
								btnRemoveLayer, 
								btnShowLayer, 
								btnHideLayer, 
								btnMoveLayerUp, 
								btnMoveLayerDown,
								MapEditor.getApplicationWindow().getMainEditorViewRenderCall().getMap()
						);
						
						layerController.repaintPane(getLayerPanel());
					}
				});
			}
		});
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(mntmNew);

		mntmOpen = new JMenuItem(Settings.getLanguage().getValue(S.$GENERIC_OPEN));
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!Settings.getMapSettings().lastSavedMapLocation.equals("")) {
					// check if saved flag is set
					int i  = JOptionPane.showConfirmDialog(
							MapEditor.getApplicationWindow(), 
							Settings.getLanguage().getValue(S.$EDITOR_NEW_NOTIFICACTION), 
							Settings.getLanguage().getValue(S.$MENU_FILE_NEW), 
							JOptionPane.YES_NO_CANCEL_OPTION
					);
					
					if(i != JOptionPane.OK_OPTION) {
						return;
					}
				}
				
				JFileChooser chooser = new JFileChooser(new File("."));
				int i  = chooser.showOpenDialog(MapEditor.getApplicationWindow());
				if(i == JFileChooser.APPROVE_OPTION) {
					final File f = chooser.getSelectedFile();
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							LoaderDialog.instance.Spawn();
							Map m = Map.openMap(chooser.getSelectedFile());
							Settings.getMapSettings().lastSavedMapLocation = chooser.getSelectedFile().getPath();
							if(m != null) {
								MapEditor.getApplicationWindow().getMainEditorViewRenderCall().setMap(m);
								MapEditor.getApplicationWindow().layerController.regenerateList();
								
								layerController.reset(
										list, 
										btnNewLayer, 
										btnRemoveLayer, 
										btnShowLayer, 
										btnHideLayer, 
										btnMoveLayerUp, 
										btnMoveLayerDown,
										MapEditor.getApplicationWindow().getMainEditorViewRenderCall().getMap()
								);
							
								layerController.repaintPane(getLayerPanel());
							}
							LoaderDialog.instance.Close();
						}
					});
					t.start();
				}
			}
		});
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpen);
		
		mntmSave = new JMenuItem(Settings.getLanguage().getValue(S.$GENERIC_SAVE));
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save(false);
			}
		});
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);
		
		mntmSaveAs = new JMenuItem(Settings.getLanguage().getValue(S.$GENERIC_SAVE_AS));
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save(true);
			}
		});
		
		mntmSaveRender = new JMenuItem(Settings.getLanguage().getValue(S.$MENU_FILE_SAVE_RENDER));
		mntmSaveRender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(FileFilterFactory.MAP_FILES);
				int i = chooser.showSaveDialog(MapEditor.getApplicationWindow());
				if(i == JFileChooser.APPROVE_OPTION) {
					try {
						ImageIO.write(getMainEditorViewRenderCall().getMap().renderMap(), "PNG", chooser.getSelectedFile());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						Console.Error("Error Saving Image", e1);
					}
				}
			}
		});
		mntmSaveRender.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mnFile.add(mntmSaveRender);
		
		mntmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFile.add(mntmSaveAs);

		separator = new JSeparator();
		mnFile.add(separator);

		mntmPreferences = new JMenuItem(Settings.getLanguage().getValue(S.$MENU_FILE_SETTINGS));
		mntmPreferences.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsWindow.Spawn(); 
			}
		});
		mntmPreferences
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFile.add(mntmPreferences);
		
		mnEdit = new JMenu(Settings.getLanguage().getValue(S.$MENU_EDIT));
		menuBar.add(mnEdit);
		
		mnView = new JMenu(Settings.getLanguage().getValue(S.$MENU_VIEW));
		menuBar.add(mnView);
		
		chckbxToggleGrid = new JCheckBox(Settings.getLanguage().getValue(S.$MENU_VIEW_DRAW_GRID));
		chckbxToggleGrid.setSelected(true);
		chckbxToggleGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chckbxToggleGrid.setSelected(!chckbxToggleGrid.isSelected());
				Settings.drawGridBox = !Settings.drawGridBox;
			}
		});
		mnView.add(chckbxToggleGrid);
		
		mnProject = new JMenu(Settings.getLanguage().getValue(S.$MENU_PROJECT));
		menuBar.add(mnProject);
		
		mnDebug = new JMenu(Settings.getLanguage().getValue(S.$MENU_DEBUG));
		menuBar.add(mnDebug);
		
		mntmShowDebugger = new JMenuItem(Settings.getLanguage().getValue(S.$MENU_DEBUG_SPAWN_DEBUGGER));
		mntmShowDebugger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Debugger.Spawn();
			}
		});
		mntmShowDebugger.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnDebug.add(mntmShowDebugger);
		
		setVisible(true);
	}

	protected JPanel getMainPanel() {
		return panel;
	}

	protected JPanel getSidePanel() {
		return panel_1;
	}

	public RenderPane getMainEditorViewPane() {
		return this.mainEditorWindowPane;
	}

	public RenderPane getSideEditorViewPane() {
		return this.sideEditorWindowPane;
	}

	public BufferedImage getCurrentTileSet() {
		return currentTileSet;
	}

	public void setCurrentTileSet(BufferedImage currentTileSet) {
		this.currentTileSet = currentTileSet;
	}

	public MainEditorView getMainEditorViewRenderCall() {
		for(RenderCall call : this.getMainEditorViewPane().getRenderCalls()) {
			if(call instanceof MainEditorView) {
				return (MainEditorView) call;
			}
		}
		return null;
	}
	
	public void save(boolean alwaysShow) {
		if(alwaysShow || Settings.getMapSettings().lastSavedMapLocation.equals("")) {
			JFileChooser chooser = new JFileChooser(new File("."));
			int choice = chooser.showSaveDialog(this);
			if(choice == JFileChooser.APPROVE_OPTION) {
				Map.saveMap(this.getMainEditorViewRenderCall().getMap(), chooser.getSelectedFile());
				Settings.getMapSettings().lastSavedMapLocation = chooser.getSelectedFile().getPath();
			}
		}else {
			Map.saveMap(this.getMainEditorViewRenderCall().getMap(), new File(Settings.getMapSettings().lastSavedMapLocation));
		}
		
		Settings.getMapSettings().mapSaved = true;
	}
	
	private static final class EditorWindowController extends WindowAdapter implements ComponentListener {

		private final EditorWindow window;

		public EditorWindowController(EditorWindow window) {
			this.window = window;
			this.window.addWindowListener(this);
			this.window.addComponentListener(this);
			this.window.addWindowStateListener(this);
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			if(!Settings.getMapSettings().mapSaved) {
				int o = JOptionPane.showConfirmDialog(window, Settings.getLanguage().getValue(S.$EDITOR_CLOSE_NOTIFICATION));
				if(o == JOptionPane.YES_OPTION) {
					window.getMainEditorViewPane().stop();
					window.getSideEditorViewPane().stop();
					window.dispose();
					MapEditor.ErrorExit(ErrorCode.EXIT_OK);
				}
			}else {
				window.getMainEditorViewPane().stop();
				window.getSideEditorViewPane().stop();
				window.dispose();
				MapEditor.ErrorExit(ErrorCode.EXIT_OK);
			}
		}

		public void windowStateChanged(WindowEvent e) {

		}

		public void windowOpened(WindowEvent e) {

		}

		@Override
		public void componentResized(ComponentEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub

		}
	}
	protected JPanel getLayerPanel() {
		return panelLayerView;
	}
}
