package ly.bithive.sugar;

public class SportReportItem {
     String date,period,time,type;


    public SportReportItem() {

    }

    public SportReportItem(String date, String period, String time, String type) {
        this.date = date;
        this.period = period;
        this.time = time;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public SportReportItem setDate(String date) {
        this.date = date;
        return this;
    }

    public String getPeriod() {
        return period;
    }

    public SportReportItem setPeriod(String period) {
        this.period = period;
        return this;
    }

    public String getTime() {
        return time;
    }

    public SportReportItem setTime(String time) {
        this.time = time;
        return this;
    }

    public String getType() {
        return type;
    }

    public SportReportItem setType(String type) {
        this.type = type;
        return this;
    }
}
