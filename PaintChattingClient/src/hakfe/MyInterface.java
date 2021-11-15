//11.11 수정
package hakfe;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.*;
import javax.swing.event.*;

public class MyInterface extends JFrame {
	
	Container c;
	
	JPanel ChattingRoom;
	JPanel ChattingRoomName;
	JPanel UserList;
	JPanel ChattingDisplay;
	JPanel ChattingSend;
	JPanel Canvas;
	JPanel SelectingColor;
	JPanel Menu;
	
	JLabel currentColorLabel = new JLabel();
	
	JButton menubutton = new JButton("메뉴버튼");
	JButton addChattingRoomButton = new JButton("방 추가");
	JButton sendButton;
	JButton openColorChoiceWindowButton = new JButton("색 선택");
	JButton baseColorChoice[] = {new JButton(), new JButton(),new JButton(),
							new JButton(),new JButton(),new JButton(),new JButton()};
	
	JTextArea ChattingSendArea;

	JScrollPane roomScroll;
	
	int sizeX;
	int sizeY;
	
	int oldX, oldY;
	int curX, curY;
	Vector<JButton> addChattingRoomButtonVec = new Vector<JButton>();
	Vector<point> tmp = new Vector<point>();
	Vector<Vector<point>> list = new Vector<Vector<point>>();
	
	int a[], b[];
	public int count = 1;
	
	Color currentColor = Color.RED;
	Color baseColor[] = {Color.red, Color.orange, Color.yellow, Color.green,
						Color.blue, new Color(0,0,128), new Color(139,0,255)};
	GridBagLayout grid = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();
	
	Graphics gp;
	
	JColorChooser colorChooser;
	JSlider settingPenThickNess;
	int penThickNess;
	
