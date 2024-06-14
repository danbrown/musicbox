package com.dannbrown.musicbox.content.items

import com.dannbrown.musicbox.MusicBoxItems
import com.dannbrown.musicbox.MusicBoxNetworking
import com.dannbrown.musicbox.content.networking.OpenDiscScreenS2CPacket
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.RecordItem
import net.minecraft.world.level.Level

class URLDiscItem(comparatorOutput: Int, sound: SoundEvent, settings: Properties) : RecordItem(comparatorOutput, sound, settings, 0) {
  companion object{
    const val URL_MAX_LENGTH = 200
    const val URL_TAG_KEY = "song_url"
    const val DURATION_TAG_KEY = "song_duration" // in seconds
    const val NAME_TAG_KEY = "song_name"
    const val RADIUS_TAG_KEY = "song_radius"
  }

  override fun getDescription(): Component {
    return Component.literal("URL Disc")
  }

  override fun getDisplayName(): MutableComponent {
    return Component.literal("URL Disc")
  }

  override fun use(world: Level, player: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
    val stackInHand: ItemStack = player.getItemInHand(pUsedHand)
    if (!world.isClientSide && stackInHand.`is`(MusicBoxItems.CUSTOM_RECORD.get())) {
      MusicBoxNetworking.sendToPlayer(OpenDiscScreenS2CPacket(stackInHand), player as ServerPlayer)
      return InteractionResultHolder.success(player.getItemInHand(pUsedHand))
    }
    return super.use(world, player, pUsedHand)
  }
}