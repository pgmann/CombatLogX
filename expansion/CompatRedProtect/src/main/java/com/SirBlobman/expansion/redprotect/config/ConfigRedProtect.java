package com.SirBlobman.expansion.redprotect.config;

import java.io.File;
import java.util.Arrays;

import org.bukkit.configuration.file.YamlConfiguration;

import com.SirBlobman.combatlogx.config.Config;
import com.SirBlobman.combatlogx.expansion.NoEntryExpansion.NoEntryMode;
import com.SirBlobman.combatlogx.utility.Util;
import com.SirBlobman.expansion.redprotect.CompatRedProtect;

public class ConfigRedProtect extends Config {
	public static double NO_ENTRY_KNOCKBACK_STRENGTH;
	public static int MESSAGE_COOLDOWN;
	private static YamlConfiguration config = new YamlConfiguration();
	private static String NO_ENTRY_MODE;

	public static void load() {
		File folder = CompatRedProtect.FOLDER;
		File file = new File(folder, "redprotect.yml");
		if (!file.exists()) copyFromJar("redprotect.yml", folder);

		config = load(file);
		defaults();
	}

	private static void defaults() {
		NO_ENTRY_MODE = get(config, "red protect options.no entry.mode", "KNOCKBACK");
		NO_ENTRY_KNOCKBACK_STRENGTH = get(config, "red protect options.no entry.knockback strength", 1.5D);
		MESSAGE_COOLDOWN = get(config, "red protect options.no entry.message cooldown", 5);
	}

	public static NoEntryMode getNoEntryMode() {
		if (NO_ENTRY_MODE == null || NO_ENTRY_MODE.isEmpty()) load();
		String mode = NO_ENTRY_MODE.toUpperCase();
		try {
			return NoEntryMode.valueOf(mode);
		} catch (Throwable ex) {
			String error = "Invalid Mode '" + NO_ENTRY_MODE + "' in 'red protect.yml'. Valid modes are " + String.join(" ", Arrays.stream(NoEntryMode.values()).map(Enum::name).toArray(String[]::new));
			Util.print(error);
			return NoEntryMode.CANCEL;
		}
	}
}