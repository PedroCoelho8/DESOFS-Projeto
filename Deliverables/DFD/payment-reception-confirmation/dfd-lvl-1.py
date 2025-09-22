#!/usr/bin/env python3
from pytm import TM, Server, Datastore, Dataflow, Actor

tm = TM("TM for confirmation of receipt of payment - Level 1")
tm.description = "Level 1 DFD: Expanded flow where the user selects and confirms a received payment"
tm.isOrdered = True

user = Actor("Receiver Entity")
web = Server("Web server")
db = Datastore("PostgreSQL")

user_to_web = Dataflow(user, web, "User wants to see the received payments")
web_to_database = Dataflow(web, db, "Searches for all received payments by this user")
database_to_web = Dataflow(db, web, "Return list of received payments")
web_to_user = Dataflow(web, user, "Display received payment's information")
user_to_web = Dataflow(user, web, "User selects a payment to confirm")
web_to_database = Dataflow(web, db, "Searches for that payment")
database_to_web = Dataflow(db, web, "Return the payment")
web_to_database = Dataflow (web, db, "Validate receiver identity")
database_to_web = Dataflow(db, web, "Return identity validation result")
web_to_database = Dataflow(web, db, "Insert confirmation of receipt")
database_to_web = Dataflow(db, web, "Return information approval/dismiss")
web_to_user = Dataflow(web, user, "Shows confirm/error revealed")

tm.process()