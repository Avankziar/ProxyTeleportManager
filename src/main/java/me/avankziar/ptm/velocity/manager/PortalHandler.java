package main.java.me.avankziar.ptm.velocity.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import main.java.me.avankziar.ptm.velocity.PTM;
import main.java.me.avankziar.ptm.velocity.assistant.StaticValues;
import main.java.me.avankziar.ptm.velocity.listener.PluginMessageListener;
import main.java.me.avankziar.ptm.velocity.objects.Mechanics;
import main.java.me.avankziar.ptm.velocity.objects.ServerLocation;
import net.kyori.adventure.text.Component;

public class PortalHandler
{
	private PTM plugin;
	
	public PortalHandler(PTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void pluginMessage(PluginMessageEvent event) throws IOException
	{
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.PORTAL_PLAYERTOPOSITION))
        {
        	String uuid = in.readUTF();
        	String playerName = in.readUTF();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	String portalname = in.readUTF();
        	boolean lava = in.readBoolean();
        	String pterc = in.readUTF();
        	String ptegc = in.readUTF();
        	BackHandler.getBack(in, uuid, playerName, Mechanics.PORTAL);
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	new PortalHandler(plugin).teleportPlayerToDestination(playerName, location, portalname, lava, pterc, ptegc);
        	return;
        } else if(task.equals(StaticValues.PORTAL_UPDATE))
        {
        	int mysqlID = in.readInt();
        	String additional = in.readUTF();
        	new PortalHandler(plugin).sendUpdate(mysqlID, additional);
        }
        return;
	}
	
	public void teleportPlayerToDestination(String playerName, ServerLocation location, String portalname, boolean lava,
			String pterc, String ptegc)
	{
		Player player = plugin.getServer().getPlayer(playerName).get();
		if(player == null)
		{
			return;
		}
		teleportPlayer(player, location, portalname, lava, pterc, ptegc); //Back wurde schon gemacht
	}
	
	public void teleportPlayer(Player player, ServerLocation location, String portalname, boolean lava, String pterc, String ptegc)
	{
		if(player == null || location == null)
		{
			return;
		}
		if(!PluginMessageListener.containsServer(location.getServer()))
		{
			player.sendMessage(Component.text("Server %server% is unknown!".replace("%server%", location.getServer())));
			return;
		}
		plugin.getServer().getScheduler().buildTask(plugin, () ->
		{
			if(player == null || location == null)
			{
				return;
			}
			if(location.getServer() == null)
			{
				return;
			}
			if(!player.getCurrentServer().get().getServerInfo().getName().equals(location.getServer()))
			{
				Optional<RegisteredServer> server = plugin.getServer().getServer(location.getServer());
				server.ifPresent((target) -> player.createConnectionRequest(target).connect());
			}
			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(streamout);
	        try {
	        	out.writeUTF(StaticValues.PORTAL_PLAYERTOPOSITION);
				out.writeUTF(player.getUsername());
				out.writeUTF(location.getWorldName());
				out.writeDouble(location.getX());
				out.writeDouble(location.getY());
				out.writeDouble(location.getZ());
				out.writeFloat(location.getYaw());
				out.writeFloat(location.getPitch());
				out.writeUTF(portalname);
				out.writeBoolean(lava);
				out.writeUTF(pterc);
				out.writeUTF(ptegc);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        plugin.getServer().getServer(location.getServer()).get().sendPluginMessage(StaticValues.PORTAL_TOSPIGOT, streamout.toByteArray());
			return;
		}).delay(0, TimeUnit.MILLISECONDS).schedule();
	}
	
	public void sendUpdate(int mysqlID, String additional)
	{
		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(streamout);
        try {
        	out.writeUTF(StaticValues.PORTAL_UPDATE);
			out.writeInt(mysqlID);
			out.writeUTF(additional);
		} catch (IOException e) 
        {
			e.printStackTrace();
		}
        for(RegisteredServer si : plugin.getServer().getAllServers())
        {
        	si.sendPluginMessage(StaticValues.PORTAL_TOSPIGOT, streamout.toByteArray());
        }
	}
}