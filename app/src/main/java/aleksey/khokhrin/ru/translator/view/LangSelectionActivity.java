package aleksey.khokhrin.ru.translator.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import aleksey.khokhrin.ru.translator.R;
import aleksey.khokhrin.ru.translator.adapters.LangSelectionAdapter;
import aleksey.khokhrin.ru.translator.data.DatabaseHelper;
import aleksey.khokhrin.ru.translator.databinding.ActivityLangSelectionBinding;
import aleksey.khokhrin.ru.translator.model.Language;
import aleksey.khokhrin.ru.translator.utils.Events;

public class LangSelectionActivity extends AppCompatActivity {
    private static final String TAG = LangSelectionActivity.class.getSimpleName();
    private ActivityLangSelectionBinding binding;
    public final static String DIRECTION = "DIRECTION";
    private LangSelectionDirection direction;

    public static Intent newIntent(Context context, LangSelectionDirection direction) {
        Intent intent = new Intent(context, LangSelectionActivity.class);
        intent.putExtra(DIRECTION, direction.getValue());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        direction = LangSelectionDirection.values()[getIntent().getIntExtra(DIRECTION, 0)];
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lang_selection);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(direction == LangSelectionDirection.SOURCE ?
                getString(R.string.language_source) : getString(R.string.language_target));

        // создаем адаптер
        LangSelectionAdapter adapter = new LangSelectionAdapter(this);

        //добавляем в список недавно использованные языки (если есть)
        List<Language> recentlyUsedLangs = direction == LangSelectionDirection.SOURCE ?
                DatabaseHelper.getInstance().getRecentlyUsedSourceLangs() :
                DatabaseHelper.getInstance().getRecentlyUsedTargetLangs();
        if (recentlyUsedLangs.size() > 0) {
            adapter.addSectionHeaderItem(getString(R.string.recently_used));
            adapter.addItems(recentlyUsedLangs);
        }

        //добавляем все языки
        List<Language> languages = DatabaseHelper.getInstance().getLanguages();
        if (languages.size() > 0) {
            adapter.addSectionHeaderItem(getString(R.string.all_languages));
            adapter.addItems(languages);
        }

        // присваиваем адаптер списку
        binding.listViewLanguages.setAdapter(adapter);

        binding.listViewLanguages.setOnItemClickListener((parent, view, position, id) -> {
            Object item = parent.getAdapter().getItem(position);
            if (!(item instanceof Language))
                return;
            Language language = (Language) item;
            EventBus.getDefault().postSticky(new Events.LanguageChangedEvent(language, direction));
            DatabaseHelper.getInstance().updateLanguageUsage(language, direction);
            finish();
        });
    }

    public enum LangSelectionDirection {
        SOURCE(0), TARGET(1);
        private int value;
        LangSelectionDirection(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
