package czzLog;

import java.util.ArrayList;

/**
 * 日志
 * @author CZZ*/
public class Log {

	public static ArrayList<Message> logList = new ArrayList<Message>();
	
	/*================================方法 methods================================*/
	
	public static void clear() {
		logList.clear();
	}
	
	public static void addMessage(String message) {
		logList.add(new Message(message));
	}
	
	public static String getAll() {
		StringBuffer ret = new StringBuffer();
		long time0;
		if(logList.size() == 1) {
			ret.append(logList.get(0));
		}
		else if(logList.size() > 1) {
			time0 = logList.get(0).getTimeMilli();
			for(int i = 0; i < logList.size(); i++) {
				ret.append((logList.get(i).getTimeMilli()-time0) + ":" + logList.get(i).getMessage() + "\n");
			}
		}
		
		return ret.toString();
	}
}
