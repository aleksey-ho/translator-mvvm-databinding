package aleksey.khokhrin.ru.translator.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import aleksey.khokhrin.ru.translator.model.Language;
import aleksey.khokhrin.ru.translator.model.Translate;
import aleksey.khokhrin.ru.translator.view.LangSelectionActivity;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Aleksey on 17.04.2017.
 */

public class DatabaseHelper {

    private static DatabaseHelper databaseHelper;

    private Realm realm;

    public static DatabaseHelper getInstance() {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper();
        }
        return databaseHelper;
    }

    public DatabaseHelper() {
        realm = Realm.getDefaultInstance();
    }

    public List<Language> getLanguages() {
        return realm.where(Language.class).findAllSorted("name", Sort.ASCENDING);
    }

    public Language getRecentlyUsedSourceLanguage() {
        return getRecentlyUsedLanguage(true);
    }

    public Language getRecentlyUsedTargetLanguage() {
        return getRecentlyUsedLanguage(false);
    }

    /**
     * @returns recently used language
     * @param isSourceLang defines what language should be returned. If TRUE - source language,
     *                     otherwise - target language
     */
    private Language getRecentlyUsedLanguage(boolean isSourceLang) {
        RealmQuery<Language> realmQuery = realm
                .where(Language.class);
        RealmResults<Language> languages;

        if (isSourceLang)
            languages = realmQuery.findAllSorted("sourceLastUseDate", Sort.DESCENDING);
        else
            languages = realmQuery.findAllSorted("targetLastUseDate", Sort.DESCENDING);

        return languages.get(0);
    }

    public List<Language> getRecentlyUsedSourceLangs() {
        return getRecentlyUsedLangs(true);
    }

    public List<Language> getRecentlyUsedTargetLangs() {
        return getRecentlyUsedLangs(false);
    }

    /**
     * @returns recently used languages
     * @param isSourceLang defines what languages should be returned. If TRUE - source languages,
     *                     otherwise - target languages
     */
    private List<Language> getRecentlyUsedLangs(boolean isSourceLang) {
        RealmQuery<Language> realmQuery = realm
                .where(Language.class);
        RealmResults<Language> languages;

        if (isSourceLang)
            languages = realmQuery.findAllSorted("sourceLastUseDate", Sort.DESCENDING);
        else
            languages = realmQuery.findAllSorted("targetLastUseDate", Sort.DESCENDING);

        List<Language> returnList = new ArrayList<>();
        for (Language language : languages) {
            if (((isSourceLang && language.getSourceLastUseDate() == null) ||
                    (!isSourceLang && language.getTargetLastUseDate() == null))
                || returnList.size() > 2)
                break;
            returnList.add(language);
        }

        return returnList;
    }

    /**
     * updates languages. Saves new languages or updates language name by code
     * @param stringMap key - language code, value - language name, e.g. "en"-"English"
     */
    public void updateLanguages(Map<String, String> stringMap) {
        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            Language language = realm.where(Language.class)
                    .equalTo("code", entry.getKey()).findFirst();
            if (language != null)
                realm.executeTransaction(realm1 -> language.setName(entry.getValue()));
            else
                realm.executeTransaction(realm1 -> realm1.copyToRealm(new Language(entry.getKey(), entry.getValue())));
        }
    }

    /**
     * saves default languages
     */
    public void initLanguages() {
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("af","Afrikaans");
        stringMap.put("am","Amharic");
        stringMap.put("ar","Arabic");
        stringMap.put("az","Azerbaijani");
        stringMap.put("ba","Bashkir");
        stringMap.put("be","Belarusian");
        stringMap.put("bg","Bulgarian");
        stringMap.put("bn","Bengali");
        stringMap.put("bs","Bosnian");
        stringMap.put("ca","Catalan");
        stringMap.put("ceb","Cebuano");
        stringMap.put("cs","Czech");
        stringMap.put("cy","Welsh");
        stringMap.put("da","Danish");
        stringMap.put("de","German");
        stringMap.put("el","Greek");
        stringMap.put("en","English");
        stringMap.put("eo","Esperanto");
        stringMap.put("es","Spanish");
        stringMap.put("et","Estonian");
        stringMap.put("eu","Basque");
        stringMap.put("fa","Persian");
        stringMap.put("fi","Finnish");
        stringMap.put("fr","French");
        stringMap.put("ga","Irish");
        stringMap.put("gd","Scottish Gaelic");
        stringMap.put("gl","Galician");
        stringMap.put("gu","Gujarati");
        stringMap.put("he","Hebrew");
        stringMap.put("hi","Hindi");
        stringMap.put("hr","Croatian");
        stringMap.put("ht","Haitian");
        stringMap.put("hu","Hungarian");
        stringMap.put("hy","Armenian");
        stringMap.put("id","Indonesian");
        stringMap.put("is","Icelandic");
        stringMap.put("it","Italian");
        stringMap.put("ja","Japanese");
        stringMap.put("jv","Javanese");
        stringMap.put("ka","Georgian");
        stringMap.put("kk","Kazakh");
        stringMap.put("km","Khmer");
        stringMap.put("kn","Kannada");
        stringMap.put("ko","Korean");
        stringMap.put("ky","Kyrgyz");
        stringMap.put("la","Latin");
        stringMap.put("lb","Luxembourgish");
        stringMap.put("lo","Lao");
        stringMap.put("lt","Lithuanian");
        stringMap.put("lv","Latvian");
        stringMap.put("mg","Malagasy");
        stringMap.put("mhr","Mari");
        stringMap.put("mi","Maori");
        stringMap.put("mk","Macedonian");
        stringMap.put("ml","Malayalam");
        stringMap.put("mn","Mongolian");
        stringMap.put("mr","Marathi");
        stringMap.put("mrj","Hill Mari");
        stringMap.put("ms","Malay");
        stringMap.put("mt","Maltese");
        stringMap.put("my","Burmese");
        stringMap.put("ne","Nepali");
        stringMap.put("nl","Dutch");
        stringMap.put("no","Norwegian");
        stringMap.put("pa","Punjabi");
        stringMap.put("pap","Papiamento");
        stringMap.put("pl","Polish");
        stringMap.put("pt","Portuguese");
        stringMap.put("ro","Romanian");
        stringMap.put("ru","Russian");
        stringMap.put("si","Sinhalese");
        stringMap.put("sk","Slovak");
        stringMap.put("sl","Slovenian");
        stringMap.put("sq","Albanian");
        stringMap.put("sr","Serbian");
        stringMap.put("su","Sundanese");
        stringMap.put("sv","Swedish");
        stringMap.put("sw","Swahili");
        stringMap.put("ta","Tamil");
        stringMap.put("te","Telugu");
        stringMap.put("tg","Tajik");
        stringMap.put("th","Thai");
        stringMap.put("tl","Tagalog");
        stringMap.put("tr","Turkish");
        stringMap.put("tt","Tatar");
        stringMap.put("udm","Udmurt");
        stringMap.put("uk","Ukrainian");
        stringMap.put("ur","Urdu");
        stringMap.put("uz","Uzbek");
        stringMap.put("vi","Vietnamese");
        stringMap.put("xh","Xhosa");
        stringMap.put("yi","Yiddish");
        stringMap.put("zh","Chinese");
        updateLanguages(stringMap);

        //init first selected languages
        String deviceLanguageCode = Locale.getDefault().getLanguage();
        Language sourceLanguage, targetLanguage;
        if (deviceLanguageCode != null) {
            sourceLanguage = realm.where(Language.class).equalTo("code", deviceLanguageCode).findFirst();
            targetLanguage = realm
                    .where(Language.class)
                    .equalTo("code", Objects.equals(deviceLanguageCode, "en") ? "ru" : "en")
                    .findFirst();
        }
        else {
            sourceLanguage = realm.where(Language.class).equalTo("code", "en").findFirst();
            targetLanguage = realm.where(Language.class).equalTo("code", "ru").findFirst();
        }

        realm.executeTransaction(realm1 -> {
            sourceLanguage.setSourceLastUseDate(Calendar.getInstance().getTime());
            targetLanguage.setTargetLastUseDate(Calendar.getInstance().getTime());
        });
    }

    /**
     * increments usage counter and updates last usage date
     */
    public void updateLanguageUsage(Language language, LangSelectionActivity.LangSelectionDirection direction) {
        realm.executeTransaction(realm1 -> {
            language.updateUsage(direction);
            realm1.copyToRealmOrUpdate(language);
        });
    }

    /**
     * Creates new record in db (and sets savedInHistory field  as TRUE) if textTarget with
     * the same source text with the same source and target language does not exist in DB.
     * Otherwise - updates usage date
     */
    public void addTranslate(String textSource, String textTranslate, Language languageSource, Language languageTarget) {
        if (textSource == null || Objects.equals(textSource, ""))
            return;
        Translate translateFromDb = realm.where(Translate.class)
                .equalTo("textSource", textSource)
                .equalTo("languageSource.code", languageSource.getCode())
                .equalTo("languageTarget.code", languageTarget.getCode())
                .findFirst();
        realm.executeTransaction(realm1 -> {
            if (translateFromDb != null) {
                translateFromDb.setDate(Calendar.getInstance().getTime());
                translateFromDb.setTextTarget(textTranslate);
                translateFromDb.setSavedInHistory(true);
            }
            else
                realm1.copyToRealm(new Translate(textSource, textTranslate, languageSource, languageTarget,
                        Calendar.getInstance().getTime(), true, false));
        });
    }

    /**
     * @returns all translates saved in history (field savedInHistory is TRUE)
     */
    public List<Translate> getTranslatesInHistory() {
        RealmResults<Translate> translates = realm
                .where(Translate.class)
                .equalTo("savedInHistory", true)
                .findAllSorted("date", Sort.DESCENDING);
        return translates;
    }

    /**
     * @returns all translates saved in Favourites translates (field savedInFavourites is TRUE)
     */
    public List<Translate> getFavouriteTranslates() {
        RealmResults<Translate> translates = realm
                .where(Translate.class)
                .equalTo("savedInFavourites", true)
                .findAllSorted("date", Sort.DESCENDING);
        return translates;
    }

    public void saveAsFavourite(Translate translate) {
        realm.executeTransaction(realm1 -> {
            translate.setSavedInFavourites(true);
        });
    }

    public void removeFromFavourites(Translate translate) {
        realm.executeTransaction(realm1 -> {
            translate.setSavedInFavourites(false);
        });
    }

    public void clearHistory() {
        RealmResults<Translate> translates = realm
                .where(Translate.class)
                .equalTo("savedInHistory", true)
                .findAll();

        realm.executeTransaction(realm1 -> {
            for (Translate translate : translates) {
                translate.setSavedInHistory(false);
            }
        });

        deleteUnnecessaryBookmarks();
    }

    public void clearFavourites() {
        RealmResults<Translate> translates = realm
                .where(Translate.class)
                .equalTo("savedInFavourites", true)
                .findAll();

        realm.executeTransaction(realm1 -> {
            for (Translate translate : translates) {
                translate.setSavedInFavourites(false);
            }
        });

        deleteUnnecessaryBookmarks();
    }

    /**
     * searches and deletes all found translates saved in DB that are not marked as savedInHistory and savedInFavourites
     */
    private void deleteUnnecessaryBookmarks() {
        RealmResults<Translate> translatesToDelete = realm
                .where(Translate.class)
                .equalTo("savedInHistory", false)
                .equalTo("savedInFavourites", false)
                .findAll();

        realm.executeTransaction(realm1 -> {
            translatesToDelete.deleteAllFromRealm();
        });
    }

}
