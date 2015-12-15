package javaPackage;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Yizina
 * Callbat.java
 * 2015/12/15
 */

public class Callbat {

      public static void  callCmd(String locationCmd){
          try {
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