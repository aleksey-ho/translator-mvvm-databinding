package aleksey.khokhrin.ru.translator.data.store;

import com.nytimes.android.external.store.base.impl.Store;
import com.nytimes.android.external.store.base.impl.StoreBuilder;

import aleksey.khokhrin.ru.translator.data.retrofit.YandexTranslateApi;
import aleksey.khokhrin.ru.translator.data.retrofit.model.LanguageModel;
import aleksey.khokhrin.ru.translator.data.retrofit.model.TranslateModel;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static aleksey.khokhrin.ru.translator.data.retrofit.YandexTranslateApi.ENDPOINT;
import static aleksey.khokhrin.ru.translator.data.retrofit.YandexTranslateApi.YANDEX_TRANSLATE_API_KEY;

public class DataStore {
    private Store<TranslateModel, TranslateStoreModel> translateStore;
    private Store<LanguageModel, String> languagesStore;

    public DataStore() {
        translateStore = provideTranslateModelStore();
        languagesStore = provideLanguageModelStore();
    }

    private Store<TranslateModel, TranslateStoreModel> provideTranslateModelStore() {
        return StoreBuilder.<TranslateStoreModel, TranslateModel>key()
                .fetcher(translate -> provideRetrofit().translate(translate.getTextSource().trim(),
                        translate.getLanguageSourceCode() + "-" + translate.getLanguageTargetCode(),
                        YANDEX_TRANSLATE_API_KEY).toObservable())
                .open();
    }

    private Store<LanguageModel, String> provideLanguageModelStore() {
        return StoreBuilder.<String, LanguageModel>key()
                .fetcher(languageCode -> provideRetrofit().getLangs(languageCode, YANDEX_TRANSLATE_API_KEY))
                .open();
    }

    public Store<TranslateModel, TranslateStoreModel> getTranslateStore() {
        return this.translateStore;
    }

    public Store<LanguageModel, String> getLanguagesStore() {
        return languagesStore;
    }

    private YandexTranslateApi provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(YandexTranslateApi.class);
    }

}
