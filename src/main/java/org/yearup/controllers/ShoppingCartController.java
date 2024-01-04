package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("/cart")
public class ShoppingCartController {
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            return ShoppingCartDao.getByUserId(userId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
@Autowired
public ShoppingCartController(ShoppingCartDao shoppingCartDao){
        this.shoppingCartDao = shoppingCartDao;
}

    @PostMapping("/products/{productId}")
    public void addProductToCart(@PathVariable int productId,ShoppingCartItem item, Principal principal)
    {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            ShoppingCart cart = ShoppingCartDao.getByUserId(userId);
            if(cart == null) {
                cart = new ShoppingCart();
            } if(cart.contains(productId)) {
                ShoppingCartItem existingItem = cart.get(productId);
                existingItem.setQuantity(existingItem.getQuantity() + 1);
            } else {
                ShoppingCartItem newItem = new ShoppingCartItem();
                newItem.setProduct(productDao.getById(productId));
                newItem.setQuantity(1);
                cart.add(item);
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("/products/{productId}")
    public void updateProductInCart(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            ShoppingCart cart = ShoppingCartDao.getByUserId(userId);
            if (cart.contains(productId)) {
                ShoppingCartItem existingItem = cart.get(productId);
                existingItem.setQuantity(existingItem.getQuantity() + 1);
                cart.add(item);
            } else {
                item.setProduct(productDao.getById(productId));
                item.setQuantity(1);
                cart.add(item);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }


    @DeleteMapping("/products/{productId}")
    public void clearCart(Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            ShoppingCart cart = ShoppingCartDao.getByUserId(userId);
            cart.getItems().clear();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
