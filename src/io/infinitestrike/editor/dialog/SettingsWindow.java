package io.infinitestrike.editor.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import io.infinitestrike.core.Core;
import io.infinitestrike.core.Vector.Vector2i;
import io.infinitestrike.editor.dialog.CreateMapDialog.MapCreationPreset;
import io.infinitestrike.flatpixelutls.Loader;
import io.infinitestrike.flatpixelutls.Loader.LoaderEntry;
import io.infinitestrike.flatpixelutls.Loader.LoaderResult;
import io.infinitestrike.flatpixelutls.Loader.LoaderType;
import io.infintestrike.editor.Settings;
import io.infintestrike.editor.core.ResourceManager;
import io.infintestrike.utils.TextFieldUtils;

public class SettingsWindow extends JPanel{

	private static boolean open = false;
	private static JFrame openFrame = null;
	
	
	public SettingsWindow() {
		this.init();
	}
	
	public static JFrame getFrame() {
		return openFrame;
	}
	
	public static void Spawn() {
		if(open) return;
		JFrame frame = Core.spawnFrame(
				new SettingsWindowContainer(),
				Settings.getLanguage().getValue("$MENU_FILE_SETTINGS"),
				new Vector2i(640,480),
				null,
				false,
				true,
				ResourceManager.APPLICATION_ICON
		);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				open = false;
			}
		});
		SettingsWindow.openFrame = frame;
	}
	
	public void init() {
		if(Settings.settingsFile == null) return;
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		LoaderResult r = Settings.settingsFile;
		for(int i = 0; i < r.size(); i++){
			LoaderEntry entry = r.getValue(i);
			LoaderType type = entry.entryType;
			String keyName = r.getKey(i);
			JPanel internal = new JPanel();
			FlowLayout flowLayout_1 = (FlowLayout) internal.getLayout();
			flowLayout_1.setAlignment(FlowLayout.LEFT);
			JLabel l = null;
			switch(type) {
			case ARRAY:
				break;
			case BOOL:
				l = new JLabel(Loader.keyNameToString(keyName) + ": ");
				JCheckBox b = new JCheckBox();
				b.setSelected(Loader.getValueBoolean(r.valueOf(keyName, "false")));
				b.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						Settings.settingsFile.set(keyName, new LoaderEntry(type, "" + b.isSelected()));
					}
				});
				internal.add(l);
				internal.add(b);
				break;
			case FLOAT:
				l = new JLabel(Loader.keyNameToString(keyName) + ": ");
				JTextField sf = new JTextField();
				sf.addKeyListener(TextFieldUtils.NUMBER_LISTENER);
				sf.setText(Loader.getValueString(r.valueOf(keyName, "0")));
				sf.addKeyListener(new KeyAdapter() {
					public void keyReleased(KeyEvent e) {
						TextFieldUtils.NUMBER_LISTENER.sterilizeString(sf);
						Settings.settingsFile.set(keyName, new LoaderEntry(type,sf.getText()));
					}
				});
				sf.setPreferredSize(new Dimension(250, 24));
				internal.add(l);
				internal.add(sf);
				break;
			case HEX:
				l = new JLabel(Loader.keyNameToString(keyName) + ": ");
				JTextField sh = new JTextField();
				sh.addKeyListener(TextFieldUtils.HEX_LISTENER);
				sh.setText(Loader.getValueString(r.valueOf(keyName, "0")));
				sh.setBackground(new Color(Loader.getValueHex(r.valueOf(keyName, "e3e3e3"))));
				
				int textColor = (Loader.getValueHex(r.valueOf(keyName, "e3e3e3")) > 0x888888) ? 0x0 : 0xFFFFFF; 
				sh.setForeground(new Color(textColor));
				
				sh.addKeyListener(new KeyAdapter() {
					public void keyReleased(KeyEvent e) {
						TextFieldUtils.HEX_LISTENER.sterilizeString(sh);
						Settings.settingsFile.set(keyName, new LoaderEntry(type,sh.getText()));
						sh.setBackground(new Color(Loader.getValueHex(r.valueOf(keyName, "e3e3e3"))));
						int textColor = (Loader.getValueHex(r.valueOf(keyName, "e3e3e3")) >= 0x888888) ? 0x0 : 0xFFFFFF; 
						sh.setForeground(new Color(textColor));
					}
				});
				sh.setPreferredSize(new Dimension(250, 24));
				JButton picker = new JButton(Settings.getLanguage().getValue("$SETTINGS_COLOR_PICKER_TEXT"));
				picker.setPreferredSize(new Dimension(128, 24));
				picker.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						Color c = JColorChooser.showDialog(
								openFrame, 
								Settings.getLanguage().getValue("$SETTINGS_COLOR_PICKER_TITLE"), 
								new Color(Loader.getValueHex(r.valueOf(keyName, "000000")))
						);
						
						if(c != null) {
							String hexColor = "";
							hexColor += Integer.toHexString(c.getRed());
							hexColor += Integer.toHexString(c.getGreen());
							hexColor += Integer.toHexString(c.getBlue());
							sh.setText(hexColor);
							Settings.settingsFile.set(keyName, new LoaderEntry(type,hexColor));
							sh.setBackground(new Color(Loader.getValueHex(r.valueOf(keyName, "e3e3e3"))));
							int textColor = (Loader.getValueHex(r.valueOf(keyName, "e3e3e3")) >= 0x888888) ? 0x0 : 0xFFFFFF; 
							sh.setForeground(new Color(textColor));
						}
					}
				});
				internal.add(l);
				internal.add(sh);
				internal.add(picker);
				break;
			case INT_32:
				l = new JLabel(Loader.keyNameToString(keyName) + ": ");
				JTextField si = new JTextField();
				si.addKeyListener(TextFieldUtils.WHOLE_NUMBER_LISTENER);
				si.setText(Loader.getValueString(r.valueOf(keyName, "0")));
				si.addKeyListener(new KeyAdapter() {
					public void keyReleased(KeyEvent e) {
						TextFieldUtils.WHOLE_NUMBER_LISTENER.sterilizeString(si);
						Settings.settingsFile.set(keyName, new LoaderEntry(type,si.getText()));
					}
				});
				si.setPreferredSize(new Dimension(250, 24));
				internal.add(l);
				internal.add(si);
				break;
			case INT_64:
				l = new JLabel(Loader.keyNameToString(keyName) + ": ");
				JTextField sl = new JTextField();
				sl.addKeyListener(TextFieldUtils.WHOLE_NUMBER_LISTENER);
				sl.setText(Loader.getValueString(r.valueOf(keyName, "0")));
				sl.addKeyListener(new KeyAdapter() {
					public void keyReleased(KeyEvent e) {
						TextFieldUtils.WHOLE_NUMBER_LISTENER.sterilizeString(sl);
						Settings.settingsFile.set(keyName, new LoaderEntry(type,sl.getText()));
					}
				});
				sl.setPreferredSize(new Dimension(250, 24));
				internal.add(l);
				internal.add(sl);
				break;
			case DIR:
				l = new JLabel(Loader.keyNameToString(keyName)+ ": ");
				JTextField dir = new JTextField(Loader.getValueString(r.valueOf(keyName, "false")));
				dir.addKeyListener(new KeyAdapter() {
					public void keyReleased(KeyEvent e) {
						Settings.settingsFile.set(keyName, new LoaderEntry(type,dir.getText()));
					}
				});
				dir.setPreferredSize(new Dimension(250, 24));
				JButton browse = new JButton("...");
				browse.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						JFileChooser c = new JFileChooser(new File("."));
						c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int choice = c.showOpenDialog(openFrame);
						if(choice == JFileChooser.APPROVE_OPTION) {
							String relativeFileName = new File(".").getAbsolutePath();
							String f = c.getSelectedFile().getPath();
							f = f.replace(relativeFileName, "./");
							dir.setText(f);
							Settings.settingsFile.set(keyName, new LoaderEntry(type,f));
						}
					}
				});
				browse.setPreferredSize(new Dimension(128, 24));
				internal.add(l);
				internal.add(dir);
				internal.add(browse);
				break;
			case STRING:
				l = new JLabel(Loader.keyNameToString(keyName) + ": ");
				JTextField s = new JTextField(Loader.getValueString(r.valueOf(keyName, "false")));
				s.addKeyListener(new KeyAdapter() {
					public void keyReleased(KeyEvent e) {
						Settings.settingsFile.set(keyName, new LoaderEntry(type,s.getText()));
					}
				});
				s.setPreferredSize(new Dimension(250, 24));
				internal.add(l);
				internal.add(s);
				break;
			case OPTION:
				l = new JLabel(Loader.keyNameToString(keyName) + ": ");
				LoaderEntry option = r.valueOf(keyName, "0,null");
				if(option.entryValueToArray()[1] != "null") {
					JComboBox<String> choices = new JComboBox<String>();
					String arrayEntry = option.entryValueToArray()[1];
					String[] optionArray = Loader.getValueArray(r.valueOf(arrayEntry, "null"));
					if(optionArray[0] != null) {
						choices.setModel(new DefaultComboBoxModel<String>(optionArray));
						choices.setSelectedIndex(Integer.parseInt(option.entryValueToArray()[0]));
						choices.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								int index = choices.getSelectedIndex();
								LoaderEntry tempEntry = Loader.constructOptionEntry(index, arrayEntry);
								Settings.settingsFile.set(keyName, tempEntry);
							}
						});
						internal.add(l);
						internal.add(choices);
					}
				}
				break;
			default:
				break;
			}
			this.add(internal);
		}
	}
}
