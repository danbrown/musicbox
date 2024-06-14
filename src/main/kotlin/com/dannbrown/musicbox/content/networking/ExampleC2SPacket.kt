package com.dannbrown.musicbox.content.networking

import com.dannbrown.databoxlib.content.networking.NetworkPacketBase
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraftforge.network.NetworkEvent

class ExampleC2SPacket : NetworkPacketBase {
  constructor()
  constructor (buffer: FriendlyByteBuf) : this()

  override fun write(buffer: FriendlyByteBuf) {
  }

  override fun handle(context: NetworkEvent.Context): Boolean {
    context.enqueueWork {
      // HERE WE ARE ON THE SERVER!
      val player = context.sender
      val level: ServerLevel = player!!.level() as ServerLevel
      EntityType.COW.spawn(level, null, player, player.blockPosition(), MobSpawnType.COMMAND, true, false)
    }
    return true
  }
}