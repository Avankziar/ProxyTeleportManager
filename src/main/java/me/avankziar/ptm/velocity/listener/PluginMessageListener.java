package main.java.me.avankziar.ptm.velocity.listener;

import java.io.IOException;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import main.java.me.avankziar.ptm.velocity.PTM;
import main.java.me.avankziar.ptm.velocity.assistant.StaticValues;
import main.java.me.avankziar.ptm.velocity.manager.BackHandler;
import main.java.me.avankziar.ptm.velocity.manager.CustomHandler;
import main.java.me.avankziar.ptm.velocity.manager.EntityTransportHandler;
import main.java.me.avankziar.ptm.velocity.manager.FirstSpawnHandler;
import main.java.me.avankziar.ptm.velocity.manager.HomeHandler;
import main.java.me.avankziar.ptm.velocity.manager.IFHHandler;
import main.java.me.avankziar.ptm.velocity.manager.PortalHandler;
import main.java.me.avankziar.ptm.velocity.manager.RandomTeleportHandler;
import main.java.me.avankziar.ptm.velocity.manager.RespawnHandler;
import main.java.me.avankziar.ptm.velocity.manager.SafeHandler;
import main.java.me.avankziar.ptm.velocity.manager.SavePointHandler;
import main.java.me.avankziar.ptm.velocity.manager.TeleportHandler;
import main.java.me.avankziar.ptm.velocity.manager.WarpHandler;

public class PluginMessageListener
{
	private PTM plugin;
	
	public PluginMessageListener(PTM plugin)
	{
		this.plugin = plugin;
	}
	
	@Subscribe
    public void onPluginMessageFromBackend(PluginMessageEvent event) 
    {
        if (!(event.getSource() instanceof ServerConnection)) 
        {
            return;
        }
        //ServerConnection backend = (ServerConnection) event.getSource(); At the Moment not needed
        try
		{
        	switch(event.getIdentifier().getId())
        	{
        	case StaticValues.GENERAL_TOBUNGEE_: break;
        	case StaticValues.BACK_TOBUNGEE_:
        		new BackHandler(plugin).pluginMessage(event); return;
        	case StaticValues.CUSTOM_TOBUNGEE_:
        		new CustomHandler(plugin).pluginMessage(event);	return;
        	case StaticValues.ENTITYTRANSPORT_TOBUNGEE_:
        		new EntityTransportHandler(plugin).pluginMessage(event); return;
        	case StaticValues.FIRSTSPAWN_TOBUNGEE_:
        		new FirstSpawnHandler(plugin).pluginMessage(event); return;
        	case StaticValues.HOME_TOBUNGEE_:
        		new HomeHandler(plugin).pluginMessage(event); return;
        	case StaticValues.IFH_TOBUNGEE_:
        		new IFHHandler(plugin).pluginMessage(event); return;
        	case StaticValues.PORTAL_TOBUNGEE_:
        		new PortalHandler(plugin).pluginMessage(event); return;
        	case StaticValues.RANDOMTELEPORT_TOBUNGEE_:
        		new RandomTeleportHandler(plugin).pluginMessage(event); return;
        	case StaticValues.RESPAWN_TOBUNGEE_:
        		new RespawnHandler(plugin).pluginMessage(event); return;
        	case StaticValues.SAFE_TOBUNGEE_:
        		new SafeHandler(plugin).pluginMessage(event); return;
        	case StaticValues.SAVEPOINT_TOBUNGEE_:
        		new SavePointHandler(plugin).pluginMessage(event); return;
        	case StaticValues.TP_TOBUNGEE_:
        		new TeleportHandler(plugin).pluginMessage(event); return;
        	case StaticValues.WARP_TOBUNGEE_:
        		new WarpHandler(plugin).pluginMessage(event); return;
        	}
		} catch (IOException e)
		{
			e.printStackTrace();
		}   
    }
	
	public static boolean containsServer(String server)
	{
		for(RegisteredServer rs : PTM.getPlugin().getServer().getAllServers())
		{
			if(rs.getServerInfo().getName().equals(server))
			{
				return true;
			}
		}
		return false;
	}
}