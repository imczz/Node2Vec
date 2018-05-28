package czz2D;

import java.awt.Color;

/**
 * 屏幕上的点，其实是一个圆，由中心与边线构成
 * @author CZZ*/
public class ViewPoint {

	/**
	 * 像素x,y*/
	int x, y;
	
	/**
	 * 这个屏幕点来自哪个逻辑点*/
	Point p;
	
	/**
	 * 点的颜色*/
	Color centerColor, lineColor;
	
	/**
	 * 线条宽度*/
	int lineWidth;
	
	/**
	 * 点的半径大小*/
	int r;
	
	/*================================方法 methods================================*/
	
	/**
	 * 空构造方法*/
	ViewPoint(){
		this.x = 0;
		this.y = 0;
		this.init();
	}
	
	/**
	 * 初始化颜色*/
	private void init() {
		this.centerColor = Color.WHITE;				//白中心
		this.lineColor = Color.BLACK;				//黑线条
		this.lineWidth = 2;
		this.r = 8;
	}
	
	/**
	 * 设置中心颜色
	 * @param centerColor 中心颜色，null代表透明*/
	public void setCenterColor(Color centerColor) {
		this.centerColor = centerColor;
	}
	
	/**
	 * 设置边线颜色
	 * @param lineColor 边线颜色，null代表透明*/
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}
	
	/**
	 * 设置边线宽度
	 * @param lineWidth 如果想用一个圆环代表一个点，宽度可以大一些，改变的是g.drawLine时的brush*/
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}
	
	/**
	 * 设置圆点半径
	 * @param r 圆点的半径*/
	public void setRadius(int r) {
		this.r = r;
	}
	
	/**
	 * 同时设置全部参数
	 * @param centerColor 中心颜色，null代表透明
	 * @param lineColor 边线颜色，null代表透明
	 * @param lineWidth 如果想用一个圆环代表一个点，宽度可以大一些
	 * @param r 圆点的半径*/
	public void set(Color centerColor, Color lineColor, int lineWidth, int r) {
		this.centerColor = centerColor;
		this.lineColor = lineColor;
		this.lineWidth = lineWidth;
		this.r = r;
	}
}
