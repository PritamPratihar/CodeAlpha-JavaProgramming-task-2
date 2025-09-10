import java.io.*;
import java.util.*;

// ===== Stock Class =====
class Stock {
    String symbol;
    double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    // Random price update to simulate market
    public void updatePrice() {
        double change = (Math.random() - 0.5) * 10;
        price = Math.max(1, price + change); // price should not go below 1
    }

    @Override
    public String toString() {
        return symbol + " @ " + String.format("%.2f", price);
    }
}

// ===== User Class =====
class User {
    String name;
    double cash;
    Map<String, Integer> holdings; // symbol -> quantity

    public User(String name, double cash) {
        this.name = name;
        this.cash = cash;
        this.holdings = new HashMap<>();
    }

    // Buy stock
    public void buy(Stock stock, int quantity) {
        double cost = stock.price * quantity;
        if (cash >= cost) {
            cash -= cost;
            holdings.put(stock.symbol, holdings.getOrDefault(stock.symbol, 0) + quantity);
            System.out.println("✅ Bought " + quantity + " shares of " + stock.symbol);
        } else {
            System.out.println("❌ Not enough cash to buy!");
        }
    }

    // Sell stock
    public void sell(Stock stock, int quantity) {
        int owned = holdings.getOrDefault(stock.symbol, 0);
        if (owned >= quantity) {
            cash += stock.price * quantity;
            holdings.put(stock.symbol, owned - quantity);
            if (holdings.get(stock.symbol) == 0) {
                holdings.remove(stock.symbol);
            }
            System.out.println("✅ Sold " + quantity + " shares of " + stock.symbol);
        } else {
            System.out.println("❌ Not enough shares to sell!");
        }
    }

    // Show portfolio
    public void showPortfolio(List<Stock> market) {
        System.out.println("\n=== Portfolio of " + name + " ===");
        System.out.println("💰 Cash: ₹" + String.format("%.2f", cash));
        double total = cash;
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            String sym = entry.getKey();
            int qty = entry.getValue();
            double price = 0;
            for (Stock s : market) {
                if (s.symbol.equals(sym)) price = s.price;
            }
            double value = qty * price;
            total += value;
            System.out.println(sym + " -> " + qty + " shares (₹" + String.format("%.2f", value) + ")");
        }
        System.out.println("📊 Total Portfolio Value = ₹" + String.format("%.2f", total));
    }
}

// ===== Main Trading Platform =====
public class StockTradingCSV {
    private static List<Stock> market = new ArrayList<>();
    private static User user;
    private static final String FILE_NAME = "portfolio.csv";

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        // Market Setup
        market.add(new Stock("TCS", 3200));
        market.add(new Stock("INFY", 1500));
        market.add(new Stock("WIPRO", 450));
        market.add(new Stock("HDFC", 1600));
        market.add(new Stock("RELIANCE", 2500));

        // Load or Create User
        System.out.print("Enter your username: ");
        String uname = sc.nextLine();
        user = new User(uname, 50000);

        File file = new File(FILE_NAME);
        if (file.exists()) {
            System.out.print("Saved data found. Load it? (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                loadPortfolio();
            }
        }

        // Menu loop
        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1) Show Market Prices");
            System.out.println("2) Buy Stock");
            System.out.println("3) Sell Stock");
            System.out.println("4) Show Portfolio");
            System.out.println("5) Simulate Market Tick");
            System.out.println("6) Save Portfolio (CSV)");
            System.out.println("7) Load Portfolio (CSV)");
            System.out.println("8) Exit");
            System.out.print("Choose: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    for (Stock s : market) System.out.println(s);
                    break;
                case 2:
                    System.out.print("Enter stock symbol: ");
                    String symB = sc.next().toUpperCase();
                    Stock stockB = findStock(symB);
                    if (stockB != null) {
                        System.out.print("Quantity: ");
                        int qtyB = sc.nextInt();
                        user.buy(stockB, qtyB);
                    } else System.out.println("❌ Invalid symbol!");
                    break;
                case 3:
                    System.out.print("Enter stock symbol: ");
                    String symS = sc.next().toUpperCase();
                    Stock stockS = findStock(symS);
                    if (stockS != null) {
                        System.out.print("Quantity: ");
                        int qtyS = sc.nextInt();
                        user.sell(stockS, qtyS);
                    } else System.out.println("❌ Invalid symbol!");
                    break;
                case 4:
                    user.showPortfolio(market);
                    break;
                case 5:
                    for (Stock s : market) s.updatePrice();
                    System.out.println("📈 Market prices updated.");
                    break;
                case 6:
                    savePortfolio();
                    break;
                case 7:
                    loadPortfolio();
                    break;
                case 8:
                    savePortfolio();
                    System.out.println("👋 Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("❌ Invalid option!");
            }
        }
    }

    // Find stock by symbol
    private static Stock findStock(String sym) {
        for (Stock s : market) if (s.symbol.equals(sym)) return s;
        return null;
    }

    // Save portfolio to CSV
    private static void savePortfolio() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            pw.println(user.name + "," + user.cash);
            for (Map.Entry<String, Integer> e : user.holdings.entrySet()) {
                pw.println(e.getKey() + "," + e.getValue());
            }
            System.out.println("💾 Portfolio saved to " + FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load portfolio from CSV
    private static void loadPortfolio() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = br.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                user = new User(parts[0], Double.parseDouble(parts[1]));
            }
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String sym = parts[0];
                int qty = Integer.parseInt(parts[1]);
                user.holdings.put(sym, qty);
            }
            System.out.println("📂 Portfolio loaded from " + FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
