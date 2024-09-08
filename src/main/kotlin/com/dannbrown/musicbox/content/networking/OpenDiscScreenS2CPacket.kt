package com.dannbrown.musicbox.content.networking

import com.dannbrown.deltaboxlib.content.networking.NetworkPacketBase
import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.content.gui.MusicDiscMenu
import com.dannbrown.musicbox.content.gui.MusicDiscScreen
import com.dannbrown.musicbox.content.items.URLDiscItem
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent

class OpenDiscScreenS2CPacket : NetworkPacketBase {
  private var discItem: ItemStack? = null

  constructor()
  constructor (buffer: FriendlyByteBuf) : this(){
    discItem = buffer.readItem()
  }

  constructor (discItem: ItemStack) : this() {
    this.discItem = discItem
  }

  override fun write(buffer: FriendlyByteBuf) {
    discItem?.let { buffer.writeItem(it) }
  }

  override fun handle(context: NetworkEvent.Context): Boolean {
    context.enqueueWork {
      // reject if we are on the server
      if (context.direction.equals(NetworkDirection.PLAY_TO_SERVER)) {
        MusicBoxModule.LOGGER.warn("OpenCustomDiscS2CPacket received on the server side!")
        context.packetHandled = false
        return@enqueueWork
      }

      MusicBoxModule.LOGGER.info("Received packet to open custom disc GUI")

      val item = discItem ?: return@enqueueWork
      Minecraft.getInstance().setScreen(MusicDiscScreen(MusicDiscMenu(0, item), Inventory(Minecraft.getInstance().player)))
    }
    return true
  }
}