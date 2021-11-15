package hakfe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//
public class MenuChoice extends JFrame{
	JFrame menuChoice;
	JButton logout, changeProfile, changeFont; // 프로필 사진 변경, 폰트변경;
	public MenuChoice() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuChoice = new JFrame("메뉴선택");
		menuChoice.setLayout(new FlowLayout());
		
		logout = new JButton("로그아웃");
		changeProfile = new JButton("프로필 변경");
		changeFont = new JButton("폰트 변경");
		
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
		// 로그아웃 버튼 액션 리스너 클래스
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	class changeProfileButtonListener implements ActionListener { 
		// 프로필 변경 버튼 액션 리스너 클래스
		public void actionPerformed(ActionEvent e) {
			ChangeProfile cp = new ChangeProfile();
			menuChoice.dispose();
		}
	}
	class changeFontButtonListener implements ActionListener { 
		// 폰트 변경 버튼 액션 리스너 클래스
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
