#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "mpi.h"


// Estrutura de ponto com duas dimensões e o cluster a qual ele pertence
typedef struct {
    double x;
    double y;
    int cluster;
} Point;

// Estrutura de cluster com os centroides do cluster( a identificação será feita pelo indice do vetor)
typedef struct Cluster {
    double centroid_x;
    double centroid_y;
} Cluster;


const int NUM_OF_POINTS = 120;
const int NUM_OF_CLUSTERS = 4;



// Cria o vetor de pontos com coordenadas aleatorias
Point *create_rand_points(const int num_of_points) {
    Point *p = (Point *) malloc(num_of_points * sizeof(Point));
    for (int i = 0; i < num_of_points; i++) {
        p[i].x = (rand() / (double) RAND_MAX);
        p[i].y = (rand() / (double) RAND_MAX);
        p[i].cluster = 0;
    }

    return p;
}

// Pega os n (numero de clusters) primeiros pontos e define como centroides iniciais dos clusters
Cluster *initialize_clusters(const int num_of_clusters, Point *p) {
    Cluster *clusters = (Cluster *) malloc(num_of_clusters * sizeof(Point));
    for (int i = 0; i < num_of_clusters; i++) {
        clusters[i].centroid_x = p[i].x;
        clusters[i].centroid_y = p[i].y;
    }
    return clusters;
}

// Distancia entre um ponto e um centroide
float distance2(const Point *point, const Cluster *cluster) {
    float dist = 0.0;
    float diff = point->x - cluster->centroid_x;
    dist += diff * diff;
    diff = point->y - cluster->centroid_y;
    dist += diff * diff;

    return dist;
}

// Chama a funcão de distancia para todos os clusters e um ponto para determinar qual o melhor centroide(menor distancia) para aquele ponto
int assign_point(const Point *point, Cluster *clusters) {
    int best_cluster = 0;
    float best_dist = distance2(point, &clusters[0]);
    for (int c = 1; c < NUM_OF_CLUSTERS; c++) {
        float dist = distance2(point, &clusters[c]);
        if (dist < best_dist) {
            best_cluster = c;
            best_dist = dist;
        }
    }
    return best_cluster;
}

// Atualiza os centroides de acordo com o vetor com as somas dos pontos ( entender melhor e modificar )
void update_centroids(int offset, int points_per_process, Point *points, Cluster *clusters, int cluster_id, int proc_id) {
    int itr_points = 0;
    double sum_x, sum_y = 0.0;
    double points_per_cluster = 0.0;
    double centroid_x, centroid_y = 0.0;

    sum_x = 0.0;
    sum_y = 0.0;
    centroid_x = 0.0;
    centroid_y = 0.0;
    points_per_cluster = 0.0;

    // Faz o vetor de soma, com a soma de todos os pontos e conta o numero de pontos e cada cluster
    for (itr_points = offset; itr_points < points_per_process + offset; itr_points++) {

        if (points[itr_points].cluster == cluster_id) {
            points_per_cluster++;
            sum_x += points[itr_points].x;
            sum_y += points[itr_points].y;
        }
    }

    // Calcula os novos centroides, soma de todos os pontos / pontos por cluster
    centroid_x = sum_x / points_per_cluster;
    centroid_y = sum_y / points_per_cluster;


    // Caso o centroid seja diferente do anterior ele atualiza no cluster com o novo centroid calculado
    if (centroid_x != clusters[cluster_id].centroid_x || centroid_y != clusters[cluster_id].centroid_y) {
        clusters[cluster_id].centroid_x = centroid_x;
        clusters[cluster_id].centroid_y = centroid_y;
        printf("Centroide{proc: %d} do cluster %d mudou!\n", proc_id, cluster_id);
    } else {
        printf("Centroide{proc: %d} do cluster %d não sofreu alterações\n", proc_id, cluster_id);
    }
}


// Imprime os centroides dos clusters (mudar para dar print em uma string formada ja com todos os centroid, varios prints ta ficando bagunçado)
void print_centroids(Cluster *c, char* proc_id) {
    printf("Centroides:{proc: %d}\n", proc_id);

    for (int i = 0; i < NUM_OF_CLUSTERS; i++) {
        printf("Centroide{proc: %s cluster:%d x: %f , y: %f}", proc_id , i,  c[i].centroid_x, c[i].centroid_y);

        printf("\n");
    }
}

