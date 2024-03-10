package teamzesa.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import teamzesa.command.register.CommandRegister;
import teamzesa.util.Enum.CommandExecutorMap;
import teamzesa.util.Enum.ColorList;


public class Fly extends CommandRegister {

    public Fly() {
        super(CommandExecutorMap.FLY);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player targetPlayer = (Player) sender;

        targetPlayer.setAllowFlight(!targetPlayer.getAllowFlight());
        String comment = targetPlayer.getAllowFlight() ? "활성화" : "비활성화";
        playerSendMsgComponentExchanger(targetPlayer,"플라이 " + comment, ColorList.YELLOW);
        return true;
    }
}
