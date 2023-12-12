package io.github.yuazer.zareaplan.runnable;

import io.github.yuazer.zareaplan.Main;
import io.github.yuazer.zareaplan.utils.PlayerUtils;
import io.github.yuazer.zareaplan.utils.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashSet;

public class PlanRunnable extends BukkitRunnable {
    private String planName;
    private YamlConfiguration conf;
    private Location loc1;
    private Location loc2;

    public PlanRunnable(String planName) {
        this.planName = planName;
        this.conf = YamlConfiguration.loadConfiguration(new File("plugins/ZAreaPlan/plans/" + this.planName + ".yml"));
        String[] x1y1z1 = conf.getString("Location.x1y1z1").split("//");
        String[] x2y2z2 = conf.getString("Location.x2y2z2").split("//");
        World world = Bukkit.getWorld(conf.getString("Location.world"));
        if (world == null) {
            Main.getInstance().getLogger().info(YamlUtils.getConfigMessage("Message.errorWorld")
                    .replace("%world%", conf.getString("Location.world"))
                    .replace("%file%", planName + ".yml"));
            cancel();
            return;
        }
        loc1 = new Location(world, Integer.parseInt(x1y1z1[0]), Integer.parseInt(x1y1z1[1]), Integer.parseInt(x1y1z1[2]));
        loc2 = new Location(world, Integer.parseInt(x2y2z2[0]), Integer.parseInt(x2y2z2[1]), Integer.parseInt(x2y2z2[2]));
    }

    @Override
    public void run() {
        HashSet<String> players = PlayerUtils.getPlayerNamesBetweenLocations(loc1,loc2);
        for (String player:players){
            if (conf.getStringList("blackListPlayer").contains(player)){
                continue;
            }
            Player player1 = Bukkit.getPlayer(player);
            if (player1==null){
                continue;
            }
            Bukkit.getScheduler().runTask(Main.getInstance(),()->{
                for (String cmd:conf.getStringList("commands")){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd.replace("%player%",player));
                }
            });
        }
    }

}
