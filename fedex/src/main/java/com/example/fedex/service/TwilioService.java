package com.example.fedex.service;

import com.example.fedex.config.TwilioConfig;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Autowired
    private TwilioConfig twilioConfig;

    public void sendSms(String phoneNumber, String message) {
        Message.creator(
                new com.twilio.type.PhoneNumber("+17409720501"),
                new com.twilio.type.PhoneNumber(twilioConfig.getFromNumber()),
                message
        ).create();
    }
}
