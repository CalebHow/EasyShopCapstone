package org.yearup.controllers;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;
@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:8080")
public class CategoriesController {
    private final CategoryDao categoryDao;
    private final ProductDao productDao;

    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }
    @GetMapping
    public List<String> getAllCategories() {
        return categoryDao.getAllCategories();
    }
    @GetMapping("/3")
    public int getById(@PathVariable int id) {
        return categoryDao.getById(id);
    }
    @GetMapping("/3/products")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        return productDao.getProductsByCategoryId(categoryId);
    }
    @PostMapping
    @Secured("ROLE_ADMIN")
    public Category addCategory(@RequestBody Category category) {
        return categoryDao.create(category);
    }
    @PutMapping("/3")
    @Secured("ROLE_ADMIN")
    public void updateCategory(@PathVariable int id, @RequestBody Category category) {
        categoryDao.update(id, category);
    }
    @DeleteMapping("/3")
    @Secured("ROLE_ADMIN")
    public void deleteCategory(@PathVariable int id) {
        categoryDao.delete(id);
    }
}


