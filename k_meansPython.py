import numpy as np
import matplotlib.pyplot as plt

num_of_clusters = 3
dim = 2


def getClusters(n, dim):
	clusters = list()
	for i in range(n):
		cluster = 10*np.random.rand(dim)-5
		clusters.append(cluster)
	return clusters

def getChilds(n, dim):
	childrens = list()
	corresponds = list()
	for i in range(n):
		child = 10*np.random.rand(dim)-5
		childrens.append(child)
		corresponds.append(np.array([10,i,-1]))
	return childrens, corresponds

clusters = getClusters(num_of_clusters, dim)
childrens, corresponds = getChilds(20, dim)

def k_run(childrens, corresponds, clusters, num_of_clusters):
	for ch in range(len(childrens)):
		child = childrens[ch]
		childs_min = 1000
		cluster_num = -1
		for c in range(num_of_clusters):
			cluster = clusters[c]
			dist = np.linalg.norm(cluster-child)
			if dist < childs_min:
				childs_min = dist
				cluster_num = c
		corresponds[ch][2] = cluster_num
	return corresponds

def rearangeClusters(childrens, corresponds, clusters, num_of_clusters):
	for i in range(num_of_clusters):
		cluster_tot = np.zeros(dim)
		cluster_dev = 0
		for c in corresponds:
			if c[2] == i:
				cluster_tot = cluster_tot + childrens[c[1]]
				cluster_dev = cluster_dev + 1
		clusters[i] = cluster_tot/cluster_dev
	return clusters

corresponds = k_run(childrens, corresponds, clusters, num_of_clusters)
clusters = rearangeClusters(childrens, corresponds, clusters, num_of_clusters)

plt.plot(clusters[0][0],clusters[0][1], 'yo')
plt.plot(clusters[1][0],clusters[1][1], 'yo')
plt.plot(clusters[2][0],clusters[2][1], 'yo')

for i in range(10):
	corresponds = k_run(childrens, corresponds, clusters, num_of_clusters)
	clusters = rearangeClusters(childrens, corresponds, clusters, num_of_clusters)
	print(clusters[0])
	print(clusters[1])
	print(clusters[2])
	print('\n')


for c in range(len(childrens)):
	child = childrens[c]
	# print(corresponds[c])
	if corresponds[c][2] == 0:
		colour = 'b.'
	elif corresponds[c][2] == 1:
		colour = 'r.'
	elif corresponds[c][2] == 2:
		colour = 'g.'
	plt.plot(child[0],child[1], colour)
plt.plot(clusters[0][0],clusters[0][1], 'bo')
plt.plot(clusters[1][0],clusters[1][1], 'ro')
plt.plot(clusters[2][0],clusters[2][1], 'go')

plt.show()