int main(int argc, char *argv[]) {

    int num_proc, proc_id, dest_proc, source_proc, offset, offset_tag, points_tag, points_per_process;

    // Inicializa o MPI
    MPI_Status status;
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &num_proc);


    MPI_Comm_rank(MPI_COMM_WORLD, &proc_id);


    // Tipo da mensagem que vai ser enviada, Tipo 1 para o offset que o processo ira tratar, e tipo 2 para os pontos que o processo ira tratar
    offset_tag = 1;
    points_tag = 2;


    // Calcula o numero de pontos por processo e inicializa os pontos e clusters.
    points_per_process = (NUM_OF_POINTS / num_proc);
    Point *points = create_rand_points(NUM_OF_POINTS);
    Cluster *clusters = initialize_clusters(NUM_OF_CLUSTERS, points);

    // Loop do kmeans
    int exec = 0;
    while (exec < 10) {



        // Processo Master (pai de todos)
        if (proc_id == 0) {
            offset = points_per_process;


            //Manda cada parte do vetor para aum processo
            for (dest_proc = 1; dest_proc < num_proc; dest_proc++) {
                MPI_Send(&offset, 1, MPI_INT, dest_proc, offset_tag, MPI_COMM_WORLD);
                MPI_Send(&points[offset], points_per_process, MPI_DOUBLE, dest_proc, points_tag, MPI_COMM_WORLD);
                offset = offset + points_per_process;
            }

            // Bota o offset como 0 para iterar denovo
            offset = 0;



            // Aqui ele determina qual melhor cluster para os pontos
            for (int point_iterator = offset; point_iterator < points_per_process + offset; point_iterator++) {
                points[point_iterator].cluster = assign_point(&points[point_iterator], clusters);
            }



            // Aqui ele atualiza os centroides
            for (int cluster_iterator = 0; cluster_iterator < NUM_OF_CLUSTERS; cluster_iterator++) {
                update_centroids(offset, points_per_process, points, clusters, cluster_iterator, proc_id);
            }



            // Aqui ele recebe os pontos que foram calculados nos processos filhos com seus respectivos clusters atribuidos e o offset
            for (int proc = 1; proc < num_proc; proc++) {
                MPI_Recv(&offset, 1, MPI_INT, proc, offset_tag, MPI_COMM_WORLD, &status);
                MPI_Recv(&points[offset], points_per_process, MPI_DOUBLE, proc, points_tag, MPI_COMM_WORLD, &status);
            }

            // Imprime os centroides atualizados
            char processor_name[MPI_MAX_PROCESSOR_NAME];
            int name_len;
            MPI_Get_processor_name(processor_name, &name_len);
            print_centroids(clusters, processor_name);


        }

        // Processos Slaves
        if (proc_id > 0) {
            source_proc = 0;
            // Aqui ele recebe do processo pai a parte do vetor que ele irá processar
            MPI_Recv(&offset, 1, MPI_INT, source_proc, offset_tag, MPI_COMM_WORLD, &status);
            MPI_Recv(&points[offset], points_per_process, MPI_DOUBLE, source_proc, points_tag, MPI_COMM_WORLD, &status);

            // Escolhe o melhor cluster para cada ponto
            for ( int point_iterator = offset; point_iterator < points_per_process + offset; point_iterator++) {
                points[point_iterator].cluster = assign_point(&points[point_iterator], clusters);
            }

            // Atualiza os centroides
            for (int cluster_iterator = 0; cluster_iterator < NUM_OF_CLUSTERS; cluster_iterator++) {
                update_centroids(offset, points_per_process, points, clusters, cluster_iterator, proc_id);
            }

            dest_proc = 0;
            // Envia para o processo pai os pontos da parte que ele recebeu do processo pai com seus respectivos clusters
            MPI_Send(&offset, 1, MPI_INT, dest_proc, offset_tag, MPI_COMM_WORLD);
            MPI_Send(&points[offset], points_per_process, MPI_DOUBLE, dest_proc, points_tag, MPI_COMM_WORLD);


            char processor_name[MPI_MAX_PROCESSOR_NAME];
            int name_len;
            MPI_Get_processor_name(processor_name, &name_len);
            print_centroids(clusters, processor_name);


        }
        exec++;
    }

    MPI_Finalize();

    return 0;
}
