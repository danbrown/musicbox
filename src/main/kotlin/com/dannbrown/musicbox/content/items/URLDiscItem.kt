package com.dannbrown.musicbox.content.items

import com.dannbrown.musicbox.MusicBoxItems
import com.dannbrown.musicbox.MusicBoxModule
import com.dannbrown.musicbox.MusicBoxNetworking
import com.dannbrown.musicbox.content.gui.MusicDiscScreen
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

class URLDiscItem(comparatorOutput: Int, sound: SoundEvent, props: Properties) : RecordItem(comparatorOutput, sound, props, 0) {
  companion object {
    const val CUSTOM_DISC_TRANSLATION_KEY = "item.${MusicBoxModule.MOD_ID}.custom_record.tooltip"

    const val URL_MAX_LENGTH = 200
    const val URL_TAG_KEY = "song_url"
    const val DURATION_TAG_KEY = "song_duration" // in seconds
    const val NAME_TAG_KEY = "song_name"
    const val TEXTURE_TAG_KEY = "song_texture"
    const val RADIUS_TAG_KEY = "song_radius"
    const val LOCKED_TAG_KEY = "song_locked"

    fun createDiscItem(url: String, duration: Int, name: String, variant: DiscVariant = DiscVariant.RED, radius: Int = 30, locked: Boolean = false): ItemStack {
      val item = ItemStack(MusicBoxItems.CUSTOM_RECORD.get())
      item.orCreateTag.putString(URL_TAG_KEY, url)
      item.orCreateTag.putInt(DURATION_TAG_KEY, duration)
      item.orCreateTag.putString(NAME_TAG_KEY, name)
      item.orCreateTag.putInt(RADIUS_TAG_KEY, radius)
      item.orCreateTag.putBoolean(LOCKED_TAG_KEY, locked)
      item.orCreateTag.putInt(TEXTURE_TAG_KEY, variant.toInt())
      item.hoverName = Component.literal(name)
      return item
    }
  }

  override fun getDescription(): Component {
    return Component.translatable(CUSTOM_DISC_TRANSLATION_KEY)
  }

  override fun getDisplayName(): MutableComponent {
    return Component.translatable(CUSTOM_DISC_TRANSLATION_KEY)
  }

  override fun isFoil(stack: ItemStack): Boolean {
    val containsTag = stack.orCreateTag.getBoolean(LOCKED_TAG_KEY)
    return containsTag || super.isFoil(stack)
  }

  override fun use(world: Level, player: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
    val stackInHand: ItemStack = player.getItemInHand(pUsedHand)
    if (!world.isClientSide && stackInHand.`is`(MusicBoxItems.CUSTOM_RECORD.get())) {
      if (stackInHand.orCreateTag.getBoolean(LOCKED_TAG_KEY)) {
        player.displayClientMessage(Component.translatable(MusicDiscScreen.LOCKED_DISC_TRANSLATION_KEY), true)
        return InteractionResultHolder.success(player.getItemInHand(pUsedHand))
      }
      MusicBoxNetworking.sendToPlayer(OpenDiscScreenS2CPacket(stackInHand), player as ServerPlayer)
      return InteractionResultHolder.success(player.getItemInHand(pUsedHand))
    }
    return super.use(world, player, pUsedHand)
  }

  enum class DiscVariant {
    LIGHT_BLUE,
    CYAN,
    BLUE,
    PURPLE,
    MAGENTA,
    RED,
    ORANGE,
    YELLOW,
    LIME,
    GREEN,
    AMETHYST,
    DIAMOND,
    EMERALD,
    GOLD,
    LAVA,
    COOKIE,
    CHORUS,
    LAPIS,
    ENDER_EYE,
    ENDER_PEARL,
    MAGMA,
    OCEAN,
    HONEY,
    PETRIFIED,
    MAGMA_CREAM,
    MELON,
    NAUTILUS;
    fun toInt(): Int {
      return this.ordinal + 1
    }
    companion object {
      fun random(): DiscVariant {
        return values().random()
      }
      fun fromInt(value: Int): DiscVariant {
        return values().getOrElse(value - 1) { RED }
      }
      fun maxVariants(): Int {
        return values().size
      }
    }
  }
}
