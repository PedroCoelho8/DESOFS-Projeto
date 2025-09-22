#!/usr/bin/env python3
from pytm import TM, Server, Datastore, Dataflow, Actor

tm = TM("TM for updating user personal information")
tm.description = "TM for updating a user's personal information"
tm.isOrdered = True

user = Actor("User Entity")
web = Server("Web server")
db = Datastore("PostgreSQL")

user_to_web = Dataflow(user, web, "User wants to update some personal information")
web_to_db = Dataflow(web, db, "Search current user personal information")
db_to_web = Dataflow(db, web, "Return current user personal information")
web_to_user = Dataflow(web, user, "Shows user current personal information")
user_to_web = Dataflow(user, web, "User submits updated personal information")
web_to_user = Dataflow(web, db, "Validate submitted personal information")
db_to_web = Dataflow(db, web, "Return validation result")
web_to_db = Dataflow(web, db, "Update personal information in database")
db_to_web = Dataflow(db, web, "Return update status")
web_to_user = Dataflow(web, user, "Display success or error message")

tm.process()