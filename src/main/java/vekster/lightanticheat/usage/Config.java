package vekster.lightanticheat.usage;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import vekster.lightanticheat.LightAntiCheat;
import vekster.lightanticheat.extra.configupdater.ConfigUpdater;
import vekster.lightanticheat.players.Violations;

import java.io.File;
import java.io.IOException;

public class Config {

    public static String punishmentCommand;
    public static boolean punishmentTimer;
    public static int punishmentTimerDelayTime;
    public static String unknownCommand;
    public static String reloadCommand;
    public static String unresolvedCommand;
    public static boolean flight;
    public static boolean flightA;
    public static boolean flightB;
    public static boolean flightC;
    public static boolean flightD;
    public static boolean boatFly;
    public static boolean elytraFly;
    public static boolean elytraFlyA;
    public static boolean elytraFlyB;
    public static boolean speed;
    public static boolean speedA;
    public static boolean speedB;
    public static boolean morePackets;
    public static boolean irregularMovement;
    public static boolean irregularMovementA;
    public static boolean irregularMovementB;
    public static boolean irregularMovementC;
    public static boolean fluidWalk;
    public static boolean fluidWalkA;
    public static boolean fluidWalkB;
    public static boolean fastClimb;
    public static boolean killAura;
    public static boolean killAuraA;
    public static boolean killAuraB;
    public static boolean killAuraC;
    public static boolean criticals;
    public static boolean irregularPlacement;
    public static boolean irregularPlacementA;
    public static boolean irregularPlacementB;
    public static boolean nuker;
    public static boolean nukerA;
    public static boolean nukerB;
    public static boolean reach;
    public static boolean groundSpoof;
    public static boolean groundSpoofA;
    public static boolean groundSpoofB;
    public static boolean noWeb;
    public static boolean noWebA;
    public static boolean noWebB;
    public static String punishmentNotice;
    public static String warningNotice;
    public static boolean cancelHacking;
    public static boolean highSpeedMode;
    public static int disablerTimeOnJoin;
    public static int disablerTimeOnLegalFlight;
    public static double minTps;
    public static int maxPing;
    public static int pingLimit;
    public static double sensitivityMultiplier;
    public static boolean debugMode;
    public static String[] logsFormat;
    public static String configFile;
    public static boolean disableBypassPermission;
    public static String debugNotice;
    public static boolean updateCheck;
    public static boolean java;
    public static boolean bedrock;

