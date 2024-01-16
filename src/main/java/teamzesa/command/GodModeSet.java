package teamzesa.command;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import teamzesa.util.ComponentExchanger;
import teamzesa.util.Enum.ColorList;
import teamzesa.entity.User;
import teamzesa.util.userHandler.UserBuilder;
import teamzesa.util.userHandler.UserController;

import java.util.Optional;


public class GodModeSet extends ComponentExchanger implements CommandExecutor {
    private Player targetPlayer;
    private User targetUser;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Optional.ofNullable(Bukkit.getPlayer(args[0])).ifPresentOrElse(
            player  -> {
                this.targetPlayer = player;
                UserController userController = new UserController();
                User user = userController.readUser(player);

                this.targetUser = new UserBuilder(user)
                        .godMode(!user.godMode()) //godMode 변경
                        .buildAndUpdate();

                setGodEffect(this.targetPlayer,this.targetUser);
                sendCommment(sender);
            },
            ()  -> {
                if (sender instanceof Player player)
                    playerSendMsgComponentExchanger(player,"/god [플레이어 이름]",ColorList.RED);
                else Bukkit.getLogger().info("[R01] /god [플레이어 이름]");
            }
        );
        return true;
    }

    private void sendCommment(@NotNull CommandSender sender) {
        boolean senderInstance = sender instanceof Player;//true == player , false == consol
        String comment = "은 이제 " + (this.targetUser.godMode() ? "신" : "인간") + " 입니다.";

        if (!this.targetPlayer.equals(sender) && senderInstance)
            playerSendMsgComponentExchanger(sender, this.targetPlayer.getName() + comment, ColorList.ORANGE);

        if (!senderInstance)
            Bukkit.getLogger().info("[R01] " + targetPlayer.getName() + comment);

        playerSendMsgComponentExchanger(this.targetPlayer, "당신" + comment, ColorList.ORANGE);
    }

    public void setGodEffect(Player player, User user) {
        if (user.godMode())
            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION,100000000,0));
        else player.removePotionEffect(PotionEffectType.SATURATION);
    }
}
