package turningcubes.object;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import turningcubes.support.ScreenDisplay;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * ��Ϸ������ Ŀǰ���ȣ���ؿ���Ϸ��Ԥѡ�������ޣ��޼�ʱ�мƷ֣� �����С�ɸ�����Ϸ�װ巽�����Զ�������
 */
public class Game {

	// ��ť
	BitmapButton bBack;
	BitmapButton bPause;
	BitmapButton bRestart;
	Context context;
	Bitmap grey;
	Bitmap black;
	Bitmap yellow;
	Bitmap red;
	Bitmap blue;
	Bitmap dkgrey;
	Bitmap magenta;
	Bitmap cyan;
	Bitmap green;
	float w,h;//��Ļ����

	// �����ؿ���Ϣ
	boolean clockWiseState = true;// �װ���ת���� true˳ʱ�� false��ʱ��
	boolean trunState = true;// �װ��Ƿ���ת
	boolean pauseState = false;// �Ƿ�����ͣ״̬
	int levelNum = 9;// �ؿ���-1
	int level = 0;// ��Ϸ�ȼ�
	int score = 0;// ��Ϸ����
	int levelScore = 0;// ������Ϸ����
	int scoreOfCube = 10;// ����һ�������û���
	int demandScore[];// ���ؿ�����Ҫ�����
	int cubesExtraNum[];// ���ؿ����÷�����(������Ԥ������10��)
	int gridsNum[];// ��Ϸ�װ���ط�����
	int usedCubes = 0;// ������Ϸ���÷�����(������Ԥ������10��)
	int cubeType[];// ���÷�������

	// ��Ϸ�װ��������Ͻ�����
	float grids_x;
	float grids_y;
	// Ԥѡ�����������Ͻ�����
	float cubes_x;
	float cubes_y;
	// �������
	float gridsRange = 375;// ��Ϸ�װ���dp
	float gridsInterval;// ������϶
	int nextCubeSide = 30;// Ԥѡ����߳�dp
	int cubesNum = 10;// ��ʾ10��Ԥѡ����

	Cube[][] gameGrids;// ��Ϸ�װ�
	Queue<Cube> nextCubes = new LinkedList<Cube>();// Ԥѡ�������

