package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Class {
    
    private String name;
    private String questionsDatabase;
    private ArrayList<String> students = new ArrayList<>();
    private ArrayList<String> livePool = new ArrayList<>();
    private ArrayList<String> deadPool = new ArrayList<>();
    private HashMap<Integer, Question> questions = new HashMap<>(); 
    private HashMap<Integer, Answer> answers = new HashMap<>(); 
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestionsDatabase() {
        return questionsDatabase;
    }

    public void setQuestionsDatabase(String questionsDatabase) {
        this.questionsDatabase = questionsDatabase;
    }
    
    public ArrayList<String> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<String> students) {
        this.students = students;
    }

    public ArrayList<String> getLivePool() {
        return livePool;
    }

    public void setLivePool(ArrayList<String> livePool) {
        this.livePool = livePool;
    }

    public ArrayList<String> getDeadPool() {
        return deadPool;
    }

    public void setDeadPool(ArrayList<String> deadPool) {
        this.deadPool = deadPool;
    }

    public  HashMap<Integer, Question> getQuestions() {
        return questions;
    }

    public void setQuestions(HashMap<Integer, Question> questions) {
        this.questions = questions;
    }

    public HashMap<Integer, Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(HashMap<Integer, Answer> answers) {
        this.answers = answers;
    }
    
    public void sortStudents()
    {
        Collections.sort(students);
        Collections.sort(livePool);
        Collections.sort(deadPool);
    }
    
    public void clearStudents(){
        students.clear();
        livePool.clear();
        deadPool.clear();
    }
            
    
}
