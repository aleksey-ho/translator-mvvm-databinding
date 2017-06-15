package aleksey.khokhrin.ru.translator.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import aleksey.khokhrin.ru.translator.R;
import aleksey.khokhrin.ru.translator.data.DatabaseHelper;
import aleksey.khokhrin.ru.translator.databinding.FragmentTranslateBinding;
import aleksey.khokhrin.ru.translator.model.Language;
import aleksey.khokhrin.ru.translator.utils.Events;
import aleksey.khokhrin.ru.translator.viewModel.TranslateViewModel;

public class TranslateFragment extends Fragment {
    public static final String TAG = TranslateFragment.class.getSimpleName();

    public static final String TEXT_SOURCE = "TEXT_SOURCE";
    public static final String TEXT_TRANSLATE = "TEXT_TRANSLATE";

    private TranslateViewModel viewModel;
    private FragmentTranslateBinding binding;

    public TranslateFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_translate, container, false);

        Language languageSource = DatabaseHelper.getInstance().getRecentlyUsedSourceLanguage();
        Language languageTarget = DatabaseHelper.getInstance().getRecentlyUsedTargetLanguage();
        viewModel = new TranslateViewModel(getContext(), languageSource, languageTarget);
        binding.setViewModel(viewModel);

        if (savedInstanceState != null)
        {
            String textSource = savedInstanceState.getString(TEXT_SOURCE);
            viewModel.setTextSource(textSource);
            String textTarget = savedInstanceState.getString(TEXT_TRANSLATE);
            viewModel.setTextTarget(textTarget);
        }

        binding.textInputPanel.editTextToTranslate.setRawInputType(InputType.TYPE_CLASS_TEXT);
        binding.textInputPanel.editTextToTranslate.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            //when user stops typing and presses Enter
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (v.getText().length() > 0)
                    viewModel.translateText();
                else {
                    viewModel.clearTranslate();
                    viewModel.clearSourceText();
                }
                handled = true;
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            return handled;
        });

        //when user is typing - textTarget in real-time
        binding.textInputPanel.editTextToTranslate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.translateText(false);
            }
        });

        //make it scrollable
        binding.textViewTranslate.setMovementMethod(new ScrollingMovementMethod());

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
    public void openTranslateEventHandler(Events.OpenTranslateEvent event) {
        viewModel.setLanguageSource(event.translate.getLanguageSource());
        viewModel.setLanguageTarget(event.translate.getLanguageTarget());
        viewModel.setTextSource(event.translate.getTextSource());
        viewModel.setTextTarget(event.translate.getTextTarget());
    }

    @Subscribe
    public void languageUpdatedEventHandler(Events.LanguageUpdatedEvent event) {
        viewModel.notifyChange();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void languageChangedEventHandler(Events.LanguageChangedEvent event) {
        if(event.direction == LangSelectionActivity.LangSelectionDirection.SOURCE)
            viewModel.setLanguageSource(event.language);
        else
            viewModel.setLanguageTarget(event.language);
        viewModel.translateText();
        EventBus.getDefault().removeStickyEvent(Events.LanguageChangedEvent.class);
    }

    @Subscribe
    public void saveTranslateEventHandler(Events.SaveTranslateEvent event) {
        viewModel.saveTranslate();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(TEXT_SOURCE, viewModel.getTextSource());
        savedInstanceState.putString(TEXT_TRANSLATE, viewModel.getTextTarget());

        super.onSaveInstanceState(savedInstanceState);
    }
}
