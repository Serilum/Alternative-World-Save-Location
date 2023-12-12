package com.natamus.alternativeworldsavelocation.fabric.mixin;

import com.mojang.datafixers.DataFixer;
import com.natamus.alternativeworldsavelocation.util.Reference;
import com.natamus.alternativeworldsavelocation.util.Util;
import com.natamus.collective.functions.ConfigFunctions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

@Mixin(value = Minecraft.class, priority = 1001)
public class MinecraftMixin {
	@Shadow private @Final DataFixer fixerUpper;
	@Shadow private @Mutable @Final LevelStorageSource levelSource;
	
	@Inject(method = "<init>(Lnet/minecraft/client/main/GameConfig;)V", at = @At(value = "TAIL"))
	private void Minecraft(GameConfig i, CallbackInfo ci) {		
		boolean changeDefaultWorldSaveLocation = false;
		boolean changeDefaultWorldBackupLocation = false;
		String rawsavespath = "";
		String rawbackupspath = "";
		
		List<String> rawconfig = ConfigFunctions.getRawConfigValues(Reference.MOD_ID);
		for (String rc : rawconfig) {
			if (rc.contains("changeDefaultWorldSaveLocation")) {
				changeDefaultWorldSaveLocation = rc.contains("true");
			}
			else if (rc.contains("changeDefaultWorldBackupLocation")) {
				changeDefaultWorldBackupLocation = rc.contains("true");
			}
			else if (rc.contains("defaultMinecraftWorldSaveLocation") && rc.contains("\":")) {
				rawsavespath = rc.split("\":")[1].replace("\"", "").trim();
			}
			else if (rc.contains("defaultMinecraftWorldBackupLocation") && rc.contains("\\\":")) {
				rawbackupspath = rc.split("\":")[1].replace("\"", "").trim();
			}
		}
		
		if (!changeDefaultWorldSaveLocation) {
			return;
		}
		
		String savespath = Util.returnSystemSpecificPath(rawsavespath);
		
		String backuppath = savespath + File.separator + "_Backups";
		if (changeDefaultWorldBackupLocation) {
			backuppath = Util.returnSystemSpecificPath(rawbackupspath);
		}
		
		this.levelSource = new LevelStorageSource(Paths.get(savespath), Paths.get(backuppath), this.fixerUpper);
	}
}
