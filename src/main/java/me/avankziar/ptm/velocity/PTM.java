package main.java.me.avankziar.ptm.velocity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelRegistrar;

import main.java.me.avankziar.ptm.velocity.assistant.StaticValues;
import main.java.me.avankziar.ptm.velocity.listener.PluginMessageListener;

@Plugin(id = "ptm", name = "ProxyTeleportManager", version = "0-0-6",
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
    }
    
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) 
    {
    	//server.getEventManager().register(this, this); INFO Die MainKlasse ist immer in Velocity schon registriert
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
    
    @Subscribe
    public void onJoin(PlayerChooseInitialServerEvent event)
    {
    	//Join
    	//event.getPlayer().sendMessage(ChatApi.tl("&eDas ist das &4alte &#ff6c0aBukkit &rFormat."));
    	//event.getPlayer().sendMessage(ChatApi.tl("<yellow>Das ist das </yellow><color:#b20000>neue</color> <color:#ff6c0a>Bukkit</color> <reset>Format."));
    	//event.getPlayer().sendMessage(ChatApi.generateOldTextComponentFormat(
    			//"&aAkzeptieren+✔~click@SUGGEST_COMMAND@/warp+Test1~hover@SHOW_TEXT@&aKlicke+hier+um+die+Teleportanfrage+anzunehmen. &f| &cAblehnen+✖~click@SUGGEST_COMMAND@/home+H1~hover@SHOW_TEXT@&cKlicke+hier+um+die+Teleportanfrage+abzulehnen!"));
    	//event.getPlayer().sendMessage(ChatApi.generateOldTextComponentFormat(
    			//"&aTest+nur+mit+click~click@SUGGEST_COMMAND@/warp+Test1"));
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