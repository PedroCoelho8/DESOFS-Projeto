from pytm import TM, Server, Datastore, Dataflow, Actor

tm = TM("TM for User Registration")
tm.description = "DFD Nível 1 para processo de registo de utilizador"
tm.isOrdered = True

user = Actor("User")
web = Server("Web Server")
db = Datastore("PostgreSQL")

df1 = Dataflow(user, web, "User submits registration form with credentials")
df2 = Dataflow(web, web, "Validate registration form fields")
df3 = Dataflow(web, db, "Check if username/email already exists")
df4 = Dataflow(db, web, "Returns existence check result")
df5 = Dataflow(web, db, "Store hashed password and user data")
df6 = Dataflow(db, web, "Return account creation result")
df7 = Dataflow(web, user, "Show success/failure registration message")

tm.process()
