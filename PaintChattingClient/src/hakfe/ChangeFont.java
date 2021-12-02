package hakfe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//폰트 default는 맑은 고딕임.
public class ChangeFont {
	JFrame changeFontFrame;
	JButton font1Button, font21Button, font3Button, ok, cancle; // etc...
	JLabel fontExamples;
	String myFont;
	boolean font1Clicked = false;
	boolean font2Clicked = false;
	boolean font3Clicked = false;
	public ChangeFont() {
		changeFontFrame = new JFrame("폰트 변경");
		changeFontFrame.setLayout(null);
		collocateFontButton();
		collocateFontLabel();
		changeFontFrame.setSize(520, 400);
		changeFontFrame.setVisible(true);
	}
	public void collocateFontButton() {
		font1Button = new JButton("맑은 고딕");
		font21Button = new JButton("굴림");
		font3Button = new JButton("궁서");
		ok = new JButton("적용");
		cancle = new JButton("취소");
		
		font1Button.setSize(100, 30);
		font1Button.setLocation(50, 50);
		changeFontFrame.add(font1Button);
		font1Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fontExamples.setFont(new Font("맑은 고딕", Font.BOLD, 20));
				fontExamples.setText("가나다abcABC123");
				myFont = "맑은 고딕";
				font1Clicked = true;
				font2Clicked = false;
				font3Clicked = false;
			}
		});
		font21Button.setSize(100, 30);
		font21Button.setLocation(200, 50);
		changeFontFrame.add(font21Button);
		font21Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fontExamples.setFont(new Font("굴림", Font.BOLD, 20));
				fontExamples.setText("가나다abcABC123");
				myFont = "굴림";
				font1Clicked = false;
				font2Clicked = true;
				font3Clicked = false;
			}
		});
		font3Button.setSize(100, 30);
		font3Button.setLocation(350, 50);
		changeFontFrame.add(font3Button);
		font3Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fontExamples.setFont(new Font("궁서", Font.BOLD, 20));
				fontExamples.setText("가나다abcABC123");
				myFont = "궁서";
				font1Clicked = false;
				font2Clicked = false;
				font3Clicked = true;
			}
		});
		ok.setSize(100, 30);
		ok.setLocation(100, 270);
		changeFontFrame.add(ok);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(font1Clicked) { // 맑은 고딕
					/*
					 * db에 현재 폰트 저장하고,  db에서 가져와서 myInterface에서 적용시켜야 함.
					 * */
				}
				else if(font2Clicked) { // 굴림
					/*
					 * db에 현재 폰트 저장하고,  db에서 가져와서 myInterface에서 적용시켜야 함.
					 * */
				}
				else if(font3Clicked) { // 궁서
					/*
					 * db에 현재 폰트 저장하고,  db에서 가져와서 myInterface에서 적용시켜야 함.
					 * */
				}
				changeFontFrame.dispose();
			}
		});
		cancle.setSize(100, 30);
		cancle.setLocation(300, 270);
		changeFontFrame.add(cancle);
		
		cancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeFontFrame.dispose();
			}
		});
	}
	
	public void collocateFontLabel() {
		fontExamples = new JLabel("폰트 미리보기");
		fontExamples.setSize(250, 200);
		fontExamples.setLocation(190, 100);
		fontExamples.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		changeFontFrame.add(fontExamples);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChangeFont();
	}

}
