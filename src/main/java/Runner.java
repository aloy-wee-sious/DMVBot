import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import java.io.IOException;

/**
 * Created by Aloy on 14/2/2017.
 */
public class Runner {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new DMVBot((args.length == 0 || args[0].isEmpty()) ? "\\DMVBot.config" : args[0]));
        } catch (TelegramApiException e) {
            System.out.println(e);
        }
    }
}
