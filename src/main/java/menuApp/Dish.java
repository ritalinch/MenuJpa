package menuApp;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "menu")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private BigDecimal price;
    private int weight;
    private boolean discountPresent;

    public Dish() {}

    public Dish(int id, String name, BigDecimal price, int weight, boolean discountPresent) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.discountPresent = discountPresent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isDiscountPresent() {
        return discountPresent;
    }

    public void setDiscountPresent() {
        this.discountPresent = true;
    }

    @Override
    public String toString() {
        return id + ". dish '" + name +
                "': price - " + price.toString() +
                "$; weight - " + weight +
                "g, with discount - " + discountPresent +
                '.';
    }
}
