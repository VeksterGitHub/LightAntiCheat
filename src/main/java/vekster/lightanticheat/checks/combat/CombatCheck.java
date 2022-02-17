package vekster.lightanticheat.checks.combat;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;
import vekster.lightanticheat.checks.combat.reachutils.AABB;
import vekster.lightanticheat.checks.combat.reachutils.Ray;
import vekster.lightanticheat.api.CheckTypes;
import vekster.lightanticheat.players.LACPlayer;
import vekster.lightanticheat.players.Violations;
import vekster.lightanticheat.usage.Config;

public class CombatCheck implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void hitCheck(EntityDamageByEntityEvent event) {

        if (event.getDamager().getType() != EntityType.PLAYER)
            return;

        EntityDamageEvent.DamageCause damageCause = event.getCause();
        if (damageCause != EntityDamageEvent.DamageCause.ENTITY_ATTACK && damageCause != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
            return;

        Player player = (Player) event.getDamager();
        Entity entity = event.getEntity();
        if (player.isGliding() || player.isRiptiding() || player.isInsideVehicle() || entity == null || entity.getType() == null)
            return;

        Location location2 = entity.getLocation();
        if (location2 == null)
            return;

        LACPlayer lacPlayer = LACPlayer.get(player);
        long time = System.currentTimeMillis();
        if (lacPlayer == null || lacPlayer.isBypass || time - lacPlayer.joinTime <= Config.disablerTimeOnJoin ||
                Config.punishmentCommand == null || lacPlayer.isApiBypass)
            return;

        Location location1 = player.getLocation();
        Location eyeLocation = player.getEyeLocation();
        int ping = lacPlayer.ping;
        boolean noTimeout = time - lacPlayer.lastHitTime < 8;
        double distance = eyeLocation.distance(location2);
        CheckTypes killAuraType = null;

        //KillAuraA (the player hits few times per tick)
        if (Config.killAuraA && damageCause == EntityDamageEvent.DamageCause.ENTITY_ATTACK && distance > 1.0D && noTimeout) {
            killAuraType = CheckTypes.KILL_AURA_A_0;
            if (Config.debugMode)
                Bukkit.broadcast(Config.debugNotice.replace("%username%", player.getName()).replace("%check%", String.valueOf(CheckTypes.KILL_AURA_A_0)), "lightanticheat.alerts");
        }

        //KillAuraB (the player doesn't look at the entity)
        if (Config.killAuraB && distance > 1.5D && !noTimeout) {
            float angle = eyeLocation.getDirection().angle(location2.toVector().setY(0.0D).subtract(eyeLocation.toVector().setY(0.0D)));
            if (ping > 150)
                angle -= 1;
            if (Math.sqrt(Math.pow(entity.getWidth(), 2.0D) + Math.pow(entity.getHeight(), 2.0D)) > 2.5D)
                angle -= 1;
            if (distance < 2.0D)
                angle -= 0.7F;
            if (angle > 1.6F && Math.abs(eyeLocation.getPitch()) < 40.0F) {
                killAuraType = CheckTypes.KILL_AURA_B_0;
                if (Config.debugMode)
                    Bukkit.broadcast(Config.debugNotice.replace("%username%", player.getName()).replace("%check%", String.valueOf(CheckTypes.KILL_AURA_B_0)), "lightanticheat.alerts");
            }
        }

        //KillAuraC (the hit is not possible)
        if (Config.killAuraC) {
            if (player.isBlocking() || player.isDead() || player.isSleeping() || player.getEntityId() == entity.getEntityId())
                Violations.interactViolation(player, CheckTypes.KILL_AURA_C_0, lacPlayer);
        }


        if (killAuraType != null) {
            lacPlayer.killauraViolations += 1;
            Violations.killauraViolation(player, lacPlayer, killAuraType);
        } else if (lacPlayer.killauraViolations >= -2) {
            lacPlayer.killauraViolations -= 1;
        }

        //CriticalsA (the player doesn't jump)
        if (Config.criticals) {
            Block block = location1.getBlock();
            String name = block.getType().name();
            double y = location1.getY();
            if (player.getFallDistance() > 0.0F && !event.getDamager().isOnGround() && player.getPotionEffect(PotionEffectType.BLINDNESS) == null &&
                    (y % 1.0D == 0.0D && (block.isEmpty() || name.contains("GRASS") || name.contains("_BUTTON")) ||
                            y % 0.5D == 0.0D && name.contains("_SLAB") ||
                            Math.round(y % 1.0D * 100000.0D % 25.0D) == 0 && block.getType() == Material.SNOW) &&
                    block.getRelative(BlockFace.DOWN).getType().isSolid())
                Violations.accurateViolation(player, CheckTypes.CRITICALS_A_0, lacPlayer);
        }

        //ReachA
        if (Config.reach) {
            int reachPing = lacPlayer.ping > 125 ? lacPlayer.ping - 125 : -1;
            double accurateDistance = AABB.from(entity).collidesD(Ray.from(player), 0, 16);
            if (accurateDistance != -1) {

                if (reachPing != -1)
                    accurateDistance -= reachPing / 250D + 0.5D;
                GameMode gameMode = player.getGameMode();
                if (accurateDistance >= 5.15D && !noTimeout && (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE))
                    Violations.interactViolation(player, CheckTypes.REACH_A_1, lacPlayer);

            } else {

                double playerX = location1.getX();
                double playerZ = location1.getZ();
                double playerY = eyeLocation.getY();
                double playerSecondY = location1.getY() + 1.0D;
                double entityX = location2.getX();
                double entityZ = location2.getZ();
                double entityY = location2.getY();
                double entitySecondY = location2.getY() + 0.5D;
                double approximateDistance = distance;
                double distance1 = Math.sqrt(Math.pow(playerX - entityX, 2.0D) + Math.pow(playerZ - entityZ, 2.0D) + Math.pow(playerSecondY - entityY, 2.0D));
                double distance2 = Math.sqrt(Math.pow(playerX - entityX, 2.0D) + Math.pow(playerZ - entityZ, 2.0D) + Math.pow(playerY - entitySecondY, 2.0D));
                double distance3 = Math.sqrt(Math.pow(playerX - entityX, 2.0D) + Math.pow(playerZ - entityZ, 2.0D) + Math.pow(playerSecondY - entitySecondY, 2.0D));
                if (approximateDistance < distance1)
                    approximateDistance = distance1;
                if (distance2 < distance3)
                    distance2 = distance3;
                if (approximateDistance < distance2)
                    approximateDistance = distance2;
                if (reachPing != -1)
                    approximateDistance -= reachPing / 200D + 0.5D;
                GameMode gameMode = player.getGameMode();
                if (approximateDistance >= 4.05D && Math.abs(location1.getY() - entityY) < 2.5D &&
                        !noTimeout && (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE))
                    Violations.interactViolation(player, CheckTypes.REACH_A_2, lacPlayer);

            }
        }

        lacPlayer.lastHitTime = time;

    }


}
