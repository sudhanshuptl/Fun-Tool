
# coding: utf-8

### Creating Email text file with group of 500

# In[2]:


from pymongo import MongoClient
import pymongo

client = MongoClient()
print client

#select or create databse if not available automaticaly create
db = client.Emaildb


# In[3]:

db.data.count()


# In[4]:

res=db.data.find({},{'_id': 0,'email':1} )


# In[5]:

res.count()


# In[6]:

f=open('freecharge','w')
count = 1
data=''
for record in res:
    data=data+', '+record["email"]
    count +=1
    if count %500 ==0:
        count=1
        f.write(data+'\n\n')
        f.write("*"*50+'\n\n')
        data=''
f.close()


# In[7]:

print "Thank You"


# In[ ]:



