package menuApp;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuService {
    private static final MenuService MENU_SERVICE = new MenuService();

    private final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("JPATest");
    private final EntityManager EM = EMF.createEntityManager();
    private final Scanner SCANNER = new Scanner(System.in);

    private MenuService() {}

    public static void getMenuService() {

        MAIN_LOOP:
        while (true) {
            System.out.println("To display any set of values press -> '1'");
            System.out.println("To insert a value press            -> '2'");
            System.out.println("To get a kilogram set press        -> '3'");

            try {
                switch (MENU_SERVICE.SCANNER.nextLine()) {
                    case "1":
                        MENU_SERVICE.searchByCriteria();
                        break;
                    case "2":
                        MENU_SERVICE.insertDish();
                        break;
                    case "3":
                        MENU_SERVICE.getKilogramSet();
                        break;
                    default:
                        break MAIN_LOOP;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Illegal arguments");
            }

        }

        MENU_SERVICE.SCANNER.close();
    }

    private Dish createDish() throws IllegalArgumentException{
        Dish dish = new Dish();
        System.out.println("Enter name: ");
        dish.setName(SCANNER.nextLine());

        System.out.println("Enter price: ");
        BigDecimal price = new BigDecimal(SCANNER.nextLine());
        dish.setPrice(price.intValue() > 0 ? price : new BigDecimal("0.01"));

        System.out.println("Enter weight: ");
        int weight = Integer.parseInt(SCANNER.nextLine());
        dish.setWeight(weight > 0 ? weight : 100);

        System.out.println("Is any discount present[yes/no]: ");
        String discount = SCANNER.nextLine();
        if (discount.equals("yes")) {
            dish.setDiscountPresent();
        }

        return dish;
    }

    private void insertDish() {
        EM.getTransaction().begin();
        try {
            EM.persist(createDish());
            EM.getTransaction().commit();
        } catch (Exception ex) {
            EM.getTransaction().rollback();
        }
    }

    private void searchByCriteria() {
        System.out.println("By which criteria do you want to search: ");
        System.out.println("By price (from - to) : choose '1'");
        System.out.println("By discount : choose '2'");
        System.out.println("Show all : tap any key");
        switch (SCANNER.nextLine()) {
            case "1" -> searchByPrice();
            case "2" -> searchByDiscount();
            default -> showAll();
        }
    }

    private void searchByPrice() {
        try {
            System.out.println("Enter minimum price: ");
            BigDecimal minPrice = new BigDecimal(SCANNER.nextLine());
            minPrice = (minPrice.intValue() > 0) ? minPrice : new BigDecimal("0.01");

            System.out.println("Enter maximum price: ");
            BigDecimal maxPrice = new BigDecimal(SCANNER.nextLine());
            maxPrice = (maxPrice.intValue() > minPrice.intValue()) ? maxPrice : minPrice;

            Query query = EM.createQuery("SELECT d FROM Dish d WHERE d.price BETWEEN :minPrice AND :maxPrice", Dish.class);
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);
            List<Dish> dishList = query.getResultList();

            for(Dish d : dishList) {
                System.out.println(d);
            }
        } catch (NumberFormatException e) {
            System.out.println("Illegal arguments");
        }

    }

    private void searchByDiscount() {
        System.out.println("Is any discount present[yes/no]: ");
        String criteria = SCANNER.nextLine();

        Query query = EM.createQuery("SELECT d FROM Dish d WHERE d.discountPresent = :discount", Dish.class);
        query.setParameter("discount", criteria.equals("yes"));
        List<Dish> dishList = query.getResultList();

        for(Dish d : dishList) {
            System.out.println(d);
        }

    }

    private void showAll() {
        Query query = EM.createQuery("SELECT d FROM Dish d", Dish.class);
        List<Dish> dishList = query.getResultList();

        for(Dish d : dishList) {
            System.out.println(d);
        }
    }

    private void getKilogramSet() {
        Query query = EM.createQuery("SELECT d FROM Dish d", Dish.class);
        List<Dish> dishList = query.getResultList();
        List<Dish> res = new ArrayList<>();

        double totalWeight = 0;

        for (Dish d : dishList) {
            if (totalWeight == 1000.00) {
                break;
            }

            if (d.getWeight() + totalWeight <= 1000.00) {
                res.add(d);
                totalWeight += d.getWeight();
            }
        }

        for(Dish d : res) {
            System.out.println(d);
        }

        System.out.println("Total weight: " + totalWeight + "g.");
    }
}
