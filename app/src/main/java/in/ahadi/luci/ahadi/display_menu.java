package in.ahadi.luci.ahadi;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.roughike.bottombar.OnTabClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static in.ahadi.luci.ahadi.AppConfig.URL_GETALLNUMBERS;
import static in.ahadi.luci.ahadi.AppConfig.URL_GETALLORDERDETAILS;

public class display_menu extends AppCompatActivity {

    ImageView callnow;

    ListView myNames;

    Activity x;

    private BottomBar mBottomBar;
    private List<FranchaiseModel> franchaises = new ArrayList<>();
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired =  new String[]{Manifest.permission.CALL_PHONE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private ProgressBar bar;

    private Franchise adapter;

    ProgressDialog dialogs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu);

        x = this;
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);


        if(ActivityCompat.checkSelfPermission(display_menu.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(display_menu.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(display_menu.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(display_menu.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(display_menu.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(display_menu.this,permissionsRequired[2])){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(display_menu.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(display_menu.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(display_menu.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(display_menu.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }

            /*


            ALert here



            */

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0],true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }


        callnow = (ImageView) findViewById(R.id.callnow);



        callnow.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(display_menu.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Title...");
                dialog.show();
                myNames= (ListView) dialog.findViewById(R.id.List);
                adapter = new Franchise(x, franchaises);
                myNames.setAdapter(adapter);

                getContacts();

            }
        });





/*
        callnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + "+919073517771"));
                if (ActivityCompat.checkSelfPermission(display_menu.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });;*/







        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(
                new BottomBarTab(R.drawable.home, "Home"),
                new BottomBarTab(R.drawable.ic_notifications_active_black_18dp, "Notification"),
                new BottomBarTab(R.drawable.set, "Settings")
        );

        // Listen for tab changes
        mBottomBar.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabSelected(int position) {
                // The user selected a tab at the specified position
                Intent intent;
                switch (position){
                    case 1:  intent = new Intent(display_menu.this,
                            cartactivaty.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 2:  intent = new Intent(display_menu.this,
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

       // mBottomBar.selectTabAtPosition(0, false);










    }


    private void getContacts(){

         dialogs = ProgressDialog.show(display_menu.this, "",
                "Connecting. Please wait...", true);
        dialogs.setCancelable(false);
        dialogs.show();


        // Tag used to cancel the request
        String tag_string_req = "req_getnumbers";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_GETALLNUMBERS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                dialogs.hide();

                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    if(status) {

                        JSONArray items = jObj.getJSONArray("franchises");


                        if(items.length() > 0) {

                            franchaises.clear();

                            for (int x = 0; x < items.length(); x++) {

                                JSONObject data = items.getJSONObject(x);
                                FranchaiseModel fmodel = new FranchaiseModel();
                                fmodel.setAddress(data.getString("address"));
                                fmodel.setMobile(data.getString("mobile"));
                                fmodel.setName(data.getString("name"));

                                franchaises.add(fmodel);

                            }

                            Log.e("Total_franchise", String.valueOf(franchaises.size()));

                            adapter.notifyDataSetChanged();
                        }

                        else{

                            Toast.makeText(getApplicationContext(), "No franchise found", Toast.LENGTH_LONG).show();

                        }



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

                dialogs.hide();

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
                params.put("status", "1");
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                proceedAfterPermission();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(display_menu.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(display_menu.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(display_menu.this,permissionsRequired[2])){

                /*
                txtPermissions.setText("Permissions Required");
                Alert here
                */


                AlertDialog.Builder builder = new AlertDialog.Builder(display_menu.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(display_menu.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(display_menu.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
        //txtPermissions.setText("We've got all permissions");

        //Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(display_menu.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
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

}