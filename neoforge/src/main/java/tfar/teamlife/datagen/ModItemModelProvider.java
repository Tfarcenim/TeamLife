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
    }



}
