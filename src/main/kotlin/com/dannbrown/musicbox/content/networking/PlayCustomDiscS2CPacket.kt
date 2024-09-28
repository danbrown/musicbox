package com.dannbrown.musicbox.content.networking

import com.dannbrown.deltaboxlib.content.networking.NetworkPacketBase
import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.content.gui.MusicDiscScreen
import com.dannbrown.musicbox.lib.client.ClientAudioManager
import com.dannbrown.musicbox.lib.main.FileSound
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

      // Check if a download is already happening at this BlockPos
      if (MusicBoxModule.ongoingDownloads[blockPos] == true) {
        MusicBoxModule.LOGGER.warn("Download already in progress for $blockPos, allowing new download but will play the first to finish.")
      } else {
        // Mark the download as in progress
        MusicBoxModule.ongoingDownloads[blockPos!!] = true
      }

      if(!ClientAudioManager.fileNameToFile(fileNameWithExtension)!!.exists() && client.player != null) {
        client.player!!.sendSystemMessage(Component.translatable(MusicDiscScreen.DOWNLOADING_DISC_TRANSLATION_KEY))
        ClientAudioManager.downloadAudio(discUrl!!, fileName).thenAccept { result ->
          // After download finishes, check if this BlockPos still wants to play the sound
          MusicBoxModule.ongoingDownloads.remove(blockPos!!)
          if(result){
            client.player!!.sendSystemMessage(Component.translatable(MusicDiscScreen.DOWNLOADING_SUCCESS_DISC_TRANSLATION_KEY))
            // Only play if there is no currently playing sound
            if (MusicBoxModule.playingSounds[blockPos] == null) {
              val newFileSound = FileSound(fileName, blockPos!!, discRadius)
              MusicBoxModule.playingSounds[blockPos!!] = newFileSound
              client.soundManager.play(newFileSound)
              MusicBoxModule.LOGGER.info("Playing sound $fileName in $blockPos")
            } else {
              MusicBoxModule.LOGGER.warn("Sound already playing at $blockPos, not playing the new sound.")
            }
          } else {
            client.player!!.sendSystemMessage(Component.translatable(MusicDiscScreen.DOWNLOADING_ERROR_DISC_TRANSLATION_KEY))
          }
        }
      } else {
        MusicBoxModule.ongoingDownloads.remove(blockPos!!) // No need to track if the file already exists
        val newFileSound = FileSound(fileName, blockPos!!, discRadius)
        MusicBoxModule.playingSounds[blockPos!!] = newFileSound
        client.soundManager.play(newFileSound)
        MusicBoxModule.LOGGER.info("Playing sound $fileName in $blockPos")
      }
    }
    return true
  }
}