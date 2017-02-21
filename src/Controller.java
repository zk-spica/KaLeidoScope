import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;


public class Controller
{
	static Socket socket = null;
	static PrintWriter output = null;
	static ftpServer ftp = null;
	static String currentService = null;
	
	static String serverAddress = "192.168.12.1";
	static int serverPort = 2333;
	
	private static void terminateService()
	{
		if (currentService == null) return;
		currentService = null;
		try
		{
			if (ftp != null)
			{
				ftp.interrupt();
				ftp.join();
			}
			//input.close();
			if (output != null) output.close();
			if (socket != null) socket.close();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	
	public static void startVideo(File videoFile)
	{
		terminateService();
		currentService = "video"; 
		String videoPath = videoFile.getParent();
		String videoName = videoFile.getName();
		ftp = new ftpServer(videoPath);
		ftp.start();
		
		
		
		try
		{
			Thread.sleep(5000);
			socket = new Socket(serverAddress, serverPort);
			output = new PrintWriter(socket.getOutputStream());
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		System.out.println("Ready to output videoName");
		output.println("FTP"+videoName);
		output.flush();
	}
	
	public static void sendCommand(String cmd)
	{
		if (output != null)
		{
			output.print(cmd);
			output.flush();
			
			if (cmd.equals("q"))
			{
				terminateService();
			}
		}
	}
	
	public static void startCastScreen()
	{
		terminateService();
		currentService = "screen"; 
		
		try
		{
			socket = new Socket(serverAddress, serverPort);
			output = new PrintWriter(socket.getOutputStream());
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		output.println("Screen");
		output.flush();
		
		String port = "4907";
		new InitConnection(Integer.parseInt(port), "lch1475369");
	}
	
	public static void startHTTPOnDemand(String url)
	{
		terminateService();
		currentService = "OnDemand";
		try
		{
			socket = new Socket(serverAddress, serverPort);
			output = new PrintWriter(socket.getOutputStream());
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		output.println("HTTP"+url);
		output.flush();		
	}
}
