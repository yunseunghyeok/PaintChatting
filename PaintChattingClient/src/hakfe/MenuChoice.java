package hakfe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//
public class MenuChoice extends JFrame{
	JFrame menuChoice;
	JButton logout, changeProfile, changeFont; // ������ ���� ����, ��Ʈ����;
	public MenuChoice() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuChoice = new JFrame("�޴�����");
		menuChoice.setLayout(new FlowLayout());
		
		logout = new JButton("�α׾ƿ�");
		changeProfile = new JButton("������ ����");
		changeFont = new JButton("��Ʈ ����");
		
		menuChoice.add(logout);
		menuChoice.add(changeProfile);
		menuChoice.add(changeFont);
		
		logout.addActionListener(new logoutButtonListener());
		changeProfile.addActionListener(new changeProfileButtonListener());
		changeFont.addActionListener(new changeFontButtonListener());
		
		menuChoice.setSize(400,100);
		menuChoice.setVisible(true);
	}
	class logoutButtonListener implements ActionListener { 
		// �α׾ƿ� ��ư �׼� ������ Ŭ����
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	class changeProfileButtonListener implements ActionListener { 
		// ������ ���� ��ư �׼� ������ Ŭ����
		public void actionPerformed(ActionEvent e) {
			ChangeProfile cp = new ChangeProfile();
			menuChoice.dispose();
		}
	}
	class changeFontButtonListener implements ActionListener { 
		// ��Ʈ ���� ��ư �׼� ������ Ŭ����
		public void actionPerformed(ActionEvent e) {
			ChangeFont cf = new ChangeFont();
			menuChoice.dispose();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MenuChoice();
	}
}
