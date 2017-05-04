import sys, random

class Nonogram(object):

    def __init__(self, row_clues, col_clues):
        self.rows = row_clues
        self.cols = col_clues
        self.magic_num_rows = []
        self.magic_num_columns = []
        for col in self.cols:
            self.magic_num_columns.append(len(self.rows) - sum(col) - len(col) + 1)

        for row in self.rows:
            self.magic_num_rows.append(len(self.cols) - sum(row) - len(row) + 1)

        temp = [0]*len(self.cols)
        self.board = []
        for i in range(len(self.rows)):
            self.board.append(temp[:]) #We want a clone of it, not a connected copy


    def display(self):
        row_buffers = [sum([len(str(j)) for j in self.rows[i]])+len(self.rows[i])-1 for i in range(len(self.rows))] #Length of the clues, horizontal
        row_max = max(row_buffers)
        col_buffers = [sum([len(str(j)) for j in self.cols[i]])+len(self.cols[i])-1 for i in range(len(self.cols))] #Length of the clues, vertical
        col_strings = [" ".join([str(j) for j in self.cols[i][::-1]]) for i in range(len(self.cols))]
        print col_buffers
        col_max = max(col_buffers)

        disp_col_clues = [""]*len(self.cols) #Each element is the string for a given column
        for i in range(col_max):
            for index, col in enumerate(col_buffers):
                if col_max-col > i:
                    disp_col_clues[index] += " "
                else:
                    disp_col_clues[index] += col_strings[index][col_max-i-1]

        disp_col_clues = [i[::-1] for i in disp_col_clues]

        disp_col_clues = [list(i) for i in disp_col_clues] #Makes it a char matrix
        for i in range(row_max+1):
            disp_col_clues.insert(0, list(" "*col_max))

        disp_col_clues = zip(*zip(*zip(*disp_col_clues[::-1])[::-1])[::-1])

        print "\n".join(["".join(i) for i in disp_col_clues])
        print "" #Adds some buffer in displaying the field

        for index, num in enumerate(row_buffers): # This prints out the row clues and the board
            print" "*(row_max-num)+" ".join([str(i)for i in self.rows[index]])+" "+''.join([" "if j==0else"#"if j==1else"X"for j in self.board[index]])

    def set_value(self, row, column, value):
        self.board[row][column] = value

    def basic_parse_row(self, row):
        magic = self.magic_num_rows[row]
        counter = 0
        for index, clue in enumerate(self.rows[row]):
            if clue < magic:
                counter += clue
                counter += 1
                continue
            else:
                counter += magic
                for i in range(clue-magic):
                    if self.board[row][counter] == -1:
                        print "OVERRIDING NEGATIVE"
                        counter += 1
                        continue
                    self.set_value(row, counter, 1)
                    counter += 1
                if magic == 0 and counter != len(self.board)-1:
                    if self.board[row][counter] == 1:
                        print "OVERRIDING POSITIVE"
                        counter += 1
                        continue
                    self.set_value(row, counter, -1)
                counter += 1

    def basic_parse_all_row(self):
        for row in range(len(self.board)):
            self.basic_parse_row(row)

    def basic_parse_col(self, col):
        magic = self.magic_num_columns[col]
        counter = 0
        for index, clue in enumerate(self.cols[col]):
            if clue < magic:
                counter += clue
                counter += 1
                continue
            else:
                counter += magic
                for i in range(clue-magic):
                    if self.board[counter][col] == -1:
                        print "OVERRIDING NEGATIVE"
                        counter += 1
                        continue
                    self.set_value(counter, col, 1)
                    counter += 1
                if magic == 0 and counter <= len(self.board[0])-1: #Make sure we're not at the end of the thing
                    print counter
                    if self.board[counter][col] == 1:
                        print "OVERRIDING POSITIVE"
                        counter += 1
                        continue
                    self.set_value(counter, col, -1)
                counter += 1

    def basic_parse_all_cols(self):
        for col in range(len(self.board[0])):
            self.basic_parse_col(col)


def main():
    height = int(raw_input("What is the height of the puzzle?\n"))
    width = int(raw_input("What is the width of the puzzle?\n"))
    rows = []
    cols = []
    for i in range(height):
        rows.append([int(i) for i in raw_input("Type in the number for the row separated by spaces\n").split()])

    if any([sum(i)+len(i)-1>width for i in rows]):
        print "Error: Row is too big"
        sys.exit(0)

    for i in range(width):
        cols.append([int(i) for i in raw_input("Type in the number for the column separate by spaces\n").split()])

    if any([sum(i)+len(i)-1>height for i in cols]):
        print "Error: Column is too big"
        sys.exit(0)

    nono = Nonogram(rows, cols)
    nono.display()
    nono.basic_parse_all_row()
    nono.display()
    nono.basic_parse_all_cols()
    nono.display()


if __name__ == '__main__':
    main()
