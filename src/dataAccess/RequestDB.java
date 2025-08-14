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

    //  Mapper une ligne de ResultSet vers Product
    private Product mapRowToProduct(ResultSet rs) throws SQLException {
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

        Supplier supplier = new Supplier(supplierName);

        Category category = new Category(categoryName);

        return new Product(
                name, supplier, category,
                minQuantityDesired, stockQuantity, reorderQuantity,
                dateOfSale, isFrozen, description
        );
    }

    /*
     * Retourne la liste des noms de produits.
     */
    /*public ArrayList<String> getProduct() throws ConnectionException {
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
    */


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
                // Création du produit
                Product p = mapRowToProduct(rs);

                products.add(p);

                rs.close();
                statement.close();
            }
        } catch (SQLException e) {
            throw new ConnectionException("Error accessing the database: " + e.getMessage());
        }
        return products;
    }

    public Product getProductByName(String name) throws ConnectionException, TitleException {
        try {
            String sql = "SELECT name, supplierName, categoryName, " +
                    "       minQuantityDesired, stockQuantity, reorderQuantity, " +
                    "       dateOfSale, isFrozen, description " +
                    "FROM product WHERE name = ?;";
            PreparedStatement statement = SingletonConnexion.getInstance().prepareStatement(sql);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                throw new TitleException(name, "Produit introuvable : " + name);
            }

            Product p = mapRowToProduct(rs);
            rs.close();
            statement.close();
            return p;
        } catch (SQLException e) {
            throw new ConnectionException("DB error (getProductByName): " + e.getMessage());
        }
    }

    public void createProduct(Product product) throws ConnectionException, TitleException {
        if (product == null || product.getName() == null || product.getName().isBlank()) {
            throw new TitleException(null, "Le nom du produit est obligatoire.");
        }

        try {
            String sql = "INSERT INTO product " +
                    "(name, supplierName, categoryName, minQuantityDesired, stockQuantity, reorderQuantity, dateOfSale, isFrozen, description) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            // les '?' servent de sécurité aux injection sql

            PreparedStatement statement = SingletonConnexion.getInstance().prepareStatement(sql);
            statement.setString(1, product.getName());
            statement.setString(2, (product.getSupplier() != null) ? product.getSupplier().getName() : null);
            statement.setString(3, (product.getCategory() != null) ? product.getCategory().getName() : null);
            statement.setInt(4, product.getMinQuantityDesired());
            statement.setInt(5, product.getStockQuantity());
            statement.setInt(6, product.getReorderQuantity());
            statement.setDate(7, (product.getDateOfSale() != null) ? java.sql.Date.valueOf(product.getDateOfSale()) : null);
            statement.setBoolean(8, product.isFrozen());
            statement.setString(9, product.getDescription());

            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            // 23000 = violation de contrainte (clé primaire dupliquée ou FK inexistante)
            if ("23000".equals(e.getSQLState())) {
                throw new TitleException(product.getName(),
                        "Création impossible : ce nom existe déjà ou le fournisseur/catégorie n'existe pas.");
            }
            throw new ConnectionException("Erreur lors de la création du produit : " + e.getMessage());
        }
    }
}