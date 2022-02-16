package vekster.lightanticheat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import vekster.lightanticheat.checks.Listeners;
import vekster.lightanticheat.checks.combat.CombatCheck;
import vekster.lightanticheat.checks.interaction.BlockBreakingCheck;
import vekster.lightanticheat.checks.interaction.BlockPlacementCheck;
import vekster.lightanticheat.checks.movement.MovementCheck;
import vekster.lightanticheat.extra.Alerts;
import vekster.lightanticheat.extra.Metrics;
import vekster.lightanticheat.extra.Tps;
import vekster.lightanticheat.players.LACPlayer;
import vekster.lightanticheat.players.ViolationUpdate;
import vekster.lightanticheat.usage.Commands;
import vekster.lightanticheat.usage.Config;

import java.util.Objects;

public class LightAntiCheat extends JavaPlugin {

    private static LightAntiCheat lightAntiCheat;

    @Override
    public void onEnable() {
        lightAntiCheat = this;
        saveDefaultConfig();
        Config.updateByConfigUpdater();
        reloadConfig();
        Config.getValuesFromConfig();
        Objects.requireNonNull(LightAntiCheat.getInstance().getCommand("lightanticheat")).setExecutor(new Commands());
        LACPlayer.addAllPlayers();
        Tps.enableTpsCalculation();
        loadBukkitListeners();
        ViolationUpdate.enableViolationUpdate();
        Alerts.checkForUnsupportedVersion();
        Alerts.checkForUpdates();
        new Metrics(this, 12841);
    }

    public static LightAntiCheat getInstance() {
        return lightAntiCheat;
    }

    private void loadBukkitListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new MovementCheck(), this);
        pluginManager.registerEvents(new CombatCheck(), this);
        pluginManager.registerEvents(new BlockPlacementCheck(), this);
        pluginManager.registerEvents(new BlockBreakingCheck(), this);
        pluginManager.registerEvents(new Listeners(), this);
    }

}
