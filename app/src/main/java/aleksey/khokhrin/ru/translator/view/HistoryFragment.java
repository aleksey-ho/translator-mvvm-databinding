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
import aleksey.khokhrin.ru.translator.databinding.FragmentHistoryBinding;
import aleksey.khokhrin.ru.translator.model.Translate;
import aleksey.khokhrin.ru.translator.utils.Events;

public class HistoryFragment extends Fragment {
    private static final String TAG = HistoryFragment.class.getSimpleName();
    private FragmentHistoryBinding binding;

    public HistoryFragment() { }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);

        List<Translate> translatesInHistory = DatabaseHelper.getInstance().getTranslatesInHistory();
        BookmarkRecyclerAdapter bookmarkRecyclerAdapter = new BookmarkRecyclerAdapter(getContext(), translatesInHistory);
        binding.listViewHistory.setAdapter(bookmarkRecyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.listViewHistory.setLayoutManager(linearLayoutManager);

        binding.historyEmpty.setVisibility(translatesInHistory.size() > 0 ? View.GONE : View.VISIBLE);

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
    public void updateHistoryEventHandler(Events.UpdateHistoryTabEvent event) {
        binding.listViewHistory.getAdapter().notifyDataSetChanged();
        List<Translate> translatesInHistory = DatabaseHelper.getInstance().getTranslatesInHistory();
        binding.historyEmpty.setVisibility(translatesInHistory.size() > 0 ? View.GONE : View.VISIBLE);
    }

}
