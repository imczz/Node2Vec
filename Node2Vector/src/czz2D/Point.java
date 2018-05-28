package czz2D;

/**
 * 坐标系中的点
 * @author CZZ*/
public class Point extends GeometricElement{

	/**
	 * 坐标*/
	float x, y;
	
	/**
	 * 屏幕上的点，屏幕上的点的投影的引用*/
	ViewPoint vp;
	
	/*================================方法 methods================================*/
	
	/**
	 * 空构造方法*/
	public Point() {
		this.x = 0;
		this.y = 0;
	}
	
	/**
	 * 构造方法*/
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
