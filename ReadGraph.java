import java.io.*;
import java.util.*;

import javax.print.attribute.SupportedValuesAttribute;

class ColEdge {
    int u;
    int v;
}

public class ReadGraph {
    public final static boolean DEBUG = true;
    public final static String COMMENT = "//";

    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("Error! No filename specified.");
            System.exit(0);
        }
        String inputfile = args[0];
        boolean seen[] = null;
        // ! n is the number of vertices in the graph
        int n = -1;
        // ! m is the number of edges in the graph
        int m = -1;
        // ! e will contain the edges of the graph
        ColEdge e[] = null;
        try {
            FileReader fr = new FileReader(inputfile);
            BufferedReader br = new BufferedReader(fr);
            String record = new String();
            // ! THe first few lines of the file are allowed to be comments, staring with a
            // // symbol.
            // ! These comments are only allowed at the top of the file.
            // ! -----------------------------------------
            while ((record = br.readLine()) != null) {
                if (record.startsWith("//"))
                    continue;
                break; // Saw a line that did not start with a comment -- time to start reading the
                       // data in!
            }
            if (record.startsWith("VERTICES = ")) {
                n = Integer.parseInt(record.substring(11));
                if (DEBUG)
                    System.out.println(COMMENT + " Number of vertices = " + n);
            }
            seen = new boolean[n + 1];
            record = br.readLine();
            if (record.startsWith("EDGES = ")) {
                m = Integer.parseInt(record.substring(8));
                if (DEBUG)
                    System.out.println(COMMENT + " Expected number of edges = " + m);
            }
            e = new ColEdge[m];
            for (int d = 0; d < m; d++) {
                if (DEBUG)
                    System.out.println(COMMENT + " Reading edge " + (d + 1));
                record = br.readLine();
                String data[] = record.split(" ");
                if (data.length != 2) {
                    System.out.println("Error! Malformed edge line: " + record);
                    System.exit(0);
                }
                e[d] = new ColEdge();
                e[d].u = Integer.parseInt(data[0]);
                e[d].v = Integer.parseInt(data[1]);
                seen[e[d].u] = true;
                seen[e[d].v] = true;
                if (DEBUG)
                    System.out.println(COMMENT + " Edge: " + e[d].u + " " + e[d].v);
            }
            String surplus = br.readLine();
            if (surplus != null) {
                if (surplus.length() >= 2)
                    if (DEBUG)
                        System.out.println(
                                COMMENT + " Warning: there appeared to be data in your file after the last edge: '"
                                        + surplus + "'");
            }
        } catch (IOException ex) {
            // catch possible io errors from readLine()
            System.out.println("Error! Problem reading file " + inputfile);
            System.exit(0);
        }
        for (int x = 1; x <= n; x++) {
            if (seen[x] == false) {
                if (DEBUG)
                    System.out.println(COMMENT + " Warning: vertex " + x
                            + " didn't appear in any edge : it will be considered a disconnected vertex on its own.");
            }
        }
        // ! At this point e[0] will be the first edge, with e[0].u referring to one
        // endpoint and e[0].v to the other
        // ! e[1] will be the second edge...
        // ! (and so on)
        // ! e[m-1] will be the last edge
        // !
        // ! there will be n vertices in the graph, numbered 1 to n
        // ! INSERT YOUR CODE HERE!

        // This line is just for clarifying where our code starts!
        System.out.println("*************************************************");

        // Let's make 2-D array!（This array is "an extra-step" to make your brain organized）

        // ”tempVertices” is (twice the numberof the edges) × 2collumns array. This array is to store both values,
        // u, v and v, u.(Again, this is just the "EXTRA-STEP," but this array will make your life easier. This 
        // array is for getting ready to make "connectedVertices" array.)
        int[][] tempVertices = new int [m*2][2];

        // "connectedVertices" is an array to see which vertix is connected to which ones. This is number of
        // vertices × (numberos vertices - 1).
        int[][] connectedVertices = new int[n][n];

    
       
        // This forloop below take vertices(u, v) in e(It's an array which has all edge information), and put 
        // the vertices in tempVertices. In each loop, it will store both [u, v] and [v, u] in tempvertices.
        // That's why the length of "tempVertices" will be TWICE of the length of e.
        for (int i = 0; i < e.length; i++){

            // row(i*2)（u, v）
            tempVertices[i*2][0] = e[i].u;
            tempVertices[i*2][1] = e[i].v;

            // row(i*2 + 1)（v, u）
            tempVertices[i*2 + 1][0] = e[i].v;
            tempVertices[i*2 + 1][1] = e[i].u;



            // ↓debug code（IGNORE ME!）
            // System.out.println(Arrays.toString(tempVertices[i*2]));
            // System.out.println(Arrays.toString(tempVertices[i*2 + 1]));
        }
            // ↓debug code（IGNORE ME!）
            // System.out.println(tempVertices.length);

            // tempVertices are done！！！！！！

        // Now we are transferrig the vertix information from tempVertices to "connectedVertices"!!!
        for(int j = 0; j < tempVertices.length; j++){
           
            // If the value is already registerd...
            if (isRegistered(connectedVertices, tempVertices[j][0])){

                
                // Create the variable "registerdRowNumber", and put the registeredRowNumber method in it
                // Since if you put the method in "connectedVertices[here][]", the method will be updated
                // everytime, and that's not what we want. (You can try putting the method in stead of variable,
                // then you might understand the problem better)
                int registeredRowNumber = registeredRowNumber(connectedVertices, tempVertices[j][0]);
                connectedVertices[registeredRowNumber][collumnNumber(connectedVertices, registeredRowNumber)] = tempVertices[j][1];


            // If not...
            }else{
                // Same reason as above, create a variable and put the method in it
                int rowNumber = rowNumber(connectedVertices);

   
                // the value(tempVertices[j][0]) is not registered in connectedVertices array, so put the value in
                // connectedVertices[j][0], and put tempVetices[j][1] right next to where tempVertices was stroed.
                connectedVertices[rowNumber][0] = tempVertices[j][0];
                connectedVertices[rowNumber][1] = tempVertices[j][1];
                // 　　　　　　　　　　　↑if you put method here, you will have to deal with a bug

            }

            
        }

        // ↓debug code（IGNORE ME!）
        System.out.println("-connectedVertices-");
        for(int k = 0; k < connectedVertices.length; k++){
        System.out.println(Arrays.toString(connectedVertices[k]));
        }
        

        //Finaly, let's color the graph!

        // Let's make an array that shows the color of the each vertices. This array correspond to the 
        // 1st collumn of the connectedVertices. That means if the colorNumber[i] is 1, it shows that
        // connnectedVertices[i][0] is colored by 1(1 is a color / ex. 1 = red).
        int [] colorNumber = new int [n];


        // This forloop is coloring each vertix with a different color from all the vertices connected to the 
        // registed vertix.
        for(int i = 0; i < connectedVertices.length; i++){
            
            int currentBaseNumber = i;

            // The if statement below saying that color the vertix
            if(i > 0){
                colorNumber[i] = 1;
            }

            getColorNumber(connectedVertices, colorNumber, currentBaseNumber);

            for(int j = 1; j < connectedVertices[i].length; j++){
                if(connectedVertices[i][j] == 0){
                    continue;
                }

                if(colorNumber[i] == checkColor(connectedVertices, colorNumber, connectedVertices[i][j])){
                    colorNumber[i]++;
                    
                }
            }
        }

        // Take the highest number from the elements in colorNumber
        int chromaticNumber = getMaximum(colorNumber);
        

        // ↓debug code（IGNORE ME!）
        System.out.println("*************************************************");
        System.out.println("colorNumber: " + Arrays.toString(colorNumber));
        System.out.println("Chromatic number of the graph is " + chromaticNumber);
       
    }

  
    // "rowNumber" is the method to find the most optimal row from the connectedVertices to put the unregistered(new) value
    // The most optimal value is the top row of the blank space
    public static int rowNumber(int [][] connectedVertices) {

        for(int i = 0; i < connectedVertices.length; i++){

            // If row i is blank, then connectedVertices[i][0] == 0. Therefore, what this method has to do is,
            // going through each row and check the 1st collumn, and find where i == 0, and return i!
            if(connectedVertices[i][0] == 0){
                return i;
            }
        }
        
        // This is just how-java-works kind problem that you have to decide the return value when all rows are
        // filled even though that situation is impossible because the height(row) of the connectedVertices
        // is the number of the vertices.(So all vertices should have the space!)
        return connectedVertices.length - 1;
    }

    
    // "isRegistered" is the boolean method to check if the value is already registerd in connectedVertices. If the number
    // is already registed, you want to return TRUE. If not, you want to return false.
    public static boolean isRegistered(int [][] connectedVertices, int tempvalue) {

        // check first collumn for each row, and see if the value is already registered
        for(int i = 0; i < connectedVertices.length; i++){
            if(connectedVertices[i][0] == tempvalue){
               return true;
            }
        }
        return false;
    }

    
    // "collumnNumber"is the method to find the most optimal "column" for the connected point (tempVertices[j][1])
    //  to be placed in.
    // Finding the most optimal collumn requires information on connectedVertices and registerdRowNumber 
    // to determine which rows to examine
    public static int collumnNumber(int [][] connectedVertices, int registeredRowNumber) {

        // forloop here is almost doing the same thing as the forloop in the rowNumber
        for(int i = 0; i < connectedVertices.length; i++){
            if(connectedVertices[registeredRowNumber][i] == 0){
                return i;
            }
        }
        // look up the same part of the rowNumber method
        return -1;
    }

    // "registeredRowNumber"is the method to find the most optimal "row" for the connected point (tempVertices[j][1])
    //  to be placed in.
    // To find the most ptimal row, you want to get the row of the connectedVertices in which the 
    // value we are looking at is registered.
    public static int registeredRowNumber(int [][] connectedVertices, int tempvalue) {
        for(int i = 0; i < connectedVertices.length; i++){
            if(connectedVertices[i][0] == tempvalue){
               return i;
            }
        }
        // look up the same part of the rowNumber method
        return -1;
    }

    // "CheckColor" method 
    public static int checkColor(int [][] connectedVertices, int []colorNumber, int currentNumber) {
        for(int i = 0; i < connectedVertices.length; i++){
            if(connectedVertices[i][0] == currentNumber){
                return colorNumber[i];
            }

        }
        
        return -1;
    }

    public static int getMaximum(int[] colorNumber){
        int maximumNumber = 0;
        for(int i = 0; i < colorNumber.length; i++){
            
            if (maximumNumber < colorNumber[i]){
                maximumNumber = colorNumber[i];
            }

        }

        return maximumNumber;
    }


    public static int getColorNumber(int [][] connectedVertices, int []colorNumber, int currentBaseNumber){
        for(int j = 1; j < connectedVertices[currentBaseNumber].length; j++){
            if(connectedVertices[currentBaseNumber][j] == 0){
                continue;
            }

            if(colorNumber[currentBaseNumber] == checkColor(connectedVertices, colorNumber, connectedVertices[currentBaseNumber][j])){
                colorNumber[currentBaseNumber]++;
                getColorNumber(connectedVertices, colorNumber, currentBaseNumber);                
            }
        }
        return -1;
    }
}

