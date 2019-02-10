# fastToDo 
**(android navive application)**<br/>
The app’s main model is the class Todo. Todos are store in sqlite lite database.<br/>
The library Room is used for managing the CRUD actions.<br/>
This application uses a **listView** to display the todos. <br/>
The listView is rendered by a customized adapter class and the row has a custom layout. <br/>
Each Todo set a reminder created by the AlarmManager class, when the timer is over, Fast ToDo builds a notification via the NotificationManager class.
