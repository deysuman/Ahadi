package in.ahadi.luci.ahadi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static in.ahadi.luci.ahadi.AppConfig.URL_GETORDERDETAILS;
import static in.ahadi.luci.ahadi.AppConfig.URL_LOGIN;
import static in.ahadi.luci.ahadi.AppConfig.URL_OTPVERIFY;

public class singleorder extends AppCompatActivity {

    TextView ordernofield,orderstatusfields;
    Button openratewiget;
    Toolbar toolbar ;
    ScrollView sview;
    private RelativeLayout mRelativeLayout;
    private static final String TAG = MainActivity.class.getSimpleName();
    private PopupWindow mPopupWindow;
    private Context mContext;
    private Activity mActivity;
    private ProgressBar spinner;
    private ProgressDialog pDialog;
    private List<Orders> movieList = new ArrayList<Orders>();
    private ExpandableHeightListView listView;
    private OrderCustomListAdapter adapter;
    String orderno,token,process;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleorder);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("Your orders items");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        orderno = i.getStringExtra("orderno");
        token = i.getStringExtra("token");
        process = i.getStringExtra("process");


        ordernofield = (TextView)findViewById(R.id.orderno);

        ordernofield.setText("Order no - "+orderno);

        orderstatusfields = (TextView)findViewById(R.id.orderstatus);
        openratewiget = (Button)findViewById(R.id.ratewigetopen);
        sview = (ScrollView)findViewById(R.id.sview);
        listView = (ExpandableHeightListView) findViewById(R.id.list);;
        adapter = new OrderCustomListAdapter(this, movieList);
        listView.setAdapter(adapter);
        listView.setExpanded(true);

        spinner=(ProgressBar)findViewById(R.id.progressbar);

        checkinternet();

        mContext = getApplicationContext();
        mActivity = singleorder.this;

        mRelativeLayout = (RelativeLayout) findViewById(R.id.activity_singleorder);


        openratewiget.setOnClickListener(new View.OnClickListener() {
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
                RatingBar ratingBar = (RatingBar) customView.findViewById(R.id.ratingBar);
                Drawable drawable = ratingBar.getProgressDrawable();
                drawable.setColorFilter(Color.parseColor("#FFFDEC00"), PorterDuff.Mode.SRC_ATOP);

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });


                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);
            }
        });


    }

    public  void getfullitems(){
        JsonArrayRequest movieReq = new JsonArrayRequest(URL_GETORDERDETAILS+"?orderno="+orderno+"&token="+token,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        spinner.setVisibility(View.GONE);
                        sview.setVisibility(View.VISIBLE);

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Orders movie = new Orders();
                                movie.setTitle(obj.getString("title"));
                                movie.setQuantity(obj.getString("quantity"));
                                movie.setPrice(obj.getString("price"));


                                // adding movie to movies array
                                movieList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
               // hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }


    public void  getalert(String getalertdata){
        AlertDialog alertDialog = new AlertDialog.Builder(
                this).create();
        alertDialog.setMessage(getalertdata);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();

    }


    private Toast toast;
    private Boolean exit = false;
    private long lastBackPressTime = 0;
    int count = 0;
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(singleorder.this,
                cartactivaty.class);
        startActivity(intent);
        finish();

        //finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, cartactivaty.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    public final boolean checkinternet(){
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet
            getfullitems();
            return false;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(this, " Not Connected to Internet", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

}
