package novamachina.exnihilosequentia.data.recipes;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import novamachina.exnihilosequentia.world.item.crafting.EXNRecipeSerializers;
import novamachina.exnihilosequentia.world.item.crafting.HarvestRecipe;
import novamachina.exnihilosequentia.world.item.crafting.ItemStackWithChance;
import novamachina.novacore.data.recipes.RecipeBuilder;

public class HarvestRecipeBuilder extends RecipeBuilder<HarvestRecipe> {

  private final Ingredient input;
  private List<ItemStackWithChance> drops;

  protected HarvestRecipeBuilder(Ingredient input, ItemStackWithChance... drops) {
    super(EXNRecipeSerializers.HARVEST_RECIPE_SERIALIZER.recipeSerializer());
    this.input = input;
    this.drops = Lists.newArrayList(drops);
  }

  public static HarvestRecipeBuilder harvest(ItemLike inputItem, ItemStackWithChance... drops) {
    return harvest(Ingredient.of(inputItem), drops);
  }

  public static HarvestRecipeBuilder harvest(TagKey<Item> inputTag, ItemStackWithChance... drops) {
    // This is for 1.21.3+
    // public static HarvestRecipeBuilder harvest(HolderGetter.Provider provider, TagKey<Item>
    // inputTag, ItemStackWithChance... drops) {
    return harvest(Ingredient.of(inputTag), drops);
    // This is for 1.21.3+
    // return
    // harvest(Ingredient.of(provider.lookupOrThrow(BuiltInRegistries.ITEM.key()).getOrThrow(inputTag)), drops);
  }

  public static HarvestRecipeBuilder harvest(Ingredient input, ItemStackWithChance... drops) {

    return new HarvestRecipeBuilder(input, drops);
  }

  @Override
  protected HarvestRecipe getRecipe(ResourceLocation id) {
    // This is for 1.21.3+
    // protected HarvestRecipe getRecipe(ResourceKey<Recipe<?>> id) {
    return new HarvestRecipe(input, drops);
  }

  @Override
  protected void validate(ResourceLocation id) {
    // This is for 1.21.3+
    // protected void validate(ResourceKey<Recipe<?>> id) {
    Preconditions.checkNotNull(input, "Input cannot be null.");
    Preconditions.checkArgument(!drops.isEmpty(), "Recipe needs at least one drop.");
  }

  public HarvestRecipeBuilder addDrop(ItemStack stack, float chance) {
    return addDrop(stack, 1, chance);
  }

  public HarvestRecipeBuilder addDrop(ItemStack stack, int count, float chance) {
    this.drops.add(ItemStackWithChance.of(stack, count, chance));
    return this;
  }
}
