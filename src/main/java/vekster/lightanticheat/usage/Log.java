package vekster.lightanticheat.usage;

import com.google.common.base.CaseFormat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import vekster.lightanticheat.LightAntiCheat;
import vekster.lightanticheat.extra.CheckTypes;
import vekster.lightanticheat.extra.Tps;
import vekster.lightanticheat.players.LACPlayer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Log {

    //For the log file

    private final File file;
    private final FileConfiguration config;
    private static vekster.lightanticheat.usage.Log log;

    public Log(String name) {
        file = new File(LightAntiCheat.getInstance().getDataFolder(), name);
        try {
            if (!file.exists() && !file.createNewFile())
                throw new IOException();
        } catch (IOException exception) {
            throw new RuntimeException("Errors during creation this log file", exception);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void updateLogFile() {
        if (Config.configFile != null)
            log = new Log(Config.configFile);
    }

    private static final DateTimeFormatter logTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static String checkTypeToString(CheckTypes checkType) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, String.valueOf(checkType));
    }

    public static void logViolation(Player player, CheckTypes checkType, LACPlayer lacPlayer) {
        if (Config.configFile == null || log == null)
            return;
        UUID uuid = UUID.randomUUID();
        FileConfiguration config = log.config;
        for (byte i = 0; i < Config.logsFormat.length; i = (byte) (i + 1)) {
            if (Objects.equals(Config.logsFormat[i], "time"))
                config.set(uuid + ".time", logTimeFormatter.format(LocalDateTime.now()));
            else if (Objects.equals(Config.logsFormat[i], "username"))
                config.set(uuid + ".username", player.getName());
            else if (Objects.equals(Config.logsFormat[i], "uuid"))
                config.set(uuid + ".uuid", String.valueOf(player.getUniqueId()));
            else if (Objects.equals(Config.logsFormat[i], "ip"))
                config.set(uuid + ".ip", String.valueOf(player.getAddress()).replace("/", "").split(":")[0]);
            else if (Objects.equals(Config.logsFormat[i], "check"))
                config.set(uuid + ".check", checkTypeToString(checkType));
            else if (Objects.equals(Config.logsFormat[i], "ping"))
                config.set(uuid + ".ping", lacPlayer.ping);
            else if (Objects.equals(Config.logsFormat[i], "tps"))
                config.set(uuid + ".tps", String.format("%.2f", Math.round(Tps.tps * 100.0D) / 100.0D));
        }
        Bukkit.getScheduler().runTaskAsynchronously(LightAntiCheat.getInstance(), () -> {
            try {
                config.save(log.file);
            } catch (IOException exception) {
                throw new RuntimeException("Errors when saving this log file");
            }
        });
    }

}
