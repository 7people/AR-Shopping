import os
import sqlite3
import random
import datetime

database_name = str(os.getcwd()+'\\Database\\arshopping.db').replace("\\", "/");
# getcwd()->it gives path of the project
# \ --> new line

# ----------------------------------------------- 
# Functions Used in scraping to fill database
# ----------------------------------------------- 


def insertCategorie(name, image):
    conn = sqlite3.connect(database_name)
    query = "INSERT INTO categories (category_name, category_image) SELECT '"+name+"', '"+image+"' \
        WHERE (SELECT COUNT(category_id) FROM categories WHERE category_name = '"+name+"') = 0; "
    conn.execute(query)
    conn.commit()
    conn.close()


def getCategorieID(name):
    conn = sqlite3.connect(database_name)
    query = "SELECT category_id FROM categories WHERE category_name = '"+name+"'"
    cursor = conn.execute(query) 
    result = 0
    for e in cursor:
        result = e[0]
    conn.commit()
    conn.close()
    return result


def getSellerID(name):
    name = name.replace("'","")
    conn = sqlite3.connect(database_name)
    query = "SELECT seller_id FROM sellers WHERE seller_name = '"+name+"'"
    cursor = conn.execute(query)
    result = 0
    for e in cursor: 
        result = e[0]
    conn.commit()
    conn.close()
    return result


def getProductID(name):
    name = name.replace("'","")
    conn = sqlite3.connect(database_name)
    query = "SELECT product_id FROM products WHERE product_name = '"+name+"'"
    cursor = conn.execute(query)
    result = 0
    for e in cursor:
        result = e[0]
    conn.commit()
    conn.close()
    return result

def getProduct_x(product_id):
    product_id = product_id.replace("'","")
    conn = sqlite3.connect(database_name)
    try:
        query = "SELECT product_id,product_image,product_name,product_description FROM products WHERE product_id = '"+product_id+"';"
        cursor = conn.execute(query)
        result = []
        for row in cursor:
            e = {
                "product_id":row[0],
                "product_image":row[1],
                "product_name":row[2],
                "product_description":row[3]
            }
            result.append(e)
        conn.commit()
        conn.close() 
        return {
            "Success":True,
            "Message":"Categories List",
            "Data": result
        }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
            "Message":str(e)
        }



####
def InsertProduct(name, img, description, category_id):
    name = name.replace("'","") #some name has ' this symbol ı fixed it by change this symbol to empty.
    img = img.replace("'","")
    description = description.replace("'","")
    conn = sqlite3.connect(database_name)
    query = "INSERT INTO products (product_name, product_image, product_description, category_id) SELECT '"+name+"', '"+img+"', \
        '"+description+"', '"+str(category_id)+"' WHERE (SELECT COUNT(product_id) FROM products WHERE product_name = '"+name+"') = 0; "
    conn.execute(query)
    conn.commit()
    conn.close()


def InsertSeller(name, img):
    name = name.replace("'","")
    if img is not None:
        img = img.replace("'","")
    else:
        img = ""
    conn = sqlite3.connect(database_name)
    query = "INSERT INTO sellers (seller_name, seller_image) SELECT '"+name+"', '"+img+"' \
        WHERE (SELECT COUNT(seller_id) FROM sellers WHERE seller_name = '"+name+"') = 0; "
    conn.execute(query)
    conn.commit()
    conn.close()


def InsertProductSeller(price, seller_id, product_id):
    price = price.replace("'","")
    conn = sqlite3.connect(database_name)
    query = "INSERT INTO product_sellers (product_id, seller_id, price) SELECT '"+str(product_id)+"', '"+str(seller_id)+"', '"+str(price)+"'  \
        WHERE (SELECT COUNT(product_sellers_id) FROM product_sellers WHERE product_id = '"+str(product_id)+"' AND seller_id = '"+str(seller_id)+"') = 0; "
    conn.execute(query)
    conn.commit()
    conn.close()


# -----------------------------------------------
# Functions Used in API
# -----------------------------------------------

