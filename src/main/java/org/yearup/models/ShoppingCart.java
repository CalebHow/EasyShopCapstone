package org.yearup.models;

import org.yearup.data.ShoppingCartDao;

import java.math.BigDecimal;
import java.sql.SQLException;
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
//Methods down below from the implementation
    @Override
    public ShoppingCart getByUserId(int userId) throws SQLException {
        return null;
    }
    @Override
    public ShoppingCart addProductToCart(int productId, int userId) {
        return null;
    }
    @Override
    public ShoppingCart updateCartItem(int userId, int productId, ShoppingCartItem shoppingCartItem) {
        return null;
    }

    @Override
    public void saveCart(ShoppingCart cart) {
    }

    @Override
    public void clearCart(int userId) {
    }
}
