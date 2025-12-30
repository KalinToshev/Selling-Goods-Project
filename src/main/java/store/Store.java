package store;

import model.CartItem;
import model.Product;
import model.Receipt;
import model.ReceiptLine;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Store {
    private final String name;
    private final Map<Product, Integer> quantities;

    public Store(String name) {
        this.name = name;
        this.quantities = new LinkedHashMap<>();
    }

    public synchronized void addProduct(Product product, int quantity) {
        Integer current = quantities.get(product);
        if (current == null) current = 0;
        quantities.put(product, current + quantity);
    }

    public synchronized int getQuantity(Product product) {
        Integer q = quantities.get(product);
        return q == null ? 0 : q;
    }

    /**
     * Checkout is synchronized, so the whole basket update is thread-safe.
     * We sell as much as possible (partial sale), and report sold quantity for each item.
     */
    public synchronized Receipt checkout(long buyerId, Set<CartItem> cart) {
        Receipt receipt = new Receipt(buyerId);

        for (CartItem item : cart) {
            Product product = item.getProduct();
            int requested = item.getQuantity();

            int available = getQuantity(product);
            int sold = Math.min(requested, available);

            quantities.put(product, available - sold);

            receipt.addLine(new ReceiptLine(product, requested, sold));
        }

        return receipt;
    }

    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Store{").append("name='").append(name).append("'").append(", quantities=").append("\n");
        for (Map.Entry<Product, Integer> entry : quantities.entrySet()) {
            sb.append("  ").append(entry.getKey().getName()).append(" -> ").append(entry.getValue()).append("\n");
        }
        sb.append('}');
        return sb.toString();
    }
}
