package czzWord2Vec;

/**
 * 因为sigmoid函数只在0附近变化较大，值域在（-1, 1），所以可以预先生成一个函数值表，这样可以节约运算时间
 * @author CZZ*/
public class ExpTable {

	/**
	 * 预exp表长度*/
	private int _tableSize;
	
	/**
	 * 自变量X的上下限*/
	private int _maxX;

	/**
	 * 预exp表*/
	private float[] _table;
	/*================================方法 methods================================*/
	
	/**
	 * 默认构造方法*/
	ExpTable(){
		this._tableSize = 2000;
		this._maxX = 8;
		_table = new float[this._tableSize + 1];			//为了右边得到闭区间
		initTable();
	}
	
	/**
	 * 设置标的大小，自变量上下限
	 * @param tableSize 细分区间数量
	 * @param max 从[-max, max]*/
	ExpTable(int tableSize, int max){
		if(tableSize <= 0) tableSize = 2000;
		if(max <= 0) max = 8;
		this._tableSize = tableSize;
		this._maxX = max;
		_table = new float[this._tableSize + 1];			//为了右边得到闭区间
		initTable();
	}
	
	/**
	 * S形的sigmoid函数
	 * @param x 自变量
	 * @return 函数值*/
	public static float sigmoid(float x) {
		float t = (float) Math.exp(x);					//t值过大会溢出，返回NaN（Not a Number）
		return t / (t + 1);							//或许可以让除数与被除数大小接近
		//return (1/(1 + (float)Math.exp(-x)));			//Sigmoid(x) = 1/(1 + e ^-x)
	}
	
	/**
	 * @return 获得表的长度*/
	public int getTableSize() {
		return _tableSize;
	}

	/**
	 * @return 自变量上下限*/
	public int getMaxX() {
		return _maxX;
	}

	/**
	 * 初始化表*/
	private void initTable() {
		float x;
		for(int i = 0; i <= this._tableSize; i++) {
			x = (2.0f * i / this._tableSize - 1) * this._maxX;			//[-maxX, maxX]分成tableSize份
			_table[i] = ExpTable.sigmoid(x);
		}
	}
	
	/**
	 * 查表操作
	 * @param x 自变量*/
	public float getSigmoid(float x) {
		float ret = -1;
		if(x > this._maxX) ret = 1;
		else if(x < -this._maxX) ret = 0;
		else ret = this._table[(int) ((x + this._maxX) * (this._tableSize / this._maxX / 2))];
		return ret;
	}
}