    public static void updateByConfigUpdater() {
        LightAntiCheat lightAntiCheat = LightAntiCheat.getInstance();
        try {
            ConfigUpdater.update(lightAntiCheat, "config.yml", new File(lightAntiCheat.getDataFolder(), "config.yml"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getValuesFromConfig() {
        FileConfiguration config = LightAntiCheat.getInstance().getConfig();

        ConfigurationSection punishmentSection = config.getConfigurationSection("punishment");
        punishmentCommand = punishmentSection.getBoolean("enable") ? punishmentSection.getString("punishmentCommand") : null;
        punishmentTimer = punishmentSection.getInt("punishmentTimer") != 0;
        punishmentTimerDelayTime = punishmentSection.getInt("punishmentTimer");
        sensitivityMultiplier = punishmentSection.getDouble("sensitivityMultiplier");

        ConfigurationSection notificationSection = config.getConfigurationSection("notifications");
        punishmentNotice = (notificationSection.getString("punishmentNotice").length() != 0) ? ChatColor.translateAlternateColorCodes('&', notificationSection.getString("punishmentNotice")) : null;
        warningNotice = (notificationSection.getString("warningNotice").length() != 0) ? ChatColor.translateAlternateColorCodes('&', notificationSection.getString("warningNotice")) : null;

        ConfigurationSection detectionSection = config.getConfigurationSection("detection");
        cancelHacking = detectionSection.getBoolean("cancelHacking");
        highSpeedMode = detectionSection.getBoolean("highSpeedMode");
        disablerTimeOnJoin = detectionSection.getInt("disablerTimeOnJoin") * 1000;
        disablerTimeOnLegalFlight = detectionSection.getInt("disablerTimeOnLegalFlight") * 1000;
        minTps = detectionSection.getDouble("minTps");
        maxPing = detectionSection.getInt("maxPing");
        pingLimit = detectionSection.getInt("pingLimit");

        ConfigurationSection debugModeSection = config.getConfigurationSection("debugMode");
        debugMode = debugModeSection.getBoolean("enable");
        debugNotice = ChatColor.translateAlternateColorCodes('&', debugModeSection.getString("debugNotice"));

        ConfigurationSection checksSection = config.getConfigurationSection("checks");
        String flightString = checksSection.getString("flight");
        flightA = flightString.contains("A");
        flightB = flightString.contains("B");
        flightC = flightString.contains("C");
        flightD = flightString.contains("D");
        flight = flightA || flightB || flightC || flightD;
        boatFly = checksSection.getString("boatFly").contains("A");
        String elytraFlyString = checksSection.getString("elytraFly");
        elytraFlyA = elytraFlyString.contains("A");
        elytraFlyB = elytraFlyString.contains("B");
        elytraFly = elytraFlyA || elytraFlyB;
        String speedString = checksSection.getString("speed");
        speedA = speedString.contains("A");
        speedB = speedString.contains("B");
        speed = speedA || speedB;
        morePackets = checksSection.getString("morePackets").contains("A");
        String irregularMovementString = checksSection.getString("irregularMovement");
        irregularMovementA = irregularMovementString.contains("A");
        irregularMovementB = irregularMovementString.contains("B");
        irregularMovementC = irregularMovementString.contains("C");
        irregularMovement = irregularMovementA || irregularMovementB || irregularMovementC;
        String fluidWalkString = checksSection.getString("fluidWalk");
        fluidWalkA = fluidWalkString.contains("A");
        fluidWalkB = fluidWalkString.contains("B");
        fluidWalk = fluidWalkA || fluidWalkB;
        fastClimb = checksSection.getString("fastClimb").contains("A");
        String killAuraString = checksSection.getString("killAura");
        killAuraA = killAuraString.contains("A");
        killAuraB = killAuraString.contains("B");
        killAuraC = killAuraString.contains("C");
        killAura = killAuraA || killAuraB || killAuraC;
        criticals = checksSection.getString("criticals").contains("A");
        String irregularPlacementString = checksSection.getString("irregularPlacement");
        irregularPlacementA = irregularPlacementString.contains("A");
        irregularPlacementB = irregularPlacementString.contains("B");
        irregularPlacement = irregularPlacementA || irregularPlacementB;
        String nukerString = checksSection.getString("nuker");
        nukerA = nukerString.contains("A");
        nukerB = nukerString.contains("B");
        nuker = nukerA || nukerB;
        reach = checksSection.getString("reach").contains("A");
        String groundSpoofString = checksSection.getString("groundSpoof");
        groundSpoofA = groundSpoofString.contains("A");
        groundSpoofB = groundSpoofString.contains("B");
        groundSpoof = groundSpoofA || groundSpoofB;
        String noWebString = checksSection.getString("noWeb");
        noWebA = noWebString.contains("A");
        noWebB = noWebString.contains("B");
        noWeb = noWebA || noWebB;

        updateCheck = config.getBoolean("updateChecker.enable", true);

        ConfigurationSection messagesSection = config.getConfigurationSection("messages");
        unknownCommand = ChatColor.translateAlternateColorCodes('&', messagesSection.getString("unknownCommand"));
        reloadCommand = ChatColor.translateAlternateColorCodes('&', messagesSection.getString("reloadCommand"));
        unresolvedCommand = ChatColor.translateAlternateColorCodes('&', messagesSection.getString("unresolvedCommand"));

        ConfigurationSection geyserSection = config.getConfigurationSection("geyser");
        java = geyserSection.getBoolean("java");
        bedrock = geyserSection.getBoolean("bedrock");

        ConfigurationSection logSection = config.getConfigurationSection("logs");
        configFile = logSection.getBoolean("enable") ? logSection.getString("file") : null;
        logsFormat = logSection.getString("format").split(",");

        ConfigurationSection permissionsSection = config.getConfigurationSection("permissions");
        disableBypassPermission = permissionsSection.getBoolean("disableBypassPermission");

        Log.updateLogFile();
        Violations.updateViolationLimits();
    }

}
