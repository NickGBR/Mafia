package org.dreamteam.mafia.bot;

import lombok.SneakyThrows;
import org.dreamteam.mafia.model.Message;
import org.dreamteam.mafia.model.TelegramUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;


@Component
public class TBot extends TelegramLongPollingBot {

    @Autowired
    /* Map для хранения пользователей по их id, для ключа используем String, так как Spring,
       не позволяет использовать другие типы для хранения ключа*/
    private Map<String, TelegramUser> users;

    /**
     * Принимает сообщения от пользователя, обрабатывает его, и отправляет ответ.
     *
     * @param update
     */
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        //Проверяем существуют ли пользователь.

        if (update.hasMessage() && update.getMessage().hasText()) {
            String userId = update.getMessage().getChatId().toString();
            System.out.println(userId);

            //Если пользователя не существует, добавляем его в мапу с пользователями
            if (!users.containsKey(userId)) {
                TelegramUser user = new TelegramUser();
                user.setChatId(userId);
                users.put(userId, user);
                createStartGameButton(Long.parseLong(userId));
            }
        }
        if(update.hasCallbackQuery()){
            String userId = update.getCallbackQuery().getFrom().getId().toString();
            if(update.getCallbackQuery().getData().equals("play_button_pressed")){
                sendMessage(userId, "Введите название комнаты");
            }
        }
    }

    public void sendMessage(String id, String text) throws TelegramApiException {
        SendMessage message = new SendMessage().setChatId(id).setText(text);
        execute(message);
    }


    @Override
    public String getBotUsername() {
        return "dt_mafia_bot";
    }

    @Override
    public String getBotToken() {
        return "1348924592:AAFZR9e7WK65yR04yDloEa5QGjfB4Hvpucw";
    }

    //Получение мапы с пользователями.
    public Map<String, TelegramUser> getUsers() {
        return users;
    }

    /**
     * Создает кнопку для старта игры.
     */
    private void createStartGameButton(Long userId) throws TelegramApiException {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();       //Создаем объект разметки кнопок.
        InlineKeyboardButton startButton = new InlineKeyboardButton();  //Создаем кнопку для начала игры.
        startButton.setText(BotConst.START_GAME_BUTTON_TEXT);
        startButton.setCallbackData("play_button_pressed");    // Отправляем значение "true" на сервер, в случае нажатия кнопки.

        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
        buttonsRow.add(startButton);

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(buttonsRow);
        markup.setKeyboard(buttons);
        execute(new SendMessage().setChatId(userId).setText(BotConst.START_GAME_MESSAGE_TEXT).setReplyMarkup(markup));
    }

    private static class BotConst {
        final static String START_GAME_BUTTON_TEXT = "start";
        final static String START_GAME_MESSAGE_TEXT = "Добро пожаловть в игру мафия, чтобы начать играть, нажмите кнопку ниже...";
    }
}

//SendMessage message = new SendMessage() // Создаем SendMessage object объект с необходимыми полями
//                    .setChatId(update.getMessage().getChatId())
//                    .setText(update.getMessage().getText());
//                execute(message); // Call method to send the message



