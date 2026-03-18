package novamachina.exnihilosequentia.common.compat.rei;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import novamachina.exnihilosequentia.ExNihiloSequentia;
import novamachina.exnihilosequentia.common.Config;
import novamachina.exnihilosequentia.common.compat.jei.melting.JEICrucibleRecipe;
import novamachina.exnihilosequentia.common.registries.ExNihiloRegistries;
import novamachina.exnihilosequentia.world.item.EXNItems;
import novamachina.exnihilosequentia.world.item.MeshItem;
import novamachina.exnihilosequentia.world.item.MeshType;
import novamachina.exnihilosequentia.world.item.crafting.CompostRecipe;
import novamachina.exnihilosequentia.world.item.crafting.CrushingRecipe;
import novamachina.exnihilosequentia.world.item.crafting.HarvestRecipe;
import novamachina.exnihilosequentia.world.item.crafting.HeatRecipe;
import novamachina.exnihilosequentia.world.item.crafting.PrecipitateRecipe;
import novamachina.exnihilosequentia.world.item.crafting.SiftingRecipe;
import novamachina.exnihilosequentia.world.item.crafting.SolidifyingRecipe;
import novamachina.exnihilosequentia.world.item.crafting.TransitionRecipe;
import novamachina.exnihilosequentia.world.level.block.EXNBlocks;
import novamachina.novacore.util.IngredientUtils;
import novamachina.novacore.util.StringUtils;

@REIPluginClient
public class ExNihiloREIPlugin implements REIClientPlugin {

  private static final ResourceLocation JEI_MID =
      ResourceLocation.fromNamespaceAndPath(ExNihiloSequentia.MOD_ID, "textures/gui/jei_mid.png");
  private static final ResourceLocation JEI_FLUID_BLOCK_TRANSFORM =
      ResourceLocation.fromNamespaceAndPath(
          ExNihiloSequentia.MOD_ID, "textures/gui/jei_fluid_block_transform.png");
  private static final ResourceLocation JEI_FLUID_ON_TOP =
      ResourceLocation.fromNamespaceAndPath(
          ExNihiloSequentia.MOD_ID, "textures/gui/jei_fluid_on_top.png");
  private static final ResourceLocation JEI_FLUID_TRANSFORM =
      ResourceLocation.fromNamespaceAndPath(
          ExNihiloSequentia.MOD_ID, "textures/gui/jei_fluid_transform.png");
  private static final ResourceLocation JEI_SINGLE_SLOT =
      ResourceLocation.fromNamespaceAndPath(
          ExNihiloSequentia.MOD_ID, "textures/gui/jei_single_slot.png");

  private static final CategoryIdentifier<ExNihiloDisplay> COMPOST = category("compost");
  private static final CategoryIdentifier<ExNihiloDisplay> CRUSHING = category("crushing");
  private static final CategoryIdentifier<ExNihiloDisplay> HARVEST = category("harvest");
  private static final CategoryIdentifier<ExNihiloDisplay> HEAT = category("heat");
  private static final CategoryIdentifier<ExNihiloDisplay> MELTING = category("melting");
  private static final CategoryIdentifier<ExNihiloDisplay> FIRED_MELTING =
      category("fired_melting");
  private static final CategoryIdentifier<ExNihiloDisplay> PRECIPITATE = category("precipitate");
  private static final CategoryIdentifier<ExNihiloDisplay> DRY_SIFTING = category("dry_sifting");
  private static final CategoryIdentifier<ExNihiloDisplay> WET_SIFTING = category("wet_sifting");
  private static final CategoryIdentifier<ExNihiloDisplay> TRANSITION = category("transition");
  private static final CategoryIdentifier<ExNihiloDisplay> SOLIDIFYING = category("solidifying");

  @Override
  public void registerCategories(CategoryRegistry registry) {
    registry.add(new CompostCategory());
    registry.add(new CrushingCategory());
    registry.add(new HarvestCategory());
    registry.add(new HeatCategory());
    registry.add(new MeltingCategory(MELTING, "jei.category.melting"));
    registry.add(new MeltingCategory(FIRED_MELTING, "jei.category.fired_melting"));
    registry.add(new PrecipitateCategory());
    registry.add(new SiftingCategory(DRY_SIFTING, false));
    registry.add(new SiftingCategory(WET_SIFTING, true));
    registry.add(new TransitionCategory());
    registry.add(new SolidifyingCategory());
    registerCatalysts(registry);
  }

