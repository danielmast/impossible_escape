# Impossible Escape

To solve the Impossible Escape puzzle (explained in [this video](https://youtu.be/wTJI_WuZSwE)) and [this article](https://datagenetics.com/blog/december12014/index.html), I implemented an algorithm in Kotlin.

#### Goals
* Improve my skills of Kotlin and functional programming
* Have fun

#### Process
I first created an overcomplicated algorithm that colored a graph in such a way that it could tell from each possible board state which coin to flip. Then I finished watching the YouTube video and understood this problem can be solved way cleverer, similar to how error correction is done. I replaced the old code, but this can still be found in previous commits. However, the old algorithm only works for smaller boards (4x4) because the graph grows astronomically, making it infeasible to generate the graph before the world ends, or store the vertices in an array.

#### How the new algorithm works
As the abovementioned references do a way better job explaining how the algorithm works conceptually, I am not going to burn my hands on that. What I can say is that I tried to write the code as functional as possible and avoid using variables. I stored both the board state and the parity in a BitSet. For convenience, I wrote a function to map an IntArray to a BitSet, so I could quickly define them (often used in the [tests](https://github.com/danielmast/impossible_escape/blob/main/src/test/kotlin/MainTest.kt)). When using the code to solve the puzzle, the board state has to be filled in a text file, as shown in the [example](https://github.com/danielmast/impossible_escape/blob/main/src/main/resources/board.txt.example). The same goes for the location of the [key](https://github.com/danielmast/impossible_escape/blob/main/src/main/resources/key.txt.example), and which [prisoner](https://github.com/danielmast/impossible_escape/blob/main/src/main/resources/mode.txt.example) is up (letting both prisoner 1 and 2 do their job requires running the algorithm twice). So the code itself does not have to be changed. After prisoner 1 has done its job, the board file is overwritten, to mimic the physical scenario, in which prisoner 2 cannot retrieve which coin was flipped by prisoner 1.

#### How the old algorithm works

The algorithm constructs a graph and colors all the vertices. 

Properties of the graph:
* n-dimensional [hypercube graph](https://en.wikipedia.org/wiki/Hypercube_graph)
* `n` different colors
* `n = 2 ^ exp` where `exp` is an integer and `exp >= 0`
* For every vertex `v` and every color `c`, `v` has exactly 1 neighbour with color `c`.

The puzzle can be played with different board sizes. For an 8x8 chess board, it is impossible to use the algorithm, due to the astronomical size of the to-be-constructed (64-dimensional hypercube) graph. The maximum size for which the algorithm works is a 4x4 board (`n = 16`).

The algorithm traverses the graph and determines for each uncolored vertex which colors it would be allowed to get (dependent of its neighbours' neighbours) and picks one. At some point, there will be a vertex for which no allowed colors are left. At that point, backtracking is used to undo the coloring of a couple of vertices, and other colors are tried, until all vertices are colored such that the graph matches the abovementioned properties.

Each vertex represents a board state. All vertices together represent all possible board states. Traversing from a vertex to a neighbour is analogous to flipping one coin on the board. A vertex contains an index which encodes the state as an integer. Imagine the following board state:

|   | A | B | C | D |
|---|---|---|---|---|
| **4** | 1 | 0 | 1 | 1 |
| **3** | 1 | 1 | 0 | 1 |
| **2** | 0 | 0 | 1 | 1 |
| **1** | 1 | 0 | 1 | 0 |
 
Instead of heads and tails, let's use 0 and 1. For the puzzle, the rows and columns are irrelevant. The state can be represented as follows: `1011110100111010`. Translating this from binary to decimal gives the integer `48442`. Let's say we flip the coin at B3. The state now becomes `1011100100111010`, or `47418`. This integer is used as the vertex index. Two vertices are neighbours when the hamming distance between their (binary) indices is equal to 1.

In fact, I do not really construct a graph in the code. Instead, I store all vertices in an array with size `2^n`. This saves a lot of references. The vertex index allows efficient lookup.

When the warden hides the key beneath the coin at for example C2, prisoner 1 needs to flip one coin in order to bring the board in a state such that prisoner 2 can tell where the key is hidden. A vertex color encodes a cell on the board. Each color is represented as an integer between 0 and `n-1`, where A4 corresponds with 0 and D1 corresponds with 15. From vertex `48442`, we have to travel to the neighbour with color 10 (C2) and check its index. Then, prisoner 1 flips the coin that makes the board match with that index. Finally, prisoner 2 looks up the color of the vertex at the state index and finds the key at the location that corresponds with the color.
