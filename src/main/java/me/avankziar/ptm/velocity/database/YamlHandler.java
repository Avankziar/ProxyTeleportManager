package main.java.me.avankziar.ptm.velocity.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import main.java.me.avankziar.ptm.velocity.PTM;

public class YamlHandler
{	
	private PTM plugin;
	private Path dataDirectory;
	private YamlManager yamlManager;
	
	private YamlDocument config;
	public YamlDocument getConfig()
	{
		return config;
	}
	
	private YamlDocument config_forbiddenlist;
	public YamlDocument getForbiddenConfig()
	{
		return config_forbiddenlist;
	}
	
	public YamlHandler(PTM plugin)
	{
		this.plugin = plugin;
		this.dataDirectory = plugin.getDataDirectory();
		loadYamlHandler();
	}
	
	public boolean loadYamlHandler()
	{
		yamlManager = new YamlManager();
		plugin.setYamlManager(yamlManager);
		if(!mkdirStaticFiles())
		{
			return false;
		}
		return true;
	}
	
	public boolean mkdirStaticFiles()
	{
		File directory = new File(dataDirectory.getParent().toFile(), "/ProxyTeleportHandler/");
		if(!directory.exists())
		{
			directory.mkdirs();
		}
		try
	    {
	    	config = YamlDocument.create(new File(directory, "config.yml"),
	    			getClass().getResourceAsStream("/default.yml"),
	    			GeneralSettings.DEFAULT,
	    			LoaderSettings.builder().setAutoUpdate(true).build(),
	    			DumperSettings.DEFAULT,
	    			UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version"))
	    			.setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());
	    	config.update();
	    	writeFile(config, yamlManager.getConfigKey());
	    } catch (IOException e)
	    {
	    	e.printStackTrace();
	    	plugin.logger.severe("Could not create/load config.yml file! Plugin will shut down!");
	    	//plugin.onDisable();
	    }
		
		try
	    {
			config_forbiddenlist = YamlDocument.create(new File(directory, "config_forbiddenlist.yml"),
	    			getClass().getResourceAsStream("/default.yml"),
	    			GeneralSettings.DEFAULT,
	    			LoaderSettings.builder().setAutoUpdate(true).build(),
	    			DumperSettings.DEFAULT,
	    			UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version"))
	    			.setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());
			config_forbiddenlist.update();
			writeFile(config_forbiddenlist, yamlManager.getForbiddenListKey());
	    } catch (IOException e)
	    {
	    	e.printStackTrace();
	    	plugin.logger.severe("Could not create/load bungeeConfig.yml file! Plugin will shut down!");
	    	//plugin.onDisable();
	    }
		return true;
	}
	
	private boolean writeFile(YamlDocument yml, LinkedHashMap<String, Language> keyMap)
	{
		try
		{
			for(String key : keyMap.keySet())
			{
				Language languageObject = keyMap.get(key);
				if(languageObject.languageValues.containsKey(plugin.getYamlManager().getLanguageType()) == true)
				{
					plugin.getYamlManager().setFileInputVelocity(yml, keyMap, key, plugin.getYamlManager().getLanguageType());
				} else if(languageObject.languageValues.containsKey(plugin.getYamlManager().getDefaultLanguageType()) == true)
				{
					plugin.getYamlManager().setFileInputVelocity(yml, keyMap, key, plugin.getYamlManager().getDefaultLanguageType());
				}
			}
			yml.save();
		} catch(Exception e)
		{
			plugin.logger.warning("Could not write file %file%".replace("%file%", yml.getNameAsString()));
			e.printStackTrace();
			return false;
		}
		return true;
	}
}