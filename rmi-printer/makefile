all: build start-rmi

build:
	javac Printer.java PrintJob.java PrinterManager.java PrinterManagerImple.java PrinterServer.java PrinterClient.java
	mkdir out

start-rmi:
	rmic PrinterManagerImple
	rmiregistry 2443 &

run-server:
	java PrinterServer

run-client:
	java PrinterClient

run-clients: run-client-01 run-client-02 run-client-03 run-client-04 run-client-05

run-client-01:
	java PrinterClient

run-client-02:
	java PrinterClient

run-client-03:
	java PrinterClient

run-client-04:
	java PrinterClient

run-client-05:
	java PrinterClient

clean:
	rm -f *.class
	rm -rf out
