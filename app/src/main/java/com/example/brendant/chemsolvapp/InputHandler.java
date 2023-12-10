package com.example.brendant.chemsolvapp;

/* This class will read in input from the user, in the form of an equation. It can be set by default to be some equation
we want to solve. This class parses the input and creates arrays that we want to pass on to be solved to the solution
handler class.*/

public class InputHandler
{
    private String eqn;
    private int num_terms, num_unique, num_lhs, num_rhs;
    private int last_coefficient = 1;
    private float [][] matrix;
    private String [] cpds;


    public InputHandler(String eqn)
    {
        //Split the eqn in two
        String [] sides = eqn.split("(=|->)", 2);
        num_lhs = sides[0].split("[+]", 0).length;
        num_rhs = sides[1].split("[+]", 0).length;

        //Splits the eqn into separate compounds (= or +)
        cpds = eqn.split("[+=]|->", 0);

        //Find what specific elements we're dealing with:
        num_terms = cpds.length;
        String [] elements = find_elements(eqn);

        //Create a matrix of coefficients that we can actually solve
        matrix = populate_matrix(cpds, elements);

        //Now solve the matrix:
        gje();
    }


    /*Parameters: string representing the chemical equation to be solved
    Return value: array of unique elements (e.g., "C", "H", etc.) in the equation

    Additionally, counts number of unique elements and saves in object field (num_unique)*/
    private String [] find_elements(String eqn)
    {
        boolean [] match = new boolean[26];     //Does a letter/string, aka element, already exist?
        String [] elements = new String[10];    //We only hold up to 10 elements; this can be expanded.
        int count = 0;

        //Parse equation string into elements.
        for(int i = 0; i < eqn.length(); i++)
        {
            if(eqn.charAt(i) >= 65 && eqn.charAt(i) < 90) //65 = A ... 90 = Z
            {
                if(!match[eqn.charAt(i) - 65])  //Is this a new letter or have we encountered it before?
                {
                    if(eqn.charAt(i + 1) >= 97 && eqn.charAt(i + 1) <= 122) //97 = a ... 122 = z
                    {
                        elements[count] = "" + eqn.charAt(i) + eqn.charAt(i + 1);   //For element symbols w/ 2 letters e.g. Ar
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
        System.arraycopy(elements, 0, res, 0, num_unique);  //copies [num_unique] items from ELEMENTS to RES

        //Returning res instead of elements means we aren't using an array of 10 empty strings
        //Not sure if this is really necessary
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

                        //If coefficient is above 10? (Rare in real world)
                        if(loc + 1 < cpds[i].length() && cpds[i].charAt(loc + 1) >= 49 && cpds[i].charAt(loc + 1) <= 57)
                        {
                            subscr *= 10;
                            subscr += cpds[i].charAt(loc + 1) - '0';
                        }
                    }
                    else
                    {
                        subscr = 1;
                    }

                    if(i >= num_lhs && i != (num_lhs + num_rhs - 1))
                    {
                        subscr *= -1;
                    }

                    matrix[j][i] = subscr;
                }
        }

        return matrix;
    }

    //The mathy part. Reduces matrix to its row echelon form, if possible.
    private void gje()
    {
        int i, j, k, c;
        for(i = 0; i < num_unique; i++)
        {
            if (matrix[i][i] == 0)
            {
                c = 1;
                while (matrix[i + c][i] == 0 && (i + c) < num_unique)
                {
                    c++;
                }
                if ((i + c) == 3)
                {
                    break;
                }
                for (j = i, k = 0; k < num_terms; k++)
                {
                    float ph = matrix[j][k];
                    matrix[j][k] = matrix[j + c][k];
                    matrix[j + c][k] = ph;
                }
            }

            for (j = 0; j < num_unique; j++)
            {
                if (i != j)
                {
                    float ratio = matrix[j][i] / matrix[i][i];

                    for (k = 0; k < num_terms; k++)
                    {
                        matrix[j][k] = matrix[j][k] - (matrix[i][k] * ratio);
                    }
                }
            }
        }

        simplify();
        makeIntegers();
    }

    //Helper function to simplify matrix
    private void simplify()
    {
        for(int i = 0; i < matrix.length; i++)
        {
            if(matrix[i][i] != 1)
            {
                matrix[i][num_terms - 1] /= matrix[i][i];
                matrix[i][i] = 1;
            }
        }
    }

    //Chemical equations (typically) should use integers not fractional numbers
    private void makeIntegers()
    {
        int maxSize = matrix[0].length - 1;
        for(int i = 0; i < matrix.length; i++)
        {
            if(matrix[i][maxSize] != (int)matrix[i][maxSize])
            {
                findIntegerFactor();
            }
        }
        for(int i = 0; i < matrix.length; i++)
        {
            matrix[i][maxSize] = Math.round(matrix[i][maxSize]);
        }
    }

        //Helper function to make answer have integers only
    public void findIntegerFactor() {
        for (int factor = 1; factor <= 100; factor++) {
            float[][] newMatrix = multiplyMatrixByFactor(matrix, factor);

            if (isMatrixInteger(newMatrix)) {
                matrix = newMatrix;
                last_coefficient *= factor;

                break;
            }
        }
    }

    public float[][] multiplyMatrixByFactor(float[][] inputMatrix, float factor) {
        float[][] resultMatrix = new float[inputMatrix.length][inputMatrix[0].length];

        for (int i = 0; i < inputMatrix.length; i++) {
            for (int j = 0; j < inputMatrix[i].length; j++) {
                resultMatrix[i][j] = inputMatrix[i][j] * factor;
            }
        }

        return resultMatrix;
    }

    public boolean isMatrixInteger(float[][] matrix) {

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (Math.abs(matrix[i][j] - Math.round(matrix[i][j])) > 0.01) {
                    return false; // If any element is not an integer, return false
                }
            }
        }

        return true; // All elements are integers
    }

    //Formats solution correctly
    public String getSolution()
    {
        String ret = "";
        int maxSize = matrix[0].length - 1;
        for(int i = 0; i < matrix.length; i++)
        {
            int coeff = (int)matrix[i][maxSize];
            if(coeff != 1)
                ret += coeff;
            ret += cpds[i].replaceAll("\\s", "");   //remove spaces

            if(i == num_lhs - 1)
            {
                ret += "=\t";
            }
            else if(i != matrix.length)
            {
                ret += "+\t";
            }
        }
        if(last_coefficient != 1)
        {
            ret += Integer.toString(last_coefficient);
        }
        ret += cpds[cpds.length - 1].replaceAll("\\s", "");;
        return ret;
    }
}

