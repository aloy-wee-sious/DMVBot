import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Aloy on 28/2/2017.
 */
public class Stalker implements Serializable {
    public String userId;
    public HashMap<String, Appointment> appointments = new HashMap<>();

    public Stalker(String userId){
        this.userId = userId;
    }
}
