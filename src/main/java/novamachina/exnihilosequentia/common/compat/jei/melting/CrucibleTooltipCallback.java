package novamachina.exnihilosequentia.common.compat.jei.melting;

import java.util.Optional;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import novamachina.exnihilosequentia.common.registries.ExNihiloRegistries;

public class CrucibleTooltipCallback implements IRecipeSlotRichTooltipCallback {

  @Override
  public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
    if (recipeSlotView.getRole() == RecipeIngredientRole.INPUT) {
      Optional<ITypedIngredient<?>> optional = recipeSlotView.getDisplayedIngredient();

      optional
          .flatMap(ITypedIngredient::getItemStack)
          .flatMap(
              itemStack -> ExNihiloRegistries.CRUCIBLE_REGISTRY.findRecipe(itemStack.getItem()))
          .ifPresent(
              recipe ->
                  tooltip.add(
                      Component.literal(
                              String.format(
                                  "Fluid Amount: %d mb", recipe.getResultFluid().getAmount()))
                          .withStyle(ChatFormatting.GRAY)));
    }
  }
}
