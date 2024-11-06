package shopping.api.app;

public class AllModel {

    private double extracted_price;
    private String title;
    private String source;
    private String thumbnail;
    String link;

    public double getExtracted_price() {
        return extracted_price;
    }

    public void setExtracted_price(double extracted_price) {
        this.extracted_price = extracted_price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
