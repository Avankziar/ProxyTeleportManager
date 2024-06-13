package main.java.me.avankziar.ptm.velocity.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import main.java.me.avankziar.ptm.velocity.PTM;
import main.java.me.avankziar.ptm.velocity.assistant.StaticValues;
import main.java.me.avankziar.ptm.velocity.listener.PluginMessageListener;
import main.java.me.avankziar.ptm.velocity.objects.Back;
import main.java.me.avankziar.ptm.velocity.objects.ForbiddenHandlerBungee;
import main.java.me.avankziar.ptm.velocity.objects.Mechanics;
import main.java.me.avankziar.ptm.velocity.objects.ServerLocation;
import net.kyori.adventure.text.Component;

public class BackHandler
{
	private PTM plugin;
	private ProxyServer server;
	private static HashMap<String,Back> backLocations = new HashMap<>(); //Playername welche anfragt
	private static HashMap<String,Back> deathBackLocations = new HashMap<>();
	public static ArrayList<String> pendingNewBackRequests = new ArrayList<>();
	
	public BackHandler(PTM plugin)
	{
		this.plugin = plugin;
		server = plugin.getServer();
	}
	
	private boolean deleteDeathBack = true; //FIXME: Wie es im original BTM machen
	public void pluginMessage(PluginMessageEvent event) throws IOException
	{
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        if(task.equals(StaticValues.BACK_SENDJOINOBJECT))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	Back back = BackHandler.getTaskBack(in, uuid, name);
        	if(ForbiddenHandlerBungee.isForbidden(back, name, Mechanics.BACK, false))
    		{
        		return;
    		}
        	if(!BackHandler.getBackLocations().containsKey(name))
        	{
        		BackHandler.getBackLocations().put(name, back);
        	}
        	return;
        } else if(task.equals(StaticValues.BACK_SENDOBJECT))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	Back back = BackHandler.getTaskBack(in, uuid, name);
        	if(ForbiddenHandlerBungee.isForbidden(back, name, Mechanics.BACK, false))
    		{
        		return;
    		}
        	if(!BackHandler.getBackLocations().containsKey(name))
        	{
        		BackHandler.getBackLocations().put(name, back);
        	} else
        	{
        		BackHandler.getBackLocations().replace(name, back);
        	}
			if(BackHandler.getPendingNewBackRequests().contains(name))
        	{
        		BackHandler.getPendingNewBackRequests().remove(name);
        	}
        	return;
        } else if(task.equals(StaticValues.BACK_SENDPLAYERBACK))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	Back back = BackHandler.getTaskBack(in, uuid, name);
        	int delayed = in.readInt();
        	String oldbacknull = in.readUTF();
        	final Back oldback = BackHandler.getBackLocations().get(name);
        	if(oldback == null)
        	{
        		Player player = server.getPlayer(UUID.fromString(uuid)).get();
        		if(player != null)
        		{
        			player.sendMessage(Component.text(oldbacknull));
        		}
        		//Sorgt dafür, dass das Back nicht überschrieben, falls der Spieler in einer Forbidden Welt oder Server ist.
            	if(!ForbiddenHandlerBungee.isForbidden(back, name, Mechanics.BACK, false))
        		{
            		BackHandler.getBackLocations().replace(name, back);
        		}
        		return;
        	}
        	String oldserver = oldback.getLocation().getServer();
        	String oldworld = oldback.getLocation().getWorldName();
        	double oldx = oldback.getLocation().getX();
        	double oldy = oldback.getLocation().getY();
        	double oldz = oldback.getLocation().getZ();
        	float oldyaw = oldback.getLocation().getYaw();
        	float oldpitch = oldback.getLocation().getPitch();
        	//Sorgt dafür, dass das Back nicht überschrieben, falls der Spieler in einer Forbidden Welt oder Server ist.
        	if(!ForbiddenHandlerBungee.isForbidden(back, name, Mechanics.BACK, false))
    		{
        		if(!BackHandler.getBackLocations().containsKey(name))
            	{
            		BackHandler.getBackLocations().put(name, back);
            	} else
            	{
            		BackHandler.getBackLocations().replace(name, back);
            	}
    		}
        	BackHandler bh = new BackHandler(plugin);
        	bh.teleportBack(oldserver, name, oldworld, oldx, oldy, oldz, oldyaw, oldpitch, deleteDeathBack, delayed, false);
        } else if(task.equals(StaticValues.BACK_SENDDEATHOBJECT))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	Back back = BackHandler.getTaskBack(in, uuid, name);
        	if(ForbiddenHandlerBungee.isForbidden(back, name, Mechanics.DEATHBACK, true))
    		{
    			return;
    		}
        	if(!BackHandler.getDeathBackLocations().containsKey(name))
        	{
        		BackHandler.getDeathBackLocations().put(name, back);
        	} else
        	{
        		BackHandler.getDeathBackLocations().replace(name, back);
        	}
        	return;
        } else if(task.equals(StaticValues.BACK_SENDPLAYERDEATHBACK))
        {
        	String uuid = in.readUTF();
        	String name = in.readUTF();
        	Player player = server.getPlayer(name).get();
        	if(!BackHandler.getDeathBackLocations().containsKey(name))
        	{
        		if(player == null)
            	{
            		return;
            	}
        		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
    	        DataOutputStream out = new DataOutputStream(streamout);
    	        try {
    				out.writeUTF(StaticValues.BACK_NODEATHBACK);
    				out.writeUTF(name);
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    	        player.getCurrentServer().get().getServer().sendPluginMessage(StaticValues.BACK_TOSPIGOT, streamout.toByteArray());
    		    return;
        	}
        	Back olddeathback = BackHandler.getDeathBackLocations().get(name);
        	String oldserver = olddeathback.getLocation().getServer();
        	String oldworld = olddeathback.getLocation().getWorldName();
        	double oldx = olddeathback.getLocation().getX();
        	double oldy = olddeathback.getLocation().getY();
        	double oldz = olddeathback.getLocation().getZ();
        	float oldyaw = olddeathback.getLocation().getYaw();
        	float oldpitch = olddeathback.getLocation().getPitch();
        	Back back = BackHandler.getTaskBack(in, uuid, name);
        	int delayed = in.readInt();
			if(!BackHandler.getBackLocations().containsKey(name))
        	{
				BackHandler.getBackLocations().put(name, back);
        	} else
        	{
        		BackHandler.getBackLocations().replace(name, back);
        	}
        	BackHandler bh = new BackHandler(plugin);
        	bh.teleportBack(oldserver, name, oldworld, oldx, oldy, oldz, oldyaw, oldpitch, deleteDeathBack, delayed, true);
        }
	}
	
	public static HashMap<String,Back> getBackLocations()
	{
		return backLocations;
	}
	
	public static HashMap<String,Back> getDeathBackLocations()
	{
		return deathBackLocations;
	}
	
	public static ArrayList<String> getPendingNewBackRequests()
	{
		return pendingNewBackRequests;
	}
	
	public static void requestNewBack(Player player)
	{
		if(!pendingNewBackRequests.contains(player.getUsername()))
        {
        	pendingNewBackRequests.add(player.getUsername());
        }
		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(streamout);
        try {
			out.writeUTF(StaticValues.BACK_REQUESTNEWBACK);
			out.writeUTF(player.getUsername());
		} catch (IOException e) {
			e.printStackTrace();
		}
	    player.getCurrentServer().get().getServer().sendPluginMessage(StaticValues.BACK_TOSPIGOT, streamout.toByteArray());
	}

	public static void getBack(DataInputStream in, String uuid, String name, Mechanics mechanics) throws IOException
	{
		Back back = getTaskBack(in, uuid, name);
    	updateBack(back, name, mechanics);
	}
	
	public static void getBackWithChoosing(DataInputStream in, String uuid, String name, Mechanics mechanics, boolean overrideBack) throws IOException
	{
		if(overrideBack)
		{
			Back back = getTaskBack(in, uuid, name);
	    	updateBack(back, name, mechanics);
		}
	}
	
	public static void updateBack(Back back, String playername, Mechanics mechanics)
	{
		if(ForbiddenHandlerBungee.isForbidden(back, playername, mechanics, false))
		{
			return;
		}
		if(!BackHandler.getBackLocations().containsKey(playername))
    	{
    		BackHandler.getBackLocations().put(playername, back);
    	} else
    	{
    		BackHandler.getBackLocations().replace(playername, back);
    	}
		if(BackHandler.getPendingNewBackRequests().contains(playername))
    	{
    		BackHandler.getPendingNewBackRequests().remove(playername);
    	}
	}
	
	public static Back getTaskBack(DataInputStream in, String uuid, String name) throws IOException
	{
		String serverName = in.readUTF();
    	String worldName = in.readUTF();
    	double x = in.readDouble();
    	double y = in.readDouble();
    	double z = in.readDouble();
    	float yaw = in.readFloat();
    	float pitch = in.readFloat();
    	boolean toggle = in.readBoolean();
    	return new Back(UUID.fromString(uuid), name, new ServerLocation(serverName, worldName, x, y, z, yaw, pitch), toggle, "");
	}
	
	public void teleportBack(String oldserver, String name, String oldworld,
			double oldx, double oldy, double oldz, float oldyaw, float oldpitch, boolean deleteDeathBack, int delay, boolean isDeathback)
	{
		Player player = plugin.getServer().getPlayer(name).get();
    	if(player == null)
    	{
    		return;
    	}
    	if(!PluginMessageListener.containsServer(oldserver))
		{
			player.sendMessage(Component.text("Server is unknow!"));
			return;
		}
    	int delayHalf = delay/2;
    	plugin.getServer().getScheduler().buildTask(plugin, () ->
		{
			if(player == null || oldserver == null)
			{
				return;
			}
			if(player.getCurrentServer() == null || player.getCurrentServer().get().getServerInfo() == null 
					|| player.getCurrentServer().get().getServerInfo().getName() == null)
			{
				return;
			}
			if(!player.getCurrentServer().get().getServerInfo().getName().equals(oldserver))
			{
				player.createConnectionRequest(plugin.getServer().getServer(oldserver).get());
			}
			AtomicInteger integer = new AtomicInteger(0);
			plugin.getServer().getScheduler().buildTask(plugin, (selftask) ->
			{
				if(player == null || oldserver == null)
				{
					selftask.cancel();
					return;
				}
				if(player.getCurrentServer() == null || player.getCurrentServer().get().getServerInfo() == null 
						|| player.getCurrentServer().get().getServerInfo().getName() == null)
				{
					selftask.cancel();
					return;
				}
				ByteArrayOutputStream streamout = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(streamout);
		        try {
		        	if(!isDeathback)
		        	{
		        		out.writeUTF(StaticValues.BACK_SENDPLAYERBACK);
		        	} else
		        	{
		        		out.writeUTF(StaticValues.BACK_SENDPLAYERDEATHBACK);
		        	}
					out.writeUTF(name);
					out.writeUTF(oldworld);
					out.writeDouble(oldx);
					out.writeDouble(oldy);
					out.writeDouble(oldz);
					out.writeFloat(oldyaw);
					out.writeFloat(oldpitch);
				} catch (IOException e) {
					e.printStackTrace();
				}
		        
		        plugin.getServer().getServer(oldserver).get().sendPluginMessage(StaticValues.BACK_TOSPIGOT, streamout.toByteArray());
			    if(isDeathback && deleteDeathBack)
			    {
			    	BackHandler.getDeathBackLocations().remove(name);
			    }
			    selftask.cancel();
				if(integer.addAndGet(1) >= 100)
				{
					selftask.cancel();
				    return;
				}
			}).repeat(5, TimeUnit.MILLISECONDS).schedule();
		}).delay(delayHalf, TimeUnit.MILLISECONDS).schedule();
	}
}
