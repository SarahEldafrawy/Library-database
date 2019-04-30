package view;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.ResourceBundle;
import Entites.*;
import Model.*;

public class Controller {

    Model database = new Model();
    TextField currentCount[];
    Button addToCartButton[];
    int HORIZONTAL_SHIFT = 800/3;
    boolean addedToCart = false;
    int VERTICAL_SHIFT = 210;
    int MARGIN = 15;
    int BOOKS_PER_ROW = 3;

    @FXML
    private ResourceBundle resources;

    @FXML
    private TextField addressTextbox;

    @FXML
    private ScrollPane booksPane;

    @FXML
    private PasswordField confirmPasswordTextbox;

    @FXML
    private TextField emailTextbox;

    @FXML
    private TextField firstNameTextbox;

    @FXML
    private Button buttonIncreaseBPP;

    @FXML
    private Button buttondecreaseBPP;

    @FXML
    private ToolBar toolBar;

    @FXML
    private TextField lastNameTextbox;

    @FXML
    private Button loginButton;

    @FXML
    private ImageView notValidIcon;

    @FXML
    private PasswordField passwordTextbox;

    @FXML
    private TextField rEmailTextbox;

    @FXML
    private PasswordField rPasswordTextbox;

    @FXML
    private Button registerButton;

    @FXML
    private Label slogan;

    @FXML
    private Label labelInfo;

    @FXML
    private TextField searchAuthorTextbox;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchCategoryTextbox;

    @FXML
    private TextField searchTitleTextbox;

    @FXML
    private TextField telefonTextbox;

    @FXML
    private ImageView validIcon;

    @FXML
    void loginClicked(MouseEvent event) {
        if(emailTextbox.isVisible()) {

            String email = emailTextbox.getText();
            String password = passwordTextbox.getText();
            showMarket();
            prepareBooks();

        } else {
            showLogin();
        }
    }


    @FXML
    void registerClicked(MouseEvent event) {
        if(rEmailTextbox.isVisible()) {
            //todo real registering stuff
        } else {
            showRegister();
        }
    }


    @FXML
    void checkPasswordMatch(Event event) {
        String password = rPasswordTextbox.getText();
        String passwordMatch = confirmPasswordTextbox.getText();
        if(password.equals(passwordMatch) && !password.equals("")) {
            validIcon.setVisible(true);
            notValidIcon.setVisible(false);
        } else if(!password.equals("")){
            validIcon.setVisible(false);
            notValidIcon.setVisible(true);
        }
    }

    @FXML
    void increaseBPP(MouseEvent event) {
        if(BOOKS_PER_ROW != 5) {
            BOOKS_PER_ROW++;
            HORIZONTAL_SHIFT = (800 / BOOKS_PER_ROW);
            prepareBooks();
        }
    }

    @FXML
    void decreaseBPP(MouseEvent event) {
        if(BOOKS_PER_ROW != 1) {
            BOOKS_PER_ROW--;
            HORIZONTAL_SHIFT = (800 / BOOKS_PER_ROW);
            prepareBooks();
        }
    }

    @FXML
    void clickCart(ActionEvent event) {
    }

    @FXML
    void clickHome(ActionEvent event) throws InterruptedException {
        showMarket();
        prepareBooks();
    }

    @FXML
    void clickLogout(ActionEvent event) {
    }

    @FXML
    void clickPlaceOrder(ActionEvent event) {
    }

    @FXML
    void clickProfile(ActionEvent event) {
    }

    @FXML
    void clickViewOrders(ActionEvent event) {
    }

    @FXML
    void clickViewUsers(ActionEvent event) {
    }


