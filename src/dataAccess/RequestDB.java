package dataAccess;

import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Category;
import model.Product;
import model.Supplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class RequestDB implements ProductDAO {

    public RequestDB() {
        // Pas besoin d'instancier SingletonConnexion
    }

    /**
     * Retourne la liste des noms de produits.
     */
    public ArrayList<String> getProduct() throws ConnectionException {
        ArrayList<String> products = new ArrayList<>();
        try {
            String sql =
                    "SELECT name " +
                            "FROM product;";

            PreparedStatement statement = SingletonConnexion.getInstance().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                products.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new ConnectionException("Error accessing the database: " + e.getMessage());
        }
        return products;
    }

    /**
     * Retourne la liste complète des produits avec toutes leurs infos.
     */
    @Override
    public ArrayList<Product> getAllProducts() throws ConnectionException, TitleException {
        ArrayList<Product> products = new ArrayList<>();
        try {
            String sql =
                    "SELECT name, supplierName, categoryName, " +
                            "       minQuantityDesired, stockQuantity, reorderQuantity, " +
                            "       dateOfSale, isFrozen, description " +
                            "FROM product;";

            PreparedStatement statement = SingletonConnexion.getInstance().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String supplierName = rs.getString("supplierName");
                String categoryName = rs.getString("categoryName");
                int minQuantityDesired = rs.getInt("minQuantityDesired");
                int stockQuantity = rs.getInt("stockQuantity");
                int reorderQuantity = rs.getInt("reorderQuantity");

                java.sql.Date sqlDate = rs.getDate("dateOfSale");
                LocalDate dateOfSale = (sqlDate != null) ? sqlDate.toLocalDate() : null;

                boolean isFrozen = rs.getBoolean("isFrozen");
                String description = rs.getString("description");

                // Création des objets liés
                Supplier supplier = new Supplier(supplierName);

                Category category = new Category(categoryName);

                // Création du produit
                Product product = new Product(
                        name,
                        supplier,
                        category,
                        minQuantityDesired,
                        stockQuantity,
                        reorderQuantity,
                        dateOfSale,
                        isFrozen,
                        description
                );

                products.add(product);
            }
        } catch (SQLException e) {
            throw new ConnectionException("Error accessing the database: " + e.getMessage());
        }
        return products;
    }
}