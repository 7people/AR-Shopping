## About
The AR shopping application we have developed for Google Glass Enterprise Edition 2 enables users to dynamically engage in shopping experiences through a wearable technological device using augmented reality. Additionally, users can interact with real-world objects and QR codes through integrated QR code scanning and object recognition technologies within the application, allowing them to view and proceed with the purchase of products in the system.
## How to open AR Shopping project in Android Studio?
First download the project file from Github. Afterwards, update the part that says 'WINDOWS_USER_NAME' in the `local.properties` files located in the `ARShopping` and `Gesture Library` folders in the project you downloaded, with your Windows user name.
```
sdk.dir=C\:\\Users\\WINDOWS_USER_NAME\\AppData\\Local\\Android\\Sdk
```
After this step, run the Android Studio program and then select the `ARShopping` folder in the project file named `AR-Shopping` that you downloaded on the `Open File or Project` screen. After opening the project on Android Studio, you need to complete the steps in `How to run API?` and `Change E-Mail Configurations`.
## How to run API?
After downloading the project file, open the folder named `scraping_api_arshoping` on Visual Studio Code. Then open a terminal in VSC, then type `python -m flask run --host=0.0.0.0` and run it.

When you run this command, you will see an output like this in the terminal, for example:
```
 * Running on all addresses (0.0.0.0)
 * Running on http://127.0.0.1:5000
 * Running on http://192.168.1.20:5000
```
Here, copy the IP address at the bottom and follow the path `app > java > com.example.glass.arshopping > utils > Global.java` on Android Studio. Then update the copied IP address in the relevant section in the code block below.
```java
public class Global {
    static public String url = "http://IP_ADDRESS:PORT";
}
```
## Change E-Mail Configurations
In order for the `Forgot Password` feature in the application to work, the e-mail address and password information in the relevant field must be updated. For this, follow the path `app > java > com.example.glass.arshopping > Forgotpassword.java`. On this file, replace `CHANGE HERE WITH YOUR EMAIL ADDRESS` and `CHANGE HERE WITH WITH PASSWORD` with the e-mail account information you want the e-mails to go to.
```java
SendMail mail = new SendMail("CHANGE HERE WITH YOUR EMAIL ADDRESS", "CHANGE HERE WITH WITH PASSWORD",
  email,
  "One Time Password - AR Shopping App",
  "Your One Time Password Code:" + OTP
);
```
The part you need to pay attention to in this section is to create an 'App Password' in Gmail or Outlook and enter it instead of your e-mail account password in the password section.
## Test The Application
There is a user account in the database to test the project. After completing the steps above, you can test whether the project works correctly using this user account. The user account information is as follows:
```
Username: demo
E-mail: demo@demo.com
Password: demo
```
## Developers
- Ali Kanal [@AlkaliNN](https://github.com/AlkaliNN "@AlkaliNN")
- Bahri Şahin Demirtaş [@bahridts](https://github.com/bahridts "@bahridts")
- Berkay Mustafaoğlu [@wilwaerin](https://github.com/wilwaerin "@wilwaerin")
- Mehmet Aytaç [@MehmetAytac](https://github.com/MehmetAytac "@MehmetAytac")
- Mehmet Sedat Yıldız [@Muunsparks](https://github.com/Muunsparks "@Muunsparks")
- Rabia Çevik [@rabiacevikk](https://github.com/rabiacevikk "@rabiacevikk")
- Uğur Barış Özyürek [@Barisozyurek](https://github.com/Barisozyurek "@Barisozyurek")
