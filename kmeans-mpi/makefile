all: clean build

build:
	mpicc kmeans-mpi.c -o kmeans

run-2:
	mpirun --allow-run-as-root -n 2 -host master,node001 ./kmeans

run-4:
	mpirun --allow-run-as-root -np 4 kmeans

run-8:
	mpirun --allow-run-as-root -np 8 kmeans

clean:
	rm -f kmeans
