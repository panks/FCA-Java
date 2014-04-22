FCA-Java
========

Formal concept analysis lattice generation and query in Java.

## Dependencies
* [JUNG](http://jung.sourceforge.net/) for generating the lattice

## Usage

For the following context



      |   1   |   2   |   3   |   4   |
    --|-------|-------|-------|-------|--
    a |   1   |   0   |   1   |   0   |
    --|-------|-------|-------|-------|--
    b |   1   |   1   |   0   |   1   |
    --|-------|-------|-------|-------|--
    c |   1   |   0   |   1   |   0   |
    --|-------|-------|-------|-------|--
    d |   0   |   1   |   0   |   1   |
    --|-------|-------|-------|-------|--


First the program would ask for number of object and number of attributes.
Then you need to input the objects separated by space::

    a b c d

After that you need to provide the attributes separated by space:

    1 2 3 4 5

and last input would be the matrix, in row major order:

    1 0 1 0
    1 1 0 1
    1 0 1 0
    0 1 0 1

A window will open containing the representation of the concept lattice. And you can query the concepts i.e. extent for a given intent or intent for a given extent in terminal.
