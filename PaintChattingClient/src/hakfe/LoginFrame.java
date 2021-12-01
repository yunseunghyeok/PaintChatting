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

		loginFrame = new JFrame("�α���");
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
			idLabel = new JLabel("���̵� : ");
			passWordLabel = new JLabel("��й�ȣ : ");
			dbCheckLabel = new JLabel("�α��� ���� üũ�� ��");
			
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
			submit = new JButton("Ȯ��");
			cancle = new JButton("���");
			signUp = new JButton("ȸ������");

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
	class loginSubmitButtonListener implements ActionListener { // Ȯ�ι�ư �׼� ������ Ŭ����
		public void actionPerformed(ActionEvent e) {
			/*
			 * db�� ���ϴ� �ڵ� ~~~~~
			 * dbCheckLabel ���� �ٲ������.
			 * dbCheckLabel �뵵 -> db�� �������� �ʴ� ���� �Է��� ���
			 * ����ڿ��� �������� �ʴ� �Է°��̶�� ǥ�����ֱ� ������.
			 * ���̵�� ��й�ȣ ���� ���� ����. 
			 */
			loginFrame.dispose();
		}
	}
	class loginCanleButtonListener implements ActionListener { 
		// ��� ��ư �׼� ������ Ŭ����
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	class loginSignUpButtonListener implements ActionListener { 
		// ȸ������ ��ư �׼� ������ Ŭ����
		public void actionPerformed(ActionEvent e) {
			new SignUpFrame();
		}
	}
	public static void main(String[] args) {
		new LoginFrame();
	}
}