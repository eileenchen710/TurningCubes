package mobile.turningcubes;

import javax.annotation.Resources;

import turningcubes.object.Chart;
import turningcubes.object.Game;
import turningcubes.object.Help;
import turningcubes.object.MainMenu;
import turningcubes.object.Survival;
import turningcubes.support.RecordFile;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameSurfaceView extends SurfaceView implements OnTouchListener,
		SurfaceHolder.Callback {
	int state = 0;// 游戏状态
	MainMenu menu;// 主界面
	Game game;// 普通游戏
	Survival survivalGame;// 无尽模式
	Chart chart;// 排行榜
	Help help;// 帮助
	RecordFile record;// 记录

	SurfaceHolder surfaceHolder;
	
	float width;//屏幕宽
	float height;
	
	public Bitmap backgroundMenu1;
	public Bitmap backgroundGame;
	public Bitmap backgroundChart;
	public Bitmap backgroundHelp;
	

	public GameSurfaceView(Context context) {
		super(context);
		// 实例化游戏类
		menu = new MainMenu(context);
		help = new Help(context);
		game = new Game(context);

		survivalGame = new Survival(context, chart);
		chart = new Chart(context,width,height);//传递屏幕大小参数
		setOnTouchListener(this);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		// 读取记录
		record = new RecordFile();
		record.readRecord(game);
		record.readRecord(survivalGame);
		record.readRecord(chart);
		// 设置焦点
		setFocusable(true);
		setFocusableInTouchMode(true);
		
		//设置背景
		backgroundMenu1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg1);
		backgroundGame = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg2);
		backgroundChart = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg3);
		backgroundHelp = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg4);
		 
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		game.setGridsXY(getWidth(), getHeight());
		survivalGame.setGridsXY(getWidth(), getHeight());
		drawCavas();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	protected void drawCavas() {
		Canvas canvas = surfaceHolder.lockCanvas();
		Paint paint=new Paint();  //画笔
		
        Matrix matrix = new Matrix();//使用matri控制图形变换 
        
        float w = ((float) width / backgroundMenu1.getWidth());//屏幕宽度除图片宽度  
        float h = ((float) height / backgroundMenu1.getHeight());//屏幕高度除图片高度  
        matrix.postScale(w, h);// 获取缩放比例  
        // 根据缩放比例获得新的位图  

        backgroundMenu1 = Bitmap.createBitmap
        		(backgroundMenu1, 0, 0, backgroundMenu1.getWidth(),
        				backgroundMenu1.getHeight(), matrix, true); 
        backgroundGame = Bitmap.createBitmap
        		(backgroundGame, 0, 0, backgroundGame.getWidth(),
        				backgroundGame.getHeight(), matrix, true);
        backgroundChart = Bitmap.createBitmap
        		(backgroundChart, 0, 0, backgroundChart.getWidth(),
        				backgroundChart.getHeight(), matrix, true);
        backgroundHelp = Bitmap.createBitmap
        		(backgroundHelp, 0, 0, backgroundHelp.getWidth(),
        				backgroundHelp.getHeight(), matrix, true);
		
	    //	 根据游戏状态调用不同的绘图方法
		switch (state) {
		case 0:
			canvas.drawBitmap(backgroundMenu1, 0 , 0 , paint);
			menu.drawButton(canvas);
			break;
		case 1:
			canvas.drawBitmap(backgroundGame, 0 , 0 , paint);
			game.draw(canvas);
			break;
		case 2:
			canvas.drawBitmap(backgroundGame, 0 , 0 , paint);
			survivalGame.draw(canvas);
			break;
		case 3:
			canvas.drawBitmap(backgroundChart, 0 , 0 , paint);
			chart.draw(canvas);
			break;
		case 4:
			canvas.drawBitmap(backgroundHelp, 0 , 0 , paint);
			help.draw(canvas);
			break;
		}
		surfaceHolder.unlockCanvasAndPost(canvas);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 根据游戏状态调用不同的响应方法
		switch (state) {
		case 0:
			state = menu.checkClick(event.getX(), event.getY());// 检查按钮
			break;
		case 1:
			state = game.checkClick(event.getX(), event.getY());
			if (state == 1) {
				game.dropCube(event.getX());// 掉下方块
			}
			break;
		case 2:
			state = survivalGame.checkClick(event.getX(), event.getY());
			if (state == 2) {
				survivalGame.dropCube(event.getX());// 掉下方块
			}
			break;
		case 3:
			state = chart.checkClick(event.getX(), event.getY());
			break;
		case 4:
			state = help.checkClick(event.getX(), event.getY());
			break;
		}
		drawCavas();// 刷新
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (state == 0){
				new RecordFile().writeRecord(game, survivalGame, chart);// 保存进度
				return false;
			}
			else {
				state = 0;
				drawCavas();// 刷新
			}
		}
		return true;
	}
	
	//传递分辨率参数
	public void getFBL(float width,float height){
		this.width = width;
		this.height = height;
	}

}
