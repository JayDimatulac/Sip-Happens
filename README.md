# 🌙 Sip Happens Café ☕✨  


## ✧ Overview
Sip Happens Café is a whimsical Java console café that turns your moods into flavorful experiences. Whether you’re craving a cozy matcha latte, a dreamy smoothie, or just a moment of virtual escape, this interactive program lets you sip, savor, and explore the charm of café life—right from the comfort of your terminal. Every choice you make is a small adventure in taste and mood, blending the joy of drinks with a dash of playful interactivity. 

**Key functionalities include:** 

🍂 Full Café Menu - Browse aesthetically named drinks with emojis and prices  
🎭 Mood-Based Recommendations - Receive drinks that match your current emotional  
🛒 Interactive Cart System - Add drinks, choose quantities, view totals  
🧾 Checkout & Receipt - enerate a clean, formatted receipt with your name and drinks
🧃 Browse drinks directly by name
💡User-Friendly Commands - Navigate easily using `menu`, `mood`, `suggest`, `cart`, and `checkout`.  

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
`╔══════════ ✧ DRINK MENU ✧ ══════════╗
 Distracted  — Evergreen Iced Matcha       ₱160 🍃
 Joyful      — Berry Bloom Blizz           ₱150 🍓
 Anxious     — Lavender Serenity Latte     ₱150 💜
╚══════════════════════════════════════╝`











