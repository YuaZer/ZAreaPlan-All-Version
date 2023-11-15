package io.github.yuazer.zareaplan;

import io.github.yuazer.zareaplan.commands.MainCommand;
import io.github.yuazer.zareaplan.manager.BukkitRunnableManager;
import io.github.yuazer.zareaplan.utils.DataLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin {
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }
    private static BukkitRunnableManager runnableManager;

    public static BukkitRunnableManager getRunnableManager() {
        return runnableManager;
    }

    @Override
    public void onEnable() {
        instance = this;
        runnableManager = new BukkitRunnableManager(this);
        File file = new File("plugins/ZAreaPlan/plans/test.yml");
        if (!file.exists()){
            saveResource("plans/test.yml",false);
        }
        saveDefaultConfig();
        reloadConfig();
        DataLoader.reloadAllPlanAsync();
        Bukkit.getPluginCommand("zareaplan").setExecutor(new MainCommand());
        logLoaded(this);
    }

    @Override
    public void onDisable() {
        runnableManager.getRunningRunnables().forEach(planName->{
            runnableManager.stopRunnable(planName);
        });
        logDisable(this);
    }
    public static void logLoaded(JavaPlugin plugin) {
        Bukkit.getLogger().info(String.format("§e[§b%s§e] §f已加载", plugin.getName()));
        Bukkit.getLogger().info("§b作者:§eZ菌[QQ:1109132]");
        Bukkit.getLogger().info("§b版本:§e" + plugin.getDescription().getVersion());
    }

    public static void logDisable(JavaPlugin plugin) {
        Bukkit.getLogger().info(String.format("§e[§b%s§e] §c已卸载", plugin.getName()));
    }
}
