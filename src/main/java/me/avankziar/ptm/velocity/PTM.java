package main.java.me.avankziar.ptm.velocity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelRegistrar;

import main.java.me.avankziar.ptm.velocity.assistant.StaticValues;
import main.java.me.avankziar.ptm.velocity.database.YamlHandler;
import main.java.me.avankziar.ptm.velocity.database.YamlManager;
import main.java.me.avankziar.ptm.velocity.listener.PluginMessageListener;

@Plugin(id = "avankziar-proxyteleportmanager", name = "ProxyTeleportManager", version = "0-0-7",
		url = "https://example.org", description = "Teleportsystem for Velocity and maybe more...", authors = {"Avankziar"})
public class PTM
{
	private static PTM plugin;
    private final ProxyServer server;
    public Logger logger = null;
    private Path dataDirectory;
    private YamlHandler yamlHandler;
    private YamlManager yamlManager;
    
    @Inject
    public PTM(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) 
    {
    	PTM.plugin = this;
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }
    
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) 
    {
    	logger = Logger.getLogger("PTM");
    	PluginDescription pd = server.getPluginManager().getPlugin("avankziar-proxyteleportmanager").get().getDescription();
        List<String> dependencies = new ArrayList<>();
        pd.getDependencies().stream().allMatch(x -> dependencies.add(x.toString()));
        //https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=PTM
		logger.info(" ██████╗ ████████╗███╗   ███╗ | Id: "+pd.getId());
		logger.info(" ██╔══██╗╚══██╔══╝████╗ ████║ | Version: "+pd.getVersion().get());
		logger.info(" ██████╔╝   ██║   ██╔████╔██║ | Author: ["+String.join(", ", pd.getAuthors())+"]");
		logger.info(" ██╔═══╝    ██║   ██║╚██╔╝██║ | Dependencies Plugins: ["+String.join(", ", dependencies)+"]");
		logger.info(" ██║        ██║   ██║ ╚═╝ ██║ | Plugin Website:"+pd.getUrl().toString());
		logger.info(" ╚═╝        ╚═╝   ╚═╝     ╚═╝ | Have Fun^^");
        
		registerChannels();
        yamlHandler = new YamlHandler(plugin);
    }
    
    public static PTM getPlugin()
    {
    	return PTM.plugin;
    }
    
    public ProxyServer getServer()
    {
    	return server;
    }
    
    public Path getDataDirectory()
    {
    	return dataDirectory;
    }
    
    public YamlHandler getYamlHandler()
    {
    	return yamlHandler;
    }
    
    public YamlManager getYamlManager()
    {
    	return yamlManager;
    }
    
    public void setYamlManager(YamlManager yamlManager)
    {
    	this.yamlManager = yamlManager;
    }
    
    public void registerChannels()
    {
    	server.getEventManager().register(plugin, new PluginMessageListener(plugin));
    	ChannelRegistrar cr = server.getChannelRegistrar();
    	cr.register(StaticValues.GENERAL_TOBUNGEE);
        cr.register(StaticValues.GENERAL_TOSPIGOT);
        cr.register(StaticValues.BACK_TOBUNGEE);
        cr.register(StaticValues.BACK_TOSPIGOT);
        cr.register(StaticValues.CUSTOM_TOBUNGEE);
        cr.register(StaticValues.CUSTOM_TOSPIGOT);
        cr.register(StaticValues.ENTITYTRANSPORT_TOBUNGEE);
        cr.register(StaticValues.ENTITYTRANSPORT_TOSPIGOT);
        cr.register(StaticValues.FIRSTSPAWN_TOBUNGEE);
        cr.register(StaticValues.FIRSTSPAWN_TOSPIGOT);
        cr.register(StaticValues.HOME_TOBUNGEE);
        cr.register(StaticValues.HOME_TOSPIGOT);
        cr.register(StaticValues.IFH_TOBUNGEE);
        cr.register(StaticValues.IFH_TOSPIGOT);
        cr.register(StaticValues.PORTAL_TOBUNGEE);
        cr.register(StaticValues.PORTAL_TOSPIGOT);
        cr.register(StaticValues.RANDOMTELEPORT_TOBUNGEE);
        cr.register(StaticValues.RANDOMTELEPORT_TOSPIGOT);
        cr.register(StaticValues.RESPAWN_TOBUNGEE);
        cr.register(StaticValues.RESPAWN_TOSPIGOT);
        cr.register(StaticValues.SAFE_TOBUNGEE);
        cr.register(StaticValues.SAFE_TOSPIGOT);
        cr.register(StaticValues.SAVEPOINT_TOBUNGEE);
        cr.register(StaticValues.SAVEPOINT_TOSPIGOT);
        cr.register(StaticValues.TP_TOBUNGEE);
        cr.register(StaticValues.TP_TOSPIGOT);
        cr.register(StaticValues.WARP_TOBUNGEE);
        cr.register(StaticValues.WARP_TOSPIGOT);
    }
}