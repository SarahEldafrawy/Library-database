package Model;

import Entites.Book;
import Entites.CartElement;
import Entites.Order;
import Entites.User;
import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;



public class Model implements IModel {
    private Paginator pagination;
    private ConnectionHandler connectionHandler;
    private SQLCommands sQlCommands;

    public Model() throws SQLException, ClassNotFoundException {
        connectionHandler = new ConnectionHandler();
        connectionHandler.startConnection();
        pagination = new Paginator();
        sQlCommands = new SQLCommands();
    }
    @Override
    public boolean register(User user) {
        //TODO implement register
        try {
            ResultSet rs = connectionHandler.executeQuery("Select * From BOOK");

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return false;
    }



    @Override
    public User logIn(String email , String password) {
        String query =sQlCommands.logInUser(email , password);
        try {
            ResultSet resultSet = connectionHandler.executeQuery(query);
            User user = null;
            while (resultSet.next()){
                user = new User();
                user.setUserId(resultSet.getInt("user_id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmailAddress(resultSet.getString("email_address"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                user.setShippingAddress(resultSet.getString("shipping_address"));
                user.setPassword(resultSet.getString("password"));
                user.setPromoted(resultSet.getBoolean("promoted"));
                return user;
            }

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return null;
        }
        return null;
    }


    @Override
    public boolean updateUser(User user) {
        String query = sQlCommands.updateUser(user);
        return update(query);
    }

    @Override
    public ArrayList<Book> getStartBooks() {
        String query = sQlCommands.selectFromBooks(pagination.getLimit(), 0);
        ArrayList<Book> books = new ArrayList<>();
        try {
            ResultSet resultSet = connectionHandler.executeQuery(query);
            while (resultSet.next()){
                Book book = new Book();
                book.setBookId(resultSet.getInt("book_id"));
                book.setTitle(resultSet.getString("title"));
                book.setPubYear(resultSet.getString("pub_year"));
                book.setSellingPrice(resultSet.getFloat("selling_price"));
                book.setCategory(resultSet.getString("category"));
                book.setQuantity(resultSet.getInt("quantity"));
                book.setPublisherId(resultSet.getInt("publisher_id"));
                book.setThreshold(resultSet.getInt("threshold"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return null;
        }
        return books;

    }

    public ArrayList<Book> getNextPage(){
        return null;
    }

    public ArrayList<Book> getPreviousPage(){
        return null;
    }
    //todo get next and previous pages

    @Override
    public ArrayList<Book> searchForBook(HashMap<String, String> searchMap) {
        //TODO implement search for book
        //TODO note: user can search for book by id, title / search for books by category, author, publisher
        return null;
    }

    @Override
    public boolean addToCart(int bookId, int quantity, int userId) {
        CartElement cartElement = new CartElement();
        cartElement.setBookId(bookId);
        cartElement.setQuantity(quantity);
        cartElement.setUserId(userId);
        String query = sQlCommands.addToCart(cartElement);
        int affectedRows = 0 ;
        try {
            affectedRows = connectionHandler.executeInsert(query);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;
        }
        if (affectedRows > 0)
            return true;
        return false;
    }

    @Override
    public boolean removeFromCart(int bookId, int quantity, int userId) {
        //TODO implement delete tuple from cart
        return false;
    }

    @Override
    public ArrayList<CartElement> getCart(int userId) {
        String query = sQlCommands.getCart(userId);
        ArrayList<CartElement> cart = new ArrayList<>();
        try {
            ResultSet resultSet = connectionHandler.executeQuery(query);
            while (resultSet.next()){
                CartElement cartElement = new CartElement();
                cartElement.setUserId(resultSet.getInt("user_id"));
                cartElement.setQuantity(resultSet.getInt("quantity"));
                cartElement.setBookId(resultSet.getInt("book_id"));
                cart.add(cartElement);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return null;
        }
        return cart;
    }

    @Override
    public boolean checkout(int userId) {
        //todo implement call checkout procedure (Transaction)
        return false;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        String query =sQlCommands.getAllUsers();
        ArrayList<User> users = new ArrayList<User>();
        try {
            ResultSet resultSet = connectionHandler.executeQuery(query);
            while (resultSet.next()){
                User user = new User();
                user.setUserId(resultSet.getInt("user_id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmailAddress(resultSet.getString("email_address"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                user.setShippingAddress(resultSet.getString("shipping_address"));
                user.setPassword(resultSet.getString("password"));
                user.setPromoted(resultSet.getBoolean("promoted"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return null;
        }
        return users;
    }

    @Override
    public boolean promote(int userId) {
        String query = sQlCommands.promoteUser(userId);
        return update(query);
    }

    private boolean update(String query) {
        int affectedRows = 0;
        try {
            affectedRows = connectionHandler.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;
        }
        if (affectedRows > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean addBook(Book book) {
        //todo implement insert new book
        return false;
    }

    @Override
    public boolean modifyBook(Book newBook) {
        String query = sQlCommands.updateBook(newBook);
        return update(query);
    }

    @Override
    public ArrayList<Order> getAllOrders() {
        String query =sQlCommands.getAllOrders();
        ArrayList<Order> orders = new ArrayList<Order>();
        try {
            ResultSet resultSet = connectionHandler.executeQuery(query);
            while (resultSet.next()){
                Order order = new Order();
                order.setBookId(resultSet.getInt("book_id"));
                order.setDate(resultSet.getString("date"));
                order.setOrderId(resultSet.getInt("order_id"));
                order.setQuantity(resultSet.getInt("quantity"));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return null;
        }
        return orders;
    }

    @Override
    public boolean placeOrder(int bookId, int quantity) {
        Order order = new Order();
        order.setBookId(bookId);
        order.setQuantity(quantity);
        String query = sQlCommands.placeOrder(order);
        return update(query);
    }

    @Override
    public boolean confirmOrder(int orderId) {
        String query = sQlCommands.deleteOrder(orderId);
        return update(query);
    }

}

