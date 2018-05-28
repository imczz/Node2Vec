package czz2D;

import java.util.ArrayList;

/**
 * 点的集合
 * @author CZZ*/
public class PointSet extends GeometricElement{

	/**
	 * 点集*/
	ArrayList<Point> pointSet;
	
	/**
	 * 屏幕上的点集*/
	ViewPointSet vps;
	
	/**
	 * 点集中点最小x坐标*/
	float minX;
	
	/**
	 * 点集中点最小y坐标*/
	float minY;
	
	/**
	 * 点集中点最大x坐标*/
	float maxX;
	
	/**
	 * 点集中点最大y坐标*/
	float maxY;
	
	/*================================方法 methods================================*/
	
	/**
	 * 构造方法*/
	public PointSet() {
		this.minX = 0;
		this.minY = 0;
		this.maxX = 0;
		this.maxY = 0;
		pointSet = new ArrayList<Point>();
	}
	

	/**
	 * @return 点集中点的个数*/
	public int getPointNumber() {
		return this.pointSet.size();
	}
	
	/**
	 * 返回对应的屏幕上的点集*/
	public ViewPointSet getViewPointSet() {
		return this.vps;
	}
	
	/**
	 * 向点集合中添加一个点
	 * @param p 待添加的点*/
	public boolean addPoint(Point p) {
		boolean ret = true;
		this.pointSet.add(p);
		if(p.x > this.maxX) this.maxX = p.x;
		if(p.x < this.minX) this.minX = p.x;
		if(p.y > this.maxY) this.maxY = p.y;
		if(p.y < this.minY) this.minY = p.y;
		if(vps != null) {
			ViewPoint vp = new ViewPoint();
			vp.p = p;						//点绑定到屏幕点
			p.vp = vp;						//屏幕点绑定到点
			if(vps.isUniformAdd()) vp.set(vps.centerColor, vps.lineColor, vps.lineWidth, vps.r);	//统一属性
			vps.viewPointSet.add(vp);
		}
		return ret;
	}
}
