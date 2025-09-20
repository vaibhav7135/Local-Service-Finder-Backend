	package com.localservice.backend_local_service.controller;
	
	import com.localservice.backend_local_service.model.ChatMessage;
	import com.localservice.backend_local_service.repository.ChatMessageRepository;




	import java.util.List;
	
	import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
	
	@RestController
	public class ChatController {

		 private final SimpMessagingTemplate messagingTemplate;
		    private final ChatMessageRepository chatMessageRepository;

		    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageRepository chatMessageRepository) {
		        this.messagingTemplate = messagingTemplate;
		        this.chatMessageRepository = chatMessageRepository;
		    }

	    @MessageMapping("/chat")
	    public void sendMessage(ChatMessage message) {
	        chatMessageRepository.save(message);

	        // Send to both sender and receiver
	        messagingTemplate.convertAndSend("/topic/messages/" + message.getSenderId(), message);
	        messagingTemplate.convertAndSend("/topic/messages/" + message.getReceiverId(), message);
	    }

	    @GetMapping("/api/chat/history/{user1}/{user2}")
	    public List<ChatMessage> getChatHistory(@PathVariable Long user1, @PathVariable Long user2) {
	        return chatMessageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
	            user1, user2, user1, user2
	        );
	    }
	}
