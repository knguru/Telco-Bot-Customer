"# Bot" 
# Bank Bot is based on the Conversation Enhanced Sample Application
 the app connects to a DashDB instance to retrive data pertaining to transactions
 nosqljson.jar,db2jcc_license_cu.jar, db2jcc4.jar need to be present in /locallib dir.
 Instances of below services need to be created in the space where the will be deployed and the corresponding name should be updated in the manifest.yml file
  
  - conversation service
  - dashDB
  - retrieve-and-rank-service

#To build the application you need to be in the root dir of this app and do
gradle.bat build

3df8aee0-345e-4082-80bc-417685a31266
#To push on bluemix, do a
cf push


<a name="env">
# Adding environment variables in Bluemix
</a>

1 In Bluemix, open the application from the Dashboard. Select **Environment Variables**.

2 Select **USER-DEFINED**.

3 Select **ADD**.

4 Add a variable with the name **WORKSPACE_ID**. For the value, paste in the Workspace ID (32 char ID of the workspace created in the conversation service). Select **SAVE**.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;![](readme_images/env.PNG)

5 Restart your application.

# about the application
the bot is training for conversation regarding transactions and Loans. You can ask questions such as 'Get me 3 Card transactions' 'what is my Auto EMI amount'. 'Is it a holiday day after tomorrow'

