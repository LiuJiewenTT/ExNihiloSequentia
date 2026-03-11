package novamachina.exnihilosequentia.common.compat.jei.sifting;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.List;
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
import novamachina.exnihilosequentia.common.registries.ExNihiloRegistries;
import novamachina.exnihilosequentia.world.item.MeshItem;
import novamachina.exnihilosequentia.world.item.crafting.MeshWithChance;
import novamachina.exnihilosequentia.world.item.crafting.SiftingRecipe;
import novamachina.novacore.util.StringUtils;

public class SieveTooltipCallback implements IRecipeSlotRichTooltipCallback {

  private final JEISieveRecipe recipe;
  private final boolean isWaterlogged;

  public SieveTooltipCallback(JEISieveRecipe recipe, boolean isWaterlogged) {
    this.recipe = recipe;
    this.isWaterlogged = isWaterlogged;
  }

  @Override
  public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
    if (recipeSlotView.getRole() == RecipeIngredientRole.OUTPUT) {
      @Nonnull final Multiset<String> condensedTooltips = HashMultiset.create();
      @Nonnull
      final List<SiftingRecipe> drops =
          ExNihiloRegistries.SIEVE_REGISTRY.getDrops(
              recipe.getInputs().get(1).getFirst().getItem(),
              ((MeshItem) recipe.getInputs().getFirst().getFirst().getItem()).getType(),
              this.isWaterlogged);
      for (@Nonnull final SiftingRecipe entry : drops) {
        @Nonnull final ItemStack drop = entry.getDrop();
        Optional<ITypedIngredient<?>> optional = recipeSlotView.getDisplayedIngredient();
        Optional<ItemStack> shownStack = optional.flatMap(ITypedIngredient::getItemStack);
        if (shownStack.isEmpty() || !drop.is(shownStack.get().getItem())) {
          continue;
        }
        for (MeshWithChance meshWithChance : entry.getRolls()) {
          condensedTooltips.add(StringUtils.formatPercent(meshWithChance.getChance()));
        }
      }
      tooltip.add(Component.translatable("jei.sieve.dropChance").withStyle(ChatFormatting.GRAY));
      for (@Nonnull final String line : condensedTooltips.elementSet()) {
        tooltip.add(
            Component.literal(" * " + condensedTooltips.count(line) + "x " + line)
                .withStyle(ChatFormatting.GRAY));
      }
    }
  }
}