	public Game(Context context) {
		super();
		this.context = context;

		// ���ؿ���Ϣ��ʼ��
		demandScore = new int[] { 5, 10, 20, 30, 40, 50, 60, 70, 80, 90 };// ����
		cubesExtraNum = new int[levelNum];
		gridsNum = new int[] { 7, 7, 7, 9, 9, 9, 9, 11, 11, 11 };
		cubeType = new int[] { 8, 5, 5, 5, 6, 7, 7, 8, 8, 8 };
		// תΪ����
		for (int i = 0; i < levelNum; i++) {
			cubesExtraNum[i] = (int) (demandScore[i] * 3.5 - 10);
			demandScore[i] = demandScore[i] * scoreOfCube;
			if (i != 0)
				demandScore[i] += demandScore[i - 1];
		}

		// ���������ʼ��
		gridsRange = ScreenDisplay.dpTopx(context, gridsRange);// תΪpx
		nextCubeSide = ScreenDisplay.dpTopx(context, nextCubeSide);

		gameGrids = new Cube[gridsNum[level]][gridsNum[level]];// ��Ϸ�װ�
		gridsInterval = gridsRange / (gridsNum[level] * 11 - 1);// ������϶,��Ϊ����߳�1/10

		for (int i = 0; i < gridsNum[level]; i++)
			for (int j = 0; j < gridsNum[level]; j++) {
				gameGrids[i][j] = new Cube(gridsInterval * 10);// ʵ����
			}
		for (int i = 0; i < cubesNum; i++) {
			nextCubes.offer(new Cube(nextCubeSide, cubeType[level]));// ʵ����
		}

		gameGrids[gridsNum[level] / 2][gridsNum[level] / 2].setStone(true);// ���װ����ĵķ�����Ϊʯ��
	}
	
	
	public void draw(Canvas canvas){
		draw(canvas,context);
	}
	public void draw(Canvas canvas,Context context){
		// ��ť
		bBack = new BitmapButton(Color.GRAY, new RectF(0, 0, 160, 50));
		bPause = new BitmapButton(Color.GRAY, new RectF(0, 60, 160, 110));
		bRestart = new BitmapButton(Color.GRAY, new RectF(0, 120, 160, 170));
		bBack.bitmapPic = BitmapFactory.decodeResource(context.getResources(),mobile.turningcubes.R.drawable.bback);
		bPause.bitmapPic = BitmapFactory.decodeResource(context.getResources(),mobile.turningcubes.R.drawable.bpause);
		bRestart.bitmapPic = BitmapFactory.decodeResource(context.getResources(),mobile.turningcubes.R.drawable.brestart);


		Paint paint = new Paint();
		// ��ť
		paint.setColor(bBack.bitmap);
	//	canvas.drawRect(bBack.getRect(), paint);
		canvas.drawBitmap(bBack.bitmapPic, 0 , 0 , paint);
		paint.setColor(bPause.bitmap);
	//	canvas.drawRect(bPause.getRect(), paint);
		canvas.drawBitmap(bPause.bitmapPic, 0 , 60 , paint);
		paint.setColor(bRestart.bitmap);
	//	canvas.drawRect(bRestart.getRect(), paint);
		canvas.drawBitmap(bRestart.bitmapPic, 0 , 120 , paint);

		black = BitmapFactory.decodeResource
				(context.getResources(),mobile.turningcubes.R.drawable.black);
		grey = BitmapFactory.decodeResource
					(context.getResources(),mobile.turningcubes.R.drawable.xxx);
		yellow = BitmapFactory.decodeResource
				(context.getResources(),mobile.turningcubes.R.drawable.yellow);
		cyan = BitmapFactory.decodeResource
				(context.getResources(),mobile.turningcubes.R.drawable.cyan);
		green = BitmapFactory.decodeResource
				(context.getResources(),mobile.turningcubes.R.drawable.green);
		blue = BitmapFactory.decodeResource
				(context.getResources(),mobile.turningcubes.R.drawable.blue);
		red = BitmapFactory.decodeResource
				(context.getResources(),mobile.turningcubes.R.drawable.red);
		magenta = BitmapFactory.decodeResource
				(context.getResources(),mobile.turningcubes.R.drawable.magenta);
		dkgrey = BitmapFactory.decodeResource
				(context.getResources(),mobile.turningcubes.R.drawable.dkgrey);
		// ��Ϸ�װ�
		for (int i = 0; i < gridsNum[level]; i++) {
			for (int j = 0; j < gridsNum[level]; j++) {
			//	paint.setColor(gameGrids[i][j].getBitmap());
				int x = gameGrids[i][j].getBitmap();//�����жϷ������ɫ
				switch (x) {
				case Color.DKGRAY:// ѡ���еķ���
					canvas.drawBitmap(dkgrey,grids_x
							+ (gameGrids[i][j].getSide() + gridsInterval) * i,
							grids_y + (gameGrids[i][j].getSide() + gridsInterval)
									* j , paint);
					break;
				case Color.GRAY:// �װ巽��
					//paint.setAlpha(200);
					canvas.drawBitmap(grey, grids_x
							+ (gameGrids[i][j].getSide() + gridsInterval) * i,
							grids_y + (gameGrids[i][j].getSide() + gridsInterval)
									* j, paint);
					//paint.setAlpha(255);
					break;
				case Color.BLACK:// ʯ�鷽��
					canvas.drawBitmap(black,grids_x
							+ (gameGrids[i][j].getSide() + gridsInterval) * i,
							grids_y + (gameGrids[i][j].getSide() + gridsInterval)
									* j , paint);
					break;
				case Color.BLUE:
					canvas.drawBitmap(blue, grids_x
							+ (gameGrids[i][j].getSide() + gridsInterval) * i,
							grids_y + (gameGrids[i][j].getSide() + gridsInterval)
									* j, paint);
					break;
				case Color.CYAN:
					canvas.drawBitmap(cyan, grids_x
							+ (gameGrids[i][j].getSide() + gridsInterval) * i,
							grids_y + (gameGrids[i][j].getSide() + gridsInterval)
									* j , paint);
					break;
				case Color.GREEN:
					canvas.drawBitmap(green,grids_x
							+ (gameGrids[i][j].getSide() + gridsInterval) * i,
							grids_y + (gameGrids[i][j].getSide() + gridsInterval)
									* j, paint);
					break;
				case Color.YELLOW:
					canvas.drawBitmap(yellow, grids_x
							+ (gameGrids[i][j].getSide() + gridsInterval) * i,
							grids_y + (gameGrids[i][j].getSide() + gridsInterval)
									* j , paint);
					break;
				case Color.MAGENTA:// ���ⷽ�� ͣתһ��
					canvas.drawBitmap(magenta,grids_x
							+ (gameGrids[i][j].getSide() + gridsInterval) * i,
							grids_y + (gameGrids[i][j].getSide() + gridsInterval)
									* j , paint);
					break;
				case Color.RED:// ���ⷽ�� ��ת������ת
					canvas.drawBitmap(red, grids_x
							+ (gameGrids[i][j].getSide() + gridsInterval) * i,
							grids_y + (gameGrids[i][j].getSide() + gridsInterval)
									* j , paint);
					break;
				}
			}
		}
		// Ԥѡ����
		float x = cubes_x;
		float y = cubes_y;
		Iterator<Cube> itr = nextCubes.iterator();
		Cube temp;
		
		// ��һ������
		if (itr.hasNext()) {
			temp = itr.next();
			//paint.setColor(temp.getBitmap());
			int yuxuan1 = temp.getBitmap();//�����жϷ������ɫ
			switch (yuxuan1) {
			case Color.DKGRAY:// ѡ���еķ���
				canvas.drawBitmap(dkgrey,grids_x + gridsNum[level] / 2 * 11 * gridsInterval,
							y, paint);
				break;
			case Color.GRAY:// �װ巽��
				canvas.drawBitmap(grey, grids_x + gridsNum[level] / 2 * 11 * gridsInterval,
						y, paint);
				break;
			case Color.BLACK:// ʯ�鷽��
				canvas.drawBitmap(black,grids_x + gridsNum[level] / 2 * 11 * gridsInterval,
						y, paint);
				break;
			case Color.BLUE:
				canvas.drawBitmap(blue, grids_x + gridsNum[level] / 2 * 11 * gridsInterval,
						y, paint);
				break;
			case Color.CYAN:
				canvas.drawBitmap(cyan,  grids_x + gridsNum[level] / 2 * 11 * gridsInterval,
						y, paint);
				break;
			case Color.GREEN:
				canvas.drawBitmap(green, grids_x + gridsNum[level] / 2 * 11 * gridsInterval,
						y, paint);
				break;
			case Color.YELLOW:
				canvas.drawBitmap(yellow,  grids_x + gridsNum[level] / 2 * 11 * gridsInterval,
						y, paint);
				break;
			case Color.MAGENTA:// ���ⷽ�� ͣתһ��
				canvas.drawBitmap(magenta, grids_x + gridsNum[level] / 2 * 11 * gridsInterval,
						y, paint);
				break;
			case Color.RED:// ���ⷽ�� ��ת������ת
				canvas.drawBitmap(red,  grids_x + gridsNum[level] / 2 * 11 * gridsInterval,
						y, paint);
				break;
			}
			//canvas.drawRect(grids_x + gridsNum[level] / 2 * 11 * gridsInterval,
				//	y, grids_x + (gridsNum[level] / 2 * 11 + 10)
					//		* gridsInterval, y + gridsInterval * 10, paint);
		}
		// �ڶ�������
		if (itr.hasNext()) {
			temp = itr.next();
			int yuxuan2 = temp.getBitmap();//�����жϷ������ɫ
			switch (yuxuan2) {
			case Color.DKGRAY:// ѡ���еķ���
				canvas.drawBitmap(dkgrey,x - gridsInterval * 5 + nextCubeSide / 2,
							y, paint);
				break;
			case Color.GRAY:// �װ巽��
				canvas.drawBitmap(grey, x - gridsInterval * 5 + nextCubeSide / 2,
						y, paint);
				break;
			case Color.BLACK:// ʯ�鷽��
				canvas.drawBitmap(black,x - gridsInterval * 5 + nextCubeSide / 2,
						y, paint);
				break;
			case Color.BLUE:
				canvas.drawBitmap(blue, x - gridsInterval * 5 + nextCubeSide / 2,
						y, paint);
				break;
			case Color.CYAN:
				canvas.drawBitmap(cyan, x - gridsInterval * 5 + nextCubeSide / 2,
						y, paint);
				break;
			case Color.GREEN:
				canvas.drawBitmap(green,x - gridsInterval * 5 + nextCubeSide / 2,
						y, paint);
				break;
			case Color.YELLOW:
				canvas.drawBitmap(yellow, x - gridsInterval * 5 + nextCubeSide / 2,
						y, paint);
				break;
			case Color.MAGENTA:// ���ⷽ�� ͣתһ��
				canvas.drawBitmap(magenta,x - gridsInterval * 5 + nextCubeSide / 2,
						y, paint);
				break;
			case Color.RED:// ���ⷽ�� ��ת������ת
				canvas.drawBitmap(red,x - gridsInterval * 5 + nextCubeSide / 2,
						y, paint);
				break;
			}
			//paint.setColor(temp.getBitmap());
			//canvas.drawRect(x - gridsInterval * 5 + nextCubeSide / 2, y, x
				//	+ gridsInterval * 5 + nextCubeSide / 2,
					//y += gridsInterval * 10, paint);
			y += 50;
		}
		// ���෽��
		while (itr.hasNext()) {
			temp = itr.next();
			//paint.setColor(temp.getBitmap());
			//canvas.drawRect(x, y, x + nextCubeSide, y += nextCubeSide, paint);
			int shengxia = temp.getBitmap();//�����жϷ������ɫ
			switch (shengxia) {
			case Color.DKGRAY:// ѡ���еķ���
				canvas.drawBitmap(dkgrey,x,
							y + nextCubeSide, paint);
				break;
			case Color.GRAY:// �װ巽��
				canvas.drawBitmap(grey, x,
						y + nextCubeSide, paint);
				break;
			case Color.BLACK:// ʯ�鷽��
				canvas.drawBitmap(black,x,
						y + nextCubeSide, paint);
				break;
			case Color.BLUE:
				canvas.drawBitmap(blue, x,
						y + nextCubeSide, paint);
				break;
			case Color.CYAN:
				canvas.drawBitmap(cyan, x,
						y + nextCubeSide, paint);
				break;
			case Color.GREEN:
				canvas.drawBitmap(green,x,
						y + nextCubeSide, paint);
				break;
			case Color.YELLOW:
				canvas.drawBitmap(yellow, x,
						y + nextCubeSide, paint);
				break;
			case Color.MAGENTA:// ���ⷽ�� ͣתһ��
				canvas.drawBitmap(magenta,x,
						y + nextCubeSide, paint);
				break;
			case Color.RED:// ���ⷽ�� ��ת������ת
				canvas.drawBitmap(red,x,
						y + nextCubeSide, paint);
				break;
			}
			y += 50;
		}

		// �ؿ���Ϣ
		paint.setColor(Color.RED);
		paint.setStrokeWidth(10);
		canvas.drawText("���ط���:" + demandScore[level], 40, 200, paint);
		canvas.drawText("score:" + score, 40, 220, paint);
		canvas.drawText("level:" + (level + 1), 40, 240, paint);
	}
	
	

