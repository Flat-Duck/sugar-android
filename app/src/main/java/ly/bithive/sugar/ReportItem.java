package ly.bithive.sugar;

public class ReportItem {

     String date, period,value,cure;

    public ReportItem() {
    }

    public ReportItem(String date, String period, String value, String cure) {
        this.date = date;
        this.period = period;
        this.value = value;
        this.cure = cure;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCure() {
        return cure;
    }

    public void setCure(String cure) {
        this.cure = cure;
    }
}
