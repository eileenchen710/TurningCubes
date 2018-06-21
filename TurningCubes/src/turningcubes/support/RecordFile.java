package turningcubes.support;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import turningcubes.object.Chart;
import turningcubes.object.Cube;
import turningcubes.object.Game;
import turningcubes.object.Survival;

import android.util.Log;
import android.util.Xml;

/**
 * 
 * 记录：封装XML文件输入输出操作 在退出游戏时保存记录
 * 
 */
public class RecordFile extends DefaultHandler {
	String filename = "cubes_record.xml";// 记录文件名
	int state = 0;// 1-Game 2-Survival 3-chart 标记段落

	// 临时类
	Game tempGame = null;
	Survival tempSurvival = null;
	Chart tempChart = null;

	StringBuffer buffer = new StringBuffer();
	Cube tempCube;
	Queue<Cube> cubeList;
	Queue<Integer> tempScore;
	Queue<String> tempName;

	public RecordFile() {
		super();
	}

	/**
	 * 封装添加节点操作
	 * 
	 * @param serializer
	 * @param name
	 * @param value
	 */
	void addElement(XmlSerializer serializer, String name, Object value) {
		try {
			serializer.startTag(null, name);
			serializer.text(value + "");
			serializer.endTag(null, name);
		} catch (Exception e) {
		}

	}

	/**
	 * 保存游戏数据
	 * 
	 * @param game
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 */
	public void writeRecord(Game game, Survival survival, Chart chart) {
		try {
			FileOutputStream fos = new FileOutputStream(
					android.os.Environment.getExternalStorageDirectory() + "/"
							+ filename);
			XmlSerializer serializer = Xml.newSerializer();

			serializer.setOutput(fos, "UTF-8");
			serializer.startDocument(null, Boolean.valueOf(true));
			serializer.startTag(null, "game");

			// 普通游戏
			serializer.startTag(null, "levelgame");
			addElement(serializer, "clockWiseState", game.isClockWiseState());
			addElement(serializer, "trunState", game.isTrunState());
			addElement(serializer, "pauseState", game.isPauseState());
			addElement(serializer, "level", game.getLevel());
			addElement(serializer, "score", game.getScore());
			addElement(serializer, "levelScore", game.getLevelScore());
			addElement(serializer, "usedCubes", game.getUsedCubes());

			serializer.startTag(null, "nextcubes");
			Queue<Cube> nextcubes = new LinkedList<Cube>(game.getNextCubes());
			while (nextcubes.peek() != null) {
				serializer.startTag(null, "cube");
				Cube cube = nextcubes.poll();
				addElement(serializer, "side", cube.getSide());
				addElement(serializer, "action", cube.getAction());
				serializer.endTag(null, "cube");
			}
			serializer.endTag(null, "nextcubes");

			serializer.startTag(null, "gamegrids");
			for (int i = 0; i < game.getGridsNum(game.getLevel()); i++)
				for (int j = 0; j < game.getGridsNum(game.getLevel()); j++) {
					serializer.startTag(null, "cube");
					Cube cube = game.getGameGrids()[i][j];
					addElement(serializer, "side", cube.getSide());
					addElement(serializer, "action", cube.getAction());
					serializer.endTag(null, "cube");
				}
			serializer.endTag(null, "gamegrids");
			serializer.endTag(null, "levelgame");

			// 无尽模式
			serializer.startTag(null, "survival");
			addElement(serializer, "pauseState", survival.isPauseState());
			addElement(serializer, "score", survival.getScore());
			addElement(serializer, "remaind", survival.getCubeRemainder());

			serializer.startTag(null, "nextcubes");
			nextcubes = new LinkedList<Cube>(survival.getNextCubes());
			while (nextcubes.peek() != null) {
				serializer.startTag(null, "cube");
				Cube cube = nextcubes.poll();
				addElement(serializer, "side", cube.getSide());
				addElement(serializer, "action", cube.getAction());
				serializer.endTag(null, "cube");
			}
			serializer.endTag(null, "nextcubes");

			serializer.startTag(null, "gamegrids");
			for (int i = 0; i < survival.getGridsNum(); i++)
				for (int j = 0; j < survival.getGridsNum(); j++) {
					serializer.startTag(null, "cube");
					Cube cube = survival.getGameGrids()[i][j];
					addElement(serializer, "side", cube.getSide());
					addElement(serializer, "action", cube.getAction());
					serializer.endTag(null, "cube");
				}
			serializer.endTag(null, "gamegrids");
			serializer.endTag(null, "survival");

			// 排行榜
			serializer.startTag(null, "chart");
			for (int i = 0; i < chart.getNum(); i++)
				addElement(serializer, "score", chart.getScore()[i]);
			for (int i = 0; i < chart.getNum(); i++)
				addElement(serializer, "name", chart.getName()[i]);
			serializer.endTag(null, "chart");

			serializer.endTag(null, "game");
			serializer.endDocument();
			serializer.flush();
			fos.close();
		} catch (Exception e) {
			Log.i("123", "xmlErro");
		}

	}

