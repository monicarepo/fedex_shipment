package com.example.fedex.utility;

import java.util.UUID;

public class Utils {
    public static String generateTrackingNumber() {
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 16)
                .toUpperCase();
    }
}
