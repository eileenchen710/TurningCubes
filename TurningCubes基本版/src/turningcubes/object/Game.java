package turningcubes.object;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import turningcubes.support.ScreenDisplay;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 游戏控制类 目前进度：多关卡游戏，预选方块有限，无计时有计分， 方块大小可根据游戏底板方块数自动调整。
 */
public class Game {

	// 按钮
	BitmapButton bBack;
	BitmapButton bPause;
	BitmapButton bRestart;

	// 基本关卡信息
	boolean clockWiseState = true;// 底板旋转方向 true顺时针 false逆时针
	boolean trunState = true;// 底板是否旋转
	boolean pauseState = false;// 是否处于暂停状态
	int levelNum = 9;// 关卡数-1
	int level = 0;// 游戏等级
	int score = 0;// 游戏积分
	int levelScore = 0;// 单次游戏积分
	int scoreOfCube = 10;// 消除一个方块获得积分
	int demandScore[];// 各关卡过关要求积分
	int cubesExtraNum[];// 各关卡可用方块数(不包括预创建的10个)
	int gridsNum[];// 游戏底板边沿方块数
	int usedCubes = 0;// 本次游戏已用方块数(不包括预创建的10个)
	int cubeType[];// 可用方块类型

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

	public Game(Context context) {
		super();

		// 按钮
		bBack = new BitmapButton(Color.GRAY, new RectF(0, 0, 80, 50));
		bPause = new BitmapButton(Color.GRAY, new RectF(0, 60, 80, 110));
		bRestart = new BitmapButton(Color.GRAY, new RectF(0, 120, 80, 170));

		// 各关卡信息初始化
		demandScore = new int[] { 5, 10, 20, 30, 40, 50, 60, 70, 80, 90 };// 个数
		cubesExtraNum = new int[levelNum];
		gridsNum = new int[] { 7, 7, 7, 9, 9, 9, 9, 11, 11, 11 };
		cubeType = new int[] { 8, 5, 5, 5, 6, 7, 7, 8, 8, 8 };
		// 转为分数
		for (int i = 0; i < levelNum; i++) {
			cubesExtraNum[i] = (int) (demandScore[i] * 3.5 - 10);
			demandScore[i] = demandScore[i] * scoreOfCube;
			if (i != 0)
				demandScore[i] += demandScore[i - 1];
		}

		// 界面参数初始化
		gridsRange = ScreenDisplay.dpTopx(context, gridsRange);// 转为px
		nextCubeSide = ScreenDisplay.dpTopx(context, nextCubeSide);

		gameGrids = new Cube[gridsNum[level]][gridsNum[level]];// 游戏底板
		gridsInterval = gridsRange / (gridsNum[level] * 11 - 1);// 方格间空隙,设为方格边长1/10
		for (int i = 0; i < gridsNum[level]; i++)
			for (int j = 0; j < gridsNum[level]; j++) {
				gameGrids[i][j] = new Cube(gridsInterval * 10);// 实例化
			}
		for (int i = 0; i < cubesNum; i++) {
			nextCubes.offer(new Cube(nextCubeSide, cubeType[level]));// 实例化
		}

		gameGrids[gridsNum[level] / 2][gridsNum[level] / 2].setStone(true);// 将底板中心的方块设为石块
	}