	/**
	 * ���λ����Ļ�ߴ�px�����ý������
	 * 
	 * @param width
	 * @param height
	 */
	public void setGridsXY(float width, float height) {
		cubes_x = (float) (width - nextCubeSide * 2.5);
		cubes_y = gridsInterval * 3;
		this.grids_x = (width - gridsRange) / 2;
		this.grids_y = cubes_y + gridsInterval * 12;
	}

	/**
	 * ��鰴ť�Ƿ񱻰���
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int checkClick(float x, float y) {
		if (bPause.checkContain(x, y)) {
			pauseState = !pauseState;
			if (pauseState)
				bPause.setBitmap(Color.BLUE);
			else
				bPause.setBitmap(Color.GRAY);
		}
		if (bBack.checkContain(x, y))
			return 0;
		if (bRestart.checkContain(x, y)) {
			reset();
		}
		return 1;
	}

	/**
	 * �ı�ѡ������� ������δʹ��
	 * 
	 * @param x
	 * @return
	 */
	public boolean selectLine(float x) {
		if (pauseState)
			return false;

		// ���
		for (int i = 0; i < gridsNum[level]; i++) {
			for (int j = 0; j < gridsNum[level]; j++) {
				if (gameGrids[i][j].getAction() == -1) {
					gameGrids[i][j].setAction(0);
					gameGrids[i][j].setBitmap();
				}
			}
		}

		// ����ѡ���б�ɫ
		int i = (int) ((x - grids_x) / (gridsInterval * 10 + gridsInterval));// �жϵ��������һ��
		if (i >= 0 && i < gridsNum[level]) {
			for (int j = 0; j < gridsNum[level]; j++) {
				if (gameGrids[i][j].getAction() == 0) {
					gameGrids[i][j].setAction(-1);
					gameGrids[i][j].setBitmap();
				} else if (gameGrids[i][j].getAction() != 1)
					return true;
			}
			return true;
		}
		return false;
	}

