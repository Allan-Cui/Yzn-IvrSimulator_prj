package javaPackage;

import java.awt.BorderLayout;
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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class DefaultSettingMenu implements ActionListener {

	private JDialog frame;
	private JTextField text1;
	private JTextField text2;
	private JTextField text3;
	private JTextField text01;
	private JTextField text03;
	private JTextField text04;
	private JTextField text05;
	private JTextField text06;
	private JTextField text07;
	private JTextField text08;
	private JTextField text11;
	private JTextField text12;
	JFileChooser jfc = new JFileChooser();// 文件选择器

	Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

	Container setting = new Container();
	JLabel label01 = new JLabel("Grammar");
	JLabel label02 = new JLabel("Audio");
	JLabel label03 = new JLabel("IP Port");
	JLabel label04 = new JLabel("Channel-Identifier");
	JLabel label05 = new JLabel("Vendor-Specific-Parameters");
	JLabel label06 = new JLabel("Speech-complete-Timeout");
	JLabel label07 = new JLabel("Confidence-Threshold");
	JLabel label08 = new JLabel("Sensitivity-Level");
	JLabel label09 = new JLabel("No-Input-Timeout");
	JLabel label10 = new JLabel("Recognition-Timeout");
	JLabel label11 = new JLabel("Speech-Incomplete-Timeout");
	JLabel label12 = new JLabel("Start-Input-Timers");
	JLabel label13 = new JLabel("Cancel-If-Queue");
	JLabel label14 = new JLabel("Content-Type");
	JLabel label15 = new JLabel("Content-Length");

	JButton btnGet = new JButton("...");
	JButton btnSave = new JButton("Save");
	JButton btnDefault = new JButton("Default");
	JButton btnClear = new JButton("Clear");

    JRadioButton radio09 = new JRadioButton("True");
    JRadioButton radio09f = new JRadioButton("False");
    ButtonGroup group1 = new ButtonGroup();
    JRadioButton radio10 = new JRadioButton("True");
    JRadioButton radio10f = new JRadioButton("False");
    ButtonGroup group2 = new ButtonGroup();

	JComboBox combobox02 = new JComboBox(new String[]{"Recognition-Mode=hotword","Recognition-Mode=normal","Recognition-Mode=humanassistant"});

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException
	 */
	DefaultSettingMenu() throws IOException {
		frame = new JDialog();

		File directory = new File("..");
		String dir = directory.getCanonicalPath();
		String locationIni = dir + "\\Yzn-IvrSimulator_prj\\src\\resource\\defaultValue.ini";
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
			location = dir + "\\Yzn-IvrSimulator_prj\\src\\mu";
		} else {
			location = audioIni;
		}
		jfc.setCurrentDirectory(new File(location));// 文件选择默认目录

		double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		frame.setLocation(new Point((int) (lx / 2) + 200, (int) (ly / 2) - 200));
		frame.setSize(450, 448);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.getContentPane().add(setting, BorderLayout.CENTER);

		label01.setBounds(10, 10, 60, 20);
		setting.add(label01);

		text1 = new JTextField();
		text1.setText(grammarIni);
		text1.setBounds(180, 10, 160, 20);
		setting.add(text1);

		label02.setBounds(10, 35, 60, 20);
		setting.add(label02);

		text2 = new JTextField();
		text2.setText(location);
		text2.setBounds(180, 35, 160, 20);
		setting.add(text2);

		btnGet.setBounds(350, 35, 50, 20);
		setting.add(btnGet);

		label03.setBounds(10, 60, 60, 20);
		setting.add(label03);

		text3 = new JTextField();
		text3.setText(ipPortIni);
		text3.setBounds(180, 60, 160, 20);
		setting.add(text3);

		btnSave.setBounds(352, 383, 70, 20);
		setting.add(btnSave);

		btnDefault.setBounds(271, 383, 75, 20);
		setting.add(btnDefault);

		btnClear.setBounds(201, 383, 65, 20);
		setting.add(btnClear);

		label04.setBounds(10, 85, 160, 20);
		setting.add(label04);

		text01 = new JTextField();
		text01.setText(ChannelIdentifierIni);
		text01.setBounds(180, 85, 160, 20);
		setting.add(text01);

		label05.setBounds(10, 110, 170, 20);
		setting.add(label05);

		combobox02.setBounds(180, 110, 210, 20);
		setting.add(combobox02);
		if (VendorSpecificParametersIni.equals("Recognition-Mode=hotword")) {
			combobox02.setSelectedIndex(0);
        } else if (VendorSpecificParametersIni.equals("Recognition-Mode=normal")) {
        	combobox02.setSelectedIndex(1);
        } else if (VendorSpecificParametersIni.equals("Recognition-Mode=humanassistant")) {
        	combobox02.setSelectedIndex(2);
        }


		label06.setBounds(10, 135, 160, 20);
		setting.add(label06);

		text03 = new JTextField();
		text03.setText(SpeechCompleteTimeoutIni);
		text03.setBounds(180, 135, 160, 20);
		setting.add(text03);

		label07.setBounds(10, 160, 160, 20);
		setting.add(label07);

		text04 = new JTextField();
		text04.setText(ConfidenceThresholdIni);
		text04.setBounds(180, 160, 160, 20);
		setting.add(text04);

		label08.setBounds(10, 185, 160, 20);
		setting.add(label08);

		text05 = new JTextField();
		text05.setText(SensitivityLevelIni);
		text05.setBounds(180, 185, 160, 20);
		setting.add(text05);

		label09.setBounds(10, 210, 160, 20);
		setting.add(label09);

		text06 = new JTextField();
		text06.setText(NoInputTimeoutIni);
		text06.setBounds(180, 210, 160, 20);
		setting.add(text06);

		label10.setBounds(10, 235, 160, 20);
		setting.add(label10);

		text07 = new JTextField();
		text07.setText(RecognitionTimeoutIni);
		text07.setBounds(180, 235, 160, 20);
		setting.add(text07);

		label11.setBounds(10, 260, 160, 20);
		setting.add(label11);

		text08 = new JTextField();
		text08.setText(SpeechIncompleteTimeoutIni);
		text08.setBounds(180, 260, 160, 20);
		setting.add(text08);

		label12.setBounds(10, 285, 160, 20);
		setting.add(label12);

		radio09.setBounds(180, 285, 60, 20);
		setting.add(radio09);

		radio09f.setBounds(240, 285, 60, 20);
		setting.add(radio09f);

		label13.setBounds(10, 310, 160, 20);
		setting.add(label13);

		radio10.setBounds(180, 310, 60, 20);
		setting.add(radio10);

		radio10f.setBounds(240, 310, 60, 20);
		setting.add(radio10f);

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

        label14.setBounds(10, 335, 160, 20);
		setting.add(label14);

		text11 = new JTextField();
		text11.setText(ContentTypeIni);
		text11.setBounds(180, 335, 160, 20);
		setting.add(text11);

		label15.setBounds(10, 360, 160, 20);
		setting.add(label15);

		text12 = new JTextField();
		text12.setText(ContentLengthIni);
		text12.setBounds(180, 360, 160, 20);
		setting.add(text12);

		btnGet.addActionListener(this);
		btnDefault.addActionListener(this);
		btnClear.addActionListener(this);
		btnSave.addActionListener(this);

		group1.add(radio09);
        group1.add(radio09f);
        group2.add(radio10);
        group2.add(radio10f);
	}

	public void actionPerformed(ActionEvent e) {
		try {
    		if (e.getSource().equals(btnGet)) {
    			jfc.setFileSelectionMode(1);// 设定只能选择到文件
    			int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
    			if (state == 1) {
    				return;// 撤销则返回
    			} else {
    				File f = jfc.getSelectedFile();// f为选择到的文件
    				text2.setText(f.getAbsolutePath());
    			}
    		}
    		if (e.getSource().equals(btnSave)) {
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
    			saveSetting();
    			JOptionPane.showMessageDialog(null, "已保存");
    			return;
    		}
    		if (e.getSource().equals(btnClear)) {
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
    		}
    		if (e.getSource().equals(btnDefault)) {
    			File directory = new File("..");
    			String dir = directory.getCanonicalPath();
    			String locationIni = dir + "\\Yzn-IvrSimulator_prj\\src\\resource\\defaultValue.ini";
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
    				location = dir + "\\Yzn-IvrSimulator_prj\\src\\mu";
    			} else {
    				location = audioIni;
    			}
    	        jfc.setCurrentDirectory(new File(location));// 文件选择默认目录
    			text1.setText(grammarIni);
    	        text2.setText(location);
    	        text3.setText(ipPortIni);
    	        text01.setText(ChannelIdentifierIni);
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
    	} catch (UnknownHostException e1) {
    		e1.printStackTrace();
    	} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void setVisible(boolean b) {
		DefaultSettingMenu.this.frame.setVisible(b);
	}

	public void setModal(boolean b) {
		DefaultSettingMenu.this.frame.setModal(b);
	}

	public void setAlwaysOnTop(boolean b) {
		DefaultSettingMenu.this.frame.setAlwaysOnTop(b);
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
		String locationIni = dir + "\\Yzn-IvrSimulator_prj\\src\\resource\\defaultValue.ini";
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