  @Override
  public void registerDisplays(DisplayRegistry registry) {
    registerCompost(registry);
    registerCrushing(registry);
    registerHarvest(registry);
    registerHeat(registry);
    registerMelting(registry);
    registerFiredMelting(registry);
    registerPrecipitate(registry);
    registerDrySifting(registry);
    registerWetSifting(registry);
    registerTransition(registry);
    registerSolidifying(registry);
  }

  private void registerCompost(DisplayRegistry registry) {
    for (CompostRecipe recipe : ExNihiloRegistries.COMPOST_REGISTRY.getRecipeList()) {
      ItemStack[] inputArray = recipe.getInput().getItems();
      List<EntryIngredient> inputs = new ArrayList<>();
      if (inputArray.length > 21) {
        EntryIngredient ingredient = EntryIngredients.ofItemStacks(Arrays.asList(inputArray));
        ingredient.forEach(this::applyCompostTooltip);
        inputs.add(ingredient);
      } else {
        for (ItemStack stack : inputArray) {
          EntryIngredient ingredient = EntryIngredients.of(stack);
          ingredient.forEach(this::applyCompostTooltip);
          inputs.add(ingredient);
        }
      }
      registry.add(
          new ExNihiloDisplay(
              COMPOST, inputs, List.of(EntryIngredients.of(new ItemStack(Blocks.DIRT)))));
    }
  }

  private void registerCrushing(DisplayRegistry registry) {
    for (CrushingRecipe recipe : ExNihiloRegistries.HAMMER_REGISTRY.getRecipeList()) {
      List<EntryIngredient> outputs =
          recipe.getDrops().stream()
              .map(
                  drop -> {
                    EntryIngredient output = EntryIngredients.of(drop.getStack());
                    List<Component> tooltipLines =
                        recipe.getDrops().stream()
                            .filter(
                                candidate ->
                                    ItemStack.isSameItem(candidate.getStack(), drop.getStack()))
                            .map(
                                candidate ->
                                    (Component)
                                        Component.literal(
                                            StringUtils.formatPercent(candidate.getChance())))
                            .toList();
                    output.forEach(stack -> applyTooltip(stack, tooltipLines));
                    return output;
                  })
              .toList();
      registry.add(
          new ExNihiloDisplay(CRUSHING, List.of(entryFromIngredient(recipe.getInput())), outputs));
    }
  }

  private void registerHarvest(DisplayRegistry registry) {
    for (HarvestRecipe recipe : ExNihiloRegistries.CROOK_REGISTRY.getRecipeList()) {
      List<EntryIngredient> outputs =
          recipe.getDrops().stream()
              .map(
                  drop -> {
                    EntryIngredient output = EntryIngredients.of(drop.getStack());
                    List<Component> tooltipLines =
                        recipe.getDrops().stream()
                            .filter(
                                candidate ->
                                    ItemStack.isSameItem(candidate.getStack(), drop.getStack()))
                            .map(
                                candidate ->
                                    (Component)
                                        Component.literal(
                                            StringUtils.formatPercent(candidate.getChance())))
                            .toList();
                    output.forEach(stack -> applyTooltip(stack, tooltipLines));
                    return output;
                  })
              .toList();
      registry.add(
          new ExNihiloDisplay(HARVEST, List.of(entryFromIngredient(recipe.getInput())), outputs));
    }
  }

  private void registerHeat(DisplayRegistry registry) {
    for (HeatRecipe recipe : ExNihiloRegistries.HEAT_REGISTRY.getRecipeList()) {
      Block input = recipe.getInputBlock();
      if (input == null) {
        continue;
      }
      registry.add(
          new ExNihiloDisplay(
              HEAT,
              List.of(EntryIngredients.of(toHeatInput(recipe))),
              List.of(),
              toHeatName(input),
              recipe.getAmount() + "X"));
    }
  }

  private void registerMelting(DisplayRegistry registry) {
    getCrucibleRecipes().forEach(recipe -> registry.add(toMeltingDisplay(MELTING, recipe)));
  }

  private void registerFiredMelting(DisplayRegistry registry) {
    getCrucibleRecipes().forEach(recipe -> registry.add(toMeltingDisplay(FIRED_MELTING, recipe)));
  }

