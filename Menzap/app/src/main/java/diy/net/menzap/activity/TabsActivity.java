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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import diy.net.menzap.R;
import diy.net.menzap.fragments.ImageFragment;
import diy.net.menzap.fragments.MenuFragment;
import diy.net.menzap.fragments.EventsFragment;
import diy.net.menzap.fragments.FriendsFragment;
import diy.net.menzap.fragments.StatsFragment;
import diy.net.menzap.helper.DataHolder;

public class TabsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Toolbar toolbar;
        ViewPager viewPager;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

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
            switch(getIntent().getAction()) {
                case "TAB_MENU":
                    /*Set the Menu tab as active*/
                    tabLayout.getTabAt(0).select();
                    break;
                case "TAB_FRIEND":
                    /*Set the Friend tab as active*/
                    tabLayout.getTabAt(1).select();
                    break;
                case "TAB_EVENT":
                    /*Set the Events tab as active*/
                    tabLayout.getTabAt(2).select();
                    break;
                case "TAB_STATS":
                    /*Set the Statistics tab as active*/
                    tabLayout.getTabAt(3).select();
                    break;
                case "TAB_IMAGE":
                    /*Set the Image tab as active*/
                    tabLayout.getTabAt(4).select();
                    break;
            }
        }
    }

    private void setupTabIcons() {
        JSONArray tabs = new JSONArray();

        try {
            JSONObject tabMenu = new JSONObject();
            tabMenu.put("text", "MENU");
            tabMenu.put("icon", R.drawable.ic_tab_menu);

            JSONObject friendMenu = new JSONObject();
            friendMenu.put("text", "FRIEND");
            friendMenu.put("icon", R.drawable.ic_tab_users);

            JSONObject eventMenu = new JSONObject();
            eventMenu.put("text", "EVENT");
            eventMenu.put("icon", R.drawable.ic_tab_events);

            JSONObject statsMenu = new JSONObject();
            statsMenu.put("text", "STATS");
            statsMenu.put("icon", R.drawable.ic_tab_stats);

            JSONObject imageMenu = new JSONObject();
            imageMenu.put("text", "IMAGE");
            imageMenu.put("icon", R.drawable.ic_tab_gallery);

            tabs.put(0, tabMenu);
            tabs.put(1, friendMenu);
            tabs.put(2, eventMenu);
            tabs.put(3, statsMenu);
            tabs.put(4, imageMenu);

            for(int i = 0; i < tabs.length(); i++) {
                if(tabLayout.getTabAt(i) != null) {
                    tabLayout.getTabAt(i).setTag(tabs.getJSONObject(i).getString("text"));
                    tabLayout.getTabAt(i).setIcon(tabs.getJSONObject(i).getInt("icon"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        this.adapter = new ViewPagerAdapter(getSupportFragmentManager());
        this.adapter.addFrag(new MenuFragment(), "Menu Today");
        this.adapter.addFrag(new FriendsFragment(), "Friends in Mensa");
        this.adapter.addFrag(new EventsFragment(), "Upcoming Events");
        this.adapter.addFrag(new StatsFragment(), "Statistics");
        this.adapter.addFrag(new ImageFragment(), "Mensa Images");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(this.adapter);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataHolder.getInstance().destroyHelper();
        finish();
    }
}
