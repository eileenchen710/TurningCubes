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
 * 游戏控制类 无尽模式：单关卡游戏，预选方块无限，无计时有计分， 方块大小可根据游戏底板方块数自动调整。
 */
public class Survival {

	// 按钮
	BitmapButton bBack;
	BitmapButton bPause;
	BitmapButton bRestart;
	Chart chart;

	// 基本关卡信息
	boolean pauseState = false;// 是否处于暂停状态
	int score = 0;// 游戏积分
	int scoreOfCube = 10;// 消除一个方块获得积分
	int gridsNum = 7;// 游戏底板边沿方块数
	int cubeType = 5;// 可用方块类型
	int cubeRemainder = 3;// 可掉方块数

	// 游戏底板区域左上角坐标
	float grids_x;
	float grids_y;
	// 预选方块区域左上角坐标
	float cubes_x;
	float cubes_y;
	// 界面参数
	float gridsRange = 250;// 游戏底板宽度dp
	float gridsInterval;// 方格间空隙
	int nextCubeSide = 20;// 预选方块边长dp
	int cubesNum = 10;// 显示10个预选方块

	Cube[][] gameGrids;// 游戏底板
	Queue<Cube> nextCubes = new LinkedList<Cube>();// 预选方块队列

	public Survival(Context context, Chart chart) {
		super();

		this.chart = chart;
		// 按钮
		bBack = new BitmapButton(Color.GRAY, new RectF(0, 0, 80, 50));
		bPause = new BitmapButton(Color.GRAY, new RectF(0, 60, 80, 110));
		bRestart = new BitmapButton(Color.GRAY, new RectF(0, 120, 80, 170));

		// 界面参数初始化
		gridsRange = ScreenDisplay.dpTopx(context, gridsRange);// 转为px
		nextCubeSide = ScreenDisplay.dpTopx(context, nextCubeSide);

		gameGrids = new Cube[gridsNum][gridsNum];// 游戏底板
		gridsInterval = gridsRange / (gridsNum * 11 - 1);// 方格间空隙,设为方格边长1/10
		for (int i = 0; i < gridsNum; i++)
			for (int j = 0; j < gridsNum; j++) {
				gameGrids[i][j] = new Cube(gridsInterval * 10);// 实例化
			}
		for (int i = 0; i < cubesNum; i++) {
			nextCubes.offer(new Cube(nextCubeSide, cubeType));// 实例化
		}

		gameGrids[gridsNum / 2][gridsNum / 2].setStone(true);// 将底板中心的方块设为石块
	}

