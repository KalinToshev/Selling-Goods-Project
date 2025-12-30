import config.ExecutorServiceConfig;
import model.CartItem;
import model.Product;
import model.Receipt;
import store.Store;
import tasks.BuyerTaskCallable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        // 1) Init store stock
        Store store = new Store("My Store");

        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Bread", 1.50));
        products.add(new Product(2L, "Milk", 2.20));
        products.add(new Product(3L, "Cheese", 7.80));
        products.add(new Product(4L, "Eggs", 4.90));
        products.add(new Product(5L, "Apples", 3.10));

        store.addProduct(products.get(0), 30);
        store.addProduct(products.get(1), 25);
        store.addProduct(products.get(2), 15);
        store.addProduct(products.get(3), 20);
        store.addProduct(products.get(4), 40);

        System.out.println("Initial store quantities:");
        System.out.println(store);

        // 2) Prepare 10 buyers with baskets (carts)
        List<BuyerTaskCallable> tasks = new ArrayList<>();

        for (int buyerId = 1; buyerId <= 10; buyerId++) {

            Set<CartItem> cart = new LinkedHashSet<>();

            // deterministic basket: 3 items per buyer
            Product p1 = products.get(buyerId % products.size());
            Product p2 = products.get((buyerId + 1) % products.size());
            Product p3 = products.get((buyerId + 2) % products.size());

            cart.add(new CartItem(p1, buyerId % 3 + 1));
            cart.add(new CartItem(p2, buyerId % 2 + 1));
            cart.add(new CartItem(p3, buyerId % 4 + 1));

            tasks.add(new BuyerTaskCallable(buyerId, store, cart));
        }

        // 3) Run concurrent tasks in 4 threads
        List<Future<Receipt>> futures;
        try {
            futures = ExecutorServiceConfig.getExecutorService().invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 4) Print receipts
        System.out.println();
        System.out.println("Receipts:");
        for (Future<Receipt> future : futures) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        ExecutorServiceConfig.getExecutorService().shutdown();

        // 5) Final store quantities
        System.out.println();
        System.out.println("Final store quantities:");
        System.out.println(store);
    }
}
