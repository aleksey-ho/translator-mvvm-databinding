package aleksey.khokhrin.ru.translator.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import aleksey.khokhrin.ru.translator.utils.Events;

/**
 * Created by Aleksey on 16.04.2017.
 */

public class BookmarkViewModel extends BaseObservable {
    public Context context;

    public BookmarkViewModel(Context context) {
        super();
        this.context = context;
    }

    public void clearBookmarks(View view) {
        EventBus.getDefault().post(new Events.ClearBookmarksEvent());
    }

}
