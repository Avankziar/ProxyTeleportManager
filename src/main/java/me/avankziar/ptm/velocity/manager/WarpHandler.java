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

public class WarpHandler
{
private PTM plugin;
	
	public WarpHandler(PTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void pluginMessage(PluginMessageEvent event) throws IOException
	{
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.WARP_PLAYERTOPOSITION))
        {
        	String uuid = in.readUTF();
        	String playerName = in.readUTF();
        	String warpName = in.readUTF();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	int delay = in.readInt();
        	String pterc = in.readUTF();
        	String ptegc = in.readUTF();
        	BackHandler.getBack(in, uuid, playerName, Mechanics.WARP);
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	WarpHandler wh = new WarpHandler(plugin);
        	wh.teleportPlayerToWarp(playerName, warpName, location, delay, pterc, ptegc);
        	return;
        }
        return;
	}
	
	public void teleportPlayerToWarp(String playerName, String warpName, ServerLocation location, int delayed,
			String pterc, String ptegc)
	{
		Player player = plugin.getServer().getPlayer(playerName).get();
		if(player == null)
		{
			return;
		}
		teleportPlayer(player, delayed, warpName, location, pterc, ptegc); //Back wurde schon gemacht
	}
	
	public void teleportPlayer(Player player, int delay, String warpName, final ServerLocation location, String pterc, String ptegc)
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
	        	out.writeUTF(StaticValues.WARP_PLAYERTOPOSITION);
				out.writeUTF(player.getUsername());
				out.writeUTF(warpName);
				out.writeUTF(location.getWorldName());
				out.writeDouble(location.getX());
				out.writeDouble(location.getY());
				out.writeDouble(location.getZ());
				out.writeFloat(location.getYaw());
				out.writeFloat(location.getPitch());
				out.writeUTF(pterc);
				out.writeUTF(ptegc);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        plugin.getServer().getServer(location.getServer()).get().sendPluginMessage(StaticValues.WARP_TOSPIGOT, streamout.toByteArray());
			return;
		}).delay(delay, TimeUnit.MILLISECONDS).schedule();
	}
}