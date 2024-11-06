package shopping.api.app;

public class Product {
    private int positionPage;
    private int positionPosition;
    private int positionGlobalPosition;
    private String asin;
    private double currentPrice;
    private String currency;
    private int totalReviews;
    private double rating;
    private String url;
    private String title;
    private String thumbnail;

    public int getPositionPage() {
        return positionPage;
    }

    public void setPositionPage(int positionPage) {
        this.positionPage = positionPage;
    }

    public int getPositionPosition() {
        return positionPosition;
    }

    public void setPositionPosition(int positionPosition) {
        this.positionPosition = positionPosition;
    }

    public int getPositionGlobalPosition() {
        return positionGlobalPosition;
    }

    public void setPositionGlobalPosition(int positionGlobalPosition) {
        this.positionGlobalPosition = positionGlobalPosition;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
