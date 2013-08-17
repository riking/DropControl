package com.github.riking.dropcontrol.matcher;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AmountMatcher implements ItemMatcher {
    private Operation operation;
    private int amount;

    public AmountMatcher(String string) {
        amount = Integer.parseInt(string.substring(1));
        char op = string.charAt(0);
        if (op == '=') {
            operation = Operation.EQUAL;
        } else if (op == '<') {
            operation = Operation.LESSTHAN;
        } else if (op == '>') {
            operation = Operation.GREATERTHAN;
        } else if (op == '!') {
            operation = Operation.NOT;
        } else {
            throw new IllegalArgumentException(Character.toString(op) + " is not a valid operation (valid: =<>!)");
        }
    }

    @Override
    public String getSerializationObject() {
        return operation.character + Integer.toString(amount);
    }

    @Override
    public boolean matches(ItemStack item, Player player) {
        switch (operation) {
            case EQUAL:
                return item.getAmount() == amount;
            case GREATERTHAN:
                return item.getAmount() > amount;
            case LESSTHAN:
                return item.getAmount() < amount;
            case NOT:
                return item.getAmount() != amount;
        }
        return true; // shouldn't happen
    }

    @Override
    public String getSerializationKey() {
        return "totalitem";
    }

    enum Operation {
        EQUAL('='),
        LESSTHAN('<'),
        GREATERTHAN('>'),
        NOT('!');

        char character;
        private Operation(char ch) {
            character = ch;
        }
    }
}
