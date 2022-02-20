package vekster.lightanticheat.players;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.geysermc.floodgate.api.FloodgateApi;
import vekster.lightanticheat.extra.Alerts;
import vekster.lightanticheat.usage.Config;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LACPlayer {

    public static final Map<Player, LACPlayer> PLAYERS = new ConcurrentHashMap<>();

    public boolean isPunishmentTimer = false;
    public boolean isBypass = false;
    public boolean isApiBypass = false;
    public int ping = 0;
    public boolean isGeyser = false;
    public byte counterForViolationUpdate = 0;
    public short movementViolations = 0;
    public short accurateViolations = 0;
    public short interactViolations = 0;
    public short killauraViolations = 0;
    public byte temporarilyViolations = 0;
    public boolean IsTemporarilyViolationAllowed = false;
    public short morepacketsViolations = 0;
    public byte morepacketsFinalViolations = 0;
    public long lastFireworkTime = 0;
    public float lastElytraSpeed = 0.0F;
    public byte forInaccurateViolationMethod = 0;
    public byte forCancelFirstViolationMethod = 0;
    public float lastNonGroundViolationX = 0.0F;
    public float lastNonGroundViolationY = 0.0F;
    public float lastNonGroundViolationZ = 0.0F;
    public long lastSpeedingBlockTime = 0;
    public float lastY = 0;
    public byte sameY = 0;
    public float lastFallDistance = (float) 1.2345;
    public byte sameFallDistance = 0;
    public byte fallDuration = 0;
    public double lastVerticalVelocity = (float) 1.2345;
    public byte sameVerticalVelocity = 0;
    public long joinTime = 0;
    public long lastGlidingTime = 0;
    public long lastPreventTime = 0;
    public long lastGroundTime = 0;
    public long lastNonGroundTime = 0;
    public long lastQuickBreakTime = 0;
    public long lastHitTime = 0;
    public long lastWarningAlertTime = 0;
    public long lastTeleportTime = 0;
    public long lastClickTime = 0;

    private static final PluginManager pluginManager = Bukkit.getPluginManager();

    public static void add(Player player) {
        final LACPlayer lacPlayer = new LACPlayer();
        PLAYERS.put(player, lacPlayer);
        lacPlayer.isGeyser = pluginManager.isPluginEnabled("floodgate") && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
        final long time = System.currentTimeMillis();
        lacPlayer.joinTime = time;
        lacPlayer.lastGroundTime = time;
        lacPlayer.lastNonGroundTime = time;
        lacPlayer.lastHitTime = time;
        lacPlayer.isBypass = player.hasPermission("lightanticheat.bypass") && !Config.disableBypassPermission ||
                !Config.java && !lacPlayer.isGeyser || !Config.bedrock && lacPlayer.isGeyser;
    }

    public static void remove(final Player player) {
        LACPlayer.PLAYERS.remove(player);
    }

    public static LACPlayer get(Player player) {
        return PLAYERS.get(player);
    }

    public static void addAllPlayers() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        if (!onlinePlayers.isEmpty()) {
            for (final Player onlinePlayer : onlinePlayers)
                add(onlinePlayer);
            Alerts.serverReloadedAlert();
        }
    }

}
