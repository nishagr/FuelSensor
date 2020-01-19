package ketank.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import ketank.bloodbank.FuelReadingApi.ApiClient;
import ketank.bloodbank.FuelReadingApi.ApiInterface;
import ketank.bloodbank.Models.reviews;
import ketank.bloodbank.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class rating extends AppCompatActivity {
    Button b1,b2;
    RatingBar r;
    EditText e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width),(int)(height*.4));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -10;
        getWindow().setAttributes(params);


        b1=findViewById(R.id.rb);
//        e=findViewById(R.id.ret);
        Bundle bundle = getIntent().getExtras();
        int id= bundle.getInt("message");
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e.setVisibility(View.VISIBLE);

            }
        });

        b2=findViewById(R.id.rsubmit);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r=findViewById(R.id.rbar);
                int s=r.getNumStars();
                // int s= Integer.parseInt(e.getText().toString());

                sendPost(s,id);
            }
        });
    }

    private void sendPost(int s,int id) {
        ApiInterface c=ApiClient.getApiClient().create(ApiInterface.class);
        c.savePost(s,id).enqueue(new Callback<reviews>() {
            @Override
            public void onResponse(Call<reviews> call, Response<reviews> response) {
                Toast.makeText(getApplicationContext(),"Rated successfully",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<reviews> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();
                finish();


            }
        });

    }
}
