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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
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
import novamachina.novacore.util.IngredientUtils;
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
    registerHeatCatalysts(registration);
  }

  // ...existing code...

  private void registerHeatCatalysts(@Nonnull final IRecipeCatalystRegistration registration) {
    // Register all heat blocks that have recipes
    List<HeatRecipe> heatRecipes = ExNihiloRegistries.HEAT_REGISTRY.getRecipeList();
    Set<Block> heatBlocks = new HashSet<>();

    for (HeatRecipe recipe : heatRecipes) {
      Block block = recipe.getInputBlock();
      if (block != null && !heatBlocks.contains(block)) {
        ItemStack catalyst = new ItemStack(block.asItem());
        // Only register if the item is valid (not Air/empty)
        if (!catalyst.isEmpty()) {
          registration.addRecipeCatalyst(catalyst, RecipeTypes.HEAT);
          heatBlocks.add(block);
        }
      }
    }
  }

  private void registerHarvestCatalyst(@Nonnull final IRecipeCatalystRegistration registration) {
    TagKey<Item> crookTag =
        TagKey.create(
            BuiltInRegistries.ITEM.key(),
            ResourceLocation.fromNamespaceAndPath(
                ExNihiloConstants.ModIds.EX_NIHILO_SEQUENTIA, "crook"));

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
                            registration.addRecipeCatalyst(catalyst, RecipeTypes.HARVEST);
                          }
                        }));
  }

  private void registerCrushingCatalyst(@Nonnull final IRecipeCatalystRegistration registration) {
    TagKey<Item> hammerTag =
        TagKey.create(
            BuiltInRegistries.ITEM.key(),
            ResourceLocation.fromNamespaceAndPath(
                ExNihiloConstants.ModIds.EX_NIHILO_SEQUENTIA, "hammer"));

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
                            registration.addRecipeCatalyst(catalyst, RecipeTypes.CRUSHING);
                          }
                        }));
  }

  private void registerCrucibles(@Nonnull final IRecipeCatalystRegistration registration) {
    TagKey<Item> cruciblesTag =
        TagKey.create(
            BuiltInRegistries.ITEM.key(),
            ResourceLocation.fromNamespaceAndPath(
                ExNihiloConstants.ModIds.EX_NIHILO_SEQUENTIA, "crucibles"));

    // Get all items in the crucibles tag
    BuiltInRegistries.ITEM
        .getTag(cruciblesTag)
        .ifPresent(
            tag ->
                tag.stream()
                    .forEach(
                        holder -> {
                          ItemStack catalyst = new ItemStack(holder.value());
                          if (!catalyst.isEmpty()) {
                            // Check if it's a fired crucible (stone/crimson/warped)
                            String itemName =
                                BuiltInRegistries.ITEM.getKey(holder.value()).toString();
                            if (itemName.contains("fired")
                                || itemName.contains("crimson")
                                || itemName.contains("warped")) {
                              registration.addRecipeCatalyst(
                                  catalyst,
                                  RecipeTypes.FIRED_MELTING,
                                  RecipeTypes.MELTING,
                                  RecipeTypes.HEAT);
                            } else {
                              // Wood crucibles
                              registration.addRecipeCatalyst(
                                  catalyst, RecipeTypes.MELTING, RecipeTypes.HEAT);
                            }
                          }
                        }));
  }

  private void registerBarrels(@Nonnull final IRecipeCatalystRegistration registration) {
    TagKey<Item> barrelsTag =
        TagKey.create(
            BuiltInRegistries.ITEM.key(),
            ResourceLocation.fromNamespaceAndPath(
                ExNihiloConstants.ModIds.EX_NIHILO_SEQUENTIA, "barrels"));

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
                            registration.addRecipeCatalyst(
                                catalyst,
                                RecipeTypes.SOLIDIFYING,
                                RecipeTypes.TRANSITION,
                                RecipeTypes.PRECIPITATE,
                                RecipeTypes.COMPOST);
                          }
                        }));
  }

  private void registerSieves(@Nonnull final IRecipeCatalystRegistration registration) {
    TagKey<Item> sievesTag =
        TagKey.create(
            BuiltInRegistries.ITEM.key(),
            ResourceLocation.fromNamespaceAndPath(
                ExNihiloConstants.ModIds.EX_NIHILO_SEQUENTIA, "sieves"));

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
                            registration.addRecipeCatalyst(
                                catalyst, RecipeTypes.DRY_SIFTING, RecipeTypes.WET_SIFTING);
                          }
                        }));
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
