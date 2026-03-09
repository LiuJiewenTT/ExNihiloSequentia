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
  @Nonnull private final IDrawableStatic slotTexture;

  private static final int SLOT_SIZE = 18;
  private static final int TEXT_LINE_HEIGHT = 9;
  private static final int TEXT_GAP = 2;
  private static final int SLOT_GAP = 3;

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

    slotTexture =
        guiHelper
            .drawableBuilder(
                ResourceLocation.fromNamespaceAndPath(
                    ExNihiloConstants.ModIds.EX_NIHILO_SEQUENTIA,
                    "textures/gui/jei_single_slot.png"),
                0,
                0,
                18,
                18)
            // Prevent zoomed/cropped UVs when the source image is larger than 18x18.
            .setTextureSize(18, 18)
            .build();
  }

  private int getCenteredSlotX() {
    return (getWidth() - SLOT_SIZE) / 2;
  }

  private int getCenteredContentTopY() {
    final int contentHeight = (TEXT_LINE_HEIGHT * 2) + TEXT_GAP + SLOT_GAP + SLOT_SIZE;
    return (getHeight() - contentHeight) / 2;
  }

  private int getCenteredSlotY() {
    return getCenteredContentTopY() + (TEXT_LINE_HEIGHT * 2) + TEXT_GAP + SLOT_GAP;
  }

  @Override
  public void draw(
      HeatRecipe recipe,
      IRecipeSlotsView recipeSlotsView,
      GuiGraphics stack,
      double mouseX,
      double mouseY) {
    @Nonnull final Minecraft minecraft = Minecraft.getInstance();
    final int lineOneY = getCenteredContentTopY();
    final int lineTwoY = lineOneY + TEXT_LINE_HEIGHT + TEXT_GAP;

    // Display the heat source block name
    @Nullable final Block inputBlock = recipe.getInputBlock();
    if (inputBlock != null) {
      String blockName;
      // Special handling for wall torches
      if (inputBlock == Blocks.WALL_TORCH) {
        blockName = "Wall Torch";
      } else if (inputBlock == Blocks.REDSTONE_WALL_TORCH) {
        blockName = "Redstone Wall Torch";
      } else {
        blockName = inputBlock.getName().getString();
      }
      int textWidth = minecraft.font.width(blockName);
      int centerX = (getWidth() - textWidth) / 2;
      stack.drawString(minecraft.font, blockName, centerX, lineOneY, Color.DARK_GRAY.getRGB());
    }

    // Display the heat multiplier
    String multiplier = recipe.getAmount() + "X";
    int multiplierWidth = minecraft.font.width(multiplier);
    int centerX = (getWidth() - multiplierWidth) / 2;
    stack.drawString(minecraft.font, multiplier, centerX, lineTwoY, Color.white.getRGB());

    // Draw the slot texture behind the slot
    slotTexture.draw(stack, getCenteredSlotX(), getCenteredSlotY());
  }

  @Nullable
  @Override
  public IDrawable getIcon() {
    return null;
  }

  @Override
  public int getWidth() {
    return 166;
  }

  @Override
  public int getHeight() {
    return 58;
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
          .addSlot(RecipeIngredientRole.INPUT, getCenteredSlotX() + 1, getCenteredSlotY() + 1)
          .addIngredients(NeoForgeTypes.FLUID_STACK, List.of(new FluidStack(fluid, 1000)));

    } else {
      @Nonnull ItemLike input = recipe.getInputBlock();
      if (input == Blocks.FIRE || input == Blocks.SOUL_FIRE) {
        input = Items.FLINT_AND_STEEL;
      }
      if (input instanceof LiquidBlock liquidBlock) {
        input = liquidBlock.defaultBlockState().getFluidState().getType().getBucket();
      }
      builder
          .addSlot(RecipeIngredientRole.INPUT, getCenteredSlotX() + 1, getCenteredSlotY() + 1)
          .addIngredients(Ingredient.of(input));
    }
  }
}
