package com.example.brendant.chemsolvapp;

public class MatrixHandler
{
    private float [][] matrix;
    private String [] elements;
    private String [] cpds;
    private int lastCoefficient;
    private int num_lhs;

    public MatrixHandler(float[][] matrix, String[] elements, String[] cpds, int num_lhs)
    {
        //solve me
        this.matrix = matrix;
        /*this.matrix = new float[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++)
        {
            for(int j = 0; j < matrix[0].length; j++)
            {
                this.matrix[i][j] = (float)matrix[i][j];
            }
        }*/
        print();
        this.elements = elements;
        this.cpds = cpds;
        lastCoefficient = 1;
        this.num_lhs = num_lhs;

        gje();
        print();
        print_elements();
    }

    private void gje()
    {
        int i, j, k, c;
        for(i = 0; i < 3; i++)
        {
            if (matrix[i][i] == 0)
            {
                c = 1;
                while (matrix[i + c][i] == 0 && (i + c) < 3)
                {
                    c++;
                }
                if ((i + c) == 3)
                {
                    break;
                }
                for (j = i, k = 0; k <= 3; k++)
                {
                    float ph = matrix[j][k];
                    matrix[j][k] = matrix[j + c][k];
                    matrix[j + c][k] = ph;
                }
            }

            for (j = 0; j < 3; j++)
            {
                if (i != j)
                {
                    float ratio = matrix[j][i] / matrix[i][i];

                    for (k = 0; k <= 3; k++)
                    {
                        matrix[j][k] = matrix[j][k] - (matrix[i][k] * ratio);
                    }
                }
            }
            print();
        }



        //float ratio = 1;
        /*ratio = find_ratio(0, 0, 1);
        if(ratio==0)
        {

        }
        sum_rows(0, 1, ratio);*/
        //ratio = find_ratio(

        simplify();
        makeIntegers();
    }

    //Turns the matrix into an identity matrix... not strictly what we need but a good jumping off point for tonight
    private void simplify()
    {
        for(int i = 0; i < matrix.length; i++)
        {
            if(matrix[i][i] != 1)
            {
                matrix[i][3] /= matrix[i][i];
                matrix[i][i] = 1;
            }
        }
    }

    //Finds the decimal ratio in a given column between two given rows of the matrix
    private float find_ratio(int col, int row_a, int row_b)
    {
        return -1*((float)matrix[row_b][col]/(float)matrix[row_a][col]);
    }

    //Reverses the location of two rows. Will need to reverse the order of the Elements array too.
    private void flip_rows(int row_a, int row_b)
    {
        float [] placeholder = new float[matrix[0].length];

        System.arraycopy(placeholder, 0, matrix[row_a], 0, matrix[row_a].length);

        matrix[row_a] = matrix[row_b];
        matrix[row_b] = placeholder;

        String elementPlaceholder = elements[row_a];
        elements[row_a] = elements[row_b];
        elements[row_b] = elementPlaceholder;
    }

    //Adds together two rows in the matrix
    private void sum_rows(int row_a, int row_b, float ratio)
    {

        for(int i = 0; i < matrix[row_a].length; i++)
        {
            matrix[row_b][i] += matrix[row_a][i] * ratio;
        }
    }

    //Prints out the matrix
    private void print()
    {
        for(int i = 0; i < matrix.length; i++)
        {
            for(int j = 0; j < matrix[0].length; j++)
            {
                //System.out.printf("%1.2f\t\t", matrix[i][j]);
            }
            //System.out.print("\n");
        }
        //System.out.print("\n");
    }

    private void print_elements()
    {
        for(int i = 0; i < elements.length; i++)
        {
            //System.out.print(elements[i] + "\t");
        }
    }

    /*private int findGCD()
    {
        //euclidean algorithm

    }

    private int findGCD(

    private int findLCM()
    {
        int matrixSize = matrix[0].length;
        int naive = 1;
        for(int i = 0; i < matrix.length; i++)
        {
            naive *= matrix[i][matrixSize];
        }

        return naive;
        //int naive = matrix[0][matrixSize]
    }
    */

    private void makeIntegers()
    {
        int maxSize = matrix[0].length - 1;
        for(int i = 0; i < matrix.length; i++)
        {
            if(matrix[i][maxSize] != (int)matrix[i][maxSize])
            {
                multiplyBy(1 / (matrix[i][maxSize] - (int) matrix[i][maxSize]));
            }
        }
        for(int i = 0; i < matrix.length; i++)
        {
            matrix[i][maxSize] = Math.round(matrix[i][maxSize]);
        }
    }

    private void multiplyBy(float factor)
    {
        for(int i = 0; i < matrix.length; i++)
        {
            matrix[i][matrix[0].length - 1] *= factor;
        }
        lastCoefficient *= factor;
    }

    public String getSolution()
    {
        String ret = "";
        int maxSize = matrix[0].length - 1;
        for(int i = 0; i < matrix.length; i++)
        {
            int coeff = (int)matrix[i][maxSize];
            if(coeff != 1)
                ret += coeff;
            ret += cpds[i];

            if(i == num_lhs - 1)
            {
                ret += "=\t";
            }
            else if(i != matrix.length)
            {
                ret += "+\t";
            }
        }
        ret += Integer.toString(lastCoefficient) + cpds[cpds.length - 1];
        return ret;
    }
}
