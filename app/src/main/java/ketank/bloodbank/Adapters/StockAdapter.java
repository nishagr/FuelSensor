package ketank.bloodbank.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ketank.bloodbank.Fragments.BankDonorsFragment;
import ketank.bloodbank.Fragments.BankStock;

public class StockAdapter extends FragmentPagerAdapter {
    String [] title = {"Stock","Donors NearBy"};

    public StockAdapter(FragmentManager childFragmentManager)
    {
        super(childFragmentManager);



    }


    @Override
    public Fragment getItem(int position) {

        if(position==0) {
            return new BankStock();
        }else
            return new BankDonorsFragment();


    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

}
