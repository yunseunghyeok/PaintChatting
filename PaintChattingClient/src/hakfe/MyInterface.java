//12 . 01 . Wed 수정
//hakfe. . .
//C:\PaintChatting\images\
package hakfe;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import javax.swing.event.*;


public class MyInterface extends JFrame {
	
	String UserID, UserNick, Font, IgmFileName, Text;
	

	Container c;
	JFrame mainFrame;
	JPanel ChattingRoom;
	JPanel ChattingRoomName;
	JPanel UserList;
	JPanel ChattingDisplayByAnother;
	JPanel ChattingDisplayByMe;
	JPanel ChattingSend;
	JPanel Canvas;
	JPanel SelectingColor;
	JPanel Menu;

	JLabel RoomName;
	JLabel currentColorLabel = new JLabel();
	JButton menubutton;
	JButton addChattingRoomButton = new JButton("방 추가");
	JButton sendButton;
	JButton openColorChoiceWindowButton;
	JButton baseColorChoice[] = { new JButton(), new JButton(), new JButton(), new JButton(), new JButton(),
			new JButton(), new JButton() };
	JButton clearCanvasButton;
	JButton sendImageButton;
	JTextArea ChattingSendArea;

	JScrollPane roomScroll;
	JScrollPane scrollPane, scrollPane2;
	ImageIcon menuImage = new ImageIcon("img/메뉴.png");
	ImageIcon changeColorImage = new ImageIcon("img/색 선택.png");
	ImageIcon sendImage = new ImageIcon("img/전송.png");
	
	int sizeX;
	int sizeY;
	int imgCount = 1;
	int oldX, oldY;
	int curX, curY;
	Vector<JButton> addChattingRoomButtonVec = new Vector<JButton>();
	Vector<point> tmp = new Vector<point>();
	Vector<Vector<point>> list = new Vector<Vector<point>>();

	int a[], b[];
	public int count = 1;
	public int displayCntByMe = 0;
	Color currentColor = Color.RED;
	Color baseColor[] = { Color.red, Color.orange, Color.yellow, Color.green, Color.blue, new Color(0, 0, 128),
			new Color(139, 0, 255) };
	GridBagLayout grid = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();

	Graphics gp = null;

	JColorChooser colorChooser;
	JSlider settingPenThickNess;
	int penThickNess = 0;

