package turningcubes.object;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 主菜单类：各种按钮及入口
 * 
 */
public class MainMenu {
	BitmapButton bGame;// 游戏按钮
	BitmapButton bSurvival;
	BitmapButton bChart;
	BitmapButton bHelp;

	public MainMenu(Context context) {
		super();
		bGame = new BitmapButton(Color.YELLOW, new RectF(0, 0, 100, 100));
		bSurvival = new BitmapButton(Color.MAGENTA, new RectF(0, 120, 100, 200));
		bChart = new BitmapButton(Color.GREEN, new RectF(0, 220, 100, 320));
		bHelp = new BitmapButton(Color.LTGRAY, new RectF(0, 340, 100, 440));
	}

	public int checkClick(float x, float y) {
		if (bGame.checkContain(x, y))
			return 1;
		if (bSurvival.checkContain(x, y))
			return 2;
		if (bChart.checkContain(x, y))
			return 3;
		if (bHelp.checkContain(x, y))
			return 4;
		return 0;
	}

	public void draw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		paint.setColor(bGame.getBitmap());
		canvas.drawRect(bGame.getRect(), paint);
		paint.setColor(bSurvival.getBitmap());
		canvas.drawRect(bSurvival.getRect(), paint);
		paint.setColor(bChart.getBitmap());
		canvas.drawRect(bChart.getRect(), paint);
		paint.setColor(bHelp.getBitmap());
		canvas.drawRect(bHelp.getRect(), paint);

	}

}
