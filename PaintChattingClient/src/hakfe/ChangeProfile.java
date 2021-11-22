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
		 * db에서 사진 불러와야 함. 
		 * 
		 */
		changeProfileFrame = new JFrame("프로필 변경");
		changeProfileFrame.setLayout(null);
		
		new CurrentProfile();
		new CurrentPassWord();
		new CurrentNickName();
		
		cancleButton = new JButton("창 닫기");
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
			proFileImage = new ImageIcon("img/프로필 기본 사진.png");
			proFileImageLabel = new JLabel(proFileImage);
			proFileImageLabel.setBounds(90, 50, 200, 200);
			changeProfileFrame.add(proFileImageLabel);
			
			changeImageButton = new JButton("프로필 변경");
			changeImageButton.setBounds(310, 220, 100, 30);
			changeProfileFrame.add(changeImageButton);
		}
	}
	class CurrentPassWord{
		public CurrentPassWord() {
			 // 현재 비밀번호를 입력하여 확인한 후, 맞으면 새로운 비밀번호로 교체하는 방식으로 구현.
			inputCurrentPassWordTF = new JTextField("현재 비밀번호를 입력해주세요.");
			inputCurrentPassWordTF.setBounds(90, 350, 200, 30);
			
			inputNewPassWordTF = new JTextField("변경할 비밀번호를 입력해주세요.");
			inputNewPassWordTF.setBounds(90, 420, 200, 30);
			
			changePassWordButton = new JButton("비밀번호 변경");
			changePassWordButton.setBounds(310, 423, 120, 25);
			changePassWordButton.addActionListener(new changePassWordButtonListener());
			
			passWordCheck = new JLabel("현재 비밀번호 확인용, db와 비교");
			passWordCheck.setBounds(90, 385, 250, 25);
			changeProfileFrame.add(passWordCheck);
			
			changeProfileFrame.add(inputCurrentPassWordTF);
			changeProfileFrame.add(inputNewPassWordTF);
			changeProfileFrame.add(changePassWordButton);
		}
	}
	class CurrentNickName{
		public CurrentNickName() {
			inputNewNickNameTF = new JTextField("변경할 닉네임 입력");
			inputNewNickNameTF.setBounds(90, 550, 200, 30);
			
			NickNameCheck = new JLabel("중복 닉네임 확인용, db연결 필요");
			NickNameCheck.setBounds(90, 580, 200, 30);
			
			changeNickNameButton = new JButton("닉네임 변경");
			changeNickNameButton.setBounds(310, 550, 110, 30);
			changeNickNameButton.addActionListener(new changeNickNameButtonListener());
			
			changeProfileFrame.add(inputNewNickNameTF);
			changeProfileFrame.add(NickNameCheck);
			changeProfileFrame.add(changeNickNameButton);
		}
	}
	class changePassWordButtonListener implements ActionListener { // 확인버튼 액션 리스너 클래스
		public void actionPerformed(ActionEvent e) {
			/*
			 *		// 사용자가 현재 자신이 사용중인 비밀번호를 잘못입력했을 경우? 
			 *		// 비밀번호 버튼을 클릭 시, Label로 알려주고, 아무 동작도 하지 않음.
			 * 		// 사용자가 올바르게 입력했을 경우만 비밀번호를 변경한다.
			 * 		db코드
			 * 
			 */
			/*
			if(//사용자가 입력한 값과 현재 비밀번호가 다를경우 Label을 붉은색으로, 올바르지 않다는 메시지){
			passWordCheck.setForeground(Color.red);
			passWordCheck.setText("현재 비밀번호가 올바르지 않습니다.");
			}
			else if(// 비밀번호가 옳게 입력되면 성공했다는 db동작, db변경, Label로 변경완료 메시지){
			passWordCheck.setForeground(Color.blue);
			passWordCheck.setText("비밀번호가 변경되었습니다!");
			}
			*/
			passWordCheck.setForeground(Color.blue);
			passWordCheck.setText("성공 test");
		}
	}
	class changeNickNameButtonListener implements ActionListener { // 확인버튼 액션 리스너 클래스
		public void actionPerformed(ActionEvent e) {
			/*
			 *		// 중복된 닉네임이 존재하는지 찾는 탐색 코드가 필요함 (순차탐색)
			 * 		// 중복된 닉네임이 존재할 경우, Label로 알려주고 아무 동작도 하지 않음.
			 *		// 사용자가 올바르게 입력했을 경우만 닉네임을 변경한다 + Label로 변경되었다고 알려줌..
			 * 		db코드
			 */
			/*
			if(//닉네임이 중복될 경우 Label을 붉은색으로, 중복된다는 메시지){
			NickNameCheck.setForeground(Color.red);
			NickNameCheck.setText("이미 존재하는 닉네임 입니다.");
			}
			else if(// 중복된 닉네임이 없을 경우, db동작, db변경, Label로 변경완료 메시지){
			NickNameCheck.setForeground(Color.red);
			NickNameCheck.setText("이미 존재하는 닉네임 입니다.");
			}
			*/
			NickNameCheck.setForeground(Color.red);
			NickNameCheck.setText("중복 test");
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChangeProfile();
	}

}