	public MyInterface() {
		
		mainFrame = new JFrame("PaintChatting");

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		c = mainFrame.getContentPane();
		mainFrame.setVisible(true);
		c.setLayout(null);
		gbc.fill = GridBagConstraints.NONE;

		sizeX = 1550; // 크기 고정
		sizeY = 839;

		mainFrame.setSize(sizeX, sizeY);
		Menu = new JPanel();
		Menu.setBorder(new TitledBorder(new LineBorder(new Color(209, 209, 209))));
		Menu.setLayout(null);
		Menu.setSize(100, 90);
		Menu.setLocation(0, 0);
		c.add(Menu);
		Menu.setBackground(new Color(107, 107, 107));
		Menu.setVisible(true);

		menubutton = new JButton(menuImage);
		Menu.add(menubutton);
		menubutton.setBounds(5, 25, 90, 30);
		menubutton.setVisible(true);
		menubutton.addActionListener(new MenuButtonListener());

		menubutton.setBorderPainted(false);
		menubutton.setFocusPainted(false);
		menubutton.setContentAreaFilled(false);

		ChattingRoom = new JPanel();
		ChattingRoom.setBorder(new TitledBorder(new LineBorder(new Color(209, 209, 209))));
		ChattingRoom.setLayout(grid);
		ChattingRoom.setBackground(new Color(107, 107, 107));
		ChattingRoom.setSize(100, 710);
		ChattingRoom.setLocation(0, 90);
		c.add(ChattingRoom);
		ChattingRoom.setVisible(true);

		roomScroll = new JScrollPane(ChattingRoom);
		roomScroll.setBounds(0, 90, 100, 710);
		c.add(roomScroll);

		gbcForm(addChattingRoomButton, 0, 0, 1, 1);
		addChattingRoomButton.addActionListener(new AddChattingRoom());
		addChattingRoomButton.setVisible(true);

		ChattingRoomName = new JPanel();
		ChattingRoomName.setBorder(new TitledBorder(new LineBorder(new Color(209, 209, 209))));
		ChattingRoomName.setLayout(null);
		ChattingRoomName.setSize(200, 70);
		ChattingRoomName.setLocation(100, 0);
		c.add(ChattingRoomName);
		ChattingRoomName.setBackground(new Color(107, 107, 107));
		ChattingRoomName.setVisible(true);

		String roomname = ""; // db에서 가져와야 함
		RoomName = new JLabel("방 이름이 여기 나옵니다.");
		ChattingRoomName.add(RoomName);
		RoomName.setBounds(35, 20, 200, 30);
		RoomName.setVisible(true);

		UserList = new JPanel();
		UserList.setBorder(new TitledBorder(new LineBorder(new Color(209, 209, 209))));
		UserList.setLayout(new FlowLayout());
		UserList.setSize(200, 730);
		UserList.setLocation(100, 70);
		c.add(UserList);
		UserList.setBackground(new Color(107, 107, 107));
		UserList.setVisible(true);

		/*
		 * db 유저 목록 출력해야함.
		 * 
		 */

		new ChattingDisplayPanelByMe();
		new ChattingDisplayPanelByAnother();

		ChattingSend = new JPanel();
		ChattingSend.setBorder(new TitledBorder(new LineBorder(new Color(209, 209, 209))));
		ChattingSend.setLayout(null);
		ChattingSend.setSize(sizeX - 950, 200);
		ChattingSend.setLocation(300, sizeY - 200);
		c.add(ChattingSend);
		ChattingSend.setBackground(new Color(107, 107, 107));
		ChattingSend.setVisible(true);

		ChattingSendArea = new JTextArea(5, 20);
		ChattingSendArea.setSize(sizeX - 1100, 100);
		ChattingSendArea.setLocation(20, 20);
		ChattingSend.add(ChattingSendArea);
		ChattingSendArea.setVisible(true);

		sendButton = new JButton(sendImage);
		ChattingSend.add(sendButton);
		sendButton.setBounds(480, 50, 90, 30);
		sendButton.setVisible(true);
		sendButton.addActionListener(new SendButtonListener());

		sendButton.setBorderPainted(false);
		sendButton.setFocusPainted(false);
		sendButton.setContentAreaFilled(false);

		new MyCanvas();
		new SelectingColorPanel();

		Menu.add(menubutton);
		menubutton.setBounds(5, 25, 90, 30);
		menubutton.setVisible(true);
	}

