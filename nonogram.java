//Calvin Godfrey
//Trochim 1st pd.
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
public class Nonogram{
   private int[][] board; //Initialized to values of 0; this is what we want
                          //-1 will represent it cannot be filled in
                          //1 represents that it has to be filled in
   private ArrayList<ArrayList<Integer>> rowClues; //rowClues[0] are the clues for the first row (from the top)
                             //This means that it will have to be a jagged matrix, since
                             //Different rows will have different numbers of clues
                             //I made this easier by using arraylists
   private ArrayList<ArrayList<Integer>> colClues;
   private ArrayList<ArrayList<Boolean>> rowCluesDone; //True means that we know that clue is done
   private ArrayList<ArrayList<Boolean>> colCluesDone;
   private int height;
   private int width;
   private String[] dispRow; //String[] containing the clues for each row to display
   private String[] dispCol; //String[] containing the clues for each column to display
   
   public Nonogram(String loc)throws IOException{
      //This is how the text file should be arranged:
      //The first line will have an integer n; this is the height
      //The second line will have an integer m; this is the width
      //Next will be n rows containing integers separated by spaces
      //These are the clues for the rows
      //Finally there are m more rows containing the clues for the columns
      rowClues = new ArrayList<ArrayList<Integer>>();
      colClues = new ArrayList<ArrayList<Integer>>();
      rowCluesDone = new ArrayList<ArrayList<Boolean>>();
      colCluesDone = new ArrayList<ArrayList<Boolean>>();
      Scanner file = new Scanner(new File(loc));
      height = file.nextInt();
      dispRow = new String[height];
      String dummy = file.nextLine();
      width = file.nextInt();
      dummy = file.nextLine();
      for(int i=0;i<height;i++){
         String clues = file.nextLine();
         if(clues.length() > width){
            System.out.println("ERROR: Row size too big");
            return;
         }
         dispRow[i] = clues;
         ArrayList<Integer> temp = new ArrayList<Integer>();
         ArrayList<Boolean> doneTemp = new ArrayList<Boolean>();
         int start = 0; //Start of number
         for(int j=0;j<clues.length();j++){
            if(clues.substring(j, j+1).equals(" ")){
               temp.add(Integer.parseInt(clues.substring(start, j)));
               doneTemp.add(false); // All of the clues should be false right now
               start = j+1; //Update where the number starts
            }
         }
         if(clues.length()>1 && !clues.substring(clues.length()-2, clues.length()-1).equals(" ")){ //Check if last number is 1 or 2 digits
            temp.add(Integer.parseInt(clues.substring(clues.length()-2)));
         }else{
            temp.add(Integer.parseInt(clues.substring(clues.length()-1))); //Don't forget the last number
         }
         doneTemp.add(false);
         rowCluesDone.add(doneTemp);
         rowClues.add(temp);
      }
      dispCol = new String[width];
      board = new int[height][width];
      for(int i=0;i<width;i++){
         String clues = file.nextLine();
         if(clues.length() > height){
            System.out.println("ERROR: Column size too big");
            return;
         }
         dispCol[i] = clues;
         ArrayList<Integer> temp = new ArrayList<Integer>();
         ArrayList<Boolean> doneTemp = new ArrayList<Boolean>();
         int start = 0;
         for(int j=0;j<clues.length();j++){
            if(clues.substring(j, j+1).equals(" ")){
               temp.add(Integer.parseInt(clues.substring(start, j)));
               doneTemp.add(false);
               start = j+1;
            }
         }
         if(clues.length()>1 && !clues.substring(clues.length()-2, clues.length()-1).equals(" ")){
            temp.add(Integer.parseInt(clues.substring(clues.length()-2)));
         }else{
            temp.add(Integer.parseInt(clues.substring(clues.length()-1)));
         }
         doneTemp.add(false);
         colCluesDone.add(doneTemp);
         colClues.add(temp);
      }
   }
   
