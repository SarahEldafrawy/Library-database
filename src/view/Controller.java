package view;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.*;

import Entites.*;
import Model.*;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller {

    Model database;
    ArrayList<Book> allBooks;
    ArrayList<Book> cartBooks;
    User currentUser;


    TextField currentCount[];
    Button addToCartButton[];
    int HORIZONTAL_SHIFT = 800/3;
    int LIMIT = 50;
    boolean addedToCart = false;
    boolean isNowCart = false;
    int VERTICAL_SHIFT = 210;
    int MARGIN = 15;
    int BOOKS_PER_ROW = 3;
    int pageNum = 0;
    String MANAGER_CODE = "1234";

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
    Label errorLoginLabel;

    @FXML
    private Button addBookButtonToolbar;

    @FXML
    private Button viewOrdersButtonToolbar;

    @FXML
    private Button viewReportButtonToolbar;

    @FXML
    private Button viewUsersButtonToolbar;

    @FXML
    private Button checkoutButton;

    @FXML
    private Label slogan;

    @FXML
    private Button nextPageButton;

    @FXML
    private Button prevPageButton;

    @FXML
    private Button findCertainBook;

    @FXML
    private Label labelInfo;

    @FXML
    private TextField searchAuthorTextbox;

    @FXML
    private CheckBox isManagerCheckBox;

    @FXML
    private PasswordField secretCodeTextField;

    @FXML
    private ImageView validIconManager;

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
        isNowCart = false;
        if(emailTextbox.isVisible()) {
            try {
                establishNewConncetion();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String email = emailTextbox.getText();
            String password = passwordTextbox.getText();
            if(!email.equals("admin") & !email.equals("")) {
                currentUser = database.logIn(email, password);
            }
            if(currentUser == null && !(email.equals("admin") && password.equals("admin")) &&
                    !(email.equals("") && password.equals(""))) {
                showErrorLogIn();
            } else {
                showMarket();
                prepareBooks(allBooks);
            }
        } else {
            showLogin();
        }
    }

    @FXML
    void registerClicked(MouseEvent event) {
        if(rEmailTextbox.isVisible()) {
            try {
                establishNewConncetion();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String email = rEmailTextbox.getText();
            String password = rPasswordTextbox.getText();
            String telefon = telefonTextbox.getText();
            String firstName = firstNameTextbox.getText();
            String lastName = lastNameTextbox.getText();
            String address = addressTextbox.getText();

            User newUser = new User();
            newUser.setEmailAddress(email);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setPassword(password);
            newUser.setPhoneNumber(telefon);
            newUser.setShippingAddress(address);
            if(validIconManager.isVisible()) {
                newUser.setPromoted(true);
            } else {
                newUser.setPromoted(false);
            }
            if(database.register(newUser)) {
                showMarket();
                currentUser = database.logIn(newUser.getEmailAddress() , newUser.getPassword());
                prepareBooks(allBooks);
            } else {
                showErrorLogIn();
            }
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
    void validateManagerCode(KeyEvent event) {
        String password = secretCodeTextField.getText();
        if(password.equals(MANAGER_CODE)) {
            validIconManager.setVisible(true);
        } else {
            validIconManager.setVisible(false);
        }
    }

    @FXML
    void increaseBPP(MouseEvent event) {
        if(BOOKS_PER_ROW != 5) {
            BOOKS_PER_ROW++;
            HORIZONTAL_SHIFT = (800 / BOOKS_PER_ROW);
            if(isNowCart) {
                prepareBooks(cartBooks);
            } else {
                prepareBooks(allBooks);
            }
        }
    }

    @FXML
    void findOneBook(ActionEvent event) {
        Pane container = new Pane();
        Scene secondScene = new Scene(container, 442.0, 296);
        secondScene.getStylesheets().add("style.css");
        Stage newWindow = new Stage();
        newWindow.setTitle("Search for a book");
        newWindow.setScene(secondScene);

        Image image = null;
        try {
            image = new Image(new FileInputStream(
                    this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
                            + "../../../src/view/Resources/images/libraryy.jpg"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        ImageView wallpaper = new ImageView(image);
        wallpaper.setFitHeight(297.5);
        wallpaper.setFitWidth(442.0);
        wallpaper.setX(0);
        wallpaper.setY(0);
        container.getChildren().add(wallpaper);
        VBox vbx = new VBox();
        vbx.setMinWidth(442);
        vbx.setMinHeight(297.5);
        vbx.setAlignment(Pos.CENTER);
        TextField id = new TextField();
        TextField title = new TextField();

        title.setPromptText("Enter the title of the book");
        title.setFocusTraversable(false);
        id.setPromptText("Enter id of the book");
        id.setFocusTraversable(false);

        Button search1 = new Button("Search by id");
        search1.setFocusTraversable(true);
        search1.getStyleClass().add("mybutton");
        search1.setTextFill(Color.WHITE);
        search1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //todo show single book page
            }
        });

        Button search2 = new Button("Search by Title");
        search2.setFocusTraversable(true);
        search2.getStyleClass().add("mybutton");
        search2.setTextFill(Color.WHITE);
        search2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //todo show single book page
            }
        });

        vbx.getChildren().addAll(id, search1, title, search2);
        container.getChildren().add(vbx);
        newWindow.setX(582);
        newWindow.setY(259);
        newWindow.show();
    }

    @FXML
    void getNextPage(ActionEvent event) {
        showMarket();
        prepareBooks(database.getBooksByPage(++pageNum, LIMIT));
    }
    @FXML
    void getPrevPage(ActionEvent event) {
        showMarket();
        if (pageNum != 0) {
            prepareBooks(database.getBooksByPage(--pageNum, LIMIT));
        }
    }

    @FXML
    void decreaseBPP(MouseEvent event) {
        if(BOOKS_PER_ROW != 1) {
            BOOKS_PER_ROW--;
            HORIZONTAL_SHIFT = (800 / BOOKS_PER_ROW);
            if(isNowCart) {
                prepareBooks(cartBooks);
            } else {
                prepareBooks(allBooks);
            }
        }
    }

    @FXML
    void clickCart(ActionEvent event) {
        isNowCart = true;
        prepareBooks(cartBooks);
        searchAuthorTextbox.setVisible(false);
        searchButton.setVisible(false);
        searchCategoryTextbox.setVisible(false);
        searchTitleTextbox.setVisible(false);

        checkoutButton.setVisible(true);
    }

    @FXML
    void backToShop(ActionEvent event) {
        showMarket();
        isNowCart = false;
    }

    @FXML
    void proceedToCheckout(ActionEvent event) {

    }

    @FXML
    void clickHome(ActionEvent event) throws InterruptedException {
        isNowCart = false;
        showMarket();
        pageNum = 0;
        prepareBooks(database.getBooksByPage(pageNum, LIMIT));
    }

    @FXML
    void clickLogout(ActionEvent event) {
        database.emptyCart(currentUser.getUserId());
        currentUser = null;
        cartBooks = null;
        showLogin();
    }

    @FXML
    void clickPlaceOrder(ActionEvent event) {
    }

    @FXML
    void addBook(ActionEvent event) {
        Pane container = new Pane();
        Scene secondScene = new Scene(container, 442.0, 296);
        secondScene.getStylesheets().add("style.css");
        Stage newWindow = new Stage();
        newWindow.setTitle("edit profile");
        newWindow.setScene(secondScene);

        Image image = null;
        try {
            image = new Image(new FileInputStream(
                    this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
                            + "../../../src/view/Resources/images/libraryy.jpg"));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        ImageView wallpaper = new ImageView(image);
        wallpaper.setFitHeight(297.5);
        wallpaper.setFitWidth(442.0);
        wallpaper.setX(0);
        wallpaper.setY(0);
        container.getChildren().add(wallpaper);
        VBox vbx = new VBox();
        vbx.setMinWidth(442);
        vbx.setMinHeight(297.5);
        vbx.setAlignment(Pos.CENTER);
        TextField title = new TextField();
        TextField bookid = new TextField();
        TextField authors = new TextField();
        TextField publisherid = new TextField();
        TextField price = new TextField();
        TextField count = new TextField();
        TextField dateOfPublishing = new TextField();
        TextField category = new TextField();
        title.setPromptText("Enter the title of the book");
        title.setFocusTraversable(false);
        publisherid.setPromptText("Enter the publisher ID");
        publisherid.setFocusTraversable(false);
        bookid.setPromptText("Enter the ISPN of the book");
        bookid.setFocusTraversable(false);
        authors.setPromptText("Enter author(s) of the book");
        authors.setFocusTraversable(false);
        price.setPromptText("How much is that book?");
        price.setFocusTraversable(false);
        count.setPromptText("How many books do we have?");
        count.setFocusTraversable(false);
        dateOfPublishing.setPromptText("Date of publishing");
        dateOfPublishing.setFocusTraversable(false);
        category.setPromptText("Category of the book");
        category.setFocusTraversable(false);
        Button done = new Button("Done");
        done.setFocusTraversable(true);
        done.getStyleClass().add("mybutton");
        done.setTextFill(Color.WHITE);
        done.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Book book = new Book();
                book.setBookId(Integer.valueOf(bookid.getText()));
                book.setPublisherId(Integer.valueOf(publisherid.getText()));
                book.setTitle(title.getText());
                book.setQuantity(Integer.valueOf(count.getText()));
                book.setCategory(category.getText());
                book.setSellingPrice(Float.valueOf(price.getText()));
                book.setPubYear(dateOfPublishing.getText());
                //todo setAuthors
                database.addBook(book);
                ((Node)event.getSource()).getScene().getWindow().hide();
            }
        });
        vbx.getChildren().addAll(title, bookid, authors,publisherid, price, count, dateOfPublishing, category, done);
        container.getChildren().add(vbx);
        newWindow.setX(582);
        newWindow.setY(259);
        newWindow.show();
    }

    @FXML
    void clickProfile(ActionEvent event) {
        Pane container = new Pane();
        Scene secondScene = new Scene(container, 884.0, 595.75);
        System.out.println();
        secondScene.getStylesheets().add("style.css");
        container.setMinWidth(884.0);
        container.setMinHeight(595.75);
        VBox secondaryLayout = new VBox();
        secondaryLayout.setMinWidth(884.0);
        secondaryLayout.setMinHeight(595.75);
        secondaryLayout.setAlignment(Pos.CENTER);
        try {
            Image image = new Image(new FileInputStream(
                    this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
                            + "../../../src/view/Resources/images/libraryy.jpg"));
            ImageView wallpaper = new ImageView(image);
            wallpaper.setFitHeight(595.75);
            wallpaper.setFitWidth(884);
            wallpaper.setX(0);
            wallpaper.setY(0);
            container.getChildren().add(wallpaper);

            Image image2 = new Image(new FileInputStream(
                    this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
                            + "../../../src/view/Resources/images/nophoto.gif"));
            ImageView profilepic = new ImageView(image2);
            profilepic.setFitHeight(220);
            profilepic.setFitWidth(200);
            secondaryLayout.getChildren().add(profilepic);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Label name = new Label(currentUser.getFirstName() + " " + currentUser.getLastName());
        Label verification;
        if(currentUser.isPromoted()) {
            verification = new Label("Verified");
        } else {
            verification = new Label("Not Verified");
        }
        Label email = new Label(currentUser.getEmailAddress());
        Label phone = new Label(currentUser.getPhoneNumber());
        Label address = new Label(currentUser.getShippingAddress());

        Button button = new Button("Edit my profile info");
        Button buttonBack = new Button("Back");
        buttonBack.setFocusTraversable(true);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Pane container = new Pane();
                Scene secondScene = new Scene(container, 442.0, 296);
                secondScene.getStylesheets().add("style.css");
                Stage newWindow = new Stage();
                newWindow.setTitle("edit profile");
                newWindow.setScene(secondScene);

                Image image = null;
                try {
                    image = new Image(new FileInputStream(
                            this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
                                    + "../../../src/view/Resources/images/libraryy.jpg"));
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                ImageView wallpaper = new ImageView(image);
                wallpaper.setFitHeight(297.5);
                wallpaper.setFitWidth(442.0);
                wallpaper.setX(0);
                wallpaper.setY(0);
                container.getChildren().add(wallpaper);
                VBox vbx = new VBox();
                vbx.setMinWidth(442);
                vbx.setMinHeight(297.5);
                vbx.setAlignment(Pos.CENTER);
                TextField email = new TextField(currentUser.getEmailAddress());
                TextField address = new TextField(currentUser.getEmailAddress());
                TextField phone = new TextField(currentUser.getPhoneNumber());
                TextField name = new TextField(currentUser.getFirstName()+ " " + currentUser.getLastName());
                Button chngpwd = new Button("Change Password");
                chngpwd.setFocusTraversable(true);
                chngpwd.getStyleClass().add("mybutton");
                chngpwd.setTextFill(Color.WHITE);
                chngpwd.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        Pane container = new Pane();
                        Scene secondScene = new Scene(container, 442.0, 296);
                        secondScene.getStylesheets().add("style.css");
                        Stage newWindow = new Stage();
                        newWindow.setTitle("edit password");
                        newWindow.setScene(secondScene);

                        Image image = null;
                        try {
                            image = new Image(new FileInputStream(
                                    this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()
                                            + "../../../src/view/Resources/images/libraryy.jpg"));
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }
                        ImageView wallpaper = new ImageView(image);
                        wallpaper.setFitHeight(297.5);
                        wallpaper.setFitWidth(442.0);
                        wallpaper.setX(0);
                        wallpaper.setY(0);
                        container.getChildren().add(wallpaper);
                        VBox vbx = new VBox();
                        vbx.setMinWidth(442);
                        vbx.setMinHeight(297.5);
                        vbx.setAlignment(Pos.CENTER);
                        TextField oldpassword = new TextField();
                        TextField newpassword = new TextField();
                        TextField confirmpasswrd = new TextField();
                        oldpassword.setFocusTraversable(false);
                        newpassword.setFocusTraversable(false);
                        confirmpasswrd.setFocusTraversable(false);
                        oldpassword.setPromptText("Enter your old password");
                        newpassword.setPromptText("Enter your new password");
                        confirmpasswrd.setPromptText("Confirm your new password");
                        Button chngpwd = new Button("Done");
                        chngpwd.setFocusTraversable(true);
                        chngpwd.getStyleClass().add("mybutton");
                        chngpwd.setTextFill(Color.WHITE);
                        chngpwd.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                User temp = new User();
                                temp.setFirstName(currentUser.getFirstName());
                                temp.setPassword(currentUser.getPassword());
                                temp.setPromoted(currentUser.isPromoted());
                                temp.setPhoneNumber(currentUser.getPhoneNumber());
                                temp.setShippingAddress(currentUser.getShippingAddress());
                                temp.setEmailAddress(currentUser.getEmailAddress());
                                temp.setLastName(currentUser.getLastName());
                                temp.setUserId(currentUser.getUserId());
                                if(newpassword.getText() != confirmpasswrd.getText() || !oldpassword.equals(currentUser.getPassword())) {
                                    //todo make error not matching
                                } else {
                                    currentUser.setPassword(newpassword.getText());
                                    if(!database.updateUser(currentUser)) {
                                        currentUser.setPassword(temp.getPassword());
                                        //todo show error
                                    }
                                }

                                ((Node)e.getSource()).getScene().getWindow().hide();
                            }
                        });
                        vbx.getChildren().addAll(oldpassword, newpassword, confirmpasswrd, chngpwd);
                        container.getChildren().add(vbx);
                        newWindow.setX(582);
                        newWindow.setY(259);
                        newWindow.show();
                    }
                });
                Button done = new Button("Done");
                done.getStyleClass().add("mybutton");
                done.setTextFill(Color.WHITE);
                done.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        User temp = new User();
                        temp.setFirstName(currentUser.getFirstName());
                        temp.setPassword(currentUser.getPassword());
                        temp.setPromoted(currentUser.isPromoted());
                        temp.setPhoneNumber(currentUser.getPhoneNumber());
                        temp.setShippingAddress(currentUser.getShippingAddress());
                        temp.setEmailAddress(currentUser.getEmailAddress());
                        temp.setLastName(currentUser.getLastName());
                        temp.setUserId(currentUser.getUserId());

                        List<String> names;
                        names = Arrays.asList(name.getText().split(" "));
                        currentUser.setFirstName(names.get(0));
                        currentUser.setLastName(names.get(names.size()-1));
                        currentUser.setEmailAddress(email.getText());
                        currentUser.setPhoneNumber(phone.getText());
                        currentUser.setShippingAddress(address.getText());
                        if(!database.updateUser(currentUser)) {
                            currentUser = temp;
                            //todo show error
                        }
                        ((Node)event.getSource()).getScene().getWindow().hide();
                    }
                });
                vbx.getChildren().addAll(name, email, phone, address, chngpwd, done);
                container.getChildren().add(vbx);
                newWindow.setX(582);
                newWindow.setY(259);
                newWindow.show();
            }
        });
        buttonBack.setOnAction((new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Node)event.getSource()).getScene().getWindow().hide();
            }
        }));
        buttonBack.getStyleClass().add("mybutton");
        buttonBack.setTextFill(Color.WHITE);
        button.getStyleClass().add("mybutton");
        button.setTextFill(Color.WHITE);

        Label empty = new Label("");
        Label empty1 = new Label("");
        Label empty2 = new Label("");

        name.setFont(new Font("Cambria", 45));
        name.setTextFill(Color.DARKRED);
        verification.setFont(Font.font("Cambria",25));
        verification.setTextFill(Color.GREEN);
        email.setFont(new Font("Cambria", 27));
        email.setTextFill(Color.BLACK);
        phone.setFont(new Font("Cambria", 27));
        phone.setTextFill(Color.BLACK);
        address.setFont(new Font("Cambria", 27));
        address.setTextFill(Color.BLACK);

        secondaryLayout.getChildren().addAll(empty, empty1, button, empty2, name, verification, email, phone, address, buttonBack);
        container.getChildren().add(secondaryLayout);
        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Profile");
        newWindow.setScene(secondScene);

        // Set position of second window, related to primary window.
        newWindow.setX(382);
        newWindow.setY(109);

        newWindow.show();
    }

    @FXML
    void clickViewOrders(ActionEvent event) {
    }

    @FXML
    void clickViewUsers(ActionEvent event) {
    }

    @FXML
    void checkedManagerBox(ActionEvent event) {
        if(isManagerCheckBox.isSelected()) {
            secretCodeTextField.setVisible(true);
        } else {
            secretCodeTextField.setVisible(false);
        }
    }

    @FXML
    void clickSearch(ActionEvent event) {
        Book book = new Book();
        HashMap<String, String> searchElements = new HashMap<>();
        if(!searchAuthorTextbox.getText().equals("")) {
            searchElements.put(book.AUTHOR_NAME, searchAuthorTextbox.getText());
        }
        if(!searchCategoryTextbox.getText().equals("")) {
            searchElements.put(book.CATEGORY, searchCategoryTextbox.getText());
        }
        if(!searchTitleTextbox.getText().equals("")) {
            searchElements.put(book.TITLE, searchTitleTextbox.getText());
        }
        if(searchElements.size() > 0) {
            allBooks = database.searchForBooks(searchElements);
            showMarket();
            if(allBooks!=null) {
                prepareBooks(allBooks);
            }
        }
    }

    private void prepareBooks(ArrayList<Book> allLibraryBooks) {
        addedToCart = false;
        int numberOfBooks = allLibraryBooks.size();
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

            String bookName = allLibraryBooks.get(i).getTitle();
            Label bookLabel = new Label(bookName);
            bookLabel.setFont(Font.font("Cambria", 23));

            String Author = allLibraryBooks.get(i).getPubYear();
            HBox bookAuthor = new HBox();
            bookAuthor.setAlignment(Pos.CENTER);
            Label bookAuthor1 = new Label("pub year: ");
            Label bookAuthor2 = new Label(Author);
            bookAuthor1.setStyle("-fx-text-fill: grey ;");
            bookAuthor1.setFont(Font.font("Cambria", 14));
            bookAuthor2.setStyle("-fx-text-fill: brown ;") ;
            bookAuthor2.setFont(Font.font("Cambria", 16));
            bookAuthor.getChildren().addAll(bookAuthor1, bookAuthor2);

            int quantity = allLibraryBooks.get(i).getQuantity();
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
            if(isNowCart) {
                addToCartButton[i].setText("Remove from cart");
                addToCartButton[i].setStyle("-fx-background-color: RED;\n" +
                        "-fx-text-fill: WHITE ;");

            }
            addToCartButton[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(((Button) (event.getSource())).getText().equals("Add to cart")) {
                        Button cartButton = ((Button) (event.getSource()));
                        cartButton.setText("Remove from cart");
                        cartButton.setStyle("-fx-background-color: RED;\n" +
                                "-fx-text-fill: WHITE ;");
                        int index = Integer.valueOf(((Button)(event.getSource())).getId());
                        int quantity = Integer.valueOf(currentCount[index].getText());
                        //todo check if not already added ( when home after cart )
                        database.addToCart(allLibraryBooks.get(index).getBookId(),quantity,currentUser.getUserId());
                        cartBooks.add(allLibraryBooks.get(index));
                    } else {
                        Button cartButton = ((Button) (event.getSource()));
                        cartButton.setText("Add to cart");
                        cartButton.setStyle("-fx-background-color: GREEN;\n" +
                                "-fx-text-fill: WHITE ;");
                        int index = Integer.valueOf(((Button)(event.getSource())).getId());
                        int quantity = Integer.valueOf(currentCount[index].getText());
                        database.removeFromCart(allLibraryBooks.get(index).getBookId(),currentUser.getUserId());
                        cartBooks.remove(allLibraryBooks.get(index));
                        if(isNowCart) {
                            prepareBooks(cartBooks);
                        }
                    }
                }
            });

            float bookPrice = allLibraryBooks.get(i).getSellingPrice();
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
    private void showErrorLogIn() {
        errorLoginLabel.setVisible(true);
    }
    private void showLogin() {
        toolBar.setVisible(false);
        nextPageButton.setVisible(false);
        prevPageButton.setVisible(false);
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
        findCertainBook.setVisible(false);
        buttonIncreaseBPP.setVisible(false);
        buttondecreaseBPP.setVisible(false);
        labelInfo.setVisible(false);
        toolBar.setVisible(false);
        checkoutButton.setVisible(false);

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
        errorLoginLabel.setVisible(false);
        isManagerCheckBox.setVisible(false);
        validIconManager.setVisible(false);
        secretCodeTextField.setVisible(false);
        checkoutButton.setVisible(false);
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
        nextPageButton.setVisible(true);
        prevPageButton.setVisible(true);
        findCertainBook.setVisible(true);
        buttondecreaseBPP.setVisible(true);
        buttonIncreaseBPP.setVisible(true);
        toolBar.setVisible(true);
        if(currentUser!= null && currentUser.isPromoted()) {
            addBookButtonToolbar.setVisible(true);
            viewOrdersButtonToolbar.setVisible(true);
            viewReportButtonToolbar.setVisible(true);
            viewUsersButtonToolbar.setVisible(true);
        }
        booksPane.setVisible(true);
        searchAuthorTextbox.setVisible(true);
        searchButton.setVisible(true);
        searchCategoryTextbox.setVisible(true);
        searchTitleTextbox.setVisible(true);
    }
    private void showRegister() {
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
        isManagerCheckBox.setVisible(true);

        nextPageButton.setVisible(false);
        findCertainBook.setVisible(false);
        prevPageButton.setVisible(false);
        slogan.setVisible(false);
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
    private void establishNewConncetion() throws SQLException, ClassNotFoundException {
        database = new Model();
        allBooks = new ArrayList<Book>();
        currentUser = new User();
        allBooks = database.getBooksByPage(pageNum,LIMIT);
        cartBooks = new ArrayList<Book>();

        /*currentUser.setFirstName("Islam");
        currentUser.setLastName("Gamal");
        currentUser.setEmailAddress("islamgamal77@gmail.com");
        currentUser.setPromoted(true);
        currentUser.setShippingAddress("Alzorkani ST, Miami, Alexandria");
        currentUser.setPhoneNumber("(+20) 109-144-8249");
        currentUser.setPassword("mypassword");*/

        /*for(int i = 0; i < 20; i++) {
            Book book = new Book();
            book.setBookId(i);
            book.setCategory("Science");
            book.setPublisherId(5);
            book.setPubYear("23 July 1997");
            book.setSellingPrice((int)(Math. random() * 2000 + 1));
            book.setTitle("Book Number " + i);
            book.setQuantity((int)(Math. random() * 50 + 1));
            allBooks.add(book);
        }*/
    }
}
