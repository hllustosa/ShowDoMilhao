package data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Answer {
    
    private SelectedQuestion question;
    private LocalDateTime date;
    private ArrayList<String> students;
    private HashMap<String, Character> alternatives;

    public Answer()
    {
        students = new ArrayList<>();
        alternatives = new HashMap<>();
    }
    
    public SelectedQuestion getQuestion() {
        return question;
    }

    public void setQuestion(SelectedQuestion question) {
        this.question = question;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public ArrayList<String> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<String> students) {
        this.students = students;
    }

    public HashMap<String, Character> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(HashMap<String, Character> alternatives) {
        this.alternatives = alternatives;
    }
    
    public String toString()
    {
        String string = question.getFile();
        for(Entry<String, Character> entry : alternatives.entrySet())
        {
            string += entry.getKey()+"=["+entry.getValue()+"] ";
        }
        return string;
    }
}
