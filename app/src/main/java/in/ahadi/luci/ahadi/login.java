package in.ahadi.luci.ahadi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.util.Log;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.identity.intents.Address;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static in.ahadi.luci.ahadi.AppConfig.URL_LOGIN;
import in.ahadi.luci.ahadi.AppController;

public class login extends AppCompatActivity {

    private static final String TAG = login.class.getSimpleName();
    Geocoder geocoder;
    List<Address> addresses;

    private EditText mobile,password;
    private TextInputLayout mobile_field, passwordfield;
    private Button loginbutton,register;
    private ProgressDialog pdialog;
    private ProgressBar spinner;
    String cell;
    SharedPreferences sharedpreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        geocoder = new Geocoder(this,Locale.getDefault());

        sharedpreferences = getSharedPreferences("profilepreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String loguserid = sharedpreferences.getString("user_id", "");
        if (!loguserid.equalsIgnoreCase("")){
            Intent intent = new Intent(login.this, display_menu.class);
            startActivity(intent);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinner=(ProgressBar)findViewById(R.id.progressbar);

        mobile_field = (TextInputLayout) findViewById(R.id.mobile_field);



        mobile = (EditText)findViewById(R.id.mobile);

        loginbutton = (Button)findViewById(R.id.email_sign_in_button);

        cell = mobile.getText().toString();


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromvalidateion();
            }
        });



    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public boolean mobileValidator(String mobileno)
    {
        Pattern pattern;
        Matcher matcher;
        final String mobilepatten = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$";
        pattern = Pattern.compile(mobilepatten);
        matcher = pattern.matcher(mobileno);
        return matcher.matches();
    }


    private void fromvalidateion(){
        if(mobile.getText().toString().trim().isEmpty()){
            mobile_field.setError("Mobile no is required");
            requestFocus(mobile_field);

        }

        else if(!mobileValidator(mobile.getText().toString())){
            mobile_field.setError(null);
            mobile_field.setError("Mobile no is not valid");
            requestFocus(mobile_field);

        }

        else{
            mobile_field.clearFocus();
            mobile_field.setError(null);
            checkinternet();
        }
    }

    private void login(){

        // Tag used to cancel the request
        String tag_string_req = "req_login";


        spinner.setVisibility(View.VISIBLE);

        StringRequest strReq = new StringRequest(Method.POST,
                URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jObj = new JSONObject(response);

                        String cellmobile = jObj.getString("mobileno");
                        String otpid = jObj.getString("otpid");
                        Intent intent = new Intent(login.this,
                                sms_activity.class);
                        intent.putExtra("mobileno", cellmobile);
                        intent.putExtra("otpid", otpid);
                        startActivity(intent);
                        finish();

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
                params.put("mobile", mobile.getText().toString());
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
            login();
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