  private void registerPrecipitate(DisplayRegistry registry) {
    for (PrecipitateRecipe recipe : ExNihiloRegistries.FLUID_BLOCK_REGISTRY.getRecipeList()) {
      registry.add(
          new ExNihiloDisplay(
              PRECIPITATE,
              List.of(entryFromFluid(recipe.getFluid()), entryFromIngredient(recipe.getInput())),
              List.of(EntryIngredients.of(recipe.getOutput()))));
    }
  }

  private void registerDrySifting(DisplayRegistry registry) {
    buildSiftingDisplays(false).forEach(registry::add);
  }

  private void registerWetSifting(DisplayRegistry registry) {
    buildSiftingDisplays(true).forEach(registry::add);
  }

  private void registerTransition(DisplayRegistry registry) {
    for (TransitionRecipe recipe : ExNihiloRegistries.FLUID_TRANSFORM_REGISTRY.getRecipeList()) {
      registry.add(
          new ExNihiloDisplay(
              TRANSITION,
              List.of(
                  entryFromFluid(recipe.getFluidInTank()),
                  entryFromIngredient(recipe.getCatalyst())),
              List.of(entryFromFluid(recipe.getResult()))));
    }
  }

  private void registerSolidifying(DisplayRegistry registry) {
    for (SolidifyingRecipe recipe : ExNihiloRegistries.FLUID_ON_TOP_REGISTRY.getRecipeList()) {
      registry.add(
          new ExNihiloDisplay(
              SOLIDIFYING,
              List.of(
                  entryFromFluid(recipe.getFluidInTank()), entryFromFluid(recipe.getFluidOnTop())),
              List.of(EntryIngredients.of(recipe.getResult()))));
    }
  }

  private void registerCatalysts(CategoryRegistry registry) {
    registerCrushingCatalyst(registry);
    registerHarvestCatalyst(registry);
    registerCrucibles(registry);
    registerBarrels(registry);
    registerSieves(registry);
    registerHeatCatalysts(registry);
  }

  private void registerHeatCatalysts(CategoryRegistry registry) {
    List<HeatRecipe> heatRecipes = ExNihiloRegistries.HEAT_REGISTRY.getRecipeList();
    Set<Block> heatBlocks = new HashSet<>();
    for (HeatRecipe recipe : heatRecipes) {
      Block block = recipe.getInputBlock();
      if (block != null && !heatBlocks.contains(block)) {
        ItemStack catalyst = new ItemStack(block.asItem());
        if (!catalyst.isEmpty()) {
          registry.addWorkstations(HEAT, EntryStacks.of(catalyst));
          heatBlocks.add(block);
        }
      }
    }
  }

  private void registerHarvestCatalyst(CategoryRegistry registry) {
    TagKey<Item> crookTag =
        TagKey.create(
            BuiltInRegistries.ITEM.key(),
            ResourceLocation.fromNamespaceAndPath(ExNihiloSequentia.MOD_ID, "crook"));

    // Get all items in the crook tag
    BuiltInRegistries.ITEM
        .getTag(crookTag)
        .ifPresent(
            tag ->
                tag.stream()
                    .forEach(
                        holder -> {
                          ItemStack catalyst = new ItemStack(holder.value());
                          if (!catalyst.isEmpty()) {
                            registry.addWorkstations(HARVEST, EntryStacks.of(catalyst));
                          }
                        }));
  }

  private void registerCrushingCatalyst(CategoryRegistry registry) {
    TagKey<Item> hammerTag =
        TagKey.create(
            BuiltInRegistries.ITEM.key(),
            ResourceLocation.fromNamespaceAndPath(ExNihiloSequentia.MOD_ID, "hammer"));

    // Get all items in the hammer tag
    BuiltInRegistries.ITEM
        .getTag(hammerTag)
        .ifPresent(
            tag ->
                tag.stream()
                    .forEach(
                        holder -> {
                          ItemStack catalyst = new ItemStack(holder.value());
                          if (!catalyst.isEmpty()) {
                            registry.addWorkstations(CRUSHING, EntryStacks.of(catalyst));
                          }
                        }));
  }