	public MyInterface() {
		JFrame frame = new JFrame("PaintChatting");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		c = frame.getContentPane();
		frame.setVisible(true);
		c.setLayout(null);
		gbc.fill = GridBagConstraints.NONE;
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize(); // 화면 전체 크기 구하기

		sizeX = res.width;
		sizeY = res.height;
		
		Menu = new JPanel();
		Menu.setLayout(null);
		Menu.setSize(100, 90);
		Menu.setLocation(0, 0);
		c.add(Menu);
		Menu.setBackground(Color.red);
		Menu.setVisible(true);

		Menu.add(menubutton);
		menubutton.setBounds(5, 25, 90, 30);
		menubutton.setVisible(true);
		menubutton.addActionListener(new MenuButtonListener());

		ChattingRoom = new JPanel();
		ChattingRoom.setLayout(grid);
		ChattingRoom.setBackground(Color.orange);
		ChattingRoom.setSize(100, 710);
		ChattingRoom.setLocation(0, 90);
		c.add(ChattingRoom);
		ChattingRoom.setVisible(true);

		roomScroll = new JScrollPane(ChattingRoom);
		roomScroll.setBounds(0, 90, 100, 710);
		c.add(roomScroll);

		// ChattingRoom.add(addChattingRoomButton);
		gbcForm(addChattingRoomButton, 0, 0, 1, 1);
		addChattingRoomButton.addActionListener(new AddChattingRoom());
		// addChattingRoomButton.setSize(90, 30);
		addChattingRoomButton.setVisible(true);

		ChattingRoomName = new JPanel();
		ChattingRoomName.setLayout(null);
		ChattingRoomName.setSize(200, 70);
		ChattingRoomName.setLocation(100, 0);
		c.add(ChattingRoomName);
		ChattingRoomName.setBackground(Color.yellow);
		ChattingRoomName.setVisible(true);
		
		UserList = new JPanel();
		UserList.setLayout(null);
		UserList.setSize(200, 730);
		UserList.setLocation(100, 70);
		c.add(UserList);
		UserList.setBackground(Color.green);
		UserList.setVisible(true);

		ChattingDisplay = new JPanel();
		ChattingDisplay.setLayout(null);
		ChattingDisplay.setSize(sizeX - 950, sizeY - 200);
		ChattingDisplay.setLocation(300, 0);
		c.add(ChattingDisplay);
		ChattingDisplay.setBackground(Color.blue);
		ChattingDisplay.setVisible(true);
		
		ChattingSend = new JPanel();
		ChattingSend.setLayout(null);
		ChattingSend.setSize(sizeX - 950, 200);
		ChattingSend.setLocation(300, sizeY - 200);
		c.add(ChattingSend);
		ChattingSend.setBackground(Color.gray);
		ChattingSend.setVisible(true);
		
		ChattingSendArea = new JTextArea(5, 20);
		ChattingSendArea.setText("여기에 메시지를 입력하세요.");
		ChattingSendArea.setSize(sizeX - 1100, 100);
		ChattingSendArea.setLocation(20, 20);
		ChattingSend.add(ChattingSendArea);
		ChattingSendArea.setVisible(true);
		
		sendButton = new JButton("전송");
		ChattingSend.add(sendButton);
		sendButton.setBounds(480, 50, 90, 30);
		sendButton.setVisible(true);
		
		Menu.add(menubutton);
		menubutton.setBounds(5, 25, 90, 30);
		menubutton.setVisible(true);
		
		new MyCanvas();
		new SelectingColorPanel();
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
	class MenuButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			MenuChoice mc = new MenuChoice();
		}
	}
	class MyCanvas extends JPanel{
		public MyCanvas() {
			Canvas = new JPanel();
			c.add(Canvas);
			//Canvas.setLayout(null);
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
			g.setColor(Color.RED);
		}
		public void panelToImage() {
			
		}
	}
	class CanvasMouseListener extends MouseAdapter{
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
	class CanvasMouseMotionListener implements MouseMotionListener{
		@Override
		public void mouseDragged(MouseEvent e) {
			curX = e.getX();
			curY = e.getY();
			tmp.add(new point(curX, curY));
			Graphics2D g2 = (Graphics2D)gp;
			g2.setStroke(new BasicStroke(penThickNess,BasicStroke.CAP_ROUND,0));
			gp.setColor(currentColor);
			gp.drawLine(oldX, oldY, curX, curY);
			oldX = curX;
			oldY = curY;
		}
		@Override
		public void mouseMoved(MouseEvent e) {}
	}
	class point {
		int x, y;
		public point(int a, int b) {
			x = a;
			y = b;
		}
	}
	class SelectingColorPanel extends JPanel{
		public SelectingColorPanel() {
			SelectingColor = new JPanel();
			c.add(SelectingColor);
			SelectingColor.setLayout(null);
			SelectingColor.setSize(650, 500);
			SelectingColor.setLocation(300 + sizeX - 950, 600);
			SelectingColor.setBackground(Color.pink);
			SelectingColor.setVisible(true);
			
			collocateCurrentColorLabel();
			collocateColorChoiceButton();
			collocateBaseColorChoiceButton();
			penThickNess();
		}
		public void	collocateCurrentColorLabel () {
			SelectingColor.add(currentColorLabel);	
			currentColorLabel.setOpaque(true);
			currentColorLabel.setBounds(5, 25, 90, 90);
			currentColorLabel.setVisible(true);
			currentColorLabel.setBackground(currentColor);
		} 
		public void collocateColorChoiceButton() {
			SelectingColor.add(openColorChoiceWindowButton);
			openColorChoiceWindowButton.setBounds(100, 85, 90, 30);
			openColorChoiceWindowButton.setVisible(true);
			openColorChoiceWindowButton.addActionListener(new ColorChoiceListener());
		}
		public void collocateBaseColorChoiceButton() {
			for(int i = 0; i < 7; i++) {
				baseColorChoice[i].addActionListener(new baseColorListener());
				SelectingColor.add(baseColorChoice[i]);
				baseColorChoice[i].setBounds(200 + 60 * i, 65, 50, 50);
				baseColorChoice[i].setBackground(baseColor[i]);
				baseColorChoice[i].setBorderPainted(false);
				baseColorChoice[i].setVisible(true);
			}
		}
		public void penThickNess() {
			settingPenThickNess = new JSlider(JSlider.HORIZONTAL,0,20,0);
			SelectingColor.add(settingPenThickNess);
			settingPenThickNess.setMinorTickSpacing(1);
			settingPenThickNess.setMajorTickSpacing(5);;
			settingPenThickNess.setPaintTicks(true);
			settingPenThickNess.setPaintLabels(true);
			
			settingPenThickNess.setBounds(80, 140, 500, 50);;
			settingPenThickNess.setVisible(true);
			settingPenThickNess.addChangeListener(new settingPenThickNessListener());
			
		}
		class settingPenThickNessListener implements ChangeListener{
			public void stateChanged(ChangeEvent e) {
				penThickNess = settingPenThickNess.getValue();
			}
		}
		class ColorChoiceListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == openColorChoiceWindowButton) {
					colorChooser = new JColorChooser();
					Color selectedColor = colorChooser.showDialog(null, "색 선택", Color.YELLOW);
					if(selectedColor == null) {
						currentColorLabel.setBackground(currentColor);
					}
					else
					{
						currentColorLabel.setBackground(selectedColor);
						currentColor = selectedColor;
					}
				}
			}
		}
		class baseColorListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == baseColorChoice[0]) {
					currentColor = baseColor[0];
				}
				else if(e.getSource() == baseColorChoice[1]) {
					currentColor = baseColor[1];
				}
				else if(e.getSource() == baseColorChoice[2]) {
					currentColor = baseColor[2];
				}
				else if(e.getSource() == baseColorChoice[3]) {
					currentColor = baseColor[3];
				}
				else if(e.getSource() == baseColorChoice[4]) {
					currentColor = baseColor[4];
				}
				else if(e.getSource() == baseColorChoice[5]) {
					currentColor = baseColor[5];
				}
				else if(e.getSource() == baseColorChoice[6]) {
					currentColor = baseColor[6];
				}
				currentColorLabel.setBackground(currentColor);
			}
		}
	}
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MyInterface();
	}

}
