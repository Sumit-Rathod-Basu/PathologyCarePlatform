package Pathology.Care.Platform.Service;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenRouterService {

    @Value("${openrouter.api-key}")
    private String apiKey;

    @Value("${openrouter.url}")
    private String apiUrl;

    @Value("${openrouter.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getResponse(String userMessage) {

        String lowerMessage = userMessage.toLowerCase();

        // =========================
        // 1. Predefined Platform Responses
        // =========================
        if (lowerMessage.contains("service")) {
            return """
            🧬 Pathology Care Platform Services:

            • Online Lab Test Booking
            • Home Sample Collection
            • View & Download Lab Reports
            • AI-Based Report Explanation
            • Nearby Pathology Lab Finder
            • Secure Patient Dashboard
            • Appointment Scheduling
            • Health Test Packages
            """;
        }

        if (lowerMessage.contains("booking") || lowerMessage.contains("appointment")) {
            return """
            📅 Booking Instructions:

            1. Login to your account
            2. Select your required test
            3. Choose home collection or lab visit
            4. Select date & time
            5. Confirm booking

            You will receive confirmation via email/SMS.
            """;
        }

        if (lowerMessage.contains("why use") || lowerMessage.contains("benefit")) {
            return """
            💙 Why Choose Pathology Care?

            • Trusted & Certified Labs
            • Fast & Accurate Reports
            • Affordable Pricing
            • AI-Powered Report Insights
            • Easy Online Booking
            • Secure Medical Data Handling
            • 24/7 Support
            """;
        }

        if (lowerMessage.contains("feature")) {
            return """
            🚀 Key Features of Pathology Care Platform:

            ✔ User-Friendly Dashboard
            ✔ Real-Time Booking System
            ✔ Report Download (PDF)
            ✔ AI Medical Assistant
            ✔ Secure Data Encryption
            ✔ Mobile Responsive Interface
            ✔ Nearby Lab Search
            """;
        }

        // =========================
        // 2. Block Non-Medical/Technical Requests
        // =========================
        if (lowerMessage.contains("python") || lowerMessage.contains("java") ||
            lowerMessage.contains("code") || lowerMessage.contains("program") ||
            lowerMessage.contains("algorithm") || lowerMessage.contains("calculator")) {
            return "I am a Pathology Care AI assistant. I can only help with diagnostics, pathology, and healthcare-related questions.";
        }

        // =========================
        // 3. AI Response for Medical/Pathology Queries
        // =========================
        return callOpenRouter(userMessage);
    }

    // =========================
    // Separate Method for OpenRouter API Call
    // =========================
    private String callOpenRouter(String userMessage) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> request = new HashMap<>();
            request.put("model", model);

            List<Map<String, String>> messages = new ArrayList<>();
            
            // SYSTEM PROMPT: Enforce Pathology/Healthcare Focus
            messages.add(Map.of(
                    "role", "system",
                    "content", """
                    You are Pathology Care AI, a medical assistant.

                    You ONLY answer questions related to:
                    - Pathology laboratories
                    - Medical tests
                    - Pathology procedures
                    - Pathology Care Platform services
                    - Diagnosis explanations
                    - Diseases and symptoms
                    - Lab reports
                    - Healthcare information

                    If a user asks anything unrelated to healthcare,
                    politely refuse with:
                    "I am a Pathology Care AI assistant. I can only help with diagnostics, pathology, and healthcare-related questions."
                    """
            ));

            // USER MESSAGE
            messages.add(Map.of(
                    "role", "user",
                    "content", userMessage
            ));

            request.put("messages", messages);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

            List choices = (List) response.getBody().get("choices");
            Map choice = (Map) choices.get(0);
            Map messageMap = (Map) choice.get("message");

            return messageMap.get("content").toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, something went wrong while processing your request.";
        }
    }
}