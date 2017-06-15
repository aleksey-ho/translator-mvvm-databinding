package aleksey.khokhrin.ru.translator;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.nytimes.android.external.store.base.impl.Store;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;
import java.util.Map;

import aleksey.khokhrin.ru.translator.data.DatabaseHelper;
import aleksey.khokhrin.ru.translator.data.SharedPreferenceHelper;
import aleksey.khokhrin.ru.translator.data.store.DataStore;
import aleksey.khokhrin.ru.translator.data.store.TranslateStoreModel;
import aleksey.khokhrin.ru.translator.data.retrofit.model.TranslateModel;
import aleksey.khokhrin.ru.translator.utils.Events;
import io.realm.Realm;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static aleksey.khokhrin.ru.translator.data.SharedPreferenceHelper.FIRST_APP_LAUNCH_PREF;

/**
 * Created by Aleksey on 15.04.2017.
 */

public class TranslatorApplication extends MultiDexApplication {
    private static final String TAG = TranslatorApplication.class.getSimpleName();

    private static Context appContext;

    private DataStore dataStore;

    @Override
    public void onCreate() {
        super.onCreate();
        TranslatorApplication.appContext = getApplicationContext();

        dataStore = new DataStore();

        Realm.init(this);

        //indicate first launch
        boolean isFirstLaunch = SharedPreferenceHelper.getInstance().getBooleanValue(this, FIRST_APP_LAUNCH_PREF, true);
        if (isFirstLaunch) {
            DatabaseHelper.getInstance().initLanguages();
            SharedPreferenceHelper.getInstance().saveBoolean(this, false, FIRST_APP_LAUNCH_PREF);
        }

        updateLanguages(Locale.getDefault().getLanguage());
    }

    public Store<TranslateModel, TranslateStoreModel> getTranslateStore() {
        return dataStore.getTranslateStore();
    }

    private void updateLanguages(String languageCode) {
        dataStore.getLanguagesStore().get(languageCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(languageModel -> {
                    Map<String, String> langs = languageModel.getLangs();
                    if (langs != null)
                        DatabaseHelper.getInstance().updateLanguages(langs);
                    else
                        updateLanguages("en");
                    EventBus.getDefault().post(new Events.LanguageUpdatedEvent());
                }, throwable -> {
                    Log.d(TAG, "throwable");
                });
    }

    public static Context getAppContext() {
        return TranslatorApplication.appContext;
    }

}
