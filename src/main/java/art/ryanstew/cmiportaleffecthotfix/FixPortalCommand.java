package art.ryanstew.cmiportaleffecthotfix;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public class FixPortalCommand implements CommandExecutor
{
    private CMIPortalParticleHotfix plugin;


    public FixPortalCommand(CMIPortalParticleHotfix plugin)
    {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args)
    {
        if (args.length == 0 || args[0].equalsIgnoreCase("help"))
        {
            plugin.sendFormattedMessage(sender, "&7Usage: &a/fixportals <help/fix/reload>", true);
            return true;
        }

        if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("reload"))
            {
                plugin.reloadAllConfigs();
                plugin.fixPortals();
                plugin.sendFormattedMessage(sender, "&aReloaded all configs!", true);
                return true;
            }

            if (args[0].equalsIgnoreCase("fix"))
            {
                int numFixedPortals = plugin.fixPortals();

                if (numFixedPortals == -1)
                {
                    plugin.sendFormattedMessage(sender, "&cNo portals were found!", true);
                    return true;
                }
                else if (numFixedPortals == 0)
                {
                    plugin.sendFormattedMessage(sender, "&cNo portals could be fixed! (make sure portals.yml is configured correctly)", true);
                    return true;
                }

                plugin.sendFormattedMessage(sender, String.format("&aFixed %d portals!", numFixedPortals), true);
                return true;
            }
        }

        plugin.sendFormattedMessage(sender, "&7Usage: &a/fixportals <help/fix/reload>", true);
        return true;
    }
}
