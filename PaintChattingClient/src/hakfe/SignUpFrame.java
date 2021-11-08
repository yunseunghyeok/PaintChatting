package hakfe;

import javax.swing.*;
import java.awt.*;

public class SignUpFrame extends JFrame {
	JLabel name, passWord, checkPassWord, nickName;
	JButton submit, cancle;
	JTextField nameField, passWordField, checkPassWordField, nickNameField;
	JFrame frame;
	public SignUpFrame() {
		Dimension dim = new Dimension(500,800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame = new JFrame("회원가입");
		frame.setLocation(100, 100);
		frame.setSize(400,500);
		frame.setPreferredSize(dim);
		frame.setVisible(true);
		frame.setLayout(null);
		
		new addLabel();
	}
	class addLabel{
		public addLabel() {
		name = new JLabel("이름 : ");
		passWord = new JLabel("비밀번호 : ");
		checkPassWord = new JLabel("비밀번호 확인 : ");
		nickName = new JLabel("사용할 닉네임 : ");
		
		frame.add(name);
		name.setBounds(20, 20, 90, 30);

		frame.add(passWord);
		passWord.setBounds(20, 110, 90, 30);
		
		frame.add(checkPassWord);
		checkPassWord.setBounds(20, 200, 90, 30);
		
		frame.add(nickName);
		nickName.setBounds(20, 290, 90, 30);
		}
	}
	class addTextField {
		
		
	}
	class addButton{
		
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new SignUpFrame();
	}

}
