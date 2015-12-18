package javaPackage;

import java.awt.Container;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * @author Yizina
 * MainMenu.java
 * 2015/12/11
 */

public class MainMenu implements ActionListener {

//	Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
	Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\:\\d{1,5}\\b");
    JFrame frame = new JFrame("IvrSimulator");// 框架布局
    JTabbedPane tabPane = new JTabbedPane();// 选项卡布局

    Container main = new Container();//
    JLabel label1 = new JLabel("Grammar");
    JLabel label2 = new JLabel("Audio");
    JLabel label3 = new JLabel("IP Port");
    JTextField text1 = new JTextField();// TextField 目录的路径
    JTextField text2 = new JTextField();// 文件的路径
    JTextField text3 = new JTextField();// IP Port
    JButton button2 = new JButton("...");// 选择
    JFileChooser jfc = new JFileChooser();// 文件选择器
    JButton button3 = new JButton("Go");//
    JButton btnDefault = new JButton("Default");
    JButton btnClear = new JButton("Clear");
    JButton btnSetting = new JButton("DefaultSetting");
    JButton btnDefaultSub = new JButton("Default");
    JButton btnClearSub = new JButton("Clear");
    JButton btnSettingSub = new JButton("DefaultSetting");

    Container audio = new Container();
    JLabel label01 = new JLabel("Channel-Identifier");
    JLabel label02 = new JLabel("Vendor-Specific-Parameters");
    JLabel label03 = new JLabel("Speech-complete-Timeout");
    JLabel label04 = new JLabel("Confidence-Threshold");
    JLabel label05 = new JLabel("Sensitivity-Level");
    JLabel label06 = new JLabel("No-Input-Timeout");
    JLabel label07 = new JLabel("Recognition-Timeout");
    JLabel label08 = new JLabel("Speech-Incomplete-Timeout");
    JLabel label09 = new JLabel("Start-Input-Timers");
    JLabel label10 = new JLabel("Cancel-If-Queue");
    JLabel label11 = new JLabel("Content-Type");
    JLabel label12 = new JLabel("Content-Length");
    JTextField text01 = new JTextField();
    JComboBox combobox02 = new JComboBox(new String[]{"Recognition-Mode=hotword","Recognition-Mode=normal","Recognition-Mode=humanassistant"});
    JTextField text03 = new JTextField();
    JTextField text04 = new JTextField();
    JTextField text05 = new JTextField();
    JTextField text06 = new JTextField();
    JTextField text07 = new JTextField();
    JTextField text08 = new JTextField();
    JRadioButton radio09 = new JRadioButton("True");
    JRadioButton radio09f = new JRadioButton("False");
    ButtonGroup group1 = new ButtonGroup();
    JRadioButton radio10 = new JRadioButton("True");
    JRadioButton radio10f = new JRadioButton("False");
    ButtonGroup group2 = new ButtonGroup();
    JTextField text11 = new JTextField();
    JTextField text12 = new JTextField();
    JButton button4 = new JButton("Go");

    final static String fileExt = ".mu";
    private final JCheckBox checkBox = new JCheckBox("remember this");
    private final JCheckBox checkBoxSub = new JCheckBox("remember this");

