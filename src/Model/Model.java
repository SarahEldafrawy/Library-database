package Model;

import Entites.*;
import sun.net.idn.Punycode;

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
        String query = sQlCommands.registerUser(user);
        return update(query);
    }

    @Override
    public User logIn(String email , String password) {
        String query =sQlCommands.logInUser(email , password);
        try {
            ResultSet resultSet = connectionHandler.executeQuery(query);
            User user = null;
            while (resultSet.next()){
                user = new User();
                setUser(resultSet, user);
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

    private void setUser(ResultSet resultSet, User user) throws SQLException {
        user.setUserId(resultSet.getInt("user_id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmailAddress(resultSet.getString("email_address"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setShippingAddress(resultSet.getString("shipping_address"));
        user.setPassword(resultSet.getString("password"));
        user.setPromoted(resultSet.getBoolean("promoted"));
    }


    @Override
    public boolean updateUser(User user) {
        String query = sQlCommands.updateUser(user);
        return update(query);
    }

    @Override
    public ArrayList<Book> getStartBooks() {
        String query = sQlCommands.selectFromBooks(pagination.getLimit(), 0);
        return getBooks(query);

    }
    @Override
    public int getNumberOfPagesOfBooks(){
        final String key = "count";
        String query = sQlCommands.getNumberOfPagesOfBooks(key);
        return getNumberOfPages(key, query);
    }

    private int getNumberOfPages(String key, String query) {
        int numberOfPages = 0;
        try {
            ResultSet resultSet = connectionHandler.executeQuery(query);
            numberOfPages = resultSet.getInt(key);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return numberOfPages;
    }

    public ArrayList<Book> getBooksByPage(int pageNumber, int limit){
        String query = sQlCommands.getBooksByPage(pageNumber , limit);
        return getBooks(query);
    }

    public int getNumberOfPagesOfUsers(){
        final String key = "count";
        String query = sQlCommands.getNumberOfPagesOfUsers(key);
        return getNumberOfPages(key, query);
    }

    public ArrayList<Book> getUsersByPage(int pageNumber, int limit){
        String query = sQlCommands.getUsersByPage(pageNumber , limit);
        return getBooks(query);
    }


    @Override
    public ArrayList<Book> searchForBooks(HashMap<String, String> searchMap) {
        String query = sQlCommands.searchForBooks(searchMap);
        return getBooks(query);
    }

    @Override
    public Book getBookById(int bookId){
        String query = sQlCommands.getBookById(bookId);
        return getBook(query);

    }

    private void setBook(Book book, ResultSet resultSet) throws SQLException {
        book.setBookId(resultSet.getInt("book_id"));
        book.setTitle(resultSet.getString("title"));
        book.setPubYear(resultSet.getString("pub_year"));
        book.setSellingPrice(resultSet.getFloat("selling_price"));
        book.setCategory(resultSet.getString("category"));
        book.setQuantity(resultSet.getInt("quantity"));
        book.setPublisherId(resultSet.getInt("publisher_id"));
        book.setThreshold(resultSet.getInt("threshold"));
    }

    @Override
    public Book getBookByTitle(String title){
        String query = sQlCommands.getBookByTitle(title);
        return getBook(query);

    }

    private Book getBook(String query) {
        Book book = null;
        try {
            ResultSet resultSet = connectionHandler.executeQuery(query);
            while (resultSet.next()){
                book = new Book();
                setBook(book, resultSet);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return null;
        }
        return book;
    }

    private ArrayList<Book> getBooks(String query) {
        ArrayList<Book> books = new ArrayList<>();
        try {
            ResultSet resultSet = connectionHandler.executeQuery(query);
            while (resultSet.next()){
                Book book = new Book();
                setBook(book, resultSet);
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
    public boolean removeFromCart(int bookId, int userId) {
        try {
            connectionHandler.prepareCall(userId , bookId);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;
        }
        return true;
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
        try {
            connectionHandler.prepareCall(userId , -1);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;
        }
        return true;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        String query =sQlCommands.getAllUsers();
        ArrayList<User> users = new ArrayList<User>();
        try {
            ResultSet resultSet = connectionHandler.executeQuery(query);
            while (resultSet.next()){
                User user = new User();
                setUser(resultSet, user);
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
        String query = sQlCommands.insertBook(book);
        return update(query);
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

    @Override
    public int getNumberOfPagesOfPublisher() {
        final String key = "count";
        String query = sQlCommands.getNumberOfPagesOfPublishers(key);
        return getNumberOfPages(key, query);
    }

    @Override
    public ArrayList<Publisher> getPublishersByPage(int pageNumber, int limit) {
        String query = sQlCommands.getPublishersByPage(pageNumber , limit);
        return getPublishers(query);
    }

    private ArrayList<Publisher> getPublishers(String query) {
        ArrayList<Publisher> publishers = new ArrayList<>();
        try {
            ResultSet resultSet = connectionHandler.executeQuery(query);
            while (resultSet.next()){
                Publisher publisher = new Publisher();
                publisher.setPublisherId(resultSet.getInt("publisher_id"));
                publisher.setName(resultSet.getString("name"));
                publisher.setPhone(resultSet.getString("phone"));
                publisher.setAddress(resultSet.getString("address"));
                publishers.add(publisher);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return null;
        }
        return publishers;
    }

    @Override
    public boolean addPublisher(Publisher publisher) {
        String query = sQlCommands.insertPublisher(publisher);
        return update(query);
    }

    @Override
    public int getNumberOfPagesOfAuthors() {
        final String key = "count";
        String query = sQlCommands.getNumberOfPagesOfAuthors(key);
        return getNumberOfPages(key, query);
    }

    @Override
    public ArrayList<Author> getAuthorsByPage(int pageNumber, int limit) {
        String query = sQlCommands.getAuthorsByPage(pageNumber , limit);
        return getAuthors(query);
    }

    @Override
    public boolean addAuthor(Author author) {
        String query = sQlCommands.insertAuthor(author);
        return update(query);
    }

    private ArrayList<Author> getAuthors(String query) {
        ArrayList<Author> authors = new ArrayList<>();
        try {
            ResultSet resultSet = connectionHandler.executeQuery(query);
            while (resultSet.next()){
                Author author = new Author();
                author.setAuthorId(resultSet.getInt("author_id"));
                author.setName(resultSet.getString("name"));
                authors.add(author);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return null;
        }
        return authors;
    }


    public boolean emptyCart(int userId){
        String query = sQlCommands.emptyCart(userId);
        return update(query);
    }
}

