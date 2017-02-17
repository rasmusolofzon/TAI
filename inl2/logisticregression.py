import numpy as np 
import sys
from math import exp
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from matplotlib import cm
from matplotlib.ticker import LinearLocator,FormatStrFormatter

#returns sum of sqared errors
def SSE(w, x, y):
	sse = 0
	for i in range(len(x[0])):
		wx=0
		for j in range(len(w)):
			wx += w[j]*x[i][j]
		sse += (y[i] - wx) ** 2
	return sse

def threshold(w, x):
	z = 0
	for i in range(len(w)):
		z += w[i] * x[i]
	return 1/(1+exp(-z))

def perceptronLearningRule(w, x, y, alfa):
	wUpdated = [0 for i in range(len(w))]
	hw = threshold(w, x)
	for i in range(len(w)):
		wUpdated[i] = w[i] + alfa*(y-hw)*hw*(1-hw)*x[i]
	return wUpdated

def shuffleTwo(x, y):
	savedSeed = np.random.randint(0, 200000)
	np.random.seed(savedSeed)
	np.random.shuffle(x)
	np.random.seed(savedSeed)
	np.random.shuffle(y)
	return (x, y)

#load file and puts in computable format in two matrices
fileName = sys.argv[1]
f = open(fileName, "r")
data = f.readlines()
rows = len(data)
cols = len(data[0].split())

x = np.zeros(shape=(rows,cols))
y = [0] * rows

for i in range(rows):
	row = data[i].split()
	y[i] = float(row[0])
	for j in range(cols):
		if (j==0):
			x[i,j] = 1
		else:
			a = row[j]
			a = a[2:]
			x[i,j] = float(a)

#Scaling. assumes positive values.
maxVal = np.amax(x)
if (maxVal>1):
	scalefactor = 1/maxVal
	for i in range(rows):
		for j in range(1,cols):
			x[i,j] = x[i][j] * scalefactor
maxVal = np.amax(x)


#set inital values
w = [0 for i in range(cols)]
x, y = shuffleTwo(x, y)
indexSet = 0
epochs = 0
alfa = 1
epsilon = 0.001
oldSSE = SSE(w, x, y)
w = perceptronLearningRule(w, x[indexSet], y[indexSet], alfa)
indexSet+=1
alfa = 1000 / (1000 + indexSet)

#loop that runs until change of sse is lower than epsilon
while (abs(SSE(w, x, y)-oldSSE) > epsilon):
	oldSSE = SSE(w, x, y)
	w = perceptronLearningRule(w, x[indexSet], y[indexSet], alfa)
	alfa = 1000 / (1000+(indexSet+epochs*rows))
	indexSet+=1

	if indexSet%rows==0 and indexSet!=0:
		indexSet = 0
		shuffleTwo(x, y)
		#print(alfa, w)
		epochs+=1

print("Success!")
print(w)


############################################
#if the data has two attributes (1 1:... 2:...)
#we plot the data and the dividing plane in 3d space
#This is the case for the language test data (lanugage_perceptron.txt)
#the plane is hardcoded to give a good view of the language data,
#and will probably work poorly with an arbitrary set of data

#plotting plane
if (len(w) == 3):
	fig = plt.figure()
	ax = fig.add_subplot(111,projection='3d')
	x1plot = np.arange(0, 1, 0.05)
	x2plot = np.arange(0, 0.1, 0.005)
	x1plot, x2plot = np.meshgrid(x1plot, x2plot)
	zplot = [[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[],[]]
	for i in range(len(x1plot[0])):
		for j in range(len(x1plot[0])):
			a = threshold(w, [1, x1plot[i][j], x2plot[i][j]])
			zplot[i].append(a)
	zVals = [[]]
	xVals = [[]]
	yVals = [[]]
	for i in range(29):
		zVals[0].append(threshold(w,x[i]))
		xVals.append(x[i][1])
		yVals.append(x[i][2])

	surf = ax.plot_surface(x1plot,x2plot, zplot,cmap=cm.coolwarm,linewidth=0, antialiased=False)
	ax.zaxis.set_major_locator(LinearLocator(10))
	ax.zaxis.set_major_formatter(FormatStrFormatter('%.02f'))

#plotting points
	c = 'r'
	m = 'o'
	for i in range(len(y)):
		x1 = [x[i][1]]
		y1 = [x[i][2]]
		if (y[i] == 1):
			#french data points are red
			ax.plot(x1,y1,threshold(w,x[i]),'r',marker=m)
			#english data points are red
		if (y[i] == 0):
			ax.plot(x1,y1,threshold(w,x[i]),'b',marker=m)
	#setting up plot
	ax.set_xlabel('X Label')
	ax.set_ylabel('Y Label')
	ax.set_zlabel('Z Label')
	fig.colorbar(surf, shrink=0.5, aspect=5)
	plt.show()