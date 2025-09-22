from pytm import TM, Server, Datastore, Dataflow, Actor

tm = TM("TM for payment cancelation")
tm.description = "TM for payment cancelation"
tm.isOrdered = True

user = Actor("Sender Entity")
web = Server("Web server")
db = Datastore("PostgreSQL")

user_to_web = Dataflow(user, web, "User wants to see the payments")
web_to_database = Dataflow(web, db, "Searches for all payments by this user")
database_to_web = Dataflow(db, web, "Retrieves all payments with specifications")
web_to_user = Dataflow(web, user, "Shows payment's information")
user_to_web = Dataflow(user, web, "User selects one payment to cancel")
web_to_database = Dataflow(web, db, "Searches for that payment")
database_to_web = Dataflow(db, web, "Retrieves the payment")
web_to_database = Dataflow(web, db, "Insert payment information to be in cancelled state")
database_to_web = Dataflow(db, web, "Retrieves information approval/dismiss")
web_to_user = Dataflow(web, user, "Shows confirm/error revealed")

tm.process()
