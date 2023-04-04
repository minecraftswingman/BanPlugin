package com.pessulum.plugin.banclass;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.crypto.Data;
import java.util.*;

public final class BanClass extends JavaPlugin implements Listener {

    public HashMap<UUID, Integer> PlayerDeathCountManager = new HashMap<>();
    public HashMap<UUID, Integer> PlayerKillCountManager = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("setLives").setExecutor(new CommandSet(this));
        this.getCommand("lives").setExecutor(new CommandSet(this));
        this.getCommand("kills").setExecutor(new CommandSet(this));
        this.getCommand("setMaxDeaths").setExecutor(new CommandSet(this));
        this.getCommand("setKills").setExecutor(new CommandSet(this));
        this.getConfig().options().copyDefaults();
        this.saveConfig();
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Set<Player> ConfigPlayers = new HashSet<>();
        if (!ConfigPlayers.contains(player)) {
            Bukkit.getLogger().info("DEBUG");
            PlayerDeathCountManager.put(player.getUniqueId(), 0);
            PlayerKillCountManager.put(player.getUniqueId(), 0);
            getConfig().set("UUID", player.getUniqueId());
            getConfig().set(player.getUniqueId() + ".kills", 0);
            getConfig().set(player.getUniqueId() + ".lives", 5);
            getConfig().set(player.getUniqueId() + ".deaths", 0);
            getConfig().set(player.getUniqueId() + ".playername", player.getName());
            getConfig().set(player.getUniqueId() + ".isBanned", false);
            ConfigPlayers.add(player);
            this.saveConfig();
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        int lives = PlayerDeathCountManager.get(player.getUniqueId());
        PlayerDeathCountManager.replace(player.getUniqueId(), lives,  lives-1);
        getConfig().set(player.getUniqueId() + ".lives", lives-1);
        getConfig().set(player.getUniqueId() + ".deaths", PlayerDeathCountManager.get(player.getUniqueId())+1);
        this.saveConfig();
        if (lives == 0) {
            Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "You have lost all of your lives", new Date(3000, Calendar.JUNE, 3), null);
        }

        Player player2 = player.getKiller();
        if (player2 == null) return;
        PlayerKillCountManager.replace(player2.getUniqueId(), PlayerKillCountManager.get(player2.getUniqueId()), PlayerKillCountManager.get(player2.getUniqueId())+1);
        getConfig().set(player2.getUniqueId() + ".kills", PlayerKillCountManager.get(player2.getUniqueId()));

        this.saveConfig();
    }
}
