# Impossible Escape

To solve the Impossible Escape puzzle (explained in [this video](https://youtu.be/wTJI_WuZSwE)), I implemented a graph coloring algorithm in Kotlin.

#### Goals
* Improve my skills of Kotlin, functional programming and graph theory
* Have fun

#### How it works
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

In fact, I am not really constructing a graph in the code. Instead, I store all vertices in an array with size `2^n`. The vertex index allows efficient lookup. 