package teamzesa.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import teamzesa.ComponentExchanger;
import teamzesa.dataValue.ColorList;
import teamzesa.dataValue.userData.User;
import teamzesa.dataValue.userData.UserMapHandler;
import teamzesa.resgisterEvent.EventExecutor;

import java.util.Optional;

public class UserObjectChecker implements CommandExecutor, EventExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Optional.ofNullable(UserMapHandler.getUserMapHandler().getUser(args[0]))
                .ifPresentOrElse(
                    existUser -> sendComment(sender, existUser),
                    () -> sendComment(sender, "존재하지 않는 유저")
                );
        return true;
    }

    private void sendComment(CommandSender sender, User user) {
        if (sender instanceof Player)
            ComponentExchanger.playerAnnouncer(sender, user.toString(), ColorList.YELLOW);
        else Bukkit.getLogger().info(user.toString());
    }

    private void sendComment(CommandSender sender, String comment) {
        if (sender instanceof Player)
            ComponentExchanger.playerAnnouncer(sender, comment, ColorList.YELLOW);
        else Bukkit.getLogger().info(comment);
    }
}
