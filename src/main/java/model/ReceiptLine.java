package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ReceiptLine {
    private Product product;
    private int requested;
    private int sold;

    @Override
    public String toString() {
        return "ReceiptLine{" +
                "product=" + product.getName() +
                ", requested=" + requested +
                ", sold=" + sold +
                '}';
    }
}
