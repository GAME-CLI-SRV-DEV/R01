package teamzesa.command.ModeratorCommand;

import org.apache.commons.lang3.BooleanUtils;
import org.bukkit.Tag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.tag.DamageTypeTags;
import org.jetbrains.annotations.NotNull;
import teamzesa.Enum.Enhance.EnhanceItemUndoInformation;
import teamzesa.command.register.CommandRegisterSection;
import teamzesa.event.Enhance.EnhanceUtil;
import teamzesa.Enum.ColorList;
import teamzesa.Enum.CommandType;

import java.util.Map;

import static org.yaml.snakeyaml.tokens.Token.ID.Tag;

public class EnhanceSet extends CommandRegisterSection {

    public EnhanceSet() {
        super(CommandType.ENHANCE_SET);
    }

    @Override
    public boolean onCommand(final @NotNull CommandSender commandSender,
                             final @NotNull Command command,
                             final @NotNull String s,
                             final @NotNull String[] strings) {

        Player player = (Player) commandSender;

        if (BooleanUtils.isFalse(commandSender.isOp())) {
            playerSendMsgComponentExchanger(player, "해당 명령어는 플레이어가 사용할 수 없습니다.", ColorList.RED);
            return false;
        }

        int enhanceLevel = Integer.parseInt(strings[0]);
        if (enhanceLevel < 0 || enhanceLevel > 10) {
            playerSendMsgComponentExchanger(player, "0 ~ 10 사이 값만 대입 가능합니다.", ColorList.RED);
            return false;
        }

        ItemStack targetItem = player.getInventory().getItemInMainHand();
        ItemMeta targetItemMeta = targetItem.getItemMeta();
        EnhanceItemUndoInformation itemInformation = EnhanceItemUndoInformation.findByMaterial(targetItem);

        if (enhanceLevel == 0) {
            ItemStack undoItem = new ItemStack(targetItem.getType(), 1);
            Map<Enchantment, Integer> targetItemEnchant = targetItem.getEnchantments();

            undoItem.addEnchantments(targetItemEnchant);
            player.getInventory().setItemInMainHand(undoItem);
            return true;
        }

        else {
            targetItemMeta.setCustomModelData(0);
            targetItemMeta.setUnbreakable(false);
            targetItemMeta.setDamageResistant(null);
            targetItemMeta.setRarity(itemInformation.getItemRarity());

            targetItem.setItemMeta(targetItemMeta);
        }

        try {
            EnhanceUtil.increaseEnhanceItemLevel(targetItem, enhanceLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        playerSendMsgComponentExchanger(player, getComment(enhanceLevel), ColorList.GREEN);
        return true;
    }

    private String getComment(int enhanceLevel) {
        return switch (enhanceLevel) {
            case 0 -> "강화를 초기화 했습니다.";
            case 10 -> "최고 레벨로 강화했습니다.";
            default -> enhanceLevel + "강 으로 강화했습니다.";
        };
    }
}
