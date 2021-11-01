package hakfe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;

public class MyInterface extends JFrame {
	JPanel ChattingRoom;
	JPanel ChattingRoomName;
	JPanel UserList;
	JPanel ChattingDisplay;
	JPanel ChattingSend;
	JPanel Canvas;
	JPanel SelectingColor;
	JPanel Menu;
	
	JButton menubutton = new JButton("메뉴버튼");
	JButton addChattingRoomButton = new JButton("방 추가");
	JButton sendButton;
	
	JTextArea ChattingSendArea;

	JScrollPane roomScroll;
	JScrollPane chattingSendScroll;
	
	public int count = 1;

	GridBagLayout grid = new GridBagLayout();
	GridBagConstraints gbc = new GridBagConstraints();

	public MyInterface() {
		JFrame frame = new JFrame("PaintChatting");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		Container c = frame.getContentPane();
		frame.setVisible(true);
		c.setLayout(null);
		gbc.fill = GridBagConstraints.NONE;
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize(); // 화면 전체 크기 구하기

		int sizeX = res.width;
		int sizeY = res.height;

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
		
		ChattingSendArea = new JTextArea(5, 10);
		ChattingSendArea.setSize(sizeX - 1100, 100);
		ChattingSendArea.setLocation(310, sizeY - 180);
		ChattingSendArea.setVisible(true);
		ChattingSend.add(ChattingSendArea);
		c.add(ChattingSendArea);
		
		sendButton = new JButton("전송");
		ChattingSend.add(sendButton);
		sendButton.setBounds(480, 50, 90, 30);
		sendButton.setVisible(true);
		
		Menu.add(menubutton);
		menubutton.setBounds(5, 25, 90, 30);
		menubutton.setVisible(true);
		
		Canvas = new JPanel();
		Canvas.setLayout(null);
		Canvas.setSize(650, 600);
		Canvas.setLocation(300 + sizeX - 950, 0);
		Canvas.setBackground(Color.BLACK);
		Canvas.setVisible(true);

		Canvas.add(new MyCanvas());
		
		SelectingColor = new JPanel();
		SelectingColor.setLayout(null);
		SelectingColor.setSize(650, 500);
		SelectingColor.setLocation(300 + sizeX - 950, 600);
		c.add(SelectingColor);
		SelectingColor.setBackground(Color.pink);
		SelectingColor.setVisible(true);

	}
	class MyCanvas extends Canvas{
		
		public MyCanvas() {
			setBackground(Color.white);
		}

		public void paint(Graphics g) {
			Graphics2D g2;
			g2 = (Graphics2D) g;
			g2.setColor(Color.black);
		    g2.draw(new Line2D.Double(30, 30, 60, 100));
		}
	}
	class AddChattingRoom implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton bt = new JButton(count + "번방");
			gbcForm(bt, 0, count, 1, 1);
			roomScroll.updateUI();
			bt.setVisible(true);
			count++;
		}
	}
	public void gbcForm(Component c, int x, int y, int w, int h) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		ChattingRoom.add(c, gbc);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MyInterface();
	}

}
