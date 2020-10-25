package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.dto.ChatMessageDTO;
import org.dreamteam.mafia.dto.RoomDisplayDTO;
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
    void sendMessage(ChatMessageDTO message) throws ClientErrorException;

    void sendMessage(ChatMessageDTO message, String destination) throws ClientErrorException;

    /**
     * Возвращает историю чата для текущего игрока.
     *
     * @return - история чата, которую может видеть текущий игрок
     */
    List<ChatMessageDTO> getChatHistory() throws ClientErrorException;

    void sendAddRoom(RoomDisplayDTO addedRoom);

    void sendRemoveRoom(RoomDisplayDTO removedRoom);

    void sendUpdateRoom(RoomDisplayDTO updatedRoom);

    void sendJoinUpdate(RoomDisplayDTO room);

    void sendReadinessUpdate() throws ClientErrorException;
}
