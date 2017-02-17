import sys
#import matplotlib.pyplot as plt
import numpy as np

####################################################
#functions

def gradientDescent(w0, w1, data, alfa, epsilon):

	oldSSE = SSE(w0,w1,data)
	w0,w1 = gradient(w0,w1,data,alfa)
	a = 0

	while (abs(SSE(w0,w1,data)-oldSSE) > epsilon):
		a+=1
		oldSSE = SSE(w0,w1,data)
		w0, w1 = gradient(w0,w1,data,alfa)
	print("number of iterations: " + str(a))
	return (w0,w1)

#a step in the direction of the gradient
def gradient(w0,w1,data,alfa):
	w0gradient = 0
	w1gradient = 0
	n = len(data[0])

	for i in range(n):
		x = float(values[0][i])
		y = float(values[1][i])
		w0gradient += -((2) * (y - (x * w1 + w0)))
		w1gradient += -((2) * x * (y - (x * w1 + w0)))

	retw0 = w0 - w0gradient*alfa
	retw1 = w1 - w1gradient*alfa
	return (retw0,retw1)

#returns sum of sqared errors
def SSE(w0,w1,values):
	sse = 0
	for i in range(len(values[0])):
		x = float(values[0][i])
		y = float(values[1][i])
		sse += (y - (w1 * x + w0)) ** 2
	return sse
	
#################################################
#the script

fileName = sys.argv[1]
f = open(fileName, "r")
data = f.readlines()
values = [[], []]

for line in data:
	values[0].append(line.split()[0])
	values[1].append(line.split()[1])

w0 = 0
w1 = 0
alfa = 0.01
epsilon = 0.000000000001

W = gradientDescent(w0,w1,values,alfa,epsilon)
print("w0: " + str(W[0]))
print("w1: " + str(W[1]))
"""
#t = np.arange(0, 80000, 1000)
plt.axis([0, 1.1, 0, 0.1])
#points data[0], data[1]
plt.plot(np.array(values[0]), np.array(values[1]), 'o')
plt.plot([0, 1], [W[0], W[1]+W[0]], '-')
"""
"""linewidth=3, color='blue'
#plt.xlabel('# of words')
#plt.ylabel('# of a\'s')
plt.savefig(sys.argv[1][:-4] + ".png")
plt.show()
"""

