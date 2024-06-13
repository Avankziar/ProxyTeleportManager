package main.java.me.avankziar.ptm.velocity.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.ptm.velocity.PTM;
import main.java.me.avankziar.ptm.velocity.assistant.StaticValues;
import main.java.me.avankziar.ptm.velocity.listener.PluginMessageListener;
import main.java.me.avankziar.ptm.velocity.objects.Mechanics;
import main.java.me.avankziar.ptm.velocity.objects.ServerLocation;
import net.kyori.adventure.text.Component;

public class HomeHandler
{
	private PTM plugin;
	
	public HomeHandler(PTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void pluginMessage(PluginMessageEvent event) throws IOException
	{
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        
        if(task.equals(StaticValues.HOME_PLAYERTOPOSITION))
        {
        	String uuid = in.readUTF();
        	String playerName = in.readUTF();
        	String homeName = in.readUTF();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	float yaw = in.readFloat();
        	float pitch = in.readFloat();
        	int delayed = in.readInt();
        	BackHandler.getBack(in, uuid, playerName, Mechanics.HOME);
        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
        	HomeHandler hh = new HomeHandler(plugin);	
        	hh.teleportPlayerToHome(playerName, uuid, location, homeName, delayed);
        	return;
        }
        return;
	}
	
	public void teleportPlayerToHome(String playerName, String uuid, ServerLocation location, String homeName, int delay)
	{
		Player player = plugin.getServer().getPlayer(playerName).get();
		if(player == null || location == null)
		{
			return;
		}		
		teleportPlayer(player, delay, location, homeName); //Back wurde schon gemacht.
	}
	
	public void teleportPlayer(Player player, int delay, ServerLocation location, String homeName)
	{
		if(player == null || location == null)
		{
			return;
		}
		if(!PluginMessageListener.containsServer(location.getServer()))
		{
			player.sendMessage(Component.text("Server is unknow!"));
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
				player.createConnectionRequest(plugin.getServer().getServer(location.getServer()).get());
			}
			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(streamout);
	        try {
	        	out.writeUTF(StaticValues.HOME_PLAYERTOPOSITION);
				out.writeUTF(player.getUsername());
				out.writeUTF(homeName);
				out.writeUTF(location.getWorldName());
				out.writeDouble(location.getX());
				out.writeDouble(location.getY());
				out.writeDouble(location.getZ());
				out.writeFloat(location.getYaw());
				out.writeFloat(location.getPitch());
			} catch (IOException e) {
				e.printStackTrace();
			}
	        plugin.getServer().getServer(location.getServer()).get().sendPluginMessage(StaticValues.HOME_TOSPIGOT, streamout.toByteArray());
	        return;
		}).delay(delay, TimeUnit.MILLISECONDS).schedule();
	}
}