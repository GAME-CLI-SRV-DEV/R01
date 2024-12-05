package teamzesa.command;

import net.kyori.adventure.title.Title;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import teamzesa.command.register.CommandRegisterSection;
import teamzesa.util.Enum.ColorList;
import teamzesa.util.Enum.CommandType;

public class Astn extends CommandRegisterSection {

    public Astn() {
        super(CommandType.ASTN);
    }

    @Override
    public boolean onCommand(final @NotNull CommandSender commandSender,
                             final @NotNull Command command,
                             final @NotNull String s,
                             final @NotNull String[] strings) {

        Player player = (Player) commandSender;
        Title title = Title.title(
                componentExchanger("최신버전 무정부 플라이 생야생", ColorList.DISCORD_COLOR),
                componentExchanger("Astn.kr", ColorList.ORANGE)
        );
        player.showTitle(title);
        return true;
    }
}