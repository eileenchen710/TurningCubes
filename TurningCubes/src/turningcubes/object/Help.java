package turningcubes.object;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 
 * ������
 * 
 */
public class Help {
	// ��ť
	BitmapButton bBack;
	BitmapButton bLeft;
	BitmapButton bRight;

	int num = 0;
	int bitmap[] = { Color.BLUE, Color.MAGENTA, Color.GREEN };// ���� ÿҳ������ɫ����

	public Help(Context context) {
		super();
		bBack = new BitmapButton(Color.GRAY, new RectF(0, 30, 223, 148));
	//	bLeft = new BitmapButton(Color.GRAY, new RectF(0, 60, 80, 110));
	//	bRight = new BitmapButton(Color.GRAY, new RectF(0, 120, 80, 170));
		bBack.bitmapPic = BitmapFactory.decodeResource(context.getResources(),mobile.turningcubes.R.drawable.back);
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
	//	canvas.drawColor(bitmap[num]);
		Paint paint = new Paint();
		paint.setColor(bBack.getBitmap());
		//canvas.drawRect(bBack.getRect(), paint);
		canvas.drawBitmap(bBack.bitmapPic, 0 , 30 , paint);
		//paint.setColor(bLeft.getBitmap());
		//canvas.drawRect(bLeft.getRect(), paint);
		//paint.setColor(bRight.getBitmap());
		//canvas.drawRect(bRight.getRect(), paint);
	}
}
