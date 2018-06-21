package mobile.turningcubes;

import java.util.Timer;
import java.util.TimerTask;

import turningcubes.object.Game;
import turningcubes.object.MainMenu;
import mobile.turningcubes.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.view.Display;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


public class MainActivity extends Activity {
	GameSurfaceView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    	//全屏
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        
    	//关联View
        GameSurfaceView gameView = new GameSurfaceView(this);
        // 获取系统分辨率  
        Display display = getWindowManager().getDefaultDisplay();
        gameView.getFBL(display.getWidth(), display.getHeight());
        setContentView(gameView);
  
    }
    



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onResume() {
     //设置为横屏
     if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
     }
     super.onResume();
    }


    
}
