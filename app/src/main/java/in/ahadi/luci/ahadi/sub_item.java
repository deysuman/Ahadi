package in.ahadi.luci.ahadi;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




public class sub_item extends AppCompatActivity {


    private static final String url = "http://m.cureesse.com/oic.php";
    private ProgressDialog pDialog;

    JSONObject jsonobject;
    JSONArray jsonarray;
    ListView listview;
    ListViewAdapter adapter;
    ArrayList<HashMap<String, String>> arraylist;
    TextView title;
    String itemcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_item);
        Intent i = getIntent();
        itemcode = i.getStringExtra("item_code");
        //setTitle(itemcode);


        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp, "Home"))
                .addItem(new BottomNavigationItem(R.drawable.ic_notifications_active_black_24dp, "Notification"))
                .addItem(new BottomNavigationItem(R.drawable.ic_settings_black_24dp, "Settings"))
                .initialise();
        //bottomNavigationBar.selectTabAtPosition(3, false);
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
            }
        });



        new getitems().execute();




    }


    private class getitems extends AsyncTask<Void, Void, Void> {
        private ProgressDialog pDialog;
        private Dialog loadingDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            loadingDialog = ProgressDialog.show(sub_item.this, "Please wait", "Loading...", false);
        }
       @Override
        protected Void doInBackground(Void... params) {
            arraylist = new ArrayList<HashMap<String, String>>();
            jsonobject = JSONfunctions
                    .getJSONfromURL(url);

            try {

                jsonarray = jsonobject.getJSONArray("item");

                for (int i = 0; i < jsonarray.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    jsonobject = jsonarray.getJSONObject(i);


                    // Retrive JSON Objects
                    map.put("itemname", jsonobject.getString("itemname"));
                    arraylist.add(map);

                }
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.list);
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(sub_item.this, arraylist);
            // Set the adapter to the ListView
            listview.setAdapter((ListAdapter) adapter);

            // Close the progressdialog
            loadingDialog.dismiss();
        }
    }






}
