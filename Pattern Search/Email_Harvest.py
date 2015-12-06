
__author__='Sudhanshu Patel'

import re


#crating regular Expression for give sample
regex = re.compile(r'<td>(.+?) &lt\;(.+\(at\).+\(dot\).+)&gt\;</td>')


#open file
file=open('email_sample.html','r')



email=[]
for line in file:
    m=regex.search(line)
    try:
        email.append((m.group(1),m.group(2)))
    except:
        pass
print 'Total Email Extracted: ',len(email)
print 'sample Email :',email[0]


# In[21]:

replaceDot = re.compile(r'\(dot\)')
replaceAt = re.compile(r'\(at\)')

PureEmail_data=[]
for rec in email:
    temp = replaceDot.sub('.',rec[1])
    PureEmail_data.append((rec[0],replaceAt.sub('@',temp)))

print "sample result :",PureEmail_data



print 'Total Harvested Email :',len(PureEmail_data)
del email




