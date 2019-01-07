package com.codetaylor.mc.pyrotech.modules.pyrotech.recipe;

import com.codetaylor.mc.athenaeum.recipe.IRecipeSingleOutput;
import com.codetaylor.mc.athenaeum.util.RecipeHelper;
import com.codetaylor.mc.pyrotech.modules.pyrotech.ModulePyrotechRegistries;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampfireRecipe
    extends IForgeRegistryEntry.Impl<CampfireRecipe>
    implements IRecipeSingleOutput {

  private static final CampfireRecipe NULL = new CampfireRecipe(ItemStack.EMPTY, Ingredient.EMPTY);

  private static final Map<String, CampfireRecipe> SMELTING_RECIPES = new HashMap<>();
  private static final List<Ingredient> WHITELIST = new ArrayList<>();
  private static final List<Ingredient> BLACKLIST = new ArrayList<>();

  @Nullable
  public static CampfireRecipe getRecipe(ItemStack input) {

    String key = CampfireRecipe.getRecipeKey(input);

    CampfireRecipe result = SMELTING_RECIPES.get(key);

    // If the recipe is cached, return it.

    if (result != null) {
      return result;
    }

    // If the recipe has a smelting output that is food, check against the
    // lists, cache the result and return it.

    if (RecipeHelper.hasFurnaceFoodRecipe(input)) {

      FurnaceRecipes furnaceRecipes = FurnaceRecipes.instance();
      ItemStack output = furnaceRecipes.getSmeltingResult(input);

      if (CampfireRecipe.hasWhitelist()
          && CampfireRecipe.isWhitelisted(output)) {
        result = new CampfireRecipe(output, Ingredient.fromStacks(input));
        SMELTING_RECIPES.put(key, result);
        return result;

      } else if (CampfireRecipe.hasBlacklist()
          && !CampfireRecipe.isBlacklisted(output)) {
        result = new CampfireRecipe(output, Ingredient.fromStacks(input));
        SMELTING_RECIPES.put(key, result);
        return result;
      }
    }

    // Finally, check the custom campfire recipes.

    for (CampfireRecipe recipe : ModulePyrotechRegistries.CAMPFIRE_RECIPE) {

      if (recipe.matches(input)) {
        return recipe;
      }
    }

    return null;
  }

  private static String getRecipeKey(ItemStack itemStack) {

    return itemStack.getItem().getUnlocalizedName() + ":" + itemStack.getItemDamage();
  }

  public static boolean removeRecipes(Ingredient output) {

    return RecipeHelper.removeRecipesByOutput(ModulePyrotechRegistries.CAMPFIRE_RECIPE, output);
  }

  public static void blacklistSmeltingRecipe(Ingredient output) {

    BLACKLIST.add(output);
  }

  public static void whitelistSmeltingRecipe(Ingredient output) {

    WHITELIST.add(output);
  }

  public static boolean hasBlacklist() {

    return !BLACKLIST.isEmpty();
  }

  public static boolean hasWhitelist() {

    return !WHITELIST.isEmpty();
  }

  public static boolean isBlacklisted(ItemStack output) {

    for (Ingredient ingredient : BLACKLIST) {

      if (ingredient.apply(output)) {
        return true;
      }
    }

    return false;
  }

  public static boolean isWhitelisted(ItemStack output) {

    for (Ingredient ingredient : WHITELIST) {

      if (ingredient.apply(output)) {
        return true;
      }
    }

    return false;
  }

  private final Ingredient input;
  private final ItemStack output;

  public CampfireRecipe(
      ItemStack output,
      Ingredient input
  ) {

    this.input = input;
    this.output = output;
  }

  public Ingredient getInput() {

    return this.input;
  }

  public ItemStack getOutput() {

    return this.output.copy();
  }

  public boolean matches(ItemStack input) {

    return this.input.apply(input);
  }
}
