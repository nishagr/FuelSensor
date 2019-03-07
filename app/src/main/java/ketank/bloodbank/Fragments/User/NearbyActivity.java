package ketank.bloodbank.Fragments.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ketank.bloodbank.Activity.MapsActivity;
import ketank.bloodbank.Adapters.BankListAdapter;
import ketank.bloodbank.Adapters.DonorsListAdapter;
import ketank.bloodbank.Models.BloodBank;
import ketank.bloodbank.Models.UserModel;
import ketank.bloodbank.Other.AppController;
import ketank.bloodbank.R;
import ketank.bloodbank.Urls.All_urls;


public class NearbyActivity extends AppCompatActivity {
    SharedPreferences preferences;
    ArrayList<UserModel>userModels = new ArrayList<>();
    ProgressBar progressBar;
    RecyclerView recyclerView;
    DonorsListAdapter adapter;
    MaterialSpinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_nearby);

        spinner = (MaterialSpinner) findViewById(R.id.spinner);

        spinner.setItems("All", "O+","O-","A+", "A-","B+","B-","AB+","AB-");

        progressBar = findViewById(R.id.progd);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView =findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        preferences= getSharedPreferences("mypref",Context.MODE_PRIVATE);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(position==0){


                 /*   for(UserModel user:userModels){
                        if(user.g>0) {
                            bloodBanks1.add(bank);
                        }
                    }*/

                    DonorsListAdapter adapter = new DonorsListAdapter(userModels,NearbyActivity.this);
                    recyclerView.setAdapter(adapter);



                }else if(position==1){
                    final ArrayList<UserModel> userModels1 = new ArrayList<>();
                         for(UserModel user:userModels){
                        if(user.getGroup().equalsIgnoreCase("O+")||user.getGroup().equalsIgnoreCase("O-")) {
                            userModels1.add(user);
                        }
                    }

                    DonorsListAdapter adapter = new DonorsListAdapter(userModels1,NearbyActivity.this);
                    recyclerView.setAdapter(adapter);
                }
                else if(position==2){
                    final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().equalsIgnoreCase("O-")) {
                            userModels1.add(user);
                        }
                    }

                    DonorsListAdapter adapter = new DonorsListAdapter(userModels1,NearbyActivity.this);
                    recyclerView.setAdapter(adapter);
                }else if(position==3){
                    final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().equalsIgnoreCase("O+")||user.getGroup().equalsIgnoreCase("O-")||user.getGroup().equalsIgnoreCase("A+")||user.getGroup().equalsIgnoreCase("A-")) {
                            userModels1.add(user);
                        }
                    }

                    DonorsListAdapter adapter = new DonorsListAdapter(userModels1,NearbyActivity.this);
                    recyclerView.setAdapter(adapter);
                }else if(position==4){
                    final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().equalsIgnoreCase("O-")||user.getGroup().equalsIgnoreCase("A-")) {
                            userModels1.add(user);
                        }
                    }

                    DonorsListAdapter adapter = new DonorsListAdapter(userModels1,NearbyActivity.this);
                    recyclerView.setAdapter(adapter);
                }else if(position==5){
                    final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().equalsIgnoreCase("O+")||user.getGroup().equalsIgnoreCase("O-")||user.getGroup().equalsIgnoreCase("B+")||user.getGroup().equalsIgnoreCase("B-")) {
                            userModels1.add(user);
                        }
                    }

                    DonorsListAdapter adapter = new DonorsListAdapter(userModels1,NearbyActivity.this);
                    recyclerView.setAdapter(adapter);
                }else if(position==6){
                    final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().equalsIgnoreCase("O-")||user.getGroup().equalsIgnoreCase("B-")) {
                            userModels1.add(user);
                        }
                    }

                    DonorsListAdapter adapter = new DonorsListAdapter(userModels1,NearbyActivity.this);
                    recyclerView.setAdapter(adapter);
                }else if(position==7){
                 /*   final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().equalsIgnoreCase("O+")||user.getGroup().equalsIgnoreCase("O-")||user.getGroup().equalsIgnoreCase("A+")||user.getGroup().equalsIgnoreCase("A-")) {
                            userModels1.add(user);
                        }
                    }*/

                    DonorsListAdapter adapter = new DonorsListAdapter(userModels,NearbyActivity.this);
                    recyclerView.setAdapter(adapter);
                }else if(position==8){
                    final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().contains("-"))
                            userModels1.add(user);
                        }


                    DonorsListAdapter adapter = new DonorsListAdapter(userModels1,NearbyActivity.this);
                    recyclerView.setAdapter(adapter);
                }
            }
        });


        GetContactTask getContactTask = new GetContactTask();
        getContactTask.execute();
    }



    private class GetContactTask extends AsyncTask<Context, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();





        }






        @Override
        protected String doInBackground(Context... params) {


            String abc = getAllContacts(NearbyActivity.this);

            return abc;


        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



          getUserId(All_urls.values.GetContacts,result,preferences.getString("PhoneNumber",""),preferences.getString("myLat",""),preferences.getString("mylang",""));

        }
    }

    public static String toCSV(ArrayList<String> array) {
        String result = "";
        if (array.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s1 : array) {
                String  s=s1.replaceAll("\\s","").replaceAll("-","");
                if(!s.matches(".*[a-zA-Z]+.*")) {
                    if(s.length()>0) {
                        if (s.substring(0, 1).equalsIgnoreCase("+")) {
                            sb.append(s.replaceAll("\\s", "")).append(",");
                        } else if (s.substring(0, 1).equalsIgnoreCase("0")) {
                            sb.append("+91" + s.substring(1).replaceAll("\\s", "")).append(",");
                        } else if (s.length() == 10) {
                            sb.append("+91" + s.replaceAll("\\s", "")).append(",");
                        }
                    }

                }
            }
            if(sb.length()>0)
                result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }


    public String getAllContacts(Context context) {

        ArrayList<String> arrContacts = new ArrayList();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Cursor cursor = context.getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,   ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.Contacts._ID}, selection, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            String contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            arrContacts.add(contactNumber);

            cursor.moveToNext();
        }
        cursor.close();



        return toCSV(arrContacts);
    }

    void  getUserId(String url, final String list, final String token,final String lat,final  String lang){
            progressBar.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                   progressBar.setVisibility(View.GONE);

                    if (response.length() > 0) {



                        JSONArray p = new JSONArray(response);

                        for(int i=p.length()-1;i>=0;i--){
                            UserModel chat_item= new UserModel();

                            chat_item.setName(p.getJSONObject(i).getString("name"));
                            chat_item.setGroup(p.getJSONObject(i).getString("bloodgroup"));
                            chat_item.setPhone(p.getJSONObject(i).getString("phone"));
                            chat_item.setDonor(p.getJSONObject(i).getInt("donor"));
                            chat_item.setFrequency(p.getJSONObject(i).getInt("frequency"));
                            chat_item.setGender(p.getJSONObject(i).getString("gender"));
                            chat_item.setLat(p.getJSONObject(i).getString("lat"));
                            chat_item.setLang(p.getJSONObject(i).getString("lang"));
                            chat_item.setContact(p.getJSONObject(i).getBoolean("isContact"));

                           userModels.add(chat_item);




                        }



                         Collections.shuffle(userModels);


                        adapter = new DonorsListAdapter( userModels,NearbyActivity.this);
                        recyclerView.setAdapter(adapter);


                    }






                } catch (Exception e) {
                    // JSON error

                    Log.d("Error", e.toString());

                  progressBar.setVisibility(View.GONE);

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Error", error.toString());


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("list",list);
                params.put("token",token);
                params.put("lat",lat);
                params.put("long",lang);



                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                5*60*1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(strReq,"");

    }


}