   private int[] getClueSize(){
      int[] ans = new int[2];
      int rowMax = 0;
      int colMax = 0;
      for(int i=0;i<rowClues.size();i++){
         int sum = 0;
         ArrayList<Integer> clues = rowClues.get(i);
         for(int j=0;j<clues.size();j++){
            if(clues.get(j)>9){
               sum += 2;
            } else {
               sum++;
            }
            sum++; //For the space between numbers
         }
         sum--; //Don't want to include one sum
         if(sum>rowMax){
            rowMax = sum;
         }
      }
      
      for(int i=0;i<colClues.size();i++){
         int sum = 0;
         ArrayList<Integer> clues = colClues.get(i);
         for(int j=0;j<clues.size();j++){
            if(clues.get(j)>9){
               sum += 2;
            } else {
               sum++;
            }
            sum++;
         }
         sum--;
         if(sum>colMax){
            colMax = sum;
         }
      }
      ans[0] = rowMax;
      ans[1] = colMax;
      return ans;
   }
   
   public void viewBoard(){
      int[] max = getClueSize();
      int rowMax = max[0];
      int colMax = max[1];
      for(int i=0;i<dispRow.length;i++){
         while(dispRow[i].length() < rowMax){
            dispRow[i] = " " + dispRow[i];
         }
      }
      for(int i=0;i<dispCol.length;i++){
         while(dispCol[i].length() < colMax){
            dispCol[i] = " " + dispCol[i];
         }
      }
      //END PREPARING TO DISPLAY//
      //BEGIN ACTUALLY PRINTING OUT THE CLUES//
      for(int i=0;i<colMax;i++){
         for(int r=0;r<rowMax+1;r++){
            System.out.print(" ");
         }
         for(int j=0;j<dispCol.length;j++){
            System.out.print(dispCol[j].substring(i, i+1));
         }
         System.out.println();
      }
      System.out.println();
      //BEGIN PRINTING OUT ROWS/FIELD
      for(int i=0;i<height;i++){
         System.out.print(dispRow[i] + " ");
         for(int j=0;j<width;j++){
            if(board[i][j] == 1){
               System.out.print("#");
            } else {
               if(board[i][j] == -1){
                  System.out.print("X");
               } else {
                  System.out.print(" ");
               }
            }
         }
         System.out.println();
      }      
   }
   
   public void setValue(int row, int col, int value){
      board[row][col] = value;
   }
   
   public void basicParseRow(int row){
      int magic; //The number of uncertain cells in each clue block
      int sum = 0;
      for(int i=0;i<rowClues.get(row).size();i++){
         sum += rowClues.get(row).get(i);
         sum++;
      }
      sum--; //We only account for spaces, so we subtract one
      magic = width-sum;
      System.out.println(magic);
      //Loop through the clues and only mark cells if we are certain about them
      int counter = 0; //Where in the row we are
      for(int i=0;i<rowClues.get(row).size();i++){
         if(rowClues.get(row).get(i) <= magic){
            counter += rowClues.get(row).get(i); //ALL of the cells are uncertain
            continue;
         }
         int clue = rowClues.get(row).get(i);
         counter += magic; //Skip the number of uncertain cells
         for(int j=0;j<clue-magic;j++){
            setValue(row, counter, 1); //Mark the certain ones
            counter++;
         }
         if(magic == 0 && counter < width){ //We know the row exactly
            setValue(row, counter, -1);
         }
         counter++;
      }
   }
   
   public void basicParseCol(int col){
      int magic;
      int sum = 0;
      for(int i=0;i<colClues.get(col).size();i++){
         sum += colClues.get(col).get(i);
         sum++;
      }
      sum--;
      magic = height-sum;
      int counter = 0;
      for(int i=0;i<colClues.get(col).size();i++){
         if(colClues.get(col).get(i) <= magic){
            counter += colClues.get(col).get(i);
            continue;
         }
         int clue = colClues.get(col).get(i);
         counter += magic;
         for(int j=0;j<clue-magic;j++){
            setValue(counter, col, 1);
            counter++;
         }
         if(magic == 0 && counter < height){
            setValue(counter, col, -1);
         }
         counter++;
      }
   }
   
   public void basicParse(){
      for(int r=0;r<height;r++){
         basicParseRow(r);
      }
      for(int c=0;c<width;c++){
         basicParseCol(c);
      }
   }
   
} //END OF CLASS
