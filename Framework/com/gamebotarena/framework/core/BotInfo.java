package com.gamebotarena.framework.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gamebotarena.framework.api.D;

public class BotInfo 
{
	private boolean paren = false;
	
	private Color color;
	private boolean kids = false;
	
	public void hide()
	{
		panel.removeAll();
		panel.setBorder(BorderFactory.createEmptyBorder());
	}
	
	public void kids()
	{
		kids = true;
		update();
		if(info != null)
		{
			info.getParent().remove(info);
		}
	}
	
	private void copy(BotInfo info)
	{
		id = info.id;
		name = info.name;
		version = info.version;
		author = info.author;
		country = info.country;
		className = info.className;
		wins = info.wins;
		played = info.played;
		draws = info.draws;
		bot = info.bot;
		size = info.size;
		aveMoveTime = info.aveMoveTime;
		totalMoves = info.totalMoves;
		rank = info.rank;
		rankDenom = info.rankDenom;
		date = info.date;
	}
	
	HashMap props;
	
	public String id;
	public String name;
	public String version;
	public String author;
	public String country;
	public String className;
	public double wins;
	public int played;
	public int draws;
	public int size;
	public long date;
	public int totalMoves;
	public double aveMoveTime;
	public int rank;
	public int rankDenom;
	public Object bot;
	
	private JLabel winField = new FixedLabel();
	private JLabel drawField = new FixedLabel();
	private JLabel rankField = new JLabel();
	
	private JButton info;
	
	private JLabel nameLabel = new JLabel();
	
	private JPanel panel;
	
	void initValues(String id)
	{
		setId(id);
		name = (String) props.get("name");
		version = (String) props.get("version");
		author = (String) props.get("author");
		country = (String) props.get("country");
		className = (String) props.get("class-name");
	}
	
	public void setColor(Color color)
	{
		this.color = color;
		nameLabel.setForeground(color);
	}
	
