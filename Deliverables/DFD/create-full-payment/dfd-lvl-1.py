from pytm import TM, Actor, Server, Datastore, Dataflow

tm = TM("Create Full Payment - Level 1")
tm.isOrdered = True

sender = Actor("Sender Entity")
WebServer = Server("WebServer")
db = Datastore("PostgreSQL")


df1 = Dataflow(sender, WebServer, "Submit full payment request")
df2 = Dataflow(WebServer, WebServer, "Validate and process full payment")
df3 = Dataflow(WebServer, db, "Store payment data")
df4 = Dataflow(db, WebServer, "Data stored confirmation")
df5 = Dataflow(WebServer, WebServer, "Processing result")
df6 = Dataflow(WebServer, sender, "Final confirmation")

tm.process()