def getProducts(category_id, search):
    conn = sqlite3.connect(database_name)
    try:
        search = search.replace("'","")
        if search != "0":
            qr = " WHERE product_name LIKE '%"+search+"%' OR product_description LIKE '%"+search+"%' \
                OR (SELECT c.category_name FROM categories c WHERE c.category_id = p.category_id) LIKE '%"+search+"%'"
        else :
            qr = ""

        if category_id == str(0):
            query = "SELECT product_id, product_name, product_image, product_description, \
                (SELECT c.category_name FROM categories c WHERE c.category_id = p.category_id) FROM products p "+qr+" ;"
        else :
            query = "SELECT product_id, product_name, product_image, product_description, \
                c.category_name FROM products p JOIN categories c ON c.category_id = p.category_id AND c.category_id = "+category_id+" "+qr+" ;"
        cursor = conn.execute(query)
        print(query)
        result = [] #declaring the list
        for row in cursor:
            e = {
                "product_id":row[0],
                "product_name":row[1],
                "product_image":row[2],
                "product_description":row[3],
                "category":row[4]
            }
            result.append(e) #pushing our object inside list
        conn.commit() #executing the query
        conn.close() 
        return {
            "Success":True,
            "Message":"Products List",
            "Data": result
        }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
            "Message":str(e)
        }

def getCategories():
    conn = sqlite3.connect(database_name)
    try:
        query = "SELECT category_id, category_name, category_image FROM categories;"
        cursor = conn.execute(query)
        result = []
        for row in cursor:
            e = {
                "category_id":row[0],
                "category_name":row[1],
                "category_image":row[2]
            }
            result.append(e)
        conn.commit()
        conn.close() 
        return {
            "Success":True,
            "Message":"Categories List",
            "Data": result
        }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
            "Message":str(e)
        }

def getSellerById(id):
    conn = sqlite3.connect(database_name)
    query = "SELECT * FROM sellers WHERE seller_id = '"+id+"';"
    cursor = conn.execute(query)
    result = {}
    for row in cursor:
        result = {
            "seller_id":row[0],
            "seller_name":row[1],
            "seller_image":row[2]
        }
    conn.commit()
    conn.close()
    return result


def getSellersPricesProduct(product_id): #getting all the seller and prices
    conn = sqlite3.connect(database_name)
    try:
        conn = sqlite3.connect(database_name)
        query = "SELECT seller_id, price FROM product_sellers WHERE product_id = '"+product_id+"';"
        cursor = conn.execute(query)
        result = []
        for row in cursor:
            e = {
                "price":row[1],
                "seller":getSellerById(row[0])
            }
            result.append(e)
        conn.commit()
        conn.close()
        return {
            "Success":True,
            "Message":"Comparaison data",
            "Data": result
        }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
            "Message":str(e)
        }


#login and signup 
def signUp(user_name,user_email,user_password):
    conn = sqlite3.connect(database_name)
    try:
        query = "INSERT INTO users (user_name,user_email,user_password) SELECT '"+user_name+"', '"+user_email+"', '"+user_password+"'"
        conn.execute(query)
        conn.commit()
        conn.close()
        return {
            "Success":True,
            "Message":"User registred!"
        }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
            "Message":str(e)
        }

def Login(user_name,user_password):
    conn = sqlite3.connect(database_name)
    try:
        query = "select user_id from users where user_name='"+user_name+"' and user_password='"+user_password+"'"
        cursor = conn.execute(query)
        result = 0
        for e in cursor:
            result = e[0]
        conn.commit()
        conn.close()
        if result !=0 :
            return {
                "Success":True,
                "Message":"User logged in!"
            }
        else: 
            return {
                "Success":False,
                "Message":"user_name or user_password incorrect!"
            }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
            "Message":str(e)
        }


def reset_password(email):
    conn = sqlite3.connect(database_name)
    try:

        otp=random.randint(100000, 999999)
        reset_date=datetime.datetime.now()
        query = "INSERT INTO password_reset (email,otp,reset_date) SELECT '"+email+"', '"+str(otp)+"', '"+str(reset_date)+"'"
        conn.execute(query)
        conn.commit()
        conn.close()
        return {
            "Success":True,
            "Message":"Code Send!"
        }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
            "Message":str(e)
        }
    
