import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ClientGUI extends JFrame
{
	static int windowWidth = 500, windowHeight = 400;
	
	private JButton buttonPlayVideo, buttonPlayPause, buttonJumpForward, buttonJumpBackward, buttonStop,
	                buttonVolumeUp, buttonVolumeDown, buttonStartCast, buttonStartOnDemand;
	private JPanel panelVideoController, panelCastController, panelOnDemandController;
	private JLabel labelVideoPath;
	private String statusPlayPause = "pause";
	private JTextField onDemandUrl;
	final JFileChooser fc = new JFileChooser();
	
	MouseAdapter buttonPlayVideoAdapter = new MouseAdapter()
	{
		public void mouseClicked(MouseEvent e)
		{
			int returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				System.out.println(file.getAbsolutePath());
				labelVideoPath.setText(file.getAbsolutePath());
				labelVideoPath.repaint();
				Controller.startVideo(file);
			}
		}
	};
	
	MouseAdapter buttonPlayPauseAdapter = new MouseAdapter()
	{
		public void mouseClicked(MouseEvent e)
		{
			Controller.sendCommand("p");
			if (statusPlayPause.equals("pause"))
			{
				statusPlayPause = "play";
				buttonPlayPause.setIcon(new ImageIcon("resources/play.gif"));
				buttonPlayPause.repaint();
			}
			else
			if (statusPlayPause.equals("play"))
			{
				statusPlayPause = "pause";
				buttonPlayPause.setIcon(new ImageIcon("resources/pause.gif"));
				buttonPlayPause.repaint();
			}
		}
	};
	
	public ClientGUI()
	{
		super("KaLeidoScope - Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(windowWidth+40, windowHeight+70);
		setLayout(new FlowLayout());

		panelVideoController = new JPanel();
		panelVideoController.setSize(windowWidth, windowHeight/3);
		panelVideoController.setPreferredSize(new Dimension(windowWidth, windowHeight/3));
		panelVideoController.setBorder(BorderFactory.createCompoundBorder(
		                               BorderFactory.createTitledBorder("Video Controller"),
		                               BorderFactory.createEmptyBorder(5,5,5,5)));
		panelVideoController.setLayout(new BoxLayout(panelVideoController, BoxLayout.PAGE_AXIS));
		add(panelVideoController);
		
		buttonPlayVideo = new JButton("Choose & play video");
		buttonPlayVideo.addMouseListener(buttonPlayVideoAdapter);
		panelVideoController.add(buttonPlayVideo);
		labelVideoPath = new JLabel("No video selected.");
		panelVideoController.add(labelVideoPath);
		
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout(FlowLayout.LEADING));
		tmp.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelVideoController.add(tmp);
		buttonJumpBackward = new JButton(new ImageIcon("resources/jumpBackward.gif"));
		buttonJumpBackward.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				Controller.sendCommand(""+(char)39);
			}
		});
		tmp.add(buttonJumpBackward);
		buttonPlayPause = new JButton(new ImageIcon("resources/pause.gif"));
		buttonPlayPause.addMouseListener(buttonPlayPauseAdapter);
		tmp.add(buttonPlayPause);
		buttonJumpForward = new JButton(new ImageIcon("resources/jumpForward.gif"));
		buttonJumpForward.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				Controller.sendCommand(""+(char)37);
			}
		});
		tmp.add(buttonJumpForward);
		buttonStop = new JButton(new ImageIcon("resources/stop.gif"));
		buttonStop.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				Controller.sendCommand("q");
				labelVideoPath.setText("No video selected.");
				labelVideoPath.repaint();
			}
		});
		tmp.add(buttonStop);
		buttonVolumeUp = new JButton("+", new ImageIcon("resources/volume.gif"));
		buttonVolumeUp.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				Controller.sendCommand("+");
			}
		});
		tmp.add(buttonVolumeUp);
		buttonVolumeDown = new JButton("-", new ImageIcon("resources/volume.gif"));
		buttonVolumeDown.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				Controller.sendCommand("-");
			}
		});
		tmp.add(buttonVolumeDown);
		
		panelCastController = new JPanel();
		panelCastController.setSize(windowWidth, windowHeight/3);
		panelCastController.setPreferredSize(new Dimension(windowWidth, windowHeight/3));
		panelCastController.setBorder(BorderFactory.createCompoundBorder(
		                              BorderFactory.createTitledBorder("Cast Controller"),
		                              BorderFactory.createEmptyBorder(5,5,5,5)));
		add(panelCastController);
		
		
		buttonStartCast = new JButton("Start Cast Screen");
		buttonStartCast.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				Controller.startCastScreen();
			}
		});
		panelCastController.add(buttonStartCast);
		
		panelOnDemandController = new JPanel();
		panelOnDemandController.setSize(windowWidth, windowHeight/3);
		panelOnDemandController.setPreferredSize(new Dimension(windowWidth, windowHeight/3));
		panelOnDemandController.setBorder(BorderFactory.createCompoundBorder(
		                                  BorderFactory.createTitledBorder("On-Demand Controller"),
		                                  BorderFactory.createEmptyBorder(5,5,5,5)));
		panelOnDemandController.setLayout(new BoxLayout(panelOnDemandController, BoxLayout.PAGE_AXIS));
		add(panelOnDemandController);
		onDemandUrl = new JTextField();
		onDemandUrl.setMaximumSize(new Dimension(windowWidth, 25));
		
		onDemandUrl.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelOnDemandController.add(onDemandUrl);
		buttonStartOnDemand = new JButton("Play On-Demand Video");
		buttonStartOnDemand.setAlignmentX(Component.LEFT_ALIGNMENT);
		buttonStartOnDemand.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				Controller.startHTTPOnDemand(onDemandUrl.getText());
			}
		});
		panelOnDemandController.add(buttonStartOnDemand);
		
		setVisible(true);
	}
}
