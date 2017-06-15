package aleksey.khokhrin.ru.translator.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import aleksey.khokhrin.ru.translator.R;
import aleksey.khokhrin.ru.translator.databinding.ItemHistoryBinding;
import aleksey.khokhrin.ru.translator.model.Translate;
import aleksey.khokhrin.ru.translator.viewModel.BookmarkItemViewModel;

/**
 * Created by Aleksey on 19.04.2017.
 */

public class BookmarkRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = BookmarkRecyclerAdapter.class.getSimpleName();

    private List<Translate> items = new ArrayList<>();
    private Context context;

    public BookmarkRecyclerAdapter(Context context, List<Translate> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ItemHistoryBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_history,
                parent,
                false);
        return new ItemBookmarkViewHolder(binding, parent.getContext());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemBookmarkViewHolder)holder).bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ItemBookmarkViewHolder extends RecyclerView.ViewHolder {
        Context context;
        private ItemHistoryBinding binding;

        ItemBookmarkViewHolder(ItemHistoryBinding binding, Context context) {
            super(binding.cardViewItemHistory);
            this.binding = binding;
            this.context = context;
            this.binding.executePendingBindings();
        }

        void bind(Translate translate) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new BookmarkItemViewModel(translate, context));
            } else {
                binding.getViewModel().setTranslate(translate);
            }
        }
    }

}
