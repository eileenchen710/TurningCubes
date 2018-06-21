package turningcubes.object;

import android.graphics.RectF;

/**
 * 
 * �Զ��尴ť����Ҫ��ʹ��ʱ�Զ�����Ӧ����
 * 
 */
public class BitmapButton {

	int bitmap;// ��ť��ɫ
	RectF rect;// ��ť��Χ

	public BitmapButton(int bitmap, RectF rect) {
		super();
		this.bitmap = bitmap;
		this.rect = rect;
	}

	/**
	 * ����Ƿ��ڰ�ť��Χ��
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean checkContain(float x, float y) {
		return (rect.contains(x, y));

	}

	public int getBitmap() {
		return bitmap;
	}

	public void setBitmap(int bitmap) {
		this.bitmap = bitmap;
	}

	public RectF getRect() {
		return rect;
	}

	public void setRect(RectF rect) {
		this.rect = rect;
	}

}
