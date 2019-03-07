package ketank.bloodbank.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.ValueIterator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ketank.bloodbank.Adapters.BankDonorsListAdapter;
import ketank.bloodbank.Fragments.User.NearbyActivity;
import ketank.bloodbank.Models.UserModel;
import ketank.bloodbank.Other.AppController;
import ketank.bloodbank.R;
import ketank.bloodbank.Urls.All_urls;

/**
 * A simple {@link Fragment} subclass.
 */
public class BankDonorsFragment extends Fragment {

 View view;
    SharedPreferences preferences;
    ArrayList<UserModel> userModels = new ArrayList<>();
    ProgressBar progressBar;
    RecyclerView recyclerView;
    BankDonorsListAdapter adapter;
    MaterialSpinner spinner;
    public BankDonorsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_nearby,container,false);

        spinner = (MaterialSpinner) view.findViewById(R.id.spinner);

        spinner.setItems("All", "O+","O-","A+", "A-","B+","B-","AB+","AB-");

        progressBar = view.findViewById(R.id.progd);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView =view.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        preferences= getActivity().getSharedPreferences("mypref",Context.MODE_PRIVATE);

        UserNearBank(All_urls.values.UserNearBank,preferences.getString("BankLat",""),preferences.getString("BankLang",""));

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(position==0){


                 /*   for(UserModel user:userModels){
                        if(user.g>0) {
                            bloodBanks1.add(bank);
                        }
                    }*/

                    BankDonorsListAdapter adapter = new BankDonorsListAdapter(userModels,getActivity());
                    recyclerView.setAdapter(adapter);



                }else if(position==1){
                    final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().equalsIgnoreCase("O+")||user.getGroup().equalsIgnoreCase("O-")) {
                            userModels1.add(user);
                        }
                    }

                    BankDonorsListAdapter adapter = new BankDonorsListAdapter(userModels1,getActivity());
                    recyclerView.setAdapter(adapter);
                }
                else if(position==2){
                    final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().equalsIgnoreCase("O-")) {
                            userModels1.add(user);
                        }
                    }

                    BankDonorsListAdapter adapter = new BankDonorsListAdapter(userModels1,getActivity());
                    recyclerView.setAdapter(adapter);
                }else if(position==3){
                    final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().equalsIgnoreCase("O+")||user.getGroup().equalsIgnoreCase("O-")||user.getGroup().equalsIgnoreCase("A+")||user.getGroup().equalsIgnoreCase("A-")) {
                            userModels1.add(user);
                        }
                    }

                    BankDonorsListAdapter adapter = new BankDonorsListAdapter(userModels1,getActivity());
                    recyclerView.setAdapter(adapter);
                }else if(position==4){
                    final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().equalsIgnoreCase("O-")||user.getGroup().equalsIgnoreCase("A-")) {
                            userModels1.add(user);
                        }
                    }

                    BankDonorsListAdapter adapter = new BankDonorsListAdapter(userModels1,getActivity());
                    recyclerView.setAdapter(adapter);
                }else if(position==5){
                    final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().equalsIgnoreCase("O+")||user.getGroup().equalsIgnoreCase("O-")||user.getGroup().equalsIgnoreCase("B+")||user.getGroup().equalsIgnoreCase("B-")) {
                            userModels1.add(user);
                        }
                    }

                    BankDonorsListAdapter adapter = new BankDonorsListAdapter(userModels1,getActivity());
                    recyclerView.setAdapter(adapter);
                }else if(position==6){
                    final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().equalsIgnoreCase("O-")||user.getGroup().equalsIgnoreCase("B-")) {
                            userModels1.add(user);
                        }
                    }

                    BankDonorsListAdapter adapter = new BankDonorsListAdapter(userModels1,getActivity());
                    recyclerView.setAdapter(adapter);
                }else if(position==7){
                 /*   final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().equalsIgnoreCase("O+")||user.getGroup().equalsIgnoreCase("O-")||user.getGroup().equalsIgnoreCase("A+")||user.getGroup().equalsIgnoreCase("A-")) {
                            userModels1.add(user);
                        }
                    }*/

                    BankDonorsListAdapter adapter = new BankDonorsListAdapter(userModels,getActivity());
                    recyclerView.setAdapter(adapter);
                }else if(position==8){
                    final ArrayList<UserModel> userModels1 = new ArrayList<>();
                    for(UserModel user:userModels){
                        if(user.getGroup().contains("-"))
                            userModels1.add(user);
                    }


                    BankDonorsListAdapter adapter = new BankDonorsListAdapter(userModels1,getActivity());
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        return view;
    }

    void  UserNearBank(String url,final String lat,final  String lang){
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


                            userModels.add(chat_item);




                        }



                        Collections.shuffle(userModels);


                        adapter = new BankDonorsListAdapter( userModels,getActivity());
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
