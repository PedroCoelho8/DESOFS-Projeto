from pytm import TM, Server, Datastore, Dataflow, Actor

tm = TM("TM for User Login")
tm.description = "DFD Nível 1 para autenticação do utilizador"
tm.isOrdered = True

user = Actor("User")
web = Server("Web Server")
db = Datastore("PostgreSQL")


df1 = Dataflow(user, web, "User submits login form with credentials")
df2 = Dataflow(web, web, "Validate login form input")
df3 = Dataflow(web, db, "Fetch hashed password and user data by email")
df4 = Dataflow(db, web, "Return user credentials")
df5 = Dataflow(web, web, "Compare input password with stored hash")
df6 = Dataflow(web, web, "Generate session token")
df7 = Dataflow(web, user, "Return login success or failure with token/session")

tm.process()
