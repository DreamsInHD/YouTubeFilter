package com.dreamsinhd.android.youtubefilter;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.dreamsinhd.android.youtubefilter.interfaces.BottomNavActivity;
import com.dreamsinhd.android.youtubefilter.interfaces.FilterActivity;
import com.dreamsinhd.android.youtubefilter.interfaces.NavigationActivity;
import com.dreamsinhd.android.youtubefilter.model.Filter;

public class MainActivity extends AppCompatActivity implements NavigationActivity, BottomNavActivity, FilterActivity {
    private Filter filter = new Filter();

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();

        if(fm.findFragmentById(R.id.fragment_container) == null) {
            Fragment fragment = VideoListFragment.newInstance(new Filter());
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.filter_page_menu_item:
                        navigateTo(FilterFragment.newInstance(filter), false);
                        return true;
                    case R.id.video_page_menu_item:
                        navigateTo(VideoListFragment.newInstance(filter), false);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment).commit();

        if(addToBackStack) {
            transaction.addToBackStack(null);
        }
    }

    @Override
    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
