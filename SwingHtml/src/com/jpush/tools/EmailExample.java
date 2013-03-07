package com.jpush.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.HTMLEditorAdapter;
import chrriis.dj.nativeswing.swtimpl.components.HTMLEditorSaveEvent;
import chrriis.dj.nativeswing.swtimpl.components.JHTMLEditor;

import com.jpush.tools.util.FileUtil;
import com.jpush.tools.util.Mywin;
import com.jpush.tools.util.SystemConfig;
import com.jpush.tools.util.XlsxUtil;
import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;

public class EmailExample {

	protected static final String LS = System.getProperty("line.separator");
	private static String awFile;
	private static String fileName;
	private static JPanel firstPanel;
	private static String bodyMail;
	private static String subjectMail;

	public static JComponent createContent() {
		final JPanel contentPane = new JPanel(new BorderLayout());
		// 文件文件
		firstPanel = new JPanel();
		firstPanel.setBackground(new Color(90, 90, 146));
		firstPanel.setLayout(null);
		JPanel filePanel = new JPanel(new BorderLayout());
		filePanel.setBorder(BorderFactory.createTitledBorder("选择发送邮件的xls文件"));
		JPanel filemiddlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel labelFileName = new JLabel("选择文档:");
		filemiddlePanel.add(labelFileName);
		final JFileChooser awPathCh = new JFileChooser();
		final JTextField awPathText = new JTextField(
				"                                               ");
		filemiddlePanel.add(awPathText);
		JButton getFileButton = new JButton("浏览");
		filemiddlePanel.add(getFileButton);
		filePanel.add(filemiddlePanel, BorderLayout.NORTH);
		contentPane.add(filePanel, BorderLayout.NORTH);
		getFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int showOpenDialog = awPathCh.showOpenDialog(firstPanel);
				File selectedFile = awPathCh.getSelectedFile();
				if (null != selectedFile
						&& showOpenDialog == JFileChooser.APPROVE_OPTION) {
					awPathText.setText("");
					awPathText.setText(selectedFile.getPath());
					awFile = awPathText.getText();
				}
			}
		});

		// 主题
		JPanel subjectPanel = new JPanel(new BorderLayout());
		subjectPanel.setBorder(BorderFactory.createTitledBorder("填写邮件主题"));
		JPanel subjectmiddlePanel = new JPanel(new BorderLayout());
		final JTextField subjectText = new JTextField();
		subjectmiddlePanel.add(subjectText);
		subjectPanel.add(subjectmiddlePanel, BorderLayout.CENTER);
		contentPane.add(subjectPanel, BorderLayout.CENTER);

		Map<String, String> optionMap = new HashMap<String, String>();
		optionMap
				.put("toolbar",
						"["
								+ "  ['Source','-','Save','NewPage','Preview','-','Templates'],"
								+ "  ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],"
								+ "  ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],"
								+ "  ['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField'],"
								+ "  '/',"
								+ "  ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],"
								+ "  ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],"
								+ "  ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],"
								+ "  ['Link','Unlink','Anchor'],"
								+ "  ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],"
								+ "  '/',"
								+ "  ['Styles','Format','Font','FontSize'],"
								+ "  ['TextColor','BGColor'],"
								+ "  ['Maximize', 'ShowBlocks','-','About']"
								+ "]");
		// optionMap.put("uiColor", "'#9AB8F3'");
		// optionMap.put("toolbarCanCollapse", "false");
		final JHTMLEditor htmlEditor = new JHTMLEditor(
				JHTMLEditor.HTMLEditorImplementation.CKEditor,
				JHTMLEditor.CKEditorOptions.setOptions(optionMap));
		htmlEditor.addHTMLEditorListener(new HTMLEditorAdapter() {
			@Override
			public void saveHTML(HTMLEditorSaveEvent e) {
				JOptionPane.showMessageDialog(contentPane, "The data of the HTML editor could be saved anywhere...");
			}
		});
		JPanel bodyPanel = new JPanel(new BorderLayout());
		bodyPanel.setBorder(BorderFactory.createTitledBorder("填写邮件内容"));
		JScrollPane bodyPane = new JScrollPane(htmlEditor);
		bodyPane.setPreferredSize(new Dimension(0, 400));
		JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		final JButton sendMsgButton = new JButton("发邮件");
		middlePanel.add(sendMsgButton);
		bodyPanel.add(middlePanel, BorderLayout.SOUTH);
		bodyPanel.add(bodyPane, BorderLayout.NORTH);
		contentPane.add(bodyPanel, BorderLayout.SOUTH);
		sendMsgButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					fileName = awPathText.getText();
					subjectMail = subjectText.getText();
					bodyMail = htmlEditor.getHTMLContent();

					if (fileName.length() == 0 || subjectMail.length() == 0
							|| bodyMail.length() == 0) {
						JOptionPane.showMessageDialog(null, "全部内容都不能为空", "系统提示", JOptionPane.OK_OPTION);
						return;
					}

					XlsxUtil.sendEmail(fileName, bodyMail, subjectMail);
					sendMsgButton.setEnabled(false);
					sendMsgButton.setText("发送邮件中……");
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage(), "系统提示", JOptionPane.OK_OPTION);
					e2.printStackTrace();
				}
			}
		});

		return contentPane;
	}
	
	public static boolean isOk(){
		boolean resultBoolean = false;
		String s1 = "2014-03-01 17:27:00";
		String s2 = "";
		java.text.DateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		s2 = df.format(new Date());
		java.util.Calendar c1=java.util.Calendar.getInstance();
		java.util.Calendar c2=java.util.Calendar.getInstance();
		try
		{
		c1.setTime(df.parse(s1));
		c2.setTime(df.parse(s2));
		}catch(java.text.ParseException e){
		   System.err.println("格式不正确");
		}
		int result=c1.compareTo(c2);
		if(result==0){
			System.out.println("明天就过期");
			resultBoolean = true;
		}
		else if(result<0){
			System.out.println("已经过期");
		}
		else{
			System.out.println("没有过期 ");
			resultBoolean = true;
		}
		return resultBoolean;
	}

	/*
	 * 
	 *  Standard main method to try that test as a standalone application. 
	 *  
	 *  */
	public static void main(String[] args) {
		FileUtil.writeErLogtoFile("begin email tools ", true);
		try {
			// 时间限制
             if(!isOk()){
            	 JOptionPane.showMessageDialog(null,
							"已经过期 联系 QQ 196610102", "系统提示",
							JOptionPane.OK_OPTION);
            	 return ;
             }
			//

			NativeInterface.open();
			UIUtils.setPreferredLookAndFeel();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					final JFrame frame = new JFrame("批量邮件发送工具");
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.getContentPane().add(createContent(),
							BorderLayout.CENTER);
					frame.setSize(800, 650);
					frame.setLocationByPlatform(true);
					frame.setVisible(true);
					addMenu(frame);
				}
			});
			NativeInterface.runEventPump();
		} catch (Exception e) {
			// TODO: handle exception
			FileUtil.writeErLogtoFile(e.getMessage(), true);
		}
	}
	/**
	 * 创建菜单
	 * @param frame
	 */
	public static void addMenu(final JFrame frame) {
		// 菜单
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("设置");
		JMenuItem cutMenuItem = new JMenuItem("参数配置");
		cutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFrame jf = new JFrame("参数配置");
				jf.setVisible(true);
				jf.setBounds(400, 200, 400, 300);
				frame.setEnabled(false);// 设置主窗体不可用,true恢复可用
										// 问题：1。当jf关闭时，主窗体setEnabled(true);//
				jf.setVisible(true);
				jf.addWindowListener(new Mywin(frame));

				JPanel cPanel = new JPanel();
				cPanel.setLayout(null);
				//
				JLabel labelFileName = new JLabel("邮件间隔时间(分)");
				labelFileName.setBounds(5, 5, 105, 26);

				final JTextField stoptimeText = new JTextField();
				stoptimeText.setSize(100, 25);
				stoptimeText.setBounds(110, 5, 200, 25);
				stoptimeText.setText(SystemConfig.getProperty("mail.stop.time", "5"));

				JLabel labelBodyName = new JLabel("每次发送邮件数");
				labelBodyName.setBounds(5, 65, 105, 26);

				final JTextField countText = new JTextField();
				countText.setSize(300, 25);
				countText.setBounds(110, 65, 200, 25);
				countText.setText(SystemConfig.getProperty("mail.count", "20"));

				JLabel labelsji = new JLabel("邮件之间随机数/秒");
				labelsji.setBounds(5, 125, 115, 26);

				final JTextField sjText = new JTextField();
				sjText.setSize(300, 25);
				sjText.setBounds(110, 125, 200, 25);
				sjText.setText(SystemConfig.getProperty("mail.rnd.time", "30"));
				
				JButton okButton, defaultText;

				okButton = new JButton("确定");
				okButton.setBounds(106, 225, 75, 26);
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String stopTime = stoptimeText.getText();
						String countNum = countText.getText();
						boolean b = true;
						if (stopTime != null && !"".equals(stopTime)) {
							SystemConfig.setProperty(
									"mail.stop.time", stopTime);
						} else {
							b = false;
							JOptionPane.showMessageDialog(null,"设置间隔时间失败", "系统提示", JOptionPane.OK_OPTION);
						}
						if (countNum != null
								&& !"".equals(countNum)) {
							SystemConfig.setProperty("mail.count",
									countNum);
						} else {
							b = false;
							JOptionPane.showMessageDialog(null,
									"设置发送邮件数", "系统提示",
									JOptionPane.OK_OPTION);
						}
						if (b) {
							JOptionPane.showMessageDialog(null,"设置成功", "系统提示", JOptionPane.PLAIN_MESSAGE);
						}
					}
				});

				defaultText = new JButton("默认配置");
				defaultText.setBounds(206, 225, 105, 26);
				defaultText.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						countText.setText("20");
						stoptimeText.setText("5");
						SystemConfig.setProperty("mail.stop.time",
								"5");
						SystemConfig
								.setProperty("mail.count", "20");
					}
				});
				cPanel.add(labelFileName);
				cPanel.add(labelBodyName);
				cPanel.add(okButton);
				cPanel.add(defaultText);
				cPanel.add(countText);
				cPanel.add(labelsji);
				cPanel.add(sjText);
				cPanel.add(stoptimeText);
				jf.add(cPanel);
			}
		});
		fileMenu.add(cutMenuItem);

		JMenu helpMenu = new JMenu("帮助");
		JMenuItem aboutMenuItem = new JMenuItem("关于");
		helpMenu.add(aboutMenuItem);
		aboutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFrame jf = new JFrame("关于");
				jf.setVisible(true);
				jf.setBounds(400, 200, 400, 300);
				frame.setEnabled(false);// 设置主窗体不可用,true恢复可用
										// 问题：1。当jf关闭时，主窗体setEnabled(true);//
				jf.setVisible(true);
				jf.addWindowListener(new Mywin(frame));
				JLabel labelFileName = new JLabel("QQ : 196610102");
				labelFileName.setBounds(5, 5, 200, 26);
				JPanel cPanel = new JPanel();
				cPanel.setLayout(null);
				cPanel.add(labelFileName);
				jf.add(cPanel);
			}
		});
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		frame.setJMenuBar(menuBar);
	}
}
