import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.*;
import java.util.Scanner;

/**
 * Created by Aloy on 14/2/2017.
 */
public class Config {
    public final String BOT_USERNAME = "DMVBot";
    public String BOT_TOKEN = "223846811:AAFcVmDxC7HH8U8QaCoqPfGJQvjMQZNmxTU";
    public String drivePath = "/geckodriver";
    public String configPath = (new File("")).getAbsolutePath() + "/DMVBot.config";
    public String DL ="Y4545461";
    public String phone = "4151110000";
    public String firstName = "DMV";
    public String lastName = "Bot";
    public String DOB = "02/03/1993";

    public static void main(String[] args) throws IOException {

        //System.out.println(configPath);
        Scanner sc = new Scanner(System.in);
        Config config = new Config(sc.nextLine());
        //System.out.println(config.drivePath);
        System.out.println(config.phone.substring(0,3));
        System.out.println(config.phone.substring(3,6));
        System.out.println(config.phone.substring(6,10));
        System.out.println(config.DOB);
    }

    public Config(){};

    public Config(String configPath) throws IOException {
        File f = new File(configPath);
        InputStream is;
        if (f.exists()) {
            is = new FileInputStream(configPath);
        } else {
            is = new FileInputStream(this.configPath);
        }

        String jsonTxt = IOUtils.toString(is, "UTF-8");
        JSONObject json = new JSONObject(jsonTxt);
        this.BOT_TOKEN = json.get("token").toString();
        this.drivePath = json.get("driver").toString();
        this.DL = json.get("DL").toString();
        this.phone = json.get("phone").toString();
        this.firstName = json.get("firstName").toString();
        this.lastName = json.get("lastName").toString();
        this.DOB = json.get("DOB").toString();
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }
    public String getBotUsername(){
        return BOT_USERNAME;
    }
    public String getDrivePath() {
        return drivePath;
    }
    public String getDL() {
        return DL;
    }
    public String getPhone() {
        return phone;
    }
}
