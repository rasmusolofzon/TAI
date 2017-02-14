import numpy as np 
import sys


#load file

fileName = sys.argv[1]
f = open(fileName, "r")
data = f.readlines()

rows = len(data)
cols = len(data[0].split())

m = np.zeros(shape=(rows,cols))

for i in range(rows):

print(m)


#for line in data:
	#values[0].append(line.split()[0])
	#values[1].append(line.split()[1])
#optional - scale data here, or do it preemptively
#check number of rows in file

#input arguments: filename, number of attributes




#matrix = [[][][]]