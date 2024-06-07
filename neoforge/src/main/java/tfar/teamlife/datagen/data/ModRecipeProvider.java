package tfar.teamlife.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import tfar.teamlife.init.ModItems;

import java.util.concurrent.CompletableFuture;


public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    protected void buildRecipes(RecipeOutput pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.NETHER_CORE)
                .define('a', Blocks.NETHERRACK).define('b',Blocks.NETHERITE_BLOCK).define('c',Items.NETHER_STAR)
                .pattern("aba")
                .pattern("bcb")
                .pattern("aba")
                .unlockedBy(getHasName(Items.NETHER_STAR),has(Items.NETHER_STAR))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEALTH_TOTEM)
                .define('a', Items.BLAZE_POWDER).define('b',Items.GOLDEN_CARROT)
                .define('g',Items.GLISTERING_MELON_SLICE).define('t',Items.TOTEM_OF_UNDYING)
                .pattern("aba")
                .pattern("gtg")
                .pattern("aba")
                .unlockedBy(getHasName(Items.TOTEM_OF_UNDYING),has(Items.TOTEM_OF_UNDYING))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TEAM_HEART)
                .define('a', Items.TOTEM_OF_UNDYING).define('b',Blocks.DIAMOND_BLOCK)
                .define('g',Items.GOLDEN_APPLE)
                .pattern("aba")
                .pattern("bgb")
                .pattern("aba")
                .unlockedBy(getHasName(Items.TOTEM_OF_UNDYING),has(Items.TOTEM_OF_UNDYING))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PERSONAL_HEART)
                .define('a', ModItems.HEALTH_TOTEM).define('b',Items.DIAMOND_BLOCK)
                .define('g',Items.GOLDEN_APPLE)
                .pattern("aba")
                .pattern("bgb")
                .pattern("aba")
                .unlockedBy(getHasName(ModItems.HEALTH_TOTEM),has(ModItems.HEALTH_TOTEM))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.END_CORE)
                .define('a', Blocks.END_STONE).define('d',Items.DRAGON_HEAD)
                .define('e',Items.END_CRYSTAL).define('l',Items.ELYTRA)
                .define('r',Items.DRAGON_BREATH)
                .pattern("ada")
                .pattern("ele")
                .pattern("ara")
                .unlockedBy(getHasName(Items.ELYTRA),has(Items.ELYTRA))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.OVERWORLD_CORE)
                .define('a', ModItems.HEALTH_TOTEM).define('b',Items.DIAMOND_BLOCK)
                .define('t',Items.GOLDEN_APPLE).define('r',Blocks.MOSS_BLOCK)
                .pattern("rbr")
                .pattern("btb")
                .pattern("aba")
                .unlockedBy(getHasName(ModItems.HEALTH_TOTEM),has(ModItems.HEALTH_TOTEM))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT,ModItems.PEARL_OF_LIFE)
                .requires(ModItems.PERSONAL_HEART)
                .unlockedBy(getHasName(ModItems.PERSONAL_HEART),has(ModItems.PERSONAL_HEART))
                .save(pWriter);

        elytraSmithing(pWriter,Items.LEATHER_CHESTPLATE,RecipeCategory.COMBAT,ModItems.LEATHER_CHESTPLATE_WITH_ELYTRA);
        elytraSmithing(pWriter,Items.CHAINMAIL_CHESTPLATE,RecipeCategory.COMBAT,ModItems.CHAINMAIL_CHESTPLATE_WITH_ELYTRA);
        elytraSmithing(pWriter,Items.GOLDEN_CHESTPLATE,RecipeCategory.COMBAT,ModItems.GOLDEN_CHESTPLATE_WITH_ELYTRA);
        elytraSmithing(pWriter,Items.IRON_CHESTPLATE,RecipeCategory.COMBAT,ModItems.IRON_CHESTPLATE_WITH_ELYTRA);
        elytraSmithing(pWriter,Items.DIAMOND_CHESTPLATE,RecipeCategory.COMBAT,ModItems.DIAMOND_CHESTPLATE_WITH_ELYTRA);
        elytraSmithing(pWriter,Items.NETHERITE_CHESTPLATE,RecipeCategory.COMBAT,ModItems.NETHERITE_CHESTPLATE_WITH_ELYTRA);
    }

    public static void elytraSmithing(RecipeOutput recipeOutput, Item ingredientItem, RecipeCategory category, Item resultItem) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.ELYTRA), Ingredient.of(ingredientItem),
                Ingredient.of(Items.IRON_NUGGET), category, resultItem).unlocks("has_elytra", has(Items.ELYTRA)).save(recipeOutput, getItemName(resultItem) + "_smithing");
    }

}
