package tfar.teamlife.init;

import net.minecraft.world.item.Item;
import tfar.teamlife.item.PersonalBeaconItem;
import tfar.teamlife.item.PersonalHeartItem;
import tfar.teamlife.item.TeamHeartItem;
import tfar.teamlife.item.TeamInventoryItem;

public class ModItems {

    public static final Item PERSONAL_HEART = new PersonalHeartItem(new Item.Properties());
    public static final Item TEAM_HEART = new TeamHeartItem(new Item.Properties());
    public static final Item PERSONAL_BEACON = new PersonalBeaconItem(new Item.Properties());
    public static final Item TEAM_INVENTORY_POUCH = new TeamInventoryItem(new Item.Properties());
    public static final Item NETHER_CORE = new Item(new Item.Properties());
    public static final Item END_CORE = new Item(new Item.Properties());
    public static final Item OVERWORLD_CORE = new Item(new Item.Properties());


}
