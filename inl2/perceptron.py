import numpy as np 
import sys

def threshold(w, x):
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
		wUpdated[i] = w[i] + alfa*(y-threshold(w, x))*x[i]
	return wUpdated

def shuffleTwo(x, y):
	savedSeed = np.random.randint(0, 200000)
	np.random.seed(savedSeed)
	np.random.shuffle(x)
	np.random.seed(savedSeed)
	np.random.shuffle(y)
	return (x, y)

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
if (maxVal>1):
	scalefactor = 1/maxVal
	for i in range(rows):
		for j in range(1,cols):
			x[i,j] = x[i][j] * scalefactor
maxVal = np.amax(x)

w = [0 for i in range(cols)]
w[0] = 1

alfa = 1
x, y = shuffleTwo(x, y)

indexSet = 0
stopcondition = False
nbrMisclassified = 0
epochs = 0
wSaved = []
print("stopcondition is " + str(int(rows*0.05)))

while not stopcondition:
	w = perceptronLearningRule(w, x[indexSet], y[indexSet], alfa)
	if (threshold(w, x[indexSet]) != y[indexSet]):
		nbrMisclassified+=1
	alfa = 1000 / (1000+(indexSet+epochs*rows))
	indexSet+=1

	if indexSet%rows==0 and indexSet!=0:
		print (str(nbrMisclassified) + " wrong in epoch " + str(epochs))
		indexSet = 0
		shuffleTwo(x, y)
		print(alfa, w)
		if (nbrMisclassified <= int(rows*0.05)):
			stopcondition = True
			print("Success!")
		nbrMisclassified = 0
		epochs+=1


print(w)