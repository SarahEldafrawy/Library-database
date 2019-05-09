package Model;

import Entites.*;

import java.sql.SQLData;
import java.util.HashMap;

public class SQLCommands {

    public String selectFromBooks(int limit , int pageNumber){
        String query = "SELECT * FROM BOOK LIMIT" + limit*pageNumber + "," + limit;
        return query;

    }
    public String logInUser(String email, String password){
        String query = "SELECT * FROM USER WHERE USER.email_address = \""
                + email + "\" AND USER.password = \"" + password + "\"";
        return query;
    }

    public String insertBook(Book book){
        String query = "INSERT INTO BOOK VALUES ("
                +book.getBookId() + ","
                + "\"" + book.getTitle() + "\","
                +"\"" + book.getPubYear() + "\","
                +book.getSellingPrice() + ","
                + "\"" + book.getCategory() + "\","
                + book.getQuantity() + ","
                +book.getPublisherId() + ","
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
                + user.PROMOTED + " = " + user.isPromoted()
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
                +"(user_id,"
                +"\"" + user.getFirstName() + "\","
                + "\"" +  user.getLastName() +  "\","
                + "\"" +  user.getEmailAddress() + "\","
                + "\"" + user.getPhoneNumber() + "\","
                + "\"" + user.getShippingAddress() + "\","
                + "\"" + user.getPassword() + "\","
                + user.isPromoted() + ")";
        return query;
    }

    public String searchForBooks(HashMap<String, String> searchMap) {
        if(searchMap.isEmpty()) {
            return null;
        }
        //todo el search 5rban wel joins
        //TODO tany el search 5arban from Islam
        //todo change author name
        String query = "SELECT BOOK.book_id , BOOK.title , BOOK.pub_year," +
                " BOOK.selling_price ,BOOK.category , BOOK.quantity , BOOK.publisher_id , BOOK.threshold " +
                "FROM BOOK WHERE";
                if(searchMap.containsKey("publisher_name")) {
                    query+= " PUBLISHER.name = \"" + searchMap.get("publisher_name") + "\"";
                } if(searchMap.containsKey("author_name")) {
                    query+= " AND Author.name = \"" + searchMap.get("author_name") +"\"";
                } if (searchMap.containsKey("category")) {
                    query+= " BOOK.category = \""+ searchMap.get("category") + "\"";
                }
        return query;
    }

    public String getBookById(int bookId) {
        String query = "SELECT * FROM BOOK WHERE BOOK.book_id = "
                + bookId;
        return query;
    }

    public String getBookByTitle(String title) {
        String query = "SELECT * FROM BOOK WHERE BOOK.title = \" "
                + title + "\"" ;
        return query;
    }

    public String getBooksByPage(int pageNumber, int limit) {
        String query = "SELECT * FROM BOOK LIMIT " + pageNumber*limit+ " , " +limit+"";
        return query;
    }

    public String getNumberOfPagesOfBooks(String key) {
        String query = "SELECT COUNT AS \"" + key + "\" FROM BOOK GROUP BY book_id";
        return query;
    }

    public String getNumberOfPagesOfUsers(String key) {
        String query = "SELECT COUNT AS \"" + key + "\" FROM USER GROUP BY user_id";
        return query;
    }

    public String getUsersByPage(int pageNumber, int limit) {
        String query = "SELECT * FROM USER LIMIT " + pageNumber*limit+ " , " +limit+"";
        return query;
    }

    public String emptyCart(int userId) {
        String query = "DELETE FROM CART WHERE user_id = " + userId;
        return query;
    }

    public String getAllPublishers() {
        String query = "SELECT * FROM PUBLISHER";
        return query;
    }

    public String insertPublisher(Publisher publisher) {
        String query = "INSERT INTO PUBLISHER VALUES ("
                + "publisher_id,"
                + "\"" + publisher.getName() + "\","
                +"\"" + publisher.getPhone() + "\","
                + "\"" + publisher.getAddress() + "\")";
        return query;
    }

    public String getNumberOfPagesOfPublishers(String key) {
        String query = "SELECT COUNT AS \"" + key + "\" FROM PUBLISHER GROUP BY publisher_id";
        return query;
    }

    public String getPublishersByPage(int pageNumber, int limit) {
        String query = "SELECT * FROM PUBLISHER LIMIT " + pageNumber*limit+ " , " +limit+"";
        return query;
    }

    public String getNumberOfPagesOfAuthors(String key) {
        String query = "SELECT COUNT AS \"" + key + "\" FROM AUTHOR GROUP BY author_id";
        return query;
    }

    public String getAuthorsByPage(int pageNumber, int limit) {
        String query = "SELECT * FROM AUTHOR LIMIT " + pageNumber*limit+ " , " +limit+"";
        return query;
    }

    public String insertAuthor(Author author) {
        String query = "INSERT INTO AUTHOR VALUES ("
                + "author_id,"
                + "\"" + author.getName() + "\")";
        return query;
    }
}
