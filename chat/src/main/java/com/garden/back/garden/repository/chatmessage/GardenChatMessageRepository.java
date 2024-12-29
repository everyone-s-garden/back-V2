package com.garden.back.garden.repository.chatmessage;

import com.garden.back.garden.domain.GardenChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface GardenChatMessageRepository {

    void markMessagesAsRead(Long roomId, Long partnerId);

    GardenChatMessage save(GardenChatMessage chatMessage);

    List<GardenChatMessage> findAll();

    Optional<GardenChatMessage> findById(Long chatMessageId);

    GardenChatMessage getById(Long chatMessageId);

    Slice<GardenChatMessage> getGardenChatMessage(Long chatRoomId, Pageable pageable);

    Slice<ChatRoomFindRepositoryResponse> findChatRooms(Long memberId, Pageable pageable);

    String getContentsById(Long chatMessageId);

    void delete(Long chatRoomId);
}
