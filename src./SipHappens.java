import java.util.*;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.Locale;

public class SipHappens {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoodAnalyzer analyzer = new MoodAnalyzer();
        Menu menu = new Menu(analyzer); // uses same keys as MoodAnalyzer
        Order order = new Order(menu);

        printHeader();
        sleepShort();

        System.out.println("Welcome! This is Full Café Mode — the menu is always available. 🍃✨");
        System.out.println("Type a drink name to add it to cart, or use commands listed below.");
        System.out.println();
        printHelp();

        // show menu at start
        System.out.println();
        menu.printMenu();

        while (true) {
            System.out.print("\nYour sip choice: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                goodbye();
                break;
            } else if (input.equalsIgnoreCase("help")) {
                printHelp();
            } else if (input.equalsIgnoreCase("menu")) {
                menu.printMenu();
            } else if (input.equalsIgnoreCase("cart")) {
                order.printCart();
            } else if (input.equalsIgnoreCase("checkout")) {
                if (order.isEmpty()) {
                    System.out.println("\nYour cart is empty. Add a drink first ✨");
                } else {
                    System.out.print("\nMay I have your name, please? ");
                    String name = scanner.nextLine().trim();
                    if (name.isEmpty()) name = "Guest";

                    order.setCustomerName(name);

                    order.checkout();
                    order = new Order(menu);
                }
            } else if (input.toLowerCase().startsWith("mood ")) {
                String mood = input.substring(5).trim();
                handleMoodFlow(mood, analyzer, menu, order, scanner);
            } else if (menu.isDrinkName(input)) {
                // user typed a drink name directly
                String key = menu.lookupKeyByName(input);
                handleDirectOrderFlow(key, menu, order, scanner);
            } else if (input.equalsIgnoreCase("suggest")) {
                // ask mood and suggest
                System.out.print("How are you feeling? > ");
                String mood = scanner.nextLine().trim();
                handleMoodFlow(mood, analyzer, menu, order, scanner);
            } else {
                System.out.println("Unknown command or drink. Type 'help' to see commands or 'menu' to view drinks.");
            }
        }

