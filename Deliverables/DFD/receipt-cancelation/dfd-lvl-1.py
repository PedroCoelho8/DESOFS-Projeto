from pytm import TM, Server, Datastore, Dataflow, Actor

tm = TM("TM for receipt cancelation")
tm.description = "TM for receipt cancelation"
tm.isOrdered = True

user = Actor("Receiver Entity")
web = Server("Web server")
db = Datastore("PostgreSQL")

user_to_web = Dataflow(user, web, "User wants to see the receipts")
web_to_database = Dataflow(web, db, "Searches for all receipts by this user")
database_to_web = Dataflow(db, web, "Retrieves all receipts with specifications")
web_to_user = Dataflow(web, user, "Shows receipt's information")
user_to_web = Dataflow(user, web, "User selects one receipt to cancel")
web_to_database = Dataflow(web, db, "Searches for that receipt")
database_to_web = Dataflow(db, web, "Retrieves the receipt")
web_to_database = Dataflow(web, db, "Insert receipt information to be in cancelled state")
database_to_web = Dataflow(db, web, "Retrieves information approval/dismiss")
web_to_user = Dataflow(web, user, "Shows confirm/error revealed")

tm.process()
