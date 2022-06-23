package cn.tpcraft.minecraft.plugin;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class TPCraftPlugin extends JavaPlugin {

    //插件主类
    public static Plugin Plugin;

    /*
     * 插件加载
     */
    @Override
    public void onEnable() {
        //获取插件主类
        Plugin = getProvidingPlugin(getClass());
        //加载配置文件
        LoadConfig();
        //监听事件
        ListenerEvent();
        //注册指令
        RegisterCommand();
        //输出日志
        getLogger().info("【TPCraftPlugin】已加载完毕。");
    }

    /*
     * 插件卸载
     */
    @Override
    public void onDisable() {
        //输出日志
        getLogger().info("【TPCraftPlugin】已卸载完毕。");
    }

    /*
     * 加载配置文件
     */
    public void LoadConfig() {
        //保存默认配置文件
        saveDefaultConfig();
    }

    /*
     * 监听事件
     */
    public void ListenerEvent() {

    }

    /*
     * 注册指令
     */
    public void RegisterCommand() {

    }

    /*
     * 重载配置文件
     */
    public void ReLoadConfig() {
        //重载配置文件
        reloadConfig();
    }
}