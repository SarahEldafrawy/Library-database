import Entites.Book;
import Entites.CartElement;
import Entites.Order;
import Entites.User;

import java.util.ArrayList;
import java.util.HashMap;

public interface IModel {
    //todo file constants
    boolean register();
    User logIn();
    boolean updateUser(User user);
    ArrayList<Book> getAllBooks();
    ArrayList<Book> searchForBook(HashMap<String,String> searchMap);
    boolean addToCart(int bookId, int quantity, int userId);
    boolean removeFromCart(int bookId, int quantity, int userId);
    ArrayList<CartElement> getCart(int userId);
    boolean checkout(int userId);

    //manager functions
    ArrayList<User> getAllUsers();
    boolean promote(int userId);
    boolean addBook(Book book);
    boolean modifyBook(Book newBook);
    ArrayList<Order> getAllOrders();
    boolean placeOrder(int bookId , int quantity);
    boolean confirmOrder(int OrderId);
    // functions that return reports


}
