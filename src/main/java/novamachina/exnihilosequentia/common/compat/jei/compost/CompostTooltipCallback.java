package novamachina.exnihilosequentia.common.compat.jei.compost;

import java.util.Optional;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import novamachina.exnihilosequentia.common.Config;
import novamachina.exnihilosequentia.common.registries.ExNihiloRegistries;

public class CompostTooltipCallback implements IRecipeSlotRichTooltipCallback {

  @Override
  public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
    Optional<ITypedIngredient<?>> optional = recipeSlotView.getDisplayedIngredient();
    if (recipeSlotView.getRole() == RecipeIngredientRole.INPUT && optional.isPresent()) {
      ItemStack stack = (ItemStack) optional.get().getIngredient();
      final int solidAmount = ExNihiloRegistries.COMPOST_REGISTRY.getSolidAmount(stack.getItem());

      tooltip.add(
          Component.literal(
              String.format("Amount: %d / %d", solidAmount, Config.getBarrelMaxSolidAmount())));
    }
  }
}