	/**
	 * �������º󷢶�����~
	 * 
	 * @param cube
	 */
	public void cubeActs(Cube cube) {
		switch (cube.getAction()) {
		case 6:// ���ⷽ�� ͣתһ��
			trunState = false;
			break;
		case 7:// ���ⷽ�� ��ת������ת
			clockWiseState = !clockWiseState;
			break;
		case 8:// ���ⷽ�� ��ȷ��
			if (new Random().nextFloat() < 0.5)
				trunState = false;
			else
				clockWiseState = !clockWiseState;
			break;
		}
	}

	/**
	 * �������� ����Ԥѡ������е��ƶ�����
	 * 
	 * @param x
	 */
	public void dropCube(float x) {
		if (pauseState)// ��ͣ��
			return;

		Cube temp = nextCubes.peek();// ȡ��Ԥѡ��������еĵ�һ��
		if (temp == null) {
			// ����һ������
			return;
		}

		int i = (int) ((x - grids_x) / (gridsInterval * 10 + gridsInterval));// �жϵ��������һ��
		if (i >= 0 && i < gridsNum[level]) {
			int j = 0;// ��¼���е�һ���ǿո������
			while (j < gridsNum[level] && gameGrids[i][j].getAction() == 0) {
				j++;
			}

			// ������δ����
			if (j != 0) {
				// �ƶ�����
				nextCubes.poll();
				// ���п��÷���
				if (usedCubes != cubesExtraNum[level]) {
					nextCubes.offer(new Cube(nextCubeSide, cubeType[level]));// ����Ԥѡ����
					usedCubes++;
				}
				// �����в�Ϊ�� ��ȡ�õķ�������װ�
				if (j != gridsNum[level]) {
					temp.setSide(gridsInterval * 10);
					gameGrids[i][j - 1] = temp;
					cubeActs(temp);// ��������~
					clearLine(i, j - 1);// ��������
				}
				turnGrids(clockWiseState);// ���趨��ת�װ�
				// �ж��ܷ������Ϸ
				j = 0;// ��¼ÿ�е�һ���ǿո������
				for (int k = 0; k >= 0 && k < gridsNum[level]; k++) {
					while (j < gridsNum[level]
							&& gameGrids[k][j].getAction() == 0) {
						j++;
					}
					// �����п��Է��÷���
					if (j != 0) {
						j = -1;
						break;
					}
				}
				if (j != -1)// ��ǰ�װ岻�������䷽�� ��Ϸ����
					levelEnd(score >= demandScore[level]);

			}

			// ȡ��Ԥѡ��������еĵ�һ��
			if (nextCubes.peek() == null) {
				// ����һ������ ��Ϸ����
				levelEnd(score >= demandScore[level]);
			}
		}
	}

