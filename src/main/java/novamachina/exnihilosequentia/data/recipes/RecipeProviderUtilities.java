package novamachina.exnihilosequentia.data.recipes;

import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;

public class RecipeProviderUtilities {

  public static ResourceLocation createSaveLocation(@Nonnull final ResourceLocation location) {
    return ResourceLocation.fromNamespaceAndPath(
        location.getNamespace(), prependRecipePrefix(location.getPath()));
  }

  // This is for 1.21.3+
  // public static ResourceKey<Recipe<?>> createSaveLocation(@Nonnull final ResourceLocation
  // location) {
  //   ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(location.getNamespace(),
  // prependRecipePrefix(location.getPath()));
  //   return ResourceKey.create(Registries.RECIPE, rl);
  // }

  public static String prependRecipePrefix(@Nonnull final String id) {
    return String.format("ens_%s", id);
  }
}
