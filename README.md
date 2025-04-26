<p align="center">
  <img src="https://i.imgur.com/jDC0An7.png" alt="WasteExpress Logo" width="150"/>
</p>

## 🌱 Ideal Garden

Ideal Garden is a smart garden automation system designed for real-time monitoring and remote control.  
An ESP8266 microcontroller collects sensor data, synchronizes it with Firebase Realtime Database, and makes it accessible to users anytime, anywhere.

Through the Android app, users can monitor live sensor analytics, manage watering, lighting, trash levels, and door access, etc... 

If internet connectivity is unavailable, manual control can still be performed locally through the same Wi-Fi network, ensuring full control at all times.

Ideal Garden combines IoT connectivity, mobile control, and real-time analytics to simplify and automate garden management.


---

## 🎓 About the Project
This prototype was created at the request of a student as part of her final graduation project at the University of Constantine 2 Abdelhamid Mehri.

The goal was to continue the development of a smart agriculture system by creating a full mobile-based solution for real-time monitoring, control, and data visualization.

---

## 🚀 Features

### 🌱 Smart Watering
- 🌡️ **Soil Moisture Monitoring** – Real-time data from soil sensors
- 💧 **Automatic Watering** – Activates when soil is dry
- 📱 **Manual Watering** – Control watering from the mobile or web app

### 💡 Smart Lighting
- 🌙 **Night Detection** – Lights automatically turn on at night and turn off during the day using an LDR (light sensor).
- 👤 **Motion Triggered Lighting** – Lights activate when someone sits on the chair
- 📲 **App-Controlled Lighting** – Manual control via app

### 🚪 Garden Door Automation
- 🔄 **Servo Motor-Controlled Gate** – Open and close the garden door remotely
- 🧭 **Manual Door Control** – Full control through the app interface

### 🗑️ Trash Monitoring
- 📏 **Ultrasonic Level Detection** – Measures trash bin fill level
- 🟢 **Status Feedback** – Detects and displays if the bin is full or empty

### 📊 Real-time Analytics
- 📈 Live sensor data on mobile and web dashboards
- 🧠 Insightful visualizations for moisture, light, motion, and trash metrics

### 🔐 User Authentication & Connectivity
- 🔐 **Firebase Authentication** – Secure login and user management
- 📶 **Wi-Fi Connectivity (ESP8266)** – Ensures continuous communication with sensors


## 📊 Technologies Used

- **Mobile App**: Android (**Kotlin**)
- **Website**: HTML / CSS / JavaScript (for real-time analytics) - [GitHub Repository](https://github.com/charafboulmerka/IdealGardenWebsite)
- **Backend**: Firebase Authentication, Firebase Realtime Database
- **Hardware**: ESP8266 (code written in **C++** in `idealgarden_arduino_code.ino`)
- **Communication**: Wi-Fi

## 👨‍💻 How It Works

1. **ESP8266 board** reads data from multiple sensors:
   - Soil moisture sensor
   - LDR sensor (light detection)
   - Motion sensor
   - Trash bin sensor
2. The ESP8266:
   - Automatically controls actuators (water pump, lights, door servo) based on sensor thresholds.
   - Sends all sensor data and device statuses to **Firebase Realtime Database**.
3. **Mobile App** retrieves live data from Firebase:
   - Displays real-time soil moisture, lighting conditions, trash levels, and door status.
   - Allows manual control of watering, lighting, and door operations.
4. **Website** provides advanced real-time analytics and visualizations.

---

## 🔢 Architecture Overview

```
                                [Firebase]
                                    |      \
                                    |        \
                  [Sensors] --- [ESP8266]  --- [Mobile App & Website]
                                    |                            
                   [Water Pump, Lighting, Door, Trash]
```

---

## 🌟 Screenshots

<p float="left">
  <img src="https://i.imgur.com/6nglZQg.jpeg" width="45%" />
  <img src="https://i.imgur.com/XspHdcD.jpeg" width="45%" />
  <img src="https://i.imgur.com/NFEfhwk.jpeg" width="45%" />
  <img src="https://i.imgur.com/jT137VU.jpeg" width="45%" />
  <img src="https://i.imgur.com/uVFhSJb.jpeg" width="45%" />
  <img src="https://i.imgur.com/2C0c2E6.jpeg" width="45%" />
  <img src="https://i.imgur.com/9LU7MCO.jpeg" width="45%" />
</p>

---


## 📬 Contact

**Charaf Boulmerka**  
Android & Laravel Developer | IoT & CRM Solutions.
📧 charaf.boulmerka25@gmail.com  

---

## 📝 License

This project is open-source and available under the [MIT License](LICENSE).

