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
	int state = 0;// ��Ϸ״̬
	MainMenu menu;// ������
	Game game;// ��ͨ��Ϸ
	Survival survivalGame;// �޾�ģʽ
	Chart chart;// ���а�
	Help help;// ����
	RecordFile record;// ��¼

	SurfaceHolder surfaceHolder;
	
	float width;//��Ļ��
	float height;
	
	public Bitmap backgroundMenu1;
	public Bitmap backgroundGame;
	public Bitmap backgroundChart;
	public Bitmap backgroundHelp;
	

	public GameSurfaceView(Context context) {
		super(context);
		// ʵ������Ϸ��
		menu = new MainMenu(context);
		help = new Help(context);
		game = new Game(context);

		survivalGame = new Survival(context, chart);
		chart = new Chart(context,width,height);//������Ļ��С����
		setOnTouchListener(this);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		// ��ȡ��¼
		record = new RecordFile();
		record.readRecord(game);
		record.readRecord(survivalGame);
		record.readRecord(chart);
		// ���ý���
		setFocusable(true);
		setFocusableInTouchMode(true);
		
		//���ñ���
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
		Paint paint=new Paint();  //����
		
        Matrix matrix = new Matrix();//ʹ��matri����ͼ�α任 
        
        float w = ((float) width / backgroundMenu1.getWidth());//��Ļ��ȳ�ͼƬ���  
        float h = ((float) height / backgroundMenu1.getHeight());//��Ļ�߶ȳ�ͼƬ�߶�  
        matrix.postScale(w, h);// ��ȡ���ű���  
        // �������ű�������µ�λͼ  

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
		
	    //	 ������Ϸ״̬���ò�ͬ�Ļ�ͼ����
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
		// ������Ϸ״̬���ò�ͬ����Ӧ����
		switch (state) {
		case 0:
			state = menu.checkClick(event.getX(), event.getY());// ��鰴ť
			break;
		case 1:
			state = game.checkClick(event.getX(), event.getY());
			if (state == 1) {
				game.dropCube(event.getX());// ���·���
			}
			break;
		case 2:
			state = survivalGame.checkClick(event.getX(), event.getY());
			if (state == 2) {
				survivalGame.dropCube(event.getX());// ���·���
			}
			break;
		case 3:
			state = chart.checkClick(event.getX(), event.getY());
			break;
		case 4:
			state = help.checkClick(event.getX(), event.getY());
			break;
		}
		drawCavas();// ˢ��
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (state == 0){
				new RecordFile().writeRecord(game, survivalGame, chart);// �������
				return false;
			}
			else {
				state = 0;
				drawCavas();// ˢ��
			}
		}
		return true;
	}
	
	//���ݷֱ��ʲ���
	public void getFBL(float width,float height){
		this.width = width;
		this.height = height;
	}

}
