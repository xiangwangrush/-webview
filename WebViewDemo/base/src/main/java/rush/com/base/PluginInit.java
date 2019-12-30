package rush.com.base;


import com.plugin.api.PluginInitUtil;

/**
 * 想生成的文件
 * 由于插件的场景原因 这边直接注册 插件的无参构造函数
 */

public class PluginInit {
    public static void init(){
        PluginInitUtil.initPlugin();
    }
}
