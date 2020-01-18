package ketank.bloodbank.Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
