package aleksey.khokhrin.ru.translator.model;

import java.util.Calendar;
import java.util.Date;

import aleksey.khokhrin.ru.translator.view.LangSelectionActivity;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Aleksey on 17.04.2017.
 */

public class Language extends RealmObject {
    @PrimaryKey
    private String code;
    private String name;
    private Date sourceLastUseDate;
    private Date targetLastUseDate;
    // can be used in future to show "Frequently used languages"
    private int usageCounter;

    public Language() {
    }

    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getSourceLastUseDate() {
        return sourceLastUseDate;
    }

    public void setSourceLastUseDate(Date sourceLastUseDate) {
        this.sourceLastUseDate = sourceLastUseDate;
    }

    public Date getTargetLastUseDate() {
        return targetLastUseDate;
    }

    public void setTargetLastUseDate(Date targetLastUseDate) {
        this.targetLastUseDate = targetLastUseDate;
    }

    public int getUsageCounter() {
        return usageCounter;
    }

    public void setUsageCounter(int usageCounter) {
        this.usageCounter = usageCounter;
    }

    public void updateUsage(LangSelectionActivity.LangSelectionDirection direction) {
        usageCounter++;
        if (direction == LangSelectionActivity.LangSelectionDirection.SOURCE)
            sourceLastUseDate = Calendar.getInstance().getTime();
        else
            targetLastUseDate = Calendar.getInstance().getTime();
    }
}
