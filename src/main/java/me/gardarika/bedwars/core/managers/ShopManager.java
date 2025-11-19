package me.gardarika.bedwars.core.managers;

import me.gardarika.bedwars.core.shop.ShopItem;
import me.gardarika.bedwars.core.shop.ShopTransaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopManager {

    public void buyItem(ShopItem shopItem, Player buyer, boolean isShifted){
        ShopTransaction transaction = new ShopTransaction();
        Inventory playerInventory = buyer.getInventory();

        if (transaction.checkMinimumAmount(playerInventory)){
            if (isShifted){

            } else {
                chargePlayer(buyer, shopItem, 1);
            }
        } else {
            buyer.sendMessage("Not enough resources for this item");
        }
        playerInventory
        for (ItemStack itemStack : playerInventory.getContents()){

        }
    }

    private void chargePlayer(Player player, ShopItem item, int amount){

    }
}
