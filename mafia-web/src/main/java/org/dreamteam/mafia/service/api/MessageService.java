package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.dao.RoomDAO;
import org.dreamteam.mafia.dto.CharacterUpdateDTO;
import org.dreamteam.mafia.dto.ChatMessageDTO;
import org.dreamteam.mafia.dto.GameDTO;
import org.dreamteam.mafia.dto.RoomDisplayDTO;
import org.dreamteam.mafia.exceptions.ClientErrorException;
import org.dreamteam.mafia.model.MessageDestinationDescriptor;

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
     * Отправляет сообщение от имени системы в указаннаю комнату. Адресаты определяются автоматически
     *
     * @param systemMessage - сообщение от системы
     * @param descriptor    - описатель адреса для клиента. Само сообщение, впрочем, все равно будет отправлено,
     *                      как системное
     */
    void sendSystemMessage(GameDTO systemMessage, MessageDestinationDescriptor descriptor) throws ClientErrorException;

    void sendVotingResultUpdate(CharacterUpdateDTO dto, RoomDAO room);

    /**
     * Возвращает историю чата для текущего игрока.
     *
     * @return - история чата, которую может видеть текущий игрок
     */
    List<ChatMessageDTO> getChatHistory() throws ClientErrorException;

    void sendAddRoom(RoomDisplayDTO addedRoom);

    void sendRemoveRoom(RoomDisplayDTO removedRoom);

    void sendUpdateRoom(RoomDisplayDTO updatedRoom);

    void sendJoinUpdate(RoomDisplayDTO room, String login);

    void sendKickUpdate(RoomDisplayDTO room, String login);

    void sendReadinessUpdate() throws ClientErrorException;

    void sendGameStartUpdate() throws ClientErrorException;
}
