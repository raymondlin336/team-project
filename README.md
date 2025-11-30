# Team Project

Please keep this up-to-date with information about your project throughout the term.

The readme should include information such as:
- a summary of what your application is all about
- a list of the user stories, along with who is responsible for each one
- information about the API(s) that your project uses 
- screenshots or animations demonstrating current functionality

By keeping this README up-to-date,
your team will find it easier to prepare for the final presentation
at the end of the term.


## Project Summary

Our app, 'Habit Tracker', is an application that allows a user to keep track of tasks they want to get done on a regular basis.

Each 'Habit' has a name and description, and repeats on a daily, weekly, or monthly basis. The user can also create habits that do not repeat.

Each 



### Mini Wordle:
#### General idea: 3 main panels of UI
- Log in and log out page
- Daily wordle page
- Personal progress page
#### API usage:
- Word pool
- Account info storage
- Just a simple wordle game with a 4x4 or 5x5 matrix of buttons, can be broken down into the UI, the backend, and other segments for a more spread-out workload.
- Optional steps: more visually appealing workload, adding and approving friends, making your results sharable


### Weather-Based Activity Planner - Kevin: 
#### General Idea:  
- User Authentication: Log in and log out functionality 
- User Preferences Profile:  Add flags such as activity interests (indoor/outdoor, sports, arts, dining) Weather sensitivities (temperature preferences, rain tolerance) Location preferences   
- Smart Activity Suggestions: Recommends activities based on current/forecasted weather conditions Activity 
- Feed: Browse suggested activities with weather compatibility scores

#### API Usage:
- OpenWeatherMap API or WeatherAPI.com: Get current weather and forecasts
- Yelp Fusion API or Google Places API: Discover local activities, restaurants, events
- Ticketmaster API: Find indoor events (concerts, shows) for bad weather days
- Optional: OpenAI API: Generate personalized activity descriptions based on weather + user preferences


## User Stories (Draft)
### Mini Wordle

- As a visitor, I want to create an account and log in so my guesses and streaks are saved.
- As a player, I want to log out so I can secure my account on shared devices.
- As a player, I want a daily Wordle board (4×4 or 5×5) so I can play one new puzzle each day.
- As a player, I want on-board feedback (correct place, wrong place, not in word) so I can adjust my next guess.
- As a player, I want a keyboard/grid of buttons so I can input guesses without typing errors.
- As a player, I want to see remaining guesses and attempt number so I know how much room I have left.
- As a player, I want to view a personal progress page (streaks, win rate, average guesses) so I can track improvement over time.
- As a system, I want to draw the daily word from a server word pool so puzzles are consistent across users and days.
- As a system, I want to store account info and results via the backend so user data persists across sessions.
- As a player, I want to see which days I’ve completed on a calendar so I can keep a streak.

### Weather-Based Activity Planner – Kevin

- As a visitor, I want to sign up and log in so my preferences and history are saved.
- As a user, I want to edit a preferences profile (indoor/outdoor, sports, arts, dining) so suggestions match my interests.
- As a user, I want to set weather sensitivities (temperature range, wind, rain tolerance) so the planner adapts to my comfort.
- As a user, I want to set a home location and travel radius so activities are nearby.
- As a user, I want to see current conditions and a 7-day forecast so I can plan now and later.
- As a user, I want a suggestions feed ranked by “weather compatibility score” so I quickly spot the best options today.
- As a user, I want to filter suggestions by time window (tonight, weekend, next 48h) so I can plan around my schedule.
- As a system, I want to fetch weather from OpenWeatherMap/WeatherAPI so recommendations reflect real conditions.
- As a system, I want to fetch activities/venues from Yelp Fusion/Google Places so results include reputable local options.
- As a system, I want to fetch events from Ticketmaster for indoor days so users have rain-proof alternatives.
  
Maybe?:
- As a user, I want to save and categorize favorites (want to try / been there) so I can build a personalized list.
- As a user, I want to dismiss or mute places so future suggestions improve.
- As a user, I want to view details (hours, price level, distance, basic reviews) so I can decide quickly.
- As a user, I want notifications when weather shifts affect my plans so I can swap activities in time -> has to have live updates, might be hard to implement

## API Information

## Current progress and functionality
