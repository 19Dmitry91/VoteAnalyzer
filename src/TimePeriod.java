import java.util.Date;

public class TimePeriod implements Comparable<TimePeriod> {
    private Date start;
    private Date end;

    public TimePeriod(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public void appendTime(Date time) {
        if (start.after(time)) {
            start = time;
        } else if (end.before(time)) {
            end = time;
        }
    }

    @Override
    public int compareTo(TimePeriod o) {
        return start.compareTo(o.start);
    }

    @Override
    public String toString() {
        return start + " - " + end;
    }
}
