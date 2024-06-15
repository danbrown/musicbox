package com.dannbrown.musicbox.content.networking

import com.dannbrown.deltaboxlib.content.networking.NetworkPacketBase

import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.client.ClientAudioManager
import com.dannbrown.musicbox.main.FileSound
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import org.apache.commons.codec.digest.DigestUtils

class PlayCustomDiscS2CPacket : NetworkPacketBase {
  private var blockPos: BlockPos? = null
  private var discUrl: String? = null
  private var discRadius: Int = 0

  constructor()
  constructor (buffer: FriendlyByteBuf) : this(){
    blockPos = buffer.readBlockPos()
    discUrl = buffer.readUtf(32767)
    discRadius = buffer.readInt()
  }
  constructor(blockPos: BlockPos, discUrl: String, discRadius: Int) : this() {
    this.blockPos = blockPos
    this.discUrl = discUrl
    this.discRadius = discRadius
  }

  override fun write(buffer: FriendlyByteBuf) {
    blockPos?.let { buffer.writeBlockPos(it) }
    buffer.writeUtf(discUrl!!)
    buffer.writeInt(discRadius)
  }

  override fun handle(context: NetworkEvent.Context): Boolean {
    context.enqueueWork {
      // reject if we are on the server
      if (context.direction.equals(NetworkDirection.PLAY_TO_SERVER)) {
        MusicBoxModule.LOGGER.warn("PlayCustomDiscS2CPacket received on the server side!")
        context.packetHandled = false
        return@enqueueWork
      }
      MusicBoxModule.LOGGER.info("Received packet to play custom disc")

      // if no blockPos is provided, return
      if (blockPos == null) {
        MusicBoxModule.LOGGER.warn("Received invalid PlayCustomDiscS2CPacket, blockPos is null!")
        return@enqueueWork
      }

      val fileName = DigestUtils.sha256Hex(discUrl) // hash the url to get a unique filename
      val fileNameWithExtension = "$fileName.ogg"
      val client  = Minecraft.getInstance()

      // stop the current sound if it is playing
      val currentSound = MusicBoxModule.playingSounds[blockPos]
      if (currentSound != null) {
        MusicBoxModule.LOGGER.info("Stopping current sound in $blockPos")
        client.soundManager.stop(currentSound)
      }

      // if no disc url is provided, stop the sound and return
      if (discUrl == null || discUrl!!.isEmpty()) {
        MusicBoxModule.LOGGER.warn("Received invalid PlayCustomDiscS2CPacket, discUrl is null!")
        return@enqueueWork
      }

      if(!ClientAudioManager.fileNameToFile(fileNameWithExtension).exists() && client.player != null) {
        client.player!!.sendSystemMessage(Component.literal("Downloading music, please wait a moment..."))
        ClientAudioManager.downloadAudio(discUrl!!, fileName).thenAccept { result ->
          if(result){
            client.player!!.sendSystemMessage(Component.literal("Download complete!"))
            val newFileSound = FileSound(fileName, blockPos, discRadius)
            MusicBoxModule.playingSounds[blockPos!!] = newFileSound
            client.soundManager.play(newFileSound)
            MusicBoxModule.LOGGER.info("Playing sound $fileName in $blockPos")
          } else {
            client.player!!.sendSystemMessage(Component.literal("Failed to download music!"))
          }
        }
      } else {
        val newFileSound = FileSound(fileName, blockPos, discRadius)
        MusicBoxModule.playingSounds[blockPos!!] = newFileSound
        client.soundManager.play(newFileSound)
        MusicBoxModule.LOGGER.info("Playing sound $fileName in $blockPos")
      }
    }
    return true
  }
}