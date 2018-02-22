package in.ahadi.luci.ahadi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static in.ahadi.luci.ahadi.AppConfig.URL_GETALLNUMBERS;

public class contacts extends AppCompatActivity {

    private List<FranchaiseModel> franchaises = new ArrayList<FranchaiseModel>();
    private Franchise adapter;
    ListView myNames;
    String loguserid;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        sharedpreferences = getSharedPreferences("profilepreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        loguserid = sharedpreferences.getString("user_id", "");

        if (loguserid.equalsIgnoreCase("")){
            Intent intent = new Intent(contacts.this, login.class);
            startActivity(intent);
            finish();
        }

        myNames= (ListView) findViewById(R.id.List);
        adapter = new Franchise(this, franchaises);
        myNames.setAdapter(adapter);

        getContacts();

    }

    private void getContacts(){


        // Tag used to cancel the request
        String tag_string_req = "req_getnumbers";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_GETALLNUMBERS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("status");
                    if(status) {

                        JSONArray items = jObj.getJSONArray("franchises");

                        for(int x = 0;x < items.length();x++){
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
                params.put("status", "1");
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


}
