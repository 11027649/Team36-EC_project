import os
import matplotlib.pyplot as plt

os.system('javac -cp contest.jar player36.java')

print(" (0) BentCigarFunction \n (1) Schaffers F17 \n (2) Katsuura \n Type your choice: ", end="")
what_function = int(input())

n = int(input("How often do you want to run this function? "))


total = 0
maxi = 0
mini = 10
scoreList = []

for i in range(0,n):
	if what_function == 0:
		title = "BentCigarFunction"
		java_output = os.popen('java -jar testrun.jar -submission=player36 -evaluation=BentCigarFunction -seed=1').read()
	elif what_function == 1:
		title = "Schaffers F17"
		java_output = os.popen('java -jar testrun.jar -submission=player36 -evaluation=SchaffersEvaluation -seed=1').read()
	elif what_function == 2:
		title = "Katsuura"
		java_output = os.popen('java -jar testrun.jar -submission=player36 -evaluation=KatsuuraEvaluation -seed=1').read()
	else:
		print("Dumbass, you couldn't complete the task of typing 0, 1 or 2")
		exit(1);

	score = float(java_output.split('\n')[-3].split(' ')[1])
	scoreList.append(score)
	total = total + score
	print(score)
	if score > maxi:
		maxi = score
	if score < mini:
		mini = score

avg = total/n
print('Average:\t',avg)
print('Maximum:\t',maxi)
print('Minimum:\t',mini)

plt.hist(scoreList, edgecolor='black', linewidth=1.2)
plt.title(title)
plt.xlabel("Score")
plt.ylabel("Frequency")
plt.show()