package com.example.final_project.utils;

import java.util.ArrayList;
import java.util.List;

public class GlobalConstants {
    public static final List<String> accountTypes = new ArrayList<String>(){{
        add("Seeker");
        add("Company");
    }};
    public static final List<String> experienceLevels = new ArrayList<String>(){{
        add("No Experience Required");
        add("Under 1 Year");
        add("1-3 Years");
        add("3-5 Years");
        add("5-10 Years");
        add("10+ Years");
    }};

    public static final List<String> salaryRanges =new ArrayList<String>(){{
        add("Not Specified");
        add("Under $20,000");
        add("$20,000 - $40,000");
        add("$40,000 - $60,000");
        add("$60,000 - $80,000");
        add("$80,000 - $100,000");
        add("$100,000+");
    }};

    public static final List<String> genders = new ArrayList<String>(){{
        add("Not Specified");
        add("Male");
        add("Female");
    }};
    public static final String defaultCompanyLogoUrl = "https://i.pinimg.com/originals/ec/d9/c2/ecd9c2e8ed0dbbc96ac472a965e4afda.jpg";
    public static final String defaultUserImageUrl = "https://firebasestorage.googleapis.com/v0/b/final-project-d559e.appspot.com/o/icons8-person-80.png?alt=media";
}
