from pytm import TM, Actor, Server, Datastore, Dataflow

tm = TM("Create Partial Payment")
tm.isOrdered = True
sender = Actor("Sender Entity")
web = Server("Web server")
db = Datastore("PostgreSQL")

df1 = Dataflow(sender, web, "User creates parcial payment")
df2 = Dataflow(web, db, "Creates payment and associated documents")
df3 = Dataflow(db, web, "Confirmation data stored")
df4 = Dataflow(web, sender, "Return confirmation to sender")

tm.process()
