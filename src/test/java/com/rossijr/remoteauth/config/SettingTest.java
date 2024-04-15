package com.rossijr.remoteauth.config;

import com.rossijr.remoteauth.config.messages.DefaultMessages;
import com.rossijr.remoteauth.config.messages.ParameterBuilder;
import com.rossijr.remoteauth.config.messages.Parameters;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SettingTest {
    private static final Logger logger = Logger.getLogger(SettingTest.class.getName());

    @Test
    public void testGetMessages() {
        try {
            logger.info("Testing getMessages() method");
            String m1 = "§aHello §a§l%player%§a§r- Nice to see you again!\n> §a> Use /login <password> to login!";
            String m2 = "§aHello §a§l%player%§a§r- Welcome to the server!\n> §a> Use /register <password> <confirm password> to register!";
            String m3 = "%chatPrefix%§aPrefix test1";
            String m4 = "%chatPrefix%§aPrefix test2";

            String newPrefix = "§a§l[Server] §r";

            HashMap<String, String> messages = new HashMap<>();
            messages.put("messages.welcome.login", m1);
            messages.put("messages.welcome.register", m2);
            messages.put("messages.error.login_usage", m3);

            logger.info("Setting messages variable");
            Settings.setMessages(messages);

            logger.info("Testing getMessage without parameters");
            assertEquals(Settings.getMessage(DefaultMessages.WELCOME_LOGIN), m1);
            assertEquals(Settings.getMessage(DefaultMessages.ERROR_ALREADY_LOGGED_IN), DefaultMessages.ERROR_ALREADY_LOGGED_IN.message);
            assertNotEquals(Settings.getMessage(DefaultMessages.WELCOME_LOGIN), DefaultMessages.WELCOME_LOGIN.message);

            logger.info("Testing getMessage with parameters");
            assertEquals(Settings.getMessage(DefaultMessages.WELCOME_REGISTER, ParameterBuilder.create()
                    .addParameter(Parameters.PLAYER, "Rossi")
                    .build()), m2.replace("%player%", "Rossi"));

            logger.info("Testing getMessage related to chatPrefix");
            assertEquals(Settings.getMessage(DefaultMessages.ERROR_LOGIN_USAGE), m3.replace("%chatPrefix%", Settings.getMessage(DefaultMessages.CHAT_PREFIX)));

            messages.clear();
            messages.put("messages.error.login_usage", m4);
            messages.put("prefix.chat_prefix", newPrefix);
            Settings.setMessages(messages);
            assertEquals(Settings.getMessage(DefaultMessages.ERROR_LOGIN_USAGE), m4.replace("%chatPrefix%", newPrefix));

            logger.info("Test passed");
        } catch (Exception e) {
            logger.severe("Error during testing getMessages() method");
            e.printStackTrace();
        }
    }
}
