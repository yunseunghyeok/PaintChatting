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

		frame = new JFrame("회원가입");
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
			name = new JLabel("닉네임 : ");
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
			submit = new JButton("확인");
			cancle = new JButton("취소");
			
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
	class submitButtonListener implements ActionListener { // 확인버튼 액션 리스너 클래스
		public void actionPerformed(ActionEvent e) {
			String pw = new String(passWordField.getPassword());
			String pwCheck = new String(checkPassWordField.getPassword());
			if(e.getSource() == submit) // 확인 버튼을 누른 경우 
			{
				if(!pw.equals(pwCheck)) {
					//비밀번호 문자열과 비밀번호 확인 문자열이 다를경우 두 문자열이 같이 않다는 메시지를 라벨에 출력. 프레임은 사라지지 않고 유지.
					passWordCheck.setForeground(Color.red);
					passWordCheck.setText("비밀번호가 올바르지 않습니다.");
				}
				else {
					//두 문자열이 같을 경우, db에 정보 저장하고, 전체 프레임이 닫기면서 작업이 완료됨.
					String id = idField.getText();
					String nick = nickNameField.getText();
					Message msg;
					try {
						new RegisterMessage(id, pw, nick).send(dos);
						msg = qs.waitForAnyMessage(new byte[] {TransferCode.REGISTER_SUC, TransferCode.REGISTER_FAIL});
						if (msg instanceof RegisterSucMessage) {
							qs.waitForMessage(TransferCode.LOGIN_SUC);
							parent.loginFrame.dispose();
							frame.dispose(); // 해당 프레임만 닫기는 코드
						}
						else if (msg instanceof RegisterFailMessage) {
							byte reason = ((RegisterFailMessage) msg).getReason();
							switch (reason) {
							case RegisterFailMessage.DUPLICATED_ID:
								passWordCheck.setForeground(Color.red);
								passWordCheck.setText("이미 사용 중인 ID입니다!");
								break;
							default:
								passWordCheck.setForeground(Color.red);
								passWordCheck.setText("알 수 없는 오류가 발생했습니다!");
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
			if(e.getSource() == cancle) { // 취소 버튼을 누른 경우 
				frame.dispose();
			}
		}
	}
}
