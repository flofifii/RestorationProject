package dataAccess;

import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Order;

import java.util.ArrayList;

public interface OrderDAO {
    ArrayList<Order> getAllOrders() throws ConnectionException, TitleException;
    Order getOrderByNumber(int number) throws ConnectionException, TitleException;
    void createOrder(Order order) throws ConnectionException, TitleException;
    void updateOrder(Order order) throws ConnectionException, TitleException;
    void deleteOrder(int number) throws ConnectionException, TitleException;
}