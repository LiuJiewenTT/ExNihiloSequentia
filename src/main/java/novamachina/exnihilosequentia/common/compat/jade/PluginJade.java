package novamachina.exnihilosequentia.common.compat.jade;

import javax.annotation.Nonnull;
import novamachina.exnihilosequentia.ExNihiloSequentia;
import novamachina.exnihilosequentia.world.level.block.BarrelBlock;
import novamachina.exnihilosequentia.world.level.block.CrucibleBlock;
import novamachina.exnihilosequentia.world.level.block.InfestingLeavesBlock;
import novamachina.exnihilosequentia.world.level.block.SieveBlock;
import novamachina.exnihilosequentia.world.level.block.entity.BarrelBlockEntity;
import novamachina.exnihilosequentia.world.level.block.entity.CrucibleBlockEntity;
import novamachina.exnihilosequentia.world.level.block.entity.InfestingLeavesBlockEntity;
import novamachina.exnihilosequentia.world.level.block.entity.SieveBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(ExNihiloSequentia.MOD_ID)
public class PluginJade implements IWailaPlugin {

  @Override
  public void register(@Nonnull final IWailaCommonRegistration registrar) {
    registrar.registerBlockDataProvider(new BarrelComponentProvider(), BarrelBlockEntity.class);
    registrar.registerBlockDataProvider(new CrucibleComponentProvider(), CrucibleBlockEntity.class);
    registrar.registerBlockDataProvider(new SieveComponentProvider(), SieveBlockEntity.class);
    registrar.registerBlockDataProvider(
        new InfestingLeavesComponentProvider(), InfestingLeavesBlockEntity.class);
  }

  @Override
  public void registerClient(IWailaClientRegistration registration) {
    registration.registerBlockComponent(new BarrelComponentProvider(), BarrelBlock.class);
    registration.registerBlockComponent(new CrucibleComponentProvider(), CrucibleBlock.class);
    registration.registerBlockComponent(new SieveComponentProvider(), SieveBlock.class);
    registration.registerBlockComponent(
        new InfestingLeavesComponentProvider(), InfestingLeavesBlock.class);
  }
}
