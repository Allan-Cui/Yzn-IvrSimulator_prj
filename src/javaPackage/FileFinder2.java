package javaPackage;

import java.io.File;
import java.util.Map;

/**
 * @author Floral
 * FileFinder2.java
 * 2015/12/15
 */

public class FileFinder2 {

	static String audioTxt = "";

	public static String findPath(String filenameSuffix, String path, Map map){
		File f = new File(path);
		File[] paths=f.listFiles();
		String audio = TxtRead.audio();
		for(File p:paths)
		{
			if(p.getAbsolutePath().endsWith(filenameSuffix)){
				audioTxt = audioTxt + audio.replace("$$$AUDIO$$$", p.toString());
			}
		}
		//add by Yizina 2015/12/15 start
		audioTxt = audioTxt.replace("$$$Channel-Identifier$$$", map.get("ChannelIdentifier").toString());
		audioTxt = audioTxt.replace("$$$Vendor-Specific-Parameters$$$", map.get("VendorSpecificParameters").toString());
		audioTxt = audioTxt.replace("$$$Speech-complete-Timeout$$$", map.get("SpeechCompleteTimeout").toString());
		audioTxt = audioTxt.replace("$$$Confidence-Threshold$$$", map.get("ConfidenceThreshold").toString());
		audioTxt = audioTxt.replace("$$$Sensitivity-Level$$$", map.get("SensitivityLevel").toString());
		audioTxt = audioTxt.replace("$$$No-Input-Timeout$$$", map.get("NoInputTimeout").toString());
		audioTxt = audioTxt.replace("$$$Recognition-Timeout$$$", map.get("RecognitionTimeout").toString());
		audioTxt = audioTxt.replace("$$$Speech-Incomplete-Timeout$$$", map.get("SpeechIncompleteTimeout").toString());
		audioTxt = audioTxt.replace("$$$Start-Input-Timers$$$", map.get("StartInputTimers").toString());
		audioTxt = audioTxt.replace("$$$Cancel-If-Queue$$$", map.get("CancelIfQueue").toString());
		audioTxt = audioTxt.replace("$$$Content-Type$$$", map.get("ContentType").toString());
		audioTxt = audioTxt.replace("$$$Content-Length$$$", map.get("ContentLength").toString());
		//add by Yizina 2015/12/15 end
		return audioTxt;
	}

	public static boolean checkMu (String filenameSuffix, String path) {
		File f = new File(path);
		File[] paths=f.listFiles();
		for (File p:paths) {
			if(p.getAbsolutePath().endsWith(filenameSuffix)){
				return false;
			}
		}
		return true;
	}
}