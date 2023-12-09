1. the two designs give 2 alternative approaches to implementing the system.
   The client side implementation applies less load for Honeyhive aws but would be hard to scale for very high workloads.
   The server side implementation has a lot more added complexity and cost but could scale up better as all processing is within the comapny.

2. the client-side folder contains the python class that can be made into a library for small scale/ be run as a seperate container for production systems.
3. The server side implementation is not complete. It was meant as a mock Kafka queue and would invoke the EmailSender.