  private void registerCrucibles(CategoryRegistry registry) {
    TagKey<Item> cruciblesTag =
        TagKey.create(
            BuiltInRegistries.ITEM.key(),
            ResourceLocation.fromNamespaceAndPath(ExNihiloSequentia.MOD_ID, "crucibles"));

    // Get all items in crucibles tag
    BuiltInRegistries.ITEM
        .getTag(cruciblesTag)
        .ifPresent(
            tag ->
                tag.stream()
                    .forEach(
                        holder -> {
                          ItemStack catalyst = new ItemStack(holder.value());
                          if (!catalyst.isEmpty()) {
                            EntryStack<?> stack = EntryStacks.of(catalyst);
                            // Check if it's a fired crucible (stone/crimson/warped)
                            String itemName =
                                BuiltInRegistries.ITEM.getKey(holder.value()).toString();
                            if (itemName.contains("fired")
                                || itemName.contains("crimson")
                                || itemName.contains("warped")) {
                              registry.addWorkstations(FIRED_MELTING, stack);
                              registry.addWorkstations(MELTING, stack);
                              registry.addWorkstations(HEAT, stack);
                            } else {
                              // Wood crucibles
                              registry.addWorkstations(MELTING, stack);
                              registry.addWorkstations(HEAT, stack);
                            }
                          }
                        }));
  }

  private void registerBarrels(CategoryRegistry registry) {
    TagKey<Item> barrelsTag =
        TagKey.create(
            BuiltInRegistries.ITEM.key(),
            ResourceLocation.fromNamespaceAndPath(ExNihiloSequentia.MOD_ID, "barrels"));

    // Get all items in the barrels tag
    BuiltInRegistries.ITEM
        .getTag(barrelsTag)
        .ifPresent(
            tag ->
                tag.stream()
                    .forEach(
                        holder -> {
                          ItemStack catalyst = new ItemStack(holder.value());
                          if (!catalyst.isEmpty()) {
                            EntryStack<?> stack = EntryStacks.of(catalyst);
                            registry.addWorkstations(SOLIDIFYING, stack);
                            registry.addWorkstations(TRANSITION, stack);
                            registry.addWorkstations(PRECIPITATE, stack);
                            registry.addWorkstations(COMPOST, stack);
                          }
                        }));
  }

  private void registerSieves(CategoryRegistry registry) {
    TagKey<Item> sievesTag =
        TagKey.create(
            BuiltInRegistries.ITEM.key(),
            ResourceLocation.fromNamespaceAndPath(ExNihiloSequentia.MOD_ID, "sieves"));

    // Get all items in the sieves tag
    BuiltInRegistries.ITEM
        .getTag(sievesTag)
        .ifPresent(
            tag ->
                tag.stream()
                    .forEach(
                        holder -> {
                          ItemStack catalyst = new ItemStack(holder.value());
                          if (!catalyst.isEmpty()) {
                            EntryStack<?> stack = EntryStacks.of(catalyst);
                            registry.addWorkstations(DRY_SIFTING, stack);
                            registry.addWorkstations(WET_SIFTING, stack);
                          }
                        }));
  }

  private List<ExNihiloDisplay> buildSiftingDisplays(final boolean isWaterLogged) {
    CategoryIdentifier<ExNihiloDisplay> category = isWaterLogged ? WET_SIFTING : DRY_SIFTING;
    final Set<Ingredient> ingredients = new HashSet<>();
    ExNihiloRegistries.SIEVE_REGISTRY
        .getRecipeList()
        .forEach(
            recipe -> {
              final Ingredient recipeIngredient = recipe.getInput();
              if (ingredients.stream()
                  .noneMatch(
                      ingredient ->
                          IngredientUtils.areIngredientsEqual(ingredient, recipeIngredient))) {
                ingredients.add(recipeIngredient);
              }
            });

    return Arrays.stream(MeshType.values())
        .filter(enumMesh -> enumMesh != MeshType.NONE)
        .flatMap(
            enumMesh -> {
              final ItemStack mesh = new ItemStack(MeshItem.getMesh(enumMesh));
              return ingredients.stream()
                  .flatMap(
                      ingredient -> {
                        final List<SiftingRecipe> drops =
                            ExNihiloRegistries.SIEVE_REGISTRY.getDrops(
                                ingredient, enumMesh, isWaterLogged);
                        if (drops.isEmpty()) {
                          return Stream.empty();
                        }
                        return Lists.partition(drops, 21).stream()
                            .map(
                                partition -> {
                                  List<EntryIngredient> outputs =
                                      partition.stream()
                                          .map(
                                              siftingRecipe -> {
                                                EntryIngredient output =
                                                    EntryIngredients.of(siftingRecipe.getDrop());
                                                Multiset<String> condensedTooltips =
                                                    HashMultiset.create();
                                                for (SiftingRecipe dropEntry : drops) {
                                                  if (!ItemStack.isSameItem(
                                                      dropEntry.getDrop(),
                                                      siftingRecipe.getDrop())) {
                                                    continue;
                                                  }
                                                  dropEntry
                                                      .getRolls()
                                                      .forEach(
                                                          meshWithChance ->
                                                              condensedTooltips.add(
                                                                  StringUtils.formatPercent(
                                                                      meshWithChance.getChance())));
                                                }
                                                List<Component> tooltipLines = new ArrayList<>();
                                                tooltipLines.add(
                                                    Component.translatable("jei.sieve.dropChance"));
                                                for (String line : condensedTooltips.elementSet()) {
                                                  tooltipLines.add(
                                                      Component.literal(
                                                          " * "
                                                              + condensedTooltips.count(line)
                                                              + "x "
                                                              + line));
                                                }
                                                output.forEach(
                                                    stack -> applyTooltip(stack, tooltipLines));
                                                return output;
                                              })
                                          .toList();
                                  return new ExNihiloDisplay(
                                      category,
                                      List.of(
                                          EntryIngredients.of(mesh),
                                          EntryIngredients.ofItemStacks(
                                              Arrays.asList(ingredient.getItems()))),
                                      outputs);
                                });
                      });
            })
        .toList();
  }

