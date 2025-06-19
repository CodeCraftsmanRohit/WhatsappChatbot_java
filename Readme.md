# 📱 WhatsApp Chatbot using Spring Boot

A simple yet extensible WhatsApp chatbot backend built with **Java + Spring Boot**, integrating **Meta's WhatsApp Cloud API**, **ngrok**, and **Firebase** for message handling and storage.

---

## 🌐 Features

- ✅ Webhook verification and message receiving
- ✅ Rule-based auto-reply logic
- ✅ WhatsApp Cloud API message sending
- ✅ Firebase integration for message logging
- ✅ Secure credential management via `application.properties` or `.env`
- ✅ Render deployment ready

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17 or later
- Maven
- ngrok (for local HTTPS tunneling)
- Firebase Service Account JSON
- Meta WhatsApp Business Account (test number, access token, phone ID)

---

## 📁 Project Structure

whatsappbot/
├── src/
│ └── main/
│ ├── java/com/chatbot/whatsappbot/
│ │ ├── WebhookController.java
│ │ ├── ChatbotService.java
│ │ └── WhatsappbotApplication.java
│ └── resources/
│ ├── application.properties
│ └── whatsapp-chatbot-firebase.json # Firebase service key
├── .gitignore
├── README.md
└── pom.xml

yaml
Copy
Edit

---

## 🔧 Configuration

### 🔐 application.properties

```properties
# App name
spring.application.name=whatsappbot

# Webhook verification
whatsapp.verify.token=my_custom_verify_token

# WhatsApp Cloud API
whatsapp.api.url=https://graph.facebook.com/v19.0/651721461364460/messages
whatsapp.api.token=YOUR_ACCESS_TOKEN

# Firebase
firebase.database.url=https://your-project-id.firebaseio.com
firebase.credentials.path=classpath:whatsapp-chatbot-firebase.json
🛡 .gitignore (Add)
bash
Copy
Edit
/src/main/resources/whatsapp-chatbot-firebase.json
.env
🧪 Running Locally
Start backend

bash
Copy
Edit
mvn spring-boot:run
Expose with ngrok

bash
Copy
Edit
ngrok http 8080
Configure webhook

Go to Meta Developer Portal → WhatsApp → Webhooks

Set:

Callback URL: https://xxxx.ngrok-free.app/webhook

Verify token: my_custom_verify_token

💬 Testing
Send a message from your registered test number in WhatsApp.
The bot will reply based on rule-based logic like:

"hello" → "Hello! How can I assist you today?"

"help" → Bot shows available commands

☁️ Deploying to Render
Push code to GitHub

Login to Render

Create new Web Service:

Build command: mvn clean install

Start command: java -jar target/whatsappbot-0.0.1-SNAPSHOT.jar

Set environment variables in Render dashboard:

WHATSAPP_ACCESS_TOKEN

WHATSAPP_API_URL

WHATSAPP_VERIFY_TOKEN

FIREBASE_CREDENTIALS_PATH (optional)

🧠 Future Improvements
Add NLP (Dialogflow, Rasa)

Firebase Auth

Analytics dashboard

Message template support

📬 Note for Reviewers / Recruiters
Due to Meta API limitations during testing, the WhatsApp bot may not connect live on your number. However, this app works perfectly with valid credentials and is fully functional locally. Feel free to review the code and run it with your own test number for a full demo.
# 📱 WhatsApp Chatbot using Spring Boot

A robust and extensible backend for a WhatsApp chatbot built with **Java + Spring Boot**, integrating **Meta's WhatsApp Cloud API**, **Firebase**, and **ngrok**. This chatbot can receive and respond to messages, store chat logs, and is deployable to platforms like **Render**.

---

## 🌐 Features

- ✅ Webhook verification and message parsing
- ✅ Rule-based auto-reply system
- ✅ WhatsApp Cloud API integration (send/receive messages)
- ✅ Firebase integration for real-time message storage
- ✅ Secure credential management via `application.properties` or `.env`
- ✅ Ready for deployment on Render

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17 or later
- Maven
- ngrok (for local HTTPS tunneling)
- Firebase Service Account JSON key
- Meta WhatsApp Business account (test phone number, access token, phone number ID)

---
