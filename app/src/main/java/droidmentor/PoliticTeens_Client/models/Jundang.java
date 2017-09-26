package droidmentor.PoliticTeens_Client.models;

/**
 * Created by User on 2017-09-16.
 */

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

public class Jundang {
    public String just_name;
    public String just_person_name;
    public String just_category;
    public String just_idea;
    public String just_color;
    public int level;
    public int partisian;

    public Jundang(){
    }

    public Jundang(String just_name,String just_person_name, String just_category, String just_idea,String just_color){
        this.just_name=just_name;
        this.just_person_name = just_person_name;
        this.just_category=just_category;
        this.just_idea=just_idea;
        this.just_color = just_color;
        level=1;
        partisian=1;

    }

    @Exclude
    public Map<String,Object> toMap()
    {
        HashMap<String,Object> result=new HashMap<>();
        result.put("category",just_category);
        result.put("name",just_name);
        result.put("name",just_person_name);
        result.put("idea",just_idea);
        result.put("color",just_color);
        result.put("level",level);
        result.put("partisian",partisian);
        return result;
    }
}

