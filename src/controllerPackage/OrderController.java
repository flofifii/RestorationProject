package controllerPackage;

import businessPackage.OrderManager;
import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Order;

import java.util.ArrayList;

public class OrderController {
    private OrderManager manager;

    public OrderController() {
        setManager(new OrderManager());
    }

    public void setManager(OrderManager manager) {
        this.manager = manager;
    }

    public ArrayList<Order> getAllOrders() throws ConnectionException, TitleException {
        return manager.getAllOrders();
    }

    public Order getOrderByNumber(int number) throws ConnectionException, TitleException {
        return manager.getOrderByNumber(number);
    }

    public void createOrder(Order order) throws ConnectionException, TitleException {
        manager.createOrder(order);
    }

    public void updateOrder(Order order) throws ConnectionException, TitleException {
        manager.updateOrder(order);
    }

    public void deleteOrder(int number) throws ConnectionException, TitleException {
        manager.deleteOrder(number);
    }
}
