package vekster.lightanticheat.players;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import vekster.lightanticheat.LightAntiCheat;
import vekster.lightanticheat.extra.Tps;
import vekster.lightanticheat.usage.Config;

//To update this values in LACPlayer

public class ViolationUpdate {

    public static void enableViolationUpdate() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(LightAntiCheat.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player == null)
                    return;
                LACPlayer lacPlayer = LACPlayer.get(player);
                if (lacPlayer == null)
                    return;
                //lacPlayer.ping
                lacPlayer.ping = player.getPing();
                //lacPlayer.isBypass
                if (Config.minTps != 0.0D && Tps.tps < Config.minTps || Config.maxPing != 0 && lacPlayer.ping > Config.maxPing ||
                        !Config.java && !lacPlayer.isGeyser || !Config.bedrock && lacPlayer.isGeyser) {
                    lacPlayer.isBypass = true;
                } else if (lacPlayer.counterForViolationUpdate % 3 == 0) {
                    Bukkit.getScheduler().runTask(LightAntiCheat.getInstance(), () -> lacPlayer.isBypass = player.hasPermission("lightanticheat.bypass") && !Config.disableBypassPermission);
                }
                //MorePackets
                if (lacPlayer.morepacketsViolations < 225 && lacPlayer.morepacketsViolations > -1)
                    lacPlayer.morepacketsFinalViolations = 0;
                lacPlayer.morepacketsViolations = 0;
                //LacPlayer violation/check values
                if (lacPlayer.counterForViolationUpdate == 16) {
                    lacPlayer.movementViolations = 0;
                    lacPlayer.accurateViolations = 0;
                    lacPlayer.interactViolations = 0;
                    lacPlayer.killauraViolations = 0;
                    lacPlayer.forCancelFirstViolationMethod = 0;
                    lacPlayer.forInaccurateViolationMethod = 0;
                    lacPlayer.sameFallDistance = 0;
                    lacPlayer.sameVerticalVelocity = 0;
                    lacPlayer.fallDuration = 0;
                    lacPlayer.counterForViolationUpdate = 0;
                } else {
                    lacPlayer.counterForViolationUpdate++;
                }
            }
        }, 70, 70);
    }

}
