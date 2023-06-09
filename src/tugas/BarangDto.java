package tugas;

public class BarangDto {
    private String code;
    private String product;
    private String price;
    private String name;

    public BarangDto() {
    }

    public BarangDto(String name, String code, String product, String price) {
        this.code = code;
        this.product = product;
        this.price = price;
        this.name = name;
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
