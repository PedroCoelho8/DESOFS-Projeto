from pytm import TM, Actor, Server, Datastore, Dataflow

tm = TM("Create Partial Payment - Level 1")
tm.isOrdered = True

sender = Actor("Sender Entity")


webserver = Server("WebServer")
db = Datastore("PostgreSQL")


df1 = Dataflow(sender, webserver, "Submit partial payment request")
df2 = Dataflow(webserver, webserver, "Validate and process partial payment")
df3 = Dataflow(webserver, db, "Store payment data")
df4 = Dataflow(db, webserver, "Data stored confirmation")
df5 = Dataflow(webserver, webserver, "Processing result")
df6 = Dataflow(webserver, sender, "Final confirmation")

tm.process()
