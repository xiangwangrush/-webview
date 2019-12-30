package com.plugin.api;

import android.util.Log;

/**
 * 初始化所有指令到命令管理器
 */

public class PluginInitUtil {
    public static void initPlugin() {
        String proxyClassFullName = "com.base.PluginInit$RushBridge";
        Log.d("RUSH", proxyClassFullName);
        try {
            Class<?> proxyClazz = Class.forName(proxyClassFullName);
            PluginInitInterface viewInjector = (PluginInitInterface) proxyClazz.newInstance();
            viewInjector.init();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
