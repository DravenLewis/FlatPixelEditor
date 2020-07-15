package io.infinitestrike.editor.dialog;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class LoaderPane extends JPanel {

	/**
	 * Create the panel.
	 */
	public LoaderPane() {
		setLayout(null);
		//354,92
		setPreferredSize(new Dimension(354,92));
		JLabel lblLoading = new JLabel("Loading");
		lblLoading.setBounds(10, 11, 48, 14);
		add(lblLoading);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setBounds(10, 36, 333, 19);
		add(progressBar);

	}
}