	/**
	 * �������·���� ���Խ������в���
	 * 
	 * @param x
	 * @param y
	 */
	public void clearLine(int x, int y) {
		int left = 0;// �Ӹ÷�������x�Ḻ�������ҵ���������ͬ������
		int right = 0;// x��������
		int up = 0;// y�Ḻ����
		int down = 0;// y��������
		int action = gameGrids[x][y].getAction();// ƥ�䷽������

		while ((y + down + 1 < gridsNum[level])
				&& (action == gameGrids[x][y + down + 1].getAction() || gameGrids[x][y
						+ down + 1].getAction() > 5))
			down++;
		while ((y - up - 1 >= 0)
				&& (action == gameGrids[x][y - up - 1].getAction() || gameGrids[x][y
						- up - 1].getAction() > 5))
			up++;
		while ((x + right + 1 < gridsNum[level])
				&& (action == gameGrids[x + right + 1][y].getAction() || gameGrids[x
						+ right + 1][y].getAction() > 5))
			right++;
		while ((x - left - 1 >= 0)
				&& (action == gameGrids[x - left - 1][y].getAction() || gameGrids[x
						- left - 1][y].getAction() > 5))
			left++;

		if (down + up > 1)// һ������������(������)����
			for (int i = 0 - up; i <= down; i++) {
				gameGrids[x][y + i] = new Cube(gridsInterval * 10);
			}
		if (right + left > 1)// һ������������(������)����
			for (int i = 0 - left; i <= right; i++) {
				// �ѱ�����
				if (i == 0 && gameGrids[x + i][y].getAction() == 0)
					continue;
				else {
					gameGrids[x + i][y] = new Cube(gridsInterval * 10);
				}
			}
		// �ӷ�
		score += addScore(down + up, right + left);
		levelScore += addScore(down + up, right + left);
	}

