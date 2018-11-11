#Kaska app similar to instagram with firebase

# How to use this app
1. Register to [firebase](https://firebase.google.com/) and create firebase application
2. Add android application with the package name `com.alexbezhan.instagram`
3. Download `google-services.json` and add it to inside `app` folder
4. Run the app

### Endpoint for get UserList and PostList
Post Method.
For get UserList you should send query(search will be carried out in the 'username,name,bio,email') to 'https://us-central1-kgkaska.cloudfunctions.net/getUserList'.
Acceptable parameters: Json, attribute => params

Post Method.
And get PostList you should send query(search will be carried out in the 'tag') to 'https://us-central1-kgkaska.cloudfunctions.net/getPostListByTag'.
Acceptable parameters: Json, attribute => params
