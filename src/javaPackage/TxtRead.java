package javaPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

	private static String path(String str) throws IOException {
		File directory = new File("..");
		String dir = directory.getCanonicalPath();
//		String path = "\\Yzn-IvrSimulator_prj\\src\\txt";
		String path = "\\src\\txt";
		return dir + path + str;
	}
	private static String headerTxt = "\\header.txt";
	private static String gramTxt = "\\gram.txt";
	private static String audioTxt = "\\audio.txt";
	private static String footerTxt = "\\footer.txt";

	public static String readHeader(String serverIP, String localIP){
		readTxt = "";//清空txt
		try {
			String encoding="UTF-8";
			String filePath = path(headerTxt);
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

	public static String readFooter(String serverIP, String localIP){
		readTxt = "";//清空txt
		try {
			String encoding="UTF-8";
			String filePath = path(footerTxt);
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

	public static String readGram(String Gram){
		readTxt = "";//清空txt
		try {
			String encoding="UTF-8";
			String filePath = path(gramTxt);
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

	public static String audio(){
		readTxt = "";//清空txt
		try {
			String encoding="UTF-8";
			String filePath = path(audioTxt);
			File file=new File(filePath);
			if(file.isFile() && file.exists()){ //判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file),encoding);//考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while((lineTxt = bufferedReader.readLine()) != null){
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