package aleksey.khokhrin.ru.translator.data.retrofit;

import aleksey.khokhrin.ru.translator.data.retrofit.model.LanguageModel;
import aleksey.khokhrin.ru.translator.data.retrofit.model.TranslateModel;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

/**
 * Created by Aleksey on 15.04.2017.
 */

public interface YandexTranslateApi {
    String ENDPOINT = "https://translate.yandex.net";
    String YANDEX_TRANSLATE_API_KEY = "trnsl.1.1.20170409T134624Z.ed295de402b7d3bc.704846fcb4ddb6da80ae8dad8a0f09a3da026688";

    @GET("api/v1.5/tr.json/translate")
    Single<TranslateModel> translate(@Query("text") String text,
                                     @Query("lang") String lang,
                                     @Query("key") String key);

    @GET("api/v1.5/tr.json/getLangs")
    Observable<LanguageModel> getLangs(@Query("ui") String ui,
                                        @Query("key") String key);
}