def check_otp(email,otp):
    conn= sqlite3.connect(database_name)
    try:
        query = "select id from password_reset where email='"+str(email)+"' and otp='"+str(otp)+"'"
        cursor = conn.execute(query)
        result = 0
        for e in cursor:
            result = e[0]
        conn.commit()
        conn.close()
        if result !=0:
            return {
                "Success":True,
                "Data":result
            }
        else: 
            return {
                "Success":False,
                "Message":"Incorrect Otp"
            }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
              "Message":str(e)
        }

def password_change1(email,password):
    conn= sqlite3.connect(database_name)
    try:
        query = "update users set user_password='"+str(password)+"' where user_email='"+str(email)+"';" 
        cursor = conn.execute(query)
        conn.commit()
        conn.close()
        return {
            "Success":True,
            "Message":"rrr!"

        }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
              "Message":str(e)
        }
def addCart(user, seller, product):
    conn = sqlite3.connect(database_name)
    try:
        quantity = 1
        is_deleted = 0
        date = datetime.datetime.now()
        cart_id = None

        # Check if the product already exists in the cart
        query1 = "SELECT cart_id, quantity FROM cart WHERE user_id = ? AND product_id = ? AND seller_id = ? AND is_deleted = 0"
        cursor = conn.cursor()
        cursor.execute(query1, (user, product, seller))
        existing_cart = cursor.fetchone()

        if existing_cart is not None:
            cart_id, quantity = existing_cart
            # Product already exists in the cart, increment the quantity by 1
            update_query = "UPDATE cart SET quantity = quantity + 1 WHERE cart_id = ? AND is_deleted = 0 AND seller_id = ?"
            cursor.execute(update_query, (cart_id, seller))
        else:
            # Generate a new cart_id
            cart_id_query = "COALESCE((SELECT MAX(cart_id) FROM cart WHERE user_id = ? AND is_deleted = 0), \
                (SELECT CASE WHEN MAX(cart_id) IS NOT NULL THEN MAX(cart_id)+1 ELSE 1 END FROM cart))"
            insert_query = "INSERT INTO cart (user_id, seller_id, product_id, quantity, date, is_deleted, cart_id) \
                VALUES (?, ?, ?, ?, ?, ?, (" + cart_id_query + "))"
            cursor.execute(insert_query, (user, seller, product, quantity, date, is_deleted, user))
        
        conn.commit()
        conn.close()

        return {
            "Success": True,
            "Message": "Added to Cart!"
        }
    except Exception as e:
        conn.close()
        return {
            "Success": False,
            "Message": str(e)
        }



def removeCart(id):
    conn = sqlite3.connect(database_name)
    try:
        # Check if the product already exists in the cart
        query1 = "SELECT id, product_id, seller_id, quantity, cart_id, user_id FROM cart WHERE id=? AND is_deleted=0 AND quantity>=1"
        cursor = conn.cursor()
        cursor.execute(query1, (id,))
        existing_product = cursor.fetchone()
        if existing_product is not None:
            query = "DELETE FROM cart WHERE id=? AND is_deleted=0 and quantity =1"
            update_query = "UPDATE cart SET quantity = quantity - 1 WHERE id=? AND is_deleted=0 AND quantity != 1"
            cursor.execute(query, (id,))
            cursor.execute(update_query, (id,))
        else:
            pass
        conn.commit()
        conn.close()
        return {
            "Success": True,
            "Message": "Removed from Cart!"
        }
    except Exception as e:
        conn.close()
        return {
            "Success": False,
            "Message": str(e)
        }

def getCartProduct(user_name): 

    conn = sqlite3.connect(database_name)
    try:
    
      
        query = "SELECT id, product_id,\
        (SELECT p.product_image from products p where p.product_id=c.product_id )product_img\
        ,(SELECT p.product_name from products p where p.product_id=c.product_id )product_name, (select seller_name from sellers where seller_id =c.seller_id) seller_id,c.quantity, c.date ,cart_id from cart c where\
               c.user_id ='"+user_name+"' and is_Deleted=0;"

        cursor = conn.execute(query)
        result = [] #declaring the list
        for row in cursor:
            e = {
                  "id":row[0],
                  "product_id":row[1],
                   "product_img":row[2],
                "product_name":row[3],
                "seller_id":row[4],
                "quantity":row[5],
                "date":row[6],
                 "cart_id":row[7]

              

            }
            result.append(e) #pushing our object inside list
        conn.commit() #executing the query
        conn.close() 
        return {
            "Success":True,
            "Message":"Cart List",
            "Data": result
        }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
            "Message":str(e)
        }   

