package novamachina.exnihilosequentia.common.compat.jei.crushing;

import java.util.Optional;
import javax.annotation.Nonnull;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import novamachina.exnihilosequentia.world.item.crafting.CrushingRecipe;
import novamachina.novacore.util.StringUtils;

public class CrushingTooltipCallback implements IRecipeSlotRichTooltipCallback {

  @Nonnull private final CrushingRecipe crushingRecipe;

  public CrushingTooltipCallback(@Nonnull final CrushingRecipe crushingRecipe) {
    this.crushingRecipe = crushingRecipe;
  }

  @Override
  public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
    if (recipeSlotView.getRole() == RecipeIngredientRole.OUTPUT) {
      Optional<ITypedIngredient<?>> optional = recipeSlotView.getDisplayedIngredient();
      optional.ifPresent(
          iTypedIngredient ->
              crushingRecipe.getDrops().stream()
                  .filter(
                      stack ->
                          ItemStack.isSameItem(
                              iTypedIngredient.getItemStack().get(), stack.getStack()))
                  .forEach(
                      stack ->
                          tooltip.add(
                              Component.literal(StringUtils.formatPercent(stack.getChance()))
                                  .withStyle(ChatFormatting.GRAY))));
    }
  }
}
