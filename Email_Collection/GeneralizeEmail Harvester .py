
# coding: utf-8

## Generalis Emails Harvester, from files

##### Using Regular Expression

# In[1]:

import re


# In[108]:

filename=raw_input("Enter file Name to extract emails : ")


# In[2]:

#reading experiment file
f=open(filename,'r')
data=f.read()
f.close()
print len(data)


# In[116]:

mailRegex = re.compile(r'([\w.]+(@|\(at\))\w+(\.|\(dot\))((\w){2,7}(\.|\(dot\))(\w){2,5}|(\w){2,7}))')

# "" [\w.]+ "" more than one alphanumaric, underscore or dot
# "" (@|\(at\)) "" either one of @ or (at)
# "" \w+(\.|\(dot\)) "" any numner of alphanumeric enad with "." or (dot)
# "" ((\w){2,7}(\.|\(dot\))(\w){2,5}|(\w){2,7}) "",pipeline for selecting email of type pqr@gmail.com or xyz@nitrkl.ac.in


# In[117]:

res=mailRegex.findall(data)


# In[118]:

#copying email in list
emails=[x[0] for x in res]
del res
print 'total, email',len(emails)
emails[6:12]


# In[119]:

#replacing all (at) by @ and (dot) by '.'

replaceDot = re.compile(r'\(dot\)')
replaceAt = re.compile(r'\(at\)')

for i in range(len(emails)):
    emails[i] = replaceDot.sub('.',emails[i])
    emails[i] = replaceAt.sub('@',emails[i])


# In[121]:

print emails[1:12]


# In[ ]:



