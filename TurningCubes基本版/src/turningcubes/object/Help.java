package turningcubes.object;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 
 * 帮助类
 * 
 */
public class Help {
	// 按钮
	BitmapButton bBack;
	BitmapButton bLeft;
	BitmapButton bRight;

	int num = 0;
	int bitmap[] = { Color.BLUE, Color.MAGENTA, Color.GREEN };// 帮助 每页暂以颜色区分

	public Help(Context context) {
		super();
		bBack = new BitmapButton(Color.GRAY, new RectF(0, 0, 80, 50));
		bLeft = new BitmapButton(Color.GRAY, new RectF(0, 60, 80, 110));
		bRight = new BitmapButton(Color.GRAY, new RectF(0, 120, 80, 170));
	}

	public int checkClick(float x, float y) {
		if (bBack.checkContain(x, y)) {
			num = 0;
			return 0;
		}
		if (bLeft.checkContain(x, y))
			if (num > 0)
				num--;
		if (bRight.checkContain(x, y))
			if (num < 2)
				num++;
		return 4;
	}

	public void draw(Canvas canvas) {
		canvas.drawColor(bitmap[num]);
		Paint paint = new Paint();
		paint.setColor(bBack.getBitmap());
		canvas.drawRect(bBack.getRect(), paint);
		paint.setColor(bLeft.getBitmap());
		canvas.drawRect(bLeft.getRect(), paint);
		paint.setColor(bRight.getBitmap());
		canvas.drawRect(bRight.getRect(), paint);
	}
}
