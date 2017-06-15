package aleksey.khokhrin.ru.translator.data.store;

/**
 * Created by aleks on 15.06.2017.
 */

public class TranslateStoreModel {

    private String languageSourceCode;
    private String languageTargetCode;
    private String textSource;

    public TranslateStoreModel(String textSource, String languageSourceCode, String languageTargetCode) {
        this.languageSourceCode = languageSourceCode;
        this.languageTargetCode = languageTargetCode;
        this.textSource = textSource;
    }

    public String getLanguageSourceCode() {
        return languageSourceCode;
    }

    public String getLanguageTargetCode() {
        return languageTargetCode;
    }

    public String getTextSource() {
        return textSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslateStoreModel that = (TranslateStoreModel) o;

        if (languageSourceCode != null ? !languageSourceCode.equals(that.languageSourceCode) : that.languageSourceCode != null)
            return false;
        if (languageTargetCode != null ? !languageTargetCode.equals(that.languageTargetCode) : that.languageTargetCode != null)
            return false;
        return textSource != null ? textSource.equals(that.textSource) : that.textSource == null;

    }

    @Override
    public int hashCode() {
        int result = languageSourceCode != null ? languageSourceCode.hashCode() : 0;
        result = 31 * result + (languageTargetCode != null ? languageTargetCode.hashCode() : 0);
        result = 31 * result + (textSource != null ? textSource.hashCode() : 0);
        return result;
    }
}
