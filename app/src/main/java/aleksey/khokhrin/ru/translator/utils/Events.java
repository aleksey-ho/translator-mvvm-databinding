package aleksey.khokhrin.ru.translator.utils;

import aleksey.khokhrin.ru.translator.model.Language;
import aleksey.khokhrin.ru.translator.model.Translate;
import aleksey.khokhrin.ru.translator.view.LangSelectionActivity;

/**
 * Created by Aleksey on 19.04.2017.
 */

public class Events {

    /**
     * Emits when languages were loaded from server and updated
     */
    public static class LanguageUpdatedEvent { }

    /**
     * Emits when History tab was switched and it could be updated
     */
    public static class UpdateHistoryTabEvent { }

    /**
     * Emits when Favourites tab was switched and it could be updated
     */
    public static class UpdateFavouritesTabEvent { }

    /**
     * Emits when textTarget should be saved
     */
    public static class SaveTranslateEvent { }

    /**
     * Emits when user presses Delete icon
     */
    public static class ClearBookmarksEvent { }

    /**
     * Emits when user changes language. LangSelectionDirection determines whether Source
     * or Target language was changed
     */
    public static class LanguageChangedEvent {
        public final Language language;
        public final LangSelectionActivity.LangSelectionDirection direction;
        public LanguageChangedEvent(Language language, LangSelectionActivity.LangSelectionDirection direction) {
            this.language = language;
            this.direction = direction;
        }
    }

    /**
     * Emits when user presses the textTarget to open it
     */
    public static class OpenTranslateEvent {
        public final Translate translate;
        public OpenTranslateEvent(Translate translate) {
            this.translate = translate;
        }
    }
}
