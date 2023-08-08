package teamzesa.combat;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Explosive implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    public void explosiveHandler(ExplosionPrimeEvent e) {
//        TNT: 4 blocks
//        Creeper:
//        Normal: 3 blocks
//        Charged: 6 blocks
//        Ghast Fireball: 1 block
//        Wither Skull:
//        Blue Skull: 1 block
//        Black Skull: Varies depending on difficulty
        switch (e.getEntityType()) {
            case PRIMED_TNT -> boomBer(e);
            case CREEPER -> creeperBoom(e);
            case FIREBALL -> ghastBoom(e);
            case WITHER_SKULL -> witherBoom(e);
            case MINECART_TNT -> cartBoom(e);
            default -> e.setCancelled(true);
        }
    }

    private void boomBer(ExplosionPrimeEvent e) {
        e.setCancelled(true);
        World world = e.getEntity().getWorld();
        world.createExplosion(e.getEntity().getLocation(), 20.0F, true);
    }

    private void creeperBoom(ExplosionPrimeEvent e) {
        Creeper creeper = (Creeper) e.getEntity();
        int explosiveRadius = 20;

        if (creeper.isPowered())
            explosiveRadius = 100;

        e.setRadius(explosiveRadius);
        e.setFire(true);
    }

    private void ghastBoom(ExplosionPrimeEvent e) {
        e.setRadius(10);
        e.setFire(true);
    }

    private void witherBoom(ExplosionPrimeEvent e) {
        WitherSkull witherSkull = (WitherSkull) e.getEntity();
        if (witherSkull.isCharged())
            e.setRadius(40);
        if (!witherSkull.isCharged())
            e.setRadius(100);
    }

    private void cartBoom(ExplosionPrimeEvent e) {
        Location location = e.getEntity().getLocation();;
    }
}
