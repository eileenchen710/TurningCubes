package turningcubes.object;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import turningcubes.support.ScreenDisplay;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * ��Ϸ������ �޾�ģʽ�����ؿ���Ϸ��Ԥѡ�������ޣ��޼�ʱ�мƷ֣� �����С�ɸ�����Ϸ�װ巽�����Զ�������
 */
public class Survival {

	// ��ť
	BitmapButton bBack;
	BitmapButton bPause;
	BitmapButton bRestart;
	Chart chart;

	// �����ؿ���Ϣ
	boolean pauseState = false;// �Ƿ�����ͣ״̬
	int score = 0;// ��Ϸ����
	int scoreOfCube = 10;// ����һ�������û���
	int gridsNum = 7;// ��Ϸ�װ���ط�����
	int cubeType = 5;// ���÷�������
	int cubeRemainder = 3;// �ɵ�������

	// ��Ϸ�װ��������Ͻ�����
	float grids_x;
	float grids_y;
	// Ԥѡ�����������Ͻ�����
	float cubes_x;
	float cubes_y;
	// �������
	float gridsRange = 250;// ��Ϸ�װ���dp
	float gridsInterval;// ������϶
	int nextCubeSide = 20;// Ԥѡ����߳�dp
	int cubesNum = 10;// ��ʾ10��Ԥѡ����

	Cube[][] gameGrids;// ��Ϸ�װ�
	Queue<Cube> nextCubes = new LinkedList<Cube>();// Ԥѡ�������

	public Survival(Context context, Chart chart) {
		super();

		this.chart = chart;
		// ��ť
		bBack = new BitmapButton(Color.GRAY, new RectF(0, 0, 80, 50));
		bPause = new BitmapButton(Color.GRAY, new RectF(0, 60, 80, 110));
		bRestart = new BitmapButton(Color.GRAY, new RectF(0, 120, 80, 170));

		// ���������ʼ��
		gridsRange = ScreenDisplay.dpTopx(context, gridsRange);// תΪpx
		nextCubeSide = ScreenDisplay.dpTopx(context, nextCubeSide);

		gameGrids = new Cube[gridsNum][gridsNum];// ��Ϸ�װ�
		gridsInterval = gridsRange / (gridsNum * 11 - 1);// ������϶,��Ϊ����߳�1/10
		for (int i = 0; i < gridsNum; i++)
			for (int j = 0; j < gridsNum; j++) {
				gameGrids[i][j] = new Cube(gridsInterval * 10);// ʵ����
			}
		for (int i = 0; i < cubesNum; i++) {
			nextCubes.offer(new Cube(nextCubeSide, cubeType));// ʵ����
		}

		gameGrids[gridsNum / 2][gridsNum / 2].setStone(true);// ���װ����ĵķ�����Ϊʯ��
	}

