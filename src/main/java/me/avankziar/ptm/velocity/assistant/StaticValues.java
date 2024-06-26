package main.java.me.avankziar.ptm.velocity.assistant;

import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

public class StaticValues
{
	 public static final MinecraftChannelIdentifier 
	 		GENERAL_TOBUNGEE = MinecraftChannelIdentifier.from("btm:generaltobungee"),
	 		GENERAL_TOSPIGOT = MinecraftChannelIdentifier.from("btm:generaltospigot"),
	 		BACK_TOBUNGEE = MinecraftChannelIdentifier.from("btm:backtobungee"),
			BACK_TOSPIGOT = MinecraftChannelIdentifier.from("btm:backtospigot"),
			CUSTOM_TOBUNGEE = MinecraftChannelIdentifier.from("btm:customtobungee"),
			CUSTOM_TOSPIGOT = MinecraftChannelIdentifier.from("btm:customtospigot"),
			ENTITYTRANSPORT_TOBUNGEE = MinecraftChannelIdentifier.from("btm:entitytransporttobungee"),
			ENTITYTRANSPORT_TOSPIGOT = MinecraftChannelIdentifier.from("btm:entitytransporttospigot"),
			FIRSTSPAWN_TOBUNGEE = MinecraftChannelIdentifier.from("btm:firstspawntobungee"),
			FIRSTSPAWN_TOSPIGOT = MinecraftChannelIdentifier.from("btm:firstspawntospigot"),
			HOME_TOBUNGEE = MinecraftChannelIdentifier.from("btm:hometobungee"),
			HOME_TOSPIGOT = MinecraftChannelIdentifier.from("btm:hometospigot"),
			IFH_TOBUNGEE = MinecraftChannelIdentifier.from("btm:ifhtobungee"),
			IFH_TOSPIGOT = MinecraftChannelIdentifier.from("btm:ifhtospigot"),
			PORTAL_TOBUNGEE = MinecraftChannelIdentifier.from("btm:portaltobungee"),
			PORTAL_TOSPIGOT = MinecraftChannelIdentifier.from("btm:portaltospigot"),
			RANDOMTELEPORT_TOBUNGEE = MinecraftChannelIdentifier.from("btm:randomteleporttobungee"),
			RANDOMTELEPORT_TOSPIGOT = MinecraftChannelIdentifier.from("btm:randomteleporttospigot"),
			RESPAWN_TOBUNGEE = MinecraftChannelIdentifier.from("btm:respawntobungee"),
			RESPAWN_TOSPIGOT = MinecraftChannelIdentifier.from("btm:respawntospigot"),
			SAFE_TOBUNGEE = MinecraftChannelIdentifier.from("btm:safetobungee"),
			SAFE_TOSPIGOT = MinecraftChannelIdentifier.from("btm:safetospigot"),
			SAVEPOINT_TOBUNGEE = MinecraftChannelIdentifier.from("btm:savepointtobungee"),
			SAVEPOINT_TOSPIGOT = MinecraftChannelIdentifier.from("btm:savepointtospigot"),
			TP_TOBUNGEE = MinecraftChannelIdentifier.from("btm:teleporttobungee"),
			TP_TOSPIGOT = MinecraftChannelIdentifier.from("btm:teleporttospigot"),
			WARP_TOBUNGEE = MinecraftChannelIdentifier.from("btm:warptobungee"),
			WARP_TOSPIGOT = MinecraftChannelIdentifier.from("btm:warptospigot");
	 
	 final public static String
		//General
		GENERAL_TOBUNGEE_ = "btm:generaltobungee",
		GENERAL_TOSPIGOT_ = "btm:generaltospigot",
		GENERAL_SENDLIST = "general-sendlist",
		GENERAL_SENDSETTING = "general-sendsetting",
		//Custom
		CUSTOM_TOBUNGEE_ = "btm:customtobungee",
		CUSTOM_TOSPIGOT_ = "btm:customtospigot",
		CUSTOM_PLAYERTOPLAYER = "custom-playertoplayer",
		CUSTOM_PLAYERTOPOSITION = "custom-playertoposition",
		
