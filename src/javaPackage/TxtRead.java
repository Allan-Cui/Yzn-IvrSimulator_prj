package javaPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author Yizina
 * TxtRead.java
 * 2015/12/14
 */
public class TxtRead {
	/**
	 * 功能：Java读取txt文件的内容
	 * 步骤：1：先获得文件句柄
	 * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
	 * 3：读取到输入流后，需要读取生成字节流
	 * 4：一行一行的输出。readline()。
	 * 备注：需要考虑的是异常情况
	 * @param filePath
	 */
	private static String readTxt = "";
	final static String headerPath = "C:\\Yzn-IvrSimulator_prj\\src\\txt\\header.txt";
	final static String gramPath = "C:\\Yzn-IvrSimulator_prj\\src\\txt\\gram.txt";
	final static String audioPath = "C:\\Yzn-IvrSimulator_prj\\src\\txt\\audio.txt";
	final static String footerPath = "C:\\Yzn-IvrSimulator_prj\\src\\txt\\footer.txt";

	public static String readHeader(String filePath, String serverIP, String localIP){
		try {
			String encoding="UTF-8";
			File file=new File(filePath);
			if(file.isFile() && file.exists()){ //判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file),encoding);//考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while((lineTxt = bufferedReader.readLine()) != null){
					lineTxt = lineTxt.replace("$$$SERVERIP$$$", serverIP);
					lineTxt = lineTxt.replace("$$$LOCALIP$$$", localIP);
					readTxt = readTxt + lineTxt + "\r\n";
				}
				read.close();
			}else{
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return readTxt;
	}

	public static String readFooter(String filePath, String serverIP, String localIP){
		try {
			String encoding="UTF-8";
			File file=new File(filePath);
			if(file.isFile() && file.exists()){ //判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file),encoding);//考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while((lineTxt = bufferedReader.readLine()) != null){
					lineTxt = lineTxt.replace("$$$SERVERIP$$$", serverIP);
					lineTxt = lineTxt.replace("$$$LOCALIP$$$", localIP);
					readTxt = readTxt + lineTxt + "\r\n";
				}
				read.close();
			}else{
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return readTxt;
	}

	public static String readGram(String filePath, String Gram){
		try {
			String encoding="UTF-8";
			File file=new File(filePath);
			if(file.isFile() && file.exists()){ //判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file),encoding);//考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while((lineTxt = bufferedReader.readLine()) != null){
					lineTxt = lineTxt.replace("$$$GRAM$$$", Gram);
					readTxt = readTxt + lineTxt + "\r\n";
				}
				read.close();
			}else{
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return readTxt;
	}

}