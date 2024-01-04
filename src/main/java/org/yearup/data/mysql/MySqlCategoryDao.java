package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<String> getAllCategories()
    {
        List<String> category = new ArrayList<>();
      ;

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM category WHERE name = ?")) {


                preparedStatement.setString(1, String.valueOf(category));
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int categoryId = resultSet.getInt("category_id");
                    String description = resultSet.getString("description");
                    String name = resultSet.getString("name");
                    String fullCategoryInfo = categoryId + " " + name + " " + description;
                    category.add(fullCategoryInfo);

                    for (String categoryI: category )
                    {
                        System.out.println(categoryI);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return category;
    }


    @Override
    public int getById(int categoryId) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM category WHERE category_id = ?")) {
                preparedStatement.setInt(1, (categoryId));
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapRow(resultSet).getCategoryId();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return categoryId;
    }

    @Override
    public Category create(Category category) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO categories (name, description, category_id) Values (?, ?, ?)")) {

                preparedStatement.setString(1, category.getName());
                preparedStatement.setString(2, category.getDescription());
                preparedStatement.setInt(3, (category.getCategoryId()));
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0){
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                        if (generatedKeys.next()){

                        }
                    }
                }
        }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }
    @Override
    public void update(int categoryId, Category category)
    {
        // update category
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

} catch (SQLException e) {
            throw new RuntimeException(e);
        }