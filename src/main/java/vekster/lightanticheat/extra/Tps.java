package vekster.lightanticheat.extra;

import org.bukkit.Bukkit;
import vekster.lightanticheat.LightAntiCheat;

public class Tps {

    public static double tps = 20.0D;
    private static long lastTime = 0;

    public static void enableTpsCalculation() {
        Bukkit.getScheduler().runTaskTimer(LightAntiCheat.getInstance(), () -> {
            long time = System.currentTimeMillis();
            if (lastTime == 0) {
                lastTime = time;
                return;
            }
            Tps.tps = 45000.0D / (time - Tps.lastTime) * 20.0D;
            if (Tps.tps > 20.0D)
                Tps.tps = 20.0D;
            Tps.lastTime = time;
        }, 300, 900);
    }

}