  private List<JEICrucibleRecipe> getCrucibleRecipes() {
    return ExNihiloRegistries.CRUCIBLE_REGISTRY.getRecipeList().stream()
        .flatMap(
            crucibleRecipe -> {
              if (crucibleRecipe.getInput().getItems().length <= 21) {
                return Stream.of(
                    new JEICrucibleRecipe(
                        crucibleRecipe.getResultFluid().getAmount(),
                        crucibleRecipe.getCrucibleType(),
                        List.of(crucibleRecipe.getInput().getItems()),
                        crucibleRecipe.getResultFluid()));
              }
              final List<List<ItemStack>> partitions =
                  Lists.partition(List.of(crucibleRecipe.getInput().getItems()), 21);
              return partitions.stream()
                  .map(
                      partition ->
                          new JEICrucibleRecipe(
                              crucibleRecipe.getResultFluid().getAmount(),
                              crucibleRecipe.getCrucibleType(),
                              partition,
                              crucibleRecipe.getResultFluid()));
            })
        .toList();
  }

  private static ExNihiloDisplay toMeltingDisplay(
      CategoryIdentifier<ExNihiloDisplay> category, JEICrucibleRecipe recipe) {
    FluidStack fluid = recipe.getResultFluid().copy();
    if (fluid.getAmount() != 1000) {
      fluid.setAmount(1000);
    }
    List<Component> inputTooltip =
        List.of(
            Component.literal(
                String.format("Fluid Amount: %d mb", recipe.getResultFluid().getAmount())));
    List<EntryIngredient> inputs =
        recipe.getInputs().stream()
            .map(EntryIngredients::of)
            .peek(ingredient -> ingredient.forEach(stack -> applyTooltip(stack, inputTooltip)))
            .toList();
    EntryIngredient output = EntryIngredients.of(fluid.getFluid());
    return new ExNihiloDisplay(category, inputs, List.of(output));
  }

  private void applyCompostTooltip(EntryStack<?> stack) {
    if (!(stack.getValue() instanceof ItemStack itemStack)) {
      return;
    }
    int solidAmount = ExNihiloRegistries.COMPOST_REGISTRY.getSolidAmount(itemStack.getItem());
    applyTooltip(
        stack,
        List.of(
            Component.literal(
                String.format("Amount: %d / %d", solidAmount, Config.getBarrelMaxSolidAmount()))));
  }

  private static <T> void applyTooltip(EntryStack<T> stack, List<Component> extraLines) {
    stack.tooltip(
        entryStack ->
            extraLines.stream()
                .map(line -> (Component) line.copy().withStyle(ChatFormatting.GRAY))
                .toList());
  }

  private static EntryIngredient entryFromIngredient(Ingredient ingredient) {
    return EntryIngredients.ofItemStacks(Arrays.asList(ingredient.getItems()));
  }

