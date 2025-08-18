package com.konecta.internship.Restaurant_POS_System.table_management.service;

import com.konecta.internship.Restaurant_POS_System.table_management.dto.TableResponseDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class TableNotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public TableNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyStatusUpdate(TableResponseDto table) {
        messagingTemplate.convertAndSend("/topic/tables", table);
    }
}
