all: clean build

build:
	javac Printer.java PrintJob.java PrinterManager.java PrinterManagerImple.java PrinterServer.java PrinterClient.java
	mkdir out

run-server:
	java -Djava.security.policy="security.policy" PrinterServer

run-client:
	java -Djava.security.policy="security.policy" PrinterClient

run-2-clients: run-client-01 run-client-02

run-4-clients: run-client-01 run-client-02 run-client-03 run-client-04 

run-8-clients: run-client-01 run-client-02 run-client-03 run-client-04 run-client-05 un-client-06 un-client-07 un-client-08

run-client-01:
	java -Djava.security.policy="security.policy" PrinterClient

run-client-02:
	java -Djava.security.policy="security.policy" PrinterClient

run-client-03:
	java -Djava.security.policy="security.policy" PrinterClient

run-client-04:
	java -Djava.security.policy="security.policy" PrinterClient

run-client-05:
	java -Djava.security.policy="security.policy" PrinterClient

run-client-06:
	java -Djava.security.policy="security.policy" PrinterClient

run-client-07:
	java -Djava.security.policy="security.policy" PrinterClient

run-client-08:
	java -Djava.security.policy="security.policy" PrinterClient

clean:
	rm -f *.class
	rm -rf out
