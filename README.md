# ChemSolv

ChemSolv is a simple Android app in Java that balances chemical equations using Gauss-Jordan elimination.

# How it works

This app takes input from a user in the form of a chemical equation, and then translates that into a system of equations represented by a matrix. From there, the rows of the matrix are reduced to produce the solution. In the system of equations, one equation is produced per element, and each of these becomes a row in the matrix. 

Gauss-Jordan elimination, or Gaussian elimination, is a method of reducing a complex matrix to its "row echelon form", or "triangular form", which when discretely solveable consists only of 1's and 0's. The input equation is run through an algorithm which produces this result, and then interprets that into readable output -- the correctly balanced chemical equation.

# The dev 

I transitioned to computer science from a career in chemistry, after I found that the parts of my job I enjoyed the most included automating tasks and improving workflow efficiency with scripts, programs, and other applications. 

One of the things I thought a lot about in my struggle through organic chemistry was writing a program that would automatically solve chemical equations. It's been done before, but it's a subject that's important to me, so I wrote this mostly to prove to myself that I could.

# Issues

The app works fine if an equation is solveable and has the correct syntax. It's going to need some error checking to tell whether or not a solution is findable, or provide feedback if input doesn't conform to its requirements. 


