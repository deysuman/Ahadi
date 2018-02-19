package in.ahadi.luci.ahadi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static in.ahadi.luci.ahadi.AppConfig.URL_GETALLORDERDETAILS;
import static in.ahadi.luci.ahadi.AppConfig.URL_LOGIN;

public class settings extends AppCompatActivity {
    Toolbar toolbar;
    private BottomBar mBottomBar;
    private PopupWindow mPopupWindow;
    private RelativeLayout mRelativeLayout;
    private Context mContext;
    private Activity mActivity;
    SharedPreferences sharedpreferences;
    String loguserid;
    LinearLayout profilesetting,callnow,rate,feedback,aboutus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        sharedpreferences = getSharedPreferences("profilepreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        loguserid = sharedpreferences.getString("user_id", "");

        mContext = getApplicationContext();
        mActivity = settings.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);


        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(
                new BottomBarTab(R.drawable.home, "Home"),
                new BottomBarTab(R.drawable.ic_notifications_active_black_18dp, "Notification"),
                new BottomBarTab(R.drawable.set, "Settings")
        );

        mBottomBar.selectTabAtPosition(2, false);
        // Listen for tab changes
        mBottomBar.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabSelected(int position) {
                // The user selected a tab at the specified position

                Intent intent;
                switch (position){
                    case 0:  intent = new Intent(settings.this,
                            display_menu.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 1:  intent = new Intent(settings.this,
                            cartactivaty.class);
                        startActivity(intent);
                        finish();
                        break;


                }




            }

            @Override
            public void onTabReSelected(int position) {
                // The user reselected a tab at the specified position!
            }
        });

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, 0xFF5D4037);
        mBottomBar.mapColorForTab(2, "#7B1FA2");

        mBottomBar.setActiveTabColor("#009688");


        profilesetting = (LinearLayout)findViewById(R.id.profilesetting);
        callnow = (LinearLayout)findViewById(R.id.requstcall);
        rate = (LinearLayout)findViewById(R.id.rateapp);
        feedback = (LinearLayout)findViewById(R.id.feednow);
        aboutus = (LinearLayout)findViewById(R.id.aboutus);



        profilesetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(settings.this,profilesettings.class);
                startActivity(i);
                finish();
            }
        });


        callnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestcallpopup();
            }
        });

        mRelativeLayout = (RelativeLayout) findViewById(R.id.activity_settings);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);


                View customView = inflater.inflate(R.layout.rateingpopup,null);

                mPopupWindow = new PopupWindow(
                        customView,
                        Toolbar.LayoutParams.WRAP_CONTENT,
                        Toolbar.LayoutParams.WRAP_CONTENT
                );

                mPopupWindow.setAnimationStyle(R.style.animationName);


                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }


                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);
                final RatingBar ratingBar = (RatingBar) customView.findViewById(R.id.ratingBar);
                Drawable drawable = ratingBar.getProgressDrawable();
                drawable.setColorFilter(Color.parseColor("#FFFDEC00"), PorterDuff.Mode.SRC_ATOP);
                Button submitrating = (Button) customView.findViewById(R.id.submitrating);

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });

                submitrating.setOnClickListener(new View.OnClickListener() {
                    @Override
                     public void onClick(View view) {
                      String ratingValue = String.valueOf(ratingBar.getRating());
                        Toast.makeText(getApplicationContext(), "Rate: " + ratingValue, Toast.LENGTH_LONG).show();
                                                    }
                                                });

                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback();
            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutus();
            }
        });






    }







    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    private void requestcallpopup(){
        Toast.makeText(getBaseContext(),"We are touch you shortly",Toast.LENGTH_LONG).show();
    }

    private void rate(){

    }

    private void feedback(){

    }

    private void aboutus(){

    }


}