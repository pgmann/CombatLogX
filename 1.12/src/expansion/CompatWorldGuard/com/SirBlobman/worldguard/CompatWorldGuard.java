package com.SirBlobman.worldguard;

import com.SirBlobman.combatlogx.Combat;
import com.SirBlobman.combatlogx.config.Config;
import com.SirBlobman.combatlogx.event.PlayerCombatEvent;
import com.SirBlobman.combatlogx.expansion.CLXExpansion;
import com.SirBlobman.combatlogx.utility.Util;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class CompatWorldGuard implements CLXExpansion, Listener {
    @Override
    public void enable() {
        if(Util.PM.isPluginEnabled("WorldGuard")) {
            Util.regEvents(this);
        } else {
            String error = "WorldGuard is not installed. This expansion is useless!";
            Util.print(error);
        }
    }

    @Override
    public String getName() {return "WorldGuard Compatability";}

    @Override
    public String getVersion() {return "1.0.0";}

    @EventHandler
    public void pce(PlayerCombatEvent e) {
        LivingEntity ler = e.getAttacker();
        LivingEntity led = e.getTarget();
        if(ler instanceof Player) { 
            Player p = (Player) ler;
            boolean pvp = WorldGuardUtil.pvp(p);
            if(!pvp) e.setCancelled(true);
        }

        if(led instanceof Player) {
            Player p = (Player) led;
            boolean pvp = WorldGuardUtil.pvp(p);
            if(!pvp) e.setCancelled(true);
        }
    }

    @EventHandler
    public void move(PlayerMoveEvent e) {
        if(e.isCancelled()) return;
        if(Config.CHEAT_PREVENT_NO_ENTRY) {
            Player p = e.getPlayer();
            Location from = e.getFrom();
            Location to = e.getTo();
            if(Combat.isInCombat(p) && WorldGuardUtil.isSafeZone(to)) {
                e.setCancelled(true);
                String error = Config.MESSAGE_NO_ENTRY;
                Util.sendMessage(p, error);
                p.teleport(from);
            }
        }
    }

    @EventHandler
    public void tp(PlayerTeleportEvent e) {
        if(e.isCancelled()) return;
        if(Config.CHEAT_PREVENT_NO_ENTRY && e.getCause() == TeleportCause.ENDER_PEARL) {
            Player p = e.getPlayer();
            Location from = e.getFrom();
            Location to = e.getTo();
            if(Combat.isInCombat(p) && WorldGuardUtil.isSafeZone(to)) {
                e.setCancelled(true);
                String error = Config.MESSAGE_NO_ENTRY;
                Util.sendMessage(p, error);
                p.teleport(from);
            }
        }
    }
}