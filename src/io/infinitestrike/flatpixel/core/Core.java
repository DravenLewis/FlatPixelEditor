package io.infinitestrike.flatpixel.core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import io.infinitestrike.flatpixel.MapEditor;
import io.infinitestrike.flatpixel.core.Vector.Vector2i;
import io.infinitestrike.flatpixel.logging.Console;

public class Core {

	public static JFrame spawnFrame(JPanel panel, String name, Vector2i size, Component parentComponent, boolean resize, boolean show, Image icon) {
		JFrame frame = new JFrame(name);
		frame.setSize(size.x,size.y);
		frame.setContentPane(panel);
		frame.setResizable(resize);
		frame.setLocationRelativeTo(parentComponent);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		if(icon != null) {
			frame.setIconImage(icon);
		}
		if(show) frame.setVisible(true);
		
		return frame;
	}
	
	public static JFrame spawnFrame(JPanel panel, String name, Vector2i size, Component parentComponent, boolean resize, boolean show) {
		return spawnFrame(panel,name,size,parentComponent,resize,show,null);
	}
	
	public static JFrame spawnFrame(JPanel panel, String name, Vector2i size, Component parentComponent, boolean resize) {
		return spawnFrame(panel,name,size,parentComponent, resize, true);
	}
	
	public static JFrame spawnFrame(JPanel panel, String name, Vector2i size, Component parentComponent) {
		return spawnFrame(panel,name,size,parentComponent, false);
	}
	
	public static void closeFrame(JFrame frame) {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		frame = null;
	}
	
	public static <T> void ArrayListCopy(ArrayList<T> source, ArrayList<T> target){
		int how = 0;
		for(T t : source) {
			target.add(t);
			how++;
		}
		
		if(MapEditor.isDebug()) {
			Console.Log("[Core::ArrayListCopy] Source: " + source.size() + " Target: " + target.size());
			Console.Log("[Core::ArrayListCopy] Copied " + how + " entries from source to target.");
		}
	}
	
	public static <K,V> void HashMapCopy(HashMap<K,V> from, HashMap<K,V> to, boolean clearTarget){
		if(clearTarget) {
			Console.Log("[Core::HashMapCopy] Clearing Target");
			to.clear();
		};
		
		Object[] keys = from.keySet().toArray();
		Object[] vals = from.values().toArray();
		Console.Log("[Core::HashMapCopy] Read Recorde: Keys: " + keys.length + " Values: " + vals.length);
		if(keys.length == vals.length) {
			for(int i = 0; i < keys.length; i++) {
				try {
					to.put((K) keys[i],(V) vals[i]);
					Console.Log("[Core::HashMapCopy] Copied " + (i + 1) + " of " + keys.length);
				}catch(Exception e) {
					Console.Error("Cannot Convert Object to Generic Type.", e);
				}
			}
		}else {
			Console.Log("[Core::HashMapCopy] Copy Failed, Key and Value sets are not the same size");
		}
	}
	
	public static <K,V> void printHashMap(HashMap<K,V> m) {
		Object[] keys = m.keySet().toArray();
		Object[] vals = m.values().toArray();
		if(keys.length == vals.length) {
			for(int i = 0; i < keys.length; i++) {
				String out = "";
				out += "[Key {ClassName:'"+((K) keys[i]).getClass().getName()+"'}] : " + keys[i] + " = ";
				out += "[Val {ClassName:'"+((K) vals[i]).getClass().getName()+"'}] : " + vals[i];
				Console.Log(out);
			}
		}
	}
	
	public static String getFileSize(File f) {
		long fileSize = f.length();
		long asKiloBytes = fileSize / 1024;
		long asMegaBytes = asKiloBytes / 1024;
		long asGigaBytes = asMegaBytes / 1024;
		
		if(asGigaBytes > 1) return asGigaBytes + "GB";
		if(asMegaBytes > 1) return asMegaBytes + "MB";
		if(asKiloBytes > 1) return asKiloBytes + "KB";
		
		return fileSize + " Bytes";
	}
	
	public static File ensureDirectoryExists(File f) {
		if(f.isDirectory()) {
			if(!f.exists()) {
				if(f.mkdirs()) {
					Console.Log("[Core::EnsureExists] Directory Created.");
					return f;
				}else {
					Console.Log("[Core::EnsureExists] Cannot Make Directory.");
					return null;
				}
			}else {
				Console.Log("[Core::EnsureExists] File exists, return.");
				return f;
			}
		}
		
		Console.Log("[Core::EnsureExists] File is a directory.");
		return null;
	}
	
	public static File ensureDirectoryExists(String directory) {
		return Core.ensureDirectoryExists(new File(directory));
	}
	
	public static int get2DXFromIndex(int id, int width) {
		return id % width;
	}
	
	public static int get2DYFromIndex(int id, int width) {
		return id / width;
	}
}
