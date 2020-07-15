package io.infintestrike.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import io.infinitestrike.core.Console;

public class TextFromFile{
	public static String getText(File f) throws FileNotFoundException{
		String out = "";
		String line = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(f));
			while((line = reader.readLine()) != null) {
				out += line + "\n";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Console.Error("Cannot Read File: " + f.getName(), e);
			throw new FileNotFoundException(e.getMessage());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Console.Error("Error While Closeing Stream", e);
			}
		}
		
		return out;
	}
	
	public static void makeFile(File f, byte[] contents) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(f));
			for(int i = 0; i < contents.length; i++) {
				writer.write((char) contents[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Console.Error("Cannot Write file.", e);
		} finally {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Console.Error("Cannot close file.", e);
			}
		}
	}
}