  private static EntryIngredient entryFromFluid(FluidStack stack) {
    return EntryIngredients.of(stack.getFluid());
  }

  private static CategoryIdentifier<ExNihiloDisplay> category(String path) {
    return CategoryIdentifier.of(
        ResourceLocation.fromNamespaceAndPath(ExNihiloSequentia.MOD_ID, path));
  }

  private static ItemStack toHeatInput(HeatRecipe recipe) {
    Block input = recipe.getInputBlock();
    if (input == null) {
      return ItemStack.EMPTY;
    }
    if (input == Blocks.FIRE || input == Blocks.SOUL_FIRE) {
      return new ItemStack(Items.FLINT_AND_STEEL);
    }
    if (input instanceof LiquidBlock liquidBlock) {
      Fluid fluid = liquidBlock.defaultBlockState().getFluidState().getType();
      return new ItemStack(fluid.getBucket());
    }
    return new ItemStack(input.asItem());
  }

  private static String toHeatName(Block block) {
    if (block == Blocks.WALL_TORCH) {
      return "Wall Torch";
    }
    if (block == Blocks.REDSTONE_WALL_TORCH) {
      return "Redstone Wall Torch";
    }
    return block.getName().getString();
  }

  private static final class ExNihiloDisplay extends BasicDisplay {
    private final CategoryIdentifier<ExNihiloDisplay> category;
    private final String heatName;
    private final String heatMultiplier;

    private ExNihiloDisplay(
        CategoryIdentifier<ExNihiloDisplay> category,
        List<EntryIngredient> inputs,
        List<EntryIngredient> outputs) {
      this(category, inputs, outputs, null, null);
    }

    private ExNihiloDisplay(
        CategoryIdentifier<ExNihiloDisplay> category,
        List<EntryIngredient> inputs,
        List<EntryIngredient> outputs,
        String heatName,
        String heatMultiplier) {
      super(inputs, outputs, Optional.empty());
      this.category = category;
      this.heatName = heatName;
      this.heatMultiplier = heatMultiplier;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
      return category;
    }
  }

  private abstract static class BaseCategory implements DisplayCategory<ExNihiloDisplay> {
    private final CategoryIdentifier<ExNihiloDisplay> id;
    private final String titleKey;
    private final EntryStack<?> icon;
    private final int height;

    protected BaseCategory(
        CategoryIdentifier<ExNihiloDisplay> id,
        String titleKey,
        EntryStack<?> icon,
        int width,
        int height) {
      this.id = id;
      this.titleKey = titleKey;
      this.icon = icon;
      this.height = height;
    }

    @Override
    public CategoryIdentifier<? extends ExNihiloDisplay> getCategoryIdentifier() {
      return id;
    }

    @Override
    public Component getTitle() {
      return Component.translatable(titleKey);
    }

    @Override
    public Renderer getIcon() {
      return icon;
    }

    @Override
    public int getDisplayHeight() {
      return height;
    }

    protected static void addBackground(
        List<Widget> widgets,
        Rectangle bounds,
        ResourceLocation texture,
        int u,
        int v,
        int width,
        int height,
        int textureWidth,
        int textureHeight) {
      widgets.add(
          Widgets.createTexturedWidget(
              texture, bounds.x, bounds.y, u, v, width, height, textureWidth, textureHeight));
    }

    protected static Point at(Rectangle bounds, int x, int y) {
      return new Point(bounds.x + x, bounds.y + y);
    }
  }

  private static final class CompostCategory extends BaseCategory {
    private CompostCategory() {
      super(COMPOST, "jei.category.compost", EntryStacks.of(Items.DIRT), 166, 58);
    }

    @Override
    public List<Widget> setupDisplay(ExNihiloDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      addBackground(widgets, bounds, JEI_MID, 0, 168, 166, 58, 256, 256);
      widgets.add(
          Widgets.createSlot(at(bounds, 3, 21))
              .entries(display.getOutputEntries().getFirst())
              .markOutput());
      List<EntryIngredient> inputs = display.getInputEntries();
      if (inputs.size() == 1) {
        widgets.add(Widgets.createSlot(at(bounds, 39, 3)).entries(inputs.getFirst()).markInput());
      } else {
        for (int i = 0; i < inputs.size(); i++) {
          int slotX = 39 + (i % 7 * 18);
          int slotY = 3 + i / 7 * 18;
          widgets.add(
              Widgets.createSlot(at(bounds, slotX, slotY)).entries(inputs.get(i)).markInput());
        }
      }
      return widgets;
    }
  }

