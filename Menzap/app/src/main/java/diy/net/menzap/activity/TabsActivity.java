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
import diy.net.menzap.fragments.MenuFragment;
import diy.net.menzap.fragments.EventsFragment;
import diy.net.menzap.fragments.FriendsFragment;
import diy.net.menzap.helper.DataHolder;
import diy.net.menzap.helper.ReviewDBHelper;

public class TabsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

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
            }
        }

        /*Set the Menu tab as active*/
        tabLayout.getTabAt(0).select();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch(position) {
                    case 0:
                        /*Invoke onSelected on MenuFragment*/
                        MenuFragment menuFragment = (MenuFragment)TabsActivity.this.adapter.getItem(position);
                        menuFragment.onSelected();
                        break;
                    case 1:
                        /*Invoke onSelected on FriendFragment*/
                        FriendsFragment friendsFragment = (FriendsFragment)TabsActivity.this.adapter.getItem(position);
                        friendsFragment.onSelected();
                        break;
                    case 2:
                        /*Invoke onSelected on EventFragment*/
                        EventsFragment eventsFragment = (EventsFragment) TabsActivity.this.adapter.getItem(position);
                        eventsFragment.onSelected();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupTabIcons() {
        JSONArray tabs = new JSONArray();

        try {
            JSONObject tabMenu = new JSONObject();
            tabMenu.put("text", "MENU");
            tabMenu.put("icon", R.drawable.ic_tab_menu);

            JSONObject friendMenu = new JSONObject();
            friendMenu.put("text", "FRIEND");
            friendMenu.put("icon", R.drawable.ic_tab_contacts);

            JSONObject eventMenu = new JSONObject();
            eventMenu.put("text", "EVENT");
            eventMenu.put("icon", R.drawable.ic_tab_events);

            tabs.put(0, tabMenu);
            tabs.put(1, friendMenu);
            tabs.put(2, eventMenu);

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
    }
}
