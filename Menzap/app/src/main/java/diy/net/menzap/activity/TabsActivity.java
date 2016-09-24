package diy.net.menzap.activity;

/**
 * Created by swathissunder on 15/09/16.
 */

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import diy.net.menzap.R;
import diy.net.menzap.fragments.MenuFragment;
import diy.net.menzap.fragments.EventsFragment;
import diy.net.menzap.fragments.FriendsFragment;
import diy.net.menzap.helper.DataHolder;
import diy.net.menzap.helper.ReviewDBHelper;

public class TabsActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Toolbar toolbar;
        ViewPager viewPager;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_tabs);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        DataHolder.getInstance().initHelper(this);

        /*When the activity is opened from Notification*/
        if(getIntent().getAction() != null) {
            if(getIntent().getAction().equals("TAB_EVENT")) {
                /*Set the Events tab as active*/
                tabLayout.getTabAt(2).select();
            }
        }
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_tab_favourite,
                R.drawable.ic_tab_contacts,
                R.drawable.ic_event_black_24dp
        };

        for(int i = 0; i < tabIcons.length; i++) {
            if(tabLayout.getTabAt(i) != null) {
                tabLayout.getTabAt(i).setIcon(tabIcons[i]);
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MenuFragment(), "Menu Today");
        adapter.addFrag(new FriendsFragment(), "Friends in Mensa");
        adapter.addFrag(new EventsFragment(), "Upcoming Events");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }

    public void saveAndPublish(String reviewText) {
        // Update the database
        ReviewDBHelper review = new ReviewDBHelper(this);
        review.insert(reviewText, 1);
        ArrayList ar = review.getAll();
        Log.d("CUSTOM INFO: REVIEWS", ar.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataHolder.getInstance().destroyHelper();
    }
}
