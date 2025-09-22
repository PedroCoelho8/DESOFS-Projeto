from pytm import TM, Actor, Server, Datastore, Dataflow

tm = TM("User Registration")
tm.isOrdered = True


user = Actor("User not registered")
web = Server("Web server")
db = Datastore("PostgreSQL")


df1 = Dataflow(user, web, "Submit registration form")
df2 = Dataflow(web, db, "Store user credentials and profile data")
df3 = Dataflow(db, web, "Registration status")
df4 = Dataflow(web, user, "Confirm registration")

tm.process()
