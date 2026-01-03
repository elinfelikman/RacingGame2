Racing Game - Android Project
A fast-paced Android racing game developed as part of an academic assignment. The game challenges players to navigate a vehicle through obstacles while managing lives and striving for the highest score.

🚗 Game Overview
In this game, the player controls a car moving across a 5-lane highway. The goal is to avoid obstacles (like rocks) and collect bonuses (like coins) to achieve the highest possible distance.

✨ Key Features
Dynamic Movement: Control the car using on-screen buttons or mobile sensors (Accelerometer) for an immersive experience.

Obstacle System: Randomly generated obstacles and bonuses appear in different lanes.

Life Management: A 3-heart system where each collision removes one life.

High Score Table: Persistent storage of the top 10 scores using SharedPreferences and JSON.

Location Integration: Uses Google Maps API to show exactly where each high score was achieved.

Adaptive UI: Designed with ConstraintLayout and LinearLayout weights to ensure a consistent experience across different screen sizes.

🛠 Tech Stack
Language: Kotlin

UI Architecture: XML (ConstraintLayout, FrameLayout, LinearLayout)

Data Persistence: SharedPreferences / GSON

APIs: Google Maps SDK

Hardware: Sensor Manager (Accelerometer & Vibration)

🚀 How to Run
Clone the repository: git clone https://github.com/elinfelikman/RacingGame2.git

Open the project in Android Studio.

Ensure you have a Google Maps API Key configured in your local.properties or AndroidManifest.xml.

Build and run the app on an emulator or a physical device.