	/**
	 * ���λ����Ļ�ߴ�px
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
		return 2;
	}

	/**
	 * �ı�ѡ������� ������δʵ�ʵ���
	 * 
	 * @param x
	 * @return
	 */
	public boolean selectLine(float x) {
		if (pauseState)
			return false;

		// ���
		for (int i = 0; i < gridsNum; i++) {
			for (int j = 0; j < gridsNum; j++) {
				if (gameGrids[i][j].getAction() == -1) {
					gameGrids[i][j].setAction(0);
					gameGrids[i][j].setBitmap();
				}
			}
		}

		// ����ѡ���б�ɫ
		int i = (int) ((x - grids_x) / (gridsInterval * 10 + gridsInterval));// �жϵ��������һ��
		if (i >= 0 && i < gridsNum) {
			for (int j = 0; j < gridsNum; j++) {
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
		if (i >= 0 && i < gridsNum) {
			int j = 0;// ��¼���е�һ���ǿո������
			while (j < gridsNum && gameGrids[i][j].getAction() == 0) {
				j++;
			}

			// ������δ����
			if (j != 0) {
				// �ƶ�����
				nextCubes.poll();

				nextCubes.offer(new Cube(nextCubeSide, cubeType));// ����Ԥѡ����

				// �����в�Ϊ�� ��ȡ�õķ�������װ�
				if (j != gridsNum) {
					temp.setSide(gridsInterval * 10);
					gameGrids[i][j - 1] = temp;
					clearLine(i, j - 1);// ��������
				} else {
					cubeRemainder--;// �������
					if (cubeRemainder == 0)
						endGame();// ������Ϸ
				}
				turnGrids(true);// ���趨��ת�װ�

				j = 0;// ��¼���е�һ���ǿո������
				for (int k = 0; k >= 0 && k < gridsNum; k++) {
					while (j < gridsNum && gameGrids[k][j].getAction() == 0) {
						j++;
					}
					if (j != 0) {
						j = -1;
						break;
					}
				}
				if (j != -1)// ��ǰ�װ岻�������䷽��
					endGame();// ������Ϸ
			}
		}
	}

	/**
	 * ��Ϸ����
	 */
	void endGame() {
		chart.insert(score, "player");// ���������¼ δʵ��������������� Ĭ��Ϊplayer
		// ���
		score = 0;
		cubeRemainder = 3;
		for (int i = 0; i < gridsNum; i++)
			for (int j = 0; j < gridsNum; j++) {
				gameGrids[i][j] = new Cube(gridsInterval * 10);// ʵ����
			}
		for (int i = 0; i < cubesNum; i++) {
			nextCubes.offer(new Cube(nextCubeSide, cubeType));// ʵ����
		}
		gameGrids[gridsNum / 2][gridsNum / 2].setStone(true);// ���װ����ĵķ�����Ϊʯ��
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

		while ((y + down + 1 < gridsNum)
				&& (action == gameGrids[x][y + down + 1].getAction() || gameGrids[x][y
						+ down + 1].getAction() > 5))
			down++;
		while ((y - up - 1 >= 0)
				&& (action == gameGrids[x][y - up - 1].getAction() || gameGrids[x][y
						- up - 1].getAction() > 5))
			up++;
		while ((x + right + 1 < gridsNum)
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
	}

	/**
	 * ���мӷ�
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public int addScore(int num1, int num2) {
		float factor = 0;
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
	 * ���¿�ʼ
	 */
	public void reset() {
		score = 0;
		cubeRemainder = 3;
		for (int i = 0; i < gridsNum; i++)
			for (int j = 0; j < gridsNum; j++) {
				gameGrids[i][j] = new Cube(gridsInterval * 10);// ʵ����
			}
		nextCubes = new LinkedList<Cube>();
		for (int i = 0; i < cubesNum; i++) {
			nextCubes.offer(new Cube(nextCubeSide, cubeType));// ʵ����
		}

		gameGrids[gridsNum / 2][gridsNum / 2].setStone(true);// ���װ����ĵķ�����Ϊʯ��
	}

	/**
	 * ��ת�װ�
	 * 
	 * @param clockwise
	 */
	public void turnGrids(boolean clockwise) {
		// ���Ƶ�ǰ�װ�
		Cube[][] temp = new Cube[gridsNum][gridsNum];
		for (int i = 0; i < gridsNum; i++)
			for (int j = 0; j < gridsNum; j++) {
				temp[i][j] = gameGrids[i][j].clone();
			}
		// ˳ʱ��(Ĭ��)
		if (clockwise) {
			for (int i = 0; i < gridsNum; i++)
				for (int j = 0; j < gridsNum; j++) {
					gameGrids[i][j] = temp[j][gridsNum - i - 1];
				}
		} else {
			for (int i = 0; i < gridsNum; i++)
				for (int j = 0; j < gridsNum; j++) {
					gameGrids[i][j] = temp[gridsNum - j - 1][i];
				}
		}
	}

	/**
	 * ����Ϸ�з����Ԫ�ػ�� ��View�����
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
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
		for (int i = 0; i < gridsNum; i++) {
			for (int j = 0; j < gridsNum; j++) {
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
			canvas.drawRect(grids_x + gridsNum / 2 * 11 * gridsInterval, y,
					grids_x + (gridsNum / 2 * 11 + 10) * gridsInterval, y
							+ gridsInterval * 10, paint);
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
		canvas.drawText("score:" + score, 40, 260, paint);
		canvas.drawText("�ɵ��䷽��:" + cubeRemainder, 40, 360, paint);
	}

	public boolean isPauseState() {
		return pauseState;
	}

	public void setPauseState(boolean pauseState) {
		this.pauseState = pauseState;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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

	public int getGridsNum() {
		return gridsNum;
	}

	public int getCubeRemainder() {
		return cubeRemainder;
	}

	public void setCubeRemainder(int cubeRemainder) {
		this.cubeRemainder = cubeRemainder;
	}

}
