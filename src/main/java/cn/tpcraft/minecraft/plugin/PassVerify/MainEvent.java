package cn.tpcraft.minecraft.plugin.PassVerify;

import cn.tpcraft.minecraft.plugin.Function;
import cn.tpcraft.minecraft.plugin.TPCraftPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class MainEvent implements Listener {

    /*
     * 玩家进入服务器事件
     */
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent PlayerJoinEvent) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    //尝试

                    //请求服务器
                    JSONObject Request = new Function().HttpGet(
                            TPCraftPlugin.Plugin.getConfig().getString("Url.PassVerify") + "?" + "&Username=" + PlayerJoinEvent.getPlayer().getName()
                    );
                    //检查请求状态
                    if (!(boolean)Request.get("Status")) {
                        //返回False

                        //获取语言文件
                        String Message = TPCraftPlugin.Message.getString("PassVerify.Kick");

                        //替换变量
                        Message = Message.replace("{Reason}", Request.get("Message").toString());

                        //踢出玩家
                        PlayerJoinEvent.getPlayer().kickPlayer(
                                translateAlternateColorCodes('&',
                                        TPCraftPlugin.Message.getString("Prefix.PassVerify") + Message
                                )
                        );
                    }
                } catch (Exception Exception) {

                    //发送信息给控制台
                    getLogger().warning(Exception.toString());
                    //踢出玩家
                    PlayerJoinEvent.getPlayer().kickPlayer(
                            translateAlternateColorCodes('&',
                                    TPCraftPlugin.Message.getString("Prefix.PassVerify") + TPCraftPlugin.Message.getString("Error.Player")
                            )
                    );
                }
            }
        }.runTaskLaterAsynchronously(TPCraftPlugin.getPlugin(TPCraftPlugin.class), TPCraftPlugin.Plugin.getConfig().getInt("PassVerify.TimeOut") * 20);
    }

    /*
     * 服务器启动完毕事件
     */
    @EventHandler
    public void ServerLoadEvent(ServerLoadEvent ServerLoadEvent) {
        new BukkitRunnable() {
            @Override
            public void run() {
                //检查所有玩家通行证
                for (Player Player : getServer().getOnlinePlayers()) {
                    try {
                        //尝试

                        //请求服务器
                        JSONObject Request = new Function().HttpGet(
                                TPCraftPlugin.Plugin.getConfig().getString("Url.PassVerify") + "?" + "&Username=" + Player.getPlayer().getName()
                        );
                        //检查请求状态
                        if (!(boolean)Request.get("Status")) {
                            //返回False

                            //获取语言文件
                            String Message = TPCraftPlugin.Message.getString("PassVerify.Kick");

                            //替换变量
                            Message = Message.replace("{Reason}", Request.get("Message").toString());

                            //踢出玩家
                            Player.getPlayer().kickPlayer(
                                    translateAlternateColorCodes('&',
                                            TPCraftPlugin.Message.getString("Prefix.PassVerify") + Message
                                    )
                            );
                        }
                    } catch (Exception Exception) {
                        //发生错误

                        //发送信息给控制台
                        getLogger().warning(Exception.toString());
                        //踢出玩家
                        Player.getPlayer().kickPlayer(
                                translateAlternateColorCodes('&',
                                        TPCraftPlugin.Message.getString("Prefix.PassVerify") + TPCraftPlugin.Message.getString("Error.Player")
                                )
                        );
                    }
                }
            }
        }.runTaskTimerAsynchronously(TPCraftPlugin.getPlugin(TPCraftPlugin.class),0 ,TPCraftPlugin.Plugin.getConfig().getInt("PassVerify.Delay") * 20);
    }
}
