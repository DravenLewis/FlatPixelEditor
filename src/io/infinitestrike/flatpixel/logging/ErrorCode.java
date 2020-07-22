package io.infinitestrike.flatpixel.logging;

public class ErrorCode {
	
	public static final ErrorCode EXIT_OK = new ErrorCode("EXIT_OK",0);
	public static final ErrorCode EXIT_FAIL_GENERAL = new ErrorCode("EXIT_FAIL_GENERAL",1);
	public static final ErrorCode EXIT_FATAL_ERROR = new ErrorCode("EXIT_FATAL_ERROR",0xF00001);
	
	
	public final String name;
	public final int code;
	
	private ErrorCode(String name, int code) {
		this.name = name;
		this.code = code;
	}
}
