package javaPackage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Yizina
 * Callbat.java
 * 2015/12/15
 */

public class Callbat {

	public static void  callCmd(){
		try {
			File directory = new File("..");
			String dir = directory.getCanonicalPath();
//			String locationCmd = dir + "\\Yzn-IvrSimulator_prj\\src\\resource\\test.bat";
//			String locationCmd = dir + "\\Yzn-IvrSimulator_prj\\src\\resource\\go.bat";
			String locationCmd = dir + "\\src\\go.bat";
			Process child = Runtime.getRuntime().exec("cmd.exe /C start "+locationCmd);
			InputStream in = child.getInputStream();
			while ((in.read()) != -1) {
			}
			in.close();
			try {
				child.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}