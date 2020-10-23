package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.dto.OutgoingChatMessageDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;

import java.util.List;

/**
 * Интерфейс сервиса чата
 */
public interface MessageService {

    /**
     * Отправляет сообщение в чат от текущего пользователя. Адресаты определяются автоматически.
     * Также сохраняет сообщение для последующего восстановления
     *
     * @param message - сообщение, полученное от интерфейса
     */
    void sendMessage(String message) throws ClientErrorException;

    /**
     * Возвращает историю чата для текущего игрока.
     *
     * @return - история чата, которую может видеть текущий игрок
     */
    List<OutgoingChatMessageDTO> getChatHistory() throws ClientErrorException;
}
