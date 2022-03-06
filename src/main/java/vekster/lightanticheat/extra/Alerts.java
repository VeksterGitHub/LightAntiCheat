package vekster.lightanticheat.extra;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import vekster.lightanticheat.LightAntiCheat;
import vekster.lightanticheat.api.CheckTypes;
import vekster.lightanticheat.extra.updater.SpigotUpdater;
import vekster.lightanticheat.extra.updater.Updater;
import vekster.lightanticheat.players.LACPlayer;
import vekster.lightanticheat.usage.Config;
import vekster.lightanticheat.usage.Log;

import java.util.Collection;

public class Alerts {

    private static final String lacVersion = LightAntiCheat.getInstance().getDescription().getVersion();

    public static void checkForUnsupportedVersion() {
        final String bukkitVersion = Bukkit.getServer().getBukkitVersion();
        if (bukkitVersion.contains("1.18") || bukkitVersion.contains("1.17"))
            return;
        Bukkit.getScheduler().runTaskTimer(LightAntiCheat.getInstance(), () -> {
            final ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
            consoleSender.sendMessage(ChatColor.RED + "     LightAntiCheat " + lacVersion);
            consoleSender.sendMessage(ChatColor.RED + "Unsupported Minecraft version!");
        }, 0, 600);
    }

    public static void serverReloadedAlert() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "(LightAntiCheat " + lacVersion + ") Expect no support from the LightAntiCheat developer when using /reload.");
    }

    public static void debugNotification(Player player, CheckTypes check) {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        String alert = Config.debugNotice.replace("%username%", player.getName()).replace("%check%", Log.checkTypeToString(check));
        Bukkit.getConsoleSender().sendMessage(alert);
        for (final Player onlinePlayer : onlinePlayers)
            if (onlinePlayer.hasPermission("lightanticheat.alerts") || onlinePlayer.hasPermission("lightanticheat.alerts.debug"))
                player.sendMessage(alert);
    }

    public static void waringNotification(Player player, CheckTypes check, LACPlayer lacPlayer) {
        final long time = System.currentTimeMillis();
        if (time - lacPlayer.lastWarningAlertTime > 60000) {
            Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
            String alert = Config.warningNotice.replace("%username%", player.getName()).replace("%check%", Log.checkTypeToString(check));
            Bukkit.getConsoleSender().sendMessage(alert);
            for (final Player onlinePlayer : onlinePlayers)
                if (onlinePlayer.hasPermission("lightanticheat.alerts") || onlinePlayer.hasPermission("lightanticheat.alerts.warn"))
                    player.sendMessage(alert);
            lacPlayer.lastWarningAlertTime = time;
        }
    }

    public static void punishmentNotification(Player player, CheckTypes check) {
        if (Config.punishmentNotice == null)
            return;
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        String alert = Config.punishmentNotice.replace("%username%", player.getName()).replace("%check%", Log.checkTypeToString(check));
        Bukkit.getConsoleSender().sendMessage(alert);
        for (final Player onlinePlayer : onlinePlayers)
            if (onlinePlayer.hasPermission("lightanticheat.alerts") || onlinePlayer.hasPermission("lightanticheat.alerts.punishment"))
                player.sendMessage(alert);
    }

    public static void checkForUpdates() {
        if (Config.updateCheck) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(LightAntiCheat.getInstance(), () -> {
                final SpigotUpdater updater = new SpigotUpdater(LightAntiCheat.getInstance(), 96341, Updater.UpdateType.VERSION_CHECK);
                if (updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE) {
                    final TextComponent updateNotification = new TextComponent("§c(§fLAC§c)§f Update available: ");
                    final TextComponent version = new TextComponent("§cLightAntiCheat " + updater.getLatestName());
                    version.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/lightanticheat.96341/"));
                    version.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Download the update from SpigotMC")));
                    updateNotification.addExtra(version);
                    Bukkit.getConsoleSender().sendMessage("§c(§fLAC§c)§f Update available: §cLightAntiCheat " + updater.getLatestName());
                    Bukkit.getConsoleSender().sendMessage("§c(§fLAC§c)§7 Spigot link: https://www.spigotmc.org/resources/lightanticheat.96341/");
                    for (final Player player : Bukkit.getOnlinePlayers())
                        if (player.hasPermission("lightanticheat.alerts") || player.hasPermission("lightanticheat.alerts.update"))
                            player.spigot().sendMessage(updateNotification);
                }
            }, 0, 360000);
        }
    }

}
