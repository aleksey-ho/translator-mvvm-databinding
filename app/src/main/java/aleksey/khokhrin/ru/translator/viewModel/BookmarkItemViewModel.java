package aleksey.khokhrin.ru.translator.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import aleksey.khokhrin.ru.translator.R;
import aleksey.khokhrin.ru.translator.data.DatabaseHelper;
import aleksey.khokhrin.ru.translator.model.Translate;
import aleksey.khokhrin.ru.translator.utils.Events;

/**
 * Created by Aleksey on 20.04.2017.
 */

public class BookmarkItemViewModel extends BaseObservable {
    private static final String TAG = BookmarkItemViewModel.class.getSimpleName();

    private Context context;
    private Translate translate;

    public BookmarkItemViewModel(Translate translate, Context context) {
        this.translate = translate;
        this.context = context;
    }

    public Translate getTranslate() {
        return translate;
    }

    public void setTranslate(Translate translate) {
        this.translate = translate;
        notifyChange();
    }

    public void onSaveAsFavouriteClick(View view) {
        if (translate.isSavedInFavourites())
            DatabaseHelper.getInstance().getInstance().removeFromFavourites(translate);
        else
            DatabaseHelper.getInstance().getInstance().saveAsFavourite(translate);

        notifyChange();

    }

    public void onShowClick(View view) {
        EventBus.getDefault().post(new Events.OpenTranslateEvent(translate));
    }

    public Drawable imageFavourite()
    {
        int retValue = translate.isSavedInFavourites() ? R.drawable.ic_favourive_light : R.drawable.ic_favourite_dark;
        return ContextCompat.getDrawable(context, retValue);
    }

    public String getTextSource()
    {
        return translate.getTextSource().replace("\n", "").replace("\r", "");
    }

    public String getTextTarget()
    {
        return translate.getTextTarget().replace("\n", " ").replace("\r", "");
    }

}
