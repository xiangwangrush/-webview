package rush.com.base.cmd;

import android.content.Context;

import java.util.HashMap;

/**
 * 命令管理器
 */

public class CommandManager {
    private HashMap<String,Command> commands;
    private static CommandManager instance;
    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public void registerCommand(Command command){
        if(commands == null){
            commands = new HashMap<>();
        }
        commands.put(command.name(),command);
    }

    private CommandManager() {
        commands = new HashMap<>();
    }

    public static CommandManager getInstance(){
        if(instance == null){
            synchronized (CommandManager.class){
                if(instance == null){
                    instance = new CommandManager();
                }
            }
        }
        return instance;
    }

    public void executeCommand(Context context,String action,String params,ResultCallback resultCallback){
        boolean methodFlag = false;
        if(getCommands().get(action) != null){
            methodFlag = true;
            getCommands().get(action).exec(context,params,resultCallback);
        }
        if(!methodFlag){
            resultCallback.onResult(-1,action,"not found action");
        }
    }

}
