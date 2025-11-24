<h1 align="center">🌙 Sip Happens Café ☕✨</h1>



<h2 align="center"> ✧ Overview </h2>
Sip Happens Café is a whimsical Java console café that turns your moods into flavorful experiences. Whether you’re craving a cozy matcha latte, a dreamy smoothie, or just a moment of virtual escape, this interactive program lets you sip, savor, and explore the charm of café life—right from the comfort of your terminal. Every choice you make is a small adventure in taste and mood, blending the joy of drinks with a dash of playful interactivity. 

**Key functionalities include:** 

🍂 Full Café Menu  
🎭 Mood-Based Recommendations  
🛒 Interactive Cart System  
🧾 Checkout & Receipt  
🧃 Browse drinks directly by name  
💡User-Friendly Commands  

This project demonstrates OOP concepts, exception handling, collections, and interactive console input, blending learning with an engaging café-like experience. 


## 🧠 Object-Oriented Principles  
| Principle       | Description                                                                 | Key Examples                                                                                     |
|----------------|-------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| 🔒 Encapsulation | Fields are private; data accessed only through getters/setters.               | - Private fields in `Drink`, `Coffee`, `Tea`, `Smoothie`                                              |
|                | Prevents invalid states and protects internal data.                           | - Cart and menu stored internally inside Order/Menu classes                                     |
| 🎭 Abstraction   | Abstract classes hide complexity and expose only essentials.                  | - Drink abstract class (`name`, `price`, `description`)                                                |
|                | High-level methods without revealing internal logic.                           | - `getDetails()` abstract method implemented differently per drink                                 |
|                |                                                                               | - `MoodAnalyzer` hides mood-matching logic                                                         |
| 🧬 Inheritance   | Subclasses extend Drink to reuse shared properties and reduce code duplication. | - `Coffee`, `Tea`, `Elixir`, `Smoothie` inherit from Drink                                               |
|                | Allows scalable, organized drink hierarchy.                                   | - Shared features (`name`, `price`) come from base class                                      |
| 🔁 Polymorphism  | Same method call behaves differently depending on subclass.                   | - Single list storing many drink types                                                           |
|                | Enables flexible and uniform handling of different drink objects.             | - `getDetails()` or `brew()` call executes subclass version                                          |
|                |                                                                               | - Recommendations return `Drink` regardless of subclass                                            |


## ✨ Features
| Feature                     | Description                                                    |
| --------------------------- | -------------------------------------------------------------- |
| 🌸 **Aesthetic Drink Menu** | Browse unique, themed drinks with emojis and prices            |
| 🎭 **Mood Detection**       | Enter your mood and get a personalized drink recommendation    |
| 🛒 **Cart System**          | Add multiple drinks with quantities and view your current cart |
| 🧾 **Checkout & Receipt**   | Generate a formatted receipt with totals and your name         |
| 💡 **Interactive Commands** | `menu`, `mood <mood>`, `suggest`, `cart`, `checkout`, `exit`   |


## 🖼️ Sample Interaction  

**Menu Display**
```
╔══════════ ✧ DRINK MENU ✧ ══════════╗
 Reflective  — Autumn Chai Calm         ₱145.00 🍂
 Joyful      — Berry Bloom Blizz        ₱150.00 🍓
 Nostalgic   — Caramel Memory Mocha     ₱170.00 🍫
 ......
╚══════════════════════════════════════╝
```

**Asking or Help**

```
Your sip choice: help

Commands:
  menu       — Show the full drink menu
  mood <mood>— Ask by mood (e.g., mood anxious)
  suggest    — Let the cafe ask how you're feeling and suggest
  <drink>    — Type a drink name exactly to add it to cart
  cart       — View your cart
  checkout   — Finish and pay
  help       — Show this help
  exit       — Exit the café
```

