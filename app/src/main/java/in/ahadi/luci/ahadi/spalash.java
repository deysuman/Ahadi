package in.ahadi.luci.ahadi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

public class spalash extends AppCompatActivity {

    private ProgressBar spinner;
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalash);

         new Handler().postDelayed(new Runnable() {
             @Override
            public void run() {
                 Intent i = new Intent(spalash.this, login.class);
                 startActivity(i);
                 finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
