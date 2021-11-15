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

		frame = new JFrame("회원가입");
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
			name = new JLabel("이름 : ");
			id = new JLabel("아이디 : ");
			passWord = new JLabel("비밀번호 : ");
			checkPassWord = new JLabel("비밀번호 확인 : ");
			nickName = new JLabel("사용할 닉네임 : ");
			
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
			nameField = new JTextField("이름을 입력하세요.", 40);
			idField = new JTextField("아이디를 입력하세요.", 40);
			passWordField = new JTextField("비밀번호를 입력하세요.", 40);
			checkPassWordField = new JPasswordField("비밀번호를 다시 입력하세요.", 40);
			nickNameField = new JTextField("사용할 닉네임을 입력하세요.", 40);

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
			submit = new JButton("확인");
			cancle = new JButton("취소");
			
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
			passWordCheck = new JLabel("비밀번호 확인용 라벨");
			frame.add(passWordCheck);
			passWordCheck.setBounds(150, 225, 200, 30);
		}
	}
	class submitButtonListener implements ActionListener { // 확인버튼 액션 리스너 클래스
		public void actionPerformed(ActionEvent e) {
			String pw = "";
			char[] secret_pw = checkPassWordField.getPassword();
			for(char cha : secret_pw){ 
				Character.toString(cha);
				pw += (pw.equals("")) ? ""+cha+"" : ""+cha+"";
			}
			if(e.getSource() == submit) // 확인 버튼을 누른 경우 
			{
				if(!passWordField.getText().equals(pw)) {
					//비밀번호 문자열과 비밀번호 확인 문자열이 다를경우 두 문자열이 같이 않다는 메시지를 라벨에 출력. 프레임은 사라지지 않고 유지.
					passWordCheck.setForeground(Color.red);
					passWordCheck.setText("비밀번호가 올바르지 않습니다.");
				}
				else if(passWordField.getText().equals(pw)){
					//두 문자열이 같을 경우, db에 정보 저장하고, 전체 프레임이 닫기면서 작업이 완료됨.
					/*
					
							db코드
					   
					 */
					passWordCheck.setText("비밀번호가 올바릅니다.");
					frame.dispose(); // 해당 프레임만 닫기는 코드
				}
			}
		}
	}
	class cancleButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == cancle) { // 취소 버튼을 누른 경우 
				frame.dispose();
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new SignUpFrame();
	}

}
