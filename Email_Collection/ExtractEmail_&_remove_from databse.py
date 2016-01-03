
# coding: utf-8

### Extract Email from deleviry failure mail & remove it from database

# In[14]:

import re
import pprint,sys


# In[2]:

filename = "email1"


# In[3]:

f=open(filename,'r')
data=f.read()
f.close()
print len(data)


# In[4]:

mailRegex = re.compile(r'([\w.]+(@|\(at\))\w+(\.|\(dot\))((\w){2,7}(\.|\(dot\))(\w){2,5}|(\w){2,7}))')


# In[10]:

res=mailRegex.findall(data)
print len(res),"emails found"


# In[11]:

#Removing duplicates & extras
ls=[]
for email in res:
    if email[0] != "daemon@googlemail.com" and email[0] != "sharecoupans@gmail.com":
        if email[0] not in ls:
            ls.append(email[0])
print "total email to remove ",len(ls)


# In[15]:

pprint.pprint(ls)


# In[16]:

print "R you sure to remove"
check=raw_input("y/n")
if check=='n':
    sys.exit()


#### Removing from database

# In[17]:

from pymongo import MongoClient


# In[21]:

client=MongoClient()

db=client.Emaildb
inidata=db.data.count()


# In[19]:

for email in ls:
    try:
        db.data.remove({"email" : email})
    except:
        print "email not found"


# In[22]:

print inidata-db.data.count(),"Documents removed"


# In[ ]:



