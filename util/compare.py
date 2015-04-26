# This is a file that compares predictions against actual results
# Exists to give an idea on how the classifier is doing

import sys

if len(sys.argv) != 3:
	print('Input 2 files: the output and the actual results')
	sys.exit()

output = open(sys.argv[1], 'r')
actual = open(sys.argv[2], 'r')

o_line = output.readline()
a_line = actual.readline()

correct_count = 0
total_count = 0

while o_line != '' and a_line != '':
	total_count += 1
	word_list = a_line.split()
	res = word_list[len(word_list)-1].strip()
	print o_line.strip()
	if o_line.strip() == res:
		correct_count += 1
	o_line = output.readline()
	a_line = actual.readline()

print('Number Correct: ' + str(correct_count))
print('Number Anaylzed: ' + str(total_count))
print('Percent Correct: ' + str(float(correct_count)/float(total_count)))