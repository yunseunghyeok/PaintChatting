package hakfe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeProfile {
	JFrame changeProfileFrame;
	JLabel proFileImageLabel, currentPassWord, currentNickName, imageCheck, passWordCheck, NickNameCheck;
	ImageIcon proFileImage;
	JButton changeImage, changePassWord, changeNickName;
	public ChangeProfile() { 
		/*
		 * db���� ���� �ҷ��;� ��. 
		 * 
		 */
		changeProfileFrame = new JFrame("������ ����");
		changeProfileFrame.setLayout(new FlowLayout());
		
		proFileImage = new ImageIcon("img/test.png");
		proFileImageLabel = new JLabel(proFileImage);
		
		changeProfileFrame.add(proFileImageLabel);
		
		new CurrentPassWord();
		new CurrentNickName();
		
		changeProfileFrame.setSize(800, 800);
		changeProfileFrame.setVisible(true);
	}
	class CurrentPassWord{
		public CurrentPassWord() {
			
		}
	}
	class CurrentNickName{
		public CurrentNickName() {
			
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChangeProfile();
	}

}
