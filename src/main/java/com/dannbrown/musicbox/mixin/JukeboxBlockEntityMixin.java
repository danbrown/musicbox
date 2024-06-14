package com.dannbrown.musicbox.mixin;


import com.dannbrown.musicbox.MusicBoxNetworking;
import com.dannbrown.musicbox.content.items.URLDiscItem;
import com.dannbrown.musicbox.content.networking.PlayCustomDiscS2CPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.ticks.ContainerSingleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin extends BlockEntityMixin implements ContainerSingleItem {

  @Shadow
  private long tickCount;

  @Shadow
  private long recordStartedTick;

  @Inject(at = @At("TAIL"), method = "popOutRecord")
  public void dropRecord(CallbackInfo ci) {
    // send to all players a empty string to stop the music on that position
    MusicBoxNetworking.sendToAllClients(new PlayCustomDiscS2CPacket(this.worldPosition, "", 0));
  }

  @Inject(at = @At("HEAD"), method = "startPlaying")
  public void startPlaying(CallbackInfo ci) {
    ItemStack recordStack = this.getFirstItem();
    if (recordStack.getItem() instanceof URLDiscItem && !this.level.isClientSide()) {
      String musicUrl = recordStack.getOrCreateTag().getString(URLDiscItem.URL_TAG_KEY);
      int musicRadius = recordStack.getOrCreateTag().getInt(URLDiscItem.RADIUS_TAG_KEY);

      if (!musicUrl.isEmpty()) {
        // send to all players the music url to play on that position
        MusicBoxNetworking.sendToAllClients(new PlayCustomDiscS2CPacket(this.worldPosition, musicUrl, musicRadius));
      }
    }
  }

  @Inject(at = @At("HEAD"), method = "shouldRecordStopPlaying", cancellable = true)
  public void shouldRecordStopPlaying(RecordItem pRecord, CallbackInfoReturnable<Boolean> cir) {
    if (pRecord instanceof URLDiscItem) {
      // get the URLDiscItem.LENGTH_TAG_KEY from the item stack
      ItemStack recordStack = this.getFirstItem();
      int songLength = recordStack.getOrCreateTag().getInt(URLDiscItem.DURATION_TAG_KEY) * 20; // convert seconds to ticks
      cir.setReturnValue(this.tickCount >= this.recordStartedTick + songLength);
    }
  }
}
