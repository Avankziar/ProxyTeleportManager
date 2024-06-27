package main.java.me.avankziar.ptm.velocity.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import main.java.me.avankziar.ptm.velocity.PTM;
import main.java.me.avankziar.ptm.velocity.assistant.ChatApi;
import main.java.me.avankziar.ptm.velocity.assistant.StaticValues;
import main.java.me.avankziar.ptm.velocity.listener.PluginMessageListener;
import main.java.me.avankziar.ptm.velocity.objects.ForbiddenHandlerBungee;
import main.java.me.avankziar.ptm.velocity.objects.Mechanics;
import main.java.me.avankziar.ptm.velocity.objects.ServerLocation;
import main.java.me.avankziar.ptm.velocity.objects.Teleport;

public class TeleportHandler
{
	private PTM plugin;
	private static HashMap<String,Teleport> pendingTeleports = new HashMap<>(); //Playername welche anfragt
	private static HashMap<String,String> playerWorld = new HashMap<>();
	
	public TeleportHandler(PTM plugin)
	{
		this.plugin = plugin;
	}
	
	public void pluginMessage(PluginMessageEvent event) throws IOException
	{
		 DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
	        String task = in.readUTF();
	        
	        if(task.equals(StaticValues.TP_EXISTPENDING))
	        {
	        	String uuid = in.readUTF();
	        	String playername = in.readUTF();
	        	String fromName = in.readUTF();
	        	String toName = in.readUTF();
	        	String type = in.readUTF();
	        	boolean bypass = in.readBoolean();
	        	String error = in.readUTF();
	        	BackHandler.getBack(in, uuid, playername, Mechanics.TPA);
	        	if(plugin.getServer().getPlayer(toName).isEmpty())
	        	{
	        		Optional<Player> opp = plugin.getServer().getPlayer(fromName);
	        		if(!opp.isEmpty())
	        		{
	        			opp.ifPresent(y -> y.sendMessage(ChatApi.text(error)));
	        		}
	        		return;
	        	}
	        	if(BackHandler.getBackLocations().get(toName) != null)
	        	{
	        		if(BackHandler.getBackLocations().get(toName).isToggle()
	        				&& !bypass)
	        		{
	        			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
	    		        DataOutputStream out = new DataOutputStream(streamout);
	    		        try {
	    					out.writeUTF(StaticValues.TP_TOGGLED);
	    					out.writeUTF(fromName);
	    				} catch (IOException e) {
	    					e.printStackTrace();
	    				}
	    		        plugin.getServer().getPlayer(fromName).get().getCurrentServer()
	    		        .ifPresent(y -> y.sendPluginMessage(StaticValues.TP_TOSPIGOT, streamout.toByteArray()));
	    		        return;
	        		}
	        	}
	        	if(TeleportHandler.getPendingTeleports().containsKey(fromName)
	        			|| TeleportHandler.getPendingTeleportValueToName(toName) != null)
	        	{
	        		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
			        DataOutputStream out = new DataOutputStream(streamout);
			        try {
						out.writeUTF(StaticValues.TP_OCCUPIED);
						out.writeUTF(fromName);
					} catch (IOException e) {
						e.printStackTrace();
					}
			        plugin.getServer().getPlayer(fromName).get().getCurrentServer()
    		        .ifPresent(y -> y.sendPluginMessage(StaticValues.TP_TOSPIGOT, streamout.toByteArray()));
	        	} else
	        	{
	        		if(ForbiddenHandlerBungee.getValues(true, Mechanics.TPA_ONLY).contains(
	        				plugin.getServer().getPlayer(fromName).get().getCurrentServer().get().getServerInfo().getName())
	        				|| ForbiddenHandlerBungee.getValues(true, Mechanics.TPA_ONLY).contains(
	                				plugin.getServer().getPlayer(toName).get().getCurrentServer().get().getServerInfo().getName()))
	        		{
	        			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
	    		        DataOutputStream out = new DataOutputStream(streamout);
	    		        try {
	    					out.writeUTF(StaticValues.TP_FORBIDDENSERVER);
	    					out.writeUTF(fromName);
	    				} catch (IOException e) {
	    					e.printStackTrace();
	    				}
	    		        plugin.getServer().getPlayer(fromName).get().getCurrentServer()
	    		        .ifPresent(y -> y.sendPluginMessage(StaticValues.TP_TOSPIGOT, streamout.toByteArray()));
	    			    return;
	        		}
	        		
	        		if(ForbiddenHandlerBungee.getValues(false, Mechanics.TPA_ONLY).contains(
	        				TeleportHandler.getPlayerWorld().get(fromName))
	        				|| ForbiddenHandlerBungee.getValues(false, Mechanics.TPA_ONLY).contains(
	        						TeleportHandler.getPlayerWorld().get(toName)))
	        		{
	        			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
	    		        DataOutputStream out = new DataOutputStream(streamout);
	    		        try {
	    					out.writeUTF(StaticValues.TP_FORBIDDENWORLD);
	    					out.writeUTF(fromName);
	    				} catch (IOException e) {
	    					e.printStackTrace();
	    				}
	    		        plugin.getServer().getPlayer(fromName).get().getCurrentServer()
	    		        .ifPresent(y -> y.sendPluginMessage(StaticValues.TP_TOSPIGOT, streamout.toByteArray()));
	    			    return;
	        		}
	        		boolean isToggled = false;
	        		if(BackHandler.getBackLocations().get(toName) != null)
	        		{
	        			if(BackHandler.getBackLocations().get(toName).isToggle()
	            				&& bypass)
	            		{
	            			isToggled = true;
	            		}
	        		}
	        		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
			        DataOutputStream out = new DataOutputStream(streamout);
			        try {
						out.writeUTF(StaticValues.TP_FREE);
						out.writeUTF(fromName);
						out.writeUTF(toName);
						out.writeUTF(type);
						out.writeBoolean(isToggled);
					} catch (IOException e) {
						e.printStackTrace();
					}
			        plugin.getServer().getPlayer(fromName).get().getCurrentServer()
    		        .ifPresent(y -> y.sendPluginMessage(StaticValues.TP_TOSPIGOT, streamout.toByteArray()));
	        	}
	        	return;
	        } else if(task.equals(StaticValues.TP_SENDMESSAGE))
	        {
	        	String fromName = in.readUTF();
	        	String toName = in.readUTF();
	        	String message = in.readUTF();
	        	boolean returns = in.readBoolean();
	        	String returnmessage = in.readUTF();
	        	if(plugin.getServer().getPlayer(toName).isEmpty())
	        	{
	        		if(returns)
	        		{
	        			plugin.getServer().getPlayer(fromName).ifPresent(y -> y.sendMessage(ChatApi.text(returnmessage)));
	        		}
	        		return;
	        	}
	        	plugin.getServer().getPlayer(toName).ifPresent(y -> y.sendMessage(ChatApi.text(message)));
	        	return;
	        } else if(task.equals(StaticValues.TP_SENDTEXTCOMPONENT))
	        {
	        	String fromName = in.readUTF();
	        	String toName = in.readUTF();
	        	String message = in.readUTF();
	        	boolean returns = in.readBoolean();
	        	String returnmessage = in.readUTF();
	        	if(plugin.getServer().getPlayer(toName).isEmpty())
	        	{
	        		if(returns)
	        		{
	        			plugin.getServer().getPlayer(fromName).ifPresent(y -> y.sendMessage(ChatApi.text(returnmessage)));
	        		}
	        		return;
	        	}
	        	plugin.getServer().getPlayer(toName).ifPresent(y -> y.sendMessage(ChatApi.generateOldTextComponentFormat(message)));
	        	return;
	        } else if(task.equals(StaticValues.TP_OBJECT))
	        {
	        	String fromUUID = in.readUTF();
	        	String fromName = in.readUTF();
	        	String toUUID = in.readUTF();
	        	String toName = in.readUTF();
	        	String type = in.readUTF();
	        	if(!TeleportHandler.getPendingTeleports().containsKey(fromName))
	        	{
	        		TeleportHandler.getPendingTeleports().put(fromName, 
	        				new Teleport(UUID.fromString(fromUUID), fromName, UUID.fromString(toUUID), toName, Teleport.Type.valueOf(type)));
	        	}
	        	return;
	        } else if(task.equals(StaticValues.TP_CANCELINVITE))
	        {
	        	String fromName = in.readUTF();
	        	String toName = in.readUTF();
	        	int runTaskPeriod = in.readInt();
	        	boolean sendMessage = in.readBoolean();
	        	String messageFrom = in.readUTF();
	        	String messageTo = in.readUTF();
	        	plugin.getServer().getScheduler().buildTask(plugin, () ->
	    		{
	    			if(TeleportHandler.getPendingTeleports().containsKey(fromName))
    	        	{
    					TeleportHandler.getPendingTeleports().remove(fromName);
        	        	if(sendMessage)
        	        	{
        	        		if(plugin.getServer().getPlayer(fromName) != null)
        	        		{
        	        			plugin.getServer().getPlayer(fromName).ifPresent(y -> y.sendMessage(ChatApi.text(messageFrom)));
        	        		}
        	        		if(plugin.getServer().getPlayer(toName) != null)
        	        		{
        	        			plugin.getServer().getPlayer(toName).ifPresent(y -> y.sendMessage(ChatApi.text(messageTo)));
        	        		}
        	            	
        	        	}
    	        	}
	    		}).delay(runTaskPeriod, TimeUnit.SECONDS).schedule();
	        	return;
	        } else if(task.equals(StaticValues.TP_ACCEPT))
	        {
	        	String fromName = in.readUTF();
	        	String toName = in.readUTF();
	        	String errormessage = in.readUTF();
	        	int delayed = in.readInt();
	        	TeleportHandler th = new TeleportHandler(plugin);
	        	th.preTeleportPlayerToPlayer(fromName, toName, errormessage, delayed);
	        	return;
	        } else if(task.equals(StaticValues.TP_DENY))
	        {
	        	String fromName = in.readUTF();
	        	String toName = in.readUTF();
	        	String message = in.readUTF();
	        	boolean sendMessage = in.readBoolean();
	        	String errormessage = in.readUTF();
	        	if(!TeleportHandler.getPendingTeleports().containsKey(fromName))
	        	{
	        		if(sendMessage)
	        		{
	        			plugin.getServer().getPlayer(toName).ifPresent(y -> y.sendMessage(ChatApi.text(errormessage)));
	        			return;
	        		}
	        	}
	        	TeleportHandler.getPendingTeleports().remove(fromName);
	    		plugin.getServer().getPlayer(fromName).ifPresent(y -> y.sendMessage(ChatApi.text(message)));
	    		plugin.getServer().getPlayer(toName).ifPresent(y -> y.sendMessage(ChatApi.text(message)));
	        	return;
	        } else if(task.equals(StaticValues.TP_CANCEL))
	        {
	        	String fromName = in.readUTF();
	        	String message = in.readUTF();
	        	boolean sendMessage = in.readBoolean();
	        	String errormessage = in.readUTF();
	        	if(!TeleportHandler.getPendingTeleports().containsKey(fromName))
	        	{
	        		if(sendMessage)
	        		{
	        			plugin.getServer().getPlayer(fromName).ifPresent(y -> y.sendMessage(ChatApi.text(errormessage)));
	        			return;
	        		}
	        	}
	        	final String toName = TeleportHandler.getPendingTeleports().get(fromName).getToName();
	        	TeleportHandler.getPendingTeleports().remove(fromName);
	        	TeleportHandler.getPendingTeleports().remove(
	        			TeleportHandler.getPendingTeleportValueToName(fromName));
	        	String messages = message.replace("%fromplayer%", fromName).replace("%toplayer%", toName);
	    		plugin.getServer().getPlayer(fromName).ifPresent(y -> y.sendMessage(ChatApi.text(messages)));
	    		plugin.getServer().getPlayer(toName).ifPresent(y -> y.sendMessage(ChatApi.text(messages)));
	        	return;
	        } else if(task.equals(StaticValues.TP_FORCE))
	        {
	        	String uuid = in.readUTF();
	        	String playername = in.readUTF();
	        	String fromUUID = in.readUTF();
	        	String fromName = in.readUTF();
	        	String toUUID = in.readUTF();
	        	String toName = in.readUTF();
	        	String type = in.readUTF();
	        	String errormessage = in.readUTF();
	        	int delayed = in.readInt();
	        	BackHandler.getBack(in, uuid, playername, Mechanics.TELEPORT);
	        	TeleportHandler th = new TeleportHandler(plugin);
	        	th.preTeleportPlayerToPlayerForceUse(
	        			new Teleport(UUID.fromString(fromUUID), fromName, UUID.fromString(toUUID), toName, Teleport.Type.valueOf(type)),
	        			errormessage, delayed);
	        	return;
	        } else if(task.equals(StaticValues.TP_SILENT))
	        {
	        	String uuid = in.readUTF();
	        	String playername = in.readUTF();
	        	String otherUUID = in.readUTF();
	        	String otherName = in.readUTF();
	        	String errormsg = in.readUTF();
	        	BackHandler.getBack(in, uuid, playername, Mechanics.TELEPORT);
	        	TeleportHandler th = new TeleportHandler(plugin);
	        	th.preTeleportPlayerToPlayerSilentUse(uuid, playername, otherUUID, otherName, errormsg);
	        	return;
	        } else if(task.equals(StaticValues.TP_ALL))
	        {
	        	String fromName = in.readUTF();
	        	boolean isSpecific = in.readBoolean();
	        	int delayed = in.readInt();
	        	if(isSpecific)
	        	{
	        		
	        	} else
	        	{
	        		TeleportHandler th = new TeleportHandler(plugin);
	            	th.preTeleportAllPlayerToOnePlayer(fromName, delayed);
	        	}
	        	return;
	        } else if(task.equals(StaticValues.TP_POS))
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
	        	String errorServerNotFound = in.readUTF();
	        	int delayed = in.readInt();
	        	BackHandler.getBack(in, uuid, playerName, Mechanics.TELEPORT);
	        	ServerLocation location = new ServerLocation(server, worldName, x, y, z, yaw, pitch);
	        	TeleportHandler th = new TeleportHandler(plugin);
	        	th.teleportPlayerToPosition(playerName, location, errorServerNotFound, delayed);
	        	return;
	        } else if(task.equals(StaticValues.TP_SENDWORLD))
	        {
	        	String playerName = in.readUTF();
	        	String worldName = in.readUTF();
	        	if(TeleportHandler.getPlayerWorld().containsKey(playerName))
	        	{
	        		TeleportHandler.getPlayerWorld().replace(playerName, worldName);
	        	} else
	        	{
	        		TeleportHandler.getPlayerWorld().put(playerName, worldName);
	        	}
	        	return;
	        }
	        return;
	}
	
	public static HashMap<String,Teleport> getPendingTeleports()
	{
		return pendingTeleports;
	}
	
	public static String getPendingTeleportValueToName(String toName)
	{
		String r = null;
		for(String fromName : getPendingTeleports().keySet())
		{
			Teleport tp = getPendingTeleports().get(fromName);
			if(tp.getToName().equals(toName))
			{
				r = fromName;
				break;
			}
		}
		return r;
	}
	
	public static HashMap<String,String> getPlayerWorld()
	{
		return playerWorld;
	}
	
	public static void sendServerQuitMessage(Player sender, String otherPlayerName)
	{
		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(streamout);
        try {
			out.writeUTF(StaticValues.TP_SERVERQUITMESSAGE);
			out.writeUTF(sender.getUsername());
			out.writeUTF(otherPlayerName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    sender.getCurrentServer().ifPresent(y -> y.sendPluginMessage(StaticValues.TP_TOSPIGOT, streamout.toByteArray()));
	}
	
	public void teleportPlayer(Player sender, Player target, int delay)
	{
		if(sender == null || target == null)
		{
			return;
		}
		plugin.getServer().getScheduler().buildTask(plugin, () ->
		{
			if(sender == null || target == null)
			{
				return;
			}
			if(!sender.getCurrentServer().get().getServerInfo().getName().equals(target.getCurrentServer().get().getServerInfo().getName()))
			{
				Optional<ServerConnection> server = target.getCurrentServer();
				server.ifPresent((t) -> sender.createConnectionRequest(t.getServer()).connect());
			}
			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(streamout);
	        try {
				out.writeUTF(StaticValues.TP_PLAYERTOPLAYER);
				out.writeUTF(sender.getUsername());
				out.writeUTF(target.getUsername());
			} catch (IOException e) {
				e.printStackTrace();
			}
		    target.getCurrentServer().ifPresent(y -> y.sendPluginMessage(StaticValues.TP_TOSPIGOT, streamout.toByteArray()));
		}).delay(delay, TimeUnit.MILLISECONDS).schedule();
	}
	
	//Player to has accepted
	public void preTeleportPlayerToPlayer(String fromName, String toName, String errormessage, int delay)
	{
		if(fromName.equals("nu") && toName.equals("nu"))
		{
			return;
		} else if(fromName.equals("nu"))
		{
			String fn = null;
			for(Entry<String, Teleport> set : getPendingTeleports().entrySet())
			{
				if(set.getValue() == null || set.getValue().getFromName() == null ||set.getValue().getToName() == null 
						|| !set.getValue().getToName().equals(toName))
				{
					continue;
				}
				fn = set.getValue().getFromName();
				break;
			}
			if(fn == null)
			{
				Optional<Player> to = plugin.getServer().getPlayer(toName); //Player witch execute the /tpa
				if(to.isEmpty())
				{
					return;
				}
				to.ifPresent(y -> y.sendMessage(ChatApi.text(errormessage)));
				return;
			}
			Optional<Player> ofrom = plugin.getServer().getPlayer(fn);
			Optional<Player> oto = plugin.getServer().getPlayer(toName);
			if(ofrom.isEmpty())
			{
				return;
			}
			if(oto.isEmpty())
			{
				ofrom.ifPresent(y -> y.sendMessage(ChatApi.text(errormessage)));
				return;
			}
			if(!getPendingTeleports().containsKey(fromName))
	    	{
				oto.ifPresent(y -> y.sendMessage(ChatApi.text(errormessage)));
				return;
	    	}
			final Teleport teleport = pendingTeleports.get(fromName);
			if(!teleport.getToName().equals(toName))
			{
				oto.ifPresent(y -> y.sendMessage(ChatApi.text(errormessage)));
				return;
			}
			getPendingTeleports().remove(fromName);
			Player from = ofrom.get();
			Player to = oto.get();
			if(teleport.getType() == Teleport.Type.TPTO)
			{
				teleportPlayer(from, to, delay);
			} else if(teleport.getType() == Teleport.Type.TPHERE)
			{
				teleportPlayer(to, from, delay);
			}
			return;
		} else if(toName.equals("nu"))
		{
			Optional<Player> ofrom = plugin.getServer().getPlayer(fromName); //Player witch execute the /tpa
			if(ofrom.isEmpty())
			{
				return;
			}
			if(!getPendingTeleports().containsKey(fromName))
	    	{
				ofrom.ifPresent(y -> y.sendMessage(ChatApi.text(errormessage)));
				return;
	    	}
			Teleport teleport = pendingTeleports.get(fromName);
			Optional<Player> oto = plugin.getServer().getPlayer(teleport.getToName());
			if(oto.isEmpty())
			{
				ofrom.ifPresent(y -> y.sendMessage(ChatApi.text(errormessage)));
				return;
			}
			getPendingTeleports().remove(fromName);
			Player from = ofrom.get();
			Player to = oto.get();
			if(teleport.getType() == Teleport.Type.TPTO)
			{
				teleportPlayer(from, to, delay);
			} else if(teleport.getType() == Teleport.Type.TPHERE)
			{
				teleportPlayer(to, from, delay);
			}
			return;
		} else
		{
			Optional<Player> ofrom = plugin.getServer().getPlayer(fromName); //Player witch execute the /tpa
			Optional<Player> oto = plugin.getServer().getPlayer(toName);
			if(ofrom.isEmpty())
			{
				return;
			}
			if(oto.isEmpty())
			{
				ofrom.ifPresent(y -> y.sendMessage(ChatApi.text(errormessage)));
				return;
			}
			Player from = ofrom.get(); //Player witch execute the /tpa
			Player to = oto.get();
			if(!getPendingTeleports().containsKey(fromName))
	    	{
				to.sendMessage(ChatApi.text(errormessage));
				return;
	    	}
			Teleport teleport = pendingTeleports.get(fromName);
			if(!teleport.getToName().equals(toName))
			{
				to.sendMessage(ChatApi.text(errormessage));
				return;
			}
			getPendingTeleports().remove(fromName);
			if(teleport.getType() == Teleport.Type.TPTO)
			{
				teleportPlayer(from, to, delay);
			} else if(teleport.getType() == Teleport.Type.TPHERE)
			{
				teleportPlayer(to, from, delay);
			}
			return;
		}
	}
	
	public void preTeleportPlayerToPlayerForceUse(Teleport teleport, String errormessage, int delay)
	{
		Optional<Player> ofrom = plugin.getServer().getPlayer(teleport.getFromName());
		Optional<Player> oto = plugin.getServer().getPlayer(teleport.getToName());
		if(ofrom == null)
		{
			return;
		}
		if(oto == null)
		{
			ofrom.ifPresent(y -> y.sendMessage(ChatApi.text(errormessage)));
			return;
		}
		Player from = ofrom.get();
		Player to = oto.get();
		if(teleport.getType() == Teleport.Type.TPTO)
		{
			teleportPlayer(from, to, delay);
		} else if(teleport.getType() == Teleport.Type.TPHERE)
		{
			teleportPlayer(to, from, delay);
		}
	}
	
	public void preTeleportPlayerToPlayerSilentUse(String uuid, String playername, String otherUUID, String otherName, String errormessage)
	{
		Optional<Player> osender = plugin.getServer().getPlayer(playername);
		Optional<Player> otarget = plugin.getServer().getPlayer(otherName);
		if(osender.isEmpty())
		{
			return;
		}
		if(otarget.isEmpty())
		{
			osender.get().sendMessage(ChatApi.text(errormessage));
			return;
		}
		Player sender = osender.get();
		Player target = otarget.get();
		plugin.getServer().getScheduler().buildTask(plugin, () ->
		{
			if(sender == null || target == null)
			{
				return;
			}
			if(!sender.getCurrentServer().get().getServerInfo().getName().equals(target.getCurrentServer().get().getServerInfo().getName()))
			{
				Optional<ServerConnection> server = target.getCurrentServer();
				server.ifPresent((t) -> sender.createConnectionRequest(t.getServer()).connect());
			}
			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(streamout);
	        try {
				out.writeUTF(StaticValues.TP_SILENTPLAYERTOPLAYER);
				out.writeUTF(sender.getUsername());
				out.writeUTF(target.getUsername());
			} catch (IOException e) {
				e.printStackTrace();
			}
		    target.getCurrentServer().ifPresent(y -> y.sendPluginMessage(StaticValues.TP_TOSPIGOT, streamout.toByteArray()));
		}).delay(1, TimeUnit.MILLISECONDS).schedule();
	}
	
	public void preTeleportAllPlayerToOnePlayer(String fromName, int delay, Object... objects)
	{
		Optional<Player> ofrom = plugin.getServer().getPlayer(fromName);
		if(ofrom.isEmpty())
		{
			return;
		}
		Player from = ofrom.get();
		String server = (String) objects[0];
		String world = (String) objects[1];
		if(server == null && world == null)
		{
			for(Player to : plugin.getServer().getAllPlayers())
			{
				if(!from.getUsername().equals(to.getUsername()))
				{
					teleportPlayer(to, from, delay);
				}
			}
		} else
		{
			for(Player to : plugin.getServer().getAllPlayers())
			{
				if(getPlayerWorld().containsKey(to.getUsername()))
				{
					boolean worlds = getPlayerWorld().get(to.getUsername()).equals(world);
					if(!from.getUsername().equals(to.getUsername())
							&& to.getCurrentServer().get().getServerInfo().getName().equals(server)
							&& worlds)
					{
						teleportPlayer(to, from, delay);
					}
				}
			}
		}
	}
	
	public void teleportPlayerToPosition(String playerName, ServerLocation location, String errorServerNotFound, int delay)
	{
		Player player = plugin.getServer().getPlayer(playerName).get();
		if(player == null)
		{
			return;
		}
		if(plugin.getServer().getServer(location.getServer()).isEmpty())
		{
			player.sendMessage(ChatApi.text(errorServerNotFound));
			return;
		}
		teleportPlayerToPositionPost(player, location, delay);
	}
	
	public void teleportPlayerToPositionPost(Player player, ServerLocation location, int delay)
	{
		if(player == null || location == null)
		{
			return;
		}
		if(!PluginMessageListener.containsServer(location.getServer()))
		{
			player.sendMessage(ChatApi.text("Server %server% is unknown!".replace("%server%", location.getServer())));
			return;
		}
		if(!player.getCurrentServer().get().getServerInfo().getName().equals(location.getServer()))
		{
			Optional<RegisteredServer> server = plugin.getServer().getServer(location.getServer());
			server.ifPresent((target) -> player.createConnectionRequest(target).connect());
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
			ByteArrayOutputStream streamout = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(streamout);
	        try {
				out.writeUTF(StaticValues.TP_PLAYERTOPOSITION);
				out.writeUTF(player.getUsername());
				out.writeUTF(location.getServer());
				out.writeUTF(location.getWorldName());
				out.writeDouble(location.getX());
				out.writeDouble(location.getY());
				out.writeDouble(location.getZ());
				out.writeFloat(location.getYaw());
				out.writeFloat(location.getPitch());
			} catch (IOException e) {
				e.printStackTrace();
			}
	        plugin.getServer().getServer(location.getServer()).ifPresent(y -> y.sendPluginMessage(StaticValues.TP_TOSPIGOT, streamout.toByteArray()));
		}).delay(delay, TimeUnit.MILLISECONDS).schedule();
	}
}