package vekster.lightanticheat.players;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import vekster.lightanticheat.LightAntiCheat;
import vekster.lightanticheat.api.LacFlagEvent;
import vekster.lightanticheat.api.LacPunishmentEvent;
import vekster.lightanticheat.extra.Alerts;
import vekster.lightanticheat.extra.CheckTypes;
import vekster.lightanticheat.usage.Config;
import vekster.lightanticheat.usage.Log;

public class Violations extends Config {

    //The methods for counting violations

    private static short maxMovementViolations;
    private static short halfOfMaxMovementViolations;
    private static short maxAccurateViolations;
    private static short halfOfMaxAccurateViolations;
    private static short maxAlmostAccurateViolations;
    private static short halfOfMaxAlmostAccurateViolations;
    private static short maxKillauraViolations;
    private static short halfOfMaxKillauraViolations;

    public static void updateViolationLimits() {
        maxMovementViolations = (short) (40 * sensitivityMultiplier);
        halfOfMaxMovementViolations = (short) (maxMovementViolations / 2);
        maxAccurateViolations = (short) (6 * sensitivityMultiplier);
        halfOfMaxAccurateViolations = (short) (maxAccurateViolations / 2);
        maxAlmostAccurateViolations = (short) (14 * sensitivityMultiplier);
        halfOfMaxAlmostAccurateViolations = (short) (maxAlmostAccurateViolations / 2);
        maxKillauraViolations = (short) (8 * sensitivityMultiplier);
        halfOfMaxKillauraViolations = (short) (maxKillauraViolations / 2);
    }

    private static final PluginManager pluginManager = Bukkit.getPluginManager();

    public static void movementViolation(Player player, CheckTypes check, LACPlayer lacPlayer) {
        LacFlagEvent event = new LacFlagEvent(player, check);
        pluginManager.callEvent(event);
        if (event.isCancelled())
            return;
        if (lacPlayer.movementViolations <= maxMovementViolations) {
            lacPlayer.movementViolations++;
            if (Config.warningNotice != null && lacPlayer.movementViolations == halfOfMaxMovementViolations)
                Alerts.waringNotification(player, check, lacPlayer);
        } else {
            checkForTimer(player, check);
            lacPlayer.movementViolations = halfOfMaxMovementViolations;
        }
        if (debugMode)
            Alerts.debugNotification(player, check);
    }

    private static boolean isNotMovementTeleport(CheckTypes check) {
        return (check != CheckTypes.FLIGHT_A_1 && check != CheckTypes.FLIGHT_A_2 && check != CheckTypes.FLIGHT_B_0 && check != CheckTypes.FLIGHT_C_0 && check != CheckTypes.FLIGHT_D_0 &&
                check != CheckTypes.IRREGULAR_MOVEMENT_A_1 && check != CheckTypes.IRREGULAR_MOVEMENT_A_2 && check != CheckTypes.IRREGULAR_MOVEMENT_B_0 && check != CheckTypes.IRREGULAR_MOVEMENT_C_1 &&
                check != CheckTypes.IRREGULAR_MOVEMENT_C_2 && check != CheckTypes.IRREGULAR_MOVEMENT_C_3);
    }

    public static void accurateViolation(Player player, CheckTypes check, LACPlayer lacPlayer) {
        LacFlagEvent event = new LacFlagEvent(player, check);
        pluginManager.callEvent(event);
        if (event.isCancelled())
            return;
        if (lacPlayer.accurateViolations <= maxAccurateViolations) {
            lacPlayer.accurateViolations++;
        } else {
            checkForTimer(player, check);
            lacPlayer.accurateViolations = halfOfMaxAccurateViolations;
        }
        if (isNotMovementTeleport(check)) {
            if (debugMode)
                Alerts.debugNotification(player, check);
            if (Config.warningNotice != null && lacPlayer.accurateViolations == halfOfMaxAccurateViolations)
                Alerts.waringNotification(player, check, lacPlayer);
        }
    }

    public static void interactViolation(Player player, CheckTypes check, LACPlayer lacPlayer) {
        LacFlagEvent event = new LacFlagEvent(player, check);
        pluginManager.callEvent(event);
        if (event.isCancelled())
            return;
        if (lacPlayer.interactViolations <= maxAlmostAccurateViolations) {
            lacPlayer.interactViolations++;
        } else {
            checkForTimer(player, check);
            lacPlayer.interactViolations = halfOfMaxAlmostAccurateViolations;
        }
        if (isNotMovementTeleport(check)) {
            if (debugMode)
                Alerts.debugNotification(player, check);
            if (Config.warningNotice != null && lacPlayer.interactViolations == halfOfMaxAlmostAccurateViolations)
                Alerts.waringNotification(player, check, lacPlayer);
        }
    }

