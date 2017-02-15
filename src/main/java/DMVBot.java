import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;

/**
 * Created by Aloy on 14/2/2017.
 */
public class DMVBot extends TelegramLongPollingBot{

    private final String driverUrl;
    private ArrayList<ScrapeRunner> runners = new ArrayList<>();

    public DMVBot(String driverUrl){
        this.driverUrl = driverUrl;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {

                Message message = update.getMessage();
                String command = message.getText();
                String[] commands = command.split(" ");

                switch (commands[0].toLowerCase()) {
                    case "stalk":
                        //scrape(message.getChatId().toString(), commands[1]);
                        for (int i=1; i<commands.length;i++) {
                            ScrapeRunner runner = new ScrapeRunner(message.getChatId().toString(), commands[i]);
                            runner.start();
                            sendMessage(message.getChatId().toString(), "Stalking DMV no." + commands[i]);
                            runners.add(runner);
                            Thread.sleep(1000);
                        }
                        break;
                    case "stop":
                        for(ScrapeRunner r: runners) {
                            r.stop();
                        }
                        break;
                    default:
                        sendMessage(message.getChatId().toString(), "Invalid");
                }
            }
        } catch (Throwable t) {
            t.getStackTrace();
            SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId()).setText("error");
            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return Config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return Config.getBotToken();
    }

    public void scrape(String chatId, String DMVId) throws InterruptedException, TelegramApiException {
        sendMessage(chatId, Scraper.scrape(this.driverUrl, DMVId));
    }



    private void sendMessage(String chatId, String message) throws TelegramApiException {
        sendMessage(new SendMessage().setChatId(chatId).setText(message));
    }

    public class ScrapeRunner implements Runnable {
        private Thread thread;
        private String chatId;
        private String DMVId;

        public ScrapeRunner(String chatId, String DMVId){
            this.chatId = chatId;
            this.DMVId = DMVId;
        }

        public void start(){
            if (thread == null) {
                thread = new Thread (this);
                thread.start ();
            }
        }

        public void stop(){
            thread.interrupt();
        }

        @Override
        public void run() {
            try {
                while (!thread.isInterrupted()) {
                    //System.out.println(" thread started");
                    scrape(chatId, DMVId);
                    Thread.sleep(600000);
                }
            } catch (InterruptedException i) {
                System.out.println("thread ended");
                try {
                    sendMessage(chatId, "Stalking stop for DMV no. " + DMVId);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
               run();
            }
        }
    }
}