    private void prepareBooks() {
        int numberOfBooks = 50;
        Pane root = new Pane();
        booksPane.setContent(root);
        String cssLayout =
                "-fx-background-color : brown;\n" +
                        "-fx-border-color: brown;\n" +
                        "-fx-border-insets: 5;\n" +
                        "-fx-border-width: 2;\n" +
                        "-fx-border-style: solid;\n";
        booksPane.setStyle(cssLayout);
        int xValue = 0;
        int yValue = 0;
        currentCount = new TextField[numberOfBooks];
        addToCartButton = new Button[numberOfBooks];
        for(int i = 0; i < numberOfBooks; i++) {
            VBox container = new VBox();
            if(i%BOOKS_PER_ROW != 0 || i == 0) {
                container.setLayoutX(xValue * HORIZONTAL_SHIFT + MARGIN);
                container.setLayoutY(yValue * VERTICAL_SHIFT + MARGIN);
                xValue++;
            } else {
                if(i==BOOKS_PER_ROW) {
                    xValue = 0;
                    yValue++;
                }
                container.setLayoutY(yValue * VERTICAL_SHIFT + MARGIN);
                container.setLayoutX(xValue * HORIZONTAL_SHIFT + MARGIN);
                if(i!=BOOKS_PER_ROW) {
                    xValue = 0;
                    yValue++;
                }

            }
            /*==============adjusting container style==================================*/
            String cssLayout2 =
                    "-fx-background-color : #fec377;\n" +
                            "-fx-border-color: brown;\n" +
                            "-fx-border-insets: 5;\n" +
                            "-fx-border-width: 2;\n" +
                            "-fx-border-style: solid;\n";
            container.setMaxWidth(HORIZONTAL_SHIFT - 15);
            container.setMinWidth(HORIZONTAL_SHIFT - 15);
            container.setStyle(cssLayout2);
            container.setSpacing(5);
            container.setAlignment(Pos.CENTER);
            //==========================================================================

            Label bookLabel = new Label("Book number" + i);
            bookLabel.setFont(Font.font("Cambria", 23));

            String Author = "Islam Gamal";
            HBox bookAuthor = new HBox();
            bookAuthor.setAlignment(Pos.CENTER);
            Label bookAuthor1 = new Label("Author/s: ");
            Label bookAuthor2 = new Label(Author);
            bookAuthor1.setStyle("-fx-text-fill: grey ;");
            bookAuthor1.setFont(Font.font("Cambria", 14));
            bookAuthor2.setStyle("-fx-text-fill: brown ;") ;
            bookAuthor2.setFont(Font.font("Cambria", 18));
            bookAuthor.getChildren().addAll(bookAuthor1, bookAuthor2);

            int quantity = 5;
            HBox count = new HBox();
            count.setAlignment(Pos.CENTER);
            Label count1 = new Label("Available in stock: ");
            Label count2 = new Label(String.valueOf(quantity));
            count1.setFont(Font.font("Cambria", 14));
            count1.setStyle("-fx-text-fill: grey ;");
            count2.setFont(Font.font("Cambria", 18));
            count.getChildren().addAll(count1, count2);

            HBox counter = new HBox();
            counter.setMaxWidth(104);
            Button decrement = new Button("-");
            decrement.setId("d" + String.valueOf(i));
            currentCount[i] = new TextField("1");
            currentCount[i].setMaxWidth(50);
            currentCount[i].setEditable(false);
            currentCount[i].setStyle("-fx-alignment: center ;");
            Button increment = new Button("+");
            increment.setId("i"+String.valueOf(i));


            decrement.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    int i = Integer.valueOf(((Node)e.getSource()).getId().substring(1,((Node)e.getSource()).getId().length()));
                    int cc = Integer.valueOf(currentCount[i].getText());
                    if(cc != 0) {
                        currentCount[i].setText(String.valueOf(--cc));
                    }
                }
            });
            increment.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    int i = Integer.valueOf(((Node)e.getSource()).getId().substring(1,((Node)e.getSource()).getId().length()));
                    int cc = Integer.valueOf(currentCount[i].getText());
                    currentCount[i].setText(String.valueOf(++cc));
                }
            });

            counter.getChildren().addAll(decrement,currentCount[i], increment);

            addToCartButton[i] = new Button("Add to cart");
            addToCartButton[i].setId(String.valueOf(i));
            addToCartButton[i].setStyle("-fx-background-color: GREEN;\n" +
                    "-fx-text-fill: WHITE ;");
            addToCartButton[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(!addedToCart) {
                        Button cartButton = ((Button) (event.getSource()));
                        cartButton.setText("Remove from cart");
                        cartButton.setStyle("-fx-background-color: RED;\n" +
                                "-fx-text-fill: WHITE ;");
                        //todo remove from cart logic
                        addedToCart = true;
                    } else {
                        Button cartButton = ((Button) (event.getSource()));
                        cartButton.setText("Add to cart");
                        cartButton.setStyle("-fx-background-color: GREEN;\n" +
                                "-fx-text-fill: WHITE ;");
                        addedToCart = false;
                        //todo add to cart logic
                    }
                }
            });
            int bookPrice = 5964;
            HBox price = new HBox();
            price.setAlignment(Pos.CENTER);
            Label p1 = new Label("Price/book: LE ");
            Label p2 = new Label(String.valueOf(bookPrice));
            p1.setFont(Font.font("Cambria", 14));
            p1.setStyle("-fx-text-fill: grey ;");
            p2.setFont(Font.font("Cambria", 18));
            p2.setStyle("-fx-text-fill: GREEN ;");
            price.getChildren().addAll(p1, p2);

            Label newLine = new Label("");

            container.getChildren().addAll(bookLabel, bookAuthor, count, counter, price, addToCartButton[i], newLine);
            root.getChildren().add(container);
        }

    }

    private void showLogin() {
        firstNameTextbox.setVisible(false);
        lastNameTextbox.setVisible(false);
        rEmailTextbox.setVisible(false);
        rPasswordTextbox.setVisible(false);
        confirmPasswordTextbox.setVisible(false);
        telefonTextbox.setVisible(false);
        addressTextbox.setVisible(false);
        validIcon.setVisible(false);
        notValidIcon.setVisible(false);
        booksPane.setVisible(false);
        searchTitleTextbox.setVisible(false);
        searchCategoryTextbox.setVisible(false);
        searchAuthorTextbox.setVisible(false);
        searchButton.setVisible(false);
        buttonIncreaseBPP.setVisible(false);
        buttondecreaseBPP.setVisible(false);
        labelInfo.setVisible(false);
        toolBar.setVisible(false);

        slogan.setVisible(false);
        loginButton.setLayoutX(226);
        loginButton.setLayoutY(458);
        loginButton.setVisible(true);
        registerButton.setVisible(true);
        registerButton.setLayoutX(456);
        registerButton.setLayoutY(458);
        emailTextbox.setVisible(true);
        passwordTextbox.setVisible(true);
    }
    private void showMarket() {
        firstNameTextbox.setVisible(false);
        lastNameTextbox.setVisible(false);
        rEmailTextbox.setVisible(false);
        rPasswordTextbox.setVisible(false);
        confirmPasswordTextbox.setVisible(false);
        telefonTextbox.setVisible(false);
        addressTextbox.setVisible(false);
        validIcon.setVisible(false);
        notValidIcon.setVisible(false);
        emailTextbox.setVisible(false);
        passwordTextbox.setVisible(false);
        loginButton.setVisible(false);
        registerButton.setVisible(false);

        labelInfo.setVisible(true);
        buttondecreaseBPP.setVisible(true);
        buttonIncreaseBPP.setVisible(true);
        toolBar.setVisible(true);
        booksPane.setVisible(true);
        searchAuthorTextbox.setVisible(true);
        searchButton.setVisible(true);
        searchCategoryTextbox.setVisible(true);
        searchTitleTextbox.setVisible(true);
    }
    private void showRegister() {
        slogan.setVisible(false);
        firstNameTextbox.setVisible(true);
        lastNameTextbox.setVisible(true);
        rEmailTextbox.setVisible(true);
        rPasswordTextbox.setVisible(true);
        confirmPasswordTextbox.setVisible(true);
        telefonTextbox.setVisible(true);
        addressTextbox.setVisible(true);
        loginButton.setLayoutX(226);
        loginButton.setLayoutY(458);
        registerButton.setLayoutX(456);
        registerButton.setLayoutY(458);

        booksPane.setVisible(false);
        searchTitleTextbox.setVisible(false);
        searchCategoryTextbox.setVisible(false);
        searchAuthorTextbox.setVisible(false);
        searchButton.setVisible(false);
        buttonIncreaseBPP.setVisible(false);
        buttondecreaseBPP.setVisible(false);
        labelInfo.setVisible(false);
        toolBar.setVisible(false);
        emailTextbox.setVisible(false);
        passwordTextbox.setVisible(false);
    }

}
