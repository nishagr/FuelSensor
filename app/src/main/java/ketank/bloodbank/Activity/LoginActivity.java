package ketank.bloodbank.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ketank.bloodbank.Other.AppController;
import ketank.bloodbank.R;
import ketank.bloodbank.Urls.All_urls;

public class LoginActivity extends AppCompatActivity {
    ProgressBar bankprog;
    Button banklogin;
    ProgressBar userprog;
    Button userlogin;
    ProgressBar registerprog;
    Button register;

    SharedPreferences preferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("mypref",Context.MODE_PRIVATE);

        if(preferences.getBoolean("Userlogin",false)){
            Intent intent =new Intent(LoginActivity.this,UserMainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public void BankLogin(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);

        LayoutInflater inflater =  getLayoutInflater();

        View customview = inflater.inflate(R.layout.popup_bank_login, null);
        dialogBuilder.setView(customview);

        final EditText userid = customview.findViewById(R.id.userid);
        final EditText password = customview.findViewById(R.id.pass);
        banklogin = customview.findViewById(R.id.log);
        bankprog = customview.findViewById(R.id.eventprog);
        bankprog.setVisibility(View.GONE);

        banklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                banklogin.setVisibility(View.GONE);
                bankprog.setVisibility(View.VISIBLE);
               BankLogin(All_urls.values.BankLogin,userid.getText().toString(),password.getText().toString());
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();



    }

    public void UserLogin(View view){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
        else {
            onLogin();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onLogin();
            }
        }

    }


    public void onLogin(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);

        LayoutInflater inflater =  getLayoutInflater();

        View customview = inflater.inflate(R.layout.popup_user_login, null);
        dialogBuilder.setView(customview);

        final EditText userid = customview.findViewById(R.id.userid);
        final EditText password = customview.findViewById(R.id.pass);
        userlogin = customview.findViewById(R.id.log);
        userprog = customview.findViewById(R.id.eventprog);
        userprog.setVisibility(View.GONE);

        TextView notsign = customview.findViewById(R.id.notsign);

        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlogin.setVisibility(View.GONE);
                userprog.setVisibility(View.VISIBLE);
                 UserLogin(All_urls.values.UserLogin,userid.getText().toString(),password.getText().toString());
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        notsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             alertDialog.dismiss();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                LayoutInflater inflater =  getLayoutInflater();

                View customview = inflater.inflate(R.layout.popup_user_signup, null);
                dialogBuilder.setView(customview);

                final EditText userid = customview.findViewById(R.id.userid);
                final EditText password = customview.findViewById(R.id.pass);
                final EditText name = customview.findViewById(R.id.name);
                final EditText group = customview.findViewById(R.id.group);
                final EditText gender = customview.findViewById(R.id.gender);
                final EditText age = customview.findViewById(R.id.age);
                final CheckBox donor = customview.findViewById(R.id.donor);

                register = customview.findViewById(R.id.log);
                registerprog = customview.findViewById(R.id.eventprog);
                registerprog.setVisibility(View.GONE);

                TextView notsign = customview.findViewById(R.id.notsign);
                final int[] check = {0};

                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        register.setVisibility(View.GONE);
                        registerprog.setVisibility(View.VISIBLE);

                        if(donor.isChecked())
                            check[0] =1;

