Activity Tinder - README
===

Activity Tinder

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)
3. [Todo](#Todo)

## Overview
### Description
Have you ever wanted to hang out with a group of friends, but have no idea what do to? Do you have no friends, but want to do something?

This app will help individuals meet people that they want to hang out or do specific activities with. It will allow them to specify what activity they want to do and find a group or join a group that already plans on doing that activity.

### [App Evaluation](https://courses.codepath.com/courses/android_university_fast_track/pages/app_brainstorming_guide)
[Evaluation of your app across the following attributes]
- **Category:**
    - social, meeting people irl (literally Tinder for friends)
- **Mobile:**
    - it's very convenient on the phone
    - most popular messenger apps are all for mobile
    - good for traveling
    - FB Messenger
    - Google Maps
- **Story:**
    - It orginally started off as solving the FB BFF issue, but quickly expaneded to being able to be used in other enterprises
    - can easily expand to general tech consumers who want to get out and do stuff and meet new people
    - Tinder for people who don't want to go out on dates, but want to have a (platonic) good time
- **Market:**
    - Facebook employees (initially)
    - The general public looking to have a platonic good time
- **Habit:**
    - Aveage user can create an event twice or three times a month
        - But coming back to the app to plan the event would be customized based on when the user joined the activity group and how frequently the gorup corresponds
    - user can decide how often they want to go out
    - if they already have a group that they just want to easily plan outings with, they can visit often whenever the group wnts to go out
- **Scope:**
    - it hopefully can be done
    - biggest issue seems to be what to add as required stories
        - could easily expand and lose focus on the main core of the app


## Product Spec

### 1. User Stories (Required and Optional)

**\*Updated\* Required Must-have Stories**
* App Icon
* Login/Signup screen
* Location permissions page
* Main card stack page with activites on the cards
    * swipe left and right depending on whether you're interested in activity or not
* Activity detail page
* Activity checkout page
* Contact us page
* Profile page

**\*Updated\* Stretch Stories**
* separate sign-up page
* location page that asks for city location
* group chat option
* color categorized cards for the activities depending on the type of activity
* organizer can compose a message to be displayed to whoever tries to join the event
*  
* type in password to confirm leaving activity
    * warning for user before leaving activity under 24 hours before activity
* detailed card view features:
    * listing of people who are planning on attending already
    * default images for types of activities
* profile page features:
    * can see person's past events and photos they took there
* cute sound and animation of adding to an event
    * toast
* export event details to calendar
* transfer ownership of events
* checking into event (attendance)


**Optional Nice-to-have Stories**

* [fill in your required user stories here]
* Help page where it describes what the app does
* Filter option where you can choose location, price, number of people, etc.
* After-event survey where you are able to rate the people who went
    * You can add pictures of the event to the survey
* Optional invite-only events
* Messenger link in profile picture

### 2. Screen Archetypes
https://guides.codepath.org/android/Mobile-Screen-Archetypes
* Login page/register
   * Login page
* Card swiping
   * Swiping for Activities
* creation page
    * allows users to create events  
* Profile Page
    * Where it says the event that you went to 
    * Rating - where you receive stars based on your performance
* Contact us

# 3. Navigation

**Tab Navigation** (Tab to Screen)
Navbar on the bottom of the screen for switching between activities
* Home tab
* Create event
* Profile page

**Flow Navigation** (Screen to Screen)

* Home card deck with activities
   * click on specific card to see that activity's details in a separate activity
   * ...
* Profile
   * click on settings button to access settings
   * ...

## Wireframes
![Flowchart](ActivityTinder.png)

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]

##	Todo before App Kickoff
- find scope of project
    - break up into features
    - try to cut features into what it needs
- need tasks and goals for every individual person
- want goals per week, per person
    - make the workflow modular
    - avoid blockers/dependencies
