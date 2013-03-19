package ad.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import ad.json.XlsxUtil;


public class AdMain implements ActionListener {
	
	private JFrame firstFram,secondFram;
	private JPanel firstPanel;
	private JPanel secondPanel;
	private JFileChooser awPathCh = new JFileChooser();
	private JTextField awPathText;
	private JTextField bodyText;
	private JTextField subjectText;
	private JLabel labelFileName;
	private JLabel labelBodyName;
	private JLabel labelSubjectName;
	
	private static String awFile;
	private static String fileName ;
	private static String bodyMail ;
	private static String subjectMail ;
	private void firstFram(){
		firstFram = new JFrame("批量发邮件");
		firstFram.setSize(500,400);
		firstFram.setDefaultCloseOperation(3);
		firstFram.setResizable(false);
		
		firstPanel = new JPanel();
		firstPanel.setBackground(new Color(90,90,146));
		firstPanel.setLayout(null);
		
		awPathText = new JTextField();
		awPathText.setSize(300,25);
		awPathText.setBounds(90, 5, 200, 25);
		awPathText.setText("");
		
		
		subjectText = new JTextField();
		subjectText.setSize(300,25);
		subjectText.setBounds(90, 65, 200, 25);
		
		bodyText = new JTextField();
		bodyText.setSize(300,25);
		bodyText.setBounds(90, 105, 200, 125);
		bodyText.setText("");
		
		JButton awButton,next;
		awButton = new JButton("文件");
		awButton.setName("awButton");
		awButton.setBounds(310, 5, 75, 26);
		awButton.addActionListener(this);
		
		next = new JButton("发邮件……");
		next.setName("next1");
		next.setBounds(90, 260, 75, 26);
		next.addActionListener(this);
		
		labelFileName = new JLabel("选择文档：");
		labelFileName.setBounds(5, 5, 75, 26);
		
		labelBodyName = new JLabel("邮件内容：");
		labelBodyName.setBounds(5, 105, 75, 26);
		
		labelSubjectName = new JLabel("邮件主题：");
		labelSubjectName.setBounds(5, 65, 75, 26);
		
		firstPanel.add(labelFileName);
		firstPanel.add(labelBodyName);
		firstPanel.add(labelSubjectName);
		firstPanel.add(subjectText);
		firstPanel.add(bodyText);
		firstPanel.add(awPathText);
		firstPanel.add(awButton);
		firstPanel.add(next);
		firstFram.add(firstPanel);
		firstFram.setVisible(true);
	}
	
	private void secondFram(){
		secondFram = new JFrame();
		secondFram.setSize(500,400);
		secondFram.setDefaultCloseOperation(3);
		secondFram.setResizable(false);
		
		secondPanel = new JPanel();
		secondPanel.setBackground(new Color(0,0,0));
		secondPanel.setLayout(null);
		
		JButton programButton,back,next;
		programButton = new JButton("File");
		programButton.setName("awPathButton");
		programButton.setBounds(360, 260, 60, 25);
		programButton.addActionListener(this);
		
		back = new JButton("Back");
		back.setName("back2");
		back.setBounds(200, 320, 75, 26);
		back.addActionListener(this);
		
		next = new JButton("Next");
		next.setName("next2");
		next.setBounds(300, 320, 75, 26);
		next.addActionListener(this);
		
		secondPanel.add(programButton);
		secondPanel.add(back);
		secondPanel.add(next);
		
		secondFram.add(secondPanel);
		secondFram.setVisible(true);
	}
	
	public static void main(String[] args) {
		AdMain entrance = new  AdMain();
		entrance.firstFram();
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton bt = (JButton) e.getSource();
		if (bt.getName() == "awButton") {
			int showOpenDialog = awPathCh.showOpenDialog(firstPanel);
			File selectedFile = awPathCh.getSelectedFile();
			if(null != selectedFile && showOpenDialog == JFileChooser.APPROVE_OPTION){
				awPathText.setText(selectedFile.getPath());
				awFile = awPathText.getText();
			}
		}else if(bt.getName() == "next1" ){
			try {
			  fileName = awPathText.getText();
			  subjectMail = subjectText.getText();
			  bodyMail = bodyText.getText();
			  
			  if(fileName.length()==0 || subjectMail.length()==0  ||bodyMail.length()==0 ){
				  JOptionPane.showMessageDialog(null, "全部内容都不能为空", "系统提示", JOptionPane.OK_OPTION);
				  return;
			  }
			
				XlsxUtil.sendEmail(fileName,bodyMail ,subjectMail); 
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(null, e2.getMessage(), "系统提示", JOptionPane.OK_OPTION);
				e2.printStackTrace();
			}
//			firstFram.removeAll();
//			firstFram.dispose();
//			secondFram();
		}else if(bt.getName() == "back2"){
			secondFram.removeAll();
			secondFram.dispose();
			firstFram();
			awPathText.setText(awFile);
		}
	}
}

