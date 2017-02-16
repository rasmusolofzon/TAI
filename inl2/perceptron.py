import numpy as np 
import sys

def treshold(w, x):
	z = 0
	for i in range(len(w)):
		z += w[i] * x[i]
	if z >= 0:
		return 1
	if z < 0:
		return 0

def perceptronLearningRule(w, x, y, alfa):
	wUpdated = [0 for i in range(len(w))]
	for i in range(len(w)):
		wUpdated[i] = w[i] + alfa*(y-treshold(w, x))*x[i]
	return wUpdated

def shuffleTwo(x, y):
	savedSeed = np.random.randint(0, 10000)
	np.random.seed(savedSeed)
	np.random.shuffle(x)
	np.random.seed(savedSeed)
	np.random.shuffle(y)
	return (x, y)

"""TODO
	implementera 'minsta felklassificerade exempel'-funktionaliteten aka stopcondition
	dela upp ett dataset i flera och använd som träningsset och valideringsset
	del 3
"""

#load file
fileName = sys.argv[1]
f = open(fileName, "r")
data = f.readlines()
rows = len(data)
cols = len(data[0].split())

x = np.zeros(shape=(rows,cols))
y = [0] * rows #np.zeros(shape=(rows,1))

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

#assumes positive  values
maxVal = np.amax(x)
print(maxVal)
if (maxVal>1):
	scalefactor = 1/maxVal
	for i in range(rows):
		for j in range(1,cols):
			x[i,j] = x[i][j] * scalefactor
maxVal = np.amax(x)
print(maxVal)
print(x)
print(y)


w = [0 for i in range(cols)]
w[0] = 1
print(w)

alfa = 1
x, y = shuffleTwo(x, y)
print(x)
print(y)

for i in range(rows):
	w = perceptronLearningRule(w, x[i], y[i], alfa)
	alfa = 1000 / (1000+i)

"""while :
	w = perceptronLearningRule(w, x[i], y[i], alfa)
	alfa = 1000 / (1000+i)
"""
print(w)