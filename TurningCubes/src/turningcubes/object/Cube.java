package turningcubes.object;

import java.util.Random;
import android.graphics.Color;

/**
 * �����ࣺ��Ϸ�װ�Ļ�������Ԫ��
 * 
 */
public class Cube {

	protected static Random RAND = new Random();
	protected float side;// ����߳�
	protected int action;// ����Ч��(�װ巽��/��ͨ����/���⹦�ܷ���)
	protected int bitmap;// ͼ�񣬸��ݷ�������ȷ������������ɫ����

	/**
	 * ��Ϸ�װ巽�鹹�캯��
	 * 
	 * @param side
	 */
	public Cube(float side) {
		super();
		// ʹ�߳���С��0
		if (side > 0)
			this.side = side;
		else
			this.side = 0;

		this.action = 0;// �÷����ǵװ巽��
		setBitmap();// ����ͼ��(��ɫ)
	}

	/**
	 * ���캯��
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
	 * ���÷���Ϊʯ��
	 * 
	 * @param stone
	 */
	public void setStone(boolean stone) {
		this.action = 1;// ʯ�鷽��(��Ϸ�װ��м䲻������/��Խ�ķ���)
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
	 * ���ݷ�������������ͼ��(��ɫ)
	 */
	public void setBitmap() {
		switch (action) {
		case -1:// ѡ���еķ���
			bitmap = Color.DKGRAY;
			break;
		case 0:// �װ巽��
			bitmap = Color.GRAY;
			break;
		case 1:// ʯ�鷽��
			bitmap = Color.BLACK;
			break;
		case 2:// ��ͨ����
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
		case 6:// ���ⷽ�� ͣתһ��
			bitmap = Color.MAGENTA;
			break;
		case 7:// ���ⷽ�� ��ת������ת
			bitmap = Color.RED;
			break;
		case 8:// ���ⷽ�� ��ȷ��
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