def getLatestPasswordReset(user_email):
    conn = sqlite3.connect(database_name)
    try:
        query = "SELECT otp FROM password_reset WHERE id = (SELECT MAX(id) FROM password_reset) and email = '"+user_email+"';"
        cursor = conn.execute(query)
        result = []
        for row in cursor:
            e = {
                "otp":row[0]
            }
            result.append(e)
        conn.commit()
        conn.close()
        return {
            "Success":True,
            "Message":"OTP",
            "Data": result
        }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
            "Message":str(e)
        }  
def getLatestcreditCardID(user_id):
    conn = sqlite3.connect(database_name)
    try:
        query = "SELECT credit_card_id FROM credit_cards WHERE credit_card_id = (SELECT MAX(credit_card_id) FROM credit_cards where user_id = '"+user_id+"') ;"
        cursor = conn.execute(query)
        result = []
        for row in cursor:
            e = {
                "credit_card_id":row[0]
            }
            result.append(e)
        conn.commit()
        conn.close()
        return {
            "Success":True,
            "Message":"Getting ID is successfull ",
            "Data": result
        }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
            "Message":str(e)
        }        
def checkOut(user_id,card_user,card_num,expr_date,cvv,adress,phone_number,cart_id):
    conn = sqlite3.connect(database_name)
    try:
        query = "INSERT INTO credit_cards (user_id,card_username,card_number,expiration_date,cvv,adress,phone_number,cart_id)\
        SELECT '"+user_id+"', '"+card_user+"', '"+card_num+"','"+expr_date+"','"+cvv+"','"+adress+"','"+phone_number+"','"+cart_id+"';"
       
        conn.execute(query)
        conn.commit()
        conn.close()
        return {
            "Success":True,
            "Message":"Checked Out!"
        }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
            "Message":str(e)
        } 
def orders(user_id, credit_card_id, cart_id):
    conn = sqlite3.connect(database_name)
    try:
        query1 = "INSERT INTO orders (user_id, credit_card_id, cart_id) \
                  VALUES ('"+user_id+"', '"+credit_card_id+"', '"+cart_id+"');"
        query2 = "UPDATE cart SET is_deleted=1 WHERE cart_id='"+cart_id+"' AND user_id='"+user_id+"' and is_deleted=0;"
        
        conn.execute(query1)
        conn.execute(query2)
        
        conn.commit()
        conn.close()
        return {
            "Success":True,
            "Message":"Checked Out!"
        }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
            "Message":str(e)
        }
def getOrdersProduct(credit_card_id): 

    conn = sqlite3.connect(database_name)
    try:
   
     query = "SELECT o.order_id, cc.user_id, p.product_id,\
             p.product_image, p.product_name, s.seller_name, cc.quantity, o.order_date, \
price AS product_price, \
 (select SUM(ps.price * c.quantity) from product_sellers ps,cart c where c.seller_id=ps.seller_id and c.product_id=ps.product_id and c.cart_id=cc.cart_id )AS total_amount, \
cd.adress, cd.card_number \
FROM orders o, cart cc, credit_cards cd, products p, sellers s, product_sellers ps \
WHERE o.cart_id = cc.cart_id AND s.seller_id = cc.seller_id \
AND ps.seller_id = s.seller_id AND ps.product_id = p.product_id AND p.product_id = cc.product_id \
AND cc.cart_id = cd.cart_id AND o.credit_card_id = cd.credit_card_id and  o.credit_card_id = '"+credit_card_id+"' \
     GROUP BY o.order_id, p.product_id,s.seller_name"
 

            
     cursor = conn.execute(query)
     result = [] #declaring the list
     for row in cursor:
            e = {
                  "order_id":row[0],
                  "user_id":row[1],
                   "product_id":row[2],
                     "product_img":row[3],
                "product_name":row[4],
                "seller_name":row[5],
                "quantity":row[6],
                "order_date":row[7],
                 "product_price":row[8],
              "total_amount":row[9],
               "adress":row[10],
                "card_number":row[11]
               
            }
            result.append(e) #pushing our object inside list
     conn.commit() #executing the query
     conn.close() 
     return {
            "Success":True,
            "Message":"Order List",
            "Data": result
        }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
            "Message":str(e)
        }     
