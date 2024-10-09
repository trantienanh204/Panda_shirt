package com.example.demo.respository;

import com.example.demo.entity.ChatLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatLieuRespository extends JpaRepository<ChatLieu,Integer> {
    boolean existsChatLieuByMaChatLieu(String maChatLieu);
    boolean existsChatLieuByTenChatLieu(String tenChatLieu);
}
