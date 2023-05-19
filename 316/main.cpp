/*This is Project two for CSCI316
Programmer @Andreea Ibanescu
*/
#include <iostream>
#include <fstream>
#include <string>
#include <sstream>

int main() {
  std::ifstream file("text.txt"); // open the file
  std::string line; //Var to store each part of the line from the text file
  std::ofstream outfile("ArrayOutput.txt"); // open the output file


  if (file.is_open()&& outfile.is_open()) { // check if the file is open
    while (std::getline(file, line)) { // read each line of 
      std::cout << "Input: "<< line << std::endl;
      std::istringstream iss(line);
      outfile <<"Input: "<< line << std::endl;
      
      int row, col; //Declare Vars to store the row/col of each line
      
      iss >> row >> col; //reads the first two integers from the input stream
      std::cout<<"Row: "<<row<<" Col: " <<col<< std::endl;  
      outfile << "Row: "<<row<<" Col: " <<col<< std::endl;  


      int size = row * col; //Finding the dimensions of the matrix 

      int* arr_row = new int[size]; //Dynamic allocation for row & col major order matrix
      int* arr_col = new int[size]; 
      
      int num; //a temporary variable is to read the integers from the input stream one at a time and store them in the array 
      int count = 0; //Use this to count total numbers in the input 

      while (iss >> num) {
          arr_row[count++] = num;
      }
      //Checking case if there are enough elements
      if (count != size) {
        std::cout << "Error: Insufficient elements in the line" << std::endl<< std::endl;
        outfile << "Error: Insufficient elements in the line" << std::endl<< std::endl;
        delete[] arr_row;
        delete[] arr_col;
      }
        //Continue analyzing the code becasue there are enough elements so no case is broken yet
         //"arr_row" and "arr_col" are simply variables that hold memory addresses
          //declared as "int*" types, point to int value stored in memory

        else {

          //pointer arithmetic
       for (int j = 0; j < col; j++) {
             for (int i = 0; i < row; i++) {
               *(arr_col + (i * col + j) ) = *(arr_row + (j * row + i) ); 
             }
         }
          //pointers are necessary b/c there is a swap occuring and it wouldn't be done as easily if we weren't swapping the exact element from its adress value
        // "i * col + j" is used to compute the linear index of the element at position (i, j) in the "arr_col" array  
      // "j * row + i" is used to compute the linear index of the element at position (j, i) 
      //then you swap the indices from row-major order to column-major order
          // takes the element from the row-major order matrix, computes its linear index in the column-major order matrix

        //Printing out the format based on the dimension given to us  
        //keeping track of the elements from the 1 day array   

          
        int indexRow=0;
        std::cout << "Row-major order:" << std::endl;
        outfile << "Row-major order:" << std::endl;
        for (int i = 0; i < row; i++) {
          for(int j=0; j<col; j++){
          std::cout << arr_row[indexRow] << " ";
          outfile << arr_row[indexRow] << " ";
            indexRow++;
         }
        std::cout << std::endl;
        }
        int indexCol=0;
        std::cout << "Column-major order:" << std::endl;
        outfile << "Column-major order:" << std::endl;
        for (int i = 0; i < row; i++) {
          for(int j=0; j< col; j++){
            std::cout << arr_col[indexCol] << " ";
            outfile  << arr_col[indexCol] << " ";
            indexCol++;
          }
           std::cout << std::endl;
           outfile  <<std::endl;
        }
        std::cout << std::endl << std::endl;
        outfile  << std::endl << std::endl;

          //Releasing the memory that was previously allocated when I created new arr_row & arr_col
        //This is an advantage for many reasons such as preventing any memory leaks ect

        delete[] arr_row;
        delete[] arr_col;
      }
    }
    //The file is read and anylzed fully so the file may be closed now
    file.close();
  } 
  else {
    //Coding for if the file is unable to open
    std::cout << "Unable to open file." << std::endl;
    outfile  << "Unable to open file." << std::endl;
  }
    //Ending the C++ program
  return 0;
}