package main.java.me.avankziar.ptm.velocity.listener;

import java.io.IOException;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import main.java.me.avankziar.ptm.velocity.PTM;
import main.java.me.avankziar.ptm.velocity.assistant.StaticValues;
import main.java.me.avankziar.ptm.velocity.manager.BackHandler;
import main.java.me.avankziar.ptm.velocity.manager.HomeHandler;
import main.java.me.avankziar.ptm.velocity.manager.PortalHandler;
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
        	if(event.getIdentifier() == StaticValues.BACK_TOBUNGEE) 
            {
            	new BackHandler(plugin).pluginMessage(event);
            	return;
            }
        	if (event.getIdentifier() == StaticValues.HOME_TOBUNGEE) 
            {
        		new HomeHandler(plugin).pluginMessage(event);
                return;
            }
        	if (event.getIdentifier() == StaticValues.PORTAL_TOBUNGEE) 
            {
        		new PortalHandler(plugin).pluginMessage(event);
        		return;
            }
        	if (event.getIdentifier() == StaticValues.TP_TOBUNGEE) 
            {
        		new TeleportHandler(plugin).pluginMessage(event);
        		return;
            }
        	if (event.getIdentifier() == StaticValues.WARP_TOBUNGEE) 
            {
        		new WarpHandler(plugin).pluginMessage(event);
        		return;
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