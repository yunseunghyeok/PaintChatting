package hakfe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//��Ʈ default�� ���� �����.
public class ChangeFont {
	JFrame changeFontFrame;
	JButton font1Button, font21Button, font3Button, ok, cancle; // etc...
	JLabel fontExamples;
	String myFont;
	boolean font1Clicked = false;
	boolean font2Clicked = false;
	boolean font3Clicked = false;
	public ChangeFont() {
		changeFontFrame = new JFrame("��Ʈ ����");
		changeFontFrame.setLayout(null);
		collocateFontButton();
		collocateFontLabel();
		changeFontFrame.setSize(520, 400);
		changeFontFrame.setVisible(true);
	}
	public void collocateFontButton() {
		font1Button = new JButton("���� ���");
		font21Button = new JButton("����");
		font3Button = new JButton("�ü�");
		ok = new JButton("����");
		cancle = new JButton("���");
		
		font1Button.setSize(100, 30);
		font1Button.setLocation(50, 50);
		changeFontFrame.add(font1Button);
		font1Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fontExamples.setFont(new Font("���� ���", Font.BOLD, 20));
				fontExamples.setText("������abcABC123");
				myFont = "���� ���";
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
				fontExamples.setFont(new Font("����", Font.BOLD, 20));
				fontExamples.setText("������abcABC123");
				myFont = "����";
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
				fontExamples.setFont(new Font("�ü�", Font.BOLD, 20));
				fontExamples.setText("������abcABC123");
				myFont = "�ü�";
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
				if(font1Clicked) { // ���� ���
					/*
					 * db�� ���� ��Ʈ �����ϰ�,  db���� �����ͼ� myInterface���� ������Ѿ� ��.
					 * */
				}
				else if(font2Clicked) { // ����
					/*
					 * db�� ���� ��Ʈ �����ϰ�,  db���� �����ͼ� myInterface���� ������Ѿ� ��.
					 * */
				}
				else if(font3Clicked) { // �ü�
					/*
					 * db�� ���� ��Ʈ �����ϰ�,  db���� �����ͼ� myInterface���� ������Ѿ� ��.
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
		fontExamples = new JLabel("��Ʈ �̸�����");
		fontExamples.setSize(250, 200);
		fontExamples.setLocation(190, 100);
		fontExamples.setFont(new Font("���� ���", Font.BOLD, 20));
		changeFontFrame.add(fontExamples);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChangeFont();
	}

}
