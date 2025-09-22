#!/usr/bin/env python3

from pytm import TM, Server, Datastore, Dataflow, Actor

tm = TM("TM for updating user personal information")
tm.description = "TM for updating a user's personal information"
tm.isOrdered = True

user = Actor("User Entity")
web = Server("Web server")
db = Datastore("PostgreSQL")

user_to_web = Dataflow(user, web, "User requests to update personal information")
web_to_database = Dataflow(web, db, "Insert user personal information")
database_to_web = Dataflow(db, web, "Retrieves update approval/dismiss")
web_to_user = Dataflow(web, user, "Display update/error result to user")

tm.process()