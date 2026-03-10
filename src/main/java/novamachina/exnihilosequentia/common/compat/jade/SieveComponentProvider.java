package novamachina.exnihilosequentia.common.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import novamachina.exnihilosequentia.ExNihiloSequentia;
import novamachina.exnihilosequentia.world.item.MeshType;
import novamachina.exnihilosequentia.world.level.block.entity.SieveBlockEntity;
import novamachina.novacore.util.StringUtils;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class SieveComponentProvider
    implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

  @Override
  public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
    CompoundTag tag = accessor.getServerData().getCompound("Sieve");

    if (tag.contains("progress") && tag.contains("block")) {
      tooltip.add(Component.literal(tag.getString("progress")));
      tooltip.add(Component.literal(tag.getString("block")));
    }
    if (tag.contains("mesh")) {
      tooltip.add(Component.literal(tag.getString("mesh")));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return ResourceLocation.fromNamespaceAndPath(ExNihiloSequentia.MOD_ID, "sieve");
  }

  @Override
  public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
    CompoundTag tag = new CompoundTag();
    BlockEntity blockEntity = blockAccessor.getBlockEntity();
    if (blockEntity instanceof SieveBlockEntity sieveBlockEntity) {
      if (!sieveBlockEntity.getBlockStack().isEmpty()) {
        tag.putString(
            "progress",
            Component.translatable(
                    "waila.progress", StringUtils.formatPercent(sieveBlockEntity.getProgress()))
                .getString());
        tag.putString(
            "block",
            Component.translatable(
                    "waila.sieve.block",
                    Component.translatable(sieveBlockEntity.getBlockStack().getDescriptionId()))
                .getString());
      }
      if (sieveBlockEntity.getMeshType() != MeshType.NONE) {
        tag.putString(
            "mesh",
            Component.translatable(
                    "waila.sieve.mesh",
                    Component.translatable(
                        "item."
                            + ExNihiloSequentia.MOD_ID
                            + "."
                            + sieveBlockEntity.getMeshType().getMeshName()))
                .getString());
      }
    }
    compoundTag.put("Sieve", tag);
  }
}
