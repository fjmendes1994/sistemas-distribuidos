all: clean build

build:
	mpicc -std=c99 kmeans-mpi.c -o kmeans

run-100-2:
	mpirun  -n 2 -host master,node001 ./kmeans 100

run-100-4:
	mpirun  -n 4 -host master,node001,node002,node003 ./kmeans 100

run-100-8:
	mpirun  -n 8 -host master,node001,node002,node003,node004,node005,node006,node007 ./kmeans 100

run-500-2:
	mpirun  -n 2 -host master,node001 ./kmeans 500

run-500-4:
	mpirun  -n 4 -host master,node001,node002,node003 ./kmeans 500

run-500-8:
	mpirun  -n 8 -host master,node001,node002,node003,node004,node005,node006,node007 ./kmeans 500

run-1000-2:
	mpirun  -n 2 -host master,node001 ./kmeans 1000

run-1000-4:
	mpirun  -n 4 -host master,node001,node002,node003 ./kmeans 1000

run-1000-8:
mpirun  -n 8 -host master,node001,node002,node003,node004,node005,node006,node007 ./kmeans 1000

clean:
	rm -f kmeans
