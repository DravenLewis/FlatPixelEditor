package io.infinitestrike.utils;

import io.infinitestrike.flatpixel.logging.Console;

public class FPStringUtils{
	public static int toInt(String s) {
		try {
			return Integer.parseInt(s);
		}catch(Exception e) {
			Console.Error("Cant Convert String", e);
		}
		return 0;
	}
	public static byte toByte(String s) {
		try {
			return Byte.parseByte(s);
		}catch(Exception e) {
			Console.Error("Cant Convert String", e);
		}
		return 0;
	}
	public static long toLong(String s) {
		try {
			return Long.parseLong(s);
		}catch(Exception e) {
			Console.Error("Cant Convert String", e);
		}
		return 0;
	}
	public static short toShort(String s) {
		try {
			return Short.parseShort(s);
		}catch(Exception e) {
			Console.Error("Cant Convert String", e);
		}
		return 0;
	}
	public static float toFloat(String s) {
		try {
			return Float.parseFloat(s);
		}catch(Exception e) {
			Console.Error("Cant Convert String", e);
		}
		return 0;
	}
	public static double toDouble(String s) {
		try {
			return Double.parseDouble(s);
		}catch(Exception e) {
			Console.Error("Cant Convert String", e);
		}
		return 0;
	}
	public static int toHex(String s) {
		try {
			return Integer.parseInt(s,16);
		}catch(Exception e) {
			Console.Error("Cant Convert String", e);
		}
		return 0;
	}
	public static int toBinary(String s) {
		try {
			return Integer.parseInt(s,2);
		}catch(Exception e) {
			Console.Error("Cant Convert String", e);
		}
		return 0;
	}
}