		//Back Sending
		BACK_TOBUNGEE_ = "btm:backtobungee",
		BACK_TOSPIGOT_ = "btm:backtospigot",
		BACK_SENDOBJECT = "back-object",
		BACK_SENDDEATHOBJECT = "back-deathobject",
		BACK_SENDJOINOBJECT = "back-joinobject",
		BACK_SENDPLAYERBACK = "back-playertopos",
		BACK_SENDPLAYERDEATHBACK = "back-playertoposdeath",
		BACK_REQUESTNEWBACK = "back-requestnewback",
		BACK_NODEATHBACK = "back-nodeathback",
		
		//EntityTransport
		ENTITYTRANSPORT_TOBUNGEE_ = "btm:entitytransporttobungee",
		ENTITYTRANSPORT_TOSPIGOT_ = "btm:entitytransporttospigot",
		ENTITYTRANSPORT_ENTITYTOPOSITION = "entitytransport-entitytoposition",
		ENTITYTRANSPORT_ENTITYTOPLAYER = "entitytransport-entitytoplayer",
		
		//FirstSpawn Sending
		FIRSTSPAWN_TOBUNGEE_ = "btm:firstspawntobungee",
		FIRSTSPAWN_TOSPIGOT_ = "btm:firstspawntospigot",
		FIRSTSPAWN_PLAYERTOPOSITION = "firstspawn-playertoposition",
		FIRSTSPAWN_DOCOMMANDS = "firstspawn-docommands",
		
		//Home Sending
		HOME_TOBUNGEE_ = "btm:hometobungee",
		HOME_TOSPIGOT_ = "btm:hometospigot",
		HOME_PLAYERTOPOSITION = "home-playertoposition",
		
		//IFH
		IFH_TOBUNGEE_ = "btm:ifhtobungee",
		IFH_TOSPIGOT_ = "btm:ifhtospigot",
		IFH_PLAYERTOPOSITION = "ifh-playertoposition",
		
		//PORTAL Sending
		PORTAL_TOBUNGEE_ = "btm:portaltobungee",
		PORTAL_TOSPIGOT_ = "btm:portaltospigot",
		PORTAL_PLAYERTOPOSITION = "portal-playertoposition",
		PORTAL_SOUND = "portal-sound",
		PORTAL_UPDATE = "portal-update",
		
		//RandomTeleport
		RANDOMTELEPORT_TOBUNGEE_ = "btm:randomteleporttobungee",
		RANDOMTELEPORT_TOSPIGOT_ = "btm:randomteleporttospigot",
		RANDOMTELEPORT_PLAYERTOPOSITION = "randomteleport-playertoposition",
		
		//WARP Sending
		RESPAWN_TOBUNGEE_ = "btm:respawntobungee",
		RESPAWN_TOSPIGOT_ = "btm:respawntospigot",
		RESPAWN_PLAYERTOPOSITION = "respawn-playertoposition",
		
		//SavePoint Sending
		SAVEPOINT_TOBUNGEE_ = "btm:savepointtobungee",
		SAVEPOINT_TOSPIGOT_ = "btm:savepointtospigot",
		SAVEPOINT_PLAYERTOPOSITION = "savepoint-playertoposition",
		
		//Teleport Sending Bungee/Spigot
		TP_TOBUNGEE_ = "btm:teleporttobungee",
		TP_TOSPIGOT_ = "btm:teleporttospigot",
		TP_SENDMESSAGE = "tp-sendmessage",
		TP_SENDTEXTCOMPONENT = "tp-sendtextcomponent",
		TP_EXISTPENDING = "tp-existpending",
		TP_OBJECT = "tp-object",
		TP_CANCELINVITE = "tp-cancelinvite",
		TP_ACCEPT = "tp-accept",
		TP_DENY = "tp-deny",
		TP_CANCEL = "tp-cancel",
		TP_FORCE = "tp-force",
		TP_SILENT = "tp-silent",
		TP_ALL = "tp-all",
		TP_POS = "tp-pos",
		TP_SENDWORLD = "tp-sendworld",
		TP_FREE = "tp-free",
		TP_OCCUPIED = "tp-occupied",
		TP_SERVERQUITMESSAGE = "tp-serverquitmessage",
		TP_PLAYERTOPLAYER = "tp-playertoplayer",
		TP_SILENTPLAYERTOPLAYER = "tp-silentplayertoplayer",
		TP_PLAYERTOPOSITION = "tp-playertoposition",
		TP_FORBIDDENSERVER = "tp-forbiddenserver",
		TP_FORBIDDENWORLD = "tp-forbiddenworld",
		TP_TOGGLED = "tp-toggled",
		
