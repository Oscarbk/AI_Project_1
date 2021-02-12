# AI_Project_1
## A comparison between uninformed search and informed search using Dijkstra's algorithm and the A* algorithm

### How to run script:
In the project folder, run “javac -cp lib\* src/Search.java” to compile.
In the project folder, run “java -cp lib\* src/Search.java” to run.
You’ll be asked for one of three inputs: 1, 2, or E.
	Select 1 to time runtime of Dijkstra and A* between two user-input nodes.
	Select 2 to time runtime of Dijkstra and A* between all nodes.
	Select 3 to exit program.

### Write-up:
The uninformed search I implemented was Dijkstra’s algorithm and for informed search, I implemented A* with landmarks as its heuristics. Landmarks are vertices which have their distances to all other nodes precomputed. Good landmarks to select are those that appear before the start vertex or after the end vertex. Therefore, for a 10x10 grid, I found the best landmarks to select were those located in each corner of the grid (vertices located around squareID 0, 9, 90, 99), which gives A* on average about a 13.69% decrease in runtime when compared to Dijkstra.

I used the [JGraphT Java library](https://jgrapht.org/) with landmark selection based on the following [paper](https://www.microsoft.com/en-us/research/wp-content/uploads/2004/07/tr-2004-24.pdf) and [video](https://www.coursera.org/lecture/algorithms-on-graphs/landmarks-optional-h3uOb).
