package hakfe;

import javax.swing.*;

import doubledeltas.messages.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;

public class SignUpFrame extends JFrame {
	JLabel name, id, passWord, checkPassWord, nickName;
	JButton submit, cancle;
	JTextField idField, nickNameField;
	JPasswordField passWordField, checkPassWordField;
	JFrame frame;
	JLabel passWordCheck;
	
	LoginFrame parent;
	DataOutputStream dos;
	MessageQueues qs;
	public SignUpFrame(LoginFrame parent) {	// parent: LoginFrame
		this.parent = parent;
		this.dos = parent.parent.dos;
		this.qs = parent.parent.qs;
		
		Dimension dim = new Dimension(500, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame = new JFrame("ȸ������");
		frame.setLocation(100, 100);
		frame.setSize(400, 500);
		frame.setPreferredSize(dim);
		frame.setVisible(true);
		frame.setLayout(null);

		new AddLabel();
		new AddTextField();
		new AddButton();
		new AddCheckLabel();
	}

	class AddLabel {
		private AddLabel() {
			name = new JLabel("�г��� : ");
			id = new JLabel("���̵� : ");
			passWord = new JLabel("��й�ȣ : ");
			checkPassWord = new JLabel("��й�ȣ Ȯ�� : ");
			nickName = new JLabel("����� �г��� : ");
			
			frame.add(name);
			name.setBounds(20, 20, 90, 30);
			
			frame.add(id);
			id.setBounds(20, 110, 90, 30);
			
			frame.add(passWord);
			passWord.setBounds(20, 200, 90, 30);
			
			frame.add(checkPassWord);
			checkPassWord.setBounds(20, 300, 90, 30);
		}
	}

	class AddTextField {
		private AddTextField() {
			nickNameField = new JTextField("", 40);
			idField = new JTextField("", 40);
			passWordField = new JPasswordField("", 40);
			checkPassWordField = new JPasswordField("", 40);

			frame.add(nickNameField);
			nickNameField.setBounds(150, 20, 200, 30);
			
			frame.add(idField);
			idField.setBounds(150, 110, 200, 30);
			
			frame.add(passWordField);
			passWordField.setBounds(150, 200, 200, 30);
			
			frame.add(checkPassWordField);
			checkPassWordField.setBounds(150, 290, 200, 30);
			
		}
	}

	class AddButton {
		private AddButton() {
			submit = new JButton("Ȯ��");
			cancle = new JButton("���");
			
			frame.add(submit);
			submit.setBounds(80, 400, 90, 30);
			submit.addActionListener(new submitButtonListener());
			
			frame.add(cancle);
			cancle.setBounds(230, 400, 90, 30);
			cancle.addActionListener(new cancleButtonListener());
		}
	}
	class AddCheckLabel {
		private AddCheckLabel() {
			passWordCheck = new JLabel();
			frame.add(passWordCheck);
			passWordCheck.setBounds(100, 330, 200, 30);
		}
	}
	class submitButtonListener implements ActionListener { // Ȯ�ι�ư �׼� ������ Ŭ����
		public void actionPerformed(ActionEvent e) {
			String pw = new String(passWordField.getPassword());
			String pwCheck = new String(checkPassWordField.getPassword());
			if(e.getSource() == submit) // Ȯ�� ��ư�� ���� ��� 
			{
				if(!pw.equals(pwCheck)) {
					//��й�ȣ ���ڿ��� ��й�ȣ Ȯ�� ���ڿ��� �ٸ���� �� ���ڿ��� ���� �ʴٴ� �޽����� �󺧿� ���. �������� ������� �ʰ� ����.
					passWordCheck.setForeground(Color.red);
					passWordCheck.setText("��й�ȣ�� �ùٸ��� �ʽ��ϴ�.");
				}
				else {
					//�� ���ڿ��� ���� ���, db�� ���� �����ϰ�, ��ü �������� �ݱ�鼭 �۾��� �Ϸ��.
					String id = idField.getText();
					String nick = nickNameField.getText();
					Message msg;
					try {
						new RegisterMessage(id, pw, nick).send(dos);
						msg = qs.waitForAnyMessage(new byte[] {TransferCode.REGISTER_SUC, TransferCode.REGISTER_FAIL});
						if (msg instanceof RegisterSucMessage) {
							qs.waitForMessage(TransferCode.LOGIN_SUC);
							parent.loginFrame.dispose();
							frame.dispose(); // �ش� �����Ӹ� �ݱ�� �ڵ�
						}
						else if (msg instanceof RegisterFailMessage) {
							byte reason = ((RegisterFailMessage) msg).getReason();
							switch (reason) {
							case RegisterFailMessage.DUPLICATED_ID:
								passWordCheck.setForeground(Color.red);
								passWordCheck.setText("�̹� ��� ���� ID�Դϴ�!");
								break;
							default:
								passWordCheck.setForeground(Color.red);
								passWordCheck.setText("�� �� ���� ������ �߻��߽��ϴ�!");
								break;
							}
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
	class cancleButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == cancle) { // ��� ��ư�� ���� ��� 
				frame.dispose();
			}
		}
	}
}
