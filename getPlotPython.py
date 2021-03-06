import os
import matplotlib.pyplot as plt
import numpy as np

os.system('javac -cp contest.jar player36.java Individual.java Population.java')

print(" (0) BentCigarFunction \n (1) Schaffers F17 \n (2) Katsuura \n Type your choice: ", end="")
what_function = int(input())



total = 0
maxi = 0
mini = 10


for i in range(0,1):
	if what_function == 0:
		java_output = os.popen('java -jar testrun.jar -submission=player36 -evaluation=BentCigarFunction -seed=1').read()
	elif what_function == 1:
		java_output = os.popen('java -jar testrun.jar -submission=player36 -evaluation=SchaffersEvaluation -seed=1').read()
	elif what_function == 2:
		java_output = os.popen('java -jar testrun.jar -submission=player36 -evaluation=KatsuuraEvaluation -seed=1').read()
	else:
		print("Dumbass, you couldn't complete the task of typing 0, 1 or 2")
		exit(1);

	score = float(java_output.split('\n')[-3].split(' ')[1])
	total = total + score
	print(score)
	if score > maxi:
		maxi = score
	if score < mini:
		mini = score

# avg = total/n
# print('Average:\t',avg)
# print('Maximum:\t',maxi)
# print('Minimum:\t',mini)

avg_array = []
best_array = []

for line in java_output.split('\n'):
	if line.startswith("avg:"):
		# avg_array.append(float(line[5:20].strip()))
		# print(float(line.split(' ')[1]))
		avg_array.append(float(line.split(' ')[1]))
	elif line.startswith("best"):
		# best_array.append(float(line[6:20].strip()))
		# best_array.append(float(line[6:20].strip()))
		# print(float(line.split(' ')[1]))
		best_array.append(float(line.split(' ')[1]))


plt.plot(avg_array, color="C1", label="avg")
plt.plot(best_array, color="C0", label="best")
plt.legend()
plt.show()
