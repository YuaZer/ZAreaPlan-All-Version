package io.github.yuazer.zareaplan.manager;


import io.github.yuazer.zareaplan.runnable.PlanRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BukkitRunnableManager {

    //创建一个HashMap对象，用来存储String和BukkitRunnable的对应关系
    private ConcurrentMap<String, PlanRunnable> runnables;
    private HashSet<String> runningRunnables;

    //创建一个JavaPlugin对象，用来获取插件实例
    private final JavaPlugin plugin;

    //创建一个构造方法，传入插件实例
    public BukkitRunnableManager(JavaPlugin plugin) {
        //初始化HashMap对象
        runnables = new ConcurrentHashMap<>();
        runningRunnables = new HashSet<>();
        //赋值插件实例
        this.plugin = plugin;
    }

    //创建一个方法，用来添加一个String和BukkitRunnable的对应关系
    public void put(String key1, PlanRunnable runnable) {
        runnables.put(key1, runnable);
    }

    public ConcurrentMap<String, PlanRunnable> getRunnables() {
        return runnables;
    }

    //创建一个方法，用来移除一个String和BukkitRunnable的对应关系
    public void remove(String key1) {
        //从HashMap中移除name对应的runnable
        runnables.remove(key1);
    }

    public void removePlayerAll() {
        getPlayerAllRunnableID().forEach(n -> {
            runnables.remove(n);
        });
    }

    //创建一个方法，用来开启指定的BukkitRunnable
    public void startRunnable(String key1, long delay, long period) {
        //从HashMap中获取name对应的runnable
        PlanRunnable runnable = runnables.get(key1);
        //判断runnable是否存在
        if (runnable != null) {
            runningRunnables.add(key1);
            //如果存在，调用runTaskTimer方法，传入插件实例，延迟时间和周期时间
            runnable.runTaskTimerAsynchronously(plugin, delay, period);
        }
    }
    public void startRunnableSync(String key1, long delay, long period) {
        //从HashMap中获取name对应的runnable
        PlanRunnable runnable = runnables.get(key1);
        //判断runnable是否存在
        if (runnable != null) {
            runningRunnables.add(key1);
            //如果存在，调用runTaskTimer方法，传入插件实例，延迟时间和周期时间
            runnable.runTaskTimer(plugin, delay, period);
        }
    }

    public PlanRunnable getRunnable(String key1) {
        return runnables.get(key1);
    }
    public HashSet<String> getRunningRunnables(){
        return this.runningRunnables;
    }
    //创建一个方法，用来关闭指定的BukkitRunnable
    public void stopRunnable(String key1) {
        //从HashMap中获取name对应的runnable
        PlanRunnable runnable = runnables.get(key1);
        //判断runnable是否存在
        if (runnable != null&&!runnable.isCancelled()) {
            runningRunnables.remove(key1);
            //如果存在，调用cancel方法，取消任务
            runnable.cancel();
        }else {
            System.out.println("error cancel");
        }
    }

    public Set<String> getPlayerAllRunnableID() {
        return runnables.keySet();
    }

}
