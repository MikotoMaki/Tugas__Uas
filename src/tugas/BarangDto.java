package tugas;

public class BarangDto {
    private String code;
    private String product;
    private String price;
    private String name;
    private String amount;

    public BarangDto() {
    }

    public BarangDto(String name, String code, String product, String price, String amount) {
        this.code = code;
        this.product = product;
        this.price = price;
        this.name = name;
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
    
}
