# City Directory App
This project is a solution of a test Assignment given by Backbase.

### Source
Complete solution can be found on a public github repository.

| Name | Github Link |
| ------ | ------ |
| CityDirectoryApp | [Source Code](https://www.google.com) |

### Problem
Fetch a list of cities from a local JSON file and show sorted city list in android app. Size of the record is around 200k. App should be optimised for searching and filtering out results in realtime (as user types).

### Solution
To solve this problem, I worked on 2 approaches of which one worked.

#### Approach #1: Using Trie data structure (didn't work)
First approach that came in to my mind was to use Trie since it is optimal for string search and has time complexity of O(M) where M is query string length. But the trade off of this was huge heap size. I tried using Trie first but it resulted in Out of Memory Exceptions.
Challenges:
  - I tried using a fixed Array of 26 for TrieNode to optimise memory but records     contained various other characters. 
  - List of coordinates had to be managed separately.
  - An ArrayList had to be managed too to show records in Recyclerview.

#### Approach #2: Using list with track of character ranges (worked!)
To optimise filtering, I used array keeping track of character ranges (start/end index) so that Iteration is on selected chunk of the list instead of complete array.

##### Algorithm
The algorithm has following steps:
1. Sort the list ignoring case.
2. Get array index information of all starting characters in a map. (This helped in reducing the search list as soon as user typed first char) for example: A (start: 0, end:20) B (start: 21,end: 24) etc
3. When query size is greater than one, search in the sublist based on index information and keep track of query indexes in a stack for performance.
