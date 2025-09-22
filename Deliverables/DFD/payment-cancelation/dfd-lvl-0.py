from pytm import TM, Server, Datastore, Dataflow, Actor

tm = TM("TM for payment cancelation")
tm.description = "TM for payment cancelation"
tm.isOrdered = True

user = Actor("Sender Entity")
web = Server("Web server")
db = Datastore("PostgreSQL")

user_to_web = Dataflow(user, web, "User wants to cancel a payment")
web_to_database = Dataflow(web, db, "Insert cancelation info")
database_to_web = Dataflow(db, web, "Retrieves information approval/dismiss")
web_to_user = Dataflow(web, user, "Shows confirm/error on cancelation")

tm.process()