	public void setId(String id)
	{
		id = id.replace("\\", "/");
		while(id.endsWith("/"))
		{
			id = id.substring(0, id.length() - 1);
		}
		int idx = id.lastIndexOf("/");
		if(idx != -1)
		{
			id = id.substring(idx + 1);
		}
		this.id = id;		
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getName()
	{
		if(name == null)
		{
			return id;
		}
		return name;
	}
	
	public String getCountry()
	{
		if(country == null)
		{
			return "";
		}
		return country;
	}
	
	public String getAuthor()
	{
		if(author == null)
		{
			return "";
		}
		
		return author;
	}
	
	public String getVersion()
	{
		if(version == null)
		{
			return "";
		}
		
		return version;
	}
	
	public String getClassName()
	{
		return className;
	}

	public JPanel getBotInfoPanel(boolean full, final Color colorIn, final int playerNum)
	{
		color = colorIn;
		
		Font font = new Font("Arial", Font.PLAIN, 12);
		Font boldFont = new Font("Arial", Font.BOLD, 12);
		
		panel = new JPanel();
		
		if(!full)
		{
			GridLayout layout = new GridLayout(2, 1);
			layout.setVgap(0);
			layout.setHgap(0);
			panel.setLayout(layout);
			
			FlowLayout f = new FlowLayout();
			//f.setHgap(0);
			f.setVgap(0);
			JPanel top = new JPanel(f);
			nameLabel = new JLabel("Player " + playerNum + ":");
			nameLabel.setFont(boldFont);
			nameLabel.setForeground(color);
			JTextField nameField = new JTextField();
			nameField.setFocusable(false);
			nameField.setBackground(nameLabel.getBackground());
			nameField.setPreferredSize(new Dimension(123, nameField.getPreferredSize().height));
			nameField.setText(name);
			nameField.setFont(font);
			nameField.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 4));
			
			nameLabel.setBorder(BorderFactory.createEmptyBorder());
			
			top.add(nameLabel);
			top.add(nameField);

			f = new FlowLayout(FlowLayout.LEFT);
			//f.setHgap(12);
			f.setVgap(2);
			JPanel bottom = new JPanel(f);
			JLabel winLabel = new JLabel("Wins:");
			winLabel.setFont(font);
			winField.setFont(font);
			//winField.setForeground(color);
			bottom.add(winLabel);
			bottom.add(winField);
			winField.setBorder(BorderFactory.createEmptyBorder());
			winLabel.setBorder(BorderFactory.createEmptyBorder());
			
			JLabel l = new JLabel();
			l.setPreferredSize(new Dimension(3, 1));
			bottom.add(l);
			
			info = new JButton("details");
			//info.setMargin(new Insets(0, 5, 0, 0));
			info.setPreferredSize(new Dimension(info.getPreferredSize().width, 18));
			bottom.add(info);
			
			info.setFocusable(false);
			info.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) 
				{
					//todo: sync this and game...
					final JFrame f =  new JFrame();
					f.getContentPane().setLayout(new BorderLayout());
					
					BotInfo infoFull = new BotInfo();
					infoFull.copy(BotInfo.this);
				
					JPanel fullInfo = infoFull.getBotInfoPanel(true, color, playerNum);
					//f.getContentPane().setMinimumSize(new Dimension(300, 10));
					f.getContentPane().add(fullInfo, BorderLayout.CENTER);
					
					f.pack();
					f.addWindowListener(new WindowAdapter()
					{
						public void windowClosing(WindowEvent e) 
						{
							f.dispose();
						}
					});
					f.setTitle(name);
					f.setSize(220, 300);
					f.setLocation((int) (300 + (Math.random() * 50)), (int) (200 + (Math.random() * 50)));
					f.setVisible(true);
				}
			});
			
			bottom.setBorder(BorderFactory.createEmptyBorder());
			top.setBorder(BorderFactory.createEmptyBorder());
			
			//paren = true;
			update();
			
			panel.add(top);
			panel.add(bottom);
			
			//panel.add(winLabel);
			panel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEtchedBorder(),
					BorderFactory.createEmptyBorder(12, 0, 8, 0)));
		}
		else
		{
			panel.setLayout(new GridLayout(10, 1));
	
			JLabel nameLabel = new JLabel("Player " + playerNum);
			nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
			JTextField nameField = new JTextField();
			nameField.setFocusable(false);
			nameField.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 0));
			nameField.setText(name);
	
			JLabel versionLabel = new JLabel("Version");
			versionLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
			JTextField versionField = new JTextField();
			versionField.setFocusable(false);
			versionField.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 0));
			versionField.setText(version);
	
			JLabel authorLabel = new JLabel("Author");
			authorLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
			JTextField authorField = new JTextField();
			authorField.setFocusable(false);
			authorField.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 0));
			authorField.setText(author);
	
			JLabel countryLabel = new JLabel("Country");
			countryLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
			JTextField countryField = new JTextField();
			countryField.setFocusable(false);
			countryField.setBorder(BorderFactory.createEmptyBorder(0, 7, 5, 0));
			countryField.setText(country);
	
			JLabel winLabel = new JLabel("Wins:");
			JLabel rankLabel = new JLabel("Rank:");
			update();
	
			panel.add(nameLabel);
			panel.add(nameField);
			panel.add(versionLabel);
			panel.add(versionField);
			panel.add(authorLabel);
			panel.add(authorField);
			panel.add(countryLabel);
			panel.add(countryField);
			panel.add(winLabel);
			
			JPanel winRow = new JPanel();
			winRow.setBorder(BorderFactory.createEmptyBorder(4, 2, 0, 0));
			winRow.setLayout(new BorderLayout()); //FlowLayout(FlowLayout.LEFT));
			winRow.add(winLabel, BorderLayout.WEST);
			winRow.add(winField, BorderLayout.CENTER);
			winLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 2));
			winLabel.setFont(boldFont);
			winField.setFont(boldFont);

			JPanel rankRow = new JPanel();
			rankRow.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
			rankRow.setLayout(new BorderLayout()); //new FlowLayout(FlowLayout.LEFT));
			rankRow.add(rankLabel, BorderLayout.WEST);
			rankRow.add(rankField, BorderLayout.CENTER);
			rankLabel.setFont(boldFont);
			rankLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
			rankField.setFont(boldFont);
			panel.add(rankRow);
			panel.add(winRow);
			
			panel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createEmptyBorder(8, 8, 8, 8), 
							BorderFactory.createEtchedBorder()), 
					BorderFactory.createEmptyBorder(3, 3, 3, 3)));

			Component[] cs = panel.getComponents();
			for(int i=0; i < cs.length; i++)
			{
				Component c = cs[i];
				c.setFont(font);
			}
			
			nameLabel.setFont(boldFont);
			nameLabel.setForeground(color);

			versionLabel.setFont(versionLabel.getFont().deriveFont(Font.BOLD));	
			authorLabel.setFont(authorLabel.getFont().deriveFont(Font.BOLD));		
			countryLabel.setFont(countryLabel.getFont().deriveFont(Font.BOLD));				

			nameField.setBackground(nameLabel.getBackground());
			versionField.setBackground(versionLabel.getBackground());
			authorField.setBackground(authorLabel.getBackground());
			countryField.setBackground(countryLabel.getBackground());
		}
		return panel;
	}

	public void readXml(InputStream is, String id)
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    try
	    {
	    	DocumentBuilder builder = factory.newDocumentBuilder();
     	    Document document = builder.parse( is );
     	    readXml(document, id);
	    }
	    catch(Exception e)
	    {
	    	D.out(e);
	    	setId(id);
	    	//setId(e.getMessage());
	    }
	}

	public void readXml(String file)
	{
		File f = new File(file);
		D.out("Reading xml file: " + f.getName());
		String id = f.getName();
		
		if(id.toLowerCase().endsWith(".xml"))
		{
			id = id.substring(0, id.length() - 4);
		}
		
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    try
	    {
	    	DocumentBuilder builder = factory.newDocumentBuilder();
     	    Document document = builder.parse( file );
     	    readXml(document, id);
	    }
	    catch(Exception e)
	    {
	    	D.out(e);
	    }
	}

	private void readXml(Document document, String id) throws Exception
	{
		HashMap descriptor = new HashMap();

	    NodeList nodeList = document.getElementsByTagName("game-bot");
	    Node node = nodeList.item(0);

	    nodeList = node.getChildNodes();

	    for(int i=0; i < nodeList.getLength(); i++)
	    {
	    	node = nodeList.item(i);
	 	    if(!node.getNodeName().startsWith("#"))
	 	    {
	 	    	descriptor.put(node.getNodeName(), node.getFirstChild().getNodeValue());
	 	    }
	    }

	    props = descriptor;
	    initValues(id);
	}

	public void won()
	{
		wins++;
		played++;
		update();
	}
	
	public void lost()
	{
		played++;
		update();
	}
	
	public void draw()
	{
		draws++;
		played++;
		wins += 0.5;
		update();
	}
	
	public void update()
	{
		String winStr = "-";
		String playedStr = "-";
		String rankStr = "-";
		String rankDenomStr = "-";
		
		if(played > 0)
		{
			if(Math.floor(wins) == wins)
			{
				winStr = "" + (int) wins;
			}
			else
			{
				winStr = "" + wins;
			}
			
			playedStr = "" + played;
		}
		
		if(rankDenom > 0)
		{
			rankStr = "" + rank;
			rankDenomStr = "" + rankDenom;
		}
		
		if(paren)
		{
			winField.setText("(" + winStr + " / " + playedStr + ")");
			rankField.setText("(" + rankStr + " / " + rankDenomStr + ")");
			drawField.setText("(" + draws + ")");			
		}
		else
		{
			StringBuffer winsBuffer = new StringBuffer();
			winsBuffer.append(winStr);
			if(!kids)
			{
				winsBuffer.append(" / ").append(playedStr);
			}
			//for(int i=winsBuffer.length(); i < 20; i++)
			//{
			//	winsBuffer.append(" ");
			//}
			
			StringBuffer rankBuffer = new StringBuffer();
			rankBuffer.append(rankStr).append(" / ").append(rankDenomStr);
			
			//for(int i=rankBuffer.length(); i < 20; i++)
			//{
			//	rankBuffer.append(" ");
			//}

			winField.setText(winsBuffer.toString());
			rankField.setText(rankBuffer.toString());
			drawField.setText("" + draws);
		}
	}
	
	private static class FixedLabel extends JLabel
	{
		public FixedLabel()
		{
		}
		
		public Dimension getPreferredSize()
		{
			Dimension prefSize = super.getPreferredSize();
			return new Dimension(Math.max(prefSize.width, 55), prefSize.height);			
		}
	}
}
