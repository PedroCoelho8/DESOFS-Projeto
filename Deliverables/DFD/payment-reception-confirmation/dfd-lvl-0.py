#!/usr/bin/env python3
from pytm import TM, Server, Datastore, Dataflow, Actor

tm = TM("TM for confirmation of receipt of payment")
tm.description = "TM for confirming the reception of a payment"
tm.isOrdered = True

user = Actor("Receiver Entity")
web = Server("Web server")
db = Datastore("PostgreSQL")

user_to_web = Dataflow(user, web, "User confirms payment received")
web_to_database = Dataflow(web, db, "Insert confirmation of received payment data")
database_to_web = Dataflow(db, web, "Retrieves confirmation status")
web_to_user = Dataflow(web, user, "Displays confirmation success/failure")

tm.process()
