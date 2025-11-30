# Team Project

## Project Summary

"Habit Tracker" is an application that allows a user to keep track of tasks they want to get done on a regular basis. You can add, edit and delete habits and tasks to organize and remind yourself of things you need to do.

Features:

- Adding habits, including a name, description, and how often they repeat

<img src="https://github.com/user-attachments/assets/ca038829-592b-450d-8db6-8651abd66d29" width="400">

- Editing information of a habit in case it changes over time
  
- Saving your habits locally, and you can see the habits you made whenever you launch the app again.

- A convenient home page to see the tasks due this particular day / week / month

<img src="https://github.com/user-attachments/assets/12060f16-8c42-4bde-8af2-c564c2f35276" width="400">

- Visualizing the progress you've made as you complete your tasks and follow your habits

<img src="https://github.com/user-attachments/assets/75fdc05b-7ebe-48bf-8c46-a742531f3da3" width="400">

- View a motivating quote each time you launch the app!

<img src="https://github.com/user-attachments/assets/383bb211-03c1-4004-ab96-41f1d3adbfc6" width="400">

## Launching the App

- Clone this repository to your local IDE

- Run src/main/java/AppBuilder.java, and the user interface should show up on your screen.

## User Stories

Kevin: As a user, I want to create and write detailed information regarding my daily habits.

Raymond: As a user, I want to view visual analytics of my habit so that I can understand my progress over time. 

Sumaid: As a user, I want to be able to complete and modify my habits after I create them in case my habits slightly change.

Henry: As a user, I want to be able to save the habits I create and see them whenever I relaunch the app later.

Farrell: As a user, I want to be able to see my habits at a glance depending on their frequency.

Farrell: As a user, I want to read motivational quotes to help me stay on top of my tasks.

## API Information

- We use ZenQuotes to periodically fetch motivational quotes by famous people each time the app is launched. We just called the API and got some strings in JSON format to display in a window.

## Current progress and functionality

Latest implemented features:

- prepareFailedView implemented for editing and adding new habits, telling the user what to change to resolve them (if possible)

- App builder finalized, placeholder files deprecated

- Homepage fixed so that it only shows upcoming tasks (habits)

- Test cases achieved 100% code coverage

- File structure reorganized, code clean ups made

## Future Features

- Daily habit reminders/notifications.

- Google Calendar sync to schedule habit time blocks.

- Social challenges and friend competitions to make habits more engaging.
