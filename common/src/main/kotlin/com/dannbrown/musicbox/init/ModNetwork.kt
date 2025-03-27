package com.dannbrown.musicbox.init

import com.dannbrown.deltaboxlib.registrate.network.NetworkDirection
import com.dannbrown.musicbox.content.networking.OpenDiscScreenS2CPacket
import com.dannbrown.musicbox.content.networking.PlayCustomDiscS2CPacket
import com.dannbrown.musicbox.content.networking.SaveDiscUrlC2SPacket
import com.dannbrown.musicbox.init.ModContent.REGISTRATE

object ModNetwork {
  @JvmField
  val CHANNEL = REGISTRATE.network()
    .register(NetworkDirection.PLAY_TO_SERVER, SaveDiscUrlC2SPacket::class.java, ::SaveDiscUrlC2SPacket)
    .register(NetworkDirection.PLAY_TO_CLIENT, OpenDiscScreenS2CPacket::class.java, ::OpenDiscScreenS2CPacket)
    .register(NetworkDirection.PLAY_TO_CLIENT, PlayCustomDiscS2CPacket::class.java, ::PlayCustomDiscS2CPacket)
    .build()

  fun register() {
    // init
  }
}