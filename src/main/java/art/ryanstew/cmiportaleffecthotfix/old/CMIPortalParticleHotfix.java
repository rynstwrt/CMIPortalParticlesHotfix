//package art.ryanstew.cmiportaleffecthotfix.old;
//
//import com.Zrips.CMI.CMI;
//import com.Zrips.CMI.Modules.Portals.CMIPortal;
//import com.Zrips.CMI.Modules.Portals.PortalManager;
//import net.Zrips.CMILib.Effects.CMIEffect;
//import net.Zrips.CMILib.Effects.CMIEffectManager;
//import org.bukkit.ChatColor;
//import org.bukkit.command.CommandSender;
//import org.bukkit.configuration.ConfigurationSection;
//import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.configuration.file.YamlConfiguration;
//import org.bukkit.plugin.java.JavaPlugin;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Set;
//import java.util.SortedMap;
//
//
//public final class CMIPortalParticleHotfix extends JavaPlugin
//{
//    private File portalsConfigFile;
//    private FileConfiguration portalsConfig;
//
//
//    @Override
//    public void onEnable()
//    {
//        saveDefaultConfig();
//        setupPortalConfig();
//
//        writePortalsInfoToPortalsConfig();
//
//        if (getConfig().getBoolean("fix-on-start"))
//            fixPortals();
//
//        Objects.requireNonNull(getCommand("cmiportalparticlehotfix")).setExecutor(new FixPortalCommand(this));
//    }
//
//
//    @Override
//    public void onDisable()
//    {
//    }
//
//
//    public void sendFormattedMessage(CommandSender sender, String message, boolean prefixed)
//    {
//        if (prefixed)
//            message = String.format("%s %s", getConfig().getString("prefix"), message);
//
//        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
//    }
//
//
//    private void setupPortalConfig()
//    {
//        portalsConfigFile = new File(getDataFolder(), "portals.yml");
//        if (!portalsConfigFile.exists())
//        {
//            portalsConfigFile.getParentFile().mkdirs();
//            saveResource("portals.yml", false);
//        }
//
//        portalsConfig = YamlConfiguration.loadConfiguration(portalsConfigFile);
//    }
//
//
//    public FileConfiguration getPortalsConfig()
//    {
//        return portalsConfig;
//    }
//
//
//    private void savePortalsConfig()
//    {
//        try
//        {
//            portalsConfig.save(portalsConfigFile);
//        } catch (IOException err)
//        {
//            getLogger().severe("CMIPORTALPARTICLEHOTFIX FAILED TO SAVE PORTALS CONFIG");
//        }
//    }
//
//
//    public void reloadAllConfigs()
//    {
//        reloadConfig();
//
//        portalsConfig = YamlConfiguration.loadConfiguration(portalsConfigFile);
//    }
//
//
//    private void writePortalsInfoToPortalsConfig()
//    {
//        PortalManager portalManager = CMI.getInstance().getPortalManager();
//        SortedMap<String, CMIPortal> portals = portalManager.getPortals();
//
//        ConfigurationSection portalSection = portalsConfig.getConfigurationSection("portals");
//
//        for (Map.Entry<String, CMIPortal> entry : portals.entrySet())
//        {
//            String portalName = entry.getKey();
//            CMIPortal portal = entry.getValue();
//
//            if (portalSection != null && portalSection.getKeys(false).contains(portalName))
//                continue;
//
//            CMIEffect effect = portal.getCMIEffect();
//            int particleAmount = effect.getAmount();
//            CMIEffectManager.CMIParticle particle = effect.getParticle();
//
//            portalsConfig.set(String.format("portals.%s.particle", portalName), particle.getName());
//            portalsConfig.set(String.format("portals.%s.amount", portalName), particleAmount);
//        }
//
//        savePortalsConfig();
//    }
//
//
//    public CMIEffectManager.CMIParticle getParticleFromPortalName(String portalName)
//    {
//        String particleName = portalsConfig.getString(String.format("portals.%s.particle", portalName));
//        return CMIEffectManager.CMIParticle.getCMIParticle(particleName);
//    }
//
//
//    public int getParticleAmountFromPortalName(String portalName)
//    {
//        return portalsConfig.getInt(String.format("portals.%s.amount", portalName));
//    }
//
//
//    public void fixPortals()
//    {
//        File portalsFile = new File(Objects.requireNonNull(getServer().getPluginManager().getPlugin("CMI")).getDataFolder(), "Saves/Portals.yml");
//        FileConfiguration portalsConfig = YamlConfiguration.loadConfiguration(portalsFile);
//
//        Set<String> worldNames = portalsConfig.getKeys(false);
//
//        for (String worldName : worldNames)
//        {
//            ConfigurationSection worldConfigSection = portalsConfig.getConfigurationSection(worldName);
//            if (worldConfigSection == null)
//                continue;
//
//            Set<String> portalNames = worldConfigSection.getKeys(false);
//            for (String portalName : portalNames)
//            {
//                String particleName = worldConfigSection.getString(String.format("%s.effect", portalName));
//                if (particleName == null)
//                {
//                    getLogger().severe("CMIPortalParticleHotfix could find the particle for portal " + portalName + " in CMI/Saves/Portals.yml!");
//                    return;
//                }
//
//                particleName = particleName.toLowerCase().replaceAll(" ", "_");
//
//                CMIEffectManager.CMIParticle particle = CMIEffectManager.CMIParticle.getCMIParticle(particleName);
//                if (particle == null)
//                {
//                    getLogger().severe("CMIPortalParticleHotfix could not load particle \"" + particleName + "\"");
//                    return;
//                }
//
//                int particleAmount = worldConfigSection.getInt(String.format("%s.particleAmount", portalName));
//
//                CMIPortal portal = CMI.getInstance().getPortalManager().getByName(portalName);
//
//                getServer().broadcastMessage(particle.getName() + particleAmount);
//
//                CMIEffect cmiEffect = portal.getCMIEffect();
//                cmiEffect.setParticle(particle);
//                cmiEffect.setAmount(particleAmount);
//
//                portal.setCMIEffect(cmiEffect);
//            }
//        }
//    }
//
//
////    public int fixPortals()
////    {
////        ConfigurationSection portalsSection = getPortalsConfig().getConfigurationSection("portals");
////        if (portalsSection == null || portalsSection.getKeys(false).isEmpty())
////            return -1;
////
////        Set<String> portalNames = portalsSection.getKeys(false);
////        PortalManager portalManager = CMI.getInstance().getPortalManager();
////        SortedMap<String, CMIPortal> portals = portalManager.getPortals();
////
////        int numFixedPortals = 0;
////        for (Map.Entry<String, CMIPortal> entry : portals.entrySet())
////        {
////            String portalName = entry.getKey();
////            if (!portalNames.contains(portalName))
////                continue;
////
////            CMIEffectManager.CMIParticle particle = getParticleFromPortalName(portalName);
////            int particleAmount = getParticleAmountFromPortalName(portalName);
////
////            CMIPortal portal = entry.getValue();
////            CMIEffect effect = portal.getCMIEffect();
////            effect.setParticle(particle);
////            effect.setAmount(particleAmount);
////
////            ++numFixedPortals;
////        }
////
////        return numFixedPortals;
////    }
//}
