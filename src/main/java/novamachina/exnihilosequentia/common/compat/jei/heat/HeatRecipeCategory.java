package novamachina.exnihilosequentia.common.compat.jei.heat;

import java.awt.*;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import novamachina.exnihilosequentia.common.utility.ExNihiloConstants;
import novamachina.exnihilosequentia.world.item.crafting.HeatRecipe;

public class HeatRecipeCategory implements IRecipeCategory<HeatRecipe> {

  @Nonnull
  public static final ResourceLocation UID =
      ResourceLocation.fromNamespaceAndPath(ExNihiloConstants.ModIds.EX_NIHILO_SEQUENTIA, "heat");

  @Nonnull private final IDrawableStatic background;

  public HeatRecipeCategory(@Nonnull final IGuiHelper guiHelper) {
    background =
        guiHelper
            .drawableBuilder(
                ResourceLocation.fromNamespaceAndPath(
                    ExNihiloConstants.ModIds.JEI, "textures/jei/gui/gui_vanilla.png"),
                0,
                134,
                18,
                34)
            .addPadding(0, 0, 0, 80)
            .build();
  }

  @Override
  public void draw(
      HeatRecipe recipe,
      IRecipeSlotsView recipeSlotsView,
      GuiGraphics stack,
      double mouseX,
      double mouseY) {
    @Nonnull final Minecraft minecraft = Minecraft.getInstance();
    stack.drawString(minecraft.font, recipe.getAmount() + "X", 24, 12, Color.white.getRGB());
    //    // TODO doing something better than just writing what it is
    //
    //    @Nullable final Ingredient block = recipe.getInput();
    //    if (block == Blocks.WALL_TORCH) {
    //      minecraft.font.draw(stack, "Wall Torch", 24, 0, Color.DARK_GRAY.getRGB());
    //    } else if (block == Blocks.REDSTONE_WALL_TORCH) {
    //      minecraft.font.draw(stack, "Redstone Wall Torch", 24, 0, Color.DARK_GRAY.getRGB());
    //    } else if (block != null) {
    //      minecraft.font.draw(stack, block.getName(), 24, 0, Color.DARK_GRAY.getRGB());
    //    }
  }

  @Nullable
  @Override
  public IDrawable getIcon() {
    return null;
  }

  @Override
  public RecipeType<HeatRecipe> getRecipeType() {
    return new RecipeType<>(UID, HeatRecipe.class);
  }

  @Nonnull
  @Override
  public Component getTitle() {
    return Component.translatable("jei.category.heat");
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, HeatRecipe recipe, IFocusGroup focuses) {
    Block blockInput = recipe.getInputBlock();
    if (blockInput == null) {
      return;
    }
    ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(blockInput);
    if (BuiltInRegistries.FLUID.containsKey(blockId)) {
      Fluid fluid = BuiltInRegistries.FLUID.get(blockId);
      builder
          .addSlot(RecipeIngredientRole.INPUT, 1, 17)
          .addIngredients(NeoForgeTypes.FLUID_STACK, List.of(new FluidStack(fluid, 1000)));

    } else {
      @Nonnull ItemLike input = recipe.getInputBlock();
      if (input == Blocks.FIRE || input == Blocks.SOUL_FIRE) {
        input = Items.FLINT_AND_STEEL;
      }
      if (input instanceof LiquidBlock liquidBlock) {
        input = liquidBlock.defaultBlockState().getFluidState().getType().getBucket();
      }
      builder.addSlot(RecipeIngredientRole.INPUT, 1, 17).addIngredients(Ingredient.of(input));
    }
  }
}