                        RegisterUser(All_urls.values.UserRegistration,userid.getText().toString(),password.getText().toString(),name.getText().toString(),group.getText().toString(),gender.getText().toString(),age.getText().toString(),""+check[0]);
                    }
                });

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });


    }



    void RegisterUser(String url, final String userid, final String password, final String name, final String group, final String gender, final String age,final String check){
        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String p) {
                        // display response
                        Log.d("Response", p);

                        try {


                            JSONObject item =  new JSONObject(p);
                            if(!item.getBoolean("error")){

                                Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_LONG).show();
/*


                                UserModel user = new UserModel();
                                user.setName();
                                user.setGender();
                                user.setPhone();
                                user.setDonor();
                                user.setGroup(item.getString("bloodgroup"));
                                user.setFrequency(item.getString("Register"));
                                user.setAge(item.getString("Judging"));
*/


                                SharedPreferences.Editor editor =preferences.edit();
                                editor.putString("PhoneNumber",userid);
                                editor.putBoolean("Userlogin",true);
                                editor.apply();


                                Intent intent =new Intent(LoginActivity.this,UserMainActivity.class);
                                startActivity(intent);
                                finish();


                            }else {
                                Toast.makeText(getApplicationContext(),"Invalid Username/Password",Toast.LENGTH_LONG).show();
                                registerprog.setVisibility(View.GONE);
                                register.setVisibility(View.VISIBLE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Login error",Toast.LENGTH_LONG).show();
                            registerprog.setVisibility(View.GONE);
                            register.setVisibility(View.VISIBLE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Login error",Toast.LENGTH_LONG).show();
                        registerprog.setVisibility(View.GONE);
                        register.setVisibility(View.VISIBLE);

                    }
                }
        )    {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("user_name", name);
                params.put("bloodgroup", group);
                params.put("phone", userid);
                params.put("password", password);
                params.put("donor", check);
                params.put("age", age);
                params.put("gender", gender);
                params.put("token", FirebaseInstanceId.getInstance().getToken());
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(getRequest,"");
    }




    public void  UserLogin(String url, final String userid, final String pass){

        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String p) {
                        // display response
                        Log.d("Response", p);

                        try {


                            JSONObject item =  new JSONObject(p);
                            if(!item.getBoolean("error")){

                                Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_LONG).show();

                                SharedPreferences.Editor editor =preferences.edit();

                                editor.putString("PhoneNumber",userid);
                                editor.putBoolean("Userlogin",true);
                                editor.apply();
                                Intent intent =new Intent(LoginActivity.this,UserMainActivity.class);
                                startActivity(intent);
                                finish();


                            }else {
                                Toast.makeText(getApplicationContext(),"Invalid Username/Password",Toast.LENGTH_LONG).show();
                                userprog.setVisibility(View.GONE);
                                userlogin.setVisibility(View.VISIBLE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Login error",Toast.LENGTH_LONG).show();
                            userprog.setVisibility(View.GONE);
                            userlogin.setVisibility(View.VISIBLE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Login error",Toast.LENGTH_LONG).show();
                        userprog.setVisibility(View.GONE);
                        userlogin.setVisibility(View.VISIBLE);

                    }
                }
        )    {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("phone", userid);
                params.put("password", pass);
                params.put("token", FirebaseInstanceId.getInstance().getToken());
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(getRequest,"");
    }

    public void  BankLogin(String url, final String userid, final String pass){

        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String p) {
                        // display response
                        Log.d("Response", p);

                        try {


                            JSONArray item =  new JSONArray(p);

                            JSONObject bnk = item.getJSONObject(0);

                                Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_LONG).show();

                                SharedPreferences.Editor editor =preferences.edit();

                                editor.putBoolean("BankLogin",true);
                                editor.putString("bbid",userid);
                                editor.putString("BankLat",bnk.getString("Lat"));
                                editor.putString("BankLang",bnk.getString("Lang"));
                                editor.putString("BankName",bnk.getString("Name"));
                                editor.putBoolean("Userlogin",false);
                                editor.apply();
                                Intent intent =new Intent(LoginActivity.this,BankMainActivity.class);
                                startActivity(intent);






                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Login error",Toast.LENGTH_LONG).show();
                            bankprog.setVisibility(View.GONE);
                            banklogin.setVisibility(View.VISIBLE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Login error",Toast.LENGTH_LONG).show();
                        bankprog.setVisibility(View.GONE);
                        banklogin.setVisibility(View.VISIBLE);

                    }
                }
        )    {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("bbid", userid);
                params.put("password", pass);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(getRequest,"");
    }



}
