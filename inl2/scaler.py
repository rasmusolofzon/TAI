f = open("datasets/french.txt")
data = f.readlines()
french = [[], []]

for line in data:
	french[0].append(line.split()[0])
	french[1].append(line.split()[1])

#print("Max of wordcount:" + max(french[0]))
#print("Max of a-count:" + max(french[1]))

sfx = 1 / int(max(french[0]))
#sfy = 1 / int(max(french[1]))
print("sfx = " + str(sfx))
#print("sfy = " + str(sfy))

counter = 0
for value in french[0]:
	french[0][counter] = float(value) * sfx
	counter = counter+1

counter = 0
for value in french[1]:
	french[1][counter] = float(value) * sfx
	counter = counter+1

fnew = open("datasets/french_scaled.txt", 'w')

for i in range(len(french[0])):
	fnew.write(str(french[0][i]) + " " + str(french[1][i]) + "\n")

fnew.close() 

#fnew = open("french_scaled.txt", 'r')
#print(fnew.readlines())

f = open("datasets/english.txt")
data = f.readlines()
english = [[], []]

for line in data:
	english[0].append(line.split()[0])
	english[1].append(line.split()[1])

#print("Max of wordcount:" + max(french[0]))
#print("Max of a-count:" + max(french[1]))

sfx = 1 / int(max(english[0]))
print("sfx = " + str(sfx))

counter = 0
for value in english[0]:
	english[0][counter] = float(value) * sfx
	counter = counter+1

counter = 0
for value in english[1]:
	english[1][counter] = float(value) * sfx
	counter = counter+1

fnew = open("datasets/english_scaled.txt", 'w')

for i in range(len(english[0])):
	fnew.write(str(english[0][i]) + " " + str(english[1][i]) + "\n")

fnew.close() 