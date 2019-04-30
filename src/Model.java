import Entites.Book;
import Entites.CartElement;
import Entites.Order;
import Entites.User;

import java.util.ArrayList;
import java.util.HashMap;

public class Model implements IModel {
    @Override
    public boolean register() {
        return false;
    }

    @Override
    public User logIn() {
        return null;
    }

    @Override
    public boolean updateUser(User user) {
        return false;
    }

    @Override
    public ArrayList<Book> getAllBooks() {
        return null;
    }

    @Override
    public ArrayList<Book> searchForBook(HashMap<String, String> searchMap) {
        return null;
    }

    @Override
    public boolean addToCart(int bookId, int quantity, int userId) {
        return false;
    }

    @Override
    public boolean removeFromCart(int bookId, int quantity, int userId) {
        return false;
    }

    @Override
    public ArrayList<CartElement> getCart(int userId) {
        return null;
    }

    @Override
    public boolean checkout(int userId) {
        return false;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        return null;
    }

    @Override
    public boolean promote(int userId) {
        return false;
    }

    @Override
    public boolean addBook(Book book) {
        return false;
    }

    @Override
    public boolean modifyBook(Book newBook) {
        return false;
    }

    @Override
    public ArrayList<Order> getAllOrders() {
        return null;
    }

    @Override
    public boolean placeOrder(int bookId, int quantity) {
        return false;
    }

    @Override
    public boolean confirmOrder(int OrderId) {
        return false;
    }
}