	public void gbcForm(Component c, int x, int y, int w, int h) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		ChattingRoom.add(c, gbc);
	}

	class AddChattingRoom implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton bt = new JButton(count + "번방");
			addChattingRoomButtonVec.add(bt);
			gbcForm(bt, 0, count, 1, 1);
			roomScroll.updateUI();
			bt.setVisible(true);
			count++;
		}
	}

	class MenuButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			new MenuChoice();
		}
	}

	class ChattingDisplayPanelByAnother extends JPanel {
		public ChattingDisplayPanelByAnother() {
			ChattingDisplayByAnother = new JPanel();
			ChattingDisplayByAnother.setBorder(new TitledBorder(new LineBorder(new Color(209, 209, 209))));
			ChattingDisplayByAnother.setLayout(new GridBagLayout());
			new JScrollPane(ChattingDisplayByAnother);
			ChattingDisplayByAnother.setSize(sizeX - 1250, sizeY - 200);
			ChattingDisplayByAnother.setLocation(300, 0);
			c.add(ChattingDisplayByAnother);
			ChattingDisplayByAnother.setBackground(new Color(130, 130, 130));
			
			scrollPane2 = new JScrollPane(ChattingDisplayByAnother, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane2.setBounds(600, 0, sizeX - 1250, sizeY - 200);
			c.add(scrollPane2);
			
			
		}
	}

	class ChattingDisplayPanelByMe extends JPanel {
		public ChattingDisplayPanelByMe() {
			ChattingDisplayByMe = new JPanel();
			ChattingDisplayByMe.setBorder(new TitledBorder(new LineBorder(new Color(209, 209, 209))));
			ChattingDisplayByMe.setLayout(new GridBagLayout());
			ChattingDisplayByMe.setSize(sizeX - 1250, sizeY - 200);
			ChattingDisplayByMe.setLocation(600, 0);
			c.add(ChattingDisplayByMe);
			ChattingDisplayByMe.setBackground(new Color(130, 130, 130));

			scrollPane = new JScrollPane(ChattingDisplayByMe, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setBounds(600, 0, sizeX - 1250, sizeY - 200);
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
			c.add(scrollPane);
		}
	}

	class MyCanvas extends JPanel {
		public MyCanvas() {
			Canvas = new JPanel(null);
			Canvas.setBorder(new TitledBorder(new LineBorder(new Color(209, 209, 209))));
			c.add(Canvas);
			Canvas.setSize(650, 600);
			Canvas.setLocation(300 + sizeX - 950, 0);
			Canvas.setVisible(true);
			Canvas.setBackground(Color.WHITE);
			Canvas.addMouseListener(new CanvasMouseListener());
			Canvas.addMouseMotionListener(new CanvasMouseMotionListener());
			gp = Canvas.getGraphics();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
		}

		class CanvasMouseListener extends MouseAdapter {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				oldX = e.getX();
				oldY = e.getY();
				tmp.add(new point(oldX, oldY));
				gp.setColor(currentColor);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				list.add(tmp);
				tmp = new Vector<point>();
			}
		}
	}

	class CanvasMouseMotionListener implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
			curX = e.getX();
			curY = e.getY();
			tmp.add(new point(curX, curY));
			Graphics2D g2 = (Graphics2D) gp;
			g2.setStroke(new BasicStroke(penThickNess, BasicStroke.CAP_ROUND, 0));
			gp.setColor(currentColor);
			gp.drawLine(oldX, oldY, curX, curY);
			oldX = curX;
			oldY = curY;
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}
	}

	class point {
		int x, y;

		public point(int a, int b) {
			x = a;
			y = b;
		}
	}

	class SelectingColorPanel extends JPanel {
		public SelectingColorPanel() {
			SelectingColor = new JPanel();
			SelectingColor.setBorder(new TitledBorder(new LineBorder(new Color(209, 209, 209))));
			c.add(SelectingColor);
			SelectingColor.setLayout(null);
			SelectingColor.setSize(650, 500);
			SelectingColor.setLocation(300 + sizeX - 950, 600);
			SelectingColor.setBackground(new Color(107, 107, 107));
			SelectingColor.setVisible(true);

			collocateCurrentColorLabel();
			collocateColorChoiceButton();
			collocateBaseColorChoiceButton();
			penThickNess();

			clearCanvasButton = new JButton("캔버스 초기화");
			SelectingColor.add(clearCanvasButton);
			clearCanvasButton.setBounds(500, 20, 120, 30);
			clearCanvasButton.setVisible(true);

			clearCanvasButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Canvas.update(gp);
					tmp.clear();
					list.clear();
				}
			});
			sendImageButton = new JButton("그림 전송");
			SelectingColor.add(sendImageButton);
			sendImageButton.setBounds(370, 20, 100, 30);
			sendImageButton.setVisible(true);
			sendImageButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ImageIcon Updateicon = null;
					// BufferedImage
					Rectangle screenRect = new Rectangle(Canvas.getX(), Canvas.getY() + 20, Canvas.getWidth() - 30,
							Canvas.getHeight());
					BufferedImage image = null;
					try {
						image = new Robot().createScreenCapture(screenRect);
					} catch (AWTException e1) {
						e1.printStackTrace();
					}
					try {
						ImageIO.write(image, "png", new File("C:/javaPanelToImage/image" + imgCount + ".png"));
						ImageIcon icon = new ImageIcon("C:/javaPanelToImage/image" + imgCount + ".png");
						Image updateImg = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
						Updateicon = new ImageIcon(updateImg);

					} catch (Exception ex) {
						ex.printStackTrace();
					}

					JLabel gpLabel = new JLabel(Updateicon);
					gpLabel.setPreferredSize(new Dimension(100, 100));
					JLabel st = new JLabel();
					gbcFormByMe(gpLabel, 0, displayCntByMe, 1, 1);
					displayCntByMe++;
					st.setPreferredSize(new Dimension(20, 20));
					gbcFormByMe(st, 0, displayCntByMe, 1, 1);
					displayCntByMe++;
					scrollPane.updateUI();

					imgCount++;
				}
			});
		}
		public void collocateCurrentColorLabel() {
			SelectingColor.add(currentColorLabel);
			currentColorLabel.setOpaque(true);
			currentColorLabel.setBounds(5, 25, 90, 90);
			currentColorLabel.setVisible(true);
			currentColorLabel.setBackground(currentColor);
		}

		public void collocateColorChoiceButton() {
			openColorChoiceWindowButton = new JButton(changeColorImage);
			SelectingColor.add(openColorChoiceWindowButton);
			openColorChoiceWindowButton.setBounds(100, 85, 90, 30);
			openColorChoiceWindowButton.setVisible(true);
			openColorChoiceWindowButton.addActionListener(new ColorChoiceListener());
			openColorChoiceWindowButton.setBorderPainted(false);
			openColorChoiceWindowButton.setFocusPainted(false);
			openColorChoiceWindowButton.setContentAreaFilled(false);
		}

		public void collocateBaseColorChoiceButton() {
			for (int i = 0; i < 7; i++) {
				baseColorChoice[i].addActionListener(new baseColorListener());
				SelectingColor.add(baseColorChoice[i]);
				baseColorChoice[i].setBounds(200 + 60 * i, 65, 50, 50);
				baseColorChoice[i].setBackground(baseColor[i]);
				baseColorChoice[i].setBorderPainted(false);
				baseColorChoice[i].setVisible(true);
			}
		}

		public void penThickNess() {
			settingPenThickNess = new JSlider(JSlider.HORIZONTAL, 0, 20, 0);
			SelectingColor.add(settingPenThickNess);
			settingPenThickNess.setMinorTickSpacing(1);
			settingPenThickNess.setMajorTickSpacing(5);

			settingPenThickNess.setPaintTicks(true);
			settingPenThickNess.setPaintLabels(true);

			settingPenThickNess.setBounds(80, 140, 500, 50);

			settingPenThickNess.setVisible(true);
			settingPenThickNess.addChangeListener(new settingPenThickNessListener());
		}

		class settingPenThickNessListener implements ChangeListener {
			public void stateChanged(ChangeEvent e) {
				penThickNess = settingPenThickNess.getValue();
			}
		}

		class ColorChoiceListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == openColorChoiceWindowButton) {
					colorChooser = new JColorChooser();
					Color selectedColor = colorChooser.showDialog(null, "색 선택", Color.YELLOW);
					if (selectedColor == null) {
						currentColorLabel.setBackground(currentColor);
					} else {
						currentColorLabel.setBackground(selectedColor);
						currentColor = selectedColor;
					}
				}
			}
		}

		class baseColorListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == baseColorChoice[0]) {
					currentColor = baseColor[0];
				} else if (e.getSource() == baseColorChoice[1]) {
					currentColor = baseColor[1];
				} else if (e.getSource() == baseColorChoice[2]) {
					currentColor = baseColor[2];
				} else if (e.getSource() == baseColorChoice[3]) {
					currentColor = baseColor[3];
				} else if (e.getSource() == baseColorChoice[4]) {
					currentColor = baseColor[4];
				} else if (e.getSource() == baseColorChoice[5]) {
					currentColor = baseColor[5];
				} else if (e.getSource() == baseColorChoice[6]) {
					currentColor = baseColor[6];
				}
				currentColorLabel.setBackground(currentColor);
			}
		}
	}

	class SendButtonListener implements ActionListener {
		String text1 = "<html>";
		String text2 = "</html>";
		String brTag = "<br />";
		public void actionPerformed(ActionEvent e) {
			JLabel displayLabel;
			String target = ChattingSendArea.getText();
			String temp = text1 + target + text2;

			temp = temp.replaceAll("\\n", brTag);

			//new ChatMessage()
			displayLabel = new JLabel(temp);
			displayLabel.setPreferredSize(new Dimension(250, 70));
			// displayLabel.setFont(new Font());
			gbcFormByMe(displayLabel, 0, displayCntByMe, 1, 1);
			displayCntByMe++;
			ChattingSendArea.setText("");
		}
	}

	public void gbcFormByMe(Component c, int x, int y, int w, int h) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		ChattingDisplayByMe.add(c, gbc);
	}

	public static void main(String[] args) {
		new MyInterface();
		new LoginFrame();
	}
}