#Orders list for each users to make list 
def getOrders(user_id):
    conn = sqlite3.connect(database_name)
    try:
        query = "SELECT o.order_id, cc.user_id, o.order_date, cc.cart_id, (SELECT SUM(ps.price * c.quantity) FROM product_sellers ps, cart c WHERE c.seller_id = ps.seller_id AND c.product_id = ps.product_id AND c.cart_id = cc.cart_id) AS total_amount FROM orders o, cart cc, credit_cards cd, products p, sellers s, product_sellers ps WHERE o.cart_id = cc.cart_id AND s.seller_id = cc.seller_id AND ps.seller_id = s.seller_id AND ps.product_id = p.product_id AND p.product_id = cc.product_id AND cc.cart_id = cd.cart_id AND o.credit_card_id = cd.credit_card_id AND o.user_id = '"+str(user_id)+"' GROUP BY o.order_id"
        cursor = conn.execute(query)
        result = []  # declaring the list
        for row in cursor:
            e = {
                "order_id": row[0],
                "user_id": row[1],
                "order_date": row[2],
                "cart_id": row[3],
                "total_amount": row[4]
            }
            result.append(e)  # pushing our object inside list

        conn.commit()  # committing the transaction
        conn.close()  # closing the connection

        return {
            "Success": True,
            "Message": "Order List2",
            "Data": result
        }
    except Exception as e:
        conn.close()
        return {
            "Success": False,
            "Message": str(e)
        }

def getOrderseach(order_id):
    conn = sqlite3.connect(database_name)
    try:
        query = "SELECT o.order_id, c.user_id, p.product_id,\
             p.product_image, p.product_name, s.seller_name, c.quantity, o.order_date, \
price AS product_price, \
SUM(ps.price * quantity) OVER (PARTITION BY c.cart_id) AS total_amount, \
cd.adress, cd.card_number \
FROM orders o, cart c, credit_cards cd, products p, sellers s, product_sellers ps \
WHERE o.cart_id = c.cart_id AND s.seller_id = c.seller_id \
AND ps.seller_id = s.seller_id AND ps.product_id = p.product_id AND p.product_id = c.product_id \
AND c.cart_id = cd.cart_id AND o.credit_card_id = cd.credit_card_id AND o.order_id = '"+order_id+"'"
        
        cursor = conn.execute(query)
        result = [] # declaring the list
        for row in cursor:
            e = {
                "order_id": row[0],
                "user_id": row[1],
                "product_id": row[2],
                "product_img": row[3],
                "product_name": row[4],
                "seller_name": row[5],
                "quantity": row[6],
                "order_date": row[7],
                "product_price": row[8],
                "total_amount": row[9],
                "adress": row[10],
                "card_number": row[11]
            }
            result.append(e) # pushing our object inside the list
        conn.commit() # executing the query
        conn.close() 
        return {
            "Success": True,
            "Message": "Order List2",
            "Data": result
        }
    except Exception as e:
        conn.close()
        return {
            "Success": False,
            "Message": str(e)
        }

def getAccountDetails(username):
    conn = sqlite3.connect(database_name)
    try:
        query = "SELECT user_id, user_name, user_email FROM users WHERE user_name = '"+username+"';"
        cursor = conn.execute(query)
        result = []
        for row in cursor:
            e = {
                "user_id":row[0],
                "user_name":row[1],
                "user_email":row[2]
            }
            result.append(e)
        conn.commit()
        conn.close()
        return {
            "Success":True,
            "Message":"User Details",
            "Data": result
        }
    except Exception as e:
        conn.close()
        return {
            "Success":False,
            "Message":str(e)
        }