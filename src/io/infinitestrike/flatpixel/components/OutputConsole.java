package io.infinitestrike.flatpixel.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import io.infinitestrike.flatpixel.Settings;
import io.infinitestrike.flatpixel.compat.Loader;
import io.infinitestrike.flatpixel.core.ResourceManager;
import io.infinitestrike.flatpixel.logging.Console;

public class OutputConsole extends JPanel {
	private JTextArea textArea;

	private final OutputStream stdout = System.out; 
	private final OutputStream stderr = System.err;
	
	private static OutputConsole oc;
	
	
	/**
	 * Create the panel.
	 */
	public OutputConsole() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
	}

	public static void load() {
		if(oc != null) return;
		oc = new OutputConsole();
	}
	
	public static void postSettingsLoad() {
		ResourceManager.step = "Styling Debugger";
		getConsole().textArea.setBackground(new Color(Loader.getValueHex(Settings.getSettings().valueOf("$CONSOLE_COLOR_BACKGROUND", "3e3e3e"))));
		getConsole().textArea.setForeground(new Color(Loader.getValueHex(Settings.getSettings().valueOf("$CONSOLE_COLOR_FOREGROUND", "888888"))));
		getConsole().textArea.setEditable(false);
		
		
		if(Loader.getValueBoolean(Settings.getSettings().valueOf("$REDIRECT_OUTPUT", "true"))) {
			ResourceManager.step = "Redirecting Input Stream";
			OutputConsole.getConsole().redirect();
		};
		
	}
	
	public static OutputConsole getConsole() {
		return OutputConsole.oc;
	}
	
	public void attach(JPanel p, String type) {
		p.add(this,type);
	}
	
	public void attach(Container c) {
		if(c instanceof JFrame) {
			((JFrame) c).setContentPane(this);
		}else {
			c.add(this);
		}
	}
	
	public void clear() {
		this.textArea.setText("");
	}
	
	public void redirect() {
		Console.Log("Redirecting Output to console");
		System.setOut(new PrintStream(new AreaStream(textArea)));
		System.setErr(new PrintStream(new AreaStream(textArea)));
	}
	
	public void destroy() {
		System.setOut(new PrintStream(this.stdout));
		System.setErr(new PrintStream(this.stderr));
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}
	
	public static void Spawn() {
	
		JFrame frame = new JFrame("Output Console");
		frame.setSize(640,400);
		frame.setContentPane(oc);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			/*@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}*/

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				oc.destroy();
			}
		});
		frame.setVisible(true);
	}
	
	public static class AreaStream extends OutputStream{

		JTextArea area;
		
		public AreaStream(JTextArea a) {
			this.area = a;
		}
		
		@Override
		public void write(int b) throws IOException {
			// TODO Auto-generated method stub
	        area.append(String.valueOf((char)b));
	        area.setCaretPosition(area.getDocument().getLength());
		}
		
	}
}
