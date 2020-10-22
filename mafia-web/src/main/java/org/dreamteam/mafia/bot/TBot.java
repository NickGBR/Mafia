package org.dreamteam.mafia.bot;

import lombok.SneakyThrows;
import org.dreamteam.mafia.model.Message;
import org.dreamteam.mafia.model.User;
import org.dreamteam.mafia.temporary.TemporaryDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//@Component
public class TBot extends TelegramLongPollingBot {

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    /**
     * Принимает сообщения от пользователя, обрабатывает его, и отправляет ответ.
     */
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        String userId;

        // Часть отвечающая за работу с сообщениями
        if (update.hasMessage() && update.getMessage().hasText()) {

            userId = update.getMessage().getChatId().toString();

            // Получаем пользователя по ID.
            User telegramUser = TemporaryDB.users.get("@t:" + userId);

            //Если пользователя не существует, добавляем его в БД.
            if (!TemporaryDB.users.containsKey(userId)) {
                telegramUser = new User();
                telegramUser.setId("@t:" + userId);
                TemporaryDB.users.put(telegramUser.getId(), telegramUser);

                // Создаем кнопку для начала игры.
                createStartGameButton(userId);
            }

            // Если пользователь не имеет комнаты, но уже нажал кнопку начала игры,
            if (telegramUser.isStartButtonPressed() && telegramUser.getRoom() == null) {

                // То проверяем существует ли комната, если да то добавляем пользователя в комнату,
                String room = update.getMessage().getText();
                if (TemporaryDB.rooms.containsKey(room)) {
                    telegramUser.setRoom(room);
                    telegramUser.setId("@t_" + telegramUser.getName());

                    // Добавляем пользователя в комнаты
                    if (TemporaryDB.usersByRooms.containsKey(room)) {
                        TemporaryDB.usersByRooms.get(room).add(telegramUser);
                    } else{ TemporaryDB.usersByRooms.put(room, new ArrayList<>());
                        TemporaryDB.usersByRooms.get(room).add(telegramUser);
                        System.out.println(TemporaryDB.usersByRooms.get(room).add(telegramUser));
                    }
                }
                // если нет, то просим повторить попытку.
                else sendTelegramMessage(userId, "Такой комнаты не существует." + "\n" + "Повторите попытку!");
            }

            if(telegramUser.getRoom()!=null){
                telegramUser.setRole(Message.Role.CIVILIAN);
                System.out.println("hi");
            }
        }

        // Часть отвечающая за работу с кнопками.
        if (update.hasCallbackQuery()) {
            userId = update.getCallbackQuery().getFrom().getId().toString();
            if (update.getCallbackQuery().getData().equals("play_button_pressed")) {

                TemporaryDB.users.get(userId).setStartButtonPressed(true);
                TemporaryDB.users.get(userId).setName(update.getCallbackQuery().getFrom().getUserName());
                sendTelegramMessage(userId, "Введите название комнаты");
            }
        }
    }

    public void sendTelegramMessage(String id, String text) throws TelegramApiException {
        SendMessage message = new SendMessage().setChatId(id).setText(text);
        execute(message);
    }

    private void sendWebMessage(String chat, User user, String text){
        Message message = new Message();
        message.setFrom(user.getName());
        message.setText(text);
        message.setRole(user.getRole());
        messagingTemplate.convertAndSend(chat + user.getRoom(), message);
    }




    @Override
    public String getBotUsername() {
        return "dt_mafia_bot";
    }

    @Override
    public String getBotToken() {
        return "1348924592:AAFZR9e7WK65yR04yDloEa5QGjfB4Hvpucw";
    }


    /**
     * Создает кнопку для старта игры.
     */
    private void createStartGameButton(String userId) throws TelegramApiException {
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