  private static final class CrushingCategory extends BaseCategory {
    private CrushingCategory() {
      super(
          CRUSHING,
          "jei.category.crushing",
          EntryStacks.of(EXNItems.HAMMER_STONE.itemStack()),
          166,
          58);
    }

    @Override
    public List<Widget> setupDisplay(ExNihiloDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      addBackground(widgets, bounds, JEI_MID, 0, 56, 166, 58, 256, 256);
      widgets.add(
          Widgets.createSlot(at(bounds, 11, 39))
              .entries(display.getInputEntries().getFirst())
              .markInput());
      for (int i = 0; i < display.getOutputEntries().size(); i++) {
        int slotX = 39 + (i % 7 * 18);
        int slotY = 3 + i / 7 * 18;
        widgets.add(
            Widgets.createSlot(at(bounds, slotX, slotY))
                .entries(display.getOutputEntries().get(i))
                .markOutput());
      }
      return widgets;
    }
  }

  private static final class HarvestCategory extends BaseCategory {
    private HarvestCategory() {
      super(
          HARVEST,
          "jei.category.harvest",
          EntryStacks.of(EXNItems.CROOK_WOOD.itemStack()),
          166,
          58);
    }

    @Override
    public List<Widget> setupDisplay(ExNihiloDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      addBackground(widgets, bounds, JEI_MID, 0, 112, 166, 58, 256, 256);
      widgets.add(
          Widgets.createSlot(at(bounds, 11, 39))
              .entries(display.getInputEntries().getFirst())
              .markInput());
      for (int i = 0; i < display.getOutputEntries().size(); i++) {
        int slotX = 39 + (i % 7 * 18);
        int slotY = 3 + i / 7 * 18;
        widgets.add(
            Widgets.createSlot(at(bounds, slotX, slotY))
                .entries(display.getOutputEntries().get(i))
                .markOutput());
      }
      return widgets;
    }
  }

  private static final class HeatCategory extends BaseCategory {
    private HeatCategory() {
      super(HEAT, "jei.category.heat", EntryStacks.of(Items.BLAZE_POWDER), 166, 58);
    }

    @Override
    public List<Widget> setupDisplay(ExNihiloDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      int contentHeight = (9 * 2) + 2 + 3 + 18;
      int contentTop = bounds.y + ((bounds.height - contentHeight) / 2);
      int lineTwoY = contentTop + 11;
      int slotX = bounds.x + ((bounds.width - 18) / 2);
      int slotY = contentTop + 23;
      int centerX = bounds.x + (bounds.width / 2);
      widgets.add(
          Widgets.createTexturedWidget(JEI_SINGLE_SLOT, slotX, slotY, 0, 0, 18, 18, 18, 18));
      if (display.heatName != null) {
        Component name = Component.literal(display.heatName).withStyle(ChatFormatting.DARK_GRAY);
        widgets.add(Widgets.createLabel(new Point(centerX, contentTop), name).centered());
      }
      if (display.heatMultiplier != null) {
        Component multiplier =
            Component.literal(display.heatMultiplier).withStyle(ChatFormatting.WHITE);
        widgets.add(Widgets.createLabel(new Point(centerX, lineTwoY), multiplier).centered());
      }
      widgets.add(
          Widgets.createSlot(new Point(slotX + 1, slotY + 1))
              .entries(display.getInputEntries().getFirst())
              .markInput());
      return widgets;
    }
  }

  private static final class MeltingCategory extends BaseCategory {
    private MeltingCategory(CategoryIdentifier<ExNihiloDisplay> id, String titleKey) {
      super(id, titleKey, EntryStacks.of(EXNBlocks.OAK_CRUCIBLE.asItem()), 166, 58);
    }

    @Override
    public List<Widget> setupDisplay(ExNihiloDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      addBackground(widgets, bounds, JEI_MID, 0, 168, 166, 58, 256, 256);
      widgets.add(
          Widgets.createSlot(at(bounds, 3, 21))
              .entries(display.getOutputEntries().getFirst())
              .markOutput());
      for (int i = 0; i < display.getInputEntries().size(); i++) {
        int slotX = 39 + (i % 7 * 18);
        int slotY = 3 + i / 7 * 18;
        widgets.add(
            Widgets.createSlot(at(bounds, slotX, slotY))
                .entries(display.getInputEntries().get(i))
                .markInput());
      }
      return widgets;
    }
  }

