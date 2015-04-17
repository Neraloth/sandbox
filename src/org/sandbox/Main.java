package org.sandbox;

import org.fusesource.jansi.AnsiConsole;
import org.sandbox.database.DatabaseHandler;
import org.sandbox.models.DAOFactory;
import org.sandbox.network.server.Server;

public class Main {

	public static boolean debug = true;
	
	public static boolean running = false;

	public static String ip = "127.0.0.1";
	public static int serverPort = 499;
	public static int gamePort = 5555;
	public static String clientVersion = "1.29.1";
	
	public static String database_host = "127.0.0.1";
	public static String database_user = "root";
	public static String database_password = "";
	public static String database_name = "sandbox";
	
	public static byte serverId = 2;
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				running = false;
				AnsiConsole.systemUninstall();
				DatabaseHandler.instance().close();
				Server.stop();
			}
		});
	}
	
	public static void main(String[] args) {
		running = true;
		long time = System.currentTimeMillis();
		AnsiConsole.systemInstall();
		Console.setTitle("Sandbox Dofus 1.29");
		Console.print(header("1.0.0-a UNSTABLE"));
		DatabaseHandler.init();
        DAOFactory.init();
		Server.start();
		Console.success("Sandbox loaded in "+ ((System.currentTimeMillis() - time) / 1000) + " seconds.\n");
	}

	public static String header(String version) {
		StringBuilder value = new StringBuilder();
		value.append("Sandbox Dofus 1.29 by Neraloth\nGithub project : http://github.com/Neraloth/sandbox\n");
		return value.toString();
	}
}
