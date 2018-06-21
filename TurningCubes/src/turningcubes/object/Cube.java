package turningcubes.object;

import java.util.Random;
import android.graphics.Color;

/**
 * 方块类：游戏底板的基本构成元素
 * 
 */
public class Cube {

	protected static Random RAND = new Random();
	protected float side;// 方块边长
	protected int action;// 方块效果(底板方块/普通方块/特殊功能方块)
	protected int bitmap;// 图像，根据方块类型确定。暂且用颜色区分

	/**
	 * 游戏底板方块构造函数
	 * 
	 * @param side
	 */
	public Cube(float side) {
		super();
		// 使边长不小于0
		if (side > 0)
			this.side = side;
		else
			this.side = 0;

		this.action = 0;// 该方块是底板方块
		setBitmap();// 设置图像(颜色)
	}

	/**
	 * 构造函数
	 * 
	 * @param side
	 * @param cubeType
	 */
	public Cube(float side, int cubeType) {
		super();

		if (side > 0)
			this.side = side;
		else
			this.side = 0;

		// cubeType>=5
		action = RAND.nextInt(cubeType - 1) + 2;

		setBitmap();
	}

	/**
	 * 设置方块为石块
	 * 
	 * @param stone
	 */
	public void setStone(boolean stone) {
		this.action = 1;// 石块方块(游戏底板中间不可消除/穿越的方块)
		this.setBitmap();
	}

	@Override
	public Cube clone() {
		// TODO Auto-generated method stub
		return this;
	}

	public float getSide() {
		if (side > 0)
			return side;
		else
			return 0;
	}

	public Cube setSide(float side) {
		if (side > 0)
			this.side = side;
		else
			this.side = 0;
		return this;
	}

	public int getBitmap() {
		return bitmap;
	}

	public void setBitmap(int bitmap) {
		this.bitmap = bitmap;
	}

	/**
	 * 根据方块类型设置其图像(颜色)
	 */
	public void setBitmap() {
		switch (action) {
		case -1:// 选中列的方块
			bitmap = Color.DKGRAY;
			break;
		case 0:// 底板方块
			bitmap = Color.GRAY;
			break;
		case 1:// 石块方块
			bitmap = Color.BLACK;
			break;
		case 2:// 普通方块
			bitmap = Color.BLUE;
			break;
		case 3:
			bitmap = Color.CYAN;
			break;
		case 4:
			bitmap = Color.GREEN;
			break;
		case 5:
			bitmap = Color.YELLOW;
			break;
		case 6:// 特殊方块 停转一次
			bitmap = Color.MAGENTA;
			break;
		case 7:// 特殊方块 旋转方向逆转
			bitmap = Color.RED;
			break;
		case 8:// 特殊方块 不确定
			bitmap = Color.DKGRAY;
			break;
		}
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

}
