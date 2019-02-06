package com.example.brendant.chemsolvapp;

/* This class will read in input from the user, in the form of an equation. It can be set by default to be some equation
we want to solve. This class parses the input and creates arrays that we want to pass on to be solved to the solution
handler class.*/

public class InputHandler
{
    private String eqn;
    private int num_terms, num_unique, num_lhs, num_rhs;
    private MatrixHandler solver;

    public InputHandler(String eqn)
    {
        //Split the eqn in two
        String [] sides = eqn.split("(=|->)", 2);
        num_lhs = sides[0].split("[+]", 0).length;
        num_rhs = sides[1].split("[+]", 0).length;

        //Splits the eqn into separate compounds (= or +)
        String [] cpds = eqn.split("[+=]|->", 0);

        //Find what specific elements we're dealing with:
        num_terms = cpds.length;
        String [] elements = find_elements(eqn);

        //Create a matrix of coefficients that we can actually solve
        float [][] matrix = populate_matrix(cpds, elements);

        //Now solve the matrix:
        solver = new MatrixHandler(matrix, elements, cpds, num_lhs);
    }


    /*Parameters: string representing the chemical equation to be solved
    Return value: array of unique elements (e.g., "C", "H", etc.) in the equation

    Additionally, counts number of unique elements and saves in object field (num_unique)*/
    private String [] find_elements(String eqn)
    {
        boolean [] match = new boolean[26];
        String [] elements = new String[10];
        int count = 0;

        for(int i = 0; i < eqn.length(); i++)
        {
            if(eqn.charAt(i) >= 65 && eqn.charAt(i) < 90) //i.e. is A-Z
            {
                if(!match[eqn.charAt(i) - 65])
                {
                    if(eqn.charAt(i + 1) >= 97 && eqn.charAt(i + 1) <= 122) //i.e. is a-z
                    {
                        elements[count] = "" + eqn.charAt(i) + eqn.charAt(i + 1);
                    }
                    else
                    {
                        elements[count] = "" + eqn.charAt(i);
                    }
                    count++;
                }
                match[eqn.charAt(i) - 65] = true;
            }
        }

        num_unique = count;

        String [] res = new String[num_unique];
        System.arraycopy(elements, 0, res, 0, num_unique);
        return res;
    }

    /*Parameters: array of strings representing compounds in the equation, "" elements in the equation
    Return value: 2D array of ints in matrix form that can be solved mathematically to provide our answer*/
    private float [][] populate_matrix(String [] cpds, String [] elements)
    {
        float [][] matrix = new float[num_unique][num_terms];
        float subscr;

        for(int i = 0; i < cpds.length; i++)
        {
            for(int j = 0; j < elements.length; j++)
                if(cpds[i].contains(elements[j]))
                {
                    int loc = cpds[i].indexOf(elements[j]) + elements[j].length();

                    //Check to see if the following character is a number (i.e., an equation subscript)
                    if(loc < cpds[i].length() && cpds[i].charAt(loc)  >= 49 && cpds[i].charAt(loc) <= 57)
                    {
                        subscr = cpds[i].charAt(loc) - '0';
                    }
                    else
                    {
                        subscr = 1;
                    }
                    /*if(loc + 1 < cpds[i].length() && cpds[i].charAt(loc + 1) >= 49 && cpds[i].charAt(loc + 1) <= 57)
                    {
                        subscr *= 10;
                        subscr += cpds[i].charAt(loc + 1) - '0';
                    }*/

                    if(i >= num_lhs && i != (num_lhs + num_rhs - 1))
                    {
                        subscr *= -1;
                    }

                    matrix[j][i] = subscr;
                }
        }

        return matrix;
    }

    public String getSolution()
    {
        return solver.getSolution();
    }
}

