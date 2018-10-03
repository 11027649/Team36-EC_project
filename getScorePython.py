import os

os.system('javac -cp contest.jar player36.java')

n = 300
total = 0
maxi = 0
mini = 10

for i in range(0,n):
	java_output = os.popen('java -jar testrun.jar -submission=player36 -evaluation=BentCigarFunction -seed=1').read()
	score = float(java_output.split('\n')[-3].split(' ')[1])
	total = total + score
	if score > maxi:
		maxi = score
	if score < mini:
		mini = score

avg = total/n
print 'Average:\t',avg
print 'Maximum:\t',maxi
print 'Minimum:\t',mini