  private static final class SiftingCategory extends BaseCategory {
    private SiftingCategory(CategoryIdentifier<ExNihiloDisplay> id, boolean wet) {
      super(
          id,
          wet ? "jei.category.wet_sifting" : "jei.category.dry_sifting",
          EntryStacks.of(EXNBlocks.OAK_SIEVE.asItem()),
          166,
          58);
    }

    @Override
    public List<Widget> setupDisplay(ExNihiloDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      addBackground(widgets, bounds, JEI_MID, 0, 0, 166, 58, 256, 256);
      widgets.add(
          Widgets.createSlot(at(bounds, 11, 39))
              .entries(display.getInputEntries().get(0))
              .markInput());
      widgets.add(
          Widgets.createSlot(at(bounds, 11, 3))
              .entries(display.getInputEntries().get(1))
              .markInput());
      for (int i = 0; i < display.getOutputEntries().size(); i++) {
        int slotX = 39 + (i % 7 * 18);
        int slotY = 3 + i / 7 * 18;
        widgets.add(
            Widgets.createSlot(at(bounds, slotX, slotY))
                .entries(display.getOutputEntries().get(i))
                .markOutput());
      }
      return widgets;
    }
  }

  private static final class PrecipitateCategory extends BaseCategory {
    private PrecipitateCategory() {
      super(
          PRECIPITATE,
          "jei.category.precipitate",
          EntryStacks.of(EXNBlocks.OAK_BARREL.asItem()),
          166,
          63);
    }

    @Override
    public List<Widget> setupDisplay(ExNihiloDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      addBackground(widgets, bounds, JEI_FLUID_BLOCK_TRANSFORM, 0, 0, 166, 63, 256, 256);
      widgets.add(
          Widgets.createSlot(at(bounds, 48, 37))
              .entries(display.getInputEntries().get(0))
              .markInput());
      widgets.add(
          Widgets.createSlot(at(bounds, 75, 10))
              .entries(display.getInputEntries().get(1))
              .markInput());
      widgets.add(
          Widgets.createSlot(at(bounds, 102, 37))
              .entries(display.getOutputEntries().getFirst())
              .markOutput());
      return widgets;
    }
  }

  private static final class SolidifyingCategory extends BaseCategory {
    private SolidifyingCategory() {
      super(
          SOLIDIFYING,
          "jei.category.solidifying",
          EntryStacks.of(EXNBlocks.OAK_BARREL.asItem()),
          166,
          63);
    }

    @Override
    public List<Widget> setupDisplay(ExNihiloDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      addBackground(widgets, bounds, JEI_FLUID_ON_TOP, 0, 0, 166, 63, 256, 256);
      widgets.add(
          Widgets.createSlot(at(bounds, 48, 37))
              .entries(display.getInputEntries().get(0))
              .markInput());
      widgets.add(
          Widgets.createSlot(at(bounds, 75, 10))
              .entries(display.getInputEntries().get(1))
              .markInput());
      widgets.add(
          Widgets.createSlot(at(bounds, 102, 37))
              .entries(display.getOutputEntries().getFirst())
              .markOutput());
      return widgets;
    }
  }

  private static final class TransitionCategory extends BaseCategory {
    private TransitionCategory() {
      super(
          TRANSITION,
          "jei.category.transition",
          EntryStacks.of(EXNBlocks.OAK_BARREL.asItem()),
          166,
          63);
    }

    @Override
    public List<Widget> setupDisplay(ExNihiloDisplay display, Rectangle bounds) {
      List<Widget> widgets = new ArrayList<>();
      addBackground(widgets, bounds, JEI_FLUID_TRANSFORM, 0, 0, 166, 63, 256, 256);

      widgets.add(
          Widgets.createSlot(at(bounds, 48, 10))
              .entries(display.getInputEntries().get(0))
              .markInput());
      widgets.add(
          Widgets.createSlot(at(bounds, 75, 37))
              .entries(display.getInputEntries().get(1))
              .markInput());
      widgets.add(
          Widgets.createSlot(at(bounds, 102, 10))
              .entries(display.getOutputEntries().getFirst())
              .markOutput());
      return widgets;
    }
  }
}