		//WARP Sending
		WARP_TOBUNGEE_ = "btm:warptobungee",
		WARP_TOSPIGOT_ = "btm:warptospigot",
		WARP_PLAYERTOPOSITION = "warp-playertoposition",
		
		//SAFE Location Sending
		SAFE_TOBUNGEE_ = "btm:safetobungee",
		SAFE_TOSPIGOT_ = "btm:safetospigot",
		SAFE_CHECKPATH = "safe-checkpath",
		SAFE_CHECKEDPATH = "safe-checkedpath"
		;
	
	public static String
	
		BYPASS_COST = "btm.bypass.cost.",
		BYPASS_DELAY = "btm.bypass.delay.",
		BYPASS_FORBIDDEN_CREATE = "btm.bypass.forbidden.create.",
		BYPASS_FORBIDDEN_USE = "btm.bypass.forbidden.use.",
		
		//EntityTransport
		BYPASS_ENTITYTRANSPORT = "btm.bypass.entitytransport.admin",
		BYPASS_ENTITYTRANSPORT_ACCESSLIST = "btm.bypass.entitytransport.accesslist",
		BYPASS_ENTITYTRANSPORT_SERIALIZATION = "btm.bypass.entitytransport.serialization.",
		
		//Home
		PERM_HOME_OTHER = "btm.cmd.staff.home.home.other",
		PERM_HOMES_OTHER = "btm.cmd.staff.home.homes.other",
		
		PERM_HOME_COUNTHOMES_WORLD = "btm.count.home.world.",
		PERM_HOME_COUNTHOMES_SERVER = "btm.count.home.server.",
		PERM_HOME_COUNTHOMES_GLOBAL = "btm.count.home.global.",
		
		PERM_BYPASS_HOME = "btm.bypass.home.admin",
		PERM_BYPASS_HOME_TOOMANY = "btm.bypass.home.toomany",
		
		//Portal
		PERM_BYPASS_PORTAL = "btm.bypass.portal.admin",
		PERM_BYPASS_PORTALPLACER = "btm.bypass.portal.placer",
		PERM_PORTALS_OTHER = "btm.bypass.portal.portals.other",
		PERM_BYPASS_PORTAL_BLACKLIST = "btm.bypass.portal.blacklist",
		PERM_BYPASS_PORTAL_TOOMANY = "btm.bypass.portal.toomany",
		
		PERM_PORTAL_COUNTPORTALS_WORLD = "btm.count.portal.world.",
		PERM_PORTAL_COUNTPORTALS_SERVER = "btm.count.portal.server.",
		PERM_PORTAL_COUNTPORTALS_GLOBAL = "btm.count.portal.global.",
		
		//SavePoint Bypass
		PERM_BYPASS_SAVEPOINT_OTHER = "btm.bypass.savepoint.other",
		PERM_BYPASS_SAVEPOINTS_OTHER = "btm.bypass.savepoint.savepoints.other",
	
		//Teleport
		PERM_BYPASS_TELEPORT_TPATOGGLE = "btm.bypass.tp.tpatoggle",
		PERM_BYPASS_TELEPORT_SILENT = "btm.bypass.tp.silent",
		
		//Warp
		PERM_WARP_OTHER = "btm.bypass.warp.warp.other",
		PERM_WARPS_OTHER = "btm.bypass.warp.warps.other",
		
		PERM_WARP_COUNTWARPS_WORLD = "btm.count.warp.world.",
		PERM_WARP_COUNTWARPS_SERVER = "btm.count.warp.server.",
		PERM_WARP_COUNTWARPS_GLOBAL = "btm.count.warp.global.",
				
		PERM_BYPASS_WARP = "btm.bypass.warp.admin",
		PERM_BYPASS_WARP_BLACKLIST = "btm.bypass.warp.blacklist",
		PERM_BYPASS_WARP_TOOMANY = "btm.bypass.warp.toomany";
}