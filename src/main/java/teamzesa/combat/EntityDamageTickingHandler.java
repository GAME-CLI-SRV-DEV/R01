package teamzesa.combat;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityDamageTickingHandler implements Listener {

    @EventHandler
    public void entityHit(EntityDamageByEntityEvent e) {
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (e.getEntityType() != EntityType.PLAYER)
                    return;

                if (e.getDamager().getType() != EntityType.PLAYER)
                    return;

                Player target = (Player) e.getEntity();
                //default 20
                target.setMaximumNoDamageTicks(1);
//                ((LivingEntity) e.getEntity()).setMaximumNoDamageTicks(1);
            }
        };
        task.run();
    }
}
