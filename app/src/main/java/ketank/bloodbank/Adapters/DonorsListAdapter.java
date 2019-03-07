package ketank.bloodbank.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ketank.bloodbank.Models.UserModel;
import ketank.bloodbank.Other.AppController;
import ketank.bloodbank.R;
import ketank.bloodbank.Urls.All_urls;


public class DonorsListAdapter extends RecyclerView.Adapter <DonorsListAdapter.MyViewHolder>{
    ArrayList<UserModel> userModels;
    Context context;
    SharedPreferences preferences;

    public DonorsListAdapter(ArrayList<UserModel> userModels, Context context) {
        this.userModels = userModels;
        this.context = context;

        preferences= context.getSharedPreferences("mypref",Context.MODE_PRIVATE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donor, parent, false);
       MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.name.setText(userModels.get(position).getName());
        holder.group.setText(userModels.get(position).getGroup());

        if(!userModels.get(position).isContact()){
            holder.call.setVisibility(View.GONE);
        }

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", userModels.get(position).getPhone(), null));
                context.startActivity(intent);
            }
        });


        holder.notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               NotifyUser(All_urls.values.NotifyByUser,userModels.get(position).getPhone(),preferences.getString("PhoneNumber",""));
            }
        });




    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,group;
        ImageView call,notify;


        public MyViewHolder(View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.name);
            group = itemView.findViewById(R.id.BloodGroup);
            call = itemView.findViewById(R.id.call);
            notify = itemView.findViewById(R.id.notify);



        }
    }

    public void  NotifyUser(String url, final String phone, final String userphone){

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Notifying User");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String p) {
                        // display response
                        Log.d("Response", p);

                        progressDialog.dismiss();





                                Toast.makeText(context,"User Notified",Toast.LENGTH_LONG).show();





                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Notify error",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                }
        )    {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("phone", phone);
                params.put("userphone", userphone);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(getRequest,"");
    }

}
