package io.infinitestrike.utils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class TextFieldUtils {

	public static final TextFieldListener WHOLE_NUMBER_LISTENER = new TextFieldListener("0123456789");
	public static final TextFieldListener NUMBER_LISTENER = new TextFieldListener("0123456789.");
	public static final TextFieldListener HEX_LISTENER = new TextFieldListener("0123456789abcdefABCDEF");
	
	public static class TextFieldListener implements KeyListener{

		private final char[] allowedChars;
		
		public TextFieldListener(char[] allowedChars) {
			this.allowedChars = allowedChars;
		}
		
		public TextFieldListener(String allowedChars) {
			this(allowedChars.toCharArray());
		}
		
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			JTextField f = (JTextField) e.getSource();
			if(!this.isInputUtilityKey(e.getKeyCode())) {
				if(!this.isCharAllowed(e.getKeyChar())) {
					f.setText(f.getText().replace(e.getKeyChar()+"",""));
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			JTextField f = (JTextField) e.getSource();
			if(!this.isInputUtilityKey(e.getKeyCode())) {
				if(!this.isCharAllowed(e.getKeyChar())) {
					f.setText(f.getText().replace(e.getKeyChar()+"",""));
				}
			}
			
		}
		
		private boolean isInputUtilityKey(int c) {
			switch(c) {
				case KeyEvent.VK_SHIFT:
				case KeyEvent.VK_ENTER:
				case KeyEvent.VK_CONTROL:
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_UP:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_SPACE:
				case KeyEvent.VK_ALT:
				case KeyEvent.VK_META:
				case KeyEvent.VK_BACK_SPACE:
				// KeyCode 0 NUL?
					return true;
				default:
					return false;
			}
		}
		
		private boolean isCharAllowed(char c) {
			boolean result = false;
			for(char ch : this.allowedChars) {
				if(ch == c) result = true;
			}
			return result;
		}
		
		public  void sterilizeString(JTextField f) {
			String source = f.getText();
			for(char s : f.getText().toCharArray()) {
				if(!isCharAllowed(s)) {
					source = source.replace(s+"","");
				}
			}
			f.setText(source);
		}
	}
}
