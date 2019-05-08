package Model;

import Entites.Book;
import Entites.CartElement;
import Entites.Order;
import Entites.User;

import java.util.ArrayList;
import java.util.HashMap;

public interface IModel {
    //todo file constants
    boolean register(User user);
    User logIn(String name, String password);
    boolean updateUser(User user);
    ArrayList<Book> getStartBooks();
    int getNumberOfPages();
    ArrayList<Book> getPage(int pageNumber, int limit);
    ArrayList<Book> searchForBooks(HashMap<String,String> searchMap);
    boolean addToCart(int bookId, int quantity, int userId);
    boolean removeFromCart(int bookId, int userId);
    ArrayList<CartElement> getCart(int userId);
    boolean checkout(int userId);
    Book getBookById(int bookId);
    Book getBookByTitle(String title);

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
