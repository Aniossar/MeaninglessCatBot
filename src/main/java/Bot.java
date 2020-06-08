import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        DefaultBotOptions defaultBotOptions = ApiContext.getInstance(DefaultBotOptions.class);
        defaultBotOptions.setProxyHost(BotProperties.getInstance().getValue("meaninglessbot.proxy.host"));
        defaultBotOptions.setProxyPort(Integer.parseInt(BotProperties.getInstance().getValue("meaninglessbot.proxy.port")));
        defaultBotOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        try {
            telegramBotsApi.registerBot(new Bot(defaultBotOptions));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Bot(DefaultBotOptions defaultBotOptions){
        super(defaultBotOptions);
    }

    public String getBotToken() {
        return BotProperties.getInstance().getValue("meaninglessbot.telegram.token");
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        //setButtons();
        try {
            if (message != null && message.hasText()) {
                switch (message.getText()) {
                    case "Случайная котофотка":
                        sendRandomCatPhoto(message);
                        break;
                    case "Котопост на Reddit":
                        sendRedditCatPhoto(message);
                        break;
                    case "/help":
                        sendMsg(message, "Бот для свежих котофоток.");
                        break;
                    default: sendMsg(message, "Привет! Это бот может следующее:" + "\n" +
                            "1. Прислать случайную фотографию с котейкой из интернета." + "\n" +
                            "2. Прислать последний котопост с Reddit." + "\n" +
                            "Выбери что-нибудь на клавиатуре:");
                }
            }
        } catch (Exception e) {
            sendMsg(message, "Что-то пошло не так:(" + "\n" + "Повторите запрос через минуту.");
        }


    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendRedditCatPhoto(Message message) throws IOException, TelegramApiException {
        Model model = new Model();
        RedditURL.getCatPost(model);
        sendPhoto(message, model);
    }

    public void sendRandomCatPhoto(Message message) throws IOException, TelegramApiException {
        Model model = new Model();
        RandomCatURL.getRandomCatPost(model);
        sendPhoto(message, model);
    }

    public void sendPhoto(Message message, Model model) throws TelegramApiException {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId().toString());
        sendPhoto.setReplyToMessageId(message.getMessageId());
        sendPhoto.setPhoto(model.getURL());
        sendPhoto.setCaption(model.getTitle());
        execute(sendPhoto);
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow firstKeyBoard = new KeyboardRow();
        KeyboardRow secondKeyBoard = new KeyboardRow();
        firstKeyBoard.add(new KeyboardButton("Случайная котофотка"));
        firstKeyBoard.add(new KeyboardButton("Котопост на Reddit"));
        firstKeyBoard.add(new KeyboardButton("/help"));

        keyboardRowList.add(firstKeyBoard);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public String getBotUsername() {
        return BotProperties.getInstance().getValue("meaninglessbot.telegram.botname");
    }
}
