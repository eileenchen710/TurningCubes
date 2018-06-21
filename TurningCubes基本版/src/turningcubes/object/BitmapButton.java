package turningcubes.object;

import android.graphics.RectF;

/**
 * 
 * 自定义按钮：需要在使用时自定义响应方法
 * 
 */
public class BitmapButton {

	int bitmap;// 按钮颜色
	RectF rect;// 按钮范围

	public BitmapButton(int bitmap, RectF rect) {
		super();
		this.bitmap = bitmap;
		this.rect = rect;
	}

	/**
	 * 检查是否在按钮范围内
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
