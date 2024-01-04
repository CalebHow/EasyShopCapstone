package org.yearup.models;

import org.yearup.data.ShoppingCartDao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart implements ShoppingCartDao
{
    private Map<Integer, ShoppingCartItem> items = new HashMap<>();

    public Map<Integer, ShoppingCartItem> getItems()
    {
        return items;
    }

    public void setItems(Map<Integer, ShoppingCartItem> items)
    {
        this.items = items;
    }
    public boolean contains(int productId)
    {
        return items.containsKey(productId);
    }

    public void add(ShoppingCartItem item)
    {
        items.put(item.getProductId(), item);
    }

    public ShoppingCartItem get(int productId)
    {
        return items.get(productId);
    }

    public BigDecimal getTotal()
    {
        BigDecimal total = items.values()
                                .stream()
                                .map(i -> i.getLineTotal())
                                .reduce( BigDecimal.ZERO, (lineTotal, subTotal) -> subTotal.add(lineTotal));

        return total;
    }
    @Override
    public void createOrUpdate(ShoppingCart shoppingCart) {

    }

    @Override
    public void addItem(int userId, ShoppingCartItem item) {

    }

    @Override
    public void updateItemQuantity(int userId, int itemId, int quantity) {

    }

    @Override
    public void removeItem(int userId, int itemId) {

    }

    @Override
    public List<ShoppingCartItem> getCartItems(int userId) {
        return null;
    }

    @Override
    public void clearCart(int userId) {

    }

    @Override
    public double calculateTotalCost(int userId) {
        return 0;
    }

    @Override
    public boolean isCartEmpty(int userId) {
        return false;
    }

    @Override
    public void save(ShoppingCart cart) {

    }
}
