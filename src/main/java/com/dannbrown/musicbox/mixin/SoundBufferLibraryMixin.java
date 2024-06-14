package com.dannbrown.musicbox.mixin;

import com.dannbrown.musicbox.MusicBoxModule;
import com.dannbrown.musicbox.client.ClientAudioManager;
import com.mojang.blaze3d.audio.OggAudioStream;
import net.minecraft.Util;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.LoopingAudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Mixin(SoundBufferLibrary.class)
public class SoundBufferLibraryMixin {
	@Inject(at = @At("HEAD"), method = "getStream", cancellable = true)
	public void loadStreamed(ResourceLocation id, boolean repeatInstantly,
			CallbackInfoReturnable<CompletableFuture<AudioStream>> cir) {
		if (!id.getNamespace().equals(MusicBoxModule.MOD_ID) || id.getPath().contains("placeholder_sound.ogg")) {
			return;
		}
		String toStrip = "sounds/customsounds/";
		String path = id.getPath().substring(toStrip.length() - 1);
		InputStream inputStream = ClientAudioManager.getAudioInputStream(path);
		String path2 = id.getPath().substring(toStrip.length());
		if (inputStream == null) {
			System.out.println("Failed to load sound: " + path2);
			return;
		}

		cir.setReturnValue(CompletableFuture.supplyAsync(() -> {
			try {
				return repeatInstantly ? new LoopingAudioStream(OggAudioStream::new, inputStream)
						: new OggAudioStream(inputStream);
			} catch (IOException iOException) {
				throw new CompletionException(iOException);
			}
		}, Util.backgroundExecutor()));

		cir.cancel();
	}
}