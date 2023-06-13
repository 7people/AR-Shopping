from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
import sys
import os
sys.path.append(os.path.abspath("Database"))
import db
#from Database import db 

categories = [
   
     {
        "name":"telefon",
        "image":"telefon"
    },
       {
        "name":"bilgisayar+esyaları",
        "image":"bilgisayar+esyaları"
    },
       {
        "name":"masa",
        "image":"masa"
    },
 {
        "name":"bilgisayarlar",
        "image":"bilgisayarlar"
    },
    {
        "name":"Elektronik",
        "image":"Elektronik"
    },
    {
        "name":"erkek-giyim-aksesuar",
        "image":"erkek-giyim-aksesuar"
    },
    {
        "name":"cocuk-giyim-aksesuar",
        "image":"cocuk-giyim-aksesuar"
    },
    {
        "name":"spor-giyim",
        "image":"spor-giyim"
    },
    {
        "name":"Araç",
        "image":"Arac"
    },
    {
        "name":"Kitap",
        "image":"Kitap"
    },
    {
        "name":"supermarket",
        "image":"supermarket"
    }
]

for c in categories :
    db.insertCategorie(c["name"], c["image"])

    driver = webdriver.Chrome(ChromeDriverManager().install())
    driver.get("https://www.akakce.com/arama/?q="+c["name"])
    driver.implicitly_wait(3)

    elems = driver.find_elements(by=By.CSS_SELECTOR, value=".fw_v9 li")

    for e in elems:
        if "TL" in e.text:
            link = e.find_element(by=By.TAG_NAME, value='a').get_attribute("href")
            name = e.find_element(by=By.TAG_NAME, value='a').get_attribute("title").strip()
            img = e.find_element(by=By.TAG_NAME, value="img").get_attribute("src")
            print("\n---------------------------------")
            print("link :",link)
            print("---------------------------------\n")
            driver2 = webdriver.Chrome(ChromeDriverManager().install())
            driver2.get(link)
            driver2.implicitly_wait(3)
            description_html = driver2.find_element(by=By.CLASS_NAME, value='bb_w').get_attribute('innerHTML')
            description = ""
            if "dts_v8" in description_html:
                description = driver2.find_element(by=By.CLASS_NAME, value='dts_v8').text.strip()
            sellers = driver2.find_elements(by=By.CSS_SELECTOR, value="#PL_h li")
            for s in sellers:
                seller_html = s.find_element(by=By.CSS_SELECTOR, value=':first-child:last-child').get_attribute('innerHTML')
                if "v_v8" in seller_html:
                    seller_html = s.find_element(by=By.CSS_SELECTOR, value=':first-child .v_v8').get_attribute('innerHTML')
                    if "<b>" in seller_html:
                        seller_name = s.find_element(by=By.CSS_SELECTOR, value=':first-child .v_v8 b').text.replace("/", "").strip()
                    else:
                        seller_name = s.find_element(by=By.CSS_SELECTOR, value=':first-child .v_v8').text.replace("/", "").replace("Yorumları oku", "").strip()
                else:
                    seller_name = s.find_element(by=By.CSS_SELECTOR, value=':first-child:last-child').text.strip()
                if "<img" in seller_html:
                    seller_image = s.find_element(by=By.CSS_SELECTOR, value=':first-child .w_v8 img').get_attribute("data-src")
                    alt = s.find_element(by=By.CSS_SELECTOR, value=':first-child .w_v8 img').get_attribute("alt")
                    if alt != "":
                        seller_name = s.find_element(by=By.CSS_SELECTOR, value=':first-child .w_v8 img').get_attribute("alt").strip()+" / "+seller_name
                else:
                    seller_image = ""
                price = s.find_element(by=By.CSS_SELECTOR, value=':first-child .pt_v8').text.strip().replace(".", "").replace(",", ".").replace(" TL", "")
                print("\n---------------------------------")
                print("link :",link)
                print("name :",name)
                print("description :",description)
                print("img :",img)
                print("category_id", db.getCategorieID(c["name"]))
                print("price :", price)
                print("seller_name :", seller_name)
                print("seller_image :", seller_image)
                print("---------------------------------\n")
                db.InsertSeller(seller_name, seller_image)
                db.InsertProduct(name, img, description, db.getCategorieID(c["name"]))
                seller_id = db.getSellerID(seller_name)
                product_id = db.getProductID(name)
                if seller_id > 0 and product_id > 0:
                    db.InsertProductSeller(price, seller_id, product_id)
                    
            driver2.quit()

    driver.quit()