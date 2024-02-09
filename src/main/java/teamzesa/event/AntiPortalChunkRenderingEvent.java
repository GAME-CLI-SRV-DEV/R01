package teamzesa.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityPortalEvent;
import teamzesa.event.register.EventRegister;
import teamzesa.util.ComponentExchanger;

import java.util.HashSet;
import java.util.Set;

public class AntiPortalChunkRenderingEvent extends ComponentExchanger implements EventRegister {
    private EntityType enteringEntityType;
    private Set<EntityType> lockingEntity;
    private final EntityPortalEvent event;

    public AntiPortalChunkRenderingEvent(EntityPortalEvent event) {
        this.event = event;
        init();
        execute();
    }

    @Override
    public void init() {
        this.enteringEntityType = this.event.getEntity().getType();
        this.lockingEntity = new HashSet<>();
        this.lockingEntity.add(EntityType.MINECART);
        this.lockingEntity.add(EntityType.MINECART_CHEST);
        this.lockingEntity.add(EntityType.MINECART_FURNACE);
        this.lockingEntity.add(EntityType.MINECART_HOPPER);
    }

    @Override
    public void execute() {
        boolean typeChecker = this.lockingEntity.stream()
                .anyMatch(banningEntityType -> banningEntityType.equals(this.enteringEntityType));

        playerSendMsgComponentExchanger(Bukkit.getPlayer("JAXPLE"),String.valueOf(typeChecker));
        if (typeChecker)
            this.event.setCancelled(true);
    }
}
