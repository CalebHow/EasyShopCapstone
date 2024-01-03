package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartDao
{
    static ShoppingCart getByUserId(int userId) {
        return null;
    }

    void createOrUpdate(ShoppingCart shoppingCart);

    void addItem(int userId, ShoppingCartItem item);

    void updateItemQuantity(int userId, int itemId, int quantity);

    void removeItem(int userId, int itemId);

    List<ShoppingCartItem> getCartItems(int userId);

    void clearCart(int userId);

    double calculateTotalCost(int userId);

    boolean isCartEmpty(int userId);
}
