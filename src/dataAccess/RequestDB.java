package dataAccess;

import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Product;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.ResultSet;

public class RequestDB implements ProductDAO {
    private SingletonConnexion singletonConnexion;

    public RequestDB() {
        singletonConnexion = new SingletonConnexion();
    }

    public ArrayList<String> getProduct() throws ConnectionException {
        ArrayList<String> products = new ArrayList<>();
        try {
            String sqlinstruction =
                    "SELECT product.name" +
                    "FROM PRODUCT u; ";
            PreparedStatement statement = SingletonConnexion.getInstance().prepareStatement(sqlinstruction);
            ResultSet data = statement.executeQuery();
            while(data.next()) {
                products.add(data.getString("name"));
            }
        } catch (SQLException e) {
            throw new ConnectionException("Error accessing the database  : " + e.getMessage());
        }
        return products;
    }

    public ArrayList<Product> getAllProducts() throws ConnectionException, TitleException {
        ArrayList<Product> products = new ArrayList<>();
        try {
            String sqlInstruction =
                    "SELECT * " +
                            "FROM product;";

            PreparedStatement statement = SingletonConnexion.getInstance().prepareStatement(sqlInstruction);
            ResultSet data = statement.executeQuery();
            int product_id;
            String name;
            int supplier_id;
            int category_id;
            int min_quantity_desired;
            int stock_quantity;
            int reorder_quantity;
            LocalDate date_of_sale;
            boolean is_frozen;

            while (data.next()) {
                product_id = data.getInt("product_id");
                name = data.getString("name");
                supplier_id = data.getInt("supplier_id");
                category_id = data.getInt("category_id");
                min_quantity_desired = data.getInt("min_quantity_desired");
                stock_quantity = data.getInt("stock_quantity");
                reorder_quantity = data.getInt("reorder_quantity");
                java.sql.Date sqlDate = data.getDate("date_of_sale");
                is_frozen = data.getBoolean("is_frozen");
                Product product = new Product(name, product_id, supplier_id, category_id, min_quantity_desired, stock_quantity, reorder_quantity, sqlDate);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new ConnectionException("Error accessing the database  : " + e.getMessage());
        }
        return products;
    }
}
