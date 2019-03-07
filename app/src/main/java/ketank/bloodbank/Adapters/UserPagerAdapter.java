package ketank.bloodbank.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ketank.bloodbank.Fragments.BankStock;

public class UserPagerAdapter extends FragmentPagerAdapter {
    String [] title = {"Blood Banks","Donors NearBy"};

    public UserPagerAdapter(FragmentManager childFragmentManager)
    {
        super(childFragmentManager);



    }


    @Override
    public Fragment getItem(int position) {

        if(position==0) {
            return new BankStock();
        }else
            return new BankStock();


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
