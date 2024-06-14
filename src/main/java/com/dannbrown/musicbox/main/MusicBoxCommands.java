package com.dannbrown.musicbox.main;

import com.dannbrown.musicbox.MusicBoxModule;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;

public class MusicBoxCommands {
  public static String DOWNLOADING_SONG = Util.makeDescriptionId("command", new ResourceLocation(MusicBoxModule.MOD_ID, MusicBoxModule.MOD_ID + ".downloading_song"));
  public static String DOWNLOADING_SONG_ERROR = Util.makeDescriptionId("command", new ResourceLocation(MusicBoxModule.MOD_ID, MusicBoxModule.MOD_ID + ".downloading_song_error"));
  public static String DOWNLOADING_SONG_SUCCESS = Util.makeDescriptionId("command", new ResourceLocation(MusicBoxModule.MOD_ID, MusicBoxModule.MOD_ID + ".downloading_song_success"));
  public static String PLAYING_SONG = Util.makeDescriptionId("command", new ResourceLocation(MusicBoxModule.MOD_ID, MusicBoxModule.MOD_ID + ".playing_song"));


  private static int current(CommandSourceStack stack) throws CommandSyntaxException {
    MinecraftServer server = stack.getServer();
    PlayerList players = server.getPlayerList();

    for (Player player : players.getPlayers()) {
      stack.sendSuccess( () -> Component.translatable(PLAYING_SONG, player.getDisplayName()), false);
    }

    return 1;
  }

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(Commands.literal(MusicBoxModule.MOD_ID)
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("current").executes(context -> current(context.getSource())))
    );
  }

}
