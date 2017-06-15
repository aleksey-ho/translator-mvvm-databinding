package aleksey.khokhrin.ru.translator.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import aleksey.khokhrin.ru.translator.R;
import aleksey.khokhrin.ru.translator.databinding.ActivityMainBinding;
import aleksey.khokhrin.ru.translator.utils.Events;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        MainViewPageAdapter adapter = new MainViewPageAdapter(getSupportFragmentManager());

        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    EventBus.getDefault().post(new Events.UpdateFavouritesTabEvent());
                    EventBus.getDefault().post(new Events.UpdateHistoryTabEvent());
                }
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        TabLayout tabLayout = binding.slidingTabs;
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.GRAVITY_CENTER);

        tabLayout.setSelectedTabIndicatorHeight(4);

        //set drawables for each tab
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_translate);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_bookmark);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_settings);

    }

    class MainViewPageAdapter extends FragmentPagerAdapter {
        public List<String> pageTitles = new ArrayList<>();

        public MainViewPageAdapter(FragmentManager fm) {
            super(fm);
            pageTitles.add("");
            pageTitles.add("");
            pageTitles.add("");
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            if (position == 0)
                fragment = new TranslateFragment();
            else if(position == 1)
                fragment = new BookmarkFragment();
            else
                fragment = new SettingsFragment();
            return fragment;
        }

        @Override
        public int getCount() {
            return pageTitles.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitles.get(position);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void openTranslateEventHandler(Events.OpenTranslateEvent event) {
        binding.viewPager.setCurrentItem(0, true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    EventBus.getDefault().post(new Events.SaveTranslateEvent());
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

}
