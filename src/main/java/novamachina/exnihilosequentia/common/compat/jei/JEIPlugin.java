package novamachina.exnihilosequentia.common.compat.jei;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import novamachina.exnihilosequentia.common.compat.jei.compost.CompostRecipeCategory;
import novamachina.exnihilosequentia.common.compat.jei.crushing.CrushingRecipeCategory;
import novamachina.exnihilosequentia.common.compat.jei.harvest.HarvestRecipeCategory;
import novamachina.exnihilosequentia.common.compat.jei.heat.HeatRecipeCategory;
import novamachina.exnihilosequentia.common.compat.jei.melting.JEICrucibleRecipe;
import novamachina.exnihilosequentia.common.compat.jei.melting.MeltingRecipeCategory;
import novamachina.exnihilosequentia.common.compat.jei.precipitate.PrecipitateRecipeCategory;
import novamachina.exnihilosequentia.common.compat.jei.sifting.DrySieveRecipeCategory;
import novamachina.exnihilosequentia.common.compat.jei.sifting.JEISieveRecipe;
import novamachina.exnihilosequentia.common.compat.jei.sifting.WetSiftingRecipeCategory;
import novamachina.exnihilosequentia.common.compat.jei.solidifying.SolidifyingRecipeCategory;
import novamachina.exnihilosequentia.common.compat.jei.transition.TransitionRecipeCategory;
import novamachina.exnihilosequentia.common.registries.ExNihiloRegistries;
import novamachina.exnihilosequentia.common.utility.ExNihiloConstants;
import novamachina.exnihilosequentia.world.item.CrookItem;
import novamachina.exnihilosequentia.world.item.EXNItems;
import novamachina.exnihilosequentia.world.item.HammerItem;
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
import novamachina.exnihilosequentia.world.level.block.BarrelBlock;
import novamachina.exnihilosequentia.world.level.block.CrucibleBlock;
import novamachina.exnihilosequentia.world.level.block.EXNBlocks;
import novamachina.exnihilosequentia.world.level.block.SieveBlock;
import novamachina.novacore.util.IngredientUtils;
import novamachina.novacore.world.item.ItemDefinition;
import novamachina.novacore.world.level.block.BlockDefinition;
import org.slf4j.Logger;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(JEIPlugin.class);

  @Nonnull
  private static final ResourceLocation CRUCIBLES =
      ResourceLocation.fromNamespaceAndPath(
          ExNihiloConstants.ModIds.EX_NIHILO_SEQUENTIA, ExNihiloConstants.Blocks.CRUCIBLES);

  @Nonnull
  private static final ResourceLocation FIRED_CRUCIBLES =
      ResourceLocation.fromNamespaceAndPath(
          ExNihiloConstants.ModIds.EX_NIHILO_SEQUENTIA, ExNihiloConstants.Blocks.FIRED_CRUCIBLE);

  @Nonnull
  @Override
  public ResourceLocation getPluginUid() {
    return ResourceLocation.fromNamespaceAndPath(
        ExNihiloConstants.ModIds.JEI, ExNihiloConstants.ModIds.EX_NIHILO_SEQUENTIA);
  }

  @Override
  public void registerCategories(@Nonnull final IRecipeCategoryRegistration registration) {
    IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

    registration.addRecipeCategories(new HarvestRecipeCategory(guiHelper));
    registration.addRecipeCategories(new DrySieveRecipeCategory(guiHelper));
    registration.addRecipeCategories(new WetSiftingRecipeCategory(guiHelper));
    registration.addRecipeCategories(new CrushingRecipeCategory(guiHelper));
    registration.addRecipeCategories(new SolidifyingRecipeCategory(guiHelper));
    registration.addRecipeCategories(new TransitionRecipeCategory(guiHelper));
    registration.addRecipeCategories(new PrecipitateRecipeCategory(guiHelper));
    registration.addRecipeCategories(new CompostRecipeCategory(guiHelper));
    registration.addRecipeCategories(new MeltingRecipeCategory(guiHelper, "melting"));
    registration.addRecipeCategories(new MeltingRecipeCategory(guiHelper, "fired_melting"));
    registration.addRecipeCategories(new HeatRecipeCategory(guiHelper));
  }

  @Override
  public void registerRecipeCatalysts(@Nonnull final IRecipeCatalystRegistration registration) {
    registerCrushingCatalyst(registration);
    registerHarvestCatalyst(registration);
    registerCrucibles(registration);
    registerBarrels(registration);
    registerSieves(registration);
  }

  private void registerHarvestCatalyst(@Nonnull final IRecipeCatalystRegistration registration) {
    Set<ItemDefinition<CrookItem>> crooks =
        Set.of(
            EXNItems.CROOK_ANDESITE,
            EXNItems.CROOK_BAMBOO,
            EXNItems.CROOK_BASALT,
            EXNItems.CROOK_BLACKSTONE,
            EXNItems.CROOK_BONE,
            EXNItems.CROOK_CALCITE,
            EXNItems.CROOK_CHERRY,
            EXNItems.CROOK_COPPER,
            EXNItems.CROOK_DEEPSLATE,
            EXNItems.CROOK_DIAMOND,
            EXNItems.CROOK_DIORITE,
            EXNItems.CROOK_DRIPSTONE,
            EXNItems.CROOK_GOLD,
            EXNItems.CROOK_GRANITE,
            EXNItems.CROOK_IRON,
            EXNItems.CROOK_NETHER_BRICK,
            EXNItems.CROOK_NETHERITE,
            EXNItems.CROOK_RED_NETHER_BRICK,
            EXNItems.CROOK_STONE,
            EXNItems.CROOK_TERRACOTTA,
            EXNItems.CROOK_TUFF,
            EXNItems.CROOK_WOOD);
    for (ItemDefinition<CrookItem> crook : crooks) {
      registration.addRecipeCatalyst(crook.itemStack(), RecipeTypes.HARVEST);
    }
  }

  private void registerCrushingCatalyst(@Nonnull final IRecipeCatalystRegistration registration) {
    Set<ItemDefinition<HammerItem>> hammers =
        Set.of(
            EXNItems.HAMMER_ANDESITE,
            EXNItems.HAMMER_BAMBOO,
            EXNItems.HAMMER_BASALT,
            EXNItems.HAMMER_BLACKSTONE,
            EXNItems.HAMMER_BONE,
            EXNItems.HAMMER_CALCITE,
            EXNItems.HAMMER_CHERRY,
            EXNItems.HAMMER_COPPER,
            EXNItems.HAMMER_DEEPSLATE,
            EXNItems.HAMMER_DIAMOND,
            EXNItems.HAMMER_DIORITE,
            EXNItems.HAMMER_DRIPSTONE,
            EXNItems.HAMMER_GOLD,
            EXNItems.HAMMER_GRANITE,
            EXNItems.HAMMER_IRON,
            EXNItems.HAMMER_NETHER_BRICK,
            EXNItems.HAMMER_NETHERITE,
            EXNItems.HAMMER_RED_NETHER_BRICK,
            EXNItems.HAMMER_STONE,
            EXNItems.HAMMER_TERRACOTTA,
            EXNItems.HAMMER_TUFF,
            EXNItems.HAMMER_WOOD);

    for (ItemDefinition<HammerItem> hammer : hammers) {
      registration.addRecipeCatalyst(hammer.itemStack(), RecipeTypes.CRUSHING);
    }
  }

  private void registerCrucibles(@Nonnull final IRecipeCatalystRegistration registration) {
    List<BlockDefinition<CrucibleBlock>> nonFiredCrucibles =
        List.of(
            EXNBlocks.ACACIA_CRUCIBLE,
            EXNBlocks.BAMBOO_CRUCIBLE,
            EXNBlocks.BIRCH_CRUCIBLE,
            EXNBlocks.CHERRY_CRUCIBLE,
            EXNBlocks.DARK_OAK_CRUCIBLE,
            EXNBlocks.JUNGLE_CRUCIBLE,
            EXNBlocks.MANGROVE_CRUCIBLE,
            EXNBlocks.OAK_CRUCIBLE,
            EXNBlocks.SPRUCE_CRUCIBLE);
    for (BlockDefinition<CrucibleBlock> blockDefinition : nonFiredCrucibles) {
      registration.addRecipeCatalyst(
          blockDefinition.itemStack(), RecipeTypes.MELTING, RecipeTypes.HEAT);
    }

    List<BlockDefinition<CrucibleBlock>> firedCrucibles =
        List.of(EXNBlocks.FIRED_CRUCIBLE, EXNBlocks.CRIMSON_CRUCIBLE, EXNBlocks.WARPED_CRUCIBLE);
    for (BlockDefinition<CrucibleBlock> blockDefinition : firedCrucibles) {
      registration.addRecipeCatalyst(
          blockDefinition.itemStack(),
          RecipeTypes.FIRED_MELTING,
          RecipeTypes.MELTING,
          RecipeTypes.HEAT);
    }
  }

  private void registerBarrels(@Nonnull final IRecipeCatalystRegistration registration) {

    List<BlockDefinition<BarrelBlock>> barrels =
        List.of(
            EXNBlocks.ACACIA_BARREL,
            EXNBlocks.BAMBOO_BARREL,
            EXNBlocks.BIRCH_BARREL,
            EXNBlocks.CHERRY_BARREL,
            EXNBlocks.DARK_OAK_BARREL,
            EXNBlocks.JUNGLE_BARREL,
            EXNBlocks.MANGROVE_BARREL,
            EXNBlocks.OAK_BARREL,
            EXNBlocks.SPRUCE_BARREL,
            EXNBlocks.STONE_BARREL,
            EXNBlocks.CRIMSON_BARREL,
            EXNBlocks.WARPED_BARREL);
    for (BlockDefinition<BarrelBlock> blockDefinition : barrels) {
      registration.addRecipeCatalyst(
          blockDefinition.itemStack(),
          RecipeTypes.SOLIDIFYING,
          RecipeTypes.TRANSITION,
          RecipeTypes.PRECIPITATE,
          RecipeTypes.COMPOST);
    }
  }

  private void registerSieves(@Nonnull final IRecipeCatalystRegistration registration) {
    List<BlockDefinition<SieveBlock>> sieves =
        List.of(
            EXNBlocks.ACACIA_SIEVE,
            EXNBlocks.BAMBOO_SIEVE,
            EXNBlocks.BIRCH_SIEVE,
            EXNBlocks.CHERRY_SIEVE,
            EXNBlocks.DARK_OAK_SIEVE,
            EXNBlocks.JUNGLE_SIEVE,
            EXNBlocks.MANGROVE_SIEVE,
            EXNBlocks.OAK_SIEVE,
            EXNBlocks.SPRUCE_SIEVE,
            EXNBlocks.CRIMSON_SIEVE,
            EXNBlocks.WARPED_SIEVE);
    for (BlockDefinition<SieveBlock> blockDefinition : sieves) {
      registration.addRecipeCatalyst(
          blockDefinition.itemStack(), RecipeTypes.DRY_SIFTING, RecipeTypes.WET_SIFTING);
    }
  }

  @Override
  public void registerRecipes(@Nonnull final IRecipeRegistration registration) {
    registerHarvest(registration);
    registerSifting(registration);
    registerCrushing(registration);
    registerSolidifying(registration);
    registerTransition(registration);
    registerPrecipitate(registration);
    registerCompost(registration);
    registerFiredMelting(registration);
    registerMelting(registration);
    registerHeat(registration);
  }

  private void registerCompost(@Nonnull final IRecipeRegistration registration) {
    @Nonnull
    final List<CompostRecipe> recipes = ExNihiloRegistries.COMPOST_REGISTRY.getRecipeList();
    registration.addRecipes(RecipeTypes.COMPOST, recipes);
    log.info("Compost Recipes Loaded: {}", recipes.size());
  }

  private void registerHarvest(@Nonnull final IRecipeRegistration registration) {
    @Nonnull
    final List<HarvestRecipe> harvestRecipes = ExNihiloRegistries.CROOK_REGISTRY.getRecipeList();
    registration.addRecipes(RecipeTypes.HARVEST, harvestRecipes);
    log.info("Harvest Recipes Loaded: {}", harvestRecipes.size());
  }

  private void registerFiredMelting(@Nonnull final IRecipeRegistration registration) {
    @Nonnull final List<JEICrucibleRecipe> recipes = getCrucibleRecipes();
    registration.addRecipes(RecipeTypes.FIRED_MELTING, recipes);
    log.info("Fired Melting Recipes Loaded: {}", recipes.size());
  }

  private void registerPrecipitate(@Nonnull final IRecipeRegistration registration) {
    @Nonnull
    final List<PrecipitateRecipe> recipes = ExNihiloRegistries.FLUID_BLOCK_REGISTRY.getRecipeList();
    registration.addRecipes(RecipeTypes.PRECIPITATE, recipes);
    log.info("Precipitate Recipes Loaded: {}", recipes.size());
  }

  private void registerSolidifying(@Nonnull final IRecipeRegistration registration) {
    @Nonnull
    final List<SolidifyingRecipe> recipes =
        ExNihiloRegistries.FLUID_ON_TOP_REGISTRY.getRecipeList();
    registration.addRecipes(RecipeTypes.SOLIDIFYING, recipes);
    log.info("Solidifying Recipes Loaded: {}", recipes.size());
  }

  private void registerTransition(@Nonnull final IRecipeRegistration registration) {
    @Nonnull
    final List<TransitionRecipe> recipes =
        ExNihiloRegistries.FLUID_TRANSFORM_REGISTRY.getRecipeList();
    registration.addRecipes(RecipeTypes.TRANSITION, recipes);
    log.info("Transition Recipes Loaded: {}", recipes.size());
  }

  private void registerCrushing(@Nonnull final IRecipeRegistration registration) {
    @Nonnull
    final List<CrushingRecipe> recipes = ExNihiloRegistries.HAMMER_REGISTRY.getRecipeList();
    registration.addRecipes(RecipeTypes.CRUSHING, recipes);
    log.info("Crushing Recipes Loaded: {}", recipes.size());
  }

  private void registerHeat(@Nonnull final IRecipeRegistration registration) {
    @Nonnull final List<HeatRecipe> recipes = ExNihiloRegistries.HEAT_REGISTRY.getRecipeList();
    registration.addRecipes(RecipeTypes.HEAT, recipes);
    log.info("Heat Recipes Loaded: {}", recipes.size());
  }

  private List<JEISieveRecipe> getSiftingRecipes(final boolean isWaterLogged) {
    @Nonnull final Set<Ingredient> ingredients = new HashSet<>();
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
                        final List<List<ItemStack>> input =
                            new ArrayList<>(
                                Arrays.asList(
                                    Collections.singletonList(mesh),
                                    Arrays.asList(ingredient.getItems())));
                        return Lists.partition(drops, 21).stream()
                            .map(results -> new JEISieveRecipe(input, results));
                      });
            })
        .toList();
  }

  public List<JEICrucibleRecipe> getCrucibleRecipes() {
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
              @Nonnull
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

  private void registerSifting(@Nonnull final IRecipeRegistration registration) {
    @Nonnull final List<JEISieveRecipe> drySieveRecipes = getSiftingRecipes(false);
    @Nonnull final List<JEISieveRecipe> wetSieveRecipes = getSiftingRecipes(true);
    registration.addRecipes(RecipeTypes.DRY_SIFTING, drySieveRecipes);
    registration.addRecipes(RecipeTypes.WET_SIFTING, wetSieveRecipes);
    log.info("Sifting Recipes Loaded: {}", (drySieveRecipes.size() + wetSieveRecipes.size()));
  }

  private void registerMelting(@Nonnull final IRecipeRegistration registration) {
    @Nonnull final List<JEICrucibleRecipe> recipes = getCrucibleRecipes();
    registration.addRecipes(RecipeTypes.MELTING, recipes);
    log.info("Melting Recipes Loaded: {}", recipes.size());
  }
}
