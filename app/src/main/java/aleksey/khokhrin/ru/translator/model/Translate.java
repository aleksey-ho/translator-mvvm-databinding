package aleksey.khokhrin.ru.translator.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Aleksey on 17.04.2017.
 */

public class Translate extends RealmObject {
    private Language languageSource;
    private Language languageTarget;
    private Date date;
    private String textSource;
    private String textTarget;
    private boolean savedInHistory;
    private boolean savedInFavourites;

    public Translate() {
    }

    public Translate(String textSource, String textTarget, Language languageSource, Language languageTarget,
                     Date date, boolean savedInHistory, boolean savedInFavourites) {
        this.languageSource = languageSource;
        this.languageTarget = languageTarget;
        this.date = date;
        this.textSource = textSource;
        this.textTarget = textTarget;
        this.savedInHistory = savedInHistory;
        this.savedInFavourites = savedInFavourites;
    }

    public Translate(String textSource, Language languageSource, Language languageTarget) {
        this.languageSource = languageSource;
        this.languageTarget = languageTarget;
        this.textSource = textSource;
    }

    public Language getLanguageSource() {
        return languageSource;
    }

    public void setLanguageSource(Language languageSource) {
        this.languageSource = languageSource;
    }

    public Language getLanguageTarget() {
        return languageTarget;
    }

    public void setLanguageTarget(Language languageTarget) {
        this.languageTarget = languageTarget;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTextSource() {
        return textSource;
    }

    public void setTextSource(String textSource) {
        this.textSource = textSource;
    }

    public String getTextTarget() {
        return textTarget;
    }

    public void setTextTarget(String textTarget) {
        this.textTarget = textTarget;
    }

    public boolean isSavedInHistory() {
        return savedInHistory;
    }

    public void setSavedInHistory(boolean savedInHistory) {
        this.savedInHistory = savedInHistory;
    }

    public boolean isSavedInFavourites() {
        return savedInFavourites;
    }

    public void setSavedInFavourites(boolean savedInFavourites) {
        this.savedInFavourites = savedInFavourites;
    }
}
