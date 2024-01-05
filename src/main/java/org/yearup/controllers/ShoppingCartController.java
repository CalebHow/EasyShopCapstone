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
    @Autowired
    private ShoppingCartDao shoppingCartDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ProductDao productDao;
    @GetMapping ("/cart")
    //^ I FOUND THE PATHING ERROR!!!!!
    public ShoppingCart getCart(Principal principal) {
        try {
            if (principal != null) {
                String userName = principal.getName();
                User user = userDao.getByUserName(userName);
                int userId = user.getId();
                return shoppingCartDao.getByUserId(userId);
            }
        } catch (Exception e) {
            System.out.println("Ruh-Roh");
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... you're bad at this >=)");
        }
        return null;
    }
    @PostMapping("cart/products/{productId}")
    public ShoppingCart addProductToCart(@PathVariable int productId,ShoppingCartItem item, Principal principal)
    {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);

            if(cart == null) {
                cart = new ShoppingCart();

            } if(cart.contains(productId)) {
                ShoppingCartItem existingItem = cart.get(productId);
                existingItem.setQuantity(existingItem.getQuantity() + 1);

            } else {
                ShoppingCartItem newItem = new ShoppingCartItem();
                newItem.setProduct(productDao.getById(productId));
                newItem.setQuantity(1);
                cart.add(newItem);
            }
            shoppingCartDao.saveCart(cart);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... your bad >=).");
        }
        return null;
    }
    @PutMapping("cart/products/{productId}")
    public void updateProductInCart(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal) {
        try {
            if (principal == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "BEGONE! GET THEE BEHIND ME SATAN!");
            }
            try {
                String userName = principal.getName();
                User user = userDao.getByUserName(userName);
                int userId = user.getId();
                ShoppingCart cart = shoppingCartDao.getByUserId(userId);
                if (cart.contains(productId)) {
                    ShoppingCartItem existingItem = cart.get(productId);
                    existingItem.setQuantity(item.getQuantity());
                } else {
                    item.setProduct(productDao.getById(productId));
                    cart.add(item);
                }
                shoppingCartDao.saveCart(cart);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
            }
        } catch (ResponseStatusException e) {
            throw new RuntimeException(e);
        }
    }
    @DeleteMapping("/cart")
    public void clearCart(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You don't exist.");
        }
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            cart.getItems().clear();
            shoppingCartDao.saveCart(cart);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
