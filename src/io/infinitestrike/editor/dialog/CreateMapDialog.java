package io.infinitestrike.editor.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filters.ImageFilter;

import io.infinitestrike.core.Console;
import io.infinitestrike.core.Core;
import io.infinitestrike.core.Vector.Vector2i;
import io.infintestrike.editor.Settings;
import io.infintestrike.editor.core.ResourceManager;

public class CreateMapDialog extends JPanel {
	private JTextField mapSizeX;
	private JTextField mapSizeY;
	private JTextField tileSize;

	public BufferedImage tileSetImage = null;
	public int imapSizeX = -1;
	public int imapSizeY = -1;
	public int itileSize = -1;

	private ActionListener currentActionListener = null;
	private ArrayList<MapCreationPreset> presets = new ArrayList<MapCreationPreset>();
	private String[] presetName = null;

	public void setActionListener(ActionListener e) {
		this.currentActionListener = e;
	}

	private static JFrame window = null;
	
	/**
	 * Create the panel.
	 */
	
	public static JFrame getWindow() {
		return window;
	}
	
	public static CreateMapDialog Spawn() {
		if(window != null) {
			window.requestFocus();
			return null;
		}
		
		CreateMapDialog createMap = new CreateMapDialog();
		JFrame frame = Core.spawnFrame(
				createMap,
				Settings.getLanguage().getValue("$DIALOG_CREATE_HEADER"),
				new Vector2i(470, 237),
				null,
				false,
				true,
				ResourceManager.APPLICATION_ICON
		);
		
		frame.pack();
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				window = null;
			}
		});
		window = frame;
		return createMap;
	}
	
	public CreateMapDialog() {

		setPreferredSize(new Dimension(470,237));
		
		// add custom presets
		presets.add(new MapCreationPreset("Classic 8-Bit (Tile Size 8, Map Size 32 x 24)", 8, 32, 24));
		presets.add(new MapCreationPreset("Small 16-Bit (Tile Size 16. Map Size 16 x 12)", 16, 16, 12));
		presets.add(new MapCreationPreset("Standard (Tile Size 32, Map Size 20x20)", 32, 20, 20));

		// load user presets
		String[] userPresets = Settings.userPresets;

		for (String s : userPresets) {
			presets.add(new MapCreationPreset(s));
		}
		// After Load
		this.presetName = new String[presets.size()];
		for (int x = 0; x < this.presetName.length; x++)
			this.presetName[x] = this.presets.get(x).name;

		setLayout(null);

		JLabel lblCreateMap = new JLabel(Settings.getLanguage().getValue("$DIALOG_CREATE_HEADER"));
		lblCreateMap.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCreateMap.setBounds(22, 31, 418, 14);
		add(lblCreateMap);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 56, 449, 2);
		add(separator);

		JLabel lblMapSize = new JLabel(Settings.getLanguage().getValue("$DIALOG_CREATE_LABEL_MAP_SIZE"));
		lblMapSize.setBounds(22, 69, 187, 14);
		add(lblMapSize);

		mapSizeX = new JTextField();
		mapSizeX.setBounds(22, 91, 48, 20);
		add(mapSizeX);
		mapSizeX.setColumns(10);

		JLabel lblX = new JLabel("x");
		lblX.setBounds(80, 94, 15, 14);
		add(lblX);

		mapSizeY = new JTextField();
		mapSizeY.setColumns(10);
		mapSizeY.setBounds(95, 91, 48, 20);
		add(mapSizeY);

		JLabel lblNewLabel = new JLabel(Settings.getLanguage().getValue("$DIALOG_CREATE_LABEL_TILE_SIZE"));
		lblNewLabel.setBounds(22, 122, 491, 14);
		add(lblNewLabel);

		tileSize = new JTextField();
		tileSize.setColumns(10);
		tileSize.setBounds(22, 142, 48, 20);
		add(tileSize);

		JLabel lblTileset = new JLabel(Settings.getLanguage().getValue("$DIALOG_CREATE_LABEL_TILESET"));
		lblTileset.setBounds(20, 173, 99, 14);
		add(lblTileset);

		JLabel tileSetName = new JLabel(Settings.getLanguage().getValue("$DIALOG_CREATE_LABEL_TILESETNAME"));
		tileSetName.setBackground(Color.WHITE);
		tileSetName.setBounds(95, 173, 394, 14);
		add(tileSetName);

		JButton btnOpen = new JButton(Settings.getLanguage().getValue("$DIALOG_CREATE_BUTTON_OPEN"));
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				c.setAcceptAllFileFilterUsed(false);
				c.addChoosableFileFilter(new ImageFilter());
				if (c.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						CreateMapDialog.this.tileSetImage = ImageIO.read(c.getSelectedFile());
						tileSetName.setText(c.getSelectedFile().getName());
						Settings.CURRENT_TILESET_FILE = c.getSelectedFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						Console.Error("Cannot Read File", e1);
					}
				}
			}
		});
		btnOpen.setBounds(22, 198, 89, 23);
		add(btnOpen);

		JButton btnCreate = new JButton(Settings.getLanguage().getValue("$DIALOG_CREATE_BUTTON_CREATE"));
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					imapSizeX = Integer.parseInt(mapSizeX.getText());
					imapSizeY = Integer.parseInt(mapSizeY.getText());
					itileSize = Integer.parseInt(tileSize.getText());

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), Settings.getLanguage().getValue("$ERROR_GENERIC"),
							JOptionPane.WARNING_MESSAGE);
					Console.Error("An Error Occurred", ex);
				}

				String message = "";
				if (imapSizeX == -1)
					message += Settings.getLanguage().getValue("$DIALOG_CREATE_ERROR_MAPSIZEX") + ", \n";
				if (imapSizeY == -1)
					message += Settings.getLanguage().getValue("$DIALOG_CREATE_ERROR_MAPSIZEY") + ", \n";
				if (itileSize == -1)
					message += Settings.getLanguage().getValue("$DIALOG_CREATE_ERROR_TILESIZE") + ", \n";
				if (tileSetImage == null)
					message += Settings.getLanguage().getValue("$DIALOG_CREATE_ERROR_TILESET") + ". \n";

				if (message != "") {
					message += "\n\n " + Settings.getLanguage().getValue("$DIALOG_CREATE_ERROR_WARNING");
					JOptionPane.showMessageDialog(null, message, Settings.getLanguage().getValue("$DIALOG_CREATE_ERROR_WARNING"), JOptionPane.WARNING_MESSAGE);
				} else {
					if (CreateMapDialog.this.currentActionListener != null) {
						currentActionListener.actionPerformed(null);
					}
				}
			}
		});
		btnCreate.setBounds(358, 198, 89, 23);
		add(btnCreate);

		JLabel lblPresets = new JLabel(Settings.getLanguage().getValue("$DIALOG_CREATE_LABEL_PRESETS"));
		lblPresets.setBounds(153, 69, 208, 14);
		add(lblPresets);

		JComboBox<String> comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MapCreationPreset s = presets.get(comboBox.getSelectedIndex());
				if (s != null) {
					tileSize.setText("" + s.tileSize);
					mapSizeX.setText("" + s.mapSizeX);
					mapSizeY.setText("" + s.mapSizeY);
				}
			}
		});
		comboBox.setModel(new DefaultComboBoxModel<String>(this.presetName));
		comboBox.setBounds(153, 90, 294, 22);
		add(comboBox);
		
		JButton btnCancel = new JButton(Settings.getLanguage().getValue("$GENERIC_CANCEL"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Core.closeFrame(window);
			}
		});
		btnCancel.setBounds(259, 198, 89, 23);
		add(btnCancel);

	}

	public static class MapCreationPreset {
		public final String name;
		public final int tileSize;
		public final int mapSizeX;
		public final int mapSizeY;

		public MapCreationPreset(String n, int ts, int tx, int ty) {
			this.name = n;
			this.tileSize = ts;
			this.mapSizeX = tx;
			this.mapSizeY = ty;
		}

		public MapCreationPreset(String n) {
			// Preset Template
			// n:Test Preset, ts:32, tx:30, ty:30
			String dName = "";
			int dTs = 0;
			int dTx = 0;
			int dTy = 0;

			String[] en = n.split(",");
			for (String s : en) {
				try {
					String[] een = s.split(":");
					switch (een[0].toLowerCase().trim()) {
					case "n":
						dName = een[1];
						break;
					case "ts":
						dTs = Integer.parseInt(een[1].trim());
						break;
					case "tx":
						dTx = Integer.parseInt(een[1].trim());
						break;
					case "ty":
						dTy = Integer.parseInt(een[1].trim());
						break;
					}
				} catch (Exception e) {
					Console.Error("Cannot Convert Letter into Int", e);
				}
			}

			this.name = dName;
			this.tileSize = dTs;
			this.mapSizeX = dTx;
			this.mapSizeY = dTy;
		}
	}
}
