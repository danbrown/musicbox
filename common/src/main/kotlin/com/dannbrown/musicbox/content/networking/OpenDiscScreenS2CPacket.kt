package com.dannbrown.musicbox.content.networking

import com.dannbrown.deltaboxlib.registrate.network.NetworkChannelHandler
import com.dannbrown.deltaboxlib.registrate.network.NetworkDirection
import com.dannbrown.deltaboxlib.registrate.network.NetworkPacket
import com.dannbrown.deltaboxlib.registrate.util.DeltaboxUtil
import com.dannbrown.musicbox.content.gui.MusicDiscMenu
import com.dannbrown.musicbox.content.gui.MusicDiscScreen
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class OpenDiscScreenS2CPacket : NetworkPacket {
  private var discItem: ItemStack? = null

  constructor()
  constructor (buffer: FriendlyByteBuf) : this() {
    discItem = buffer.readItem()
  }

  constructor (discItem: ItemStack) : this() {
    this.discItem = discItem
  }

  override fun handle(context: NetworkChannelHandler.Context) {
    // reject if we are on the server
    if (context.direction == NetworkDirection.PLAY_TO_SERVER) {
      DeltaboxUtil.LOGGER.warn("OpenCustomDiscS2CPacket received on the server side!")
      return
    }

    DeltaboxUtil.LOGGER.info("Received packet to open custom disc GUI")
    val item = discItem ?: return

    Minecraft.getInstance()
      .setScreen(MusicDiscScreen(MusicDiscMenu(0, item), Inventory(Minecraft.getInstance().player as Player)))
  }

  override fun writeToBuffer(buf: FriendlyByteBuf) {
    discItem?.let { buf.writeItem(it) }
  }
}