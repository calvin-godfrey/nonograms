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
   private ArrayList<ArrayList<Boolean>> isRowClueDone;
   private ArrayList<ArrayList<Boolean>> isColClueDone;
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
      isRowClueDone = new ArrayList<ArrayList<Boolean>>();
      isColClueDone = new ArrayList<ArrayList<Boolean>>();
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
         ArrayList<Boolean> tempDone = new ArrayList<Boolean>();
         int start = 0; //Start of number
         for(int j=0;j<clues.length();j++){
            if(clues.substring(j, j+1).equals(" ")){
               temp.add(Integer.parseInt(clues.substring(start, j)));
               tempDone.add(false);
               start = j+1; //Update where the number starts
            }
         }
         if(clues.length()>1 && !clues.substring(clues.length()-2, clues.length()-1).equals(" ")){ //Check if last number is 1 or 2 digits
            temp.add(Integer.parseInt(clues.substring(clues.length()-2)));
         }else{
            temp.add(Integer.parseInt(clues.substring(clues.length()-1))); //Don't forget the last number
         }
         tempDone.add(false);
         isRowClueDone.add(tempDone);
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
         ArrayList<Boolean> tempDone = new ArrayList<Boolean>();
         int start = 0;
         for(int j=0;j<clues.length();j++){
            if(clues.substring(j, j+1).equals(" ")){
               tempDone.add(false);
               temp.add(Integer.parseInt(clues.substring(start, j)));
               start = j+1;
            }
         }
         if(clues.length()>1 && !clues.substring(clues.length()-2, clues.length()-1).equals(" ")){
            temp.add(Integer.parseInt(clues.substring(clues.length()-2)));
         }else{
            temp.add(Integer.parseInt(clues.substring(clues.length()-1)));
         }
         tempDone.add(false);
         isColClueDone.add(tempDone);
         colClues.add(temp);
      }
   }
   
   private int[] getCol(int index){
      int[] ans = new int[height];
      for(int i=0;i<height;i++){
         ans[i] = board[i][index];
      }
      return ans;
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
   
   public void display(){
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
   
   public void basicParse(int row, boolean isRow){ //True for row, false for column
      int magic; //The number of uncertain cells in each clue block
      int sum = 0;
      ArrayList<Integer> clues = isRow ? rowClues.get(row) : colClues.get(row);
      for(int i=0;i<clues.size();i++){
         sum += clues.get(i);
         sum++;
      }
      sum--; //We only account for spaces, so we subtract one
      magic = width-sum;
      //Loop through the clues and only mark cells if we are certain about them
      int counter = 0; //Where in the row we are
      for(int i=0;i<clues.size();i++){
         if(clues.get(i) <= magic){
            counter += clues.get(i); //ALL of the cells are uncertain
            counter++;
            continue;
         }
         int clue = clues.get(i);
         counter += magic; //Skip the number of uncertain cells
         for(int j=0;j<clue-magic;j++){
            if(isRow){
               setValue(row, counter, 1); //Mark the certain ones
            } else {
               setValue(counter, row, 1);
            }
            counter++;
         }
         if(magic == 0 && counter < width){ //We know the row exactly
            if(isRow){
               setValue(row, counter, -1);
            } else {
               setValue(counter, row, -1);
            }
         }
         counter++;
      }
   }
   
   public void basicParse(){
      for(int r=0;r<height;r++){
         basicParse(r, true);
      }
      for(int c=0;c<width;c++){
         basicParse(c, false);
      }
   }
   
   private int[] findNthBlock(int[] set, int n){ //Returns [start, end] index of the nth block of filled in squares in a row
                                                   //1-indexed! Not 0
      int curr = 0;
      int currStart = 0;
      int currEnd = 0;
      boolean inBlock = false;
      for(int i=0;i<set.length;i++){
         if(set[i] == 1 && !inBlock){
            inBlock = true;
            curr++;
            currStart = i;
         }
         if(set[i] != 1 && inBlock){
            currEnd = i-1;
            if(curr == n){
               return new int[]{currStart, currEnd};
            }
            inBlock = false;
         }
      }
      return new int[]{-1, -1}; //Not found
   }
   
   public boolean isDone(int index, boolean isRow){
      ArrayList<Integer> clues = isRow ? rowClues.get(index) : colClues.get(index);
      int n = 1; //The block we're looking for
      for(int i=0;i<clues.size();i++){
         int[] result = isRow ? findNthBlock(board[index], n) : findNthBlock(getCol(index), n); //The block
         int width = result[1] - result[0] + 1; //Because block includes the end
         if(width != clues.get(i) || result[0] == -1){ //Then the row doesn't match the clues
            return false;
         }
         n++;
      }
      return true;
   }
   
   public void checkAllDone(){
      for(int i=0;i<width;i++){
         if(isDone(i, false))finishCol(i);
      }
      for(int i=0;i<height;i++){
         if(isDone(i, true))finishRow(i);
      }
   }
   
   public void finishCol(int index){ //Called when we can X out the rest
      for(int i=0;i<isColClueDone.get(index).size();i++){
         isColClueDone.get(index).set(i, true);
      }
      for(int i=0;i<height;i++){
         if(board[i][index] == 0){
            setValue(i, index, -1);
         }
      }
   }
   
   public void finishRow(int index){
      for(int i=0;i<isColClueDone.get(index).size();i++){
         isColClueDone.get(index).set(i, true);
      }
      for(int i=0;i<width;i++){
         if(board[index][i] == 0){
            setValue(index, i, -1);
         }
      }
   }
   
   private int maximum(ArrayList<Integer> nums){
      int ans = -100;
      for(int i=0;i<nums.size();i++){
         if(nums.get(i)>ans){
            ans = nums.get(i);
         }
      }
      return ans;
   }
   
   private int firstX(int[] nums, int start){ //returns the index of first value of -1 after the given index in the given array
      for(int i=start;i<nums.length;i++){
         if(nums[i] == -1)return i;
      }
      return -1;   
   }
      
   
   public void preventBigBlock(int index, boolean isRow){ //Checks if there are two blocks adjacent that would create one too big
      ArrayList<Integer> clues = isRow ? rowClues.get(index) : colClues.get(index);
      int[] row = isRow ? board[index] : getCol(index);
      int biggestClue = maximum(clues);
      int n = 1; //Current block we're finding
      while(findNthBlock(row, n+1)[0] != -1){ //Check to make sure there is still a pair
         int[] res1 = findNthBlock(row, n);
         int width1 = res1[1] - res1[0] + 1;
         int[] res2 = findNthBlock(row, n+1);
         int width2 = res2[1] - res2[1] + 1;
         if(res2[0] - res1[1] == 2 && width1+width2 >= biggestClue){ //Gap of exactly one between blocks but would be too big
            setValue(index, res2[0]-1, -1); //Mark it out
         }
         n++;
      }
   }
   
   
   public void preventBigBlocks(){
      for(int i=0;i<height;i++){
         preventBigBlock(i, true);
      }
      for(int i=0;i<width;i++){
         preventBigBlock(i, false);
      }
   }
   
   public void solve(){
      display();
      basicParse();
      display();
      checkAllDone();
      display();
      preventBigBlocks();
      display();
   }
      
      
   
} //END OF CLASS
