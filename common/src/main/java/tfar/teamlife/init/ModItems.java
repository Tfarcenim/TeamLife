package tfar.teamlife.init;

import net.minecraft.world.item.*;
import tfar.teamlife.item.*;

import java.util.Set;

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
    public static final Item INFINITE_FIREWORK_ROCKET = new InfiniteFireworkRocketItem(new Item.Properties());

    public static final Item INFINITE_FIREWORK_ROCKET_ARTIFACT = new InfiniteFireworkRocketItem(new Item.Properties());
    public static final Item ENCHANTMENT_TOME_ARTIFACT = ArtifactItem.enchantedTome(new Item.Properties());
    public static final Item PERSONAL_BEACON_ARTIFACT = new PersonalBeaconItem(new Item.Properties());
    public static final Item TEAM_INVENTORY_POUCH_ARTIFACT = new TeamInventoryItem(new Item.Properties());


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

    public static final Item CHESTPLATE_WITH_ELYTRA_ARTIFACT = new ArtifactItem(new Item.Properties(),() -> Set.of(LEATHER_CHESTPLATE_WITH_ELYTRA,GOLDEN_CHESTPLATE_WITH_ELYTRA,CHAINMAIL_CHESTPLATE_WITH_ELYTRA,IRON_CHESTPLATE_WITH_ELYTRA,DIAMOND_CHESTPLATE_WITH_ELYTRA,NETHERITE_CHESTPLATE_WITH_ELYTRA));



    public static final Item PEDESTAL = new BlockItem(ModBlocks.PEDESTAL,new Item.Properties());



}
