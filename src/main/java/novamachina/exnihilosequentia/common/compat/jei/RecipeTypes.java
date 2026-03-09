package novamachina.exnihilosequentia.common.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import novamachina.exnihilosequentia.ExNihiloSequentia;
import novamachina.exnihilosequentia.common.compat.jei.melting.JEICrucibleRecipe;
import novamachina.exnihilosequentia.common.compat.jei.sifting.JEISieveRecipe;
import novamachina.exnihilosequentia.common.utility.ExNihiloConstants;
import novamachina.exnihilosequentia.world.item.crafting.CompostRecipe;
import novamachina.exnihilosequentia.world.item.crafting.CrushingRecipe;
import novamachina.exnihilosequentia.world.item.crafting.HarvestRecipe;
import novamachina.exnihilosequentia.world.item.crafting.HeatRecipe;
import novamachina.exnihilosequentia.world.item.crafting.PrecipitateRecipe;
import novamachina.exnihilosequentia.world.item.crafting.SolidifyingRecipe;
import novamachina.exnihilosequentia.world.item.crafting.TransitionRecipe;

public class RecipeTypes {

  public static final RecipeType<CompostRecipe> COMPOST =
      RecipeType.create(ExNihiloSequentia.MOD_ID, "compost", CompostRecipe.class);
  public static final RecipeType<CrushingRecipe> CRUSHING =
      RecipeType.create(ExNihiloSequentia.MOD_ID, "crushing", CrushingRecipe.class);
  public static final RecipeType<HarvestRecipe> HARVEST =
      RecipeType.create(ExNihiloSequentia.MOD_ID, "harvest", HarvestRecipe.class);
  public static final RecipeType<HeatRecipe> HEAT =
      RecipeType.create(ExNihiloSequentia.MOD_ID, "heat", HeatRecipe.class);
  public static final RecipeType<JEICrucibleRecipe> MELTING =
      RecipeType.create(
          ExNihiloSequentia.MOD_ID, ExNihiloConstants.Blocks.CRUCIBLES, JEICrucibleRecipe.class);
  public static final RecipeType<JEICrucibleRecipe> FIRED_MELTING =
      RecipeType.create(
          ExNihiloSequentia.MOD_ID,
          ExNihiloConstants.Blocks.FIRED_CRUCIBLE,
          JEICrucibleRecipe.class);
  public static final RecipeType<PrecipitateRecipe> PRECIPITATE =
      RecipeType.create(ExNihiloSequentia.MOD_ID, "precipitate", PrecipitateRecipe.class);
  public static final RecipeType<JEISieveRecipe> DRY_SIFTING =
      RecipeType.create(ExNihiloSequentia.MOD_ID, "dry_sifting", JEISieveRecipe.class);
  public static final RecipeType<JEISieveRecipe> WET_SIFTING =
      RecipeType.create(ExNihiloSequentia.MOD_ID, "wet_sifting", JEISieveRecipe.class);
  public static final RecipeType<TransitionRecipe> TRANSITION =
      RecipeType.create(ExNihiloSequentia.MOD_ID, "transition", TransitionRecipe.class);
  public static final RecipeType<SolidifyingRecipe> SOLIDIFYING =
      RecipeType.create(ExNihiloSequentia.MOD_ID, "solidifying", SolidifyingRecipe.class);

  private RecipeTypes() {}
}
