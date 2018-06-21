package mobile.turningcubes;

import turningcubes.object.Chart;
import turningcubes.object.Game;
import turningcubes.object.Help;
import turningcubes.object.MainMenu;
import turningcubes.object.Survival;
import turningcubes.support.RecordFile;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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

	public GameSurfaceView(Context context) {
		super(context);
		// 实例化游戏类
		help = new Help(context);
		chart = new Chart(context);
		game = new Game(context);
		survivalGame = new Survival(context, chart);
		menu = new MainMenu(context);
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
		canvas.drawColor(Color.WHITE);
		// 根据游戏状态调用不同的绘图方法
		switch (state) {
		case 0:
			menu.draw(canvas);
			break;
		case 1:
			game.draw(canvas);
			break;
		case 2:
			survivalGame.draw(canvas);
			break;
		case 3:
			chart.draw(canvas);
			break;
		case 4:
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

}
