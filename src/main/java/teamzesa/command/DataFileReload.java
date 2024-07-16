package teamzesa.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamzesa.command.register.CommandRegisterSection;
import teamzesa.DataBase.entity.RObject.User;
import teamzesa.util.Enum.CommandExecutorMap;
import teamzesa.R01;
import teamzesa.util.Enum.ColorMap;
import teamzesa.DataBase.userHandler.UserController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class DataFileReload extends CommandRegisterSection {

    private Player player;
    private boolean isConsoleSend = false;

    public DataFileReload() {
        super(CommandExecutorMap.CONFIG_RELOAD);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        User user = null;
        try {
            user = new UserController().readUser(commandSender.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional.ofNullable(user).ifPresentOrElse(
                existUser -> this.player = Bukkit.getPlayer(existUser.uuid()),
                ()        -> this.isConsoleSend = true
        );

//        operation Check
        if (!player.isOp() && !this.isConsoleSend) {
            playerSendMsgComponentExchanger(player,"해당 명령어는 플레이어가 사용할 수 없습니다.", ColorMap.RED);
            return Collections.emptyList();
        }

        sendComment();
        R01.getPlugin(R01.class).configFileLoader();

        return new ArrayList<>(List.of("dataFileReload"));
    }

    private void sendComment() {
        String comment = "Reload Done";
        if (this.isConsoleSend)
            Bukkit.getLogger().info("[R01] " + comment);
        else playerSendMsgComponentExchanger(this.player, comment, ColorMap.YELLOW);
    }
}