    //For checks lasting for ~80 ticks in a row
    public static void temporarilyViolation(final Player player, CheckTypes check, LACPlayer lacPlayer, boolean isAllowed) {
        LacFlagEvent event = new LacFlagEvent(player, check);
        pluginManager.callEvent(event);
        if (event.isCancelled())
            return;
        if (lacPlayer.temporarilyViolations == 0) {
            lacPlayer.temporarilyViolations = 1;
            Bukkit.getScheduler().runTaskLaterAsynchronously(LightAntiCheat.getInstance(), () -> {
                if (player == null)
                    return;
                lacPlayer.temporarilyViolations = 0;
                lacPlayer.IsTemporarilyViolationAllowed = false;
            }, 85);
        } else if (lacPlayer.temporarilyViolations < 83) {
            lacPlayer.temporarilyViolations++;
            if (isAllowed)
                lacPlayer.IsTemporarilyViolationAllowed = true;
        } else if (lacPlayer.IsTemporarilyViolationAllowed) {
            if (debugMode)
                Alerts.debugNotification(player, check);
            if (Config.warningNotice != null)
                Alerts.waringNotification(player, check, lacPlayer);
            checkForTimer(player, check);
            lacPlayer.temporarilyViolations = 41;
        }
    }

    public static void killauraViolation(Player player, LACPlayer lacPlayer, CheckTypes check) {
        LacFlagEvent event = new LacFlagEvent(player, check);
        pluginManager.callEvent(event);
        if (event.isCancelled())
            return;
        if (lacPlayer.killauraViolations > maxKillauraViolations) {
            checkForTimer(player, check);
            lacPlayer.killauraViolations = halfOfMaxKillauraViolations;
        }
        if (Config.warningNotice != null && lacPlayer.killauraViolations == halfOfMaxKillauraViolations)
            Alerts.waringNotification(player, check, lacPlayer);
    }

    public static void morepacketsViolation(Player player, LACPlayer lacPlayer) {
        if (lacPlayer.morepacketsViolations < 225) {
            lacPlayer.morepacketsViolations++;
        } else if (lacPlayer.morepacketsViolations == 225) {
            lacPlayer.morepacketsViolations++;
            lacPlayer.morepacketsFinalViolations++;
            if (Config.debugMode)
                Alerts.debugNotification(player, CheckTypes.MORE_PACKETS_A_0);
            if (Config.warningNotice != null && lacPlayer.morepacketsFinalViolations == 2)
                Alerts.waringNotification(player, CheckTypes.MORE_PACKETS_A_0, lacPlayer);
            if (lacPlayer.morepacketsFinalViolations >= 3)
                Violations.checkForTimer(player, CheckTypes.MORE_PACKETS_A_0);
        }
    }

    public static void checkForTimer(final Player player, final CheckTypes check) {
        LACPlayer lacPlayer = LACPlayer.get(player);
        if (lacPlayer == null)
            return;
        if (!punishmentTimer) {
            if (lacPlayer.isPunishmentTimer)
                return;
            lacPlayer.isPunishmentTimer = true;
            punishment(player, check);
        } else if (!lacPlayer.isPunishmentTimer) {
            lacPlayer.isPunishmentTimer = true;
            Bukkit.getScheduler().runTaskLater(LightAntiCheat.getInstance(), () -> {
                if (player == null)
                    return;
                Violations.punishment(player, check);
            }, punishmentTimerDelayTime * 20L);
        }
    }

    private static void punishment(final Player player, final CheckTypes check) {
        LACPlayer lacPlayer = LACPlayer.get(player);
        if (punishmentCommand == null || lacPlayer == null)
            return;
        LacPunishmentEvent event = new LacPunishmentEvent(player, check);
        pluginManager.callEvent(event);
        if (event.isCancelled())
            return;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Violations.punishmentCommand.replace("%username%", player.getName()).replace("%check%", Log.checkTypeToString(check)));
        vekster.lightanticheat.usage.Log.logViolation(player, check, lacPlayer);
        if (punishmentNotice != null)
            Bukkit.broadcast(punishmentNotice.replace("%username%", player.getName()).replace("%check%", Log.checkTypeToString(check)), "lightanticheat.alerts");
        Bukkit.getScheduler().runTaskLater(LightAntiCheat.getInstance(), () -> {
            LACPlayer lacPlayer1 = LACPlayer.get(player);
            if (lacPlayer1 != null)
                lacPlayer1.isPunishmentTimer = false;
        }, 20);
    }

}
