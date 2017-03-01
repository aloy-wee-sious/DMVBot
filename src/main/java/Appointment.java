import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aloy on 28/2/2017.
 */
public class Appointment implements Serializable{
    public Date date;
    public String details;

    public Appointment (String details) throws ParseException {
        this.details = details;
    }

    public Appointment (String details, String date) throws ParseException {
        this.details = details;
        this.date = parse(date);
    }

    private static Date parse(String date) throws ParseException {
        date = date.replace("at ", "");
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm a");
        return  formatter.parse(date);
    }

    public static void main(String[] args) throws ParseException {
        String date = "Monday, May 1, 2017 at 10:40 AM";
        Date formatted = parse(date);
        System.out.println(date);
        System.out.println(formatted);
        System.out.println(formatted.before(new Date()));
        System.out.println(formatted.after(new Date()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appointment that = (Appointment) o;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return details != null ? details.equals(that.details) : that.details == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (details != null ? details.hashCode() : 0);
        return result;
    }
}
