package org.yearup.data.mysql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Component
public class MySqlCartDao implements ShoppingCartDao {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProductDao productDao;

    private ShoppingCart UpdateAndBuildCart(String query, List<Object> params) throws SQLException {
        Map<Integer, ShoppingCartItem> items = new HashMap<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int parameterIndex = 1;
            for (Object parameter : params) {
                preparedStatement.setObject(parameterIndex++, parameter);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int amount = resultSet.getInt("amount");
                    int productId = resultSet.getInt("product_id");
                    Product product = productDao.getById(productId);
                    ShoppingCartItem item = new ShoppingCartItem();
                    items.put(productId, item);
                }
            }
        }

        return new ShoppingCart();
    }
    // Retrieves the shopping cart for a given user ID from the database
    @Override
    public ShoppingCart getByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM shopping_cart WHERE user_id = ?";
        return UpdateAndBuildCart(query, Collections.singletonList(userId));
    }

    // Adds a product to the user's shopping cart in the database
    @Override
    public ShoppingCart addProductToCart(int userId, int productId) {
        String insertQuery = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateQuery = "ON DUPLICATE KEY UPDATE quantity = quantity + 1";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery + " " + updateQuery)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, productId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            return getByUserId(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Clears the entire shopping cart for a given user ID in the database
    @Override
    public void clearCart(int userId) {
        String query = "DELETE FROM shopping_cart WHERE user_id = ?";
        executeAndUpdate(query, userId);
    }

    // Updates the quantity of a specific product in the user's shopping cart in the database
    @Override
    public ShoppingCart updateCartItem(int userId, int productId, ShoppingCartItem shoppingCartItem) {
        try {
            ShoppingCart shoppingCart = getByUserId(userId);

            if (shoppingCart == null) {
                throw new RuntimeException("Shopping cart not found for user: " + userId);
            }

            ShoppingCartItem matchedItem = shoppingCart.getItems().get(productId);

            if (matchedItem != null) {
                matchedItem.setQuantity(shoppingCartItem.getQuantity());
                String updateQuery = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

                try (Connection connection = dataSource.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

                    preparedStatement.setInt(1, matchedItem.getQuantity());
                    preparedStatement.setInt(2, userId);
                    preparedStatement.setInt(3, productId);
                    preparedStatement.executeUpdate();
                }

            } else {
                throw new RuntimeException("Product not found in the shopping cart: " + productId);
            }

            return shoppingCart;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Saves the shopping cart (not implemented in this version)
    @Override
    public void saveCart(ShoppingCart cart) {
        String saveQuery = "INSERT INTO saved_carts (user_id, product_id, quantity) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE quantity = VALUES(quantity)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {

            for (Map.Entry<Integer, ShoppingCartItem> entry : cart.getItems().entrySet()) {
                int productId = entry.getKey();
                int quantity = entry.getValue().getQuantity();

                List<Object> saveParams = new ArrayList<>();
                saveParams.add(cart.getItems());
                saveParams.add(productId);
                saveParams.add(quantity);

                setParameters(preparedStatement, saveParams);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save the shopping cart.", e);
        }
    }

    private void setParameters(PreparedStatement preparedStatement, List<Object> params) throws SQLException {
        int index = 1;
        for (Object param : params) {
            preparedStatement.setObject(index++, param);
        }
    }
    @Override
    public void executeAndUpdate(String query, Object... params) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            int index = 1;
            for (Object param : params) {
                preparedStatement.setObject(index++, param);
            }

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to execute update query.", e);
        }
    }
}
