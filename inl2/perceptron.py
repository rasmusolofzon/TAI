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
	row = data[i].split()
	for j in range(cols):


		if (j==0):

			m[i,j] = float(row[j])
		else:
			a = row[j]
			a = a[2:]
			
			m[i,j] = float(a)


#assumes positive  values
maxVal = np.amax(m)
print(maxVal)
if (maxVal>1):
	scalefactor = 1/maxVal
	for i in range(rows):
		for j in range(1,cols):
			m[i,j] = m[i][j] * scalefactor
maxVal = np.amax(m)
print(maxVal)
print(m)



#for line in data:
	#values[0].append(line.split()[0])
	#values[1].append(line.split()[1])
#optional - scale data here, or do it preemptively
#check number of rows in file

#input arguments: filename, number of attributes




#matrix = [[][][]]