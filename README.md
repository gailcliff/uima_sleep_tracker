Brian Sun, bsun28
Clifford Odhiambo, codhiam3


Questions:

1. How did you decide to handle invalid undo attempts and why?
2. If you customize and enhance the aesthetics of the user interface according to your taste (and best practices, of course), explain and justify any variations to the original design that you made. Note that the functionality of each element, activity and navigation must not be altered.
3. Does it make sense to have multiple activities for this simple version of the app? Are there any design changes would you suggest with respect to the purpose of each activity and navigation between views?
4. How did you test whether the app displayed the correct results for various time inputs?
5. What was most challenging or frustrating in doing this assignment?


Responses:

1. After the user clicks the undo button once, the undo button becomes disabled and it appears translucent (its visibility is faded to give a visual cue that it's not clickable).
If the user keeps clicking it, nothing happens. The daily sleep average stays the same and will only change when the user enters another sleep amount. 
We did this because the user is only allowed to use the undo button once since the app is launched according to the requirements. 
Also, this is the most clear way of letting the user know that they can’t keep undoing sleep times from several days in the past.

2. Our user interface is similar to the staff design’s because we believe it is soothing to look at and intuitive to use. The color contrast is clear while 
still being easy on the eye so people won’t feel bothered when they open the app in the morning or before going to sleep.

3. Yes, having multiple activities makes a lot of sense because a good app design separates different features to different pages in order to keep things organized 
and intuitive. The main sleep tracker page has the most important functionality of the app (to calculate sleep time based on input start and end times) and also 
displays the running average. It has very neatly placed “undo”, and “submit” buttons whose purposes are intuitive. The settings button smoothly navigates the user 
to the settings page where they can set a sleep minimum. If all of these functionalities were located on one view, the user would be overwhelmed and struggle to 
benefit from using this app. We don’t think there are any design changes that would enhance this particular version of this app.

4. We input different sleep start times and different sleep end times of different cases to check whether the app correctly calculated the Sleep Time. 
There were 2 general cases that we tested several times to ensure the app was functioning properly. The first case is when the start and end times were in the same 
“half” of the day (ex. 3 PM to 10 PM). The second case is when the start and end times were in different “halves” of the day (ex. 9 PM to 6 AM). After inputting 
random times of both cases many times, we were sure that our app correctly calculated the correct results for various time inputs.


5. Implementing shared preferences was a challenging part of this assignment. We created a history to track the user’s daily sleep inputs and use this to calculate 
their daily sleep average. However, when the user closes the application, the daily sleep average would reset and we would also lose how many times they submitted 
sleep times, so the only way to solve this issue is to use shared preferences and keep updating the history every time it gets altered when the user adds a new entry, 
which was the most difficult part of the assignment to implement.
