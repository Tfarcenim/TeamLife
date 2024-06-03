package tfar.teamlife.init;

import net.minecraft.world.item.Item;
import tfar.teamlife.item.PersonalHeartItem;
import tfar.teamlife.item.TeamHeartItem;

public class ModItems {

    public static final Item PERSONAL_HEART = new PersonalHeartItem(new Item.Properties());
    public static final Item TEAM_HEART = new TeamHeartItem(new Item.Properties());
    public static final Item PORTABLE_BEACON = new Item(new Item.Properties());


}
