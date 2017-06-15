package aleksey.khokhrin.ru.translator.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.net.UnknownHostException;
import java.util.Objects;

import aleksey.khokhrin.ru.translator.BR;
import aleksey.khokhrin.ru.translator.TranslatorApplication;
import aleksey.khokhrin.ru.translator.R;
import aleksey.khokhrin.ru.translator.data.DatabaseHelper;
import aleksey.khokhrin.ru.translator.data.store.TranslateStoreModel;
import aleksey.khokhrin.ru.translator.model.Language;
import aleksey.khokhrin.ru.translator.utils.Events;
import aleksey.khokhrin.ru.translator.view.LangSelectionActivity;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static aleksey.khokhrin.ru.translator.TranslatorApplication.getAppContext;

/**
 * Created by Aleksey on 16.04.2017.
 */

public class TranslateViewModel extends BaseObservable {
    public static final String TAG = TranslateViewModel.class.getSimpleName();
    private Context context;
    private Language languageSource, languageTarget;
    @Bindable
    public final ObservableField<String> textTarget = new ObservableField<>();
    @Bindable
    public final ObservableField<String> textSource = new ObservableField<>();
    @Bindable
    public final ObservableField<Boolean> internetConnectionError = new ObservableField<>(false);
    @Bindable
    public Language getLanguageSource() {
        return languageSource;
    }
    @Bindable
    public Language getLanguageTarget() {
        return languageTarget;
    }

    public TranslateViewModel(Context context, Language languageSource, Language languageTarget) {
        super();
        this.context = context;
        this.languageSource = languageSource;
        this.languageTarget = languageTarget;
    }

    public void onSelectSourceLanguageClick(View view) {
        openLanguageSelection(LangSelectionActivity.LangSelectionDirection.SOURCE);
    }

    public void onSwapLanguagesClick(View view) {
        Language tempLanguageSource = this.languageSource;
        languageSource = languageTarget;
        languageTarget = tempLanguageSource;
        notifyPropertyChanged(BR.languageTarget);
        notifyPropertyChanged(BR.languageSource);
        translateText();
    }

    public String getTextTarget() {
        return textTarget.get();
    }

    public void setTextTarget(String textTarget) {
        this.textTarget.set(textTarget);
        notifyPropertyChanged(BR.textTarget);
    }

    public void onSelectTargetLanguageClick(View view) {
        openLanguageSelection(LangSelectionActivity.LangSelectionDirection.TARGET);
    }

    public void onTryAgainButtonClick(View view) {
        translateText();
    }

    private void openLanguageSelection(LangSelectionActivity.LangSelectionDirection direction) {
        context.startActivity(LangSelectionActivity.newIntent(context, direction));
    }

    public void setLanguageSource(Language languageSource) {
        this.languageSource = languageSource;
        notifyPropertyChanged(BR.languageSource);
    }

    public void setLanguageTarget(Language languageTarget) {
        this.languageTarget = languageTarget;
        notifyPropertyChanged(BR.languageTarget);
    }

    public void clearTranslate() {
        setTextTarget("");
    }

    public void clearSourceText() {
        setTextSource("");
    }

    public void clearSourceText(View view) {
        clearSourceText();
        clearTranslate();
    }

    public void voiceInput(View view) {
        Toast.makeText(context, R.string.not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void playback(View view) {
        Toast.makeText(context, R.string.not_implemented, Toast.LENGTH_SHORT).show();
    }

    public String getTextSource() {
        return textSource.get();
    }

    public void setTextSource(String textSource) {
        this.textSource.set(textSource);
        notifyPropertyChanged(BR.textSource);
    }

    /**
     * Translates text and saves result in database
     */
    public void translateText()
    {
        translateText(true);
    }

    /**
     * Translates text and saves result in database if saveOnCompleted is TRUE
     * Also handles errors on server request
     */
    public void translateText(boolean saveOnCompleted) {
        if (getTextSource() == null || Objects.equals(getTextSource(), "")) {
            clearTranslate();
            return;
        }

        TranslateStoreModel translate = new TranslateStoreModel(getTextSource(), languageSource.getCode(), languageTarget.getCode());
        ((TranslatorApplication) getAppContext()).getTranslateStore().get(translate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(translateModel -> {
                    internetConnectionError.set(false);
                    textTarget.set(translateModel.getText().get(0));
                    notifyPropertyChanged(BR.textTarget);
                    if (saveOnCompleted)
                        saveTranslate();
                }, error -> {
                    if (error instanceof UnknownHostException) {
                        internetConnectionError.set(true);
                        clearTranslate();
                    }
                    else {
                        internetConnectionError.set(false);
                        if (error instanceof HttpException) {
                            HttpException exception = (HttpException) error;
                            AlertDialog.Builder messageBox = new AlertDialog.Builder(context);
                            messageBox.setTitle(R.string.error_title);
                            messageBox.setNegativeButton(R.string.ok, null);
                            if (exception.code() == 413)
                                messageBox.setMessage(R.string.error_text_length);
                            else if (exception.code() == 501)
                                messageBox.setMessage(R.string.error_translation_direction);
                            else
                                messageBox.setMessage(R.string.error_default);
                            messageBox.show();
                        }
                    }
                });
    }

    public void saveTranslate() {
        DatabaseHelper.getInstance().getInstance().addTranslate(getTextSource().trim(), textTarget.get(), languageSource, languageTarget);
        EventBus.getDefault().post(new Events.UpdateHistoryTabEvent());
    }

}
