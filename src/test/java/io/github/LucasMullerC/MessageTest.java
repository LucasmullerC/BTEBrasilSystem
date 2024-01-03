package io.github.LucasMullerC;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.util.MessageUtils;

public class MessageTest {
    private ServerMock server;
    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(BTEBrasilSystem.class);
    }

    @Test
    public void getPortugueseMessageTest() {
        PlayerMock player = server.addPlayer();
        player.setLocale(new Locale("pt"));
        String expected = "Sua conta não está linkada ao Discord, Siga os passos abaixo:";
        String result = MessageUtils.getMessage("link1", player);
        assertTrue(expected.equals(result));
    }

    @Test
    public void getEnglishMessageTest() {
        PlayerMock player = server.addPlayer();
        String expected = "Your account is not linked to Discord, follow the steps below:";
        String result = MessageUtils.getMessage("link1", player);
        assertTrue(expected.equals(result));
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }
}
