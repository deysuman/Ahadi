package in.ahadi.luci.ahadi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static in.ahadi.luci.ahadi.AppConfig.URL_GETALLORDERDETAILS;
import static in.ahadi.luci.ahadi.AppConfig.URL_GETORDERDETAILS;


public class cartactivaty extends AppCompatActivity {
    Toolbar toolbar ;
    private BottomBar mBottomBar;
    LinearLayout ft;
    private ExpandableHeightListView listView;
    //private List<Carts> cartiems = new ArrayList<Carts>();

    private List<Carts> cartiems;

    private OrdercartCustomListAdapter adapter;
    private ProgressBar spinner;
    SharedPreferences sharedpreferences;
    String loguserid;
    ScrollView sview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartactivaty);

        sharedpreferences = getSharedPreferences("profilepreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        loguserid = sharedpreferences.getString("user_id", "");

        if (loguserid.equalsIgnoreCase("")){
            Intent intent = new Intent(cartactivaty.this, login.class);
            startActivity(intent);
            finish();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("Your orders");
        setSupportActionBar(toolbar);
        sview = (ScrollView)findViewById(R.id.sview);
        listView = (ExpandableHeightListView) findViewById(R.id.list);;
        spinner=(ProgressBar)findViewById(R.id.progressbar);
        ft = (LinearLayout)findViewById(R.id.sad);
        cartiems = new ArrayList<Carts>();
        adapter = new OrdercartCustomListAdapter(this, cartiems);
        listView.setAdapter(adapter);
        listView.setExpanded(true);







        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(
                new BottomBarTab(R.drawable.home, "Home"),
                new BottomBarTab(R.drawable.ic_notifications_active_black_18dp, "Notification"),
                new BottomBarTab(R.drawable.set, "Settings")
        );

        mBottomBar.selectTabAtPosition(1, false);
        // Listen for tab changes
        mBottomBar.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabSelected(int position) {
                // The user selected a tab at the specified position

                Intent intent;
                switch (position){
                    case 0:  intent = new Intent(cartactivaty.this,
                            display_menu.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 2:  intent = new Intent(cartactivaty.this,
                            settings.class);
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


        checkinternet();





    }



    private void getcartitems(){


            // Tag used to cancel the request
            String tag_string_req = "req_getcartitems";


        spinner.setVisibility(View.GONE);
        sview.setVisibility(View.VISIBLE);

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    URL_GETALLORDERDETAILS, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean validcart = jObj.getBoolean("validcart");
                        if(validcart) {

                            JSONObject items = jObj.getJSONObject("items");

                            parseJsonFeed(jObj);




                        }




                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }



                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    //hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userid", "1");
                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }



    public  void getfullitems(){
        JsonArrayRequest movieReq = new JsonArrayRequest(URL_GETALLORDERDETAILS+"?userid="+loguserid,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d(TAG, response.toString());
                        spinner.setVisibility(View.GONE);
                        sview.setVisibility(View.VISIBLE);

                        if(response.length() > 0) {
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    Carts item = new Carts();
                                    item.setTitle(obj.getString("orderid"));
                                    item.setDate(obj.getString("orderdate"));
                                    item.setProcess(obj.getString("orderstatus"));
                                    item.setToken(obj.getString("ordertoken"));


                                    // adding movie to movies array
                                    cartiems.add(item);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        else{

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hidePDialog();
                spinner.setVisibility(View.GONE);
                sview.setVisibility(View.VISIBLE);
                ft.setVisibility(View.VISIBLE);

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }









    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray items = response.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject obj = (JSONObject) items.get(i);
                String orderid = obj.getString("orderid");
                String orderdate = obj.getString("orderdate");
                String orderstatus = obj.getString("orderstatus");
                String ordertoken = obj.getString("ordertoken");

                Carts item = new Carts();
                item.setTitle(obj.getString("orderid"));
                item.setDate(obj.getString("orderdate"));
                item.setProcess(obj.getString("orderstatus"));
                item.setToken(obj.getString("ordertoken"));
                cartiems.add(item);

                Log.i("log_tag", "_id" + obj.getString("orderid"));
            }

            adapter.notifyDataSetChanged();

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

















    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    private Toast toast;
    private Boolean exit = false;
    private long lastBackPressTime = 0;
    int count = 0;
    @Override
    public void onBackPressed() {

        if(count == 1)
        {   moveTaskToBack(true);
            count=0;
            exit = true;
            android.os.Process.killProcess(android.os.Process.myPid());
            this.finish();

            //finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Press Back again to quit.", Toast.LENGTH_SHORT).show();
            count++;
            //android.os.Process.killProcess(android.os.Process.myPid());
            //this.finish();
        }


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
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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
