package Entites;

public class Book {
    public final String BOOKID = "book_id";
    public final String TITLE = "title";
    public final String PUBYEAR = "pub_year";
    public final String SELLINGPRICE = "selling_price";
    public final String CATEGORY = "book_id";
    public final String QUANTITY = "book_id";
    public final String PUBLISHERID = "publisher_id";
    public final String THRESHOLD = "threshold";

    int bookId;
    String title;
    String pubYear;
    float sellingPrice;
    String category;
    int quantity;
    int publisherId;
    int threshold ;

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }



    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubYear() {
        return pubYear;
    }

    public void setPubYear(String pubYear) {
        this.pubYear = pubYear;
    }

    public float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }
}
