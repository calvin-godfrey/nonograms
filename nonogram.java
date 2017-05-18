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
      System.out.println();
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
                  System.out.print("_");
               } else {
                  System.out.print(" ");
               }
            }
         }
         System.out.println();
      }      
   }
   
   public void setValue(int row, int col, int value){
      if(row<0)System.out.println("COL: " + "\t" + col + "\t" + value);
      if(col<0)System.out.println("ROW: " + "\t" + row + "\t" + value);
      board[row][col] = value;
   }
   
   public void basicParse(int row, int start, int end, boolean isRow){ //isrow true for row, false for column
                                                                       //Use start/end if they are already done
      int magic; //The number of uncertain cells in each clue block
      int sum = 0;
      ArrayList<Integer> clues = isRow ? rowClues.get(row) : colClues.get(row);
      ArrayList<Boolean> completed = isRow ? isRowClueDone.get(row) : isColClueDone.get(row);
      int startIndex = 0;
      int endIndex = completed.size();
      boolean foundStart = false;
      boolean foundEnd = false;
      for(int i=0;i<clues.size();i++){
         if(completed.get(i))continue;
         if(!completed.get(i)&&!foundStart){
            startIndex = i;
            foundStart = true;
         }
         if(completed.get(i)&&foundStart&&!foundEnd){
            endIndex = i;
            foundEnd = true;
         }
         sum += clues.get(i);
         sum++;
      }
      sum--; //We only account for spaces, so we subtract one
      magic = (end-start)-sum;
      //Loop through the clues and only mark cells if we are certain about them
      int counter = start; //Where in the row we are
      for(int i=startIndex;i<endIndex&&counter<=end;i++){
         if(completed.get(i) == true)continue;
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
         ArrayList<Boolean> cluesDone = isRowClueDone.get(r);
         int start = 0;
         int end = width;
         for(int i=0;i<cluesDone.size();i++){ //This part doesn't work when the row/column is already complete but that is OK
            if(cluesDone.get(i)){
               start=i==0?start+findNthBlock(board[r], i+1)[0] : start+(findNthBlock(board[r], i+1)[0]-findNthBlock(board[r], i)[1]-1);
               //Above accounts for the gap before a clue
               start += rowClues.get(r).get(i); //Space of the clue
               if(i!=cluesDone.size()-1&&!cluesDone.get(i+1))start++; //Space one after the clue, only if the next one isn't
            } else {
               break;
            }
         }
         
         for(int i=cluesDone.size()-1;i>=0;i--){
            if(cluesDone.get(i)){
               end = i==cluesDone.size()-1?end-(width-findNthBlock(board[r], findMaxN(board[r]))[1]):end-(findNthBlock(board[r], findMaxN(board[r])-i)[0]-findNthBlock(board[r], findMaxN(board[r])-i-1)[1]-1);
               end -= rowClues.get(r).get(i);
               if(i!=0&&!cluesDone.get(i-1))end--;
            } else {
               break;
            }
         }
         if(end==width){
            basicParse(r, start, end, true);
         } else {
            basicParse(r, start, end+1, true);
         }
      }
      displayCompleted();
      for(int c=0;c<width;c++){
         ArrayList<Boolean> cluesDone = isColClueDone.get(c);
         int start = 0;
         int end = height;
         for(int i=0;i<cluesDone.size();i++){
            if(cluesDone.get(i)){
               start = i==0?start+findNthBlock(getCol(c), i+1)[0]:start+(findNthBlock(getCol(c), i+1)[0]-findNthBlock(getCol(c), i)[1]-1);
               start += colClues.get(c).get(i);
               if(i!=cluesDone.size()-1&&!cluesDone.get(i+1))start++;
            } else {
               break; //Then we can't narrow down the start any more
            }
         }
         for(int i=cluesDone.size()-1;i>0;i--){
            if(cluesDone.get(i)){
               if(i==cluesDone.size()-1){
                  end -= height-findNthBlock(getCol(c), findMaxN(getCol(c)))[1];
               } else {
                  int var1 = findNthBlock(getCol(c), findMaxN(getCol(c))-(cluesDone.size()-1-i))[0];
                  int[] block2 = findNthBlock(getCol(c), findMaxN(getCol(c))-(cluesDone.size()-1-i)-1);
                  int var2 = block2[0]-1;
                  if(var2<0||block2[1]-block2[0]+1!=colClues.get(c).get(i-1)){
                     break;
                  }
                  boolean shouldBreak = false;
                  for(int k=findNthBlock(getCol(c), findMaxN(getCol(c))-(cluesDone.size()-1-i)-1)[1]+1;k<findNthBlock(getCol(c), findMaxN(getCol(c))-(cluesDone.size()-1-i))[1];k++){
                     if(getCol(c)[k] != -1)shouldBreak = true;
                  }
                  if(shouldBreak)break;
                  end -= var1-var2;
               }
               end -= colClues.get(c).get(i);
               if(i!=0&&!cluesDone.get(i-1))end--;
            } else {
               break;
            }
         }
         if(end==height){
            basicParse(c, start, end, false);
         } else {
            System.out.println("FINALLY\t" + end);
            basicParse(c, start, end+1, false);
         }
      }
   }
   
   private int[] findNthBlock(int[] set, int n){ //Returns [start, end] index of the nth block of filled in squares in a row
                                                   //1-indexed! Not 0
      if(n<1)return new int[]{-1, -1};
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
      if(inBlock && curr == n)return new int[]{currStart, set.length-1};
      return new int[]{-1, -1}; //Not found
   }
   
   private int findMaxN(int[] row){ //Basically the number of blocks in a row
      int n=1;
      while(findNthBlock(row, n)[0] != -1){
         n++;
      }
      return n-1;
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
      for(int i=0;i<isRowClueDone.get(index).size();i++){
         isRowClueDone.get(index).set(i, true);
      }
      for(int i=0;i<width;i++){
         if(board[index][i] == 0){
            setValue(index, i, -1);
         }
      }
   }
   
   private int maximum(ArrayList<Integer> nums, ArrayList<Boolean> complete){
      /*Returns the biggest clue in the row/column not yet completed*/
      int ans = -100;
      for(int i=0;i<nums.size();i++){
         if(nums.get(i)>ans&&!complete.get(i)){
            ans = nums.get(i);
         }
      }
      return ans;
   }
   
   private int[] findNthX(int[] nums, int start, int n){ //returns an array in the same format as getNthBlock
      int ans=1;
      int startIndex = -1;
      int endIndex = -1;
      boolean inX = false;
      for(int i=start;i<nums.length;i++){
         if(nums[i] == -1&&!inX){
            if(ans == n)startIndex = i;
            ans++;
            inX = true;
         }
         if(nums[i] != -1&&inX){
            inX = false;
            if(ans-1 == n)return new int[]{startIndex, i-1};
         }
      }
      if(startIndex != -1)return new int[]{startIndex, nums.length-1};
      return new int[]{-1, -1};   
   }
   
   private int[] findLastX(int[] nums, int start){
      int n = 1;
      while(findNthX(nums, start, n)[0] != -1)n++;
      return findNthX(nums, start, n-1);
   }
      
   
   public void preventBigBlock(int index, boolean isRow){ //Checks if there are two blocks adjacent that would create one too big
      if(isDone(index, isRow))return;
      ArrayList<Integer> clues = isRow ? rowClues.get(index) : colClues.get(index);
      ArrayList<Boolean> complete = isRow? isRowClueDone.get(index) : isColClueDone.get(index);
      int[] row = isRow ? board[index] : getCol(index);
      int biggestClue = maximum(clues, complete);
      int n = 1; //Current block we're finding
      while(findNthBlock(row, n+1)[0] != -1){ //Check to make sure there is still a pair
         int[] res1 = findNthBlock(row, n);
         int width1 = res1[1] - res1[0] + 1;
         int[] res2 = findNthBlock(row, n+1);
         int width2 = res2[1] - res2[1] + 1;
         if(res2[0] - res1[1] == 2 && width1+width2 >= biggestClue){ //Gap of exactly one between blocks but would be too big
            if(isRow){
               setValue(index, res2[0]-1, -1); //Mark it out
            } else {
               setValue(res2[0]-1, index, -1);
            }
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
   
   private void checkBorderStart(int index, boolean isRow){ //Will check the first clue that is not already complete
      if(isDone(index, isRow))return;
      ArrayList<Integer> clues = isRow ? rowClues.get(index) : colClues.get(index);
      ArrayList<Boolean> complete = isRow ? isRowClueDone.get(index) : isColClueDone.get(index);
      int[] row = isRow ? board[index] : getCol(index);
      int max = isRow ? width : height;
      int firstClue = 0;
      int which = 0;
      for(int i=0;i<clues.size();i++){
         if(!complete.get(i)){
            firstClue = clues.get(i);
            which = i+1;
            break;
         }
      }
      int[] res = findNthBlock(row, which); //Which will always be at least 1
      int start = res[0];
      int end = res[1];
      if(start == -1 || end == -1)return;
      int firstX = findNthX(row, findNthBlock(row, which)[1]+1, 1)[0]; //First X after the first block
      int rowStart = which==1?-1:findNthBlock(row, which-1)[1]+1;
      for(int i=1;i<max-rowStart;i++){
         rowStart++;
         if(row[rowStart]!=-1)break;
      }
      //BEGIN CHECKING THE FIRST BLOCK//
      if(firstX > -1 && firstX < firstClue){ //Case when the firstX means that we can eliminate everything before firstX
         for(int i=0;i<firstX;i++){
            if(isRow){
               setValue(index, i, -1);
            } else {
               setValue(i, index, -1);
            }
         }
         return; //Nothing else to do in that row
      }
      int blockWidth = end - start + 1;
      if(blockWidth == firstClue){ //it matches
         if(start<=firstClue){ //and there is no space before it to insert it
            if(isRow){
               isRowClueDone.get(index).set(0, true); //mark that clue as done
            } else {
               isColClueDone.get(index).set(0, true);
            }
            if(start != 0){
               if(isRow){
                  setValue(index, start-1, -1);
               } else {
                  setValue(start-1, index, -1);
               }
            }
            if(isRow){
               setValue(index, end+1, -1);
            } else {
               setValue(end+1, index, -1);
            }
         }
      }
      //Finally, check to see if you can extend the first block
      if(start-rowStart<firstClue-1){
         for(int i=0;i<firstClue;i++){
            if(i<end-rowStart)continue;
            if(isRow){
               if(board[index][i+rowStart] == -1)break;
               setValue(index, i+rowStart, 1);
            } else {
               if(board[i+rowStart][index] == -1)break;
               setValue(i+rowStart, index, 1);
            }
         }
      }
      int startSecondBlock = findNthBlock(row, which+1)[0];
      if(firstX == -1)firstX = isRow ? width : height;
      int gap = firstX - rowStart;
      if(firstClue+clues.get(which-1)+1<gap)return;
      if(startSecondBlock == -1 || startSecondBlock > firstX){ //Then we can work backwards; only one block
         for(int i=firstX-1, j=0;i>=0&&j<firstClue;i--){
            if(i<end){
               if(isRow){
                  setValue(index, i, 1);
               } else {
                  setValue(i, index, 1);
               }
            }
            j++;
         }
      }
   }
   
   private void checkBorderEnd(int index, boolean isRow){
      if(isDone(index, isRow))return;
      ArrayList<Integer> clues = isRow ? rowClues.get(index) : colClues.get(index);
      int[] row = isRow ? board[index] : getCol(index);
      int lastClue = clues.get(clues.size()-1);
      int[] lastBlock = findNthBlock(row, findMaxN(row));
      int start = lastBlock[0];
      int end = lastBlock[1];
      int max = isRow ? width : height;
            //Find the last X before the last block starts
      int lastX = 0, n = 0;
      for(int i=0;i<height;i++){
         int Xend = findNthX(row, 0, n+1)[1];
         if(Xend > start || Xend == -1)break;
         lastX = Xend;
         n++;
      }
      if(start == -1 || end == -1)return;
      int blockWidth = end - start + 1;
      if(blockWidth == lastClue){
         if(end >= max-blockWidth-1){ //No space to fit it later on in row
            if(isRow){
               isRowClueDone.get(index).set(isRowClueDone.get(index).size()-1, true);
            } else {
               isColClueDone.get(index).set(isColClueDone.get(index).size()-1, true);
            }
            if(end != max-1){
               if(isRow){
                  setValue(index, end+1, -1);
               } else {
                  setValue(end+1, index, -1);
               }
            }
            if(isRow){
               setValue(index, start-1, -1);
            } else {
               setValue(start-1, index, -1);
            }
         }
         return;
      }
      if(end>max-lastClue){
         for(int i=0;i<lastClue;i++){
            if(max-i>start)continue;
            if(isRow){
               if(board[index][max-i-1] == -1)break;
               setValue(index, max-i-1, 1);
            } else {
               if(board[max-i-1][index] == -1)break;
               setValue(max-i-1, index, 1);
            }
         }
      }
      int counter = lastX+1;
      for(int i=0;i<lastClue;i++){
         if(counter+i>=start){
            if(isRow){
               setValue(index, counter+i, 1);
            } else {
               setValue(counter+i, index, 1);
            }
         }
      }
   }
   
   public void checkAllBorder(){
      for(int i=0;i<height;i++){
         checkBorderStart(i, true);
         checkBorderEnd(i, true);
      }
      System.out.println("AFTER CHECK BORDER FOR ROWS");
      display();
      for(int i=0;i<width;i++){
         checkBorderStart(i, false);
         checkBorderEnd(i, false);
      }
   }
   
   private void removeSmallGaps(int index, boolean isRow){
      if(isDone(index, isRow))return;
      ArrayList<Integer> clues = isRow ? rowClues.get(index) : colClues.get(index);
      ArrayList<Boolean> completed = isRow ? isRowClueDone.get(index) : isColClueDone.get(index);
      int[] row = isRow ? board[index] : getCol(index);
      int minimum = 100;
      for(int i=0;i<clues.size();i++){
         if(completed.get(i))continue;
         if(clues.get(i)<minimum)minimum = clues.get(i); //biggest uncompleted clue
      }
      int n = 1;
      int[] currX = findNthX(row, 0, n);
      if(currX[0] == -1 || currX[1] == -1)return;
      while(findNthX(row, 0, n+1)[0] != -1){ //While there is a second block
         int[] nextX = findNthX(row, 0, n+1);
         if(nextX[0] == -1 ||nextX[1] == -1)return;
         int blockWidth = nextX[0]-currX[1]-1;
         if(blockWidth < minimum){
            for(int i=currX[1]+1;i<nextX[0];i++){
               if(row[i] == 1)return; //Can't mark them out if there's a block between X's
            }
            for(int i=currX[1]+1;i<nextX[0];i++){
               if(isRow){
                  setValue(index, i, -1);
               } else {
                  setValue(i, index, -1);
               }
            }
         }
         n++;
         currX = findNthX(row, 0, n);
      }
   }
   
   public void removeSmallGaps(){
      for(int r=0;r<height;r++)removeSmallGaps(r, true);
      for(int c=0;c<width;c++)removeSmallGaps(c, false);
   }
   
   private void surroundMax(int index, boolean isRow){
      if(isDone(index, isRow))return;
      ArrayList<Integer> clues = isRow ? rowClues.get(index) : colClues.get(index);
      ArrayList<Boolean> completed = isRow ? isRowClueDone.get(index) : isColClueDone.get(index);
      int[] row = isRow ? board[index] : getCol(index);
      int max = 0;
      for(int i=0;i<clues.size();i++){
         if(completed.get(i))continue;
         if(clues.get(i) > max)max = clues.get(i);
      } //get the max
      
      for(int n=0;n<findMaxN(row);n++){
         int[] res = findNthBlock(row, n+1);
         int blockWidth = res[1] - res[0] + 1;
         if(blockWidth == max){
            if(isRow){
               if(res[0] != 0)setValue(index, res[0]-1, -1);
               if(res[1] != row.length-1)setValue(index, res[1]+1, -1);
            } else {
               if(res[0] != 0)setValue(res[0]-1, index, -1);
               if(res[1] != row.length-1)setValue(res[1]+1, index, -1);
            }
         }
      }
   }
   
   public void surroundMax(){
      for(int r=0;r<height;r++)surroundMax(r, true);
      for(int c=0;c<width;c++)surroundMax(c, false);
   }
   
   private void markDoneStart(int index, boolean isRow){
      if(isDone(index, isRow))return;
      ArrayList<Integer> clues = isRow ? rowClues.get(index) : colClues.get(index);
      ArrayList<Boolean> completed = isRow ? isRowClueDone.get(index) : isColClueDone.get(index);
      int[] row = isRow ? board[index] : getCol(index);
      int clue1 = clues.get(0);
      int[] block1 = findNthBlock(row, 1);
      int tempWidth = block1[1]-block1[0]+1;
      if(tempWidth!=clue1)return;
      boolean found = false;
      for(int i=0;i<clue1&&i<block1[0];i++){
         if(row[i] != -1)found = true;
      }
      if(!found){
         completed.set(0, true);
      } else {
         return;
      }
      for(int i=0;i<clues.size()-1;i++){
         clue1 = clues.get(i);
         int clue2 = clues.get(i+1);
         block1 = findNthBlock(row, i+1);
         int width1 = block1[1]-block1[0]+1;
         int[] block2 = findNthBlock(row, i+2);
         int width2 = block2[1]-block2[0]+1;
         if(width1!=clue1||width2!=clue2||block1[1]==-1||block2[1]==-1)return;
         found = false;
         for(int j=block1[1]+1;j<block2[0];j++){
            if(row[j] != -1)found = true;
         }
         if(!found){
            completed.set(i+1, true);
         } else {
            return;
         }
      }
   }
   
   private void markDoneEnd(int index, boolean isRow){
      if(isDone(index, isRow))return;
      ArrayList<Integer> clues = isRow ? rowClues.get(index) : colClues.get(index);
      ArrayList<Boolean> completed = isRow ? isRowClueDone.get(index) : isColClueDone.get(index);
      int[] row = isRow ? board[index] : getCol(index);
      int max = isRow ? width : height;
      int clue1 = clues.get(clues.size()-1);
      int[] block1 = findNthBlock(row, findMaxN(row));
      int tempWidth = block1[1]-block1[0]+1;
      if(tempWidth != clue1)return;
      boolean found = false;
      for(int i=max-1;i>max-i-clue1&&i>block1[1];i--){
         if(row[i] != -1)found = true;
      }
      if(!found){
         if(isRow&&index==0)return;
         completed.set(completed.size()-1, true);
      } else {
         return;
      }
      for(int i=clues.size()-1;i>0;i--){
         clue1 = clues.get(i);
         block1 = findNthBlock(row, findMaxN(row)-(clues.size()-1-i));
         int[] block2 = findNthBlock(row, findMaxN(row)-(clues.size()-i));
         int clue2 = clues.get(i-1);
         int width1 = block1[1] - block1[0] + 1;
         int width2 = block2[1] - block2[0] + 1;
         if(width1!=clue1||width2!=clue2||block1[1]==-1||block2[1]==-1)return;
         found = false;
         for(int j=block2[1]+1;j<block1[0];j++){
            if(row[j]!=-1)found=true;
         }
         if(!found){
            completed.set(i-1, true);
         } else {
            return;
         }
      }
   }
   
   public void markAllDone(){
      for(int r=0;r<height;r++){
         markDoneStart(r, true);
         markDoneEnd(r, true);
      }
      for(int c=0;c<width;c++){
         markDoneStart(c, false);
         markDoneEnd(c, false);
     }
   }
   
   private void displayCompleted(){
      System.out.println("ROWS:");
      for(int r=0;r<height;r++)System.out.println(isRowClueDone.get(r));
      System.out.println("COLS");
      for(int c=0;c<width;c++)System.out.println(isColClueDone.get(c));
   }
      
   public void solve(){
      boolean done = false;
      while(!done){
         display();
         int[][] temp = new int[height][width];
         for(int r=0;r<height;r++){
            for(int c=0;c<width;c++){
               temp[r][c] = board[r][c]; //create copy of the board
            }
         }
         basicParse();
         System.out.println("AFTER BASIC PARSE");
         display();
         checkAllDone();
         System.out.println("AFTER CHECK ALL DONE");
         display();
         preventBigBlocks();
         System.out.println("AFTER PREVENT BIG BLOCKS");
         display();
         removeSmallGaps();
         System.out.println("AFTER REMOVE SMALL GAPS");
         display();
         checkAllBorder();
         System.out.println("AFTER CHECK ALL BORDER");
         display();
         surroundMax();
         System.out.println("AFTER SURROUND MAX");
         display();
         markAllDone(); //Doesn't really affect the board, so no display
         boolean end = true;
         for(int r=0;r<height;r++){
            for(int c=0;c<width;c++){
               if(temp[r][c]!=board[r][c]){
                  end = false;
               }
            }
         }
         if(end)done = true;
      }
      display();
      displayCompleted();
   }   
} //END OF CLASS
