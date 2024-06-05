package tfar.teamlife.datagen;

import com.google.gson.JsonElement;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;
import tfar.teamlife.init.ModItems;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelGenerators {

    public ModItemModelProvider(BiConsumer<ResourceLocation, Supplier<JsonElement>> pOutput) {
        super(pOutput);
    }

    @Override
    public void run() {
        this.generateFlatItem(ModItems.PERSONAL_HEART, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.TEAM_HEART, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.PERSONAL_BEACON, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.TEAM_INVENTORY_POUCH, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.NETHER_CORE, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.END_CORE, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.OVERWORLD_CORE, ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.PEARL_OF_LIFE,ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.HEALTH_TOTEM,ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.ENCHANTMENT_TOME,ModelTemplates.FLAT_ITEM);
        generateFlatItem(ModItems.ROCKET_ARTIFACT,ModelTemplates.FLAT_ITEM);
        generateFlatItem(ModItems.NETHERITE_CHESTPLATE_WITH_ELYTRA,ModelTemplates.FLAT_ITEM);

    }



}
