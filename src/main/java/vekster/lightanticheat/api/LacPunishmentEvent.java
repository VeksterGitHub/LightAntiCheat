package vekster.lightanticheat.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import vekster.lightanticheat.extra.CheckTypes;
import vekster.lightanticheat.usage.Log;

public class LacPunishmentEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final String checkType;
    private boolean cancelled;

    public LacPunishmentEvent(Player player, CheckTypes checkType) {
        this.player = player;
        this.checkType = Log.checkTypeToString(checkType);
    }

    public Player getPlayer() {
        return player;
    }

    public String getCheckType() {
        return checkType;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
