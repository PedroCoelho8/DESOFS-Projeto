from pytm import TM, Actor, Server, Datastore, Dataflow

tm = TM("User Login")
tm.isOrdered = True

user = Actor("User already registered")
web = Server("Web Server")
db = Datastore("PostgreSQL")

df1 = Dataflow(user, web, "Submit login credentials")
df2 = Dataflow(web, db, "Validate credentials")
df3 = Dataflow(db, web, "Authentication result")
df4 = Dataflow(web, user, "Login success/failure message")

tm.process()
