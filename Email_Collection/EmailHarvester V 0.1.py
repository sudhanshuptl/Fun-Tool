
# coding: utf-8

# In[212]:

import xlrd,sys
import pprint
from datetime import datetime


# In[2]:

filename= "NIT Rourkela LOI Not Received List.xls"


# In[4]:

#open file
xl_workbook = xlrd.open_workbook(filename)


# In[5]:

#sheet names
sheet_names=xl_workbook.sheet_names()
print "sheet Names :",sheet_names
print "Right now we gona deel with single sheet only"


# In[7]:

#select sheet
workingSheet=xl_workbook.sheet_by_index(0)


# In[199]:

#extracting first row {it contain header}
row=workingSheet.row(0)


# In[200]:

#selecting field of our need
###
# Standerd column names
# u'name', u'email',u'gender',mobile,phone',College Name

###
slected_field=dict()
for i in range(workingSheet.ncols):
    if row[i].value.lower() == u'name':
        slected_field[row[i].value.lower()] = i
    elif row[i].value.lower() == u'email':
        slected_field[row[i].value.lower()] = i
    elif row[i].value.lower() == u'gender':
        slected_field[row[i].value.lower()] = i
    elif row[i].value.lower().find('mobile') != -1 or row[i].value.lower().find('phone') !=-1:
        slected_field['phone'] = i
    elif row[i].value == u'College Name':
        slected_field['org'] = i

print 'selected field for update'    
pprint.pprint(slected_field)


# In[202]:

#create list of document to insert
data=[]
for row in range(1,workingSheet.nrows):
    temp={}
    for key in slected_field:
        if key=='org':
            if workingSheet.cell(row,slected_field[key]).value.find('NIT Rourkela') !=-1:
                temp[key]='NITR'
            else:
                temp[key]=workingSheet.cell(row,slected_field[key]).value
        if key=='phone':
            temp[key]=int(workingSheet.cell(row,slected_field[key]).value)
                
        else:
            temp[key]=workingSheet.cell(row,slected_field[key]).value
        temp['timestamp']=datetime.utcnow()
    data.append(temp)
for i in range(3):
    pprint.pprint(data[i])


# In[214]:

print 'Type, Y/y to continue Insertion & N/n  to exit:'
checkpoint=raw_input()
if checkpoint.lower() !='y':
    print 'Bye Bye'
    sys.exit()


#### Database Insertion Updation 

# In[82]:


from pymongo import MongoClient
import pymongo

client = MongoClient()
print client

#select or create databse if not available automaticaly create
db = client.Emaildb


# In[84]:

initialStatus=db.data.count() #before insertion


# In[205]:

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
            print "applied update"
        except:
            print "error during updation,"
            print dcopy[i]
            
    except:
        print "error in,insertion"
        print data[i]
print "Insertion:",insertion
print "updation:",updation
print "New total No of Document",db.data.count() 
print "New document added :",db.data.count() -initialStatus


# In[206]:

#pprint.pprint(db.data.find_one({'email':data[14]['email']}))


# In[215]:

print 'Current no of documents =',db.data.count()
print 'Initial state was',initialStatus,' Documents'
print 'thank you'


# In[ ]:



