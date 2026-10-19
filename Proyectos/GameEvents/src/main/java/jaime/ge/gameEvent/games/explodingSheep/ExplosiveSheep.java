package jaime.ge.gameEvent.games.explodingSheep;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;

public class ExplosiveSheep {
    private final Sheep entity;
    private int timer;

    public ExplosiveSheep(Location spawnLocation, int timer) {
        this.entity = (Sheep) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SHEEP);
        this.timer = timer;

        entity.setInvulnerable(true);
        entity.setColor(DyeColor.GREEN);
        spawnParticles();
    }

    private void spawnParticles() {
        entity.getWorld().spawnParticle(Particle.CLOUD, entity.getLocation(),
                10, 0.0D, 0.0D, 0.0D, 0.1D);
    }

    public void tick(){
        if(timer > 2){
            entity.setColor(DyeColor.GREEN);
        }
        if(timer == 2){
            entity.setColor(DyeColor.YELLOW);
        }
        if(timer == 1){
            entity.setColor(DyeColor.RED);
        }
        if(timer == 0){
            explode();
            entity.remove();
        }
        timer--;
    }

    private void explode() {
        entity.getWorld().createExplosion(entity.getLocation(), 4f, false, false);
    }

    public void despawn(){
        entity.remove();
    }

    public boolean isDead(){
        return !entity.isValid();
    }
}
