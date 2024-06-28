package main.java.me.avankziar.ptm.velocity.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import main.java.me.avankziar.ptm.velocity.database.Language.ISO639_2B;

public class YamlManager
{
	private ISO639_2B languageType = ISO639_2B.GER;
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	private static LinkedHashMap<String, Language> configKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> forbiddenListKeys = new LinkedHashMap<>();
	
	public YamlManager()
	{
		initConfigBungee();
		initForbiddenList();	
	}
	
	public ISO639_2B getLanguageType()
	{
		return languageType;
	}

	public void setLanguageType(ISO639_2B languageType)
	{
		this.languageType = languageType;
	}
	
	public ISO639_2B getDefaultLanguageType()
	{
		return defaultLanguageType;
	}
	
	public LinkedHashMap<String, Language> getConfigKey()
	{
		return configKeys;
	}
	
	public LinkedHashMap<String, Language> getForbiddenListKey()
	{
		return forbiddenListKeys;
	}
	
	/*
	 * The main methode to set all paths in the yamls.
	 */
	public void setFileInputVelocity(dev.dejvokep.boostedyaml.YamlDocument yml,
			LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType) throws org.spongepowered.configurate.serialize.SerializationException
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(key.startsWith("#"))
		{
			//Comments
			String k = key.replace("#", "");
			if(yml.get(k) == null)
			{
				//return because no actual key are present
				return;
			}
			if(yml.getBlock(k) == null)
			{
				return;
			}
			if(yml.getBlock(k).getComments() != null && !yml.getBlock(k).getComments().isEmpty())
			{
				//Return, because the comments are already present, and there could be modified. F.e. could be comments from a admin.
				return;
			}
			if(keyMap.get(key).languageValues.get(languageType).length == 1)
			{
				if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
				{
					String s = ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", "");
					yml.getBlock(k).setComments(Arrays.asList(s));
				}
			} else
			{
				List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
				ArrayList<String> stringList = new ArrayList<>();
				if(list instanceof List<?>)
				{
					for(Object o : list)
					{
						if(o instanceof String)
						{
							stringList.add(((String) o).replace("\r\n", ""));
						}
					}
				}
				yml.getBlock(k).setComments((List<String>) stringList);
			}
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", ""));
			} else
			{
				yml.set(key, keyMap.get(key).languageValues.get(languageType)[0]);
			}
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add(((String) o).replace("\r\n", ""));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	private void addComments(LinkedHashMap<String, Language> mapKeys, String path, Object[] o)
	{
		mapKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, o));
	}
	
	private void addConfig(String path, Object[] c, Object[] o)
	{
		configKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER}, c));
		addComments(configKeys, "#"+path, o);
	}
	
	public void initConfigBungee() //INFO:Config
	{
		addConfig("Language",
				new Object[] {
				"ENG"},
				new Object[] {
				"Die eingestellte Sprache. Von Haus aus sind 'ENG=Englisch' und 'GER=Deutsch' mit dabei.",
				"Falls andere Sprachen gewünsch sind, kann man unter den folgenden Links nachschauen, welchs Kürzel für welche Sprache gedacht ist.",
				"Siehe hier nach, sowie den Link, welche dort auch für Wikipedia steht.",
				"https://github.com/Avankziar/RootAdministration/blob/main/src/main/java/me/avankziar/roota/general/Language.java",
				"The set language. By default, ENG=English and GER=German are included.",
				"If other languages are required, you can check the following links to see which abbreviation is intended for which language.",
				"See here, as well as the link, which is also there for Wikipedia.",
				"https://github.com/Avankziar/RootAdministration/blob/main/src/main/java/me/avankziar/roota/general/Language.java"});
		addConfig("DeleteDeathBackAfterUsing", 
				new Object[] {
				true},
				new Object[] {
				"",
				"Beschreibt, ob ein Deathback nach dem Nutzen des /deathback Befehls gelöscht werden soll.",
				"Wenn true, kann der /deathback Befehl nur ein einziges Mal pro Tod genutzt werden.",
				"",
				"Describes whether a deathback should be deleted after using the /deathback command.",
				"If true, the /deathback command can only be used once per death."});
		/*configKeys.put("DeleteDeathBackAfterUsing"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));*/
	}
	
	/*private void addForbiddenList(String path, Object[] c, Object[] o)
	{
		forbiddenListKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER}, c));
		addComments(forbiddenListKeys, "#"+path, o);
	}*/
	
	public void initForbiddenList() //INFO:ForbiddenListBungee
	{
		forbiddenListKeys.put("ForbiddenToCreate.Back.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		addComments(forbiddenListKeys, "#ForbiddenToCreate", 
				new Object[] {
				"Die ForbiddenToCreate Sektion ist dafür da, um Welten und Server im Proxynetz zu deklarieren,",
				"wo es verboten ist, dass diese jeweilige Mechanik für den Spieler erstellt wird.",
				"The ForbiddenToCreate section is there to declare worlds and servers in the proxy network",
				"where it is forbidden for this particular mechanic to be created for the player."});
		forbiddenListKeys.put("ForbiddenToCreate.Back.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		forbiddenListKeys.put("ForbiddenToCreate.Deathback.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		forbiddenListKeys.put("ForbiddenToCreate.Deathback.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		
		forbiddenListKeys.put("ForbiddenToUse.TPA.Server",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"hub","nether"}));
		addComments(forbiddenListKeys, "#ForbiddenToUse", 
				new Object[] {
				"",
				"Die ForbiddenToUse Sektion ist dafür da, um Welten und Server im Proxynetz zu deklarieren,",
				"wo es verboten ist, dass der Spieler die jeweilige Mechanik nutzten darf.",
				"",
				"The ForbiddenToUse section is there to declare worlds and servers in the proxy network",
				"where it is forbidden for the player to use the respective mechanic."});
		forbiddenListKeys.put("ForbiddenToUse.TPA.World",
				new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						"spawnworld","city1"}));
		
	}
}