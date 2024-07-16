package teamzesa.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamzesa.DataBase.UserKillStatusHandler.KillStatusBuilder;
import teamzesa.DataBase.UserKillStatusHandler.KillStatusController;
import teamzesa.command.register.CommandRegisterSection;
import teamzesa.DataBase.entity.RObject.User;
import teamzesa.util.Enum.CommandExecutorMap;
import teamzesa.util.Enum.ColorMap;
import teamzesa.DataBase.userHandler.UserController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class SetHealth extends CommandRegisterSection {
    private Player senderPlayer;
    private Player targetPlayer;
    private boolean isConsoleSend = false;

    public SetHealth() {
        super(CommandExecutorMap.HEALTH_RESET);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        User senderUser = null;
        try {
            senderUser =  new UserController().readUser(commandSender.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional.ofNullable(senderUser).ifPresentOrElse(
                existUser -> this.senderPlayer = Bukkit.getPlayer(existUser.uuid()),
                ()        -> this.isConsoleSend = true
        );

        if (senderUser != null && !this.senderPlayer.isOp()) {
            playerSendMsgComponentExchanger(this.senderPlayer,"해당 명령어는 플레이어가 사용할 수 없습니다.", ColorMap.RED);
            return Collections.emptyList();
        }

        User targetUser = null;
        try {
            targetUser = new UserController().readUser(strings[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional.ofNullable(Bukkit.getPlayer(targetUser.uuid())).ifPresent(
                player -> {
                    this.targetPlayer = player;
                    setPlayerHealth(Double.parseDouble(strings[1]));
                }
        );
        return new ArrayList<>(List.of("setHealth"));
    }

    private void setPlayerHealth(double setHealthValue) {
        KillStatusController userKillStatusController = new KillStatusController();
        userKillStatusController.healthUpdate(
                new KillStatusBuilder(userKillStatusController.readUser(this.targetPlayer.getUniqueId()))
                        .healthScale(setHealthValue)
                        .buildAndUpdate()
        );

        sendComment(setHealthValue);
    }

    private void sendComment(double setHealthValue) {
        String comment = this.targetPlayer.getName() + "님의 체력이" + setHealthValue + "으로 설정됐습니다.";
        if (isConsoleSend) {
            Bukkit.getLogger().info("[R01] " + comment);
            return;
        }

        if (!this.senderPlayer.equals(this.targetPlayer))
            playerSendMsgComponentExchanger(this.senderPlayer, comment, ColorMap.YELLOW);
        playerSendMsgComponentExchanger(this.targetPlayer, comment, ColorMap.YELLOW);
    }
}
