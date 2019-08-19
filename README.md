# transaction-tracker
Java / JavaFX app with GUI to track finances and stock market in Runescape (video game)

The tool bar at the top of the screen allows the User to select items based on grouping and quickly switch between different screens through provided buttons. It also is there the Menu is available through the 'File' button

## File --> Menu
IMAGE0

The menu allows the user to add new accounts to track, new categories to group items under, and new items themselves to track. They simply enter the name of the item and the app will scrape multiple RuneScape websites to gather historical pricing information on the object. Here the user can also save all new updates that have been made so as to keep a history of all current and finished transactions. 

## Home Screen
![image1](https://github.com/bmaltbie/transaction-tracker/blob/master/images/home.png)

The home screen shows what Active Offers are currently open. These are orders that have been sent into the market but are currently still open (haven't been completed). The bought checkbox allows the user to quickly enter the price the item was bought or sold at and then check the box, thus archiving the transaction. 

The Watchlist table on the right allows the user to monitor which items are currently at the buy limit (you can only buy a certain number of items per hour) and how much longer until the user it able to buy the item again. 

## New Buy
![image2](https://github.com/bmaltbie/transaction-tracker/blob/master/images/newbuy.png)

The new buy screen shows historical pricing information about the item selected in the tool bar. The buy limit, price, 30/90/180 day trends and 180 day highs/lows are scraped from RuneScape websites live so that the information is as up to date as possible. The Market Graph is constructed using historical information also scraped live so as to have the most accurate sense of what the price trends have been like. 

There are two buttons 'RS Wiki' and 'Grand Exchange' which will open those specific web pages for the selected item in a pop-up window. This allows the user to find more specific, detailed information easily without having to search for it manually. 

The Recent History table shows which transactions the user, in that particular account, has made so they can see what prices they have been buying and selling the item at. 

The collective of this information allows the user to make the most informed decisions on whether to buy or sell an item and at what price.

## Quick Buy
![image3](https://github.com/bmaltbie/transaction-tracker/blob/master/images/quickbuy.png)

The quick buy screen allows users, if they don't want to see all the information on the New Buy screen, to enter multiple orders quickly. This is essentially an advanced string parser that parses abbreviated information the user types so as to record a transaction. The screen shows in real-time what the user is entering and how it will be entered into the system. It also has error checking so the user can easily see if they made a mistake. Quantity is automatically set to 1 unless specified and the date is automatically set to today's date unless specified.

## History
![image4](https://github.com/bmaltbie/transaction-tracker/blob/master/images/history.png)

The history screen shows all completed transactions that have been archived. Here it records the profit from each transaction and the cumulative profit for the specified time range. It also has the feature for users to search for transactions of a specific item to see what transactions have been made and what profits have been made for the specific item. 
