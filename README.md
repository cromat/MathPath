# Welcome to MathPath!

MathPath is simple math equation generating Android application/game made for elementary school students. Math solving was never simpler and more entertaining.


# About application

MathPath is fully offline application developed with "Keep it simple" methodology. No registration or any complications are needed to start solving tasks. On the start screen you can only change game configurations. When you hit the "start" button, you are proceeded to solving screen where you need to input right number to solve the given equation. At the end, the results screen is displayed.


# Technical stuff

MathPath is developed in Kotlin. SQLite with anko-sqlite library is used for backend (local) and MVEL library is used for equation strings evaluation.

# Todo

* An algorithm that will generate or repeat tasks based on former user solutions:
	User solutions will be saved to database and application will generate equations based on operators or numbers that user 	tends to solve harder.
	Also, if user gives wrong answer, application will provide hints or give same equation but with changed input field eg. if user gives wrong answer on 2 + 5 = _ , next equation could be 2 + _ = 7.
* ~~More game options like parentheses and random input field placing (currently it is only behind = character)~~
* ~~Visual changes (game icon, in-game icons like graph chart, child friendly graphs and fonts)~~
* ~~Checkbox for possible negative results~~
* ~~Check for operators order in more complex equations (give hints next time)~~
* Measure time for each task and show that info in answers list at the end
* ~~Multilanguage support (Croatian, German, French, Spanish)~~
* Third game option: PickGame with picking on of possible answers
* Game type where will be all game type mixed
* ~~Short game instructions~~
* Better UX:
	* ~~some kind of in-game awarding to keep users active:
		one of ideas is to include a pet (something like tamagochi) and if user solves enougl equations, he will be able 		to feed or buy something for their pet.~~
	* ~~more useful statistics like solving stats by operators~~
	* achievements like 7 active days in row
	* personalization (application graphics will depend on gender)
	* ~~add fun music~~
	* ~~animations and transitions~~
	* ~~colon as dividing operator~~
	* pet simple chatbot (IRIS)
	* ~~some in-game instructions (for difficulties)~~
	* ~~each difficulty gives different amount of money~~
	* ~~pet hapiness bar~~


# Application flowchart

![alt text](https://image.ibb.co/gjWy9c/Selection_068.png)


# Screenshots

<img src="https://github.com/cromat/MathPath/blob/master/screenshots/Screenshot_20180605-223800.png" width="350">
<img src="https://github.com/cromat/MathPath/blob/master/screenshots/Screenshot_20180605-223827.png" width="500">
<img src="https://github.com/cromat/MathPath/blob/master/screenshots/Screenshot_20180605-224158.png" width="350">
<img src="https://github.com/cromat/MathPath/blob/master/screenshots/Screenshot_20180605-224144.png" width="350">
<img src="https://github.com/cromat/MathPath/blob/master/screenshots/Screenshot_20180605-223900.png" width="500">

If you have any remarks or suggestions to improve the app, please feel free to contact me at brabec.tomislav@gmail.com
