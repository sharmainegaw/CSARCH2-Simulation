# 🚀 CSARCH2-Simulation

This application demonstrates a cache simulator that
uses a block set associative (BSA) cache memory mapping function 
and Least Recently Used (LRU) replacement algorithm.

## 💻 How to run the app: ##
1. Please make sure you have a Java version of 1.8.
2. Double click or right click the jar file and then select "Open".
3. Place valid inputs for each of the fields and click `Start Simulation`.
4. Click `Next` in order to see the next step in inserting the data. Click `Skip Simulation` to see the final cache and the cache-related computations immediately.
5. Click `Finish`.
6. A prompt will appear. Clicking the `Save File` button saves the output in a text file located in the folder `Cache Simulation Output` found in the same directory as the application.
7. Another prompt will appear. To simulate another cache, click the `Simulate` button. To exit the program, click the `Exit` button.

## 🛠 Scope and Limitations ##

The following are the inputs:
1. Cache size (in either blocks or words) - accepts integers only
2. Data type of cache size (blocks or words)
3. Main memory size (in either blocks or words) - accepts integers only
4. Data type of main memory size (blocks or words)
5. Cache access time (in ns) - accepts floating point numbers
6. Main memory access time (in ns) - accepts floating point numbers
7. Data separated in commas followed by a space (e.g. `200, 204, 20C`) in blocks or address (hex or decimal)
8. Number of loops - accepts 1 until 10

The application is limited in solving the following problems:
1. The program can only perform single-level loops in the range of 1 to 10 loops. Nested loops aren't included.
2. All of the input fields must be filled out by the user. No random generator in this program.
3. Data can either be Hex (Address) or Decimal (Address or Blocks).

### Sample Screenshots ###