	/**
	 * 读取数据
	 * 
	 * @return
	 */
	public void readRecord(Game game) {
		tempGame = game;
		try {
			FileInputStream fis = new FileInputStream(
					android.os.Environment.getExternalStorageDirectory() + "/"
							+ filename);
			Xml.parse(fis, Xml.Encoding.UTF_8, this);
			tempGame.setGridsInterval(tempGame.getGameGrids()[0][0].getSide() / 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		game = tempGame;
		tempGame = null;
		state = 0;
	}

	public void readRecord(Survival survival) {
		tempSurvival = survival;
		try {
			FileInputStream fis = new FileInputStream(
					android.os.Environment.getExternalStorageDirectory() + "/"
							+ filename);
			Xml.parse(fis, Xml.Encoding.UTF_8, this);
			tempGame.setGridsInterval(tempGame.getGameGrids()[0][0].getSide() / 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		survival = tempSurvival;
		tempSurvival = null;
		state = 0;
	}

	public void readRecord(Chart chart) {
		tempChart = chart;
		try {
			FileInputStream fis = new FileInputStream(
					android.os.Environment.getExternalStorageDirectory() + "/"
							+ filename);
			Xml.parse(fis, Xml.Encoding.UTF_8, this);
			tempGame.setGridsInterval(tempGame.getGameGrids()[0][0].getSide() / 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		chart = tempChart;
		tempChart = null;
		state = 0;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buffer.append(ch, start, length);// 将节点内容放入缓冲区
		super.characters(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		String temp = buffer.toString().trim();
		buffer.setLength(0);

		// 普通游戏
		if (state == 1 && tempGame != null) {
			if (localName.equals("clockWiseState")) {
				tempGame.setClockWiseState(Boolean.parseBoolean(temp));
			}
			if (localName.equals("trunState")) {
				tempGame.setTrunState(Boolean.parseBoolean(temp));
			}
			if (localName.equals("pauseState")) {
				tempGame.setPauseState(Boolean.parseBoolean(temp));
			}
			if (localName.equals("level")) {
				tempGame.setLevel(Integer.parseInt(temp));
			}
			if (localName.equals("score")) {
				tempGame.setScore(Integer.parseInt(temp));
			}
			if (localName.equals("levelScore")) {
				tempGame.setLevelScore(Integer.parseInt(temp));
			}
			if (localName.equals("usedCubes")) {
				tempGame.setUsedCubes(Integer.parseInt(temp));
			}

			if (localName.equals("side")) {
				tempCube.setSide(Float.parseFloat(temp));
			}
			if (localName.equals("action")) {
				tempCube.setAction(Integer.parseInt(temp));
				tempCube.setBitmap();
			}
			if (localName.equals("cube")) {
				cubeList.offer(tempCube);
			}

			if (localName.equals("nextcubes")) {
				tempGame.setNextCubes(cubeList);
				cubeList = null;
			}
			if (localName.equals("gamegrids")) {
				Cube grids[][] = tempGame.getGameGrids();
				for (int i = 0; i < tempGame.getGridsNum(tempGame.getLevel()); i++)
					for (int j = 0; j < tempGame.getGridsNum(tempGame
							.getLevel()); j++) {
						grids[i][j] = cubeList.poll();
					}
				tempGame.setGameGrids(grids);
				cubeList = null;
			}
		}

		// 无尽模式
		if (state == 2 && tempSurvival != null) {
			if (localName.equals("pauseState")) {
				tempSurvival.setPauseState(Boolean.parseBoolean(temp));
			}
			if (localName.equals("score")) {
				tempSurvival.setScore(Integer.parseInt(temp));
			}
			if (localName.equals("remaind")) {
				tempSurvival.setCubeRemainder(Integer.parseInt(temp));
			}
			if (localName.equals("side")) {
				tempCube.setSide(Float.parseFloat(temp));
			}
			if (localName.equals("action")) {
				tempCube.setAction(Integer.parseInt(temp));
				tempCube.setBitmap();
			}
			if (localName.equals("cube")) {
				cubeList.offer(tempCube);
			}

			if (localName.equals("nextcubes")) {
				tempSurvival.setNextCubes(cubeList);
				cubeList = null;
			}
			if (localName.equals("gamegrids")) {
				Cube grids[][] = tempSurvival.getGameGrids();
				for (int i = 0; i < tempSurvival.getGridsNum(); i++)
					for (int j = 0; j < tempSurvival.getGridsNum(); j++) {
						grids[i][j] = cubeList.poll();
					}
				tempSurvival.setGameGrids(grids);
				cubeList = null;
			}
		}

		// 排行榜
		if (state == 3 && tempChart != null) {
			if (localName.equals("score")) {
				tempScore.offer(Integer.parseInt(temp));
			}
			if (localName.equals("name")) {
				tempName.offer(temp);
			}
			if (localName.equals("chart")) {

				for (int i = 0; i < tempChart.getNum(); i++) {
					tempChart.getScore()[i] = tempScore.poll();
					tempChart.getName()[i] = tempName.poll();
				}
				tempScore = null;
				tempName = null;
			}
		}

		super.endElement(uri, localName, qName);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// 标记读取到的段落
		if (localName.equals("levelgame"))
			state = 1;
		if (localName.equals("survival"))
			state = 2;
		if (localName.equals("chart")) {
			state = 3;
			tempScore = new LinkedList<Integer>();
			tempName = new LinkedList<String>();
		}
		if (localName.equals("nextcubes") || localName.equals("gamegrids")) {
			cubeList = new LinkedList<Cube>();
		}
		if (localName.equals("cube")) {
			tempCube = new Cube(0);
		}
		super.startElement(uri, localName, qName, attributes);
	}

}