	/**
	 * ���мӷ�
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public int addScore(int num1, int num2) {
		float factor = 0;//��ȥ�ķ�����
		if (num1 == 2)
			factor += 3;
		else if (num1 > 2)// ��������������0.5
			factor += (3 + (num1 - 2) / 2);
		if (num2 == 2) {
			if (factor != 0)// �����ظ�Ϊͬһ����ӷ�
				factor--;
			factor += 3;
		} else if (num2 > 2)
			factor += (3 + (num2 - 2) / 2);

		return (int) (factor * scoreOfCube);
	}

	/**
	 * ���¿�ʼ��Ϸ
	 */
	public void reset() {
		usedCubes = 0;
		level = 0;
		score = 0;
		levelScore = 0;
		for (int i = 0; i < gridsNum[level]; i++)
			for (int j = 0; j < gridsNum[level]; j++) {
				gameGrids[i][j] = new Cube(gridsInterval * 10);// ʵ����
			}
		nextCubes = new LinkedList<Cube>();
		for (int i = 0; i < cubesNum; i++) {
			nextCubes.offer(new Cube(nextCubeSide, cubeType[level]));// ʵ����
		}

		gameGrids[gridsNum[level] / 2][gridsNum[level] / 2].setStone(true);// ���װ����ĵķ�����Ϊʯ��
	}

	/**
	 * �ؿ�����
	 */
	public void levelEnd(boolean pass) {
		// ����
		if (pass) {
			if (level < levelNum)
				level++;
			else
				level = 0;

		} else {
			score -= levelScore;// ��ձ��λ�÷���
		}

		// ���
		levelScore = 0;
		usedCubes = 0;
		for (int i = 0; i < gridsNum[level]; i++)
			for (int j = 0; j < gridsNum[level]; j++) {
				gameGrids[i][j] = new Cube(gridsInterval * 10);// ʵ����
			}
		for (int i = 0; i < cubesNum; i++) {
			nextCubes.offer(new Cube(nextCubeSide, cubeType[level]));// ʵ����
		}

		gameGrids[gridsNum[level] / 2][gridsNum[level] / 2].setStone(true);// ���װ����ĵķ�����Ϊʯ��
	}

	/**
	 * ��ת�װ�
	 * 
	 * @param clockwise
	 */
	public void turnGrids(boolean clockwise) {
		// ������ת
		if (!trunState) {
			trunState = !trunState;// ��ԭ
			return;
		}

		// ���Ƶ�ǰ�װ�
		Cube[][] temp = new Cube[gridsNum[level]][gridsNum[level]];
		for (int i = 0; i < gridsNum[level]; i++)
			for (int j = 0; j < gridsNum[level]; j++) {
				temp[i][j] = gameGrids[i][j].clone();
			}
		// ˳ʱ��
		if (clockwise) {
			for (int i = 0; i < gridsNum[level]; i++)
				for (int j = 0; j < gridsNum[level]; j++) {
					gameGrids[i][j] = temp[j][gridsNum[level] - i - 1];
				}
		} else {
			for (int i = 0; i < gridsNum[level]; i++)
				for (int j = 0; j < gridsNum[level]; j++) {
					gameGrids[i][j] = temp[gridsNum[level] - j - 1][i];
				}
		}
	}

