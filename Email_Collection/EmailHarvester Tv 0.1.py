
# coding: utf-8

##### write emails from text file to database

# In[21]:

import pprint,sys
import re
from datetime import datetime


# In[15]:

#open file
fl=open("csGeek",'r')
flData=fl.read()
fl.close()


##### Write now it uses direct exraction But we need to add reguler expression search for emails in text files

# In[17]:

ls=flData.split(',\n')


# In[19]:

#testing
ls[:3]


# In[24]:

data=[]
for email in ls:
    temp=dict()
    temp['email'] = email
    temp['timestamp']=datetime.utcnow()
    data.append(temp)

pprint.pprint(data[:3])


# In[23]:

#checkPoint
print 'Type, Y/y to continue Insertion & N/n  to exit:'
checkpoint=raw_input()
if checkpoint.lower() !='y':
    print 'Bye Bye'
    sys.exit()


### Database insert or Update

# In[25]:

from pymongo import MongoClient
import pymongo

client = MongoClient()
print client

#select or create databse if not available automaticaly create
db = client.Emaildb


# In[27]:

initialStatus=db.data.count() #before insertion
print initialStatus


# In[57]:

#here alise create huge issue becouse if match found it update the list so its create error in updating
insertion=0
updation=0
for i in range(len(data)):
    try:
        db.data.insert(data[i])
        insertion +=1
    except pymongo.errors.DuplicateKeyError:
        try:
            del data[i]['_id']
            db.data.update({'email':data[i]['email']},{"$set" : data[i]})
            updation+=1
        except:
            print "error during updation,"
            print data[i]
            
    except:
        print "error in,insertion"
        print data[i]
print "Insertion:",insertion
print "updation:",updation
print "New total No of Document",db.data.count() 
print "New document added :",db.data.count() -initialStatus


# In[62]:

print 'Initial no of Documents =',initialStatus
print 'Current no of documents =',db.data.count()
print 'thank you'


# In[ ]:



