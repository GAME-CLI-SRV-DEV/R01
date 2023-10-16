package teamzesa.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import teamzesa.ComponentExchanger;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TotemStacking extends ComponentExchanger implements CommandExecutor {
    private final Material TOTEM = Material.TOTEM_OF_UNDYING;
    private final int STACK = 64;
    private final int MINIMUM = 1; // 합칠 수 있는 최소 단위 +1
    private Player player;
    private PlayerInventory playerInventory;
    private List<Integer> totemCountData;

    @Override
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command command,@NotNull String label, String[] args) {
        this.player = (Player) sender;
        this.playerInventory = this.player.getInventory();
        getAllOfPlayerTotems();

        if (validationInventory()) return false;
        removeTotemInInv();
        supplyTotems();
        return true;
    }

    private void getAllOfPlayerTotems() {
        this.totemCountData = Arrays.stream(this.playerInventory.getContents())
                .filter(Objects::nonNull)
                .filter(item -> item.getType().equals(TOTEM))
                .map(ItemStack::getAmount)
                .toList();
    }

    private void supplyTotems() {
        int totalAmount = totemCountData.stream()
                .mapToInt(Integer::intValue)
                .sum();

//        n <= STACK
        if (totalAmount <= STACK) {
            this.playerInventory.setItemInOffHand(new ItemStack(TOTEM,totalAmount));
        } else {
//        n >= STACK
            this.playerInventory.setItemInOffHand(new ItemStack(TOTEM, STACK));
            this.playerInventory.addItem(new ItemStack(TOTEM, totalAmount - STACK));
        }

        playerAnnouncer(this.player,"토템을 합쳤습니다.", Color.YELLOW);
    }

    private void removeTotemInInv() {
        this.playerInventory.remove(TOTEM);

//        전체 아이템 창 정보 다 가져옴 armour , offHand Whatever
        Optional <ItemStack> tempHelmetStuff = Optional.ofNullable(this.playerInventory.getHelmet());
        tempHelmetStuff.ifPresent(helmet -> {
            if (helmet.getType() == TOTEM)
                this.playerInventory.setHelmet(null);
        });

        Optional <ItemStack> tempOffHandStuff = Optional.of(this.playerInventory.getItemInOffHand());
        tempOffHandStuff.ifPresent(offhand -> {
            if (offhand.getType() == TOTEM)
                this.playerInventory.setItemInOffHand(null);
            else
                this.playerInventory.addItem(tempOffHandStuff.get());
        });
    }

    private boolean validationInventory() {
        StringBuilder message = new StringBuilder();

        if (this.totemCountData.isEmpty())
            message.append("인벤토리에 토템이 없습니다.");

        else if (maxTotemCnt() < 2 && minTotemCnt() < 1)
            message.append("합칠 토템이 없습니다.");

        else if (maxTotemCnt() < 2 && minTotemCnt() < 2)
            message.append("2개 이상의 토템을 가지고 있으셔야 합니다.");

        if (message.isEmpty())
            return false;

        playerAnnouncer(this.player, message.toString(), Color.RED);
        return true;
    }

    private long maxTotemCnt() {
        return this.totemCountData.stream()
                .filter(cnt -> cnt >= MINIMUM && cnt < STACK)
                .count();
    }

    private long minTotemCnt() {
        return this.totemCountData.stream()
                .filter(cnt -> cnt == MINIMUM)
                .count();
    }
}