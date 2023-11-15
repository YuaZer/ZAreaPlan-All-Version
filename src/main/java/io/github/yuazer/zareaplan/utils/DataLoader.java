package io.github.yuazer.zareaplan.utils;

import io.github.yuazer.zareaplan.Main;
import io.github.yuazer.zareaplan.runnable.PlanRunnable;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DataLoader {
    public static void reloadAllPlanAsync(){
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),DataLoader::reloadPlanData);
    }
    public static void reloadPlanAsync(String planName){
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),()-> reloadPlanData(planName));
    }
    public static void reloadPlanData() {
        File file = new File("plugins/ZAreaPlan/plans");
        if (!file.exists()){
            file.mkdirs();
        }
        if (file.listFiles() == null) {
            Main.getInstance().getLogger().info(YamlUtils.getConfigMessage("Message.fileNull"));
            return;
        }
        for (File planFile : file.listFiles()) {
            YamlConfiguration conf = YamlConfiguration.loadConfiguration(planFile);
            String planName = planFile.getName().replace(".yml","");
            if (Main.getRunnableManager().getRunningRunnables().contains(planName)){
                Main.getRunnableManager().stopRunnable(planName);
            }
            Main.getRunnableManager().put(planName,new PlanRunnable(planName));
            if (!conf.getBoolean("isAutoRun")){
                return;
            }
            if (conf.getBoolean("async")){
                Main.getRunnableManager().startRunnable(planName,Long.parseLong(conf.getString("delay")), Long.parseLong(conf.getString("period")));
            }else {
                Main.getRunnableManager().startRunnableSync(planName,Long.parseLong(conf.getString("delay")), Long.parseLong(conf.getString("period")));
            }
        }
    }
    public static void reloadPlanData(String planName) {
        File file = new File("plugins/ZAreaPlan/plans/"+planName+".yml");
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
        if (Main.getRunnableManager().getRunningRunnables().contains(planName)){
            Main.getRunnableManager().stopRunnable(planName);
        }
        Main.getRunnableManager().put(planName,new PlanRunnable(planName));
        if (!conf.getBoolean("isAutoRun")){
            return;
        }
        if (conf.getBoolean("async")){
            Main.getRunnableManager().startRunnable(planName,Long.parseLong(conf.getString("delay")), Long.parseLong(conf.getString("period")));
        }else {
            Main.getRunnableManager().startRunnableSync(planName,Long.parseLong(conf.getString("delay")), Long.parseLong(conf.getString("period")));
        }
    }
}
