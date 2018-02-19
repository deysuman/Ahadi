package in.ahadi.luci.ahadi;

import java.util.ArrayList;

/**
 * Created by Luci on 3/24/2017.
 */

public class Orders {
    private String title, thumbnailUrl,quantity,price;

    public Orders() {
    }

    public Orders(String name, String quantityy,String pricee) {
        this.title = name;
        this.quantity = quantityy;
        this.price = pricee;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getQantity() {
        return quantity;
    }

    public void setQuantity(String quantityy) {
        this.quantity = quantityy;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String pricee) {
        this.price = pricee;
    }

}
