package org.sandbox;

import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.internal.Kernel32;

public class Console {
	
	private Console() { 
		super();
	}
	
	public static final Object LOCK = new Object();
	
	public static void setTitle(final String title) {
		Kernel32.SetConsoleTitle(title);
	}
	
	private static String INFO = "[INFO] ";
	private static String ERROR = "[ERROR] ";
	private static String SUCCESS = "[SUCCESS] ";
	private static String DEBUG = "[DEBUG] ";
	
	public static void print(String mess) {
		println(mess, Color.DEFAULT);
	}
	
	public static void info(String mess) {
		println(INFO + mess, Color.CYAN);
	}
	
	public static void error(String mess) {
		println(ERROR + mess, Color.RED);
	}
	
	public static void success(String mess) {
		println(SUCCESS + mess, Color.GREEN);
	}
	
	public static void debug(String mess) {
		if(Main.debug)
			println(DEBUG + mess, Color.YELLOW);
	}
	
	public static void println(String mess,  Color color) {
		synchronized(LOCK) {
			Color l_color = color;
			AnsiConsole.out.println("\033[" + l_color.fg() + "m" + mess + "\033[0m");
		}
	}
	
	public static void clear() { 
		synchronized(LOCK) {
			AnsiConsole.out.print("\033[H\033[2J");
		}
	}
}
