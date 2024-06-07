package tfar.teamlife.init;

import net.minecraft.world.item.*;
import tfar.teamlife.item.*;

public class ModItems {

    public static final Item PERSONAL_HEART = new PersonalHeartItem(new Item.Properties());
    public static final Item TEAM_HEART = new TeamHeartItem(new Item.Properties());
    public static final Item PERSONAL_BEACON = new PersonalBeaconItem(new Item.Properties());
    public static final Item TEAM_INVENTORY_POUCH = new TeamInventoryItem(new Item.Properties());
    public static final Item NETHER_CORE = new Item(new Item.Properties());
    public static final Item END_CORE = new Item(new Item.Properties());
    public static final Item OVERWORLD_CORE = new Item(new Item.Properties());
    public static final Item PEARL_OF_LIFE = new PearlOfLifeItem(new Item.Properties());
    public static final Item HEALTH_TOTEM = new Item(new Item.Properties());
    public static final Item ENCHANTMENT_TOME = new Item(new Item.Properties());
    public static final Item ROCKET_ARTIFACT = new ArtifactFireworkRocketItem(new Item.Properties());

    public static final Item LEATHER_CHESTPLATE_WITH_ELYTRA = new ChestplateWithElytraItem(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE,
            new Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(37)));
    public static final Item GOLDEN_CHESTPLATE_WITH_ELYTRA = new ChestplateWithElytraItem(ArmorMaterials.GOLD, ArmorItem.Type.CHESTPLATE,
            new Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(37)));
    public static final Item CHAINMAIL_CHESTPLATE_WITH_ELYTRA = new ChestplateWithElytraItem(ArmorMaterials.CHAIN, ArmorItem.Type.CHESTPLATE,
            new Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(37)));
    public static final Item IRON_CHESTPLATE_WITH_ELYTRA = new ChestplateWithElytraItem(ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE,
            new Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(37)));
    public static final Item DIAMOND_CHESTPLATE_WITH_ELYTRA = new ChestplateWithElytraItem(ArmorMaterials.DIAMOND, ArmorItem.Type.CHESTPLATE,
            new Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(37)));
    public static final Item NETHERITE_CHESTPLATE_WITH_ELYTRA = new ChestplateWithElytraItem(ArmorMaterials.NETHERITE, ArmorItem.Type.CHESTPLATE,
            new Item.Properties().fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(37)));

    public static final Item PEDESTAL = new BlockItem(ModBlocks.PEDESTAL,new Item.Properties());



}
