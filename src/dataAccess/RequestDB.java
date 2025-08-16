package dataAccess;

import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Category;
import model.Product;
import model.Supplier;
import model.Order;
import model.Person;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class RequestDB implements ProductDAO, OrderDAO {

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

    @Override
    /* Retourne la liste complète des produits avec toutes leurs infos.
     */
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


            }
            rs.close();
            statement.close();
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

    public void updateProduct(Product product) throws ConnectionException, TitleException {
        if (product == null || product.getName() == null || product.getName().isBlank()) {
            throw new TitleException(null, "Le nom du produit est obligatoire pour la mise à jour.");
        }

        final String sql =
                "UPDATE product SET " +
                        "  supplierName = ?, " +
                        "  categoryName = ?, " +
                        "  minQuantityDesired = ?, " +
                        "  stockQuantity = ?, " +
                        "  reorderQuantity = ?, " +
                        "  dateOfSale = ?, " +
                        "  isFrozen = ?, " +
                        "  description = ? " +
                        "WHERE name = ?;";

        try (PreparedStatement ps = SingletonConnexion.getInstance().prepareStatement(sql)) {
            ps.setString(1,  (product.getSupplier() != null) ? product.getSupplier().getName() : null);
            ps.setString(2,  (product.getCategory() != null) ? product.getCategory().getName() : null);
            ps.setInt(3,     product.getMinQuantityDesired());
            ps.setInt(4,     product.getStockQuantity());
            ps.setInt(5,     product.getReorderQuantity());
            ps.setDate(6,    (product.getDateOfSale() != null) ? java.sql.Date.valueOf(product.getDateOfSale()) : null);
            ps.setBoolean(7, product.isFrozen());
            ps.setString(8,  product.getDescription());
            ps.setString(9,  product.getName()); // PK immuable : on cherche par name

            int rows = ps.executeUpdate();
            if (rows == 0) {
                // rien mis à jour → produit inexistant
                throw new TitleException(product.getName(), "Produit introuvable : " + product.getName());
            }
        } catch (SQLException e) {
            // 23000 = violation de contrainte (FK supplier/category inexistants, etc.)
            if ("23000".equals(e.getSQLState())) {
                throw new TitleException(product.getName(),
                        "Mise à jour refusée (contrainte). Vérifie fournisseur/catégorie.");
            }
            throw new ConnectionException("Erreur DB (updateProduct) : " + e.getMessage());
        }
    }

    public void deleteProduct(String name) throws ConnectionException, TitleException {
        if (name == null || name.isBlank()) {
            throw new TitleException(null, "Le nom du produit est obligatoire pour la suppression.");
        }

        final String sql = "DELETE FROM product WHERE name = ?;";
        try (PreparedStatement ps = SingletonConnexion.getInstance().prepareStatement(sql)) {
            ps.setString(1, name);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new TitleException(name, "Produit introuvable : " + name);
            }
        } catch (SQLException e) {
            // 23000 = violation de contrainte FK (référencé par itemDetail, restockDetail, orderDetail, etc.)
            if ("23000".equals(e.getSQLState())) {
                throw new TitleException(name,
                        "Suppression impossible : ce produit est utilisé ailleurs (composition, réassort, etc.).");
            }
            throw new ConnectionException("Erreur DB (deleteProduct) : " + e.getMessage());
        }
    }


    // Mapper une ligne de ResultSet vers Order
    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        int number = rs.getInt("number");
        int customerId = rs.getInt("customerId");
        String paymentMethod = rs.getString("paymentMethod");
        boolean isTakeaway =rs.getBoolean("isTakeaway");

        java.sql.Date dCreation = rs.getDate("creationDate");
        java.sql.Date dDelivery = rs.getDate("deliveryDate");
        java.time.LocalDate creationDate = (dCreation != null) ? dCreation.toLocalDate() : null;
        java.time.LocalDate deliveryDate = (dDelivery != null) ? dDelivery.toLocalDate() : null;

        String commentary = rs.getString("commentary");
        String specificRequest = rs.getString("specificRequest");

        Customer customer = new Customer(customerId);

        Order order = new Order(number, customer, paymentMethod, isTakeaway, creationDate);
        order.setDeliveryDate(deliveryDate);
        order.setCommentary(commentary);
        order.setSpecificRequest(specificRequest);
        return order;
    }

    public ArrayList<model.Order> getAllOrders() throws ConnectionException, TitleException {
        ArrayList<model.Order> orders = new ArrayList<>();
        final String sql =
                "SELECT number, customerId, paymentMethod, isTakeaway, " +
                        "       creationDate, deliveryDate, commentary, specificRequest " +
                        "FROM `order`;";

        try (PreparedStatement ps = SingletonConnexion.getInstance().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                orders.add(mapRowToOrder(rs));
            }
            return orders;
        } catch (SQLException e) {
            throw new ConnectionException("Error accessing the database (getAllOrders): " + e.getMessage());
        }
    }

    public model.Order getOrderByNumber(int number) throws ConnectionException, TitleException {
        final String sql =
                "SELECT number, customerId, paymentMethod, isTakeaway, " +
                        "       creationDate, deliveryDate, commentary, specificRequest " +
                        "FROM `order` WHERE number = ?;";

        try (PreparedStatement ps = SingletonConnexion.getInstance().prepareStatement(sql)) {
            ps.setInt(1, number);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new TitleException(String.valueOf(number), "Commande introuvable : " + number);
                }
                return mapRowToOrder(rs);
            }
        } catch (SQLException e) {
            throw new ConnectionException("DB error (getOrderByNumber): " + e.getMessage());
        }
    }

    public void createOrder(model.Order order) throws ConnectionException, TitleException {
        if (order == null || order.getcustomer() == null) {
            throw new TitleException(null, "Le client est obligatoire pour créer une commande.");
        }

        final String sql =
                "INSERT INTO `order` " +
                        "(customerId, paymentMethod, isTakeaway, creationDate, deliveryDate, commentary, specificRequest) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement ps = SingletonConnexion.getInstance().prepareStatement(sql)) {

            ps.setInt(1, order.getcustomer().getPersonID());
            ps.setString(2, order.getPaymentMethod());

            // Boolean nullable -> setNull si null
            if (order.isTakeaway() == null) {
                ps.setNull(3, java.sql.Types.BOOLEAN);
            } else {
                ps.setBoolean(3, order.isTakeaway());
            }

            // LocalDate -> java.sql.Date
            ps.setDate(4, (order.getCreationDate() != null) ? java.sql.Date.valueOf(order.getCreationDate()) : null);
            ps.setDate(5, (order.getDeliveryDate() != null) ? java.sql.Date.valueOf(order.getDeliveryDate()) : null);

            ps.setString(6, order.getCommentary());
            ps.setString(7, order.getSpecificRequest());

            ps.executeUpdate();

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                throw new TitleException(null, "Création refusée (contrainte). Vérifie le client (customerId).");
            }
            throw new ConnectionException("Erreur DB (createOrder): " + e.getMessage());
        }
    }

    public void updateOrder(model.Order order) throws ConnectionException, TitleException {
        if (order == null || order.getNumber() <= 0) {
            throw new TitleException(null, "Le numéro de commande est obligatoire pour la mise à jour.");
        }
        if (order.getcustomer() == null) {
            throw new TitleException(null, "Le client est obligatoire.");
        }

        final String sql =
                "UPDATE `order` SET " +
                        "  customerId = ?, " +
                        "  paymentMethod = ?, " +
                        "  isTakeaway = ?, " +
                        "  creationDate = ?, " +
                        "  deliveryDate = ?, " +
                        "  commentary = ?, " +
                        "  specificRequest = ? " +
                        "WHERE number = ?;";

        try (PreparedStatement ps = SingletonConnexion.getInstance().prepareStatement(sql)) {
            ps.setInt(1, order.getcustomer().getPersonID());
            ps.setString(2, order.getPaymentMethod());

            if (order.isTakeaway() == null) {
                ps.setNull(3, java.sql.Types.BOOLEAN);
            } else {
                ps.setBoolean(3, order.isTakeaway());
            }

            ps.setDate(4, (order.getCreationDate() != null) ? java.sql.Date.valueOf(order.getCreationDate()) : null);
            ps.setDate(5, (order.getDeliveryDate() != null) ? java.sql.Date.valueOf(order.getDeliveryDate()) : null);

            ps.setString(6, order.getCommentary());
            ps.setString(7, order.getSpecificRequest());
            ps.setInt(8, order.getNumber());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new TitleException(String.valueOf(order.getNumber()), "Commande introuvable : " + order.getNumber());
            }
        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                throw new TitleException(String.valueOf(order.getNumber()),
                        "Mise à jour refusée (contrainte). Vérifie le client (customerId).");
            }
            throw new ConnectionException("Erreur DB (updateOrder): " + e.getMessage());
        }
    }

    public void deleteOrder(int number) throws ConnectionException, TitleException {
        if (number <= 0) {
            throw new TitleException(null, "Le numéro de commande est obligatoire pour la suppression.");
        }
        final String sql = "DELETE FROM `order` WHERE number = ?;";

        try (PreparedStatement ps = SingletonConnexion.getInstance().prepareStatement(sql)) {
            ps.setInt(1, number);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new TitleException(String.valueOf(number), "Commande introuvable : " + number);
            }
        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                throw new TitleException(String.valueOf(number),
                        "Suppression impossible : la commande possède des détails (orderDetail).");
            }
            throw new ConnectionException("Erreur DB (deleteOrder): " + e.getMessage());
        }
    }

}


