package javaPackage;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author Yizina
 * TxtWrite.java
 * 2015/12/15
 */

public class TxtWrite {

	public static void writeTxt(String txt) {
		PrintWriter writer = null;
		try{
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Yzn-IvrSimulator_prj\\src\\resource\\output.txt"), "UTF-8")));
			writer.write(txt);
			writer.close();
		}catch(IOException e){
			System.out.println("ERROR: can't read .txt file");
		}
	}
}