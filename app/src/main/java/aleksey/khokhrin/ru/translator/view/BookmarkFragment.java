package aleksey.khokhrin.ru.translator.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import aleksey.khokhrin.ru.translator.R;
import aleksey.khokhrin.ru.translator.data.DatabaseHelper;
import aleksey.khokhrin.ru.translator.databinding.FragmentBookmarkBinding;
import aleksey.khokhrin.ru.translator.utils.Events;
import aleksey.khokhrin.ru.translator.viewModel.BookmarkViewModel;

public class BookmarkFragment extends Fragment {
    private static final String TAG = BookmarkFragment.class.getSimpleName();
    private BookmarkViewModel viewModel;
    private FragmentBookmarkBinding binding;
    private BookmarkFragmentViewPageAdapter adapter;
    private ViewPager viewPager;

    public BookmarkFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false);
        viewModel = new BookmarkViewModel(getContext());
        binding.setViewModel(viewModel);

        FragmentActivity context = (FragmentActivity) getContext();
        adapter = new BookmarkFragmentViewPageAdapter(context.getSupportFragmentManager());

        viewPager = binding.bookmarkViewPager;
        viewPager.setAdapter(adapter);

        binding.tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                EventBus.getDefault().post(new Events.UpdateFavouritesTabEvent());
                EventBus.getDefault().post(new Events.UpdateHistoryTabEvent());
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        return binding.getRoot();
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
    public void clearBookmarksEventHandler(Events.ClearBookmarksEvent event) {
        int currentItem = viewPager.getCurrentItem();
        String message = currentItem == 0 ? getString(R.string.delete_history_question) :
                getString(R.string.delete_favourites_question);

        AlertDialog.Builder messageBox = new AlertDialog.Builder(getContext());
        messageBox.setTitle(currentItem == 0 ? R.string.view_pager_history : R.string.view_pager_favourites);
        messageBox.setMessage(message);
        messageBox.setPositiveButton(R.string.yes, (dialog, which) -> {
            if (currentItem == 0) {
                DatabaseHelper.getInstance().getInstance().clearHistory();
                EventBus.getDefault().post(new Events.UpdateHistoryTabEvent());
            }
            else {
                DatabaseHelper.getInstance().clearFavourites();
                EventBus.getDefault().post(new Events.UpdateFavouritesTabEvent());
            }
        });
        messageBox.setNegativeButton(R.string.cancel, null);
        messageBox.show();
    }

    class BookmarkFragmentViewPageAdapter extends FragmentPagerAdapter {
        public List<String> pageTitles = new ArrayList<>();

        public BookmarkFragmentViewPageAdapter(FragmentManager fm) {
            super(fm);
            pageTitles.add(getString(R.string.view_pager_history));
            pageTitles.add(getString(R.string.view_pager_favourites));
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            if (position == 0)
                fragment = HistoryFragment.newInstance();
            else
                fragment = FavouritesFragment.newInstance();
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
}
