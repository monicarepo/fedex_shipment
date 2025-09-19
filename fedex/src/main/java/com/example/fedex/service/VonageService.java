package com.example.fedex.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.SmsSubmissionResponseMessage;
import com.vonage.client.sms.messages.TextMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VonageService {

    private final VonageClient client;

    public VonageService(
            @Value("${vonage.api.key}") String apiKey,
            @Value("${vonage.api.secret}") String apiSecret
    ) {
        this.client = VonageClient.builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();
    }

    public void sendSms(String to, String text) {
        String fromPhoneNumber = "17325938449";
        TextMessage message = new TextMessage(fromPhoneNumber, to, text);

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        for (SmsSubmissionResponseMessage r : response.getMessages()) {
            if ("0".equals(r.getStatus())) {
                System.out.println("Message sent successfully to " + to);
            } else {
                System.err.println("Message failed with status: " + r.getStatus() +
                        " - " + r.getErrorText());
            }
        }
    }
}