**Mood-Based Recommendation**
```
Your sip choice: mood hopeful
────────── ✧ Mood Detected ✧ ──────────
 HOPEFUL 
"Every sunrise tastes like another chance to begin again."
────────── ✧ Mood Detected ✧ ──────────
✨ Brewing your Sunrise Vanilla Bliss...
🧊 Blending until smooth and bright...

Your drink is ready!
* SUNRISE VANILLA BLISS *
"Every sunrise tastes like another chance to begin again."
Ingredients: vanilla, honey, oat milk, dash of cinnamon
Price: ₱140.00

Add this to cart? (yes/no) > yes
Quantity > 2
Added 2 x Sunrise Vanilla Bliss to cart.
```

**Direct Drink Selection**
```
Your sip choice: Berry Bloom Blizz
────────── ✧ DRINK PREVIEW ✧ ──────────
   Berry Bloom Blizz 🍓
"Let happiness bubble — you deserve to sparkle today."
Ingredients: strawberry, raspberry, soda water, mint leaves
Price: ₱150
───────────────────────────────────────

Add to cart? (yes/no) > yes
Quantity > 1
Added 1 x Berry Bloom Blizz to cart.
```

**Viewing the Cart**

```
Your sip choice: cart

────────── ✧ CURRENT CART ✧ ──────────
Sunrise Vanilla Bliss        x2    ₱280.00
Berry Bloom Blizz            x1    ₱150.00
-----------------------------------------
TOTAL: ₱430.00
─────────────────────────────────────────
```

**Checkout & Receipt**
```
Your sip choice: checkout

May I have your name, please? Emmanuel

────────── ✧ FINAL RECEIPT ✧ ──────────
Customer: Emmanuel ⭐
Sunrise Vanilla Bliss        x2    ₱280.00
Berry Bloom Blizz            x1    ₱150.00
-----------------------------------------
TOTAL                         ₱430.00
-----------------------------------------
"Thank you, Emmanuel! Your presence warmed our café 🌙"
Enjoy your brews — see you again ✨
─────────────────────────────────────────
```

**Exiting the Café**
```
Your sip choice: exit

"May your days stay warm, and your heart stay full."
🌙 Sip Happens Café — come back when your mood seeks another flavor.
```


## Brewing the Program
To fully enjoy the Sip Happens Café experience, we recommend using IntelliJ IDEA Community Edition 2025.2 for a smooth coding and running experience. You can grab it **[here](https://www.jetbrains.com/idea/download/)**.

**Step 1: Prepare the Code**
      Save your Java file as:
```
SipHappens.java

```
**Step 2: Compile the Program**  
      Open your terminal or command prompt, navigate to the folder, and mix your code together with:
```
javac SipHappens.java

```
**Step 3: Run the Café**  
     Now it’s time to serve your virtual café! Start the program by typing:
 ```
 java SipHappens
    
 ```
You’ll be greeted with the full menu and interactive experience.  

**Step 4: Optional — IntelliJ Magic**  

+ Open IntelliJ IDEA.
+ Select Open Project and choose your SipHappens folder.
+ IntelliJ will detect your Java file automatically.
+ Click Run (or press Shift + F10) to start your café without touching the terminal.

💡 Pro Tip: IntelliJ offers live syntax hints, error detection, and debugging tools—perfect for experimenting with new drinks or adding features to your café code.

### 🌙 The Baristas Behind *Sip Happens*

<div align="center">

| Photo | Name | Role |
|-------|------|------|
| ![Head Brewmaster](images/Head_Brewmaster.jpg) | John Joseph Dimatulac | ☕ Brewmaster |
| ![Mood Alchemist](images/Mood_Alchemist.jpg) | Sheri Lou Hong | 🫖 Order Flow Guardian |
| ![Order Flow Assistant](images/Order_Flow_Assistant.jpg) | Stella Abigail Verana | 🌿 Mood Alchemist & Drink Conjurer |

</div>

> ✨ Every sip of this program reflects teamwork, creativity, and a sprinkle of debugging magic.