	/**
	 * 传参获得屏幕尺寸px并设置界面参数
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
		return 1;
	}

	/**
	 * 改变选中列外观 本方法未使用
	 * 
	 * @param x
	 * @return
	 */
	public boolean selectLine(float x) {
		if (pauseState)
			return false;

		// 清空
		for (int i = 0; i < gridsNum[level]; i++) {
			for (int j = 0; j < gridsNum[level]; j++) {
				if (gameGrids[i][j].getAction() == -1) {
					gameGrids[i][j].setAction(0);
					gameGrids[i][j].setBitmap();
				}
			}
		}

		// 设置选中列变色
		int i = (int) ((x - grids_x) / (gridsInterval * 10 + gridsInterval));// 判断点击的是哪一列
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
	 * 方块落下后发动技能~
	 * 
	 * @param cube
	 */
	public void cubeActs(Cube cube) {
		switch (cube.getAction()) {
		case 6:// 特殊方块 停转一次
			trunState = false;
			break;
		case 7:// 特殊方块 旋转方向逆转
			clockWiseState = !clockWiseState;
			break;
		case 8:// 特殊方块 不确定
			if (new Random().nextFloat() < 0.5)
				trunState = false;
			else
				clockWiseState = !clockWiseState;
			break;
		}
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
		if (i >= 0 && i < gridsNum[level]) {
			int j = 0;// 记录该列第一个非空格的行数
			while (j < gridsNum[level] && gameGrids[i][j].getAction() == 0) {
				j++;
			}

			// 若该列未到顶
			if (j != 0) {
				// 移动队列
				nextCubes.poll();
				// 还有可用方块
				if (usedCubes != cubesExtraNum[level]) {
					nextCubes.offer(new Cube(nextCubeSide, cubeType[level]));// 新增预选方块
					usedCubes++;
				}
				// 若该列不为空 将取得的方块填入底板
				if (j != gridsNum[level]) {
					temp.setSide(gridsInterval * 10);
					gameGrids[i][j - 1] = temp;
					cubeActs(temp);// 发动技能~
					clearLine(i, j - 1);// 尝试消行
				}
				turnGrids(clockWiseState);// 按设定旋转底板
				// 判断能否继续游戏
				j = 0;// 记录每列第一个非空格的行数
				for (int k = 0; k >= 0 && k < gridsNum[level]; k++) {
					while (j < gridsNum[level]
							&& gameGrids[k][j].getAction() == 0) {
						j++;
					}
					// 还有列可以放置方块
					if (j != 0) {
						j = -1;
						break;
					}
				}
				if (j != -1)// 当前底板不可再下落方块 游戏结束
					levelEnd(score >= demandScore[level]);

			}

			// 取得预选方块队列中的第一个
			if (nextCubes.peek() == null) {
				// 无下一个方块 游戏结束
				levelEnd(score >= demandScore[level]);
			}
		}
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

		if (down + up > 1)// 一列有连续三个(或以上)方块
			for (int i = 0 - up; i <= down; i++) {
				gameGrids[x][y + i] = new Cube(gridsInterval * 10);
			}
		if (right + left > 1)// 一行有连续三个(或以上)方块
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
		levelScore += addScore(down + up, right + left);
	}

	/**
	 * 消行加分
	 * 
	 * @param num1
	 * @param num2
	 * @return
	 */
	public int addScore(int num1, int num2) {
		float factor = 0;//消去的方块数
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
	 * 重新开始游戏
	 */
	public void reset() {
		usedCubes = 0;
		level = 0;
		score = 0;
		levelScore = 0;
		for (int i = 0; i < gridsNum[level]; i++)
			for (int j = 0; j < gridsNum[level]; j++) {
				gameGrids[i][j] = new Cube(gridsInterval * 10);// 实例化
			}
		nextCubes = new LinkedList<Cube>();
		for (int i = 0; i < cubesNum; i++) {
			nextCubes.offer(new Cube(nextCubeSide, cubeType[level]));// 实例化
		}

		gameGrids[gridsNum[level] / 2][gridsNum[level] / 2].setStone(true);// 将底板中心的方块设为石块
	}

	/**
	 * 关卡结束
	 */
	public void levelEnd(boolean pass) {
		// 过关
		if (pass) {
			if (level < levelNum)
				level++;
			else
				level = 0;

		} else {
			score -= levelScore;// 清空本次获得分数
		}

		// 清空
		levelScore = 0;
		usedCubes = 0;
		for (int i = 0; i < gridsNum[level]; i++)
			for (int j = 0; j < gridsNum[level]; j++) {
				gameGrids[i][j] = new Cube(gridsInterval * 10);// 实例化
			}
		for (int i = 0; i < cubesNum; i++) {
			nextCubes.offer(new Cube(nextCubeSide, cubeType[level]));// 实例化
		}

		gameGrids[gridsNum[level] / 2][gridsNum[level] / 2].setStone(true);// 将底板中心的方块设为石块
	}

	/**
	 * 旋转底板
	 * 
	 * @param clockwise
	 */
	public void turnGrids(boolean clockwise) {
		// 不可旋转
		if (!trunState) {
			trunState = !trunState;// 还原
			return;
		}

		// 复制当前底板
		Cube[][] temp = new Cube[gridsNum[level]][gridsNum[level]];
		for (int i = 0; i < gridsNum[level]; i++)
			for (int j = 0; j < gridsNum[level]; j++) {
				temp[i][j] = gameGrids[i][j].clone();
			}
		// 顺时针
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
		// 预选方块
		float x = cubes_x;
		float y = cubes_y;
		Iterator<Cube> itr = nextCubes.iterator();
		Cube temp;
		// 第一个方块
		if (itr.hasNext()) {
			temp = itr.next();
			paint.setColor(temp.getBitmap());
			canvas.drawRect(grids_x + gridsNum[level] / 2 * 11 * gridsInterval,
					y, grids_x + (gridsNum[level] / 2 * 11 + 10)
							* gridsInterval, y + gridsInterval * 10, paint);
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
		canvas.drawText("过关分数:" + demandScore[level], 40, 240, paint);
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
