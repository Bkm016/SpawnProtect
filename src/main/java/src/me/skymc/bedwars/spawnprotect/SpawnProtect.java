package me.skymc.bedwars.spawnprotect;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.bedwarsrel.BedwarsRel;
import io.github.bedwarsrel.game.Game;
import io.github.bedwarsrel.game.Team;
import lombok.Getter;
import me.skymc.taboolib.fileutils.ConfigUtils;

/**
 * 插件主类
 * 
 * @author sky
 * @since 2018年2月3日11:48:58
 */
public class SpawnProtect extends JavaPlugin implements Listener {
	
	@Getter
	private static FileConfiguration conf;
	
	@Override
	public FileConfiguration getConfig() {
		return conf;
	}
	
	@Override
	public void reloadConfig() {
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			saveResource("config.yml", true);
		}
		conf = ConfigUtils.load(this, file);
	}
	
	@Override
	public void onLoad() {
		reloadConfig();
	}
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		reloadConfig();
		sender.sendMessage("reload ok!");
		return true;
	}
	
	@EventHandler
	public void ing(BlockIgniteEvent e) {
		Game game = BedwarsRel.getInstance().getGameManager().getGameByLocation(e.getBlock().getLocation());
		if (game == null) {
			return;
		}
		
		for (Team team : game.getTeams().values()) {
			if (team.getSpawnLocation().distance(e.getBlock().getLocation()) <= SpawnProtect.getConf().getInt("distance")) {
				e.setCancelled(true);
				
				/**
				 * 提示信息
				 */
				if (e.getPlayer() != null) {
					e.getPlayer().sendMessage(SpawnProtect.getConf().getString("message").replace("&", "§"));
				}
				return;
			}
		}
	}
}
