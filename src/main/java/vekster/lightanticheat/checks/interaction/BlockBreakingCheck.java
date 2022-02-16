package vekster.lightanticheat.checks.interaction;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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

        Block block1 = event.getBlock();
        Location blockLocation = block1.getLocation();
        Location eyeLocation = player.getEyeLocation();
        if (eyeLocation.distance(blockLocation) < 1.0D || time - lacPlayer.lastClickTime < 100)
            return;

        //NukerA1 (the player doesn't look at the block)
        if (Config.nukerA && Math.abs(eyeLocation.getPitch()) < 30.0F && Math.abs(eyeLocation.getY() - blockLocation.getY()) > 3.5D) {
            Violations.interactViolation(player, CheckTypes.NUKER_A_1, lacPlayer);
            event.setCancelled(true);
        }

        //NukerA2 (the player doesn't look at the block)
        Block block2 = player.getTargetBlockExact(6);
        if (Config.nukerA && block2 != null && Math.sqrt(Math.pow(block1.getX() - block2.getX(), 2.0D) + Math.pow(block1.getZ() - block2.getZ(), 2.0D)) > 5.75D) {
            Violations.interactViolation(player, CheckTypes.NUKER_A_2, lacPlayer);
            event.setCancelled(true);
        }

    }

}