        scanner.close();
    }

    // -------------------- UI Helpers --------------------
    private static void printHeader() {
        System.out.println("╔══════════════════ SIP HAPPENS CAFÉ ══════════════════╗");
        System.out.println("║        🌙  Where moods turn into warm brews  ☕        ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
    }

    private static void printHelp() {
        System.out.println("\nCommands:");
        System.out.println("  menu       — Show the full drink menu");
        System.out.println("  mood <mood>— Ask by mood (e.g., mood anxious)");
        System.out.println("  suggest    — Let the cafe ask how you're feeling and suggest");
        System.out.println("  <drink>    — Type a drink name exactly to add it to cart");
        System.out.println("  cart       — View your cart");
        System.out.println("  checkout   — Finish and pay");
        System.out.println("  help       — Show this help");
        System.out.println("  exit       — Exit the café");
    }

    private static void goodbye() {
        System.out.println("\n\"May your days stay warm, and your heart stay full.\"");
        System.out.println("🌙 Sip Happens Café — come back when your mood seeks another flavor.");
    }

    private static void sleepShort() {
        try { TimeUnit.MILLISECONDS.sleep(350); } catch (InterruptedException ignored) {}
    }

    private static void sleepMedium() {
        try { TimeUnit.MILLISECONDS.sleep(600); } catch (InterruptedException ignored) {}
    }

    // -------------------- Flow Handlers --------------------
    private static void handleMoodFlow(String mood, MoodAnalyzer analyzer, Menu menu, Order order, Scanner scanner) {
        String normalizedMood = mood.toLowerCase();
        try {
            // Check if mood is valid before proceeding
            if (!analyzer.hasMood(normalizedMood)) {
                throw new InvalidMoodException("I don't recognize the mood '" + mood + "'.");
            }

            // Show mood card
            printMoodCard(normalizedMood, analyzer);

            // Get drink suggestion
            Drink suggestion = analyzer.getDrinkForMood(normalizedMood); // NOW EXISTS
            String key = analyzer.getKeyForMood(normalizedMood);
            double price = menu.getPriceForKey(key);

            // Brew & show
            suggestion.brew();
            suggestion.serve();
            System.out.println("Price: " + formatCurrency(price));

            // Ask to add to cart
            System.out.print("\nAdd this to cart? (yes/no) > ");
            String ans = scanner.nextLine().trim();
            if (ans.equalsIgnoreCase("yes") || ans.equalsIgnoreCase("y")) {
                System.out.print("Quantity > ");
                int qty = readPositiveInt(scanner);
                order.addItem(key, qty);
                System.out.println("Added " + qty + " x " + suggestion.getName() + " to cart.");
            } else {
                System.out.println("No problem — enjoy the moment ✨");
            }

        } catch (InvalidMoodException ime) {
            System.out.println("💬 " + ime.getMessage());
            System.out.println("Try one of: " + String.join(", ", analyzer.getSupportedMoods()));
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private static void handleDirectOrderFlow(String key, Menu menu, Order order, Scanner scanner) {
        // Show drink preview, price, ask quantity
        Drink preview = menu.buildDrinkFromKey(key);
        double price = menu.getPriceForKey(key);

        // mini preview UI
        System.out.println();
        System.out.println("────────── ✧ DRINK PREVIEW ✧ ──────────");
        System.out.println("   " + preview.getName() + " " + menu.getEmojiForKey(key));
        System.out.println("\"" + preview.getDescription() + "\"");
        System.out.println("Ingredients: " + String.join(", ", preview.getIngredients()));
        System.out.println("Price: " + formatCurrency(price));
        System.out.println("───────────────────────────────────────");

        System.out.print("Add to cart? (yes/no) > ");
        String ans = scanner.nextLine().trim();
        if (ans.equalsIgnoreCase("yes") || ans.equalsIgnoreCase("y")) {
            System.out.print("Quantity > ");
            int qty = readPositiveInt(scanner);
            order.addItem(key, qty);
            System.out.println("Added " + qty + " x " + preview.getName() + " to cart.");
        } else {
            System.out.println("Alright — let the cup wait for another day.");
        }
    }

    private static int readPositiveInt(Scanner scanner) {
        int qty = 1;
        try {
            String s = scanner.nextLine().trim();
            qty = Integer.parseInt(s);
            if (qty <= 0) qty = 1;
        } catch (Exception ignored) {
            qty = 1;
        }
        return qty;
    }

    private static void printMoodCard(String mood, MoodAnalyzer analyzer) {
        String normalized = mood == null ? "" : mood.toLowerCase();
        String quote;
        String displayMood = normalized;
        if (analyzer.hasMood(normalized)) {
            quote = analyzer.getQuoteForMood(normalized);
            displayMood = normalized;
        } else {
            quote = "I can't read that mood — try another feeling or 'menu'.";
            displayMood = mood;
        }

        String border = "────────── ✧ Mood Detected ✧ ──────────";
        System.out.println();
        System.out.println(border);
        String emoji = analyzer.getEmojiForMood(normalized);
        if (emoji == null) emoji = "";
        System.out.println("        " + emoji + " " + displayMood.toUpperCase() + " " + emoji);
        System.out.println("\"" + quote + "\"");
        System.out.println(border);
        sleepShort();
    }

    private static String formatCurrency(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        // nf will show PHP sign; if default not desired, we fallback to manual
        String res;
        try {
            res = nf.format(amount);
        } catch (Exception e) {
            res = "₱" + String.format("%.2f", amount);
        }
        // ensure ₱ prefix (some locales show PHP)
        if (!res.contains("₱")) {
            res = "₱" + String.format("%.2f", amount);
        }
        return res;
    }
}

class Menu {
    private final Map<String, Double> priceByKey;
    private final Map<String, String> nameByKey; // <-- private
    private final Map<String, String> emojiByKey;
    private final MoodAnalyzer analyzer;

    public Menu(MoodAnalyzer analyzer) {
        this.analyzer = analyzer;
        priceByKey = new HashMap<>();
        nameByKey = new HashMap<>();
        emojiByKey = new HashMap<>();

        // Prices chosen to match the A + C aesthetic suggestions
        put("evergreen_iced_matcha", "Evergreen Iced Matcha", 160, "🍃");
        put("misty_matcha_delight", "Misty Matcha Delight", 170, "🌙");
        put("frosted_matcha_frappe", "Frosted Matcha Frappe", 165, "❄️");
        put("comfort_matcha_latte", "Comfort Matcha Latte", 155, "🍵");
        put("dream_matcha_smoothie", "Dream Matcha Smoothie", 150, "🥭");
        put("caramel_memory_mocha", "Caramel Memory Mocha", 170, "🍫");
        put("midnight_mocha_tears", "Midnight Mocha Tears", 175, "💧");
        put("sunrise_vanilla_bliss", "Sunrise Vanilla Bliss", 140, "🌅");
        put("autumn_chai_calm", "Autumn Chai Calm", 145, "🍂");
        put("berry_bloom_blizz", "Berry Bloom Blizz", 150, "🍓");
        put("mocha_mystery_mix", "Mocha Mystery Mix", 160, "🌀");
        put("vanilla_dream_brew", "Vanilla Dream Brew", 145, "🌸");
        put("sparkling_strawberry", "Sparkling Strawberry", 140, "✨");
        put("honey_warmth_milk", "Honey Warmth Milk", 135, "🐚");
        put("lavender_serenity_latte", "Lavender Serenity Latte", 150, "💜");
    }

    private void put(String key, String displayName, double price, String emoji) {
        priceByKey.put(key, price);
        nameByKey.put(key, displayName);
        emojiByKey.put(key, emoji);
    }

    public void printMenu() {
        System.out.println();
        System.out.println("╔══════════ ✧ DRINK MENU ✧ ══════════╗");
        // print each mood mapping for clarity
        List<String> keys = new ArrayList<>(nameByKey.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            String mood = analyzer.getMoodForKey(key);
            String line = String.format(" %-13s — %-28s %s %s",
                    (mood == null ? "" : capitalize(mood)),
                    nameByKey.get(key),
                    formatCurrency(priceByKey.get(key)),
                    emojiByKey.get(key));
            System.out.println(" " + line);
        }
        System.out.println("╚══════════════════════════════════════╝");
    }

    public boolean isDrinkName(String typed) {
        String lower = typed.toLowerCase();
        return nameByKey.values().stream().map(String::toLowerCase).anyMatch(n -> n.equals(lower));
    }

    public String lookupKeyByName(String typed) {
        String lower = typed.toLowerCase();
        for (Map.Entry<String, String> e : nameByKey.entrySet()) {
            if (e.getValue().toLowerCase().equals(lower)) return e.getKey();
        }
        return null;
    }

    // ADDED PUBLIC GETTER for use in Order class
    public String getNameForKey(String key) {
        return nameByKey.getOrDefault(key, "Unknown Drink");
    }

    public double getPriceForKey(String key) {
        return priceByKey.getOrDefault(key, 0.0);
    }

    public String getEmojiForKey(String key) {
        return emojiByKey.getOrDefault(key, "");
    }

    public Drink buildDrinkFromKey(String key) {
        // delegate to analyzer (same builder keys)
        return analyzer.buildDrinkFromKeyPublic(key);
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }

    private static String formatCurrency(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        String res;
        try {
            res = nf.format(amount);
        } catch (Exception e) {
            res = "₱" + String.format("%.2f", amount);
        }
        if (!res.contains("₱")) res = "₱" + String.format("%.2f", amount);
        return res;
    }
}

class Order {
    private final Map<String, Integer> items; // key -> qty
    private final Menu menu;
    private String customerName = "Guest";

    public void setCustomerName(String name) {
        this.customerName = name;
    }
    public String getCustomerName(){
        return this.customerName;
    }

    public Order(Menu menu) {
        this.items = new LinkedHashMap<>();
        this.menu = menu;
    }

    public void addItem(String key, int qty) {
        items.put(key, items.getOrDefault(key, 0) + qty);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void printCart() {
        if (items.isEmpty()) {
            System.out.println("\nYour cart is empty ✨");
            return;
        }
        System.out.println("\n────────── ✧ CURRENT CART ✧ ──────────");
        double total = 0.0;
        for (Map.Entry<String, Integer> e : items.entrySet()) {
            String key = e.getKey();
            int qty = e.getValue();
            double price = menu.getPriceForKey(key);
            // FIXED: use menu.getNameForKey instead of menu.nameByKey.get(key)
            System.out.printf("%-28s x%d    %s\n", menu.getNameForKey(key), qty, formatCurrency(price * qty));
            total += price * qty;
        }
        System.out.println("-----------------------------------------");
        System.out.println("TOTAL: " + formatCurrency(total));
        System.out.println("─────────────────────────────────────────");
    }

    public void checkout() {
        if (items.isEmpty()) {
            System.out.println("\nCart empty. Nothing to checkout.");
            return;
        }
        // print receipt
        System.out.println();
        System.out.println("────────── ✧ FINAL RECEIPT ✧ ──────────");
        System.out.println("Customer: " + customerName + " ⭐");
        double total = 0.0;
        for (Map.Entry<String, Integer> e : items.entrySet()) {
            String key = e.getKey();
            int qty = e.getValue();
            double price = menu.getPriceForKey(key);
            // FIXED: use menu.getNameForKey instead of menu.nameByKey.get(key)
            System.out.printf("%-28s x%d    %s\n", menu.getNameForKey(key), qty, formatCurrency(price * qty));
            total += price * qty;
        }
        System.out.println("-----------------------------------------");
        System.out.println("TOTAL                         " + formatCurrency(total));
        System.out.println("-----------------------------------------");
        System.out.println("\"Thank you, " + customerName + "! Your presence warmed our café 🌙\"");
        System.out.println("Enjoy your brews — see you again ✨");
        System.out.println("─────────────────────────────────────────");
        items.clear();
    }

    private static String formatCurrency(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        String res;
        try {
            res = nf.format(amount);
        } catch (Exception e) {
            res = "₱" + String.format("%.2f", amount);
        }
        if (!res.contains("₱")) res = "₱" + String.format("%.2f", amount);
        return res;
    }
}

abstract class Drink {
    private final String name;
    private final List<String> ingredients;
    private String description;

    public Drink(String name) {
        this.name = name;
        this.ingredients = new ArrayList<>();
        this.description = "";
    }

    public String getName() { return name; }
    public List<String> getIngredients() { return new ArrayList<>(ingredients); }
    public void addIngredient(String ingredient) { ingredients.add(ingredient); }
    public void setDescription(String desc) { this.description = desc; }
    public String getDescription() { return description; }
    public abstract void brew();
    public void serve() {
        System.out.println("\nYour drink is ready!");
        System.out.println(formatTitle(this.getName()));
        System.out.println("\"" + this.getDescription() + "\"");
        System.out.println("Ingredients: " + String.join(", ", this.getIngredients()));
    }

    protected String formatTitle(String title) { return "* " + title.toUpperCase() + " *"; }
}

class Smoothie extends Drink {
    public Smoothie(String name) { super(name); }
    @Override
    public void brew() {
        System.out.println("\n✨ Brewing your " + getName() + "...");
        System.out.println("---------------------------------------");
        for (String ing : getIngredients()) System.out.println("🥭 Adding " + ing + "...");
        try { TimeUnit.MILLISECONDS.sleep(450); } catch (InterruptedException ignored) {}
        System.out.println("🧊 Blending until smooth and bright...");
    }
}

class Tea extends Drink {
    public Tea(String name) { super(name); }
    @Override
    public void brew() {
        System.out.println("\n🍵 Brewing your " + getName() + "...");
        System.out.println("---------------------------------------");
        for (String ing : getIngredients()) System.out.println("🌿 Steeping " + ing + "...");
        try { TimeUnit.MILLISECONDS.sleep(450); } catch (InterruptedException ignored) {}
        System.out.println("🫖 Letting the warmth steep softly...");
    }
}

class Coffee extends Drink {
    public Coffee(String name) { super(name); }
    @Override
    public void brew() {
        System.out.println("\n☕ Brewing your " + getName() + "...");
        System.out.println("---------------------------------------");
        for (String ing : getIngredients()) System.out.println("💥 Preparing " + ing + "...");
        try { TimeUnit.MILLISECONDS.sleep(450); } catch (InterruptedException ignored) {}
        System.out.println("🔥 Extracting a bold and comforting pour...");
    }
}

class Elixir extends Drink {
    public Elixir(String name) { super(name); }
    @Override
    public void brew() {
        System.out.println("\n🌟 Brewing your " + getName() + "...");
        System.out.println("---------------------------------------");
        for (String ing : getIngredients()) System.out.println("✨ Mixing " + ing + " gently...");
        try { TimeUnit.MILLISECONDS.sleep(450); } catch (InterruptedException ignored) {}
        System.out.println("💫 Infusing with a whisper of comfort...");
    }
}

class Cocoa extends Drink {
    public Cocoa(String name) { super(name); }
    @Override
    public void brew() {
        System.out.println("\n🍫 Brewing your " + getName() + "...");
        System.out.println("---------------------------------------");
        for (String ing : getIngredients()) System.out.println("🫗 Melting/Adding " + ing + "...");
        try { TimeUnit.MILLISECONDS.sleep(450); } catch (InterruptedException ignored) {}
        System.out.println("☁️  Stirring until nostalgia warms the cup...");
    }
}

class MoodAnalyzer {
    private final Map<String, String> moodToDrinkKey;
    private final List<String> supportedMoods;
    private final Map<String, String> quoteByMood;
    private final Map<String, String> emojiByMood;
    private final Map<String, String> keyToMood;

    public MoodAnalyzer() {
        moodToDrinkKey = new HashMap<>();
        supportedMoods = new ArrayList<>();
        quoteByMood = new HashMap<>();
        emojiByMood = new HashMap<>();
        keyToMood = new HashMap<>();

        register("distracted", "evergreen_iced_matcha", "Focus is simply the choice to return your attention, again and again.", "🍃");
        register("sleepy", "misty_matcha_delight", "If you lack energy, start anyway. Consistency creates the momentum.", "🌙");
        register("cranky", "frosted_matcha_frappe", "Pause. Your mood is a temporary state, not a permanent perspective.", "❄️");
        register("overwhelmed", "comfort_matcha_latte", "Break the giant task into tiny, visible beginnings.", "🍵");
        register("sluggish", "dream_matcha_smoothie", "Don't wait for motivation to arrive. Start moving, and it will catch up.", "🥭");
        register("nostalgic", "caramel_memory_mocha", "Sip and remember — sweet moments never really fade.", "🍫");
        register("heartbroken", "midnight_mocha_tears", "Even the bitter has its beauty — let it teach you to heal.", "💧");
        register("hopeful", "sunrise_vanilla_bliss", "Every sunrise tastes like another chance to begin again.", "🌅");
        register("reflective", "autumn_chai_calm", "Slow down — some thoughts are best brewed in silence.", "🍂");
        register("joyful", "berry_bloom_blizz", "Let happiness bubble — you deserve to sparkle today.", "🍓");
        register("bored", "mocha_mystery_mix", "Life’s too bland for dull flavors — stay curious!", "🌀");
        register("inspired", "vanilla_dream_brew", "May every sip spark another idea worth chasing.", "🌸");
        register("excited", "sparkling_strawberry", "You’re fizzing with life — let this match your thrill!", "✨");
        register("lonely", "honey_warmth_milk", "A cup to remind you — you’re never alone.", "🐚");
        register("anxious", "lavender_serenity_latte", "Let the aroma slow your thoughts and soothe your soul.", "💜");
    }

    private void register(String mood, String key, String quote, String emoji) {
        moodToDrinkKey.put(mood.toLowerCase(), key);
        keyToMood.put(key, mood.toLowerCase());
        supportedMoods.add(mood.toLowerCase());
        quoteByMood.put(mood.toLowerCase(), quote);
        emojiByMood.put(mood.toLowerCase(), emoji);
    }

    public List<String> getSupportedMoods() { return new ArrayList<>(supportedMoods); }
    public boolean hasMood(String m) { return m != null && moodToDrinkKey.containsKey(m.toLowerCase()); }
    public String getQuoteForMood(String m) { return quoteByMood.getOrDefault(m.toLowerCase(), ""); }
    public String getEmojiForMood(String m) { return emojiByMood.getOrDefault(m.toLowerCase(), ""); }
    public String getKeyForMood(String mood) { return moodToDrinkKey.get(mood.toLowerCase()); }
    public String getMoodForKey(String key) { return keyToMood.getOrDefault(key, null); }
    public String getEmojiForKey(String key) {
        String mood = keyToMood.get(key);
        if (mood == null) return "";
        return emojiByMood.getOrDefault(mood, "");
    }

    // ADDED METHOD: Required by SipHappens.handleMoodFlow
    public Drink getDrinkForMood(String mood) throws InvalidMoodException {
        String key = getKeyForMood(mood);
        if (key == null) {
            throw new InvalidMoodException("Could not find a drink for the mood: " + mood);
        }
        return buildDrinkFromKeyPublic(key);
    }

    public Drink buildDrinkFromKeyPublic(String key) {
        return buildDrinkFromKey(key);
    }


    public Drink buildDrinkFromKey(String key) {
        switch (key) {
            case "evergreen_iced_matcha": {
                Coffee c = new Coffee("Evergreen Iced Matcha");
                c.addIngredient("matcha powder"); c.addIngredient("honey"); c.addIngredient("oat milk");
                c.setDescription("Focus is simply the choice to return your attention, again and again.");
                return c;
            }
            case "misty_matcha_delight": {
                Coffee c = new Coffee("Misty Matcha Delight");
                c.addIngredient("matcha powder"); c.addIngredient("honey"); c.addIngredient("oat milk"); c.addIngredient("espresso shot");
                c.setDescription("If you lack energy, start anyway. Consistency creates the momentum.");
                return c;
            }
            case "frosted_matcha_frappe": {
                Coffee c = new Coffee("Frosted Matcha Frappe");
                c.addIngredient("matcha powder"); c.addIngredient("honey"); c.addIngredient("oat milk"); c.addIngredient("whipped cream");
                c.setDescription("Pause. Your mood is a temporary state, not a permanent perspective.");
                return c;
            }
            case "comfort_matcha_latte": {
                Coffee c = new Coffee("Comfort Matcha Latte");
                c.addIngredient("matcha powder"); c.addIngredient("honey"); c.addIngredient("steamed oat milk");
                c.setDescription("Break the giant task into tiny, visible beginnings.");
                return c;
            }
            case "dream_matcha_smoothie": {
                Smoothie s = new Smoothie("Dream Matcha Smoothie");
                s.addIngredient("matcha powder"); s.addIngredient("honey"); s.addIngredient("oat milk"); s.addIngredient("frozen banana");
                s.setDescription("Don't wait for motivation to arrive. Start moving, and it will catch up.");
                return s;
            }
            case "caramel_memory_mocha": {
                Coffee c = new Coffee("Caramel Memory Mocha");
                c.addIngredient("espresso"); c.addIngredient("caramel syrup"); c.addIngredient("steamed milk"); c.addIngredient("whipped cream");
                c.setDescription("Sip and remember — sweet moments never really fade.");
                return c;
            }
            case "midnight_mocha_tears": {
                Coffee c = new Coffee("Midnight Mocha Tears");
                c.addIngredient("dark chocolate"); c.addIngredient("espresso"); c.addIngredient("sea salt"); c.addIngredient("milk foam");
                c.setDescription("Even the bitter has its beauty — let it teach you to heal.");
                return c;
            }
            case "sunrise_vanilla_bliss": {
                Smoothie s = new Smoothie("Sunrise Vanilla Bliss");
                s.addIngredient("vanilla"); s.addIngredient("honey"); s.addIngredient("oat milk"); s.addIngredient("dash of cinnamon");
                s.setDescription("Every sunrise tastes like another chance to begin again.");
                return s;
            }
            case "autumn_chai_calm": {
                Tea t = new Tea("Autumn Chai Calm");
                t.addIngredient("chai spices"); t.addIngredient("black tea"); t.addIngredient("milk"); t.addIngredient("nutmeg");
                t.setDescription("Slow down — some thoughts are best brewed in silence.");
                return t;
            }
            case "berry_bloom_blizz": {
                Smoothie s = new Smoothie("Berry Bloom Blizz");
                s.addIngredient("strawberry"); s.addIngredient("raspberry"); s.addIngredient("soda water"); s.addIngredient("mint leaves");
                s.setDescription("Let happiness bubble — you deserve to sparkle today.");
                return s;
            }
            case "mocha_mystery_mix": {
                Coffee c = new Coffee("Mocha Mystery Mix");
                c.addIngredient("espresso"); c.addIngredient("cocoa"); c.addIngredient("choco flakes");
                c.setDescription("Life’s too bland for dull flavors — stay curious!");
                return c;
            }
            case "vanilla_dream_brew": {
                Smoothie s = new Smoothie("Vanilla Dream Brew");
                s.addIngredient("vanilla"); s.addIngredient("almond milk"); s.addIngredient("cinnamon dust");
                s.setDescription("May every sip spark another idea worth chasing.");
                return s;
            }
            case "sparkling_strawberry": {
                Elixir e = new Elixir("Sparkling Strawberry ");
                e.addIngredient("strawberry syrup"); e.addIngredient("soda water"); e.addIngredient("mint");
                e.setDescription("You’re fizzing with life — let this match your thrill!");
                return e;
            }
            case "honey_warmth_milk": {
                Elixir e = new Elixir("Honey Warmth Milk");
                e.addIngredient("milk"); e.addIngredient("honey"); e.addIngredient("cinnamon");
                e.setDescription("A cup to remind you — you’re never alone.");
                return e;
            }
            case "lavender_serenity_latte": {
                Coffee c = new Coffee("Lavender Serenity Latte");
                c.addIngredient("lavender syrup"); c.addIngredient("steamed milk"); c.addIngredient("vanilla");
                c.setDescription("Let the aroma slow your thoughts and soothe your soul.");
                return c;
            }
            default: {
                Tea defaultTea = new Tea("House Tea");
                defaultTea.addIngredient("water");
                defaultTea.setDescription("A simple sip.");
                return defaultTea;
            }
        }
    }
}


class InvalidMoodException extends Exception {
    public InvalidMoodException(String message) { super(message); }
}