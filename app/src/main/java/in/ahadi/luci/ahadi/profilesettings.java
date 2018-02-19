package in.ahadi.luci.ahadi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static in.ahadi.luci.ahadi.AppConfig.URL_LOGIN;

public class profilesettings extends AppCompatActivity {
    Toolbar toolbar;
    Button delprofile,submit;
    EditText fullanme,mobilephone,addressarea;

    SharedPreferences sharedpreferences;
    String loguserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesettings);

        sharedpreferences = getSharedPreferences("profilepreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        loguserid = sharedpreferences.getString("user_id", "");

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("Profile Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        delprofile = (Button)findViewById(R.id.delprofile);

        submit = (Button) findViewById(R.id.save);

        delprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertMessage();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkinternet2();
            }
        });




        fullanme = (EditText) findViewById(R.id.name);
        mobilephone = (EditText) findViewById(R.id.mobile);
        addressarea = (EditText) findViewById(R.id.addreess);

        mobilephone.setEnabled(false);






        checkinternet();

    }

    private Toast toast;
    private Boolean exit = false;
    private long lastBackPressTime = 0;
    int count = 0;
    @Override
    public void onBackPressed() {

        Intent homeIntent = new Intent(this, settings.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);


        //finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, settings.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }


    private void fetchuser(){

        // Tag used to cancel the request
        String tag_string_req = "req_login";


        //spinner.setVisibility(View.VISIBLE);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://ahadi.in/app/php/userdetails.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jObj = new JSONObject(response);

                    String status = jObj.getString("error");

                    if(status.equals("1")) {
                        String name = jObj.getString("fullname");
                        String mobile = jObj.getString("mobile");
                        String address = jObj.getString("address");

                        fullanme.setText(name);
                        mobilephone.setText(mobile);
                        addressarea.setText(address);


                    }

                    else{

                        Toast.makeText(profilesettings.this,String.valueOf(jObj.get("message")),Toast.LENGTH_LONG).show();
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
                params.put("userid", loguserid);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void savedata(){

        // Tag used to cancel the request
        String tag_string_req = "req_login";


        //spinner.setVisibility(View.VISIBLE);

        Toast.makeText(profilesettings.this,"Saved",Toast.LENGTH_LONG);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://ahadi.in/app/php/savedetails.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jObj = new JSONObject(response);

                    String status = jObj.getString("error");

                    if(status.equals("1")) {

                        Toast.makeText(profilesettings.this,String.valueOf(jObj.get("message")),Toast.LENGTH_LONG).show();

                    }

                    else{

                        Toast.makeText(profilesettings.this,String.valueOf(jObj.get("message")),Toast.LENGTH_LONG).show();
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
                params.put("userid", loguserid);
                params.put("fullanme", fullanme.getText().toString().trim());
                params.put("address", addressarea.getText().toString().trim());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
            fetchuser();
            return false;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(this, " Not Connected to Internet", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }


    public final boolean checkinternet2(){
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet
            savedata();
            return false;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(this, " Not Connected to Internet", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }



    public void alertMessage() { DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) { switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                // Yes button clicked
                Toast.makeText(profilesettings.this, "Yes Clicked", Toast.LENGTH_LONG).show();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                // No button clicked
           Toast.makeText(profilesettings.this, "No Clicked", Toast.LENGTH_LONG).show();
                break; } }

    }; AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?") .setPositiveButton("Yes", dialogClickListener) .setNegativeButton("No", dialogClickListener).show(); }






}
