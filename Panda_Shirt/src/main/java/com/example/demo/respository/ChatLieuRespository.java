package com.example.demo.respository;

import com.example.demo.entity.ChatLieu;
import com.example.demo.entity.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatLieuRespository extends JpaRepository<ChatLieu,Integer> {
    boolean existsChatLieuByMaChatLieu(String maChatLieu);
    boolean existsChatLieuByTenChatLieu(String tenChatLieu);
    Optional<ChatLieu> findBytenChatLieu(String name);
}
