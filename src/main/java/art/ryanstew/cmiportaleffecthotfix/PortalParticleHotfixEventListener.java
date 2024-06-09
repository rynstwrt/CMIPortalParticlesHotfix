package art.ryanstew.cmiportaleffecthotfix;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class PortalParticleHotfixEventListener implements Listener
{
    private final CMIPortalParticleHotfix plugin;


    public PortalParticleHotfixEventListener(CMIPortalParticleHotfix plugin)
    {
        this.plugin = plugin;
    }


    @EventHandler
    private void onPlayerMessage(PlayerCommandPreprocessEvent event)
    {
        if (event.getMessage().toLowerCase().trim().equalsIgnoreCase("/cmi reload"))
        {
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    plugin.fixPortals();
                }
            }.runTaskLaterAsynchronously(plugin, plugin.getConfig().getLong("player-reload-fix-delay"));
        }
    }


    @EventHandler
    private void onConsoleCommand(ServerCommandEvent event)
    {
        if (event.getCommand().toLowerCase().trim().equalsIgnoreCase("cmi reload"))
            plugin.fixPortals();
    }
}
