package ketank.bloodbank.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import ketank.bloodbank.Models.BloodBank;
import ketank.bloodbank.Models.UserModel;
import ketank.bloodbank.R;


public class BankListAdapter extends RecyclerView.Adapter <BankListAdapter.MyViewHolder>{
    ArrayList<BloodBank> bloodBanks;
    Context context;

    public BankListAdapter(ArrayList<BloodBank> bloodBanks, Context context) {
        this.bloodBanks = bloodBanks;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baks, parent, false);
       MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.name.setText(bloodBanks.get(position).getName());
        holder.group.setText(bloodBanks.get(position).getPlace());

        Glide
                .with(context)
                .load(bloodBanks.get(position).getImgUrl())
                .centerCrop()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.image);




    }

    @Override
    public int getItemCount() {
        return bloodBanks.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,group;
        ImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.name);
            group = itemView.findViewById(R.id.location);
            image= itemView.findViewById(R.id.image);



        }
    }
}