	/**
	 * 传参获得屏幕尺寸px
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
	 * 检查按钮是否被按下
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
	 * 改变选中列外观 本方法未实际调用
	 * 
	 * @param x
	 * @return
	 */
	public boolean selectLine(float x) {
		if (pauseState)
			return false;

		// 清空
		for (int i = 0; i < gridsNum; i++) {
			for (int j = 0; j < gridsNum; j++) {
				if (gameGrids[i][j].getAction() == -1) {
					gameGrids[i][j].setAction(0);
					gameGrids[i][j].setBitmap();
				}
			}
		}

		// 设置选中列变色
		int i = (int) ((x - grids_x) / (gridsInterval * 10 + gridsInterval));// 判断点击的是哪一列
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
	 * 方块下落 包括预选方块队列的移动操作
	 * 
	 * @param x
	 */
	public void dropCube(float x) {
		if (pauseState)// 暂停中
			return;

		Cube temp = nextCubes.peek();// 取得预选方块队列中的第一个
		if (temp == null) {
			// 无下一个方块
			return;
		}

		int i = (int) ((x - grids_x) / (gridsInterval * 10 + gridsInterval));// 判断点击的是哪一列
		if (i >= 0 && i < gridsNum) {
			int j = 0;// 记录该列第一个非空格的行数
			while (j < gridsNum && gameGrids[i][j].getAction() == 0) {
				j++;
			}

			// 若该列未到顶
			if (j != 0) {
				// 移动队列
				nextCubes.poll();

				nextCubes.offer(new Cube(nextCubeSide, cubeType));// 新增预选方块

				// 若该列不为空 将取得的方块填入底板
				if (j != gridsNum) {
					temp.setSide(gridsInterval * 10);
					gameGrids[i][j - 1] = temp;
					clearLine(i, j - 1);// 尝试消行
				} else {
					cubeRemainder--;// 方块掉落
					if (cubeRemainder == 0)
						endGame();// 结束游戏
				}
				turnGrids(true);// 按设定旋转底板

				j = 0;// 记录该列第一个非空格的行数
				for (int k = 0; k >= 0 && k < gridsNum; k++) {
					while (j < gridsNum && gameGrids[k][j].getAction() == 0) {
						j++;
					}
					if (j != 0) {
						j = -1;
						break;
					}
				}
				if (j != -1)// 当前底板不可再下落方块
					endGame();// 结束游戏
			}
		}
	}

	/**
	 * 游戏结束
	 */
	void endGame() {
		chart.insert(score, "player");// 保存分数记录 未实现输入玩家名功能 默认为player
		// 清空
		score = 0;
		cubeRemainder = 3;
		for (int i = 0; i < gridsNum; i++)
			for (int j = 0; j < gridsNum; j++) {
				gameGrids[i][j] = new Cube(gridsInterval * 10);// 实例化
			}
		for (int i = 0; i < cubesNum; i++) {
			nextCubes.offer(new Cube(nextCubeSide, cubeType));// 实例化
		}
		gameGrids[gridsNum / 2][gridsNum / 2].setStone(true);// 将底板中心的方块设为石块
	}

	/**
	 * 在落下新方块后 尝试进行消行操作
	 * 
	 * @param x
	 * @param y
	 */
	public void clearLine(int x, int y) {
		int left = 0;// 从该方块起向x轴负方向能找到的连续相同方块数
		int right = 0;// x轴正方向
		int up = 0;// y轴负方向
		int down = 0;// y轴正方向
		int action = gameGrids[x][y].getAction();// 匹配方块类型

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

		if (down + up > 1)// 一行有连续三个(或以上)方块
			for (int i = 0 - up; i <= down; i++) {
				gameGrids[x][y + i] = new Cube(gridsInterval * 10);
			}
		if (right + left > 1)// 一列有连续三个(或以上)方块
			for (int i = 0 - left; i <= right; i++) {
				// 已被消除
				if (i == 0 && gameGrids[x + i][y].getAction() == 0)
					continue;
				else {
					gameGrids[x + i][y] = new Cube(gridsInterval * 10);
				}
			}
		// 加分
		score += addScore(down + up, right + left);
	}

	/**
	 * 消行加分
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public int addScore(int num1, int num2) {
		float factor = 0;
		if (num1 == 2)
			factor += 3;
		else if (num1 > 2)// 多于三个分数加0.5
			factor += (3 + (num1 - 2) / 2);
		if (num2 == 2) {
			if (factor != 0)// 避免重复为同一方格加分
				factor--;
			factor += 3;
		} else if (num2 > 2)
			factor += (3 + (num2 - 2) / 2);

		return (int) (factor * scoreOfCube);
	}

	/**
	 * 重新开始
	 */
	public void reset() {
		score = 0;
		cubeRemainder = 3;
		for (int i = 0; i < gridsNum; i++)
			for (int j = 0; j < gridsNum; j++) {
				gameGrids[i][j] = new Cube(gridsInterval * 10);// 实例化
			}
		nextCubes = new LinkedList<Cube>();
		for (int i = 0; i < cubesNum; i++) {
			nextCubes.offer(new Cube(nextCubeSide, cubeType));// 实例化
		}

		gameGrids[gridsNum / 2][gridsNum / 2].setStone(true);// 将底板中心的方块设为石块
	}

	/**
	 * 旋转底板
	 * 
	 * @param clockwise
	 */
	public void turnGrids(boolean clockwise) {
		// 复制当前底板
		Cube[][] temp = new Cube[gridsNum][gridsNum];
		for (int i = 0; i < gridsNum; i++)
			for (int j = 0; j < gridsNum; j++) {
				temp[i][j] = gameGrids[i][j].clone();
			}
		// 顺时针(默认)
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
	 * 将游戏中方块等元素绘出 供View类调用
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		// 按钮
		paint.setColor(bBack.bitmap);
		canvas.drawRect(bBack.getRect(), paint);
		paint.setColor(bPause.bitmap);
		canvas.drawRect(bPause.getRect(), paint);
		paint.setColor(bRestart.bitmap);
		canvas.drawRect(bRestart.getRect(), paint);

		// 游戏底板
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
		// 预选方块
		float x = cubes_x;
		float y = cubes_y;
		Iterator<Cube> itr = nextCubes.iterator();
		Cube temp;
		// 第一个方块
		if (itr.hasNext()) {
			temp = itr.next();
			paint.setColor(temp.getBitmap());
			canvas.drawRect(grids_x + gridsNum / 2 * 11 * gridsInterval, y,
					grids_x + (gridsNum / 2 * 11 + 10) * gridsInterval, y
							+ gridsInterval * 10, paint);
		}
		// 第二个方块
		if (itr.hasNext()) {
			temp = itr.next();
			paint.setColor(temp.getBitmap());
			canvas.drawRect(x - gridsInterval * 5 + nextCubeSide / 2, y, x
					+ gridsInterval * 5 + nextCubeSide / 2,
					y += gridsInterval * 10, paint);
			y += 5;
		}
		// 其余方块
		while (itr.hasNext()) {
			temp = itr.next();
			paint.setColor(temp.getBitmap());
			canvas.drawRect(x, y, x + nextCubeSide, y += nextCubeSide, paint);
			y += 3;
		}

		// 关卡信息
		paint.setColor(Color.RED);
		paint.setStrokeWidth(10);
		canvas.drawText("score:" + score, 40, 260, paint);
		canvas.drawText("可掉落方块:" + cubeRemainder, 40, 360, paint);
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
