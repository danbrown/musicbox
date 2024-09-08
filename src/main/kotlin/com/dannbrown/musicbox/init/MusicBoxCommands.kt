package com.dannbrown.musicbox.init

import com.dannbrown.musicbox.MusicBoxModule
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.Util
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

object MusicBoxCommands {
  var DOWNLOADING_SONG: String = Util.makeDescriptionId("command", ResourceLocation(MusicBoxModule.MOD_ID, MusicBoxModule.MOD_ID + ".downloading_song"))
  var DOWNLOADING_SONG_ERROR: String = Util.makeDescriptionId("command", ResourceLocation(MusicBoxModule.MOD_ID, MusicBoxModule.MOD_ID + ".downloading_song_error"))
  var DOWNLOADING_SONG_SUCCESS: String = Util.makeDescriptionId("command", ResourceLocation(MusicBoxModule.MOD_ID, MusicBoxModule.MOD_ID + ".downloading_song_success"))
  var PLAYING_SONG: String = Util.makeDescriptionId("command", ResourceLocation(MusicBoxModule.MOD_ID, MusicBoxModule.MOD_ID + ".playing_song"))
  @Throws(CommandSyntaxException::class)
  private fun current(stack: CommandSourceStack): Int {
    val server = stack.server
    val players = server.playerList

    for (player in players.players) {
      stack.sendSuccess({ Component.translatable(PLAYING_SONG, player.displayName) }, false)
    }

    return 1
  }

  fun register(dispatcher: CommandDispatcher<CommandSourceStack?>) {
    dispatcher.register(Commands.literal(MusicBoxModule.MOD_ID)
      .requires { source -> source.hasPermission(2) }
      .then(Commands.literal("current")
        .executes { context -> current(context.source) })
    )
  }
}