    MainMenu() throws IOException {

    	File directory = new File("..");
		String dir = directory.getCanonicalPath();
//		String locationIni = dir + "\\Yzn-IvrSimulator_prj\\src\\resource\\defaultValue.ini";
		String locationIni = dir + "\\src\\txt\\defaultValue.ini";
		IniEditer iniRead = new IniEditer();
		String grammarIni = iniRead.getProfileString(locationIni, "Main", "Grammar");
		String audioIni = iniRead.getProfileString(locationIni, "Main", "Audio");
		String ipPortIni = iniRead.getProfileString(locationIni, "Main", "IP Port");
		String ChannelIdentifierIni = iniRead.getProfileString(locationIni, "Audio", "Channel-Identifier");
		String VendorSpecificParametersIni = iniRead.getProfileString(locationIni, "Audio", "Vendor-Specific-Parameters");
		String SpeechCompleteTimeoutIni = iniRead.getProfileString(locationIni, "Audio", "Speech-complete-Timeout");
		String ConfidenceThresholdIni = iniRead.getProfileString(locationIni, "Audio", "Confidence-Threshold");
		String SensitivityLevelIni = iniRead.getProfileString(locationIni, "Audio", "Sensitivity-Level");
		String NoInputTimeoutIni = iniRead.getProfileString(locationIni, "Audio", "No-Input-Timeout");
		String RecognitionTimeoutIni = iniRead.getProfileString(locationIni, "Audio", "Recognition-Timeout");
		String SpeechIncompleteTimeoutIni = iniRead.getProfileString(locationIni, "Audio", "Speech-Incomplete-Timeout");
		String StartInputTimersIni = iniRead.getProfileString(locationIni, "Audio", "Start-Input-Timers");
		String CancelIfQueueIni = iniRead.getProfileString(locationIni, "Audio", "Cancel-If-Queue");
		String ContentTypeIni = iniRead.getProfileString(locationIni, "Audio", "Content-Type");
		String ContentLengthIni = iniRead.getProfileString(locationIni, "Audio", "Content-Length");
		String location;
		if (audioIni.isEmpty()) {
//			location = dir + "\\Yzn-IvrSimulator_prj\\src\\mu";
			location = dir + "\\src\\mu";
		} else {
			location = audioIni;
		}
        jfc.setCurrentDirectory(new File(location));// 文件选择默认目录

        double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        frame.setLocation(new Point((int) (lx / 2) - 200, (int) (ly / 2) - 200));// 设定窗口出现位置
        frame.setSize(420, 436);// 设定窗口大小
        frame.setContentPane(tabPane);// 设置布局

        label1.setBounds(10, 10, 60, 20);
        text1.setText(grammarIni);
        text1.setBounds(75, 10, 160, 20);
        label2.setBounds(10, 35, 60, 20);
        text2.setText(location);
        text2.setBounds(75, 35, 160, 20);
        button2.setBounds(240, 35, 50, 20);
        label3.setBounds(10, 60, 60, 20);
        text3.setText(ipPortIni);
        text3.setBounds(75, 60, 160, 20);
        button3.setBounds(330, 341, 60, 20);
        button2.addActionListener(this); // 添加事件处理
        button3.addActionListener(this); // 添加事件处理
        btnDefault.setBounds(250, 341, 75, 20);
        btnClear.setBounds(180, 341, 65, 20);
        btnSetting.setBounds(10, 341, 120, 20);
        btnDefault.addActionListener(this);
        btnClear.addActionListener(this);
        btnSetting.addActionListener(this);
        checkBox.setBounds(275, 314, 115, 21);
        checkBox.addActionListener(this);
        main.add(checkBox);
        main.add(label1);
        main.add(text1);
        main.add(label2);
        main.add(text2);
        main.add(button2);
        main.add(label3);
        main.add(text3);
        main.add(button3);
        main.add(btnDefault);
        main.add(btnClear);
        main.add(btnSetting);

        label01.setBounds(10, 10, 160, 20);
        label02.setBounds(10, 35, 170, 20);
        label03.setBounds(10, 60, 160, 20);
        label04.setBounds(10, 85, 160, 20);
        label05.setBounds(10, 110, 160, 20);
        label06.setBounds(10, 135, 160, 20);
        label07.setBounds(10, 160, 160, 20);
        label08.setBounds(10, 185, 160, 20);
        label09.setBounds(10, 210, 160, 20);
        label10.setBounds(10, 235, 160, 20);
        label11.setBounds(10, 260, 160, 20);
        label12.setBounds(10, 285, 160, 20);
        text01.setText(ChannelIdentifierIni);
        text01.setBounds(180, 10, 160, 20);
        if (VendorSpecificParametersIni.equals("Recognition-Mode=hotword")) {
        	combobox02.setSelectedIndex(0);
        } else if (VendorSpecificParametersIni.equals("Recognition-Mode=normal")) {
        	combobox02.setSelectedIndex(1);
        } else if (VendorSpecificParametersIni.equals("Recognition-Mode=humanassistant")) {
        	combobox02.setSelectedIndex(2);
        }
        combobox02.setBounds(180, 35, 210, 20);
        text03.setText(SpeechCompleteTimeoutIni);
        text03.setBounds(180, 60, 160, 20);
        text04.setText(ConfidenceThresholdIni);
        text04.setBounds(180, 85, 160, 20);
        text05.setText(SensitivityLevelIni);
        text05.setBounds(180, 110, 160, 20);
        text06.setText(NoInputTimeoutIni);
        text06.setBounds(180, 135, 160, 20);
        text07.setText(RecognitionTimeoutIni);
        text07.setBounds(180, 160, 160, 20);
        text08.setText(SpeechIncompleteTimeoutIni);
        text08.setBounds(180, 185, 160, 20);
        if (StartInputTimersIni.equals("true")) {
        	radio09.setSelected(true);
        } else if (StartInputTimersIni.equals("false")) {
        	radio09f.setSelected(true);
        }
        radio09.setBounds(180, 210, 60, 20);
        radio09f.setBounds(240, 210, 60, 20);
        if (CancelIfQueueIni.equals("true")) {
        	radio10.setSelected(true);
        } else if (CancelIfQueueIni.equals("false")) {
        	radio10f.setSelected(true);
        }
        radio10.setBounds(180, 235, 60, 20);
        radio10f.setBounds(240, 235, 60, 20);
        text11.setText(ContentTypeIni);
        text11.setBounds(180, 260, 160, 20);
        text12.setText(ContentLengthIni);
        text12.setBounds(180, 285, 160, 20);
        button4.setBounds(330, 341, 60, 20);
        btnDefaultSub.setBounds(250, 341, 75, 20);
        btnClearSub.setBounds(180, 341, 65, 20);
        btnSettingSub.setBounds(10, 341, 120, 20);
        btnDefaultSub.addActionListener(this);
        btnClearSub.addActionListener(this);
        btnSettingSub.addActionListener(this);
        checkBoxSub.setBounds(275, 314, 115, 21);
        checkBoxSub.addActionListener(this);
        audio.add(checkBoxSub);
        audio.add(label01);
        audio.add(label02);
        audio.add(label03);
        audio.add(label04);
        audio.add(label05);
        audio.add(label06);
        audio.add(label07);
        audio.add(label08);
        audio.add(label09);
        audio.add(label10);
        audio.add(label11);
        audio.add(label12);
        audio.add(text01);
        audio.add(combobox02);
        audio.add(text03);
        audio.add(text04);
        audio.add(text05);
        audio.add(text06);
        audio.add(text07);
        audio.add(text08);
        audio.add(radio09);
        audio.add(radio09f);
        group1.add(radio09);
        group1.add(radio09f);
        audio.add(radio10);
        audio.add(radio10f);
        group2.add(radio10);
        group2.add(radio10f);
        audio.add(text11);
        audio.add(text12);
        audio.add(button4);
        audio.add(btnDefaultSub);
        audio.add(btnClearSub);
        audio.add(btnSettingSub);
        button4.addActionListener(this); // 添加事件处理

        frame.setVisible(true);// 窗口可见
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 使能关闭窗口，结束程序
        tabPane.add("Main", main);// 添加布局1
        tabPane.add("Audio", audio);// 添加布局2

    }
    /**
     * 时间监听的方法
     */
    public void actionPerformed(ActionEvent e) {
        // 绑定到选择文件，先择文件事件
    	try {
    		if (e.getSource().equals(button2)) {
    			jfc.setFileSelectionMode(1);// 设定只能选择到文件
    			int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
    			if (state == 1) {
    				return;// 撤销则返回
    			} else {
    				File f = jfc.getSelectedFile();// f为选择到的文件
    				text2.setText(f.getAbsolutePath());
    			}
    		}
    		if (e.getSource().equals(button3)||e.getSource().equals(button4)) {
    			// 弹出对话框可以改变里面的参数具体得靠大家自己去看，时间很短
    			String txt;
    			String grammar = text1.getText();
    			String audioPath = text2.getText();
    			String serverIP = text3.getText();
    			String localIP = InetAddress.getLocalHost().getHostAddress();

    			Map map = new HashMap();
    			String ChannelIdentifier = text01.getText();
    			Object VendorSpecificParameters = combobox02.getSelectedItem();
    			String SpeechCompleteTimeout = text03.getText();
    			String ConfidenceThreshold = text04.getText();
    			String SensitivityLevel = text05.getText();
    			String NoInputTimeout = text06.getText();
    			String RecognitionTimeout = text07.getText();
    			String SpeechIncompleteTimeout = text08.getText();
    			String StartInputTimers = "";
    			if (radio09.isSelected()) {
    				StartInputTimers = "true";
    			} else {
    				StartInputTimers = "false";
    			}
    			String CancelIfQueue = "";
    			if (radio10.isSelected()) {
    				CancelIfQueue = "true";
    			} else {
    				CancelIfQueue = "false";
    			}
    			String ContentType = text11.getText();
    			String ContentLength = text12.getText();
    			map.put("ChannelIdentifier", ChannelIdentifier);
    			map.put("VendorSpecificParameters", VendorSpecificParameters);
    			map.put("SpeechCompleteTimeout", SpeechCompleteTimeout);
    			map.put("ConfidenceThreshold", ConfidenceThreshold);
    			map.put("SensitivityLevel", SensitivityLevel);
    			map.put("NoInputTimeout", NoInputTimeout);
    			map.put("RecognitionTimeout", RecognitionTimeout);
    			map.put("SpeechIncompleteTimeout", SpeechIncompleteTimeout);
    			map.put("StartInputTimers", StartInputTimers);
    			map.put("CancelIfQueue", CancelIfQueue);
    			map.put("ContentType", ContentType);
    			map.put("ContentLength", ContentLength);

    			Matcher matcher = pattern.matcher(text3.getText());
    			String empty = "ヾ(ｰｰ )ｫｨｫｨ";
    			//空白检测
    			if(isNull(grammar)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(isNull(audioPath)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(isNull(serverIP)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(!matcher.matches()){
    				JOptionPane.showMessageDialog(null, "请输入正确IP地址", "message", 2);
    				return;
    			}
    			if(isNull(ChannelIdentifier)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(isNull(VendorSpecificParameters)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(isNull(SpeechCompleteTimeout)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(isNull(ConfidenceThreshold)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(isNull(SensitivityLevel)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(isNull(NoInputTimeout)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(isNull(RecognitionTimeout)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(isNull(SpeechIncompleteTimeout)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(isNull(StartInputTimers)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(isNull(CancelIfQueue)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(isNull(ContentType)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(isNull(ContentLength)){
    				JOptionPane.showMessageDialog(null, empty, "message", 2);
    				return;
    			}
    			if(FileFinder2.checkMu(fileExt, audioPath)){
    				JOptionPane.showMessageDialog(null, "指定路径下不存在'*" + fileExt + "'文件", "message", 2);
    				return;
    			}
    			String header = TxtRead.readHeader(serverIP, localIP);
    			String gram = TxtRead.readGram(grammar);
    			String footer = TxtRead.readFooter(serverIP, localIP);
    			String audio = FileFinder2.findPath(fileExt, audioPath, map);
    			txt = header + gram + audio + footer;
    			TxtWrite.writeTxt(txt);
    			if (checkBox.isSelected()) {
    				saveSetting();
    			}
    			Callbat.callCmd();
    			return;
    		}
    		if (e.getSource().equals(btnClear)||e.getSource().equals(btnClearSub)) {
    			text1.setText("");
    			text2.setText("");
    			text3.setText("");
    			text01.setText("");
    			combobox02.setSelectedIndex(1);
    			text03.setText("");
    			text04.setText("");
    			text05.setText("");
    			text06.setText("");
    			text07.setText("");
    			text08.setText("");
    			radio09.setSelected(true);
    			radio10.setSelected(true);
    			text11.setText("");
    			text12.setText("");
    			checkBox.setSelected(false);
    			checkBoxSub.setSelected(false);
    		}
    		if (e.getSource().equals(btnDefault)||e.getSource().equals(btnDefaultSub)) {
    			File directory = new File("..");
    			String dir = directory.getCanonicalPath();
//    			String locationIni = dir + "\\Yzn-IvrSimulator_prj\\src\\resource\\defaultValue.ini";
    			String locationIni = dir + "\\src\\txt\\defaultValue.ini";
    			IniEditer iniRead = new IniEditer();
    			String grammarIni = iniRead.getProfileString(locationIni, "Main", "Grammar");
    			String audioIni = iniRead.getProfileString(locationIni, "Main", "Audio");
    			String ipPortIni = iniRead.getProfileString(locationIni, "Main", "IP Port");
    			String ChannelIdentifierIni = iniRead.getProfileString(locationIni, "Audio", "Channel-Identifier");
    			String VendorSpecificParametersIni = iniRead.getProfileString(locationIni, "Audio", "Vendor-Specific-Parameters");
    			String SpeechCompleteTimeoutIni = iniRead.getProfileString(locationIni, "Audio", "Speech-complete-Timeout");
    			String ConfidenceThresholdIni = iniRead.getProfileString(locationIni, "Audio", "Confidence-Threshold");
    			String SensitivityLevelIni = iniRead.getProfileString(locationIni, "Audio", "Sensitivity-Level");
    			String NoInputTimeoutIni = iniRead.getProfileString(locationIni, "Audio", "No-Input-Timeout");
    			String RecognitionTimeoutIni = iniRead.getProfileString(locationIni, "Audio", "Recognition-Timeout");
    			String SpeechIncompleteTimeoutIni = iniRead.getProfileString(locationIni, "Audio", "Speech-Incomplete-Timeout");
    			String StartInputTimersIni = iniRead.getProfileString(locationIni, "Audio", "Start-Input-Timers");
    			String CancelIfQueueIni = iniRead.getProfileString(locationIni, "Audio", "Cancel-If-Queue");
    			String ContentTypeIni = iniRead.getProfileString(locationIni, "Audio", "Content-Type");
    			String ContentLengthIni = iniRead.getProfileString(locationIni, "Audio", "Content-Length");
    			String location;
    			if (audioIni.isEmpty()) {
//    				location = dir + "\\Yzn-IvrSimulator_prj\\src\\mu";
    				location = dir + "\\src\\mu";
    			} else {
    				location = audioIni;
    			}
    	        jfc.setCurrentDirectory(new File(location));// 文件选择默认目录
    			text1.setText(grammarIni);
    	        text2.setText(location);
    	        text3.setText(ipPortIni);
    	        text01.setText(ChannelIdentifierIni);
    	        text01.setBounds(180, 10, 160, 20);
    	        if (VendorSpecificParametersIni.equals("Recognition-Mode=hotword")) {
    	        	combobox02.setSelectedIndex(0);
    	        } else if (VendorSpecificParametersIni.equals("Recognition-Mode=normal")) {
    	        	combobox02.setSelectedIndex(1);
    	        } else if (VendorSpecificParametersIni.equals("Recognition-Mode=humanassistant")) {
    	        	combobox02.setSelectedIndex(2);
    	        }
    	        text03.setText(SpeechCompleteTimeoutIni);
    	        text04.setText(ConfidenceThresholdIni);
    	        text05.setText(SensitivityLevelIni);
    	        text06.setText(NoInputTimeoutIni);
    	        text07.setText(RecognitionTimeoutIni);
    	        text08.setText(SpeechIncompleteTimeoutIni);
    	        if (StartInputTimersIni.equals("true")) {
    	        	radio09.setSelected(true);
    	        } else if (StartInputTimersIni.equals("false")) {
    	        	radio09f.setSelected(true);
    	        }
    	        if (CancelIfQueueIni.equals("true")) {
    	        	radio10.setSelected(true);
    	        } else if (CancelIfQueueIni.equals("false")) {
    	        	radio10f.setSelected(true);
    	        }
    	        text11.setText(ContentTypeIni);
    	        text12.setText(ContentLengthIni);
    		}
    		if (e.getSource().equals(btnSetting)||e.getSource().equals(btnSettingSub)) {
    			DefaultSettingMenu dialog = new DefaultSettingMenu();
    			dialog.setVisible(true);
    		}
    		if (e.getSource().equals(checkBox)) {
    			if (checkBox.isSelected()) {
    				checkBoxSub.setSelected(true);
    			} else if (!checkBox.isSelected()) {
    				checkBoxSub.setSelected(false);
    			}
    		}
    		if (e.getSource().equals(checkBoxSub)) {
    			if (checkBoxSub.isSelected()) {
    				checkBox.setSelected(true);
    			} else if (!checkBoxSub.isSelected()) {
    				checkBox.setSelected(false);
    			}
    		}
    	} catch (UnknownHostException e1) {
    		e1.printStackTrace();
    	} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    public static void main(String[] args) throws IOException {
        new MainMenu();
    }

    /**
     * 空白检测function
     * @param obj
     */
    private boolean isNull(Object obj) {
    	if (obj.equals("")||obj == null) {
    		return true;
    	}
    	return false;
    }

    private void saveSetting() throws IOException{
    	File directory = new File("..");
		String dir = directory.getCanonicalPath();
//		String locationIni = dir + "\\Yzn-IvrSimulator_prj\\src\\resource\\defaultValue.ini";
		String locationIni = dir + "\\src\\txt\\defaultValue.ini";
    	IniEditer iniWrite = new IniEditer();
    	iniWrite.setProfileString(locationIni, "Main", "Grammar", text1.getText());
		iniWrite.setProfileString(locationIni, "Main", "Audio", text2.getText());
		iniWrite.setProfileString(locationIni, "Main", "IP Port", text3.getText());
		iniWrite.setProfileString(locationIni, "Audio", "Channel-Identifier", text01.getText());
		String selectedValue = "";
		if (combobox02.getSelectedIndex() == 0) {
			selectedValue = "Recognition-Mode=hotword";
		} else if (combobox02.getSelectedIndex() == 1) {
			selectedValue = "Recognition-Mode=normal";
		} else if (combobox02.getSelectedIndex() == 2) {
			selectedValue = "Recognition-Mode=humanassistant";
		}
		iniWrite.setProfileString(locationIni, "Audio", "Vendor-Specific-Parameters", selectedValue);
		iniWrite.setProfileString(locationIni, "Audio", "Speech-complete-Timeout", text03.getText());
		iniWrite.setProfileString(locationIni, "Audio", "Confidence-Threshold", text04.getText());
		iniWrite.setProfileString(locationIni, "Audio", "Sensitivity-Level", text05.getText());
		iniWrite.setProfileString(locationIni, "Audio", "No-Input-Timeout", text06.getText());
		iniWrite.setProfileString(locationIni, "Audio", "Recognition-Timeout", text07.getText());
		iniWrite.setProfileString(locationIni, "Audio", "Speech-Incomplete-Timeout", text08.getText());
		String radio1 = "";
		String radio2 = "";
		if (radio09.isSelected()) {
			radio1 = "true";
		} else if (radio09f.isSelected()) {
			radio1 = "false";
		}
		if (radio10.isSelected()) {
			radio2 = "true";
		} else if (radio10f.isSelected()) {
			radio2 = "false";
		}
		iniWrite.setProfileString(locationIni, "Audio", "Start-Input-Timers", radio1);
		iniWrite.setProfileString(locationIni, "Audio", "Cancel-If-Queue", radio2);
		iniWrite.setProfileString(locationIni, "Audio", "Content-Type", text11.getText());
		iniWrite.setProfileString(locationIni, "Audio", "Content-Length", text12.getText());
    }
}
