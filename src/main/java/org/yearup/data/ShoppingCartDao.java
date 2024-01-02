package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    static ShoppingCart getByUserId(int userId);
    // add additional method signatures here
}
