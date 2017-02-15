#On python:

#this is a comment

"""This is a 
multiline comment""" #These need to be indented right!


####################################################
#variable assignment, types, data structures

#variable names are case sensitive, capital letters can be used 


#int
i = 5

#float
f = 26.3

#complex
c = 1 + 4j

#booleans
true_bool = True
false_bool = False


####################################################
#lists
list = [1,2,3,"hello", [1,2],2.0] #this is a list. Lists are mutable
list.append(x)
list.insert(i, x)

#tuples
(1,2,3,"hello", 2.0) #this is a tuple. Tuples are immutable (nice!)

#both lists and tuples can have items of varying type
#they can both have their own type in them (nesting)
#they are ordered [0][1] etc.

####################################################
#strings
"this is a string"
'this is also a string' #equal, no char type


s = 'the horse's ass' 
#fungerar inte pga tror ' avslutar sträng
s = 'the horse\'s ass' #fungerar. "\" används som escape character

#strängar är indexerade på samma sätt som i Java
c = "cats"[0] #the variable c gets the value "c"

#substrings är sjukt smidiga:
print(c[1:]) => 'ats'
print(c[:3]) => 'cat'
print(c[:-2]) => 'ts'
print(c[1:-1]) => 'at'

#string functions:
 	
####################################################
#mathematical operations

#addition
 a = 1 + 2 #or
 a=1+2 #no difference with white-spaces

#subtraction
b = a-2

#multiplication
c = 4*6
 
#division (obs python 3)
a = 5/2  #gives float answer
2.5

a = 25//4 #gives integer division
6

a = 25%4 #modulus, like in Java
1

#exponents / power off
 a = 2**10  #this gives 2 to the power of 10 (2^10)

#floor division
a = 9//2 #this gives a = 4

#comparisons exactly like Java
<,>,<=,>=,!=,==

#logic operators
and, or, not 

#####################################################
#import commands
#imports an entire module, or a specific function

>>> from math import * 
>>> from math import sqrt

####################################################
#multi-line commands in interpreter
>>> for n in [1,2,3,4,5,6]:  #the ":" tells the interpreter that we want to type a multiline command
 ... print n**2 
 ... 1
  4
  9
  16
  25 
  36

####################################################
#functions

def functionName(functionArguments):
	#doStuff
	return value

def square(x):
	return x*x

####################################################
#file handling
f = open("filename.txt", 'r')
	#'r' for read mode (is default, no 2nd parameter means 'r')
	#'w' for write mode
	#'r+'' for both read and write (might not work?)
	#'a' for append mode

f.read(size) 	#returns size, in either string or bytes
				#(size < 0 or size = null) => whole file is read (even if larger than machine's memory)
				#(size >= 0) => size bytes are returned

####################################################

####################################################

####################################################
#lambdas - function as parameter
#example:
def filter(whatToFilter, list):
#anrop:
filter(lambda x: x % 2 == 0, [1, 2, 3, 4, 5, 6]) #ger jämna tal

####################################################
#CLI arguments
import sys

sys.argv #is the list of CLI args
len(sys.argv) #is the number of CLI args

####################################################
#reserved words (keywords)
and, def, del, for, is, raise, assert, elif, from, lambda, return, break, else, global, not, try, class, except, if, or, while, continue, exec, import, pass, yield. 

####################################################


#variable types are declared implicitly

#variable types are decided at runtime.
#This means a variabe assigned to an object can be reassigned to another object of a different type
#This wouldnt compile in Java, but does in python

#the "Python interpreter" is what you use when running python directly via the command line (also in IDLE)
#type a command in after the COMMAND PROMPT, get answer/result of command

# this is the python prompt: >>> *write command here*

#type help(object/variable) for help on that object
#this is basically "javadoc" for that object

#to execute a python file, type "python fileName.py" (no compilation needed. this is like javac and java in one)