package org.dreamteam.mafia.service.api;

import org.dreamteam.mafia.dto.CharacterUpdateDTO;
import org.dreamteam.mafia.dto.ChatMessageDTO;
import org.dreamteam.mafia.dto.GameDTO;
import org.dreamteam.mafia.dto.RoomDisplayDTO;
import org.dreamteam.mafia.entities.RoomEntity;
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

    void sendVotingResultUpdate(CharacterUpdateDTO dto, RoomEntity room);

    /**
     * Возвращает историю чата для текущего игрока.
     *
     * @return - история чата, которую может видеть текущий игрок
     */
    List<ChatMessageDTO> getChatHistory() throws ClientErrorException;

    /**
     * Рассылает клиентам уведомление о добавлении комнаты в список доступных
     *
     * @param addedRoom - описание добавленной комнаты
     */
    void sendAddRoom(RoomDisplayDTO addedRoom);

    /**
     * Рассылает клиентам уведомление об удаленнии комнаты из списка доступных
     *
     * @param removedRoom - описание удаленной комнаты
     */
    void sendRemoveRoom(RoomDisplayDTO removedRoom);

    /**
     * Рассылает клиентам уведомление об обновлении состояния комнаты в списке доступных
     *
     * @param updatedRoom - описание обновленной комнаты
     */
    void sendUpdateRoom(RoomDisplayDTO updatedRoom);

    /**
     * Рассылает клиентам уведомление о присоединении игрока к комнате
     *
     * @param room  - описание комнаты, к которой присоединился игрок
     * @param login - логин присоединившегося игрока
     */
    void sendJoinUpdate(RoomDisplayDTO room, String login);

    /**
     * Рассылает клиентам уведомление об изгнании игрока из комнаты
     *
     * @param room  - описание комнаты, из которой был изгнан игрок
     * @param login - логин изгнанного игрока
     */
    void sendKickUpdate(RoomDisplayDTO room, String login);

    /**
     * Рассылает клиентам уведомление о готовности комнаты к началу игры
     *
     * @throws ClientErrorException - если текущий пользователь не находится в комнате
     */
    void sendReadinessUpdate() throws ClientErrorException;

    /**
     * Рассылает клиентам уведомление о начале игры в комнате
     *
     * @throws ClientErrorException - если текущий пользователь не находится в комнате
     */
    void sendGameStartUpdate() throws ClientErrorException;
}
