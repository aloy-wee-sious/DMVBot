import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aloy on 14/2/2017.
 */
public class DMVBot extends TelegramLongPollingBot{

    private Config config;
    private HashMap<Long, Stalker> stalkers = new HashMap<>();
    private HashMap<Long, ArrayList<ScrapeRunner>> threads = new HashMap<>();

    public DMVBot(String configUrl) throws IOException {
        this.config = new Config(configUrl);
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

                        if (!stalkers.containsKey(message.getChatId())) {
                            stalkers.put(message.getChatId(), new Stalker(message.getChatId().toString()));
                        }

                        if (!threads.containsKey(message.getChatId())) {
                            threads.put(message.getChatId(), new ArrayList<ScrapeRunner>());
                        }

                        //scrapeDL(message.getChatId().toString(), commands[1]);
                        for (int i=1; i<commands.length;i++) {
                            ScrapeRunner runner = new ScrapeRunner(stalkers.get(message.getChatId()), commands[i]);
                            runner.start();
                            sendMessage(message.getChatId().toString(), "Stalking DMV no." + commands[i]);
                            threads.get(message.getChatId()).add(runner);
                            Thread.sleep(1000);
                        }
                        break;
                    case "stop":
                        if (!threads.containsKey(message.getChatId()) || threads.get(message.getChatId()) == null || threads.get(message.getChatId()).isEmpty()) {
                            sendMessage(message.getChatId().toString(), "Not stalking anytrhing");
                            break;
                        }
                        ArrayList<ScrapeRunner> runners = threads.get(message.getChatId());
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
        return config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    public void scrapeDL(String DMVId, Stalker stalker) throws InterruptedException, TelegramApiException, ParseException {

        if (!stalker.appointments.containsKey(DMVId)) {
            stalker.appointments.put(DMVId, null);
        }
        Appointment appointment = Scraper.scrapeBTW(this.config.getDrivePath(), DMVId, config.phone, config.firstName, config.lastName, stalker.appointments.get(DMVId), config.DL, config.DOB);
/*        if (stalker.appointments.get(DMVId) == null || appointment.date.before(stalker.appointments.get(DMVId).date)) {
            stalker.appointments.put(DMVId, appointment);
            sendMessage(stalker.userId, "New appointment made:\n"+appointment.details);
        } else {
            sendMessage(stalker.userId, appointment.details);
        }*/
        sendMessage(stalker.userId, appointment.details);
    }



    private void sendMessage(String chatId, String message) throws TelegramApiException {
        sendMessage(new SendMessage().setChatId(chatId).setText(message));
    }

    public class ScrapeRunner implements Runnable {
        private Thread thread;
        private Stalker stalker;
        private String DMVId;

        public ScrapeRunner(Stalker stalker, String DMVId){
            this.stalker = stalker;
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
                    scrapeDL(DMVId, stalker);
                    Thread.sleep(600000);
                }
            } catch (InterruptedException i) {
                System.out.println("thread ended");
                try {
                    sendMessage(stalker.userId, "Stalking stop for DMV no. " + DMVId);
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
