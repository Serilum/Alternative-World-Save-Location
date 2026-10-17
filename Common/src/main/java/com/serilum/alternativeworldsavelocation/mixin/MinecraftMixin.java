package com.serilum.alternativeworldsavelocation.mixin;

import com.mojang.datafixers.DataFixer;
import com.serilum.alternativeworldsavelocation.util.Reference;
import com.serilum.alternativeworldsavelocation.util.Util;
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
import java.util.HashMap;

@Mixin(value = Minecraft.class, priority = 1001)
public class MinecraftMixin {
	@Shadow private @Final DataFixer fixerUpper;
	@Shadow private @Mutable @Final LevelStorageSource levelSource;
	
	@Inject(method = "<init>(Lnet/minecraft/client/main/GameConfig;)V", at = @At(value = "TAIL"))
	private void Minecraft(GameConfig i, CallbackInfo ci) {		
		HashMap<String, String> configValues = ConfigFunctions.getConfigValues(Reference.MOD_ID);
		if (!configValues.getOrDefault("changeDefaultWorldSaveLocation", "false").equals("true")) {
			return;
		}

		String savespath = Util.returnSystemSpecificPath(configValues.getOrDefault("defaultMinecraftWorldSaveLocation", "").strip());

		String backuppath = savespath + File.separator + "_Backups";
		if (configValues.getOrDefault("changeDefaultWorldBackupLocation", "false").equals("true")) {
			backuppath = Util.returnSystemSpecificPath(configValues.getOrDefault("defaultMinecraftWorldBackupLocation", "").strip());
		}
		
		this.levelSource = new LevelStorageSource(Paths.get(savespath), Paths.get(backuppath), LevelStorageSource.parseValidator(Minecraft.getInstance().gameDirectory.toPath().resolve("allowed_symlinks.txt")), this.fixerUpper);
	}
}
