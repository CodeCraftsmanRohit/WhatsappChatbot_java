package com.chatbot.whatsappbot;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ChatbotService {

    @Value("${whatsapp.api.url}")
    private String whatsappApiUrl;

    @Value("${whatsapp.access.token}")
    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public String handleIncomingMessage(Map<String, Object> payload) {
        try {
            Map<String, Object> entry = ((List<Map<String, Object>>) payload.get("entry")).get(0);
            Map<String, Object> change = ((List<Map<String, Object>>) entry.get("changes")).get(0);
            Map<String, Object> value = (Map<String, Object>) change.get("value");
            List<Map<String, Object>> messages = (List<Map<String, Object>>) value.get("messages");

            if (messages != null && !messages.isEmpty()) {
                Map<String, Object> messageObject = messages.get(0);
                String from = (String) messageObject.get("from");

                Map<String, Object> text = (Map<String, Object>) messageObject.get("text");
                String userMessage = (text != null) ? (String) text.get("body") : null;

                if (userMessage != null) {
                    String reply = generateReply(userMessage);
                    sendMessageToUser(from, reply);
                    saveToFirebase(from, userMessage, reply);
                    return "Message processed and replied.";
                } else {
                    System.out.println("⚠️ No text field found in message.");
                }
            } else {
                System.out.println("⚠️ No messages found in payload.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error parsing incoming message: " + e.getMessage());
        }

        return "No valid message to process.";
    }

    public String generateReply(String userMessage) {
        userMessage = userMessage.toLowerCase();
        if (userMessage.contains("hello")) {
            return "Hello! How can I assist you today?";
        } else if (userMessage.contains("help")) {
            return "Here are some things I can help with:\n- Get info\n- Check timings\n- Ask anything.";
        } else if (userMessage.contains("thanks")) {
            return "You're welcome! 😊";
        } else {
            return "I'm not sure I understand. Try typing 'help'.";
        }
    }

    public void sendMessageToUser(String recipientPhone, String messageText) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> body = Map.of(
            "messaging_product", "whatsapp",
            "to", recipientPhone,
            "type", "text",
            "text", Map.of("body", messageText)
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(whatsappApiUrl, request, String.class);
            System.out.println("✅ WhatsApp Message Sent: " + response.getBody());
        } catch (Exception e) {
            System.err.println("❌ Error sending message: " + e.getMessage());
        }
    }

    public void saveToFirebase(String userId, String userMessage, String botReply) {
    try {
        Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> data = Map.of(
            "timestamp", System.currentTimeMillis(),
            "userMessage", userMessage,
            "botReply", botReply
        );

        ApiFuture<DocumentReference> future = db.collection("conversations")
                .document(userId)
                .collection("messages")
                .add(data);

        DocumentReference ref = future.get();  // ✅ Waits for completion
        System.out.println("✅ Saved to Firebase with ID: " + ref.getId());

    } catch (InterruptedException | ExecutionException e) {
        System.err.println("❌ Error saving to Firebase: " + e.getMessage());
    }
}
}
