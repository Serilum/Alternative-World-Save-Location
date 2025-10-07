package com.natamus.alternativeworldsavelocation.config;

import com.natamus.alternativeworldsavelocation.util.Reference;
import com.natamus.collective.config.DuskConfig;
import com.natamus.collective.functions.DataFunctions;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry public static boolean changeDefaultWorldSaveLocation = false;
	@Entry public static String defaultMinecraftWorldSaveLocation = (DataFunctions.getGameDirectory() + File.separator + "saves").replace(File.separator, "/");
	@Entry public static boolean changeDefaultWorldBackupLocation = false;
	@Entry public static String defaultMinecraftWorldBackupLocation = (DataFunctions.getGameDirectory() + File.separator + "backups").replace(File.separator, "/");

	public static void initConfig() {
		configMetaData.put("changeDefaultWorldSaveLocation", Arrays.asList(
			"Disabled by default, to prevent unwanted modpack behaviour. When enabled, changes the world location to 'defaultMinecraftWorldSaveLocation'"
		));
		configMetaData.put("defaultMinecraftWorldSaveLocation", Arrays.asList(
			"Use either \\\\ or / as a path separator. The location of the folder containing the world saves."
		));
		configMetaData.put("changeDefaultWorldBackupLocation", Arrays.asList(
			"Disabled by default. Enable this to set a specific world backup folder. If disabled, this will be set to 'defaultMinecraftWorldSaveLocation'/_Backup."
		));
		configMetaData.put("defaultMinecraftWorldBackupLocation", Arrays.asList(
			"Use either \\\\ or / as a path separator. The world backup folder if both 'changeDefaultWorldSaveLocation' and 'changeDefaultWorldBackupLocation' are enabled."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}