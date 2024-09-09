package teamzesa.event.Restricted;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import teamzesa.event.EventRegister.EventRegister;
import teamzesa.util.Enum.ColorMap;
import teamzesa.util.Interface.StringComponentExchanger;

public class RestrictedEntityPInteractHandler extends StringComponentExchanger implements EventRegister {
    private final PlayerInteractEvent event;

    public RestrictedEntityPInteractHandler(PlayerInteractEvent event) {
        this.event = event;
        init();
        execute();
    }

    @Override
    public void init() {
    }

    @Override
    public void execute() {
        if (ObjectUtils.notEqual(this.event.getHand(), EquipmentSlot.HAND))
            return;

        if (this.event.getPlayer().isOp())
            return;

        Material playerMainHandItemMaterial = this.event.getPlayer().getInventory().getItemInMainHand().getType();
        boolean restrictedItem = new RestrictedChatElement().restrictedItem.stream()
                .anyMatch(playerMainHandItemMaterial::equals);

        if (BooleanUtils.isFalse(restrictedItem))
            return;

        this.event.setCancelled(true);
        playerSendMsgComponentExchanger(this.event.getPlayer(), "해당 아이템은 설치할 수 없습니다.", ColorMap.RED);
    }
}
