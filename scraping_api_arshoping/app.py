import sys
import os
sys.path.append(os.path.abspath("Database"))
import db
#from Database import db 
from flask import Flask, jsonify,request

app = Flask(__name__)

if __name__ == '__main__':
    app.debug = True
    app.run()
@app.route("/products/<category_id>/<search>", methods=["GET"])
def getProducts(category_id, search):
    return jsonify(db.getProducts(category_id, search)) #converting object to json

@app.route("/product/<product_id>", methods=["GET"])
def getProduct_x(product_id):
    return jsonify(db.getProduct_x(product_id)) #converting object to json
@app.route("/categories", methods=["GET"])
def getCategories():
    return jsonify(db.getCategories())

@app.route("/comparison/<product_id>", methods=["GET"])
def getSellersProduct(product_id):
    return jsonify(db.getSellersPricesProduct(product_id))

@app.route("/signup", methods=["POST"])
def signUp():
    data = request.get_json(force=True)
    user_name = str(data['user_name'])
    email =str(data['user_email']) 
    password = str(data['user_password'])
    return db.signUp(user_name,email,password)

@app.route("/login", methods=["POST"])
def logIn():
    data = request.get_json(force=True)
    email =str(data['user_name']) 
    password = str(data['user_password'])
    return db.Login(email, password) 

@app.route("/resetpsw", methods=["POST"])
def reset_password():
    data = request.get_json(force=True)
    email =str(data['email'])
    #otp =str(data['otp'])
    #date =str(data['reset_date'])
    return db.reset_password(email) 

 
@app.route("/chcotp", methods=["POST"])
def check_otp():
    data = request.get_json(force=True)
    email =str(data['email'])
    otp =str(data['otp'])
    return db.check_otp(email,otp) 



@app.route("/pswchange", methods=["POST"])
def password_change():
    data = request.get_json(force=True)
    email = str(data['email'])
    password = str(data['password'])
    return db.password_change1(email, password)

@app.route("/addcart", methods=["POST"])
def addCart():
    data = request.get_json(force=True)
    user = str(data['user_id']) 
    seller = str(data['seller_id'])
    product = str(data['product_id']) 
    return db.addCart(user, seller, product)

@app.route("/removecart", methods=["POST"])
def removeCart():
    data = request.get_json(force=True)
    id = str(data['id']) 
    return db.removeCart(id)

@app.route("/checkout", methods=["POST"])
def checkOut():
    data = request.get_json(force=True)
    user_id=str(data['user_id'])
    card_num = str(data['card_number'])
    card_user =str(data['card_username']) 
    expr_date = str(data['expiration_date'])
    adress=str(data['address'])
    phone_number=str(data['phone_number'])
    cvv=str(data['cvv'])
    cart_id=str(data['cart_id'])

    return db.checkOut(user_id,card_user,card_num,expr_date,cvv,adress,phone_number,cart_id)
@app.route("/orders", methods=["POST"])
def orders():
    data = request.get_json(force=True)
    user_id=str(data['user_id'])
    credit_card_id = str(data['credit_card_id'])
    cart_id =str(data['cart_id']) 

    return db.orders(user_id, credit_card_id, cart_id)
@app.route("/cart/<user_name>", methods=["GET"])
def getCartProduct(user_name):
    return jsonify(db.getCartProduct(user_name)) #converting object to json

@app.route("/orderlist/<credit_card_id>", methods=["GET"])
def getOrdersProduct(credit_card_id):
    return jsonify(db.getOrdersProduct(credit_card_id)) #converting object to json
    
@app.route("/myorders/<order_id>", methods=["GET"])
def getOrderseach(order_id):
    return jsonify(db.getOrderseach(order_id)) #converting object to json
    
@app.route("/orderl/<user_id>", methods=["GET"])
def getOrders(user_id):
    return jsonify(db.getOrders(user_id)) #converting object to json

@app.route("/getlatestpasswordreset/<user_email>", methods=["GET"])
def getLatestPasswordReset(user_email):
    return jsonify(db.getLatestPasswordReset(user_email))    

@app.route("/getlatestcreditCardID/<user_id>", methods=["GET"])
def getLatestcreditCardID(user_id):
    return jsonify(db.getLatestcreditCardID(user_id))   
    
@app.route("/getAccountDetails/<username>", methods=["GET"])
def getAccountDetails(username):
    return jsonify(db.getAccountDetails(username))