	/**
	 * ����Ϸ�з����Ԫ�ػ�� ��View�����
	 * 
	 * @param canvas
	 */
	public void draw1(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		// ��ť
		paint.setColor(bBack.bitmap);
		canvas.drawRect(bBack.getRect(), paint);
		paint.setColor(bPause.bitmap);
		canvas.drawRect(bPause.getRect(), paint);
		paint.setColor(bRestart.bitmap);
		canvas.drawRect(bRestart.getRect(), paint);

		// ��Ϸ�װ�
		for (int i = 0; i < gridsNum[level]; i++) {
			for (int j = 0; j < gridsNum[level]; j++) {
				paint.setColor(gameGrids[i][j].getBitmap());
				canvas.drawRect(grids_x
						+ (gameGrids[i][j].getSide() + gridsInterval) * i,
						grids_y + (gameGrids[i][j].getSide() + gridsInterval)
								* j, grids_x
								+ (gameGrids[i][j].getSide() + gridsInterval)
								* i + gameGrids[i][j].getSide(), grids_y
								+ (gameGrids[i][j].getSide() + gridsInterval)
								* j + gameGrids[i][j].getSide(), paint);
			}
		}
		// Ԥѡ����
		float x = cubes_x;
		float y = cubes_y;
		Iterator<Cube> itr = nextCubes.iterator();
		Cube temp;
		// ��һ������
		if (itr.hasNext()) {
			temp = itr.next();
			paint.setColor(temp.getBitmap());
			canvas.drawRect(grids_x + gridsNum[level] / 2 * 11 * gridsInterval,
					y, grids_x + (gridsNum[level] / 2 * 11 + 10)
							* gridsInterval, y + gridsInterval * 10, paint);
		}
		// �ڶ�������
		if (itr.hasNext()) {
			temp = itr.next();
			paint.setColor(temp.getBitmap());
			canvas.drawRect(x - gridsInterval * 5 + nextCubeSide / 2, y, x
					+ gridsInterval * 5 + nextCubeSide / 2,
					y += gridsInterval * 10, paint);
			y += 5;
		}
		// ���෽��
		while (itr.hasNext()) {
			temp = itr.next();
			paint.setColor(temp.getBitmap());
			canvas.drawRect(x, y, x + nextCubeSide, y += nextCubeSide, paint);
			y += 3;
		}

		// �ؿ���Ϣ
		paint.setColor(Color.RED);
		paint.setStrokeWidth(10);
		canvas.drawText("���ط���:" + demandScore[level], 40, 240, paint);
		canvas.drawText("score:" + score, 40, 260, paint);
		canvas.drawText("level:" + (level + 1), 40, 280, paint);
	}

	public int getLevelScore() {
		return levelScore;
	}

	public void setLevelScore(int levelScore) {
		this.levelScore = levelScore;
	}

	public boolean isClockWiseState() {
		return clockWiseState;
	}

	public void setClockWiseState(boolean clockWiseState) {
		this.clockWiseState = clockWiseState;
	}

	public boolean isTrunState() {
		return trunState;
	}

	public void setTrunState(boolean trunState) {
		this.trunState = trunState;
	}

	public boolean isPauseState() {
		return pauseState;
	}

	public void setPauseState(boolean pauseState) {
		this.pauseState = pauseState;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getUsedCubes() {
		return usedCubes;
	}

	public float getGridsInterval() {
		return gridsInterval;
	}

	public void setGridsInterval(float gridsInterval) {
		this.gridsInterval = gridsInterval;
	}

	public void setUsedCubes(int usedCubes) {
		this.usedCubes = usedCubes;
	}

	public int getGridsNum(int level) {
		return gridsNum[level];
	}

	public Cube[][] getGameGrids() {
		return gameGrids;
	}

	public void setGameGrids(Cube[][] gameGrids) {
		this.gameGrids = gameGrids;
	}

	public Queue<Cube> getNextCubes() {
		return nextCubes;
	}

	public void setNextCubes(Queue<Cube> nextCubes) {
		this.nextCubes = nextCubes;
	}
}
