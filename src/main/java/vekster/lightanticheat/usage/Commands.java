package vekster.lightanticheat.usage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import vekster.lightanticheat.LightAntiCheat;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(Config.unknownCommand);
            return true;
        }

        if (sender.hasPermission("lightanticheat.reload")) {
            LightAntiCheat.getInstance().reloadConfig();
            Config.getValuesFromConfig();
            sender.sendMessage(Config.reloadCommand);
        } else {
            sender.sendMessage(Config.unresolvedCommand);
        }

        return true;
    }

}
