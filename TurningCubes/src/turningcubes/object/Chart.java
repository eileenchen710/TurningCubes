package turningcubes.object;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * �޾�ģʽ���а񣺰���ǰʮ��������
 *
 */
public class Chart {
	// ��ť
	BitmapButton bBack;

	//��ʼ���Ͻ�
	float chart_x;
	float chart_y;

	int num = 7;// ��¼ʮ��

	int score[] = new int[num];//�ɼ�
	String name[] = new String[num];//����

	public Chart(Context context,float width,float height) {
		super();
		chart_x = 375;
		chart_y = 250;
		bBack = new BitmapButton(Color.GRAY, new RectF(0, 30, 223, 148));
		bBack.bitmapPic = BitmapFactory.decodeResource(context.getResources(),mobile.turningcubes.R.drawable.back);
		
		//��ʼ��
		for (int i = 0; i < num; i++) {
			score[i] = 0;
			name[i] = "player";
		}
		
	}

	/**
	 * �����¼�¼
	 * 
	 * @param score
	 * @param name
	 */
	void insert(int score, String name) {
		for (int i = 0; i < num; i++) {
			//�Ƚ� ѡ����ȷ�����
			if (this.score[i] < score) {
				for (int j = num - 1; j > i;) {
					this.score[j] = this.score[j - 1];
					this.name[j] = this.name[--j];
				}
				this.score[i] = score;
				this.name[i] = name;
				break;
			}
		}
	}

	/**
	 * �ж��Ƿ����˰�ť ����ֵΪ��Ϸ״̬
	 * @param x
	 * @param y
	 * @return
	 */
	public int checkClick(float x, float y) {
		if (bBack.checkContain(x, y))
			return 0;
		return 3;
	}

	/**
	 * �������
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
	//	canvas.drawColor(Color.CYAN);
		Paint paint = new Paint();
		paint.setColor(bBack.getBitmap());
		//canvas.drawRect(bBack.getRect(), paint);
		canvas.drawBitmap(bBack.bitmapPic, 0 , 30 , paint);
		

		for (int i = 0; i < num; i++) {
			paint.setColor(Color.RED);
			canvas.drawText((i + 1) + ":\t" + score[i], chart_x, chart_y + 30
					* i, paint);
			canvas.drawText("" + name[i], chart_x + 80, chart_y + 30 * i, paint);
		}
	}
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int[] getScore() {
		return score;
	}

	public void setScore(int[] score) {
		this.score = score;
	}

	public String[] getName() {
		return name;
	}

	public void setName(String[] name) {
		this.name = name;
	}
}
