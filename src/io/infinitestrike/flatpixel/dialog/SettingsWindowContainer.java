package io.infinitestrike.flatpixel.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import io.infinitestrike.flatpixel.Settings;
import io.infinitestrike.flatpixel.compat.Loader.LoaderEntry;
import io.infinitestrike.flatpixel.core.Core;

public class SettingsWindowContainer extends JPanel {

	/**
	 * Create the panel.
	 */
	public SettingsWindowContainer() {
		setLayout(new BorderLayout(0, 0));
		
		HashMap<String,LoaderEntry> copy = Settings.getSettings().getCopy();
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panel, BorderLayout.NORTH);
		
		JLabel lblPreferences = new JLabel(Settings.getLanguage().getValue("$MENU_FILE_SETTINGS"));
		lblPreferences.setFont(new Font("Tahoma", Font.PLAIN, 32));
		lblPreferences.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblPreferences);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		add(panel_1, BorderLayout.SOUTH);
		
		JButton btnCancel = new JButton(Settings.getLanguage().getValue("$GENERIC_CANCEL"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!Settings.getSettings().isMapSame(copy)) {
					int choice = JOptionPane.showConfirmDialog(
							SettingsWindow.getFrame(), 
							Settings.getLanguage().getValue("$DIALOG_SAVE_UNSAVED_MESSAGE"),
							Settings.getLanguage().getValue("$DIALOG_SAVE_UNSAVED_TITLE"),
							JOptionPane.YES_NO_CANCEL_OPTION
					);
					
					if(choice == JOptionPane.OK_OPTION) {
						Settings.getSettings().setMap(copy);
						Core.closeFrame(SettingsWindow.getFrame());
					}
				}else {
					Core.closeFrame(SettingsWindow.getFrame());
				}
			}
		});
		panel_1.add(btnCancel);
		
		JButton btnSave = new JButton(Settings.getLanguage().getValue("$GENERIC_SAVE"));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Settings.saveSettingsFile();
				Core.closeFrame(SettingsWindow.getFrame());
			}
		});
		panel_1.add(btnSave);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		SettingsWindow settingsWindow = new SettingsWindow();
		scrollPane.setViewportView(settingsWindow);

	}

}
