package czzVector;

/**
 * 向量（浮点型）
 * @author CZZ*/
public class CVector implements IVector{
	
	/**
	 * 向量*/
	private float[] _vector;
	
	/*================================方法 methods================================*/
	
	/**
	 * 空构造方法，初始化一个长度为length的向量，初始化为0
	 * @param length 初始化长度*/
	public CVector() {
		_vector = null;
	}
	
	/**
	 * 构造方法，初始化一个长度为length的向量，初始化为0
	 * @param length 初始化长度*/
	public CVector(int size) {
		_vector = new float[size];
		for(int i = 0; i < _vector.length; i++) {
			_vector[i] = 0f;								//初始化为0
		}
	}
	
	/**
	 * @return 这个向量实例*/
	@Override
	public float[] getVector() {
		return this._vector;
	}
	
	/**
	 * @return 此向量维数*/
	@Override
	public int getSize() {
		int ret;
		if(this._vector == null) ret = 0;				//向量未初始化
		else ret = this._vector.length;
		return ret;
	}
	
	
	/**
	 * 向量数乘，将此向量每个分量乘number
	 * @param 一个浮点数，用来与每个分量相乘*/
	@Override
	public void multiply(float number) {
		if(this._vector != null){
			for(int i = 0; i < this._vector.length; i++) {
				this._vector[i] *= number;
			}
		}
	}

	/**
	 * 两个向量分量分别相乘
	 * @param 另一个通长度的向量*/
	@Override
	public float multiply(IVector v2) {
		float ret = 0;
		if(this._vector != null && this._vector.length == v2.getSize()) {			//向量需要返回同样长度（维度）
			for(int i = 0; i < this._vector.length; i++) {
				ret += (this._vector[i] * v2.getVector()[i]);
			}
		}
		return ret;
	}

	/**
	 * @向量的长度（模，2-范数）*/
	@Override
	public float getLength() {
		float s = 0;
		if(this._vector != null) {
			for(int i = 0; i < this._vector.length; i++) {
				s += this._vector[i] * this._vector[i];
			}
		}
		return (float) Math.sqrt(s);
	}

	@Override
	public IVector new_Multi(float number) {
		IVector vret = new CVector(this.getSize());
		for(int i = 0; i < this.getSize(); i++) {
			vret.getVector()[i] = this._vector[i] * number;					//数乘
		}
		return vret;
	}

	@Override
	public void add(IVector v2) {
		if(this.getSize() == v2.getSize()) {
			for(int i = 0; i < this.getSize(); i++) {
				this._vector[i] += v2.getVector()[i];					//对应位置相加
			}
		}
	}

	@Override
	public IVector new_Add(IVector v2) {
		IVector vret = new CVector(this.getSize());
		for(int i = 0; i < this.getSize(); i++) {
			vret.getVector()[i] = this._vector[i] + v2.getVector()[i];					//对应位置相加
		}
		return vret;
	}


	@Override
	public void load(float seed) {
		if(this._vector != null){
			for(int i = 0; i < this._vector.length; i++) {
				this._vector[i] = seed;
			}
		}
	}
	
	/**
	 * 重新调整大小，重新申请空间
	 * @param size 初始化空间长度
	 * @param seed 每个分量初始化为seed*/
	@Override
	public void resizeLoad(int size, float seed) {
		if(size <= 0) return;
		_vector = new float[size];				//ArrayList，初始化容量的构造方法
		for(int i = 0; i < _vector.length; i++) {
			_vector[i] = seed;								//初始化为0
		}
	}
	
	/**
	 * 重新调整大小，重新申请空间
	 * @param size 初始化空间长度*/
	@Override
	public void resize(int size) {
		resizeLoad(size, 0);
	}
	
	/**
	 * 转换为字符串用以显示*/
	public String toString() {
		StringBuffer str = new StringBuffer("[");
		if(this._vector != null) {
			for(int i = 0; i < this._vector.length; i++) {
				if(i != 0) str.append(", ");
				str.append(this._vector[i]);
			}
		}
		str.append("]");
		return str.toString();
	}

	/**
	 * @param v另一个向量
	 * @return 此向量与v向量的的欧式距离*/
	@Override
	public float distance(IVector v) {
		float ret = -1;
		if(this.getSize() == v.getSize()) {
			ret = 0;
			for(int i = 0; i < this.getSize(); i++) {
				ret += Math.pow(this._vector[i] - v.getVector()[i], 2);				//分量平方和
			}
			ret = (float) Math.sqrt(ret);
		}
		return ret;
	}

	/**
	 * 复制一个向量，深度复制
	 * @param v 另一个向量*/
	@Override
	public void copy(IVector v) {
		if(v.getSize() > 0) {
			this._vector = new float[v.getSize()];
			for(int i = 0; i < this._vector.length; i++) {
				this._vector[i] = v.getVector()[i];
			}
		}
	}


}
