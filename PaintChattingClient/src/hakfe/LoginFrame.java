package hakfe;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import javax.swing.event.*;

import doubledeltas.messages.*;

public class LoginFrame extends JFrame {
	JFrame loginFrame;
	JLabel idLabel, passWordLabel,dbCheckLabel;
	JTextField nameField;
	JPasswordField passWordField;
	JButton submit, cancle, signUp;

	MyInterface parent;
	DataOutputStream dos;
	MessageQueues qs;
	public LoginFrame(MyInterface parent) {
		this.parent = parent;
		this.dos = parent.dos;
		this.qs = parent.qs;
		
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
			dbCheckLabel = new JLabel();
			
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
			passWordField = new JPasswordField();

			loginFrame.add(nameField);
			nameField.setBounds(80, 20, 150, 30);

			loginFrame.add(passWordField);
			passWordField.setBounds(80, 90, 150, 30);
			passWordField.setEchoChar('*');
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
			String id = nameField.getText();
			String pw = "";
			Message msg;
			char[] secret_pw = passWordField.getPassword();
			for(char cha : secret_pw){ 
				Character.toString(cha);
				pw += (pw.equals("")) ? ""+cha+"" : ""+cha+"";
			}
			try {
				new LoginMessage(id, pw).send(dos);
				msg = qs.waitForAnyMessage(new byte[]{TransferCode.LOGIN_SUC, TransferCode.LOGIN_FAIL});
				if (msg instanceof LoginSucMessage) {
					parent.UserID = id;
					for (String name: ((LoginSucMessage) msg).getRooms()) {
						JButton bt = new JButton(name);
						bt.setVisible(true);
						parent.addChattingRoomButtonVec.add(bt);
						parent.gbcForm(bt, 0, parent.count, 1, 1);
						parent.count++;
					}
					parent.roomScroll.updateUI();
					loginFrame.dispose();
				}
				else if (msg instanceof LoginFailMessage) {
					byte reason = ((LoginFailMessage) msg).getReason();
					switch (reason) {
					case LoginFailMessage.NO_ID_FOUND:
						dbCheckLabel.setText("�������� �ʴ� ID�Դϴ�!");
						break;
					case LoginFailMessage.PASSWORD_WRONG:
						dbCheckLabel.setText("�н����尡 Ʋ�Ƚ��ϴ�!");
						break;
					default:
						dbCheckLabel.setText("�� �� ���� ������ �߻��߽��ϴ�!");
						break;
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
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
			new SignUpFrame(LoginFrame.this);
		}
	}
}