package art.ryanstew.cmiportalparticlehotfix;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Portals.CMIPortal;
import com.Zrips.CMI.Modules.Portals.PortalManager;
import net.Zrips.CMILib.Effects.CMIEffect;
import net.Zrips.CMILib.Effects.CMIEffectManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;


public final class CMIPortalParticleHotfix extends JavaPlugin
{
    private File portalsConfigFile;
    private FileConfiguration portalsConfig;


    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        setupPortalConfig();

        writePortalsInfoToPortalsConfig();

        if (getConfig().getBoolean("fix-on-start"))
            fixPortals();

        getServer().getPluginManager().registerEvents(new PortalParticleHotfixEventListener(this), this);

        PluginCommand command = Objects.requireNonNull(getCommand("cmiportalparticlehotfix"));
        command.setExecutor(new FixPortalCommand(this));
        command.setTabCompleter(new FixPortalTabCompleter());
    }


    @Override
    public void onDisable() { }


    @SuppressWarnings("deprecation")
    public void sendFormattedMessage(CommandSender sender, String message, boolean prefixed)
    {
        if (prefixed)
            message = String.format("%s %s", getConfig().getString("prefix"), message);

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }


    private void setupPortalConfig()
    {
        portalsConfigFile = new File(getDataFolder(), "portals.yml");
        if (!portalsConfigFile.exists())
        {
            //noinspection ResultOfMethodCallIgnored
            portalsConfigFile.getParentFile().mkdirs();
            saveResource("portals.yml", false);
        }

        portalsConfig = YamlConfiguration.loadConfiguration(portalsConfigFile);
    }


    private void savePortalsConfig()
    {
        try
        {
            portalsConfig.save(portalsConfigFile);
        }
        catch (IOException err)
        {
            getLogger().severe("CMIPORTALPARTICLEHOTFIX FAILED TO SAVE PORTALS CONFIG");
        }
    }


    public void reloadAllConfigs()
    {
        reloadConfig();

        portalsConfig = YamlConfiguration.loadConfiguration(portalsConfigFile);
    }


    private void writePortalsInfoToPortalsConfig()
    {
        PortalManager portalManager = CMI.getInstance().getPortalManager();
        SortedMap<String, CMIPortal> portals = portalManager.getPortals();

        ConfigurationSection portalSection = portalsConfig.getConfigurationSection("portals");

        for (Map.Entry<String, CMIPortal> entry : portals.entrySet())
        {
            String portalName = entry.getKey();
            CMIPortal portal = entry.getValue();

            if (portalSection != null && portalSection.getKeys(false).contains(portalName))
                continue;

            CMIEffect effect = portal.getCMIEffect();
            int particleAmount = effect.getAmount();
            CMIEffectManager.CMIParticle particle = effect.getParticle();

            portalsConfig.set(String.format("portals.%s.particle", portalName), particle.getName());
            portalsConfig.set(String.format("portals.%s.amount", portalName), particleAmount);
        }

        savePortalsConfig();
    }


    private CMIEffectManager.CMIParticle getParticleFromPortalName(String portalName)
    {
        String particleName = portalsConfig.getString(String.format("portals.%s.particle", portalName));
        if (particleName == null)
            return null;

        particleName = particleName.toLowerCase().replaceAll(" ", "_");
        return CMIEffectManager.CMIParticle.getCMIParticle(particleName);
    }


    private int getParticleAmountFromPortalName(String portalName)
    {
        return portalsConfig.getInt(String.format("portals.%s.amount", portalName));
    }


    public int fixPortals()
    {
        ConfigurationSection portalsSection = portalsConfig.getConfigurationSection("portals");
        if (portalsSection == null || portalsSection.getKeys(false).isEmpty())
            return -1;

        Set<String> portalNames = portalsSection.getKeys(false);
        PortalManager portalManager = CMI.getInstance().getPortalManager();
        SortedMap<String, CMIPortal> portals = portalManager.getPortals();

        int numFixedPortals = 0;
        for (Map.Entry<String, CMIPortal> entry : portals.entrySet())
        {
            String portalName = entry.getKey();
            if (!portalNames.contains(portalName))
                continue;

            CMIEffectManager.CMIParticle particle = getParticleFromPortalName(portalName);
            if (particle == null)
                continue;

            int particleAmount = getParticleAmountFromPortalName(portalName);

            CMIPortal portal = entry.getValue();
            CMIEffect effect = portal.getCMIEffect();
            effect.setParticle(particle);
            effect.setAmount(particleAmount);

            portal.setCMIEffect(effect);

            ++numFixedPortals;
        }

        return numFixedPortals;
    }
}
