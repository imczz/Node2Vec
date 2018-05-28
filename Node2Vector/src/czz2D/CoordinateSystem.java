package czz2D;

import java.awt.Container;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * 逻辑坐标系
 * @author CZZ*/
public class CoordinateSystem {

	/**
	 * 画布控件*/
	private Container canvas;
	
	/**
	 * 将屏幕右上角在逻辑坐标系的screenLT点*/
	private Point screenLT;
	
	/**
	 * 逻辑坐标系x坐标单位长度等于多少屏幕坐标系像素*/
	private float kx;
	
	/**
	 * 逻辑坐标系y坐标单位长度等于多少屏幕坐标系像素*/
	private float ky;
	
	/**
	 * 点列表*/
	private ArrayList<Point> pointList;
	
	/**
	 * 屏幕点列表*/
	private ArrayList<ViewPoint> viewPointList;
	
	/**
	 * 点的集合列表*/
	private ArrayList<PointSet> pointSetList;
	
	/**
	 * 屏幕点的集合列表*/
	private ArrayList<ViewPointSet> viewPointSetList;
	
	/*================================方法 methods================================*/
	
	/**
	 * 构造方法*/
	public CoordinateSystem(Container canvas, Point screenLT, float kx, float ky) {
		this.pointList = new ArrayList<Point>();
		this.viewPointList = new ArrayList<ViewPoint>();
		pointSetList = new ArrayList<PointSet>();
		viewPointSetList = new ArrayList<ViewPointSet>();
		this.setCanvas(canvas);
		this.screenLT = screenLT;
		this.kx = kx;
		this.ky = ky;
	}
	
	/**
	 * 获取画布*/
	public Container getCanvas() {
		return canvas;
	}

	/**
	 * 设置画布*/
	public void setCanvas(Container canvas) {
		this.canvas = canvas;
	}
	
	/**
	 * 坐标映射计算，由坐标系中的点计算屏幕上的点*/
	public void Point2View(Point p, ViewPoint vp) {
		vp.x = (int) ((p.x - screenLT.x) * kx);
		vp.y = (int) ((screenLT.y - p.y) * ky);
	}
	
	/**
	 * 坐标映射计算，由屏幕上的点计算坐标系中的点*/
	public void View2Point(ViewPoint vp, Point p) {
		
	}

	/**
	 * 添加一个点*/
	public boolean addPoint(Point p) {
		boolean ret = true;
		this.pointList.add(p);
		ViewPoint vp = new ViewPoint();
		p.vp = vp;							//投影点绑定到点
		viewPointList.add(vp);
		vp.p = p;							//点绑定到投影点
		this.Point2View(p, vp);					//坐标变换
		return ret;
	}
	
	/**
	 * 添加一个点*/
	public boolean addPointSet(PointSet ps) {
		boolean ret = true;
		this.pointSetList.add(ps);
		ViewPointSet vps = new ViewPointSet();
		ps.vps = vps;							//投影点绑定到点
		viewPointSetList.add(vps);
		vps.ps = ps;							//点绑定到投影点
		Point p;
		ViewPoint vp;
		for(int i = 0; i < ps.pointSet.size(); i++) {
			p = ps.pointSet.get(i);
			vp = new ViewPoint();
			vps.viewPointSet.add(vp);
			p.vp = vp;
			vp.p = p;
			this.Point2View(p, vp);					//坐标变换
		}
		return ret;
	}
	
	/**
	 * 画图*/
	public void draw(Graphics g) {
		g.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
		drawPointSet(g);
		ViewPoint vp;
		for(int i = 0; i < this.viewPointList.size(); i++) {
			vp = viewPointList.get(i);				//TODO：同时透明时，修改默认颜色的代码
			if(vp.centerColor != null) {					//透明中心
				g.setColor(vp.centerColor);
				g.fillOval(vp.x - vp.r, vp.y - vp.r, vp.r, vp.r);
			}
			if(vp.lineColor != null) {						//透明边线
				g.setColor(vp.lineColor);
				g.drawOval(vp.x - vp.r, vp.y - vp.r, vp.r, vp.r);
			}
		}
	}
	
	/**
	 * 画点集*/
	private void drawPointSet(Graphics g) {
		ViewPointSet vps;
		ViewPoint vp;
		for(int i = 0; i < this.viewPointSetList.size(); i++) {
			vps = this.viewPointSetList.get(i);
			for(int j = 0; j < vps.viewPointSet.size(); j++) {
				vp = vps.viewPointSet.get(j);				//TODO：同时透明时，修改默认颜色的代码
				if(vp.centerColor != null) {					//透明中心
					g.setColor(vp.centerColor);
					g.fillOval(vp.x - vp.r, vp.y - vp.r, vp.r, vp.r);
				}
				if(vp.lineColor != null) {						//透明边线
					g.setColor(vp.lineColor);
					g.drawOval(vp.x - vp.r, vp.y - vp.r, vp.r, vp.r);
				}
			}
		}
	}
}
