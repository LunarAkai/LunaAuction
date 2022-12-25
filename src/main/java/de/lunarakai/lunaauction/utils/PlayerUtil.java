package de.lunarakai.lunaauction.utils;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerUtil {

    public static UUID getUuid(Player player) {
        return player.getUniqueId();
    }

}
