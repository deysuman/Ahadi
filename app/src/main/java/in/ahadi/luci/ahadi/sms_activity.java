package in.ahadi.luci.ahadi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static in.ahadi.luci.ahadi.AppConfig.URL_LOGIN;
import static in.ahadi.luci.ahadi.AppConfig.URL_OTPVERIFY;

public class sms_activity extends AppCompatActivity {
    public static final String profilepreference = "profilepreference" ;
    SharedPreferences sharedpreferences;
    TextView mobilenofield;
    String mobileno,otpid;
    ImageView backbutton;
    ProgressBar spinner;
    Button verifybutton;
    EditText otp;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_activity);
        sharedpreferences = getSharedPreferences(profilepreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        spinner = (ProgressBar)findViewById(R.id.progressbar);

        Intent i = getIntent();
        mobileno = i.getStringExtra("mobileno");
        otpid = i.getStringExtra("otpid");

        mobilenofield = (TextView)findViewById(R.id.txt_edit_mobile);

        LinearLayout mobile_change = (LinearLayout)findViewById(R.id.layout_edit_mobile);

        verifybutton = (Button)findViewById(R.id.btn_verify_otp);

        otp = (EditText)findViewById(R.id.inputOtp);


        mobilenofield.setText(mobileno);

        backbutton = (ImageView)findViewById(R.id.btn_edit_mobile);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sms_activity.this,
                        login.class);
                startActivity(intent);
                finish();
            }
        });
        verifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationfrom();
            }
        });






    }

    public boolean otpValidator(String otpp)
    {
        Pattern pattern;
        Matcher matcher;
        final String mobilepatten = "^\\d{6}$";
        pattern = Pattern.compile(mobilepatten);
        matcher = pattern.matcher(otpp);
        return matcher.matches();
    }

    private void validationfrom(){
        if(otp.getText().toString().trim().isEmpty()){
            getalert("OTP is required");
        }
        else if(!otpValidator(otp.getText().toString())){
            getalert("OTP is not valid");
        }else {
            checkinternet();
        }
    }


    private void verifyotp(){



        // Tag used to cancel the request
        String tag_string_req = "req_otpverify";


        verifybutton.setVisibility(View.GONE);

        loadingDialog = ProgressDialog.show(sms_activity.this, "Please wait", "Loading...", false);


        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_OTPVERIFY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean validotp = jObj.getBoolean("validotp");
                    if(validotp) {
                        boolean validuser = jObj.getBoolean("validuser");
                        if (validuser) {

                            JSONObject user = jObj.getJSONObject("user");
                            String usertype = user.getString("usertype");
                            String userid = user.getString("userid");
                            String fullname = user.getString("fullname");

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("user_full_name", fullname);
                            editor.putString("user_id", userid);
                            editor.putString("user_type", usertype);
                            editor.commit();


                            Intent intent = new Intent(sms_activity.this,
                                    display_menu.class);
                            startActivity(intent);
                            finish();
                        } else {

                           Intent intent = new Intent(sms_activity.this,
                                    from_fill.class);
                            intent.putExtra("mobileno", mobileno);
                            startActivity(intent);
                            finish();

                        }
                    }else{
                        getalert("You enter wrong otp");
                        loadingDialog.dismiss();
                        verifybutton.setVisibility(View.VISIBLE);
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
                Intent i = getIntent();
                params.put("otpid", i.getStringExtra("otpid"));
                params.put("otpvalue", otp.getText().toString());
                params.put("mobile", i.getStringExtra("mobileno"));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void  getalert(String getalertdata){
        AlertDialog alertDialog = new AlertDialog.Builder(
                sms_activity.this).create();



        // Setting Dialog Message
        alertDialog.setMessage(getalertdata);


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();

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
            verifyotp();
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

