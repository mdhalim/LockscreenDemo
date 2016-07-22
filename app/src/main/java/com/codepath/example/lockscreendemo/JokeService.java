package com.codepath.example.lockscreendemo;

/**
 * Created by mdhalim on 14/07/16.
 */

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class JokeService extends Service {

    private WindowManager windowManager;
    private ImageView broken_screen_View;
    private Boolean showing;

    public JokeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    public int onStartCommand (Intent intent, int flags, int startId){

        try {
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);

            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);

            BroadcastReceiver mReceiver = new receiverScreen();

            registerReceiver(mReceiver, filter);
        } catch (Exception e) {

        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        showing=false;
    }

    @Override
    public void onDestroy(){
        Log.v("SERVICE", "Service killed");
        if (showing){
            windowManager.removeViewImmediate(broken_screen_View);
            showing=false;
        }
        super.onDestroy();
    }

    private void Show_joke(){

        if (showing){return;} //Si l'image est déjà affichée, on sort.

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        broken_screen_View = new ImageView(this);
        Bitmap bmp_broken_screen;
        if(getResources().getDisplayMetrics().widthPixels>getResources().getDisplayMetrics().heightPixels)
        {
            bmp_broken_screen = BitmapFactory.decodeResource(getResources(), R.drawable.broken_screen_land);
        }
        else
        {
            bmp_broken_screen = BitmapFactory.decodeResource(getResources(), R.drawable.broken_screen);
        }
        broken_screen_View.setImageBitmap(bmp_broken_screen);
        broken_screen_View.setScaleType(ImageView.ScaleType.FIT_XY);
        broken_screen_View.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hide_broken_screen_and_destroy_service();
                return true;
            }
        });

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        windowManager.addView(broken_screen_View, params);
        showing=true;
    }

    private void hide_broken_screen_and_destroy_service(){
        Log.e("test", "hide_broken_screen_and_destroy_service: remove");
//        windowManager.removeViewImmediate(broken_screen_View);
        windowManager.removeView(broken_screen_View);
        showing=false;
        stopSelf();
    }

    public class receiverScreen extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                Log.e("test", "onReceive: " + "screen off!");
                Show_joke(); //Affichage de l'image de l'écran cassé
            }
            if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
                Log.e("test", "onReceive: " + "user present!");
//                hide_broken_screen_and_destroy_service(); //On supprime l'image et on détruit le service.
            }
        }

    }

}
