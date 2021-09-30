package duke;

import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;
import java.io.FileWriter;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.Scanner;

public class Storage {

    private static String filepath;

    public Storage(String filePath) throws IOException{
        this.filepath = filePath;
        //create duke.txt in folder named 'data'
        //idea from https://stackoverflow.com/questions/2833853/create-whole-path-automatically-when-writing-to-a-new-file
        //author: cdmihai
        Path pathToFile = Paths.get(filepath);
        Files.createDirectories(pathToFile.getParent());
        try{
            Files.createFile(pathToFile);
        } catch (FileAlreadyExistsException e){
            Ui.fileAlreadyExist();
        }
    }

    //@@author {siyuancheng178}-reused
    //{Some Load usage. with minor modifications}
    public static TaskList loadFileContents() throws FileNotFoundException {
        File f = new File(filepath); // create a File for the given file path
        Scanner s = new Scanner(f);
        TaskList tasks = new TaskList();
        while (s.hasNext()) {
            String[] split = s.nextLine().split("]");
            //split [D][X] return book (by: Sunday)
            //split[0] = [D
            //split[1] = [X
            //split[2] =  return book (by: Sunday)
            //description: return book /by Sunday
            String description = split[2].strip().replace('(','/').replace(')',':').replaceAll(":","");
            switch (split[0].charAt(1)){
            case 'T':
                Task t = new Task(split[2].trim(),split[1].charAt(1)=='X');
                tasks.addTask(t);
                break;
            case 'D':
                String[] splitBy = description.split("/by");
                Task d = new Deadline(splitBy[0].trim(),splitBy[1].trim(),split[1].charAt(1)=='X');
                tasks.addTask(d);
                break;
            case 'E':
                String[] splitAt = description.split("/at");
                Task e = new Event(splitAt[0].trim(),splitAt[1].trim(),split[1].charAt(1)=='X');
                tasks.addTask(e);
                break;
            default:
                break;
            }
        }
        s.close();
        return tasks;
    }

    public static void writeToFile(TaskList tasks) throws IOException {

        FileWriter fw = new FileWriter(filepath);
        fw.write(tasks.save());
        fw.close();
    }

}
