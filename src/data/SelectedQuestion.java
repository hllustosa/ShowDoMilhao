
package data;

import java.util.ArrayList;

public class SelectedQuestion {

    private static final String ALTERNATIVES[] = {" ", "A", "B", "C", "D", "E"};
    private int id;
    private String file;
    private String question;
    private ArrayList<String> alternatives = new ArrayList<>();
    private Integer answer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(ArrayList<String> alternatives) {
        this.alternatives = alternatives;
    }

    public Integer getAnswer() {
        return answer;
    }
    
    public String getStringAnswer(){
        return ALTERNATIVES[answer];
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
    
    public String toString()
    {
        String[] letter = {"A", "B", "C", "D", "E"};
        String string = this.question;
        string +="\n\n";
       
        for(int i = 0; i < alternatives.size(); i++)
        {
            string += letter[i]+" - "+alternatives.get(i)+"\n\n";
        }
            
        return string;
    }
}
