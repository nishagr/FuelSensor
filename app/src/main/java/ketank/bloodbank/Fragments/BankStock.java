package ketank.bloodbank.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ketank.bloodbank.Adapters.BankListAdapter;
import ketank.bloodbank.Models.BloodBank;
import ketank.bloodbank.Other.AppController;
import ketank.bloodbank.R;
import ketank.bloodbank.Urls.All_urls;

public class BankStock extends Fragment {
    TextView B1, B2, B3, B4, B5, B6, B7, B8;
    View view;
    SharedPreferences preferences;

    MaterialSpinner spinner;
    public BankStock() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.layout_stock,container,false);

        spinner = (MaterialSpinner) view.findViewById(R.id.spinner);
        preferences= getActivity().getSharedPreferences("mypref",Context.MODE_PRIVATE);

        spinner.setItems("All", "O+","O-","A+", "A-","B+","B-","AB+","AB-");

        B1= view.findViewById(R.id.B1);
        B2= view.findViewById(R.id.B2);
        B3= view.findViewById(R.id.B3);
        B4= view.findViewById(R.id.B4);
        B5= view.findViewById(R.id.B5);
        B6= view.findViewById(R.id.B6);
        B7= view.findViewById(R.id.B7);
        B8= view.findViewById(R.id.B8);


        

        getData(All_urls.values.Getbankstock(preferences.getString("bbid",null)));

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                if(position==0){
                    B1.setVisibility(View.VISIBLE);
                    B2.setVisibility(View.VISIBLE);
                    B3.setVisibility(View.VISIBLE);
                    B4.setVisibility(View.VISIBLE);
                    B5.setVisibility(View.VISIBLE);
                    B6.setVisibility(View.VISIBLE);
                    B7.setVisibility(View.VISIBLE);
                    B8.setVisibility(View.VISIBLE);

                }    else   if(position==1){

                    B1.setVisibility(View.VISIBLE);
                    B2.setVisibility(View.GONE);
                    B3.setVisibility(View.GONE);
                    B4.setVisibility(View.GONE);
                    B5.setVisibility(View.GONE);
                    B6.setVisibility(View.GONE);
                    B7.setVisibility(View.GONE);
                    B8.setVisibility(View.GONE);

                }
                else if(position==2){
                    B1.setVisibility(View.GONE);
                    B2.setVisibility(View.VISIBLE);
                    B3.setVisibility(View.GONE);
                    B4.setVisibility(View.GONE);
                    B5.setVisibility(View.GONE);
                    B6.setVisibility(View.GONE);
                    B7.setVisibility(View.GONE);
                    B8.setVisibility(View.GONE);


                } else   if(position==3){

                    B1.setVisibility(View.GONE);
                    B2.setVisibility(View.GONE);
                    B3.setVisibility(View.VISIBLE);
                    B4.setVisibility(View.GONE);
                    B5.setVisibility(View.GONE);
                    B6.setVisibility(View.GONE);
                    B7.setVisibility(View.GONE);
                    B8.setVisibility(View.GONE);


                } else   if(position==4){
                    B1.setVisibility(View.GONE);
                    B2.setVisibility(View.GONE);
                    B3.setVisibility(View.GONE);
                    B4.setVisibility(View.VISIBLE);
                    B5.setVisibility(View.GONE);
                    B6.setVisibility(View.GONE);
                    B7.setVisibility(View.GONE);
                    B8.setVisibility(View.GONE);



                } else   if(position==5){

                    B1.setVisibility(View.GONE);
                    B2.setVisibility(View.GONE);
                    B3.setVisibility(View.GONE);
                    B4.setVisibility(View.GONE);
                    B5.setVisibility(View.VISIBLE);
                    B6.setVisibility(View.GONE);
                    B7.setVisibility(View.GONE);
                    B8.setVisibility(View.GONE);


                } else   if(position==6){

                    B1.setVisibility(View.GONE);
                    B2.setVisibility(View.GONE);
                    B3.setVisibility(View.GONE);
                    B4.setVisibility(View.GONE);
                    B5.setVisibility(View.GONE);
                    B6.setVisibility(View.VISIBLE);
                    B7.setVisibility(View.GONE);
                    B8.setVisibility(View.GONE);



                } else   if(position==7){
                    B1.setVisibility(View.GONE);
                    B2.setVisibility(View.GONE);
                    B3.setVisibility(View.GONE);
                    B4.setVisibility(View.GONE);
                    B5.setVisibility(View.GONE);
                    B6.setVisibility(View.GONE);
                    B7.setVisibility(View.VISIBLE);
                    B8.setVisibility(View.GONE);


                } else   if(position==8){
                    B1.setVisibility(View.GONE);
                    B2.setVisibility(View.GONE);
                    B3.setVisibility(View.GONE);
                    B4.setVisibility(View.GONE);
                    B5.setVisibility(View.GONE);
                    B6.setVisibility(View.GONE);
                    B7.setVisibility(View.GONE);
                    B8.setVisibility(View.VISIBLE);

                }

            }
        });
        


        return view;
    }

    private void getData(String url) {

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray p) {
                        // display response
                        Log.d("Response", p.toString());

                        //dialog.dismiss();
                        try {



                            if (p.length() > 0) {


                                    BloodBank mapModel = new BloodBank();

                                    JSONObject post = p.getJSONObject(0);
                                    mapModel.setId(post.getString("BB_id"));
                                    mapModel.setOps(post.getInt("Ops"));
                                    mapModel.setOng(post.getInt("Ong"));
                                    mapModel.setAng(post.getInt("Ang"));
                                    mapModel.setAps(post.getInt("Aps"));
                                    mapModel.setAbps(post.getInt("ABps"));
                                    mapModel.setAbng(post.getInt("ABng"));
                                    mapModel.setBps(post.getInt("Bps"));
                                    mapModel.setBng(post.getInt("Bng"));


                                B1.setText("      O+                         "+mapModel.getOps());
                                B2.setText("      O-                        "+mapModel.getOng());
                                B3.setText("      A+                         "+mapModel.getAps());
                                B4.setText("      A-                        "+mapModel.getAng());
                                B5.setText("      B+                         "+mapModel.getBps());
                                B6.setText("      B-                       "+mapModel.getBng());
                                B7.setText("      AB+                         "+mapModel.getAbps());
                                B8.setText("      AB-                        "+mapModel.getAbng());

                                ArrayList<String> less = new ArrayList<>();

                                if(mapModel.getOps()<10){
                                    less.add("O+");
                                }
                                if(mapModel.getOng()<10){
                                    less.add("O-");
                                }
                                if(mapModel.getAps()<10){
                                    less.add("A+");
                                }
                                if(mapModel.getAng()<10){
                                    less.add("A-");
                                }
                                if(mapModel.getBps()<10){
                                          less.add("B+");
                                }
                                if(mapModel.getBng()<10){
                                    less.add("B-");
                                }
                                if(mapModel.getAbps()<10){
                                    less.add("AB+");
                                }
                                if(mapModel.getAbng()<10){
                                    less.add("AB-");
                                }

                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(getActivity());
                                }
                                StringBuilder s=new StringBuilder();
                                for(int i=0;i<less.size();i++){
                                    s.append(less.get(i));
                                    if(i<less.size()-1) {
                                        s.append(" , ");
                                    }
                                }
                                builder.setTitle("Blood Requires")
                                        .setMessage(s.toString())
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();





                            }


                        } catch (Exception e) {
                            // JSON error
                            e.printStackTrace();
                            //dialog.dismiss();



                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "");
                        //dialog.dismiss();


                    }
                }
        );


        AppController.getInstance().addToRequestQueue(getRequest);
    }


}
