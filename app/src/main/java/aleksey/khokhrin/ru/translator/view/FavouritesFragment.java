package aleksey.khokhrin.ru.translator.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import aleksey.khokhrin.ru.translator.R;
import aleksey.khokhrin.ru.translator.adapters.BookmarkRecyclerAdapter;
import aleksey.khokhrin.ru.translator.data.DatabaseHelper;
import aleksey.khokhrin.ru.translator.databinding.FragmentFavouriteBinding;
import aleksey.khokhrin.ru.translator.model.Translate;
import aleksey.khokhrin.ru.translator.utils.Events;

public class FavouritesFragment extends Fragment {
    private static final String TAG = FavouritesFragment.class.getSimpleName();
    private FragmentFavouriteBinding binding;

    public FavouritesFragment() { }

    public static FavouritesFragment newInstance() {
        return new FavouritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite, container, false);

        List<Translate> favouriteTranslates = DatabaseHelper.getInstance().getFavouriteTranslates();
        BookmarkRecyclerAdapter bookmarkRecyclerAdapter = new BookmarkRecyclerAdapter(getContext(), favouriteTranslates);
        binding.listViewFavourite.setAdapter(bookmarkRecyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.listViewFavourite.setLayoutManager(linearLayoutManager);

        binding.favouritesEmpty.setVisibility(favouriteTranslates.size() > 0 ? View.GONE : View.VISIBLE);

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
    public void updateFavouritesEventHandler(Events.UpdateFavouritesTabEvent event) {
        binding.listViewFavourite.getAdapter().notifyDataSetChanged();
        List<Translate> favouriteTranslates = DatabaseHelper.getInstance().getFavouriteTranslates();
        binding.favouritesEmpty.setVisibility(favouriteTranslates.size() > 0 ? View.GONE : View.VISIBLE);
    }

}
