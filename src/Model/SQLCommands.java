package Model;

import Entites.Book;
import Entites.CartElement;
import Entites.Order;
import Entites.User;

public class SQLCommands {
    public final String SELECT= "SELECT";
    public final String UPDATE= "UPDATE";
    public  final String INSERT= "INSERT";
    public  final String DELETE= "DELETE";
    public final String ASTRIC = "*";
    public final String FROM = "FROM";
    public final String WHERE = "WHERE";
    public final String LIMIT = "LIMIT";
    public final String COMMA = ",";
    public final String DOT = ".";
    public final String EQUAL = "=";
    public final String AND = "AND";
    public final String SPACE = " ";
    public final String NEWLINE = "\n";


    public  final String BOOK= "BOOK";
    public  final String AUTHOR= "Author";
    public  final String CART= "CART";
    public  final String CREDITCARD= "CREDIT_CARD";
    public  final String ORDER= "ORDER";
    public  final String PUBLISHER= "PUBLISHER";
    public  final String SALES= "SALES";
    public  final String USER= "USER";

    public String selectFromBooks(int limit , int pageNumber){
        StringBuilder query = new StringBuilder();
        query.append(SELECT);
        query.append(SPACE);
        query.append(ASTRIC);
        query.append(NEWLINE);
        query.append(FROM);
        query.append(SPACE);
        query.append(BOOK);
        query.append(SPACE);
        query.append(LIMIT);
        query.append(SPACE);
        query.append(Integer.toString(limit * pageNumber));
        query.append(COMMA);
        query.append(limit);
        return query.toString();

    }
    public String logInUser(String email, String password){
        StringBuilder query = new StringBuilder();
        query.append(SELECT);
        query.append(SPACE);
        query.append(ASTRIC);
        query.append(NEWLINE);
        query.append(FROM);
        query.append(SPACE);
        query.append(USER);
        query.append(NEWLINE);
        query.append(WHERE);
        query.append(SPACE);
        query.append("email_address");
        query.append(SPACE);
        query.append(EQUAL);
        query.append(SPACE);
        query.append(email);
        query.append(SPACE);
        query.append(AND);
        query.append(SPACE);
        query.append("password");
        query.append(SPACE);
        query.append(EQUAL);
        query.append(SPACE);
        query.append(password);
        return query.toString();
    }

    public String insertBook(Book book){
        return null;
    }

    public String updateBook(Book book){
        return null;
    }


    public String updateUser(User user) {
        return null;
    }

    public String addToCart(CartElement cartElement) {
        return null;
    }

    public String getCart(int userId) {
        return null;
    }

    public String getAllUsers() {
        return null;
    }

    public String promoteUser(int userId) {
        return null;
    }

    public String getAllOrders() {
        return null;
    }

    public String placeOrder(Order order) {
        return null;
    }

    public String deleteOrder(int orderId) {
        return null;
    }
}
