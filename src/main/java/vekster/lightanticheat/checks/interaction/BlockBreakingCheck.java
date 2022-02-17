package vekster.lightanticheat.checks.interaction;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;
import vekster.lightanticheat.api.CheckTypes;
import vekster.lightanticheat.players.LACPlayer;
import vekster.lightanticheat.players.Violations;
import vekster.lightanticheat.usage.Config;

public class BlockBreakingCheck implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void breakingCheck(BlockBreakEvent event) {

        Player player = event.getPlayer();
        LACPlayer lacPlayer = LACPlayer.get(player);
        long time = System.currentTimeMillis();

        if (time - lacPlayer.lastClickTime < 100)
            lacPlayer.lastQuickBreakTime = time;

        if (!Config.nuker || lacPlayer == null || lacPlayer.isBypass || time - lacPlayer.joinTime <= Config.disablerTimeOnJoin ||
                Config.punishmentCommand == null || lacPlayer.isApiBypass)
            return;

        //NukerB (the breaking is not possible)
        if (Config.nukerB) {
            if (player.isBlocking() || player.isDead() || player.isSleeping())
                Violations.interactViolation(player, CheckTypes.NUKER_B_0, lacPlayer);
        }

        //NukerA0 (the player doesn't look at the block)
        if (!Config.nukerA)
            return;

        Block block = event.getBlock();
        Location blockLocation = block.getLocation();
        Location eyeLocation = player.getEyeLocation();
        double distance = eyeLocation.distance(blockLocation);

        if (distance < 1.0D || time - lacPlayer.lastClickTime < 100)
            return;

        Block block1 = player.getTargetBlockExact(6);
        if (block1 != null) {
            if (blockLocation.distance(block1.getLocation()) > 3.2D)
                Violations.interactViolation(player, CheckTypes.NUKER_A_1, lacPlayer);
        } else {
            Block block2 = eyeLocation.add(eyeLocation.getDirection().multiply(Math.round(distance))).getBlock();
            if (blockLocation.distance(block2.getLocation()) > 4.8D)
                Violations.interactViolation(player, CheckTypes.NUKER_A_2, lacPlayer);
        }

    }

}
