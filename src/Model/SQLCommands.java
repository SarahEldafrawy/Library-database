package Model;

import Entites.Book;
import Entites.CartElement;
import Entites.Order;
import Entites.User;

import java.sql.SQLData;
import java.util.HashMap;

public class SQLCommands {

    public String selectFromBooks(int limit , int pageNumber){
        String query = "SELECT * FROM BOOK LIMIT" + limit*pageNumber + "," + limit;
        return query;

    }
    public String logInUser(String email, String password){
        String query = "SELECT * FROM USER WHERE USER.user_email = \""
                + email + "\" AND USER.password = \"" + password + "\"";
        return query;
    }

    public String insertBook(Book book){
        String query = "INSER INTO BOOK VALUES ("
                +book.getBookId() + ","
                + "\"" + book.getTitle() + "\","
                +"\"" + book.getPubYear() + "\","
                +book.getSellingPrice() + ","
                + "\"" + book.getCategory() + "\","
                +book.getThreshold() + ")";
        return query;
    }

    public String updateBook(Book book){
        String query = "UPDATE BOOK SET " + book.TITLE + " = " + book.getTitle() + ","
                + book.PUBYEAR + " = " + book.getPubYear() + ","
                + book.SELLINGPRICE + " = " + book.getSellingPrice() + ","
                + book.CATEGORY + " = " + "\"" +  book.getCategory() + "\","
                + book.QUANTITY + " = " + book.getQuantity() + ","
                + book.THRESHOLD + " = " + book.getThreshold() + ")"
                + " WHERE BOOK." + book.BOOKID + " = " +"\""  + book.getBookId() + "\"";
        return query;
    }


    public String updateUser(User user) {
        String query = "UPDATE USER SET "
                + user.FIRSTNAME + " = \"" + user.getFirstName() + "\","
                + user.LASTNAME + " = " + "\"" +  user.getLastName() +  "\","
                + user.EMAILADDRESS + " = " + "\"" +  user.getEmailAddress() + "\","
                + user.PHONENUMBER + " = " + "\"" + user.getPhoneNumber() + "\","
                + user.SHIPPINGADDRESS + " = " + "\"" + user.getShippingAddress() + "\","
                + user.PASSWORD + " = " + "\"" + user.getPassword() + "\","
                + user.PROMOTED + " = " + user.isPromoted() + ")"
                + " WHERE USER." + user.USERID + " = "  + user.getUserId() ;
        return query;
    }

    public String addToCart(CartElement cartElement) {
        String query = "INSERT INTO CART VALUES ("
                +cartElement.getBookId() + ","
                +cartElement.getUserId() + ","
                +cartElement.getQuantity() + ","
                +cartElement.isInCart() + ")";
        return query;
    }

    public String getCart(int userId) {
        String query = "SELECT * FROM CART WHERE CART.user_id = " + userId;
        return query;
    }

    public String getAllUsers() {
        String query = "SELECT * FROM USER";
        return query;
    }

    public String promoteUser(int userId) {
        String query = "UPDATE USER SET promoted = 1 WHERE USER.user_id = " + userId;

        return query;
    }

    public String getAllOrders() {
        String query = "SELECT * FROM ORDER";
        return query;
    }

    public String placeOrder(Order order) {
        String query = "INSERT INTO ORDER VALUES ("
                +order.getOrderId() + ","
                +order.getBookId() + ","
                +order.getQuantity() + ","
                +order.getDate() + ")";
        return query;
    }

    public String deleteOrder(int orderId) {
        String query = "DELETE FROM ORDER WHERE ORDER.order_id = "
                + orderId ;
        return query;
    }

    public String registerUser(User user) {
        String query = "INSERT INTO USER VALUES "
                +"(\"" + user.getFirstName() + "\","
                + "\"" +  user.getLastName() +  "\","
                + "\"" +  user.getEmailAddress() + "\","
                + "\"" + user.getPhoneNumber() + "\","
                + "\"" + user.getShippingAddress() + "\","
                + "\"" + user.getPassword() + "\","
                + user.isPromoted() + ")";
        return query;
    }

    public String searchForBooks(HashMap<String, String> searchMap) {
        return null;
        //todo handle keys names with front end
    }

    public String getBookById(int bookId) {
        String query = "SELECT * FROM BOOK WHERE BOOK.book_id = "
                + bookId;
        return query;
    }

    public String getBookByTitle(String title) {
        String query = "SELECT * FROM BOOK WHERE BOOK.title = \" "
                + title + "\"" ;
        return null;
    }

    public String getBooksByPage(int pageNumber, int limit) {
        //todo by page
        return null;
    }

    public String getNumberOfPages(String key) {
        String query = "SELECT COUNT AS \"" + key + "\" FROM BOOK GROUP BY book_id";
        return query;
    }
}
