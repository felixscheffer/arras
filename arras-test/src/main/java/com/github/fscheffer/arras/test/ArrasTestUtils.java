package com.github.fscheffer.arras.test;


public class ArrasTestUtils {

    public static boolean isBlank(String input) {
        return input == null || input.length() == 0 || input.trim().length() == 0;
    }

    public static final String buildUrl(String host, String port, String context) {

        StringBuilder builder = new StringBuilder();

        builder.append(host);

        if (!isBlank(port)) {
            builder.append(":");
            builder.append(port);
        }

        if (!isBlank(context)) {

            if (!context.startsWith("/")) {
                builder.append("/");
            }

            builder.append(context);
        }

        return builder.toString();
    }

    public static final String appendPath(String baseUrl, String path) {
        return path.startsWith("/") ? baseUrl + path : baseUrl + "/" + path;
    }
}
