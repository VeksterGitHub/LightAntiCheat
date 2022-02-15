package vekster.lightanticheat.api;

import org.bukkit.entity.Player;
import vekster.lightanticheat.players.LACPlayer;

public class Utils {

    public static boolean isApiBypass(Player player) {
        LACPlayer lacPlayer = LACPlayer.get(player);
        if (lacPlayer != null)
            return lacPlayer.isApiBypass;
        return false;
    }

    public static void setApiBypass(Player player, boolean bypass) {
        LACPlayer lacPlayer = LACPlayer.get(player);
        if (lacPlayer != null)
            lacPlayer.isApiBypass = bypass;
    }

}
