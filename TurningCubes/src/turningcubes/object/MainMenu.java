package turningcubes.object;

import android.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	Context context;
	public MainMenu(Context context) {
		super();
		this.context = context;
		bGame = new BitmapButton(Color.YELLOW, new RectF(0, 60, 160, 110));
		bSurvival = new BitmapButton(Color.MAGENTA, new RectF(0, 120, 160, 170));
		bChart = new BitmapButton(Color.GREEN, new RectF(0, 180, 160, 230));
		bHelp = new BitmapButton(Color.LTGRAY, new RectF(0, 240, 160, 290));
		

	}
	
	public void drawButton(Canvas canvas){
		drawButton(canvas,context);
	}
	public void drawButton(Canvas canvas,Context context){
		bGame.bitmapPic = BitmapFactory.decodeResource(context.getResources(),mobile.turningcubes.R.drawable.button1);
		bSurvival.bitmapPic = BitmapFactory.decodeResource(context.getResources(),mobile.turningcubes.R.drawable.button2);
		bChart.bitmapPic = BitmapFactory.decodeResource(context.getResources(),mobile.turningcubes.R.drawable.button3);
		bHelp.bitmapPic = BitmapFactory.decodeResource(context.getResources(),mobile.turningcubes.R.drawable.button4);
		
		Paint paint=new Paint();
		paint.setColor(bGame.getBitmap());
		//canvas.drawRect(bGame.getRect(), paint);
		canvas.drawBitmap(bGame.bitmapPic, 0 , 60 , paint);
		
		paint.setColor(bSurvival.getBitmap());
		//canvas.drawRect(bSurvival.getRect(), paint);
		canvas.drawBitmap(bSurvival.bitmapPic, 0 , 120 , paint);
		
		paint.setColor(bChart.getBitmap());
		//canvas.drawRect(bChart.getRect(), paint);
		canvas.drawBitmap(bChart.bitmapPic, 0 , 180 , paint);
		
		paint.setColor(bHelp.getBitmap());
		//canvas.drawRect(bHelp.getRect(), paint);
		canvas.drawBitmap(bHelp.bitmapPic, 0 , 240 , paint);
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
