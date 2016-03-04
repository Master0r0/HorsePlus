package com.github.master0r0.horseplus.Listeners;

import com.github.master0r0.horseplus.HorseHandling.CheckHorse;
import com.github.master0r0.horseplus.HorsePlus;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import static com.github.master0r0.horseplus.HorseHandling.HorseRegistration.RegisterHorse;

/**
 * Licensed under the GNU Public License
 * <p/>
 * Created by Master0r0 on 04/03/2016.
 */
public class EntityListener implements Listener {

    private HorsePlus plugin = HorsePlus.getPlugin(HorsePlus.class);

    @EventHandler
    public void onEntityMount(EntityMountEvent event){
        if(event.getMount() instanceof Horse)
            if(((Horse) event.getMount()).getOwner()!=null)
                if(!RegisterHorse((Horse) event.getMount()))
                    new CheckHorse((Horse) event.getMount());

    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Horse)
            if(!plugin.config.getConfigurationSection("Horse Configs").getBoolean("Should Horses take damage"))
                event.setCancelled(true);

    }

}
