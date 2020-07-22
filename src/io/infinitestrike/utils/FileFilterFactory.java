package io.infinitestrike.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;


public class FileFilterFactory {

	public static final FileFilter MAP_FILES =  getFileFilter(new String[] {".map"},"Map Files");
	public static final FileFilter IMAGE_FILES = getFileFilter(new String[] {".png",".jpg",".jpeg",".tiff",".tif",".gif"},"Image Files");
	
	public static FileFilter getFileFilter(String[] strings, String desc) {
		FileFilter f = new FileFilter() {
			
			int i = -1;
			
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				
				if(pathname.isDirectory()) return true;
				
				for (int i = 0; i < strings.length; i++) {
					if (pathname.getName().endsWith(strings[i])) {
						this.i = i;
						return true;
					}
				}
				return false;
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return desc;
			}
		};
		return f;
	}
}
