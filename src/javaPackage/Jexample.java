package javaPackage;

import java.awt.Container;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class Jexample implements ActionListener {
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
    JComboBox combobox02 = new JComboBox();
    JTextField text03 = new JTextField();
    JTextField text04 = new JTextField();
    JTextField text05 = new JTextField();
    JTextField text06 = new JTextField();
    JTextField text07 = new JTextField();
    JTextField text08 = new JTextField();
    JRadioButton radio09 = new JRadioButton();
    JRadioButton radio10 = new JRadioButton();
    JTextField text11 = new JTextField();
    JTextField text12 = new JTextField();

    Jexample() {
        jfc.setCurrentDirectory(new File("C://"));// 文件选择器的初始目录定为D盘
        double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        frame.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// 设定窗口出现位置
        frame.setSize(420, 385);// 设定窗口大小
        frame.setContentPane(tabPane);// 设置布局

        label1.setBounds(10, 10, 60, 20);
        text1.setBounds(75, 10, 120, 20);
        label2.setBounds(10, 35, 60, 20);
        text2.setBounds(75, 35, 120, 20);
        button2.setBounds(210, 35, 50, 20);
        label3.setBounds(10, 60, 60, 20);
        text3.setBounds(75, 60, 120, 20);
        button3.setBounds(327, 290, 60, 20);
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

        label01.setBounds(10, 10, 150, 20);
        label02.setBounds(10, 35, 150, 20);
        label03.setBounds(10, 60, 150, 20);
        label04.setBounds(10, 85, 150, 20);
        label05.setBounds(10, 110, 150, 20);
        label06.setBounds(10, 135, 150, 20);
        label07.setBounds(10, 160, 150, 20);
        label08.setBounds(10, 185, 150, 20);
        label09.setBounds(10, 210, 150, 20);
        label10.setBounds(10, 235, 150, 20);
        label11.setBounds(10, 260, 150, 20);
        label12.setBounds(10, 285, 150, 20);
        text01.setBounds(170, 10, 120, 20);
        combobox02.setBounds(170, 35, 120, 20);
        text03.setBounds(170, 60, 120, 20);
        text04.setBounds(170, 85, 120, 20);
        text05.setBounds(170, 110, 120, 20);
        text06.setBounds(170, 135, 120, 20);
        text07.setBounds(170, 160, 120, 20);
        text08.setBounds(170, 185, 120, 20);
        radio09.setBounds(170, 210, 20, 20);
        radio10.setBounds(170, 235, 20, 20);
        text11.setBounds(170, 260, 120, 20);
        text12.setBounds(170, 285, 120, 20);
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
        audio.add(radio10);
        audio.add(text11);
        audio.add(text12);

        frame.setVisible(true);// 窗口可见
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 使能关闭窗口，结束程序
        tabPane.add("Main", main);// 添加布局1
        tabPane.add("Audio", audio);// 添加布局2
    }
    /**
     * 时间监听的方法
     */
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
//        if (e.getSource().equals(button1)) {// 判断触发方法的按钮是哪个
//            jfc.setFileSelectionMode(1);// 设定只能选择到文件夹
//            int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
//            if (state == 1) {
//                return;
//            } else {
//                File f = jfc.getSelectedFile();// f为选择到的目录
//                text1.setText(f.getAbsolutePath());
//            }
//        }
        // 绑定到选择文件，先择文件事件
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
        if (e.getSource().equals(button3)) {
            // 弹出对话框可以改变里面的参数具体得靠大家自己去看，时间很短
        	Matcher matcher = pattern.matcher(text3.getText());
        	if(!matcher.matches()){
        		JOptionPane.showMessageDialog(null, "请输入正确IP地址", "message", 2);
        		return;
        	}
            JOptionPane.showMessageDialog(null, "！", "message", 2);
        }
    }
    public static void main(String[] args) {
        new Jexample();
    }
}
