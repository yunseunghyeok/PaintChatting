package hakfe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpFrame extends JFrame {
	JLabel name, id, passWord, checkPassWord, nickName;
	JButton submit, cancle;
	JTextField nameField, idField, passWordField, nickNameField;
	JPasswordField checkPassWordField;
	JFrame frame;
	JLabel passWordCheck;
	public SignUpFrame() {
		Dimension dim = new Dimension(500, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame = new JFrame("ȸ������");
		frame.setLocation(100, 100);
		frame.setSize(400, 550);
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
			name = new JLabel("�̸� : ");
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
			checkPassWord.setBounds(20, 290, 90, 30);
			
			frame.add(nickName);
			nickName.setBounds(20, 380, 90, 30);
		}
	}

	class AddTextField {
		private AddTextField() {
			nameField = new JTextField("�̸��� �Է��ϼ���.", 40);
			idField = new JTextField("���̵� �Է��ϼ���.", 40);
			passWordField = new JTextField("��й�ȣ�� �Է��ϼ���.", 40);
			checkPassWordField = new JPasswordField("��й�ȣ�� �ٽ� �Է��ϼ���.", 40);
			nickNameField = new JTextField("����� �г����� �Է��ϼ���.", 40);

			frame.add(nameField);
			nameField.setBounds(150, 20, 200, 30);
			
			frame.add(idField);
			idField.setBounds(150, 110, 200, 30);
			
			frame.add(passWordField);
			passWordField.setBounds(150, 200, 200, 30);
			
			frame.add(checkPassWordField);
			checkPassWordField.setBounds(150, 290, 200, 30);
			checkPassWordField.setEchoChar('*');
			
			frame.add(nickNameField);
			nickNameField.setBounds(150, 380, 200, 30);
		}
	}

	class AddButton {
		private AddButton() {
			submit = new JButton("Ȯ��");
			cancle = new JButton("���");
			
			frame.add(submit);
			submit.setBounds(80, 450, 90, 30);
			submit.addActionListener(new submitButtonListener());
			
			frame.add(cancle);
			cancle.setBounds(230, 450, 90, 30);
			cancle.addActionListener(new cancleButtonListener());
		}
	}
	class AddCheckLabel {
		private AddCheckLabel() {
			passWordCheck = new JLabel("��й�ȣ Ȯ�ο� ��");
			frame.add(passWordCheck);
			passWordCheck.setBounds(150, 225, 200, 30);
		}
	}
	class submitButtonListener implements ActionListener { // Ȯ�ι�ư �׼� ������ Ŭ����
		public void actionPerformed(ActionEvent e) {
			String pw = "";
			char[] secret_pw = checkPassWordField.getPassword();
			for(char cha : secret_pw){ 
				Character.toString(cha);
				pw += (pw.equals("")) ? ""+cha+"" : ""+cha+"";
			}
			if(e.getSource() == submit) // Ȯ�� ��ư�� ���� ��� 
			{
				if(!passWordField.getText().equals(pw)) {
					//��й�ȣ ���ڿ��� ��й�ȣ Ȯ�� ���ڿ��� �ٸ���� �� ���ڿ��� ���� �ʴٴ� �޽����� �󺧿� ���. �������� ������� �ʰ� ����.
					passWordCheck.setForeground(Color.red);
					passWordCheck.setText("��й�ȣ�� �ùٸ��� �ʽ��ϴ�.");
				}
				else if(passWordField.getText().equals(pw)){
					//�� ���ڿ��� ���� ���, db�� ���� �����ϰ�, ��ü �������� �ݱ�鼭 �۾��� �Ϸ��.
					/*
					
							db�ڵ�
					   
					 */
					passWordCheck.setText("��й�ȣ�� �ùٸ��ϴ�.");
					frame.dispose(); // �ش� �����Ӹ� �ݱ�� �ڵ�
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
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new SignUpFrame();
	}

}
