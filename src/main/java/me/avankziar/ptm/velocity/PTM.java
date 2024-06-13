package main.java.me.avankziar.ptm.velocity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelRegistrar;

import main.java.me.avankziar.ptm.velocity.assistant.StaticValues;
import main.java.me.avankziar.ptm.velocity.listener.PluginMessageListener;

@Plugin(id = "ptm", name = "ProxyTeleportManager", version = "0-0-1",
		url = "https://example.org", description = "Teleportsystem for Velocity and maybe more...", authors = {"Avankziar"})
public class PTM
{
	private static PTM plugin;
    private final ProxyServer server;
    public static Logger logger = null;
    
    @Inject
    public PTM(ProxyServer server, Logger logger) 
    {
    	PTM.plugin = this;
        this.server = server;
        PTM.logger = logger;

        PluginDescription pd = server.getPluginManager().getPlugin("ptm").get().getDescription();
        List<String> dependencies = new ArrayList<>();
        pd.getDependencies().stream().allMatch(x -> dependencies.add(x.toString()));
        //https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=PTM
		logger.info(" ██████╗ ████████╗███╗   ███╗ | Id: "+pd.getId());//plugin.getDescription().getVersion());
		logger.info(" ██╔══██╗╚══██╔══╝████╗ ████║ | Author: ["+String.join(" ", pd.getAuthors())+"]");
		logger.info(" ██████╔╝   ██║   ██╔████╔██║ | Plugin Website: "+pd.getUrl().toString());
		logger.info(" ██╔═══╝    ██║   ██║╚██╔╝██║ | Dependencies Plugins: ["+String.join(" ", dependencies)+"]");
		logger.info(" ██║        ██║   ██║ ╚═╝ ██║ | Version: "+pd.getVersion().get());
		logger.info(" ╚═╝        ╚═╝   ╚═╝     ╚═╝ | Have Fun^^");
    }
    
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) 
    {
    	//server.getEventManager().register(this, this); INFO Die MainKlasse ist immer in Velocity schon registriert
    	server.getEventManager().register(plugin, new PluginMessageListener(plugin));
    	ChannelRegistrar cr = server.getChannelRegistrar();
        cr.register(StaticValues.BACK_TOBUNGEE);
        cr.register(StaticValues.BACK_TOSPIGOT);
        cr.register(StaticValues.HOME_TOBUNGEE);
        cr.register(StaticValues.HOME_TOSPIGOT);
    }
    
    public static PTM getPlugin()
    {
    	return PTM.plugin;
    }
    
    public ProxyServer getServer()
    {
    	return server;
    }
}
