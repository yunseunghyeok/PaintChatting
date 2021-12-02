package hakfe;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.*;
import javax.swing.event.*;

public class LoginFrame extends JFrame {
	JFrame loginFrame;
	JLabel idLabel, passWordLabel,dbCheckLabel;
	JTextField nameField, passWordField;
	JButton submit, cancle, signUp;
	public LoginFrame() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		loginFrame = new JFrame("로그인");
		loginFrame.setLocation(250, 100);
		loginFrame.setSize(350, 300);
		loginFrame.setVisible(true);
		loginFrame.setLayout(null);

		new addLabel();
		new addTextField();
		new addButton();
	}
	class addLabel {
		private addLabel() {
			idLabel = new JLabel("아이디 : ");
			passWordLabel = new JLabel("비밀번호 : ");
			dbCheckLabel = new JLabel("로그인 여부 체크용 라벨");
			
			loginFrame.add(idLabel);
			idLabel.setBounds(20, 20, 90, 30);

			loginFrame.add(passWordLabel);
			passWordLabel.setBounds(20, 90, 90, 30);
			
			loginFrame.add(dbCheckLabel);
			dbCheckLabel.setBounds(80, 120, 150, 30);
		}
	}
	class addTextField {
		private addTextField() {
			nameField = new JTextField();
			passWordField = new JTextField();

			loginFrame.add(nameField);
			nameField.setBounds(80, 20, 150, 30);

			loginFrame.add(passWordField);
			passWordField.setBounds(80, 90, 150, 30);
		}
	}
	class addButton {
		private addButton() {
			submit = new JButton("확인");
			cancle = new JButton("취소");
			signUp = new JButton("회원가입");

			submit.addActionListener(new loginSubmitButtonListener());
			cancle.addActionListener(new loginCanleButtonListener());
			signUp.addActionListener(new loginSignUpButtonListener());
			
			loginFrame.add(submit);
			submit.setBounds(80, 180, 70, 30);

			loginFrame.add(cancle);
			cancle.setBounds(170, 180, 70, 30);
			
			loginFrame.add(signUp);
			signUp.setBounds(240, 20, 90, 30);
			
		}
	}
	class loginSubmitButtonListener implements ActionListener { // 확인버튼 액션 리스너 클래스
		public void actionPerformed(ActionEvent e) {
			/*
			 * db와 비교하는 코드 ~~~~~
			 * dbCheckLabel 값도 바꿔줘야함.
			 * dbCheckLabel 용도 -> db에 존재하지 않는 값을 입력할 경우
			 * 사용자에게 존재하지 않는 입력값이라고 표시해주기 위함임.
			 * 아이디와 비밀번호 구분 하지 않음. 
			 */
			loginFrame.dispose();
		}
	}
	class loginCanleButtonListener implements ActionListener { 
		// 취소 버튼 액션 리스너 클래스
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	class loginSignUpButtonListener implements ActionListener { 
		// 회원가입 버튼 액션 리스너 클래스
		public void actionPerformed(ActionEvent e) {
			new SignUpFrame();
		}
	}
	public static void main(String[] args) {
		new LoginFrame();
	}
}