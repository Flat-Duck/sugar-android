package ly.bithive.sugar;

public class ReportItem {
     String date, morningGlycemia, noonGlycemia, eveningGlycemia, sleepGlycemia, morningInsulin, noonInsulin, eveningInsulin, sleepInsulin;

    public ReportItem(String date, String morningGlycemia, String noonGlycemia, String eveningGlycemia, String sleepGlycemia, String morningInsulin, String noonInsulin, String eveningInsulin, String sleepInsulin) {
        this.date = date;
        this.morningGlycemia = morningGlycemia;
        this.noonGlycemia = noonGlycemia;
        this.eveningGlycemia = eveningGlycemia;
        this.sleepGlycemia = sleepGlycemia;
        this.morningInsulin = morningInsulin;
        this.noonInsulin = noonInsulin;
        this.eveningInsulin = eveningInsulin;
        this.sleepInsulin = sleepInsulin;
    }

    public ReportItem() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMorningGlycemia() {
        return morningGlycemia;
    }

    public void setMorningGlycemia(String morningGlycemia) {
        this.morningGlycemia = morningGlycemia;
    }

    public String getNoonGlycemia() {
        return noonGlycemia;
    }

    public void setNoonGlycemia(String noonGlycemia) {
        this.noonGlycemia = noonGlycemia;
    }

    public String getEveningGlycemia() {
        return eveningGlycemia;
    }

    public void setEveningGlycemia(String eveningGlycemia) {
        this.eveningGlycemia = eveningGlycemia;
    }

    public String getSleepGlycemia() {
        return sleepGlycemia;
    }

    public void setSleepGlycemia(String sleepGlycemia) {
        this.sleepGlycemia = sleepGlycemia;
    }

    public String getMorningInsulin() {
        return morningInsulin;
    }

    public void setMorningInsulin(String morningInsulin) {
        this.morningInsulin = morningInsulin;
    }

    public String getNoonInsulin() {
        return noonInsulin;
    }

    public void setNoonInsulin(String noonInsulin) {
        this.noonInsulin = noonInsulin;
    }

    public String getEveningInsulin() {
        return eveningInsulin;
    }

    public void setEveningInsulin(String eveningInsulin) {
        this.eveningInsulin = eveningInsulin;
    }

    public String getSleepInsulin() {
        return sleepInsulin;
    }

    public void setSleepInsulin(String sleepInsulin) {
        this.sleepInsulin = sleepInsulin;
    }
}
