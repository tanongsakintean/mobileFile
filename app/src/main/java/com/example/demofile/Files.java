package com.example.demofile;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Files {

    File myExternalFile;
    final String FILENAME = "data.txt";
    final String FILEPATH = "/mystorage";
    private static final String TAG = "Files";
    private String[][] data;

    public Files(Context applicationContext) throws IOException {
        // make file
        ContextWrapper contextWrapper = new ContextWrapper(applicationContext);
        //  myExternalFile  path file
        myExternalFile = new File(contextWrapper.getExternalFilesDir(FILEPATH), FILENAME);
        if (this.selectAll().length != 0) {
            this.selectAll();
        }
      // update("1","1,hiiiii,beer");
      //  delete("1");
    }

    public int lenRow() throws FileNotFoundException {
        FileInputStream fileIn = new FileInputStream(myExternalFile);
        DataInputStream in = new DataInputStream(fileIn);
        InputStreamReader inputRead = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputRead);
        int line = 0;
        try {
            while (( bufferedReader.readLine()) != null) {
                line++;
            }
        } catch (Exception error) {
            Log.e(TAG, "Error inserting data: " + error.getMessage());
        }
        return line;
    }


    public int lenCol() throws FileNotFoundException {
        FileInputStream fileIn = new FileInputStream(myExternalFile);
        DataInputStream in = new DataInputStream(fileIn);
        InputStreamReader inputRead = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputRead);
        try {
          return  bufferedReader.readLine().toString().split(",").length ;
        } catch (Exception error) {
            Log.e(TAG, "Error inserting data: " + error.getMessage());
        }
        return 0;
    }

    public void insert(String[] data) {
        try {
            FileOutputStream out = new FileOutputStream(myExternalFile, true);
            String row = "";
            String id = String.valueOf(lenRow()+1);
            for (String item : data) {
                row+=item + ",";
            }
            out.write((id+","+row).getBytes());
            out.write("\n".getBytes());
            out.close();
            selectAll();
        } catch (IOException e) {
            Log.e(TAG, "Error inserting data: " + e.getMessage());
        }
    }

    public String[][] selectAll() throws IOException {

        try{
            data = new String[lenRow()][lenCol()];
            String text = "";
            FileInputStream fileIn = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fileIn);
            InputStreamReader inputRead = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputRead);

            int idxRow = 0;
            while (( text =  bufferedReader.readLine()) != null) {
                int idxCol = 0;
                for (String item:text.split(",")) {
                    data[idxRow][idxCol] = item;
                    idxCol++;
                }
                idxRow++;
            }
            return data;
        }catch (Exception e){
           return new String[][]{new String[]{"", ""}};
        }

    }

    public String select(String id){
        String text = "";
        for (int i = 0; i < data.length ; i++) {
                if (data[i][0].toString().equals(id)) {
                    for (String item: data[i]) {
                       text+=item+",";
                    }
                }
        }
        return text;
    }

    public void update(String id,String updateData){
       int index = Integer.parseInt(select(id).split(",")[0]) - 1;
       String[] value = updateData.split(",");
        data[index][1] = value[1];
       data[index][2] = value[2];
        refresh();
    }

    public void refresh(){
        try {
            FileOutputStream out = new FileOutputStream(myExternalFile);
            for (int i = 0; i <data.length ; i++) {
                if (!data[i][0].toString().equals("0")) {
                    out.write(select(data[i][0].toString()).getBytes());
                    out.write("\n".getBytes());
                }
            }
            out.close();
        } catch (IOException e) {
            Log.e(TAG, "Error inserting data: " + e.getMessage());
        }
    }

    public void delete(String id){
        int index = Integer.parseInt(select(id).split(",")[0]) - 1;
        data[index][0] = "0";
        refresh();
    }



    public File getMyExternalFile() {
        return myExternalFile;
    }

}

