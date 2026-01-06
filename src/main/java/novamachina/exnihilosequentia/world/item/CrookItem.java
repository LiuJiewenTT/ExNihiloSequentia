package novamachina.exnihilosequentia.world.item;

import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.RecipeType;
// import net.minecraft.world.item.ToolMaterial;
import novamachina.exnihilosequentia.tags.ExNihiloTags;
import novamachina.novacore.world.item.ItemDefinition;

public class CrookItem extends DiggerItem {

  public CrookItem(Tier tier, final int maxDamage) {
    super(
        tier,
        ExNihiloTags.MINEABLE_WITH_CROOK,
        new Item.Properties().durability(maxDamage));
  }

  // This is for 1.21.3+
  // public CrookItem(ToolMaterial tier, final float baseDamage, final float attackSpeed, Item.Properties properties) {
  //   super(tier, ExNihiloTags.MINEABLE_WITH_CROOK, baseDamage, attackSpeed, properties);
  // }

  // This is removed when upgrading to 1.21.3+
  @Override
  public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
    ItemDefinition<CrookItem> woodRegistryObject = EXNItems.CROOK_WOOD;
    if (itemStack.getItem() == woodRegistryObject.asItem()) {
      return 200;
    } else {
      return 0;
    }
  }

  @FunctionalInterface
  public interface CrookFunction {
    CrookItem apply(
        Tier tier, final int maxDamage);
  }

  // This is added when upgrading to 1.21.3+
  // @FunctionalInterface
  // public interface CrookFunction {
  //   CrookItem apply(
  //       ToolMaterial tier, float baseDamage, float attackSpeed, Item.Properties properties);
  // }
}
