package czzLog;

/**
 * 一条消息
 * @author CZZ*/
public class Message {

	/**
	 * 消息*/
	String message;
	
	/**
	 * 时间戳*/
	long timeMilli;
	
	/*================================方法 methods================================*/
	
	/**
	 * 构造方法*/
	Message(String message){
		this.message = message;
		timeMilli = System.currentTimeMillis();
	}
	
	/**
	 * 获取时间戳*/
	public long getTimeMilli() {
		return timeMilli;
	}
	
	/**
	 * 获取消息*/
	public String getMessage() {
		return message;
	}
	
	/**
	 * 信息转化为字符串*/
	public String toString() {
		return (timeMilli + ":" + message);
	}
}
