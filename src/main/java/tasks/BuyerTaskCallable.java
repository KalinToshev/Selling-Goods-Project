package tasks;

import lombok.AllArgsConstructor;
import model.CartItem;
import model.Receipt;
import store.Store;

import java.util.Set;
import java.util.concurrent.Callable;

@AllArgsConstructor
public class BuyerTaskCallable implements Callable<Receipt> {
    private long buyerId;
    private Store store;
    private Set<CartItem> cart;

    @Override
    public Receipt call() {

        System.out.println("Buyer " + buyerId + " -> START in thread " +
                Thread.currentThread().getName() + " " + Thread.currentThread().threadId());

        // Simulate scanning products (work)
        for (CartItem item : cart) {
            System.out.println("Buyer " + buyerId + " scanning: " + item.getProduct().getName()
                    + " x" + item.getQuantity() + " (" + Thread.currentThread().getName() + ")");
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Stock update happens after the basket is processed
        Receipt receipt = store.checkout(buyerId, cart);

        System.out.println("Buyer " + buyerId + " -> END in thread " +
                Thread.currentThread().getName() + " " + Thread.currentThread().threadId());

        return receipt;
    }
}
