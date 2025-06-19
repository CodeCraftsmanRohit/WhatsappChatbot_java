package com.chatbot.whatsappbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final String YOUR_VERIFY_TOKEN = "my_custom_verify_token"; // Set this in your env/config

    @Autowired
    private ChatbotService chatbotService;

    // ✅ GET - For webhook verification
    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String verifyToken) {
        if ("subscribe".equals(mode) && YOUR_VERIFY_TOKEN.equals(verifyToken)) {
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(403).body("Verification failed");
        }
    }

    // ✅ POST - To receive messages
    @PostMapping
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> payload) {
        System.out.println("📥 Incoming Webhook Payload:\n" + payload);

        try {
            // 🔍 Parse the message from payload
            Map<String, Object> entry = ((List<Map<String, Object>>) payload.get("entry")).get(0);
            Map<String, Object> changes = ((List<Map<String, Object>>) entry.get("changes")).get(0);
            Map<String, Object> value = (Map<String, Object>) changes.get("value");
            Map<String, Object> messageObject = ((List<Map<String, Object>>) value.get("messages")).get(0);

            String from = (String) messageObject.get("from");
            String messageBody = (String) ((Map<String, Object>) messageObject.get("text")).get("body");

            // 🤖 Send to chatbot logic
            String reply = chatbotService.generateReply(messageBody);

            // 🚀 Send response to user (implement API call inside service)
            chatbotService.sendMessageToUser(from, reply);

        } catch (Exception e) {
            System.err.println("⚠️ Error parsing incoming message: " + e.getMessage());
        }

        return ResponseEntity.ok("EVENT_RECEIVED");
    }
}
