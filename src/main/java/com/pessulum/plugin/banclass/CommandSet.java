package com.pessulum.plugin.banclass;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CommandSet implements CommandExecutor {
    BanClass plugin;

    public CommandSet(BanClass plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;


        if (player.isOp()) {
            if (command.getName().equalsIgnoreCase("setMaxDeaths")) {

                if (args.length == 1) {
                    try {
                        int number = Integer.parseInt(args[0]);

                        Bukkit.getWhitelistedPlayers().forEach(player1 -> plugin.PlayerDeathCountManager.replace(player1.getUniqueId(), plugin.PlayerDeathCountManager.get(player1.getUniqueId()),
                                number+plugin.PlayerDeathCountManager.get(player1.getUniqueId()
                                )));
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.RED + args[0] + " is not a valid number");
                    }
                }
                if (command.getName().equalsIgnoreCase("setKills")) {

                    if (args.length == 2) {
                        try {
                            Player player1 = Bukkit.getPlayer(args[0]);
                            UUID uuid = player1.getUniqueId();
                        } catch (NullPointerException e) {
                            player.sendMessage(ChatColor.RED + "No such name as " + args[0]);
                            return true;
                        }
                        Player player1 = Bukkit.getPlayer(args[0]);
                        UUID uuid = player1.getUniqueId();
                        try {
                            int number = Integer.parseInt(args[1]);
                            plugin.PlayerKillCountManager.replace(uuid, plugin.PlayerKillCountManager.get(uuid),number);
                            plugin.getConfig().set(uuid + ".kills", number);
                            plugin.saveConfig();
                            player.sendMessage(ChatColor.GREEN + "Successful player " + args[0] + " kill count has been changed");
                        } catch (Exception e) {
                            player.sendMessage(ChatColor.RED + "wtf is " + args[1] + " give me a danm number");}
                        return true;
                    }
                }
                if (command.getName().equalsIgnoreCase("lives")) {
                    player.sendMessage(ChatColor.BOLD.toString() + ChatColor.DARK_RED + "Lives: " +  plugin.PlayerDeathCountManager.get(player.getUniqueId()) + "");
                    return true;
                }
                if (command.getName().equalsIgnoreCase("kills")) {
                    player.sendMessage(ChatColor.BOLD.toString() + ChatColor.DARK_RED + "Kills: " +  plugin.PlayerKillCountManager.get(player.getUniqueId()) + "");
                    return true;
                }


                if (command.getName().equalsIgnoreCase("setLives")) {

                    if (args.length == 2) {
                        try {
                            Bukkit.getPlayer(args[0]);
                        } catch (NullPointerException e) {
                            player.sendMessage(ChatColor.RED +"who tf is " + args[0] + " you on crack right?");
                        }
                        Player player1 = Bukkit.getPlayer(args[0]);
                        UUID uuid = player1.getUniqueId();
                        try {
                            int number = Integer.parseInt(args[1]);
                            plugin.PlayerDeathCountManager.replace(uuid, plugin.PlayerDeathCountManager.get(uuid), number);
                            plugin.getConfig().set(uuid + ".lives", number);
                            plugin.saveConfig();
                            if (number <= 0) {
                                Bukkit.getBanList(BanList.Type.NAME).addBan(player1.getName(), "You have lost all of your lives", new Date(3000, Calendar.JUNE, 3), null);
                            }
                            player.sendMessage(ChatColor.GREEN + "Successful player " + args[0] + " lives amount has been changed");
                            player1.sendMessage(ChatColor.GREEN +player.getName() + " your lives count was changed to " + number);
                        } catch (Exception e) {
                            player.sendMessage(ChatColor.RED + "wtf is " + args[1] + " give me a damn number");
                            return true;}
                    }
                }

            }

    }
        return false;
    }
 }