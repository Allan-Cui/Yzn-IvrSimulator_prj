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

	Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
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
    JComboBox combobox02 = new JComboBox(new String[]{"Recognition-Mode","Recognition-Mode=normal","Recognition-Mode"});
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

    MainMenu() throws IOException {

    	File directory = new File("..");
		String dir = directory.getCanonicalPath();
		String location = dir + "\\Yzn-IvrSimulator_prj\\src\\mu";
        jfc.setCurrentDirectory(new File(location));// 文件选择默认目录

        double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        frame.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// 设定窗口出现位置
        frame.setSize(420, 404);// 设定窗口大小
        frame.setContentPane(tabPane);// 设置布局

        label1.setBounds(10, 10, 60, 20);
        text1.setBounds(75, 10, 160, 20);
        label2.setBounds(10, 35, 60, 20);
        text2.setText(location);
        text2.setBounds(75, 35, 160, 20);
        button2.setBounds(240, 35, 50, 20);
        label3.setBounds(10, 60, 60, 20);
        text3.setBounds(75, 60, 160, 20);
        button3.setBounds(327, 309, 60, 20);
        button2.addActionListener(this); // 添加事件处理
        button3.addActionListener(this); // 添加事件处理
        main.add(label1);
        main.add(text1);
        main.add(label2);
        main.add(text2);
        main.add(button2);
        main.add(label3);
        main.add(text3);
        main.add(button3);

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
        text01.setText("*@speechrecog");
        text01.setBounds(180, 10, 160, 20);
        combobox02.setSelectedIndex(1);
        combobox02.setBounds(180, 35, 160, 20);
        text03.setText("1000");
        text03.setBounds(180, 60, 160, 20);
        text04.setText("0.0");
        text04.setBounds(180, 85, 160, 20);
        text05.setText("10");
        text05.setBounds(180, 110, 160, 20);
        text06.setText("10000");
        text06.setBounds(180, 135, 160, 20);
        text07.setText("10000");
        text07.setBounds(180, 160, 160, 20);
        text08.setText("500");
        text08.setBounds(180, 185, 160, 20);
        radio09.setSelected(true);
        radio09.setBounds(180, 210, 60, 20);
        radio09f.setBounds(240, 210, 60, 20);
        radio10.setBounds(180, 235, 60, 20);
        radio10f.setSelected(true);
        radio10f.setBounds(240, 235, 60, 20);
        text11.setText("text/uri-list");
        text11.setBounds(180, 260, 160, 20);
        text12.setText("*");
        text12.setBounds(180, 285, 160, 20);
        button4.setBounds(327, 309, 60, 20);
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
    			String fileExt = ".mu";
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
    			Callbat.callCmd();
    			return;
    		}
    	} catch (UnknownHostException e1) {
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
}
