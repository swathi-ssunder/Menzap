package diy.net.menzap.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import java.util.Random;

import diy.net.menzap.R;
import diy.net.menzap.fragments.MenuFragment;
import diy.net.menzap.fragments.EventsFragment;
import diy.net.menzap.fragments.FriendsFragment;
import diy.net.menzap.helper.ReviewDBHelper;
import diy.net.menzap.service.AppLibService;

public class IconTabsActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private AppLibService appLibService;
    private static final Random RNG = new Random();
    private ServiceConnection serviceConnection;

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

        this.appLibService = new AppLibService();
        this.doBindService();
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_tab_favourite,
                R.drawable.ic_tab_contacts,
                R.drawable.ic_event_black_24dp
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
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
    //==========================================================================//
    // Service Handling
    //------------------------------------------------------------------------//
    // The new post activity must bind to the applib service in order to
    // publish the new message.
    //==========================================================================//
    private ServiceConnection getServiceConnection() {
        ServiceConnection sc = new ServiceConnection() {
            @Override
            public void onServiceConnected( ComponentName componentName,
                                            IBinder iBinder ) {
                if ( !( iBinder instanceof AppLibService.AppLibBinder ) ) {
                    Log.e( "Logs ::::", "Wrong type of binder in onServiceConnected()" );
                    return;
                }

                AppLibService.AppLibBinder binder =
                        ( AppLibService.AppLibBinder ) iBinder;
                IconTabsActivity.this.appLibService = binder.getService();
            }

            @Override
            public void onServiceDisconnected( ComponentName componentName ) {
                Log.d("on service disconnected", "here");
            }
        };

        return sc;
    }

    private void doBindService() {
        this.serviceConnection = this.getServiceConnection();
        Intent intent = new Intent( this, AppLibService.class );
        super.bindService(intent,
                this.serviceConnection, Context.BIND_AUTO_CREATE );
    }

    private void doUnbindService() {
        super.unbindService( this.serviceConnection );
    }

    public void saveAndPublish(String reviewText) {
        long timestamp = System.currentTimeMillis();
        long uniqueid = RNG.nextLong();

        // Update the database
        ReviewDBHelper review = new ReviewDBHelper(this);
        review.insert(reviewText, 1);
        ArrayList ar = review.getAll();
        Log.d("CUSTOM INFO: REVIEWS", ar.toString());

        // Send the message
        if (this.appLibService != null) {
            boolean published = this.appLibService.publish("xx",reviewText ,
                    timestamp, uniqueid);
            Log.d("CUSTOM INFO: REVIEWS",  Boolean.toString(published));

            if (published) {
                Log.d("CUSTOM INFO", "published");
            }
        } else {
            Log.d("CUSTOM INFO: REVIEWS", "Couldn't send message, no AppLib instance.");
        }
    }
}
