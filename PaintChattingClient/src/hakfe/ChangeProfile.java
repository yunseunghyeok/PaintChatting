package hakfe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeProfile {
	JFrame changeProfileFrame;
	JLabel proFileImageLabel, currentPassWord, currentNickName, passWordCheck, NickNameCheck;
	ImageIcon proFileImage;
	JButton changeImageButton, changePassWordButton, changeNickNameButton, cancleButton;
	JTextField inputCurrentPassWordTF, inputNewPassWordTF, inputNewNickNameTF;
	public ChangeProfile() { 
		/*
		 * db���� ���� �ҷ��;� ��. 
		 * 
		 */
		changeProfileFrame = new JFrame("������ ����");
		changeProfileFrame.setLayout(null);
		
		new CurrentProfile();
		new CurrentPassWord();
		new CurrentNickName();
		
		cancleButton = new JButton("â �ݱ�");
		cancleButton.setBounds(190, 680, 80, 30);
		cancleButton.addActionListener ( new ActionListener() {
		    public void actionPerformed(ActionEvent ev)
		    {
		    	changeProfileFrame.dispose();
		    }
		} );
		changeProfileFrame.add(cancleButton);
		
		changeProfileFrame.setSize(500, 800);
		changeProfileFrame.setVisible(true);
	}
	class CurrentProfile {
		public CurrentProfile() {
			proFileImage = new ImageIcon("img/������ �⺻ ����.png");
			proFileImageLabel = new JLabel(proFileImage);
			proFileImageLabel.setBounds(90, 50, 200, 200);
			changeProfileFrame.add(proFileImageLabel);
			
			changeImageButton = new JButton("������ ����");
			changeImageButton.setBounds(310, 220, 100, 30);
			changeProfileFrame.add(changeImageButton);
		}
	}
	class CurrentPassWord{
		public CurrentPassWord() {
			 // ���� ��й�ȣ�� �Է��Ͽ� Ȯ���� ��, ������ ���ο� ��й�ȣ�� ��ü�ϴ� ������� ����.
			inputCurrentPassWordTF = new JTextField("���� ��й�ȣ�� �Է����ּ���.");
			inputCurrentPassWordTF.setBounds(90, 350, 200, 30);
			
			inputNewPassWordTF = new JTextField("������ ��й�ȣ�� �Է����ּ���.");
			inputNewPassWordTF.setBounds(90, 420, 200, 30);
			
			changePassWordButton = new JButton("��й�ȣ ����");
			changePassWordButton.setBounds(310, 423, 120, 25);
			changePassWordButton.addActionListener(new changePassWordButtonListener());
			
			passWordCheck = new JLabel("���� ��й�ȣ Ȯ�ο�, db�� ��");
			passWordCheck.setBounds(90, 385, 250, 25);
			changeProfileFrame.add(passWordCheck);
			
			changeProfileFrame.add(inputCurrentPassWordTF);
			changeProfileFrame.add(inputNewPassWordTF);
			changeProfileFrame.add(changePassWordButton);
		}
	}
	class CurrentNickName{
		public CurrentNickName() {
			inputNewNickNameTF = new JTextField("������ �г��� �Է�");
			inputNewNickNameTF.setBounds(90, 550, 200, 30);
			
			NickNameCheck = new JLabel("�ߺ� �г��� Ȯ�ο�, db���� �ʿ�");
			NickNameCheck.setBounds(90, 580, 200, 30);
			
			changeNickNameButton = new JButton("�г��� ����");
			changeNickNameButton.setBounds(310, 550, 110, 30);
			changeNickNameButton.addActionListener(new changeNickNameButtonListener());
			
			changeProfileFrame.add(inputNewNickNameTF);
			changeProfileFrame.add(NickNameCheck);
			changeProfileFrame.add(changeNickNameButton);
		}
	}
	class changePassWordButtonListener implements ActionListener { // Ȯ�ι�ư �׼� ������ Ŭ����
		public void actionPerformed(ActionEvent e) {
			/*
			 *		// ����ڰ� ���� �ڽ��� ������� ��й�ȣ�� �߸��Է����� ���? 
			 *		// ��й�ȣ ��ư�� Ŭ�� ��, Label�� �˷��ְ�, �ƹ� ���۵� ���� ����.
			 * 		// ����ڰ� �ùٸ��� �Է����� ��츸 ��й�ȣ�� �����Ѵ�.
			 * 		db�ڵ�
			 * 
			 */
			/*
			if(//����ڰ� �Է��� ���� ���� ��й�ȣ�� �ٸ���� Label�� ����������, �ùٸ��� �ʴٴ� �޽���){
			passWordCheck.setForeground(Color.red);
			passWordCheck.setText("���� ��й�ȣ�� �ùٸ��� �ʽ��ϴ�.");
			}
			else if(// ��й�ȣ�� �ǰ� �ԷµǸ� �����ߴٴ� db����, db����, Label�� ����Ϸ� �޽���){
			passWordCheck.setForeground(Color.blue);
			passWordCheck.setText("��й�ȣ�� ����Ǿ����ϴ�!");
			}
			*/
			passWordCheck.setForeground(Color.blue);
			passWordCheck.setText("���� test");
		}
	}
	class changeNickNameButtonListener implements ActionListener { // Ȯ�ι�ư �׼� ������ Ŭ����
		public void actionPerformed(ActionEvent e) {
			/*
			 *		// �ߺ��� �г����� �����ϴ��� ã�� Ž�� �ڵ尡 �ʿ��� (����Ž��)
			 * 		// �ߺ��� �г����� ������ ���, Label�� �˷��ְ� �ƹ� ���۵� ���� ����.
			 *		// ����ڰ� �ùٸ��� �Է����� ��츸 �г����� �����Ѵ� + Label�� ����Ǿ��ٰ� �˷���..
			 * 		db�ڵ�
			 */
			/*
			if(//�г����� �ߺ��� ��� Label�� ����������, �ߺ��ȴٴ� �޽���){
			NickNameCheck.setForeground(Color.red);
			NickNameCheck.setText("�̹� �����ϴ� �г��� �Դϴ�.");
			}
			else if(// �ߺ��� �г����� ���� ���, db����, db����, Label�� ����Ϸ� �޽���){
			NickNameCheck.setForeground(Color.red);
			NickNameCheck.setText("�̹� �����ϴ� �г��� �Դϴ�.");
			}
			*/
			NickNameCheck.setForeground(Color.red);
			NickNameCheck.setText("�ߺ� test");
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChangeProfile();
	}

}
