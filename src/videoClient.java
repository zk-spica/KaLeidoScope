import java.io.*;
import java.net.*;
import java.lang.Thread;
import org.apache.ftpserver.*;
import org.apache.ftpserver.usermanager.impl.*;

class ftpServer extends Thread
{
	private String videoPath;

	public ftpServer(String path)
	{
		videoPath = path;
	}
	public void run()
	{
		FtpServerFactory serverFactory = new FtpServerFactory();
		
		BaseUser user = new BaseUser();
		user.setName("anonymous");
		user.setHomeDirectory(videoPath);
		
		try
		{
			serverFactory.getUserManager().save(user);
			FtpServer server = serverFactory.createServer();
			System.out.println("Ready");
			server.start();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

public class videoClient
{
	public static void dumbRun() throws Exception
	{
		String videoPath = "F:\\Pku\\network\\final project\\";
		String videoName = "gee.avi";
		
		ftpServer ftp = new ftpServer(videoPath);
		ftp.start();
		
//		Thread.sleep(5000);
		
		String serverAddress = "192.168.12.1";
		int serverPort = 2333;
		
		Socket socket = new Socket(serverAddress, serverPort);
		PrintWriter output = new PrintWriter(socket.getOutputStream());
		System.out.println("Ready to output videoName");
		output.println(videoName);
		output.flush();
		
		System.out.println("Sleeping");
		Thread.sleep(10000);
		output.print("q");
		output.flush();
		System.out.println("Finished output up");

		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		while (true)
		{
			Thread.sleep(1000);
			String signal = input.readLine();
			System.out.println(signal);
			if (signal.equals("end"))
				break;
		}
		System.out.println("Finished");
		
		ftp.interrupt();
		ftp.join();
		System.out.println("Finished");
		
		input.close();
		output.close();
		socket.close();
		System.out.println("Finished");
	}
}