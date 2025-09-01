package com.radvonswag.playerstats.playerdata;

import com.radvonswag.playerstats.cache.UserCacheHandler;
import com.radvonswag.playerstats.model.PlayerStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerDataHandlerTest {

    private final String UUID1 = "9ce6c71f-a6e8-40ab-a580-3ba226b34d12";
    private final String UUID2 = "b8404ae7-ed21-4d10-96ee-d5071d869b0c";
    private final String UUID3 = "a76804ac-b31d-4400-96e1-636ebb889af2";
    private final String userName1 = "LoneGator899";
    private final String userName2 = "LuckyWolf262";
    private final String userName3 = "SwiftCobra412";

    @Mock
    UserCacheHandler userCacheHandler;

    @InjectMocks
    PlayerDataHandler playerDataHandler;

    @Test
    public void associateUserName_test() {
        Map<String, String> mockUserCache = new HashMap<>();
        mockUserCache.put(UUID1, userName1);
        mockUserCache.put(UUID2, userName2);
        mockUserCache.put(UUID3, userName3);

        PlayerStats playerStats = new PlayerStats();
        playerStats.setPlayerUUID(UUID2);

        when(userCacheHandler.loadUserCache()).thenReturn(mockUserCache);

        String expectedUserName = userName2;
        String actualUserName = playerDataHandler.associateUserName(playerStats);

        assertEquals(expectedUserName, actualUserName);
    }

}